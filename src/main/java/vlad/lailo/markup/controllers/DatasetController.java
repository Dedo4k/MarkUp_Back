package vlad.lailo.markup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vlad.lailo.markup.models.Data;
import vlad.lailo.markup.models.User;
import vlad.lailo.markup.models.dto.DatasetDto;
import vlad.lailo.markup.services.DatasetService;
import vlad.lailo.markup.services.StorageService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v2/datasets")
public class DatasetController {

    private final StorageService storageService;
    private final DatasetService datasetService;

    @Autowired
    public DatasetController(@Qualifier("localStorageService") StorageService storageService,
                             @Qualifier("localDatasetService") DatasetService datasetService) {
        this.storageService = storageService;
        this.datasetService = datasetService;
    }

    @GetMapping
    public ResponseEntity<List<DatasetDto>> getLoadedDatasets() {
        return ResponseEntity.ok(datasetService.getLoadedDatasets().stream().map(DatasetDto::fromModel).toList());
    }

    @GetMapping("/{datasetName}/names")
    public ResponseEntity<List<String>> getDatasetNames(@PathVariable String datasetName) {
        return ResponseEntity.ok(datasetService.getDataMap(datasetName).keySet().stream().sorted().toList());
    }

    @GetMapping("/{datasetName}/{dataName}")
    public ResponseEntity<Data> getDataFromDataset(@PathVariable String datasetName,
                                                   @PathVariable String dataName) {
        return ResponseEntity.ok(datasetService.getDataFromDataset(datasetName, dataName));
    }

    @PostMapping("/{datasetName}/{dataName}")
    public ResponseEntity<Data> updateDataFromDataset(@PathVariable String datasetName,
                                                      @PathVariable String dataName,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime openedAt,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sendAt,
                                                      @RequestParam MultipartFile file,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(datasetService.updateDataLayout(datasetName, dataName, openedAt, sendAt, file, user));
    }

    @PutMapping("/load")
    public ResponseEntity<List<DatasetDto>> loadDatasets(@RequestBody List<String> datasetNames,
                                                         @AuthenticationPrincipal User user) {
        datasetService.loadDatasets(datasetNames, user);
        return ResponseEntity.ok(user.getDatasets().stream().map(DatasetDto::fromModel).toList());
    }
}
