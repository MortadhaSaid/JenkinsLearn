package tn.esprit.cloudwizards.connect4aid.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloudwizards.connect4aid.entities.Event;
import tn.esprit.cloudwizards.connect4aid.entities.Label;
import tn.esprit.cloudwizards.connect4aid.entities.User;
import tn.esprit.cloudwizards.connect4aid.services.EventServices.IEventService;
import tn.esprit.cloudwizards.connect4aid.services.LabelServices.ILabelService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("event")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {
    IEventService iEventService;
    ILabelService iLabelService;
    private final SimpMessagingTemplate messagingTemplate;
    public EventController(IEventService iEventService,ILabelService iLabelService, SimpMessagingTemplate messagingTemplate){
        this.iEventService = iEventService;
        this.messagingTemplate = messagingTemplate;
        this.iLabelService = iLabelService;
    }
    @GetMapping("{id}")
    Event getEvent(@PathVariable Long Id)
    {
        return iEventService.getEventById(Id);
    }
    @GetMapping("getevent")
    List<Event> getEvent(){return iEventService.getAllEvents();}
    @PostMapping("postevent")
    Event postEvent(@RequestBody Event e){
       // messagingTemplate.convertAndSend("/topic/notifications/user-12", "New event created: " + e.getEventName());

       iEventService.postEvent(e);
       User Res=e.getResponsable();
        this.iEventService.eventSubscribe(Res.getUserId(), e.getEventId());
       return e;

    }
    @DeleteMapping("deleteevent")
    String deleteEvent(@RequestParam Long Id)
    {
        iEventService.deleteEvent(Id);
        return "Deletion is successful";
    }
    @PutMapping("updateEvent")

    Event updateEvent(@RequestBody Event e){
        return iEventService.updateEvent(e);
    }

    @PostMapping("addlabel")
    Label addLabel(@RequestBody Label L){

        return iLabelService.postLabel(L);
    }
    @PostMapping("subscribe/{userId}/{eventId}")
    Event eventSubscribe(@PathVariable Long userId,@PathVariable Long eventId){
        return this.iEventService.eventSubscribe(userId, eventId);
    };
    @GetMapping("labelall")
    List<Label> getLabels(){
        return iLabelService.getLabels();
    }
    @GetMapping("/qrcode/{eventId}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long eventId) throws WriterException, IOException {
        Event event = iEventService.getEventById(eventId);
        String eventLink = "http://localhost:9000/event/" + eventId;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitMatrix bitMatrix = new QRCodeWriter().encode(eventLink, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        byte[] bytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }




//    @GetMapping("testtoken")
//    String tokener(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String token = (String) authentication.getCredentials();
//        JwtUtils n = new JwtUtils();
//        if(!token.isEmpty())
//        return n.extractAllClaims(token).getId();
//        else return ("JWt is empty");
//    }

}
