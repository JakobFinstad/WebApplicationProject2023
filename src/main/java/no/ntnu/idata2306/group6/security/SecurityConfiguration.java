package no.ntnu.idata2306.group6.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Creates AuthenticationManager - set up authentication type
 * The @EnableGlobalMethodSecurity is needed so that each endpoint can specify which role it requires
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;


    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Called automatically by the framework to find out what authentication to use.
     * Here we tell that we want to load users from a database
     *
     * @param auth Authentication builder
     * @throws Exception
     */
    @Autowired
    protected void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * This method will be called automatically by the framework to find out what authentication to use.
     *
     * @param http HttpSecurity setting builder
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain configureAuthorizationFilterChain(HttpSecurity http) throws Exception {
        boolean enableSecurity = true;

        if (enableSecurity) {
            http.csrf(csrf -> csrf.disable())
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests()
                    .requestMatchers("/css/**").permitAll()
//                    .requestMatchers("/webjars/bootstrap/js/bootstrap.min.js").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .requestMatchers("/templates/**").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/signup").permitAll()
                    .requestMatchers("/products/**").hasAnyRole("ADMIN", "USER")
                    .requestMatchers("/api/user/signup").permitAll()
                    .requestMatchers("/api/testimonial").permitAll()
                    .and().formLogin().loginPage("/login")
                    .and().logout().logoutSuccessUrl("/");
        } else {
            http.csrf(csrf -> csrf.disable())
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests()
                    .anyRequest()
                    .permitAll();
        }


        return http.build();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password(getPasswordEncoder().encode("password")).roles("USER")
//                .and()
//                .withUser("admin").password(getPasswordEncoder().encode("admin")).roles("ADMIN");
//    }

    /**
     * Returns the authentication manager.
     *
     * @param config Authentication Configuration
     * @return the authentication manager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Returns the password encoder.
     *
     * @return password encoder
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
