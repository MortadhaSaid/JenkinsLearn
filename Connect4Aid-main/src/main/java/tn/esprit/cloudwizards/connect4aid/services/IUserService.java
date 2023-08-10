package tn.esprit.cloudwizards.connect4aid.services;

import org.springframework.http.ResponseEntity;
import tn.esprit.cloudwizards.connect4aid.Wrapper.UserWithoutPass;
import tn.esprit.cloudwizards.connect4aid.entities.User;

import java.util.List;
import java.util.Map;

public interface IUserService {
    public ResponseEntity<String> signUp (Map<String,String> requestMap);

    public ResponseEntity<String> login (Map<String,String> requestMap);

    public ResponseEntity<List<UserWithoutPass>> getAllUsers();

    public ResponseEntity<String> deleteUser(Long id);
    public ResponseEntity<String> activateAccount(Map<String,String> requestMap);

    public ResponseEntity<String> changeRole(Map<String,String> requestMap);
    public ResponseEntity<String > checkToken();
    public ResponseEntity<String> changePassword(Map<String,String> requestMap);
    public ResponseEntity<String> forgotPassword(Map<String,String> requestMap);
}
