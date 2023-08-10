package tn.esprit.cloudwizards.connect4aid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import tn.esprit.cloudwizards.connect4aid.Wrapper.UserWithoutPass;
import tn.esprit.cloudwizards.connect4aid.entities.Role;
import tn.esprit.cloudwizards.connect4aid.entities.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailId(@Param("email") String email);
    List<UserWithoutPass> getAllUsers();

    List<String> getAllAdmins();

    @Transactional
    @Modifying
    // idha me t7ottech edhom me tnejjem te5ou el access el methode "updateStatus" w me tnejemch tmodifi
    //7attit type de retour Long melloul me mchech .. log 9allik modifying queries return only Integer
    Integer updateStatus(@Param("status") String status,@Param("userId") Long userId);
    @Transactional
    @Modifying
    Integer updateRole(@Param("role") Role role, @Param("userId") Long userId);
}
