package vn.io.sontd.learning.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.entity.UserEntity;
import vn.io.sontd.learning.server.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @GetMapping
    public List<UserEntity> test() {
        return userRepository.findAll();
    }
}
