package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.company.CompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.CompanyMapper;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping("/companies")
    @ApiMessage("Get all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<Company> spec, Pageable pageable) {
        ResultPaginationDTO res = this.companyService.handleGetAll(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @GetMapping("/companies/{id}")
    @ApiMessage("Get company by id")
    public ResponseEntity<Company> GetCompanyById(@Validated @PathVariable Long id) {
        return ResponseEntity.ok().body(this.companyService.handleFindOneCompanyById(id));
    }


    @PostMapping("/companies")
    @ApiMessage("Create company")
    public ResponseEntity<Company> createNewCompany(@Validated @RequestBody CompanyDTO.Create createData) {
        Company entity = companyMapper.toEntity(createData);
        Company res = companyService.handleCreateCompany(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/companies")
    @ApiMessage("Update company")
    public ResponseEntity<Company> updateCompany(@Validated @RequestBody CompanyDTO.Update updateData) {
        Company entity = companyMapper.toEntity(updateData);
        Company res = companyService.handleUpdateCompany(updateData);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.handleDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
