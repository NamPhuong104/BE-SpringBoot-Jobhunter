package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("Create user")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User res = this.userService.handleCreateUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/users")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {
        ResultPaginationDTO res = this.userService.handleGetAllUser(spec, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User users = this.userService.handleGetUserById(id);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<User> updateUser(@RequestBody User reqData) {
        User user = this.userService.handleUpdateUser(reqData);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
