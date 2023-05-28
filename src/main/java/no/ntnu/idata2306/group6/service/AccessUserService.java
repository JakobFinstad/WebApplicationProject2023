package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Role;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.entity.dto.UserDTO;
import no.ntnu.idata2306.group6.repository.RoleRepository;
import no.ntnu.idata2306.group6.repository.UserRepository;
import no.ntnu.idata2306.group6.security.AccessUserDetails;
import no.ntnu.idata2306.group6.service.RoleService;
import no.ntnu.idata2306.group6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Lets one access user service.
 */
@Component
public class AccessUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final int MIN_PASSWORD_LENGTH=8;
    private static final String MIN_ONE_CAPITAL_LETTER = ".*[A-Z]+.*";
    private static final String MIN_ONE_SMALL_LETTER = ".*[a-z]+.*";
    private static final String MIN_ONE_NUMBER = ".*\\d+.*";
    private static final String CORRECT_EMAIL_REQUIREMENTS =  "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";


    /**
     * Returns the user by searching for username.
     *
     * @param email of the user
     * @return user
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new AccessUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("User " + email + " not found!");
        }
    }

    /**
     * Get the user which is authenticated for the current session
     *
     * @return User object or null if no user has logged in
     */
    public User getSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String email = authentication.getName();
        return this.userRepository.findByEmail(email).orElse(null);
    }

    private boolean userExists(String email) {
        try {
            loadUserByUsername(email);
            return true;
        } catch (UsernameNotFoundException ex) {
            return false;
        }
    }

    public String tryCreateNewUser(String firstName, String lastName, String email, int phoneNumber, String password, int age) {
        String errorMessage;
        if(firstName == null || firstName.trim().isEmpty()) {
            return  "First name can't be empty";
        }
        if(lastName == null || lastName.trim().isEmpty()) {
            return "Last name can't be empty";
        }
        errorMessage = checkEmailRequirements(email);
        if(errorMessage != null) {
            return errorMessage;
        }
        errorMessage = checkPasswordRequirements(password);
        if(errorMessage == null) {
            createUser(firstName, lastName, email, phoneNumber, password, age);
        }
        return errorMessage;
    }

    private String checkEmailRequirements(String email) {
        String errorMessage = null;
        if(email == null || email.isEmpty()) {
            errorMessage = "Email can't be empty";
        }
        else if (userExists(email)) {
            errorMessage = "Email already taken";
        }
        else if (!email.matches(CORRECT_EMAIL_REQUIREMENTS)) {
            errorMessage = "Email requirements not fulfilled";
        }
        return errorMessage;
    }

    private String checkPasswordRequirements(String password) {
        String errorMessage = null;
        if (password == null || password.isEmpty()) {
            errorMessage = "Password can't be empty";
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            errorMessage = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
        }
        else if(!password.matches(MIN_ONE_CAPITAL_LETTER) || !password.matches(MIN_ONE_SMALL_LETTER) || !password.matches(MIN_ONE_NUMBER)) {
            errorMessage = "Password must contain at least one capital letter, one small letter and on number";
        }
        return errorMessage;
    }

    private void createUser(String firstName, String lastName, String email, int phoneNumber,  String password, int age) {
        System.out.println("here1");
        Role userRole = roleRepository.findOneByName("USER");
        System.out.println(roleRepository.findOneByName("USER"));
        if (userRole != null) {
            System.out.println("here2");
            User user = new User(firstName, lastName, email, phoneNumber, PasswordUtil.createHash(password), age);
            user.addRole(userRole);
            userRepository.save(user);
        }
    }

    public boolean updateProfile(User user, UserDTO userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return true;
    }
}
