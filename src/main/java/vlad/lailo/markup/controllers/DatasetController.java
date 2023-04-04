package vlad.lailo.markup.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vlad.lailo.markup.services.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class DatasetController {

    private final StorageService storageService;

    @Autowired
    public DatasetController(@Qualifier("localStorageService") StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test");
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getFiles(@PathParam("filename") String filename) throws IOException {
        List<String> paths = Files.list(Path.of(filename)).map(Path::toString).toList();
        return ResponseEntity.ok(paths);
    }
}
