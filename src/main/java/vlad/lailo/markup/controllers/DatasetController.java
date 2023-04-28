package vlad.lailo.markup.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vlad.lailo.markup.models.Data;
import vlad.lailo.markup.models.Dataset;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.services.DatasetService;
import vlad.lailo.markup.services.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @GetMapping("/list")
    public ResponseEntity<List<String>> getFiles(@PathParam("filename") String filename) throws IOException {
        List<String> paths = Files.list(Path.of(filename)).map(Path::toString).toList();
        return ResponseEntity.ok(paths);
    }

    @GetMapping("/datasets")
    public ResponseEntity<List<Dataset>> getLoadedDatasets() {
        return ResponseEntity.ok(datasetService.getLoadedDatasets());
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

    @PostMapping("/datasets/{datasetName}/{dataName}")
    public ResponseEntity<Data> updateDataFromDataset(@PathVariable String datasetName,
                                                      @PathVariable String dataName,
                                                      @RequestParam("file") MultipartFile layout) {
        return ResponseEntity.ok(datasetService.updateDataLayout(datasetName, dataName, layout));
    }

    @GetMapping("/datasets/load")
    public ResponseEntity<List<Dataset>> loadDatasets(@RequestParam List<String> datasetNames,
                                                      @AuthenticationPrincipal User user) {
        datasetService.loadDatasets(datasetNames, user);
        return ResponseEntity.ok(user.getDatasets());
    }
}
