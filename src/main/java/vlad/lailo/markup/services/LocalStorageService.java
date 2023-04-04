package vlad.lailo.markup.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class LocalStorageService implements StorageService {

    @Override
    public List<String> getFilesList(String path) {
        return null;
    }

    @Override
    public File downloadFile(String path) {
        return null;
    }

    @Override
    public boolean uploadFile(File file, String path) {
        return false;
    }
}
