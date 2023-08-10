package tn.esprit.cloudwizards.connect4aid.services;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tn.esprit.cloudwizards.connect4aid.JWT.CustomerUsersDetailsServices;
import tn.esprit.cloudwizards.connect4aid.JWT.JwtFilter;
import tn.esprit.cloudwizards.connect4aid.JWT.JwtUtils;
import tn.esprit.cloudwizards.connect4aid.Wrapper.UserWithoutPass;
import tn.esprit.cloudwizards.connect4aid.entities.Role;
import tn.esprit.cloudwizards.connect4aid.entities.User;
import tn.esprit.cloudwizards.connect4aid.repositories.UserRepo;
import tn.esprit.cloudwizards.connect4aid.utils.ApplicationUtils;
import tn.esprit.cloudwizards.connect4aid.utils.Consts;
import tn.esprit.cloudwizards.connect4aid.utils.EmailService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {

    UserRepo userRepo;
    AuthenticationManager authenticationManager;
    CustomerUsersDetailsServices customerUsersDetailsServices;
    JwtUtils jwtUtils;
    JwtFilter jwtFilter;

    EmailService emailService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("inside SignUp {}",requestMap);
        try {
            if(validateRequestMap(requestMap)){
                User user = userRepo.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepo.save(getUserFromRequestMap(requestMap));
                    log.info("user from requestMAP {}",getUserFromRequestMap(requestMap));
                    return ApplicationUtils.getResponseMessage(Consts.SIGNUP_SUCCESSFUL,HttpStatus.OK);
                }
                else
                {
                    return ApplicationUtils.getResponseMessage("Email Already Exists !!",HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return ApplicationUtils.getResponseMessage(Consts.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private boolean validateRequestMap(Map<String,String> reqMap){
        return reqMap.containsKey("lastName") && reqMap.containsKey("firstName") && reqMap.containsKey("email")
                && reqMap.containsKey("contactNumber") && reqMap.containsKey("password") && reqMap.containsKey("dateOfBirth");
    }

    private User getUserFromRequestMap(Map<String,String> reqMap){
        User user = new User();
        user.setFirstName(reqMap.get("firstName"));
        user.setLastName(reqMap.get("lastName"));
        user.setEmail(reqMap.get("email"));
        user.setPassword(reqMap.get("password"));
        user.setStatus("false");
        user.setRole(Role.USER);
        user.setContactNumber(reqMap.get("contactNumber"));
        user.setCreatedAt(LocalDate.now());
        user.setDateOfBirth(LocalDate.parse(reqMap.get("dateOfBirth")));
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("inside login {}",requestMap);
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUsersDetailsServices.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtils.generateToken(customerUsersDetailsServices.getUserDetails().getEmail(),
                                    customerUsersDetailsServices.getUserDetails().getRole())+"\"}",HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+"wait for admin approval. "+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            log.error("{}",ex);
        }return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials. "+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWithoutPass>> getAllUsers() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userRepo.getAllUsers(),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        try{
            if(jwtFilter.isAdmin()){
                if(userRepo.existsById(id)) {
                    userRepo.deleteById(id);
                    return new ResponseEntity<String>("{\"message\":\"" + "Successfully deleted . " + "\"}", HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<String>(String.format("{\"message\":\"" + "no User with id %2d . " + "\"}",id), HttpStatus.NOT_FOUND);
                }
            }
            else{
                return new ResponseEntity<String>("{\"message\":\""+"You're unauthorized to access methode. "+"\"}",HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> activateAccount(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<User> user = userRepo.findById(Long.parseLong(requestMap.get("userId")));
                if(!user.isEmpty()){
                    userRepo.updateStatus(requestMap.get("status"),Long.parseLong(requestMap.get("userId")));
                    //sending emails to all admins to announce about newly activated accounts
                    sendMailToAllAdmins(requestMap.get("status"),user.get().getEmail(),userRepo.getAllAdmins());
                    return new ResponseEntity<String>("{\"message\":\"" + "Status updated Successfully  . " + "\"}", HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>(String.format("{\"message\":\"" + "no User with id %2d . " + "\"}",Long.parseLong(requestMap.get("userId"))), HttpStatus.NOT_FOUND);
                }
            }else{
                return ApplicationUtils.getResponseMessage(Consts.NOT_AUTHORIZED,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> changeRole(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<User> user = userRepo.findById(Long.parseLong(requestMap.get("userId")));
                log.info("inside user {}",user);
                if(!user.isEmpty()){
                    if(user.get().getStatus().equals("true")){
                        userRepo.updateRole(Role.valueOf(requestMap.get("role")),Long.parseLong(requestMap.get("userId")));
                        //sending emails to all admins to announce about newly activated accounts
                        annouceRoleChanging(user.get().getEmail(),userRepo.getAllAdmins(),requestMap.get("role"));
                        return new ResponseEntity<String>("{\"message\":\"" + "Role updated Successfully  . " + "\"}", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<String>("{\"message\":\"" + "this account is not activated yet . " + "\"}", HttpStatus.NOT_ACCEPTABLE);
                    }
                }else{
                    return new ResponseEntity<String>(String.format("{\"message\":\"" + "no User with id %2d . " + "\"}",Long.parseLong(requestMap.get("userId"))), HttpStatus.NOT_FOUND);
                }
            }else {
                return ApplicationUtils.getResponseMessage(Consts.NOT_AUTHORIZED,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private void sendMailToAllAdmins(String status, String user, List<String> allAdmins) {
        //bech me teb3athch el mail el nefs el user elli 9a3ed teb3ath mennou
        allAdmins.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            emailService.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Activated","USER:~ "+user+" is approved by ADMIN:~ " + jwtFilter.getCurrentUser(),allAdmins);
        }else{
            emailService.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disabled","USER:~ "+user+" is disabled by ADMIN:~ " + jwtFilter.getCurrentUser(),allAdmins);
        }

    }

    private void annouceRoleChanging(String user, List<String> allAdmins,String role) {
        allAdmins.remove(jwtFilter.getCurrentUser());
            emailService.sendSimpleMessage(jwtFilter.getCurrentUser(),"Role Changed","Role changed for USER:~ "+user+" to "+role+" by ADMIN:~ " + jwtFilter.getCurrentUser(),allAdmins);
    }

    //alech el checkToken edhi raghmelli hia simple w t7essha zeyda .. idha user yetada al methode edhi w me andouch
    //valid token bech yetla3 status 403 mta3 el ivalid token w ki tebda andik token valid tetada
    //method edhi juste bech tvalidi el user
    @Override
    public ResponseEntity<String> checkToken() {
        try{
            return ApplicationUtils.getResponseMessage("true",HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userRepo.findByEmailId(jwtFilter.getCurrentUser());
            if(!user.equals(null)){
                if(user.getPassword().equals(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userRepo.save(user);
                    return ApplicationUtils.getResponseMessage("Password Changed Successfully. ",HttpStatus.ACCEPTED);
                }else{
                    return ApplicationUtils.getResponseMessage("Incorrect Old Password",HttpStatus.BAD_REQUEST);
                }
            }else{
                return ApplicationUtils.getResponseMessage("Email Not Found. ",HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepo.findByEmailId(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
                emailService.forgotPasswordMail(user.getEmail(), "Credentials by Connect4Aid App", user.getPassword());
                return ApplicationUtils.getResponseMessage("Check Your Mail Box for Credentials. ", HttpStatus.OK);
            }else{
                return ApplicationUtils.getResponseMessage("No Registration Found With This Email. ",HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ApplicationUtils.getResponseMessage(Consts.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
