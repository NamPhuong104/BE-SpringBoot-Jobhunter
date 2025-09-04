package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResCreateRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResUpdateRoleDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification spec, Pageable pageable) {
        ResultPaginationDTO res = this.roleService.handleGetAllRole(spec, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role by id")
    public ResponseEntity<ResRoleDTO> getRoleById(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Role res = this.roleService.handleGetRoleById(id);
        if (res == null) throw new IdInvalidException("Id không tồn tại !!!");
        return ResponseEntity.ok().body(this.roleService.convertRole(res));
    }

    @PostMapping("/roles")
    @ApiMessage("Create role")
    public ResponseEntity<ResCreateRoleDTO> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role res = this.roleService.handleCreateRole(role);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.convertCreateRole(res));
    }

    @PutMapping("/roles")
    @ApiMessage("Update role")
    public ResponseEntity<ResUpdateRoleDTO> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role res = this.roleService.handleUpdateRole(role);
        if (res == null) throw new IdInvalidException("Id không tồn tại !!!");
        return ResponseEntity.ok(this.roleService.convertUpdateRole(res));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Role existRole = this.roleService.handleGetRoleById(id);
        if (existRole == null) throw new IdInvalidException("id không tồn tại !!!");
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.ok(null);
    }
}
