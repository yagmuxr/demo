package com.example.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClientUserRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcClientUserRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcClientUserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list();
    }

    public Optional<User> findById(Integer id) {
        return jdbcClient.sql("SELECT id, username, password FROM users WHERE id = ?")
                .param(id)
                .query(User.class)
                .optional();
    }

    public void create(User user) {
        var updated = jdbcClient.sql("INSERT INTO users (username, password) VALUES (?, ?)")
                .params(List.of(user.getUsername(), user.getPassword()))
                .update();
        Assert.state(updated == 1, "Failed to create User " + user.getUsername());
    }

    public void update(User user, Integer id) {
        var updated = jdbcClient.sql("UPDATE users SET username = ?, password = ? WHERE id = ?")
                .params(List.of(user.getUsername(), user.getPassword(), id))
                .update();
        Assert.state(updated == 1, "Failed to update User " + user.getUsername());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("DELETE FROM users WHERE id = ?")
                .param(id)
                .update();
        Assert.state(updated == 1, "Failed to delete User with id " + id);
    }

    public int count() {
        return jdbcClient.sql("SELECT * FROM users").query().listOfRows().size();
    }

    public void saveAll(List<User> users) {
        users.forEach(this::create);
    }
}
