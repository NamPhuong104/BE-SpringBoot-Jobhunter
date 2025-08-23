package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    @ApiMessage("Get all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<Company> spec, Pageable pageable) {
        ResultPaginationDTO res = this.companyService.handleGetAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @GetMapping("/companies/{id}")
    @ApiMessage("Get company by id")
    public ResponseEntity<Company> GetCompanyById(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Company res = this.companyService.handleFindOneCompanyById(id);
        if (res == null) {
            throw new IdInvalidException("Công ty với id: " + id + " Không tồn tại");
        }
        return ResponseEntity.ok().body(res);
    }


    @PostMapping("/companies")
    @ApiMessage("Create company")
    public ResponseEntity<ResCreateCompanyDTO> createNewCompany(@Valid @RequestBody Company createData) {
        Company res = companyService.handleCreateCompany(createData);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.convertToResCreateComDTO(res));
    }

    @PutMapping("/companies")
    @ApiMessage("Update company")
    public ResponseEntity<ResUpdateCompanyDTO> updateCompany(@Valid @RequestBody Company updateData) throws IdInvalidException {
        Company res = companyService.handleUpdateCompany(updateData);
        if (res == null) {
            throw new IdInvalidException("Công ty không tồn tại với id: " + updateData.getId());
        }
        return ResponseEntity.ok(this.companyService.convertToResUpdateComDTO(res));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) throws IdInvalidException {
        Company company = this.companyService.handleFindOneCompanyById(id);
        if (company == null) throw new IdInvalidException("Id không tồn tại: " + id);
        companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }
}
