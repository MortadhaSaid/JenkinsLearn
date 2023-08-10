package tn.esprit.cloudwizards.connect4aid.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NamedQuery(name = "User.findByEmailId",query = "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUsers",query = "select new tn.esprit.cloudwizards.connect4aid.Wrapper.UserWithoutPass(u.userId,u.firstName,u.lastName,u.dateOfBirth,u.email,u.contactNumber,u.createdAt,u.role,u.status) from User u")
@NamedQuery(name = "User.getAllAdmins",query = "select u.email from User u where u.role='ADMIN'")
@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where u.userId=:userId")
@NamedQuery(name = "User.updateRole",query = "update User u set u.role=:role where u.userId=:userId")


@Entity
@Data
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String email;
    String contactNumber;
    LocalDate createdAt;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    String status;
    @ManyToMany
    List<Event> Events;

}
