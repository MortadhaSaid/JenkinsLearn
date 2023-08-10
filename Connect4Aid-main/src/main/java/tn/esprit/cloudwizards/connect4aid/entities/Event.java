package tn.esprit.cloudwizards.connect4aid.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="event")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
        Long eventId;
        String eventName;
        String eventInfo;
        Long maxAttend;
        LocalDate eventStart;
        LocalDate eventEnd;
        @ManyToOne
        User Responsable;
        @ManyToMany
        List<User> Attendies ;
        @OneToMany
        List<Label> labels;

}
