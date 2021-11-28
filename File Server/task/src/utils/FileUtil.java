package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {


    public static void saveDownloadedFile(String path, String fileName, byte[] content) throws IOException {
        if (fileName.length() == 0) {
            fileName = generateFileName();
        }

        File file = new File(path + File.separator + fileName);

        Files.write(file.toPath(),content);//TODO checking if file already exists
    }

    private static String generateFileName() {
        return "file.txt";//TODO default file name generation
    }

    public static byte[] getFile(String path, String fileName) throws IOException {
        File file = new File(path + File.separator + fileName);
        return  Files.readAllBytes(file.toPath());
    }

    public static boolean isFileExist(String fileName, String path ) {
        File file = new File(path + File.separator + fileName);
        return file.exists();

    }

    public static boolean fileHasContent(String fileName, String path) {
        File file = new File(path + File.separator + fileName);
        return file.length() > 0;
    }
}
