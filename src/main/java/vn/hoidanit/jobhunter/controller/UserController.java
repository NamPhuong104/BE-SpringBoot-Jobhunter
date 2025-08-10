package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import javax.swing.text.html.Option;
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
    public ResponseEntity<ResultPaginationDTO> getAllUser(@RequestParam("current") Optional<String> currentOptional, @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "1";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "10";

        int current = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current, pageSize);

        ResultPaginationDTO res = this.userService.handleGetAllUser(pageable);
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
