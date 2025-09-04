package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResCreateRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResRoleDTO;
import vn.hoidanit.jobhunter.domain.response.role.ResUpdateRoleDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public ResRoleDTO convertRole(Role r) {
        ResRoleDTO res = new ResRoleDTO();
        res.setId(r.getId());
        res.setName(r.getName());
        res.setDescription(r.getDescription());
        res.setActive(r.isActive());
        res.setCreatedAt(r.getCreatedAt());
        res.setUpdatedAt(r.getUpdatedAt());
        if (r != null) {
            List<ResRoleDTO.Permission> listPer = r.getPermissions().stream().map(p -> new ResRoleDTO.Permission(p.getId(), p.getName())).collect(Collectors.toList());
            res.setPermissions(listPer);
        }
        return res;
    }

    public ResCreateRoleDTO convertCreateRole(Role r) {
        ResCreateRoleDTO res = new ResCreateRoleDTO();
        res.setId(r.getId());
        res.setName(r.getName());
        res.setDescription(r.getDescription());
        res.setActive(r.isActive());
        res.setCreatedAt(r.getCreatedAt());
        if (r != null) {
            List<ResCreateRoleDTO.Permission> listPer = r.getPermissions().stream().map(p -> new ResCreateRoleDTO.Permission(p.getId(), p.getName())).collect(Collectors.toList());
            res.setPermissions(listPer);
        }
        return res;
    }

    public ResUpdateRoleDTO convertUpdateRole(Role r) {
        ResUpdateRoleDTO res = new ResUpdateRoleDTO();
        res.setId(r.getId());
        res.setName(r.getName());
        res.setDescription(r.getDescription());
        res.setActive(r.isActive());
        res.setUpdatedAt(r.getUpdatedAt());
        if (r != null) {
            List<ResUpdateRoleDTO.Permission> listPer = r.getPermissions().stream().map(p -> new ResUpdateRoleDTO.Permission(p.getId(), p.getName())).collect(Collectors.toList());
            res.setPermissions(listPer);
        }
        return res;
    }

    public ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> roles = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setTotal(roles.getTotalElements());
        mt.setPages(roles.getTotalPages());

        Page<ResRoleDTO> dtoPage = roles.map(this::convertRole);
        List<ResRoleDTO> dtoList = dtoPage.getContent();
        rs.setResult(dtoList);

        rs.setMeta(mt);
        return rs;
    }

    public Role handleGetRoleById(Long id) {
        Optional<Role> res = this.roleRepository.findById(id);
        if (res.isPresent()) return res.get();
        return null;
    }

    public Role handleCreateRole(Role r) throws IdInvalidException {
        if (roleRepository.existsByName(r.getName())) throw new IdInvalidException("Tên đã tồn tại !!!");
        if (r.getPermissions() != null) {
            List<Long> listPer = r.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPer = this.permissionRepository.findByIdIn(listPer);
            r.setPermissions(dbPer);
        }
        return this.roleRepository.save(r);
    }

    public Role handleUpdateRole(Role r) {
        Role res = this.handleGetRoleById(r.getId());
        if (res == null) return null;
        res.setName(r.getName());
        res.setDescription(r.getDescription());
        res.setActive(r.isActive());

        if (r.getPermissions() != null) {
            List<Long> perIds = r.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> listPer = this.permissionRepository.findByIdIn(perIds);
            res.setPermissions(listPer);
        }
        return this.roleRepository.save(res);
    }

    public void handleDeleteRole(Long id) {
        this.roleRepository.deleteById(id);
    }
}
