package tn.esprit.cloudwizards.connect4aid.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Label {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long LabelId;
    String value;
    @ManyToMany
    List<User> Subscribers;
}
