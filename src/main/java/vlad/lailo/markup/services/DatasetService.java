package vlad.lailo.markup.services;

import org.springframework.web.multipart.MultipartFile;
import vlad.lailo.markup.models.Data;
import vlad.lailo.markup.models.Dataset;
import vlad.lailo.markup.models.User;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface DatasetService {

    List<Dataset> getLoadedDatasets();

    boolean uploadDataset(String datasetName, MultipartFile file);

    void loadDatasets(List<String> datasetNames, User user);

    void deleteDataset();

    Dataset getDatasetByName(String datasetName);

    List<Data> getDataByDatasetName(String datasetName);

    Data getDataFromDataset(String datasetName, String dataName);

    Data updateDataLayout(String datasetName, String dataName, LocalDateTime openedAt, LocalDateTime sendAt, MultipartFile file, User user);

    Stream<Path> getFilesFromDataset(String datasetName);

    Map<String, List<Path>> getDataMap(String datasetName);
}
