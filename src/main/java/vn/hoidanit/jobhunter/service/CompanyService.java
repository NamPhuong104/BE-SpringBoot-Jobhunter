package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.company.CompanyDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleGetAll(Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageCompany.getNumber());
        mt.setPageSize(pageCompany.getSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company handleFindOneCompanyById(Long id) {
        return this.companyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy company với id: " + id));
    }

    public Company handleUpdateCompany(CompanyDTO.Update company) {
        Company companyExist = this.handleFindOneCompanyById(company.getId());

        if (companyExist != null) {
            companyExist.setName(company.getName());
            companyExist.setLogo(company.getLogo());
            companyExist.setDescription(company.getDescription());
            companyExist.setAddress(company.getAddress());

            companyExist = this.companyRepository.save(companyExist);

        }
        return companyExist;
    }

    public void handleDeleteCompany(Long id) {
        Company companyExist = this.handleFindOneCompanyById(id);
        if (companyExist != null) {
            this.companyRepository.deleteById(companyExist.getId());
        }
    }
}
