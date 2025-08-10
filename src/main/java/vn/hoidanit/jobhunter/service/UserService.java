package vn.hoidanit.jobhunter.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        User isUserExist = this.userRepository.findUserByEmail(user.getEmail());
//        if (isUserExist.isPresent()) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "The user with email " + user.getEmail() + " already exists."
//            );
//        } else {
        String rawPassword = user.getPassword();
        String hashed = this.passwordEncoder.encode(rawPassword);

        user.setPassword(hashed);
        return this.userRepository.save(user);

//        }

    }

    public ResultPaginationDTO handleGetAllUser(Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPages(pageUser.getNumber());
        mt.setPageSize(pageUser.getSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
    }

    public User handleGetUserById(long id
    ) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;

    }

    public User handleUpdateUser(User reqData) {
        User currentUser = this.handleGetUserById(reqData.getId());

        if (currentUser != null) {
            currentUser.setEmail(reqData.getEmail());
            currentUser.setName(reqData.getName());
            currentUser.setPassword(reqData.getPassword());

            currentUser = this.userRepository.save(currentUser);

        }
        return currentUser;
    }

    public void handleDeleteUser(long id) {

        this.userRepository.deleteById(id);
    }

    public User handleFindUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }
}
