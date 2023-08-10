package tn.esprit.cloudwizards.connect4aid.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloudwizards.connect4aid.Wrapper.UserWithoutPass;
import tn.esprit.cloudwizards.connect4aid.entities.User;
import tn.esprit.cloudwizards.connect4aid.services.IUserService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserRest {

    IUserService iUserService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody  Map<String, String> requestMap){
        return iUserService.signUp(requestMap);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String,String> requestMap){
        return iUserService.login(requestMap);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserWithoutPass>> getAllUsers(){
        return iUserService.getAllUsers();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long id){
        return iUserService.deleteUser(id);
    }

    @PutMapping("/activateaccount")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String,String> requestMap){
        return iUserService.activateAccount(requestMap);
    }

    @PutMapping("/changerole")
    public ResponseEntity<String>  updateRole(@RequestBody Map<String,String> requesMap){
        return iUserService.changeRole(requesMap);
    }

    @GetMapping("/checktoken")
    public ResponseEntity<String > checkToken(){
        return iUserService.checkToken();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap){
        return iUserService.changePassword(requestMap);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap){
        return iUserService.forgotPassword(requestMap);
    }
}
