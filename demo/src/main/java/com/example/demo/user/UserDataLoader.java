package com.example.demo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@Component
public class UserDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(UserDataLoader.class);
    private final JdbcClientUserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserDataLoader(JdbcClientUserRepository userRepository) {
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            try (InputStream inputStream = new ClassPathResource("data/users.json").getInputStream()) {
                List<User> runs = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
                log.info("okey, {}", runs.size());
                userRepository.saveAll(runs);
            } catch (IOException e) {
                throw new RuntimeException("failed to read json data", e);
            }
        } else {
            log.info("contains data");
        }
    }
}

