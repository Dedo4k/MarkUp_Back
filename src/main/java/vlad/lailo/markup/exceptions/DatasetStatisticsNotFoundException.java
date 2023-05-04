package vlad.lailo.markup.exceptions;

public class DatasetStatisticsNotFoundException extends RuntimeException {

    public DatasetStatisticsNotFoundException(String datasetName, long userId) {
        super("Dataset statistics not found with datasetName: " + datasetName + " userId: " + userId);
    }
}
