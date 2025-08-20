package vn.hoidanit.jobhunter.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.hoidanit.jobhunter.domain.response.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.user.UserMapper;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CompanyService companyService;


    public UserService(UserRepository userRepository, UserMapper userMapper, CompanyService companyService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.companyService = companyService;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());

        return res;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());

        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());

        return res;
    }

    public User handleFindUserById(long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public User handleFindUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy email: " + email));
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        List<ResUserDTO> listUser = pageUser.getContent().stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getEmail(),
                item.getName(),
                item.getGender(),
                item.getAddress(),
                item.getAge(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        )).collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
    }

    public User handleCreateUser(User userDto) {
        return userRepository.save(userDto);
    }

    public User handleUpdateUser(User reqData) {
        User user = this.handleFindUserById(reqData.getId());
        if (user != null) {
            user.setAddress(reqData.getAddress());
            user.setGender(reqData.getGender());
            user.setAge(reqData.getAge());
            user.setName(reqData.getName());
        }

        user = this.userRepository.save(user);
        return user;
    }


    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public void handleUpdateUserToken(String refresh_token, String email) {
        User user = this.userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK));
        user.setRefreshToken(refresh_token);
        this.userRepository.save(user);
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
