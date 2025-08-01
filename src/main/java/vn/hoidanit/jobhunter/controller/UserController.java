package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User res = this.userService.handleCreateUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> res = this.userService.handleGetAllUser();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User users = this.userService.handleGetUserById(id);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User reqData) {
        User user = this.userService.handleUpdateUser(reqData);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
