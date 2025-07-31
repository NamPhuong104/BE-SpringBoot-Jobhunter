package vn.hoidanit.jobhunter.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public List<User> handleGetAllUser() {
        return this.userRepository.findAll();
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
}
