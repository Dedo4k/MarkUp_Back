package vlad.lailo.markup.services;

import vlad.lailo.markup.models.Data;
import vlad.lailo.markup.models.Dataset;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface DatasetService {

    List<Dataset> getLoadedDatasets();

    void loadDataset();

    void deleteDataset();

    Dataset getDatasetByName(String datasetName);

    List<Data> getDataByDatasetName(String datasetName);

    Data getDataFromDataset(String datasetName, String dataName);

    Stream<Path> getFilesFromDataset(String datasetName);

    Map<String, List<Path>> getDataMap(String datasetName);
}
