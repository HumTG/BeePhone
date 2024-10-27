package org.example.beephone.controller.api;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final Path root = Paths.get("src/main/resources/static/admin/images");

    @GetMapping
    public List<String> listImages() throws IOException {
        return Files.walk(root, 1)
                .filter(path -> !path.equals(root))
                .map(root::relativize)
                .map(Path::toString)
                .collect(Collectors.toList());
    }
}
