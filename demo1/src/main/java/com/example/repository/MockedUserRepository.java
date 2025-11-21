package com.example.repository;

import com.example.bean.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;

// this class was  done with the help of ai
/**
 * Mock implémentation de UserRepository pour développement
 * Simule une base de données en mémoire

 */
@Repository

public class MockedUserRepository implements UserRepository {

    private Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    public MockedUserRepository() {
        initializeData();
    }

    private void initializeData() {
        save(new User(null, "John Doe", "john@example.com", "0611111111"));
        save(new User(null, "Jane Smith", "jane@example.com", "0622222222"));
        save(new User(null, "Bob Johnson", "bob@example.com", "0633333333"));
    }

    // ===== CrudRepository methods =====

    @Override
    public <S extends User> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter++);
        }
        users.put(entity.getId(), entity);
        System.out.println("[MOCK] User saved: " + entity);
        return entity;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> saved = new ArrayList<>();
        entities.forEach(e -> saved.add(save(e)));
        return saved;
    }

    @Override
    public Optional<User> findById(Long id) {
        System.out.println("[MOCK] Finding user by ID: " + id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        System.out.println("[MOCK] Checking if user exists: " + id);
        return users.containsKey(id);
    }

    @Override
    public List<User> findAll() {
        System.out.println("[MOCK] Finding all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        System.out.println("[MOCK] Finding users by IDs");
        List<User> result = new ArrayList<>();
        ids.forEach(id -> users.get(id));
        return result;
    }

    @Override
    public long count() {
        System.out.println("[MOCK] Counting users");
        return users.size();
    }

    @Override
    public void deleteById(Long id) {
        System.out.println("[MOCK] Deleting user by ID: " + id);
        users.remove(id);
    }

    @Override
    public void delete(User entity) {
        if (entity.getId() != null) {
            users.remove(entity.getId());
            System.out.println("[MOCK] User deleted: " + entity);
        }
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        System.out.println("[MOCK] Deleting users by IDs");
        ids.forEach(users::remove);
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        System.out.println("[MOCK] Deleting multiple users");
        entities.forEach(e -> {
            if (e.getId() != null) {
                users.remove(e.getId());
            }
        });
    }

    @Override
    public void deleteAll() {
        System.out.println("[MOCK] Deleting all users");
        users.clear();
    }

    // ===== JpaRepository methods =====
    @Override
    public List<User> findAll(Sort sort) {
        System.out.println("[MOCK] Finding all users with sort");
        return new ArrayList<>(users.values());
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        System.out.println("[MOCK] Finding all users with pagination");
        List<User> allUsers = new ArrayList<>(users.values());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allUsers.size());
        List<User> pageContent = allUsers.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allUsers.size());
    }


    @Override
    public void flush() {
        System.out.println("[MOCK] Flushing");
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return (List<S>) saveAll(entities);
    }

    @Override
    public void deleteInBatch(Iterable<User> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        deleteAllById(ids);
    }

    @Override
    public User getOne(Long id) {
        return users.get(id);
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public User getReferenceById(Long id) {
        return users.get(id);
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return new ArrayList<>();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, java.util.function.Function<org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    public void reset() {
        users.clear();
        idCounter = 1L;
        initializeData();
        System.out.println("[MOCK] Repository reset");
    }

    public void printAllUsers() {
        System.out.println("[MOCK] === All Users ===");
        users.values().forEach(System.out::println);
    }

    public void addUser(User user) {
        save(user);
    }

    public int getTotalUsers() {
        return users.size();
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}