package pl.lodz.p.repo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.repo.client.data.ClientTypeEnt;
import pl.lodz.p.repo.client.data.PremiumEnt;
import pl.lodz.p.repo.client.data.StandardEnt;

public class ClientTypeCodec implements Codec<ClientTypeEnt> {
    private final CodecRegistry codecRegistry;

    public ClientTypeCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public ClientTypeEnt decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        String type = bsonReader.readString();
        int maxRentedMachines = bsonReader.readInt32();
        String clientType = bsonReader.readString();
        bsonReader.readEndDocument();

        if(type.equals("standardent")) {
            return new StandardEnt();
        }
        else if(type.equals("premiument")) {
            return new PremiumEnt();
        }
        else {
            throw new RuntimeException("Unsupported type: " + type);
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, ClientTypeEnt clientType, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_clazz", clientType.getClass().getSimpleName().toLowerCase());
        bsonWriter.writeInt32("maxRentedMachines" ,clientType.getMaxRentedMachines());
        bsonWriter.writeString("name", clientType.getName());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<ClientTypeEnt> getEncoderClass() {
        return ClientTypeEnt.class;
    }
}
