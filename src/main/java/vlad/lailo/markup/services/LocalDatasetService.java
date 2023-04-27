package vlad.lailo.markup.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vlad.lailo.markup.exceptions.DataImageNotFoundException;
import vlad.lailo.markup.exceptions.DataLayoutNotFoundException;
import vlad.lailo.markup.exceptions.DatasetNotFoundException;
import vlad.lailo.markup.exceptions.StorageNotFoundException;
import vlad.lailo.markup.models.Data;
import vlad.lailo.markup.models.Dataset;
import vlad.lailo.markup.utils.FileHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocalDatasetService implements DatasetService {

    @Value("${datasets.storage.location}")
    private String path;

    @Value("${datasets.extensions.image}")
    private List<String> imagesExtensions;

    @Value("${datasets.extensions.layout}")
    private List<String> layoutExtensions;

    @Override
    public List<Dataset> getLoadedDatasets() {
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            return stream
                    .filter(f -> f.toFile().isDirectory())
                    .map(f -> getDatasetByName(f.getFileName().toString()))
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
        Dataset dataset = new Dataset();
        dataset.setName(datasetName);
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get(path).resolve(datasetName),
                    BasicFileAttributes.class);
            dataset.setCreatedAt(LocalDateTime.ofInstant(fileAttributes.creationTime().toInstant(), ZoneId.systemDefault()));
            dataset.setUpdatedAt(LocalDateTime.ofInstant(fileAttributes.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataset;
    }

    @Override
    public List<Data> getDataByDatasetName(String datasetName) {
        List<Data> dataList = new ArrayList<>();
        for (Map.Entry<String, List<Path>> entry : getDataMap(datasetName).entrySet()) {
            try {
                Data data = buildData(datasetName, entry.getKey(), entry.getValue());
                dataList.add(data);
            } catch (DataImageNotFoundException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return dataList;
    }

    @Override
    public Data getDataFromDataset(String datasetName, String dataName) {
        try {
            return buildData(datasetName, dataName, getDataMap(datasetName).get(dataName));
        } catch (DataImageNotFoundException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Data updateDataLayout(String datasetName, String dataName, MultipartFile layout) {
        Path layoutPath = null;
        try {
            layoutPath = getLayoutPath(getDataMap(datasetName).get(dataName));
        } catch (DataLayoutNotFoundException e) {
            layoutPath = Paths.get(path).resolve(datasetName).resolve(Objects.requireNonNull(layout.getOriginalFilename()));
        }

        try (InputStream is = layout.getInputStream()) {
            Files.copy(is, layoutPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("File update failed.");
        }

        return getDataFromDataset(datasetName, dataName);
    }

    @Override
    public Stream<Path> getFilesFromDataset(String datasetName) {
        try {
            return Files.list(Paths.get(path).resolve(datasetName));
        } catch (IOException e) {
            throw new DatasetNotFoundException(datasetName);
        }
    }

    @Override
    public Map<String, List<Path>> getDataMap(String datasetName) {
        try (Stream<Path> stream = getFilesFromDataset(datasetName)) {
            return stream.collect(Collectors
                    .groupingBy(path -> FileHelper.filenameWithoutExtension(path.getFileName().toString())));
        }
    }

    private Data buildData(String datasetName, String dataName, List<Path> paths) throws DataImageNotFoundException {
        if (paths == null || paths.isEmpty()){
            throw new DataImageNotFoundException(dataName);
        }
        Data data = new Data();
        data.setDatasetName(datasetName);
        data.setDataName(dataName);
        try {
            Path image = getImagePath(paths);
            data.setImageName(image.getFileName().toString());
            data.setImageBytes(Files.readAllBytes(image));
        } catch (IOException ex) {
            throw new DataImageNotFoundException(dataName);
        }
        try {
            Path layout = getLayoutPath(paths);
            try (Stream<String> lines = Files.lines(layout)) {
                data.setLayoutType(FileHelper.fileExtension(layout.toString()));
                data.setLayoutName(layout.getFileName().toString());
                data.setLayout(lines.collect(Collectors.joining("\n")));
            }
        } catch (IOException e) {
            System.err.println(new DataLayoutNotFoundException(dataName).getMessage());
        }
        return data;
    }

    private Path getImagePath(List<Path> paths) throws DataImageNotFoundException {
        return paths.stream()
                .filter(path -> imagesExtensions.contains(FileHelper.fileExtension(path.toString())))
                .findFirst()
                .orElseThrow(DataImageNotFoundException::new);
    }

    private Path getLayoutPath(List<Path> paths) throws DataLayoutNotFoundException {
        return paths.stream()
                .filter(path -> layoutExtensions.contains(FileHelper.fileExtension(path.toString())))
                .findFirst()
                .orElseThrow(DataLayoutNotFoundException::new);
    }
}
