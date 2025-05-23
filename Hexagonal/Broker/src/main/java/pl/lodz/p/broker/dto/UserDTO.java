package pl.lodz.p.broker.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class UserDTO extends RESTAbstractEntityMgd {
    private String firstName;
    private String surname;
    private String username;
    private String password;
    private String emailAddress;
    private RESTRole role;
    private boolean active;
    private RESTClientType clientType;
    private int currentRents;

    public UserDTO(RESTMongoUUID userId,
                    String firstName,
                    String username,
                    String password,
                    String surname,
                    String emailAddress,
                    RESTRole role,
                    boolean active, RESTClientType RESTClientType, int currentRents) {
        super(userId);
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;//BCrypt.hashpw(password, BCrypt.gensalt());
        this.emailAddress = emailAddress;
        this.role = role;
        this.active = active;
        this.clientType = RESTClientType;
        this.currentRents = currentRents;
    }

//    public boolean checkPassword(String password) {
//        return BCrypt.checkpw(password, this.password);
//    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
