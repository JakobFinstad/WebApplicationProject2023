package no.ntnu.idata2306.group6.security;

import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.repository.RoleRepository;
import no.ntnu.idata2306.group6.service.RoleService;
import no.ntnu.idata2306.group6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Lets one access user service.
 */
@Component
public class AccessUserService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    private final Map<String, AccessUserDetails> userRegistry = new HashMap<>();

    public void init() {
        User user = new User(
                "Harald",
                "Fredriksen",
                "haraldwangsvik@gmail.com",
                94133252,
                passwordEncoder.encode("Harald123"),
                21
        );
        user.addRole(roleService.findById(3));

        userRegistry.put("user", new AccessUserDetails(user));
        User admin = new User(
                "Jakob",
                "Finstad",
                "jakobfinstad@gmail.com",
                42069420,
                passwordEncoder.encode("Jakob123"),
                21
        );
        admin.addRole(roleService.findById(1));

        userRegistry.put("admin", new AccessUserDetails(admin));
    }

    /**
     * Returns the user by searching for username.
     *
     * @param email of the user
     * @return user
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccessUserDetails userDetails = userRegistry.get(email);
//        Optional<User> user = userService.findByEmail(email);
//        if (user.isPresent()) {
//            return new AccessUserDetails(user.get());
//        } else {
//            throw new UsernameNotFoundException("User " + email + " not found!");
//        }
        if (userDetails == null) {
            throw new UsernameNotFoundException(email);
        }
        return userDetails;
    }
}
