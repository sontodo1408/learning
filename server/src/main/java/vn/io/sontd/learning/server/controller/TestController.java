package vn.io.sontd.learning.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.repository.UserRepository;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;

/**
 * Scratch endpoint used to sanity-check the database connection and repository layer.
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController extends BaseController {
    private final UserRepository userRepository;

    /**
     * Returns every row in the {@code users} table.
     */
    @GetMapping
    public ResponseRoot test() {
        return success(new ResponseData<>(userRepository.findAll()));
    }
}
