package pl.lodz.p.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.model.user.ClientType;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.VMachine;

public class CodecProvider implements org.bson.codecs.configuration.CodecProvider {
    public CodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == ClientType.class) {
            return (Codec<T>) new ClientTypeCodec(registry);
        }
        if (clazz == VMachine.class) {
            return (Codec<T>) new VMachineCodec(registry);
        }
        if (clazz == MongoUUID.class) {
            return (Codec<T>) new MongoUUIDCodec(registry);
        }
        // return null when there is no provider for the requested class
        return null;
    }
}
