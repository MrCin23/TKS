package pl.lodz.p.repo;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.lodz.p.repo.user.ClientEnt;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class RentEnt extends AbstractEnt {
    @BsonProperty("client")
    @NotNull(message = "Client cannot be null for rent to exist")
    private ClientEnt clientEnt;
    @Getter
    @BsonProperty("vMachine")
    @NotNull(message = "VM cannot be null for rent to exist")
    private VMachineEnt vMachine;
    @Getter
    @BsonProperty("beginTime")
    private LocalDateTime beginTime;
    @Getter
    @BsonProperty("endTime")
    private LocalDateTime endTime;
    @Getter
    @BsonProperty("rentCost")
    private double rentCost;

    public RentEnt() {
        super(new MongoUUIDEnt(UUID.randomUUID()));
    }

    public RentEnt(ClientEnt clientEnt, VMachineEnt vMachine, LocalDateTime beginTime) {
        super(new MongoUUIDEnt(UUID.randomUUID()));
        if(vMachine.isRented() == 0) {
            this.clientEnt = clientEnt;
            this.vMachine = vMachine;
            beginRent(beginTime);
        }
        else {
            throw new RuntimeException("This machine is already rented");
        }
    }

    @BsonCreator
    public RentEnt(@BsonProperty("_id") MongoUUIDEnt uuid, @BsonProperty("client") ClientEnt clientEnt, @BsonProperty("vMachine") VMachineEnt vMachine,
                   @BsonProperty("beginTime") LocalDateTime beginTime, @BsonProperty("endTime") LocalDateTime endTime, @BsonProperty("rentCost") double rentCost) {
        super(uuid);
        this.clientEnt = clientEnt;
        this.vMachine = vMachine;
//        this.beginTime = beginTime;
        this.rentCost = rentCost;
        beginRent(beginTime);
        if(endTime == null) {
            this.endTime = null;
        }
        else {
            endRent(endTime);
        }
    }

    //Methods
    public void beginRent(LocalDateTime beginTime) {
        if(this.beginTime == null && getVMachine().isRented()==0){
            this.setBeginTime(Objects.requireNonNullElseGet(beginTime, LocalDateTime::now));
            vMachine.setIsRented(vMachine.getIsRented()+1);
        }
        else if(beginTime == null){
            throw new RuntimeException("beginRent() called twice");
        }
        else {
            throw new RuntimeException("this VMachine is already rented");
        }
    }

    public void endRent(LocalDateTime endTime) {
        if(this.endTime == null){
            if(endTime == null)
            {
                this.setEndTime(LocalDateTime.now());
            }
            this.setEndTime(endTime);
            this.calculateRentalPrice();
            this.getVMachine().setRented(0);
        }
        else {
            throw new RuntimeException("endRent() called twice");
        }
    }

    public void calculateRentalPrice() {
        Duration d = Duration.between(beginTime, endTime);
        int days = (int) d.toDays() + 1;
        this.rentCost = days * vMachine.getActualRentalPrice();
    }

    @Override
    public String toString() {
        return "Rent{" +
                "client=" + clientEnt +
                ", vMachine=" + vMachine +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", rentCost=" + rentCost +
                '}';
    }

}
