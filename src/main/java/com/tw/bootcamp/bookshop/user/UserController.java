package com.tw.bootcamp.bookshop.user;


import com.tw.bootcamp.bookshop.documentation.UserCreationDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name="User Service", description = "APIs for user service")

public class UserController {
    @Autowired
    private UserService userService;

    @UserCreationDocumentation
    @PostMapping
    ResponseEntity<UserView> create(@RequestBody CreateUserRequest userRequest) throws InvalidEmailException {
        User user = userService.create(userRequest);
        return new ResponseEntity<>(new UserView(user), HttpStatus.CREATED);
    }
}
