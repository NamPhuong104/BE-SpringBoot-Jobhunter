package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("Get permission by id")
    public ResponseEntity<Permission> getPermissionById(@PathVariable("id") Long id) throws IdInvalidException {
        Permission res = this.permissionService.handleFindById(id);
        if (res == null) throw new IdInvalidException("Permission không tồn tại");
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/permissions")
    @ApiMessage("Get all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPer(@Filter Specification spec, Pageable pageable) {
        ResultPaginationDTO res = this.permissionService.handleGetAllPer(spec, pageable);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission reqBody) throws IdInvalidException {
        Permission res = this.permissionService.handleCreatePermission(reqBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission reqBody) throws IdInvalidException {
        Permission res = this.permissionService.handleUpdatePermission(reqBody);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id) throws IdInvalidException {
        Permission existPer = this.permissionService.handleFindById(id);
        if (existPer == null) throw new IdInvalidException("Id không tồn tại !!!");

        this.permissionService.handleDeletePermission(id);
        return ResponseEntity.ok(null);
    }

}
