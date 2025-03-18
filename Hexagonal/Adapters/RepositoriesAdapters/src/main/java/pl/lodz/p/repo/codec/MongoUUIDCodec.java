package pl.lodz.p.repo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.repo.MongoUUIDEnt;

import java.util.UUID;


public class MongoUUIDCodec implements Codec<MongoUUIDEnt> {

    private final Codec<UUID> uuidCodec;

    public MongoUUIDCodec(CodecRegistry registry) {
        uuidCodec = registry.get(UUID.class);
    }

    @Override
    public MongoUUIDEnt decode(BsonReader bsonReader, DecoderContext decoderContext) {
        UUID uuid = uuidCodec.decode(bsonReader, decoderContext);
        return new MongoUUIDEnt(uuid);
    }

    @Override
    public void encode(BsonWriter bsonWriter, MongoUUIDEnt uuid, EncoderContext encoderContext) {
        uuidCodec.encode(bsonWriter, uuid.getUuid(), encoderContext);
    }

    @Override
    public Class<MongoUUIDEnt> getEncoderClass() {
        return MongoUUIDEnt.class;
    }
}