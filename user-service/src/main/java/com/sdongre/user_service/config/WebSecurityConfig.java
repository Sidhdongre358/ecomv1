package com.sdongre.user_service.config;

import com.sdongre.user_service.model.entity.Role;
import com.sdongre.user_service.model.entity.RoleName;
import com.sdongre.user_service.model.entity.User;
import com.sdongre.user_service.repository.RoleRepository;
import com.sdongre.user_service.repository.UserRepository;
import com.sdongre.user_service.security.jwt.JwtEntryPoint;
import com.sdongre.user_service.security.jwt.JwtTokenFilter;
import com.sdongre.user_service.security.userprinciple.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.config.CorsRegistry;

import java.util.Set;

@EnableWebSecurity
@Configuration
//@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailService userDetailService;
    private final JwtEntryPoint jwtEntryPoint;

    @Autowired
    public WebSecurityConfig(UserDetailService userDetailService, JwtEntryPoint jwtEntryPoint) {
        this.userDetailService = userDetailService;
        this.jwtEntryPoint = jwtEntryPoint;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    // Filter chain

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        System.out.println("security filterChain before");
        ;
        // configure here
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->

                                auth.requestMatchers("/api/auth/**").permitAll()
                                        .requestMatchers("/api/manager/token").permitAll()
                                        .requestMatchers("/api/manager/change-password").permitAll()
                                        .requestMatchers("/api/manager/delete/**").permitAll()
                                        .requestMatchers("/api/auth/logout").permitAll()
                                        .requestMatchers("/api/manager/user/**").permitAll()
                                        .requestMatchers("/v3/api-docs/user-service").permitAll()
                                        .requestMatchers("/v3/api-docs/**").permitAll()
                                        .requestMatchers("/swagger-ui/**").permitAll()
                                        .requestMatchers("/swagger-resources/**").permitAll()
                                        .requestMatchers("/webjars/**").permitAll()
//                                .requestMatchers( "/swagger-resources/**").permitAll()
                                        .anyRequest().authenticated()

                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //TODO check if not working then turn on this
//        http.headers(headers -> headers.frameOptions(
//                HeadersConfigurer.FrameOptionsConfig::sameOrigin));


        System.out.println("security filterChain after");

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/webjars/**"
        );
    }



    // Default users - Admin, PM , USER

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseGet(() -> roleRepository.save(new Role(RoleName.USER)));

            Role pmRole = roleRepository.findByName(RoleName.PM)
                    .orElseGet(() -> roleRepository.save(new Role(RoleName.PM)));

            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(RoleName.ADMIN)));

            // Define role sets
            Set<Role> userRoles = Set.of(userRole);
            Set<Role> pmRoles = Set.of(pmRole);
            Set<Role> adminRoles = Set.of(userRole, pmRole, adminRole);

            // Create test users
            if (!userRepository.existsByUsername("user")) {

                User user = new User();

                user.setUsername("user");
                user.setEmail("user@gmail.com");
                //  user.setPhone("987654388");
                user.setPassword(passwordEncoder().encode("user123"));
                user.setGender("MALE");
                user.setFullname("John doe");
                //user.setAvatar("defaultImage");

                //     User user = new User("user", "user@example.com", passwordEncoder.encode("pass123"));
                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("pmuser")) {
                //User pm = new User("pm", "pm@example.com", passwordEncoder.encode("pm123"));
                User pm = new User();
                //   pm.setAvatar("defaultImage");
                pm.setEmail("pm@gmail.com");
                //  pm.setPhone("9876545678");
                pm.setFullname("Kelvin Doe");
                pm.setUsername("pmuser");
                pm.setPassword(passwordEncoder.encode("pm123"));
                pm.setGender("MALE");


                userRepository.save(pm);
            }

            if (!userRepository.existsByUsername("admin")) {
                //  User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin123"));

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@gmail.com");
                admin.setGender("MALE");
                //   admin.setPhone("987654457");
                admin.setFullname("Charlie");
                userRepository.save(admin);
            }

            // Assign roles to users
            userRepository.findByUsername("user").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("pmuser").ifPresent(pm -> {
                pm.setRoles(pmRoles);
                userRepository.save(pm);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }


}
