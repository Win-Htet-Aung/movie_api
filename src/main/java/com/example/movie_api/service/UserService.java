package com.example.movie_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.movie_api.model.Review;
import com.example.movie_api.model.Role;
import com.example.movie_api.model.User;
import com.example.movie_api.repository.RoleRepository;
import com.example.movie_api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReviewService reviewService;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public User createUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword("{bcrypt}" + encoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("user");
        user.setRole(role);
        return userRepository.save(user);
    }

    public void updateUser(Long id, User updateUser, String username) {
        User loggedInUser = userRepository.findByUsername(username);
        User user = getUserById(id);
        if (loggedInUser.getRole().getName().equals("admin")) {
            if (user.getRole().getName().equals("admin") && loggedInUser.getId() != user.getId()) {
                throw new SecurityException("Admin cannot update other admin");
            }
        } else if (loggedInUser.getId() != user.getId()) {
            throw new SecurityException("User can only update own account");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        updateUser.setPassword("{bcrypt}" + encoder.encode(updateUser.getPassword()));
        if (updateUser.getRole() == null) {
            updateUser.setRole(user.getRole());
        }
        if (updateUser.getReviews() == null) {
            updateUser.setReviews(user.getReviews());
        }
        updateUser.setId(user.getId());
        userRepository.save(updateUser);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        for (Review review : user.getReviews()) {
            reviewService.deleteReview(review.getId());
        }
        userRepository.deleteById(id);
    }
}
