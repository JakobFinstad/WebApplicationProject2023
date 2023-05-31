package no.ntnu.idata2306.group6.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

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
            http
                    .csrf().disable()
                    .cors().and()
                    .authorizeRequests()
                    // Coding for all the scripts and the images and css files
                    .requestMatchers("/css/**", "/images/**", "/js/**", "/templates/**", "/swagger-ui/**").permitAll()


                    // Request matchers for all the pages that is accessible for everyone
                    .requestMatchers("/", "/home", "/index", "/login", "/signup", "/api/user/signup", "/api/testimonial").permitAll()
                    .requestMatchers("/products").permitAll()
                    .requestMatchers("/products/{productId}").permitAll()

                    // Request matchers for all the pages that is accessible for everyone with a user
//                    .requestMatchers("/login", "/signup").not().hasAnyRole("USER","ADMIN")
                    .requestMatchers("/products/{productId}/payment").hasAnyRole("ADMIN", "USER")
                    .requestMatchers("/profile-page").hasAnyRole("USER", "ADMIN")

                    // Request matchers for all the admin pages
                    .requestMatchers("/admin/**", "/api/**").hasRole("ADMIN")

                    .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .successHandler(savedRequestAwareAuthenticationSuccessHandler())
                    .and()
// Rest of your configuration...

                    .logout()
                    .logoutSuccessUrl("/").and()
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())
                    .and();


        } else {
            http.csrf(csrf -> csrf.disable())
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests()
                    .anyRequest()
                    .permitAll();
        }


        return http.build();
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setUseReferer(true);
        return successHandler;
    }


    private AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
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
