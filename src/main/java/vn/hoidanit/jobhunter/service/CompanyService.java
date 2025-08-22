package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.time.Instant;
import java.util.Optional;


@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public ResCreateCompanyDTO convertToResCreateComDTO(Company company) {
        ResCreateCompanyDTO res = new ResCreateCompanyDTO();
        res.setName(company.getName());
        res.setLogo(company.getLogo());
        res.setAddress(company.getAddress());
        res.setDescription(company.getDescription());
        res.setCreatedAt(company.getCreatedAt());
        return res;
    }

    public ResUpdateCompanyDTO convertToResUpdateComDTO(Company company) {
        ResUpdateCompanyDTO res = new ResUpdateCompanyDTO();
        res.setId(company.getId());
        res.setName(company.getName());
        res.setLogo(company.getLogo());
        res.setAddress(company.getAddress());
        res.setDescription(company.getDescription());
        res.setUpdatedAt(company.getUpdatedAt());
        return res;
    }


    public ResultPaginationDTO handleGetAll(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company handleFindOneCompanyById(Long id) {
        Optional<Company> res = this.companyRepository.findById(id);
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Company handleUpdateCompany(Company company) {
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
        this.companyRepository.deleteById(id);
    }
}
