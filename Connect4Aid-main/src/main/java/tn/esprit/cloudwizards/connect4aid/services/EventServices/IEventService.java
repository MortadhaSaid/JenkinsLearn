package tn.esprit.cloudwizards.connect4aid.services.EventServices;

import org.springframework.data.repository.query.Param;
import tn.esprit.cloudwizards.connect4aid.entities.Event;
import tn.esprit.cloudwizards.connect4aid.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface IEventService {
    public Event postEvent(Event e);
    public List<Event> getAllEvents();
    public void deleteEvent(Long e);
    public Event updateEvent(Event e);
    public Event getEventById(Long Id);
    public List<Event> getEventByName(String eventName);
    public Event eventSubscribe(Long userId,Long eventId);
    public void eventUnsub(Long userId,Long evenId);
    public List<Event> getEventByResponsable(User Res);
    public List<Event> retrieveEventByDate(LocalDate startDate, LocalDate endDate);
    public List<User> getEventAttendies(Event E);
    public User getEventResponsable(Event E);

}
