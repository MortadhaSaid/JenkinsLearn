package tn.esprit.cloudwizards.connect4aid.services.EventServices.EventListener;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.cloudwizards.connect4aid.entities.Event;

import javax.persistence.PostPersist;

public class EventListener {
    @Autowired
    private ListenerService listenerService;
    @PostPersist
    public void onAddition(Event E) {
        //
    }
}
