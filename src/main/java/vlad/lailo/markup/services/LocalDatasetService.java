package vlad.lailo.markup.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vlad.lailo.markup.exceptions.*;
import vlad.lailo.markup.models.*;
import vlad.lailo.markup.repository.DatasetRepository;
import vlad.lailo.markup.repository.DatasetStatisticsRepository;
import vlad.lailo.markup.repository.UserRepository;
import vlad.lailo.markup.repository.UserStatisticsRepository;
import vlad.lailo.markup.utils.FileHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class LocalDatasetService implements DatasetService {

    @Value("${datasets.storage.location}")
    private String path;

    @Value("${datasets.extensions.image}")
    private List<String> imagesExtensions;

    @Value("${datasets.extensions.layout}")
    private List<String> layoutExtensions;

    private final DatasetRepository datasetRepository;

    private final UserRepository userRepository;

    private final DatasetStatisticsRepository datasetStatisticsRepository;
    private final UserStatisticsRepository userStatisticsRepository;

    public LocalDatasetService(DatasetRepository datasetRepository,
                               UserRepository userRepository,
                               DatasetStatisticsRepository datasetStatisticsRepository,
                               UserStatisticsRepository userStatisticsRepository) {
        this.datasetRepository = datasetRepository;
        this.userRepository = userRepository;
        this.datasetStatisticsRepository = datasetStatisticsRepository;
        this.userStatisticsRepository = userStatisticsRepository;
    }

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
    public boolean uploadDataset(String datasetName, MultipartFile file) {
        Path copyTo = Paths.get(path).resolve(Objects.requireNonNull(file.getOriginalFilename()));
        if (!copyTo.toFile().exists()) {
            try {
                Files.createDirectories(copyTo.getParent());
            } catch (IOException e) {
                throw new RuntimeException("File upload failed.");
            }
        }
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, copyTo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("File upload failed.");
        }
        return true;
    }

    @Override
    public void loadDatasets(List<String> datasetNames, User user) {
        datasetNames.forEach(datasetName -> {
            Dataset dataset = datasetRepository.findById(datasetName).orElse(getDatasetByName(datasetName));
            if (datasetStatisticsRepository.findByDataset_NameAndUser_Id(datasetName, user.getId()).isEmpty()) {
                DatasetStatistic datasetStatistic = new DatasetStatistic();
                datasetStatistic.setDataset(dataset);
                datasetStatistic.setUser(user);
                datasetStatistic.setModeratingTime(Duration.ZERO);
                dataset.getDatasetStatistics().add(datasetStatistic);
            }
            if (user.getDatasets().stream().noneMatch(d -> d.getName().equals(datasetName))) {
                user.addDataset(dataset);
            }
        });
        userRepository.save(user);
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
            dataset.setCreatedAt(getDatasetCreationTime(datasetName));
            dataset.setUpdatedAt(getDatasetLastModifiedTime(datasetName));
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
    public Data updateDataLayout(String datasetName, String dataName, LocalDateTime openedAt, LocalDateTime sendAt, MultipartFile file, User user) {

        Dataset dataset = datasetRepository.findById(datasetName).orElseThrow(() -> new DatasetNotFoundException(datasetName));
        DatasetStatistic datasetStatistic = datasetStatisticsRepository.findByDataset_NameAndUser_Id(datasetName, user.getId())
                .orElseThrow(() -> new DatasetStatisticsNotFoundException(dataName, user.getId()));

        LocalDate date = LocalDate.now(ZoneOffset.UTC);
        LocalDateTime dateTime = LocalDateTime.now(ZoneOffset.UTC);
        UserStatistic userStatistic = userStatisticsRepository.findByDateAndUser_Id(date, user.getId()).orElseGet(() -> {
            UserStatistic userStat = new UserStatistic();
            userStat.setUser(user);
            userStat.setDate(date);
            userStat.setLastUpdateAt(dateTime);
            userStat.setTotalTimeWorked(Duration.ZERO);
            user.getUserStatistics().add(userStat);
            return userStat;
        });

        Path layoutPath = null;
        try {
            layoutPath = getLayoutPath(getDataMap(datasetName).get(dataName));
        } catch (DataLayoutNotFoundException e) {
            layoutPath = Paths.get(path).resolve(datasetName).resolve(Objects.requireNonNull(file.getOriginalFilename()));
        }

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, layoutPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("File update failed.");
        }

        dataset.setUpdatedAt(dateTime);
        userStatistic.setLastUpdateAt(dateTime);
        Duration duration = Duration.between(openedAt, sendAt);
        datasetStatistic.setModeratingTime(datasetStatistic.getModeratingTime()
                .plus(duration));
        userStatistic.setTotalTimeWorked(userStatistic.getTotalTimeWorked().plus(duration));
        userStatistic.setFilesChecked(userStatistic.getFilesChecked() + 1);

        datasetRepository.save(dataset);
        userStatisticsRepository.save(userStatistic);

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
        if (paths == null || paths.isEmpty()) {
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

    private LocalDateTime getDatasetCreationTime(String datasetName) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get(path).resolve(datasetName),
                    BasicFileAttributes.class);
            return LocalDateTime.ofInstant(fileAttributes.creationTime().toInstant(), ZoneOffset.UTC);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDateTime getDatasetLastModifiedTime(String datasetName) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get(path).resolve(datasetName),
                    BasicFileAttributes.class);
            return LocalDateTime.ofInstant(fileAttributes.lastModifiedTime().toInstant(), ZoneOffset.UTC);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
