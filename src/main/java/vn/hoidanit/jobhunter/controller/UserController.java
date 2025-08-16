package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.UserResponseDTO;
import vn.hoidanit.jobhunter.domain.dto.user.UserDTO;
import vn.hoidanit.jobhunter.domain.dto.user.UserMapper;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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
        User users = this.userService.handleFindUserById(id);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    @ApiMessage("Create user")
    public ResponseEntity<UserResponseDTO> createNewUser(@Validated @RequestBody UserDTO.Create userData) {

        UserResponseDTO res = this.userService.handleCreateUser(userData);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<UserResponseDTO> updateUser(@Validated @RequestBody UserDTO.Update reqData) {
        UserResponseDTO user = this.userService.handleUpdateUser(reqData);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
