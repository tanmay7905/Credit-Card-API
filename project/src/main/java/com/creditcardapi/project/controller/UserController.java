package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shepherdmoney.interviewproject.model.User;

@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        // Create a new User object and populate it with information from the payload
        User user = new User();
        user.setName(payload.getName());
        user.setEmail(payload.getEmail());

        // Save the newly created user to the database
        userRepository.save(user);

        // Return a response with the ID of the newly created user
        return ResponseEntity.ok(user.getId());
    }   
       

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        // Retrieve the user from the repository based on the provided userId
        User user = userRepository.findById(userId).orElse(null);

        // Check if the user exists
        if (user == null) {
            // Return a bad request response with an error message if the user is not found
            return ResponseEntity.badRequest().body("User not found");
        }

        // Delete the user from the repository
        userRepository.delete(user);

        // Return a success response indicating that the user has been deleted
        return ResponseEntity.ok("User deleted successfully");
    }
       
}
