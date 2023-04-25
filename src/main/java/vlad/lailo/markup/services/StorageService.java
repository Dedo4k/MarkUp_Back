package vlad.lailo.markup.services;

import java.io.File;
import java.util.List;

public interface StorageService {

    List<String> getFilesList(String path);

    File downloadFile(String path);

    boolean uploadFile(File file, String path);
}
