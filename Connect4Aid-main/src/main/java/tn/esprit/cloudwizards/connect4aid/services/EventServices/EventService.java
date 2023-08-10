package tn.esprit.cloudwizards.connect4aid.services.EventServices;

import org.springframework.stereotype.Service;
import tn.esprit.cloudwizards.connect4aid.entities.Event;
import tn.esprit.cloudwizards.connect4aid.entities.User;
import tn.esprit.cloudwizards.connect4aid.repositories.EventRepo;
import tn.esprit.cloudwizards.connect4aid.repositories.UserRepo;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService implements IEventService {
    EventRepo eventRepo;
    UserRepo userRepo;

    public EventService(EventRepo eventRepo,UserRepo userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }
    public List<Event> getAllEvents(){
        return eventRepo.findAll();
    }
    public Event postEvent(Event e) {
        return eventRepo.save(e);
    }
    public void deleteEvent(Long e) {
        eventRepo.deleteById(e);
    }
    public Event updateEvent(Event e) {
        return eventRepo.save(e);
    }

    public Event getEventById(Long Id) {
        return eventRepo.findById(Id).orElse(null);
    }
    public List<Event> getEventByName(String eventName) {
        return eventRepo.getEventByName(eventName);
    }
    public List<Event> getEventByResponsable(User Res){
        return eventRepo.getEventByResponsable(Res);
    }
    public List<Event> retrieveEventByDate(LocalDate startDate,LocalDate endDate){
        return eventRepo.retrieveEventByDate(startDate, endDate);
    }
    public List<User> getEventAttendies(Event E){
        return getEventById(E.getEventId()).getAttendies();
    }
    public User getEventResponsable(Event E){
        return getEventById(E.getEventId()).getResponsable();
    }

    public Event eventSubscribe(Long userId,Long eventId) {
       Event E = eventRepo.findById(eventId).get();
       if (E.getMaxAttend()< (long) E.getAttendies().size()) {
           User U = userRepo.findById(userId).get();

           E.getAttendies().add(U);
           return eventRepo.save(E);
       }
       else {
           return null;
       }
       // eventRepo.findAllById(Collections.singleton(eventId)).;
    }

    public void eventUnsub(Long userId, Long eventId) {
        Event E = eventRepo.findById(eventId).get();
        User U = userRepo.findById(userId).get();
        E.getAttendies().remove(U);
        eventRepo.save(E);
    }


}
