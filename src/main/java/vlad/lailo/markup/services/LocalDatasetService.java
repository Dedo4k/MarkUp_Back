package vlad.lailo.markup.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vlad.lailo.markup.exceptions.StorageNotFoundException;
import vlad.lailo.markup.models.Dataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocalDatasetService implements DatasetService {

    @Value("${datasets.storage.location}")
    private String path;


    @Override
    public List<Dataset> getLoadedDatasets() {
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            return stream
                    .filter(f -> f.toFile().isDirectory())
                    .map(f -> {
                        Dataset dataset = new Dataset();
                        dataset.setName(f.getFileName().toString());
                        return dataset;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageNotFoundException(path);
        }
    }

    @Override
    public void loadDataset() {

    }

    @Override
    public void deleteDataset() {

    }

    @Override
    public Dataset getDatasetByName(String datasetName) {
        return null;
    }
}
