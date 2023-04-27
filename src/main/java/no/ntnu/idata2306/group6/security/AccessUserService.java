package no.ntnu.idata2306.group6.security;

import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Lets one access user service.
 */
@Service
public class AccessUserService implements UserDetailsService {
    @Autowired
    UserService userService;

    /**
     * Returns the user by searching for username.
     *
     * @param email of the user
     * @return user
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return new AccessUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User " + email + " not found!");
        }
    }
}
