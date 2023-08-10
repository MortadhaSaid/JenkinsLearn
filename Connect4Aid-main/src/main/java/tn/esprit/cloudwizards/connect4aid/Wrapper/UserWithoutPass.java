package tn.esprit.cloudwizards.connect4aid.Wrapper;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import tn.esprit.cloudwizards.connect4aid.entities.Role;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserWithoutPass {
    Long userId;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String email;
    String contactNumber;
    LocalDate createdAt;
    Role role;
    String status;

    public UserWithoutPass(Long userId, String firstName, String lastName, LocalDate dateOfBirth, String email, String contactNumber, LocalDate createdAt, Role role, String status) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.contactNumber = contactNumber;
        this.createdAt = createdAt;
        this.role = role;
        this.status = status;
    }
}
