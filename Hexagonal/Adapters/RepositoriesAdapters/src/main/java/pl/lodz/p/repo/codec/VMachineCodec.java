package pl.lodz.p.repo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.repo.vm.data.AppleArchEnt;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.vm.data.VMachineEnt;
import pl.lodz.p.repo.vm.data.x86Ent;

import java.util.UUID;

public class VMachineCodec implements Codec<VMachineEnt> {
    private final CodecRegistry codecRegistry;

    public VMachineCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public VMachineEnt decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        MongoUUIDEnt uuid = new MongoUUIDEnt(UUID.fromString(bsonReader.readString()));
        String type = bsonReader.readString("_clazz");
        int CPUNumber = bsonReader.readInt32("CPUNumber");
        String ramSize = bsonReader.readString("ramSize");
        int isRented = bsonReader.readInt32("isRented");
        float actualRentalPrice = (float) bsonReader.readDouble("actualRentalPrice");
        String manufacturer = "";
        if(type.equalsIgnoreCase("x86ent") || type.equalsIgnoreCase("x86")) {
            manufacturer = bsonReader.readString("manufacturer");
        }
        bsonReader.readEndDocument();
        if(type.equalsIgnoreCase("x86ent")  || type.equalsIgnoreCase("x86")) {
            return new x86Ent(uuid, CPUNumber, ramSize, isRented, manufacturer);
        }
        else if(type.equals("applearchent") || type.equals("applearch")) {
            return new AppleArchEnt(uuid, CPUNumber, ramSize, isRented);
        }
        else {
            throw new RuntimeException("Unknown VMachine type: " + type);
        }
    }

    @Override
    public void encode(BsonWriter bsonWriter, VMachineEnt vMachine, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_id", vMachine.getEntityId().toString());
        bsonWriter.writeString("_clazz", vMachine.getClass().getSimpleName().toLowerCase());
        bsonWriter.writeInt32("CPUNumber", vMachine.getCPUNumber());
        bsonWriter.writeString("ramSize", vMachine.getRamSize());
        bsonWriter.writeInt32("isRented", vMachine.isRented());
        bsonWriter.writeDouble("actualRentalPrice", vMachine.getActualRentalPrice());
        System.out.println("GIGABALLS" + vMachine.getClass().getSimpleName().toLowerCase());
        if(vMachine.getClass().getSimpleName().equalsIgnoreCase("x86ent") || vMachine.getClass().getSimpleName().equalsIgnoreCase("x86")) {
            x86Ent pom = (x86Ent) vMachine;
            bsonWriter.writeString("manufacturer", pom.getManufacturer());
            System.out.println("MEGABALLS" + vMachine.getClass().getSimpleName().toLowerCase() + "        " + pom + pom.getManufacturer());
        }
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<VMachineEnt> getEncoderClass() {
        return VMachineEnt.class;
    }
}
