package pl.lodz.p.repo.adapter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.domain.x86;
import pl.lodz.p.port.infrastructure.vmachine.VMAdd;
import pl.lodz.p.port.infrastructure.vmachine.VMGet;
import pl.lodz.p.port.infrastructure.vmachine.VMRemove;
import pl.lodz.p.port.infrastructure.vmachine.VMUpdate;

import pl.lodz.p.repo.model.AppleArchEnt;
import pl.lodz.p.repo.model.VMachineEnt;
import pl.lodz.p.repo.model.MongoUUIDEnt;

import pl.lodz.p.repo.model.x86Ent;
import pl.lodz.p.repo.repository.VMachineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class VMachineAdapter implements VMGet, VMUpdate, VMRemove, VMAdd {
    private final VMachineRepository vMachineRepo;

    @Override
    public void add(VMachine vMachine) {
        vMachineRepo.add(convert(vMachine));
    }

    @Override
    public List<VMachine> getVMachines() {
        List<VMachine> result = new ArrayList<>();
        for (VMachineEnt ent : vMachineRepo.getVMachines()) {
            result.add(convert(ent));
        }
        return result;
    }

    @Override
    public VMachine getVMachineByID(MongoUUID uuid) {
        return convert(vMachineRepo.getVMachineByID(convert(uuid)));
    }

    @Override
    public void remove(VMachine vMachine) {
        vMachineRepo.remove(convert(vMachine));
    }

    @Override
    public void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate) {
        vMachineRepo.update(convert(uuid), fieldsToUpdate);
    }

    @Override
    public void update(MongoUUID uuid, String field, Object value) {
        vMachineRepo.update(convert(uuid), field, value);
    }

    private VMachineEnt convert(VMachine vm) {
        VMachineEnt ent = switch (vm.getClass().getSimpleName()) {
            case "x86" -> new x86Ent();
            case "AppleArch" -> new AppleArchEnt();
            default -> throw new RuntimeException(vm.getClass().getSimpleName() + " not supported");
        };
        try {
            PropertyUtils.copyProperties(ent, vm);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }

    private VMachine convert(VMachineEnt ent) {
        VMachine vm = switch (ent.getClass().getSimpleName()) {
            case "x86Ent" -> new x86();
            case "AppleArchEnt" -> new AppleArch();
            default -> throw new RuntimeException(ent.getClass().getSimpleName() + " not supported");
        };
        try {
            PropertyUtils.copyProperties(vm, ent);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return vm;
    }

    private MongoUUID convert(MongoUUIDEnt ent) {
        MongoUUID uuid = new MongoUUID();
        try {
            PropertyUtils.copyProperties(uuid, ent);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return uuid;
    }

    private MongoUUIDEnt convert(MongoUUID uuid) {
        MongoUUIDEnt ent = new MongoUUIDEnt();
        try {
            PropertyUtils.copyProperties(ent, uuid);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }
}
