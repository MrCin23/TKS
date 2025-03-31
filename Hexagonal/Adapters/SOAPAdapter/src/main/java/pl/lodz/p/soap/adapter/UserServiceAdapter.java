//package pl.lodz.p.soap.adapter;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import pl.lodz.p.core.domain.MongoUUID;
//import pl.lodz.p.core.domain.user.*;
//import pl.lodz.p.core.services.service.UserService;
//import pl.lodz.p.soap.model.user.SOAPUser;
//import pl.lodz.p.ui.UserServicePort;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Component
//@AllArgsConstructor
//public class UserServiceAdapter implements UserServicePort {
//
//    private final UserService userService;
//
//    @Override
//    public SOAPUser createUser(SOAPUser user) {
//        return convert(userService.createUser(convert(user)));
//    }
//
//    @Override
//    public List<SOAPUser> getAllUsers() {
//        List<SOAPUser> users = new ArrayList<>();
//        for(User user : userService.getAllUsers()) {
//            users.add(convert(user));
//        }
//        return users;
//    }
//
//    @Override
//    public SOAPUser getUser(UUID uuid) {
//        return convert(userService.getUser(uuid));
//    }
//
//    @Override
//    public void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate) {
//        userService.updateUser(uuid, fieldsToUpdate);
//    }
//
//    @Override
//    public void activateUser(UUID uuid) {
//        userService.activateUser(uuid);
//    }
//
//    @Override
//    public void deactivateUser(UUID uuid) {
//        userService.deactivateUser(uuid);
//    }
//
//    @Override
//    public String getUserByUsername(LoginDTO loginDTO) {
//        return userService.getUserByUsername(loginDTO.getUsername(), loginDTO.getPassword());
//    }
//
//    @Override
//    public SOAPUser getUserByUsername(String username) {
//        return convert(userService.getUserByUsername(username));
//    }
//
//    @Override
//    public List<SOAPUser> getUsersByUsername(String username) {
//        List<SOAPUser> users = new ArrayList<>();
//        for(User user : userService.getUsersByUsername(username)) {
//            users.add(convert(user));
//        }
//        return users;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        return userService.loadUserByUsername(username);
//    }
//
//    @Override
//    public void invalidateToken(String token) {
//        userService.invalidateToken(token);
//    }
//
//    @Override
//    public boolean checkToken(String token) {
//        return userService.checkToken(token);
//    }
//
//    @Override
//    public void changePassword(String username, String newPassword) {
//        userService.changePassword(username, newPassword);
//    }
//
//    private MongoUUID convert(SOAPMongoUUID r) {
//        return new MongoUUID(r.getUuid());
//    }
//    private SOAPMongoUUID convert(MongoUUID r) {
//        return new SOAPMongoUUID(r.getUuid());
//    }
//
//    private Role convert(SOAPRole ent) {
//        if (ent == SOAPRole.ADMIN) {
//            return Role.ADMIN;
//        }
//        else if (ent == SOAPRole.CLIENT) {
//            return Role.CLIENT;
//        }
//        else if (ent == SOAPRole.RESOURCE_MANAGER) {
//            return Role.RESOURCE_MANAGER;
//        }
//        return null;
//    }
//    private SOAPRole convert(Role role) {
//        if (role == Role.ADMIN) {
//            return SOAPRole.ADMIN;
//        }
//        else if (role == Role.CLIENT) {
//            return SOAPRole.CLIENT;
//        }
//        else if (role == Role.RESOURCE_MANAGER) {
//            return SOAPRole.RESOURCE_MANAGER;
//        }
//        return null;
//    }
//
//    private SOAPUser convert(User user) {
//        return switch (user.getClass().getSimpleName()) {
//            case "Client" -> new SOAPClient(convert(user.getEntityId()), user.getFirstName(), user.getUsername(), user.getPassword(),
//                    user.getSurname(), user.getEmailAddress(), convert(user.getRole()), user.isActive(), new SOAPStandard(), ((Client) user).getCurrentRents());
//            case "ResourceManager" -> new SOAPResourceManager(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmailAddress());
//            case "Admin" -> new SOAPAdmin(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getEmailAddress(), user.getPassword());
//            default -> throw new RuntimeException("Unsupported type: " + user.getClass().getSimpleName());
//        };
//    }
//
//    private User convert(SOAPUser ent) {
//        if (ent == null) {
//            return null;
//        }
//        return switch (ent.getClass().getSimpleName()) {
//            case "SOAPClient" -> new Client(convert(ent.getEntityId()), ent.getFirstName(), ent.getUsername(), ent.getPassword(),
//                    ent.getSurname(), ent.getEmailAddress(), convert(ent.getRole()), ent.isActive(), new Standard(), ((SOAPClient) ent).getCurrentRents());
//            case "SOAPResourceManager" -> new ResourceManager(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getPassword(), ent.getEmailAddress());
//            case "SOAPAdmin" -> new Admin(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getEmailAddress(), ent.getPassword());
//            default -> throw new RuntimeException("Unsupported type: " + ent.getClass().getSimpleName());
//        };
//    }
//}
