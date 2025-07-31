package vn.hoidanit.jobhunter.controller;

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
    public User createNewUser(@RequestBody User user) {
        User res = this.userService.handleCreateUser(user);

        return res;
    }

    @GetMapping("/users")
    public List<User> getAllUser() {
        List<User> res = this.userService.handleGetAllUser();
        return res;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id) {
        return this.userService.handleGetUserById(id);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User reqData) {

        return this.userService.handleUpdateUser(reqData);
    }


    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.handleDeleteUser(id);
        return "success";
    }
}
