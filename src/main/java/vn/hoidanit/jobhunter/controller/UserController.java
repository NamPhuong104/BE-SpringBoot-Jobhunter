package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, CompanyService companyService, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
    }


    @GetMapping("/users")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {
        ResultPaginationDTO res = this.userService.handleGetAllUser(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.handleFindUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User với id:  " + id + " không tồn tại !!!!!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    @PostMapping("/users")
    @ApiMessage("Create user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User userData) throws IdInvalidException {

        boolean isEmailExist = userService.isEmailExist(userData.getEmail());
        Company isCompanyExist = companyService.handleFindOneCompanyById(userData.getCompany().getId());
        Role isRoleExist = roleService.handleGetRoleById(userData.getRole().getId());

        if (isCompanyExist == null) throw new IdInvalidException("Id company không tồn tại:");

        if (isEmailExist) {
            throw new IdInvalidException("Email " + userData.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }

        if (isRoleExist == null) throw new IdInvalidException("Role không tồn tại !!!!");

        String hashPassword = this.passwordEncoder.encode(userData.getPassword());

        userData.setPassword(hashPassword);
        userData.setCompany(isCompanyExist);
        userData.setRole(isRoleExist);

        User res = this.userService.handleCreateUser(userData);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(res));
    }

    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqData) throws IdInvalidException {
        Company isCompanyExist = companyService.handleFindOneCompanyById(reqData.getCompany().getId());
        Role isRoleExist = roleService.handleGetRoleById(reqData.getRole().getId());

        if (isCompanyExist == null) throw new IdInvalidException("Id company không tồn tại:");
        if (isRoleExist == null) throw new IdInvalidException("Role không tồn tại !!!!");

        reqData.setCompany(isCompanyExist);
        reqData.setRole(isRoleExist);

        User existUser = this.userService.handleUpdateUser(reqData);
        if (existUser == null) {
            throw new IdInvalidException("User với id:  " + reqData.getId() + " không tồn tại !!!!!");
        }

        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(existUser));
    }


    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        User existing = this.userService.handleFindUserById(id);

        if (existing == null) {
            throw new IdInvalidException("User với id:  " + id + " không tồn tại !!!!!");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }
}
