package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public ResCompanyDTO convertToResCompanyDTO(Company company) {
        ResCompanyDTO res = new ResCompanyDTO();
        res.setId(company.getId());
        res.setLogo(company.getLogo());
        res.setDescription(company.getDescription());
        res.setName(company.getName());
        res.setAddress(company.getAddress());
        res.setCreatedAt(company.getCreatedAt());
        res.setUpdatedAt(company.getUpdatedAt());

        if (company.getUsers() != null) {
            List<ResCompanyDTO.UserSummaryDTO> userSummaryDTO = company.getUsers().stream().map(user -> new ResCompanyDTO.UserSummaryDTO(user.getId(), user.getName(), user.getEmail())).collect(Collectors.toList());
            res.setUsers(userSummaryDTO);
        }
        return res;
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
        rs.setResult(pageCompany);
//        List<ResCompanyDTO> listCompanyDTO = pageCompany.getContent().stream().map(this::convertToResCompanyDTO).collect(Collectors.toList());

        rs.setResult(pageCompany.get());

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
        Company currentCompany = this.handleFindOneCompanyById(id);
        if (currentCompany != null) {
            List<User> userList = this.userRepository.findUserByCompany(currentCompany);
            this.userRepository.deleteAll(userList);
        }
        this.companyRepository.deleteById(id);
    }
}
