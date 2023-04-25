package vlad.lailo.markup.utils;

import java.util.Locale;

public class FileHelper {

    public static String filenameWithoutExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public static String fileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
    }
}
