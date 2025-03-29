package pl.lodz.p.soap.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.rest.model.RESTAbstractEntityMgd;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.RESTVMachine;
import pl.lodz.p.rest.model.user.RESTClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class SOAPRent extends RESTAbstractEntityMgd {
    private RESTClient client;
    @Getter
    private RESTVMachine RESTVMachine;
    @Getter
    private LocalDateTime beginTime;
    @Getter

    private LocalDateTime endTime;
    @Getter

    private double rentCost;

    public SOAPRent() {
        super(new RESTMongoUUID(UUID.randomUUID()));
    }

    public SOAPRent(RESTClient client, RESTVMachine RESTVMachine, LocalDateTime beginTime) {
        super(new RESTMongoUUID(UUID.randomUUID()));
        if(RESTVMachine.isRented() == 0) {
            this.client = client;
            this.RESTVMachine = RESTVMachine;
            beginRent(beginTime);
        }
        else {
            throw new RuntimeException("This machine is already rented");
        }
    }

    public SOAPRent(RESTMongoUUID uuid, RESTClient client, RESTVMachine RESTVMachine,
                    LocalDateTime beginTime, LocalDateTime endTime, double rentCost) {
        super(uuid);
        this.client = client;
        this.RESTVMachine = RESTVMachine;
        this.beginTime = beginTime;
        this.rentCost = rentCost;
//        beginRent(beginTime);
        if(endTime == null) {
            this.endTime = null;
        }
        else {
//            endRent(endTime);
            this.endTime = endTime;
        }
    }

    //Methods
    public void beginRent(LocalDateTime beginTime) {
        if(this.beginTime == null && getRESTVMachine().isRented()==0){
            this.setBeginTime(Objects.requireNonNullElseGet(beginTime, LocalDateTime::now));
            RESTVMachine.setIsRented(RESTVMachine.getIsRented()+1);
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
            this.getRESTVMachine().setRented(0);
        }
        else {
            throw new RuntimeException("endRent() called twice");
        }
    }

    public void calculateRentalPrice() {
        Duration d = Duration.between(beginTime, endTime);
        int days = (int) d.toDays() + 1;
        this.rentCost = days * RESTVMachine.getActualRentalPrice();
    }

    @Override
    public String toString() {
        return "Rent{" +
                "client=" + client +
                ", vMachine=" + RESTVMachine +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", rentCost=" + rentCost +
                '}';
    }

}
