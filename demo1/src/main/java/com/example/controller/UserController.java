package com.example.controller;

import com.example.bean.User;
import com.example.dto.UserCreateDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*",methods = {RequestMethod.PUT,RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS}) // allow only this origin

public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> fetchUsers() {
        try {
            Map<String, Object> succesBody = new HashMap<>();
            succesBody.put("users", userService.findAll());
            return ResponseEntity.ok(succesBody);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorBody);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto user, BindingResult bindingResult) {
        try {
            // check valdiation errors
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);

            }
            // create users
            int response = userService.create(user);
            Map<String, Object> body = new HashMap<>();
            if (response == 1) {
                body.put("status", "success");
                body.put("user", user);
                return ResponseEntity.status(HttpStatus.CREATED).body(body);
            }
            throw new Exception("Error creating user");
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorBody);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
        try {
            User user = userService.findById(id);

            if (user == null) {
                Map<String, Object> errorBody = new HashMap<>();
                errorBody.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
            }

            Map<String, Object> body = new HashMap<>();
            body.put("user", user);
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorBody);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserCreateDto updatedUser,@PathVariable("id") long id, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);
            }
            int response = userService.update(updatedUser, id);
            if (response == 1) {
                Map<String, Object> body = new HashMap<>();
                body.put("status", "success");
                body.put("user", updatedUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(body);
            }
            throw new Exception("Error updating user");


        }
        catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorBody);
        }

    }

}
