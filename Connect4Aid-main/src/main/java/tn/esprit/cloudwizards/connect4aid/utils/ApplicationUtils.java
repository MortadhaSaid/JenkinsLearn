package tn.esprit.cloudwizards.connect4aid.utils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ApplicationUtils {

    public static ResponseEntity<String> getResponseMessage(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
    }
}
