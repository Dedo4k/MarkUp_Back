package vlad.lailo.markup.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlad.lailo.markup.models.Data;
import vlad.lailo.markup.models.Dataset;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.services.DatasetService;
import vlad.lailo.markup.services.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
public class DatasetController {

    private final StorageService storageService;
    private final DatasetService datasetService;

    @Autowired
    public DatasetController(@Qualifier("localStorageService") StorageService storageService,
                             @Qualifier("localDatasetService") DatasetService datasetService) {
        this.storageService = storageService;
        this.datasetService = datasetService;
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

    @GetMapping("/datasets")
    public ResponseEntity<List<Dataset>> getLoadedDatasets() {
        return ResponseEntity.ok(datasetService.getLoadedDatasets());
    }

    @GetMapping("/datasets/available")
    public ResponseEntity<List<Dataset>> getAvailableDatasets(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/datasets/{datasetName}/size")
    public ResponseEntity<Integer> getDatasetSize(@PathVariable String datasetName) {
        return ResponseEntity.ok(datasetService.getDataMap(datasetName).keySet().size());
    }

    @GetMapping("/datasets/{datasetName}")
    public ResponseEntity<List<Data>> getDataset(@PathVariable String datasetName) {
        return ResponseEntity.ok(datasetService.getDataByDatasetName(datasetName).subList(0, 100));
    }

    @GetMapping("/datasets/{datasetName}/names")
    public ResponseEntity<List<String>> getDatasetNames(@PathVariable String datasetName) {
        return ResponseEntity.ok(datasetService.getDataMap(datasetName).keySet().stream().sorted().toList());
    }

    @GetMapping("/datasets/{datasetName}/{dataName}")
    public ResponseEntity<Data> getDataFromDataset(@PathVariable String datasetName,
                                                   @PathVariable String dataName) {
        return ResponseEntity.ok(datasetService.getDataFromDataset(datasetName, dataName));
    }
}
