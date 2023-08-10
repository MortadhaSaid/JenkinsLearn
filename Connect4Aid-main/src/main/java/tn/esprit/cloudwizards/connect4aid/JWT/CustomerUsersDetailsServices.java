package tn.esprit.cloudwizards.connect4aid.JWT;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.cloudwizards.connect4aid.entities.User;
import tn.esprit.cloudwizards.connect4aid.repositories.UserRepo;

import java.util.ArrayList;
import java.util.Objects;
@Slf4j
@Service
public class CustomerUsersDetailsServices implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    private tn.esprit.cloudwizards.connect4aid.entities.User userDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}",username);
        userDetails = userRepo.findByEmailId(username);
        if(!Objects.isNull(userDetails))
            return new org.springframework.security.core.userdetails.User(userDetails.getEmail(),userDetails.getPassword(),new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not Found. ");
    }

    public tn.esprit.cloudwizards.connect4aid.entities.User getUserDetails() {
        return userDetails;
    }
}
