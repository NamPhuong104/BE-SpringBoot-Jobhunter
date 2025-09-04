package vn.hoidanit.jobhunter.service;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleFindById(long id) {
        Optional<Permission> currentPer = this.permissionRepository.findById(id);
        if (currentPer.isPresent()) return currentPer.get();
        return null;
    }

    public ResultPaginationDTO handleGetAllPer(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> perPage = this.permissionRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(perPage.getTotalPages());
        mt.setTotal(perPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(perPage.get());
        return rs;
    }

    public Permission handleCreatePermission(Permission p) throws IdInvalidException {
        boolean isExistPer = this.permissionRepository.existsByModuleAndApiPathAndMethod(p.getModule(), p.getApiPath(), p.getMethod());
        if (isExistPer) throw new IdInvalidException("Permission đã tồn tại");

        return this.permissionRepository.save(p);
    }

    public Permission handleUpdatePermission(Permission p) throws IdInvalidException {
        Permission existPer = this.handleFindById(p.getId());
        if (existPer == null) throw new IdInvalidException("Permisstion không tồn tại");

        boolean isExistPer = this.permissionRepository.existsByModuleAndApiPathAndMethod(p.getModule(), p.getApiPath(), p.getMethod());
        if (isExistPer) {
            throw new IdInvalidException("Permission đã tồn tại");
        } else {
            existPer.setName(p.getName());
            existPer.setModule(p.getModule());
            existPer.setMethod(p.getMethod());
            existPer.setApiPath(p.getApiPath());

            Permission res = this.permissionRepository.save(existPer);
            return res;
        }
    }

    public void handleDeletePermission(Long id) {
        Permission currentPer = this.handleFindById(id);
        if (currentPer != null) {
            currentPer.getRole().forEach(role -> role.getPermissions().remove(currentPer));
            this.permissionRepository.deleteById(currentPer.getId());
        }

    }
}
