package vlad.lailo.markup.services;

import vlad.lailo.markup.models.Dataset;

import java.util.List;

public interface DatasetService {

    List<Dataset> getLoadedDatasets();

    void loadDataset();

    void deleteDataset();

    Dataset getDatasetByName(String datasetName);
}
