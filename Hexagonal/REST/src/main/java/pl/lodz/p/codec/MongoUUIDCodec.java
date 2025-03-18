package pl.lodz.p.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.model.MongoUUID;

import java.util.UUID;


public class MongoUUIDCodec implements Codec<MongoUUID> {

    private final Codec<UUID> uuidCodec;

    public MongoUUIDCodec(CodecRegistry registry) {
        uuidCodec = registry.get(UUID.class);
    }

    @Override
    public MongoUUID decode(BsonReader bsonReader, DecoderContext decoderContext) {
        UUID uuid = uuidCodec.decode(bsonReader, decoderContext);
        return new MongoUUID(uuid);
    }

    @Override
    public void encode(BsonWriter bsonWriter, MongoUUID uuid, EncoderContext encoderContext) {
        uuidCodec.encode(bsonWriter, uuid.getUuid(), encoderContext);
    }

    @Override
    public Class<MongoUUID> getEncoderClass() {
        return MongoUUID.class;
    }
}