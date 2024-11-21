package io.test.security;

import com.google.common.base.Strings;
import io.jmix.security.CoreSecurityConfiguration;
import io.jmix.security.annotation.Authenticated;
import io.jmix.security.authentication.CurrentAuthentication;
import io.jmix.security.user.InMemoryUserRepository;
import io.jmix.security.user.UserRepository;
import java.util.Collections;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@EnableScheduling
@SpringBootApplication
@RestController
public class Application {
    @Autowired
    private Environment environment;
    @Autowired
    private CurrentAuthentication currentAuthentication;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(Application.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("server.port", "8080")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @EnableWebSecurity
    static class SecurityConfiguration extends CoreSecurityConfiguration {
        @Override
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(auth -> auth.requestMatchers("/anonymous").anonymous());
            return super.securityFilterChain(http);
        }

        @Override
        public UserRepository userRepository() {
            InMemoryUserRepository userRepository = new InMemoryUserRepository();
            userRepository.addUser(User.builder()
                    .username("user")
                    .password("{noop}pass")
                    .authorities(Collections.emptyList())
                    .build());
            return userRepository;
        }
    }

    @Authenticated
//    @Scheduled(fixedRate = 10000L)
    public void test() {
        LoggerFactory.getLogger(Application.class).info("user: {}", currentAuthentication.getAuthentication().getName());
    }

    @GetMapping("/")
    public String index() {
        return "hello";
    }

    @GetMapping("/anonymous")
    public String anonymous() {
        return "anonymous: " + currentAuthentication.getAuthentication().getName();
    }
}