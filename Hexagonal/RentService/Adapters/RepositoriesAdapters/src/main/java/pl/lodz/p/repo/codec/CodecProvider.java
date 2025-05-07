package pl.lodz.p.repo.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.vm.data.VMachineEnt;
import pl.lodz.p.repo.client.data.ClientTypeEnt;

public class CodecProvider implements org.bson.codecs.configuration.CodecProvider {
    public CodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == ClientTypeEnt.class) {
            return (Codec<T>) new ClientTypeCodec(registry);
        }
        if (clazz == VMachineEnt.class) {
            return (Codec<T>) new VMachineCodec(registry);
        }
        if (clazz == MongoUUIDEnt.class) {
            return (Codec<T>) new MongoUUIDCodec(registry);
        }
        // return null when there is no provider for the requested class
        return null;
    }
}
