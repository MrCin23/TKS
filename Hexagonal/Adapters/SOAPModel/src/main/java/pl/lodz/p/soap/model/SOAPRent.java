package pl.lodz.p.soap.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.soap.model.user.SOAPClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class SOAPRent extends SOAPAbstractEntityMgd {
    private SOAPClient client;
    @Getter
    private SOAPVMachine vMachine;
    @Getter
    private LocalDateTime beginTime;
    @Getter

    private LocalDateTime endTime;
    @Getter

    private double rentCost;

    public SOAPRent() {
        super(new SOAPMongoUUID(UUID.randomUUID()));
    }

    public SOAPRent(SOAPClient client, SOAPVMachine vMachine, LocalDateTime beginTime) {
        super(new SOAPMongoUUID(UUID.randomUUID()));
        if(vMachine.isRented() == 0) {
            this.client = client;
            this.vMachine = vMachine;
            beginRent(beginTime);
        }
        else {
            throw new RuntimeException("This machine is already rented");
        }
    }

    public SOAPRent(SOAPMongoUUID uuid, SOAPClient client, SOAPVMachine vMachine,
                    LocalDateTime beginTime, LocalDateTime endTime, double rentCost) {
        super(uuid);
        this.client = client;
        this.vMachine = vMachine;
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
                "client=" + client +
                ", vMachine=" + vMachine +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", rentCost=" + rentCost +
                '}';
    }

}
