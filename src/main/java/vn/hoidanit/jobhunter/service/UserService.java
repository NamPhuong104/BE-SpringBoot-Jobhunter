package vn.hoidanit.jobhunter.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.UserResponseDTO;
import vn.hoidanit.jobhunter.domain.dto.user.UserDTO;
import vn.hoidanit.jobhunter.domain.dto.user.UserMapper;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
    }

    public UserResponseDTO handleCreateUser(UserDTO.Create userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email đã tồn tại");
        }

        String rawPassword = userDto.getPassword();
        String hashed = this.passwordEncoder.encode(rawPassword);

        userDto.setPassword(hashed);

        User entity = userMapper.toEntity(userDto);

        User saved = userRepository.save(entity);

        UserResponseDTO resp = userMapper.toUserResponseDTO(saved);

        return resp;
    }

    public UserResponseDTO handleUpdateUser(UserDTO.Update reqData) {
        User existing = this.handleFindUserById(reqData.getId());

        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại !!!!!");

        }

        existing.setGender(GenderEnum.valueOf(reqData.getGender()));
        existing.setName(reqData.getName());
        existing.setAddress(reqData.getAddress());
        existing.setAge(Integer.parseInt(reqData.getAge()));

        existing = this.userRepository.save(existing);

        UserResponseDTO resp = userMapper.toUserResponseDTO(existing);
        resp.setEmail(null);
        resp.setCreatedAt(null);
        return resp;
    }


    public void handleDeleteUser(Long id) {
        User existing = this.handleFindUserById(id);

        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại !!!!!");

        }
        this.userRepository.deleteById(id);
    }

}
