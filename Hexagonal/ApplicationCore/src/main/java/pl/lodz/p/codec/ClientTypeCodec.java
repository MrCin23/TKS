package pl.lodz.p.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.model.user.Premium;
import pl.lodz.p.model.user.ClientType;
import pl.lodz.p.model.user.Standard;

public class ClientTypeCodec implements Codec<ClientType> {
    private final CodecRegistry codecRegistry;

    public ClientTypeCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public ClientType decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        String type = bsonReader.readString();
        int maxRentedMachines = bsonReader.readInt32();
        String clientType = bsonReader.readString();
        bsonReader.readEndDocument();

        if(type.equals("standard")) {
            return new Standard();
        }
        else if(type.equals("premium")) {
            return new Premium();
        }
        else {
            throw new RuntimeException("Unsupported type: " + type);
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, ClientType clientType, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_clazz", clientType.getClass().getSimpleName().toLowerCase());
        bsonWriter.writeInt32("maxRentedMachines" ,clientType.getMaxRentedMachines());
        bsonWriter.writeString("name", clientType.getName());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<ClientType> getEncoderClass() {
        return ClientType.class;
    }
}
