package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.company.CompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.CompanyMapper;
import vn.hoidanit.jobhunter.service.CompanyService;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@RequestParam("current") Optional<String> currentOptional, @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "1";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "10";

        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        ResultPaginationDTO res = this.companyService.handleGetAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> GetCompanyById(@Validated @PathVariable Long id) {
        return ResponseEntity.ok().body(this.companyService.handleFindOneCompanyById(id));
    }


    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Validated @RequestBody CompanyDTO.Create createData) {
        Company entity = companyMapper.toEntity(createData);
        Company res = companyService.handleCreateCompany(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Validated @RequestBody CompanyDTO.Update updateData) {
        Company entity = companyMapper.toEntity(updateData);
        Company res = companyService.handleUpdateCompany(updateData);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.handleDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
