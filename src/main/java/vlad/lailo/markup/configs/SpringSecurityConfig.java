package vlad.lailo.markup.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private static final String API_URL = "/api/v2";
    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf()
                .disable()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, API_URL + "/moderators/{id}")
                    .hasAuthority("OP_MODERATOR_READ")
                .requestMatchers(HttpMethod.POST, API_URL + "/moderators/create")
                    .hasAuthority("OP_MODERATOR_CREATE")
                .requestMatchers(HttpMethod.PUT, API_URL + "/moderators/{id}")
                    .hasAuthority("OP_MODERATOR_UPDATE")
                .requestMatchers(HttpMethod.DELETE, API_URL + "/moderators/{id}")
                    .hasAuthority("OP_MODERATOR_DELETE")
                .requestMatchers(HttpMethod.GET, API_URL + "/roles", API_URL + "/roles/ops")
                    .hasAuthority("OP_ROLE_READ")
                .requestMatchers(HttpMethod.POST, API_URL + "/roles/create")
                    .hasAuthority("OP_ROLE_CREATE")
                .requestMatchers(HttpMethod.PUT, API_URL + "/roles/{id}")
                    .hasAuthority("OP_ROLE_UPDATE")
                .requestMatchers(HttpMethod.DELETE, API_URL + "/roles/{id}")
                    .hasAuthority("OP_ROLE_DELETE")
                .requestMatchers(HttpMethod.GET, API_URL + "/datasets/{datasetName}/names", API_URL + "/datasets/{datasetName}/{dataName}")
                    .hasAuthority("OP_DATASET_READ")
                .requestMatchers(HttpMethod.POST, API_URL + "/datasets/{datasetName}/{dataName}")
                    .hasAuthority("OP_DATASET_UPDATE")
                .requestMatchers(HttpMethod.PUT, API_URL + "/datasets/load")
                    .hasAuthority("OP_DATASET_CREATE")
                .anyRequest()
                .authenticated();
        return http.build();
    }
}
