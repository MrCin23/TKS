package pl.lodz.p.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.model.AppleArch;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.VMachine;
import pl.lodz.p.model.x86;

import java.util.UUID;

public class VMachineCodec implements Codec<VMachine> {
    private final CodecRegistry codecRegistry;

    public VMachineCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public VMachine decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        MongoUUID uuid = new MongoUUID(UUID.fromString(bsonReader.readString()));
        String type = bsonReader.readString("_clazz");
        int CPUNumber = bsonReader.readInt32("CPUNumber");
        String ramSize = bsonReader.readString("ramSize");
        int isRented = bsonReader.readInt32("isRented");
        float actualRentalPrice = (float) bsonReader.readDouble("actualRentalPrice");
        String CPUManufacturer = "";
        if(type.equals("x86")) {
            CPUManufacturer = bsonReader.readString("CPUManufacturer");
        }
        bsonReader.readEndDocument();
        if(type.equals("x86")) {
            return new x86(uuid, CPUNumber, ramSize, isRented, CPUManufacturer);
        }
        else if(type.equals("applearch")) {
            return new AppleArch(uuid, CPUNumber, ramSize, isRented);
        }
        else {
            throw new RuntimeException("Unknown VMachine type: " + type);
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, VMachine vMachine, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_id", vMachine.getEntityId().toString());
        bsonWriter.writeString("_clazz", vMachine.getClass().getSimpleName().toLowerCase());
        bsonWriter.writeInt32("CPUNumber", vMachine.getCPUNumber());
        bsonWriter.writeString("ramSize", vMachine.getRamSize());
        bsonWriter.writeInt32("isRented", vMachine.isRented());
        bsonWriter.writeDouble("actualRentalPrice", vMachine.getActualRentalPrice());
        if(vMachine.getClass().getSimpleName().equals("x86")) {
            x86 pom = (x86) vMachine;
            bsonWriter.writeString("CPUManufacturer", pom.getCPUManufacturer());
        }
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<VMachine> getEncoderClass() {
        return VMachine.class;
    }
}
