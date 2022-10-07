import java.io.File;

public interface FileUtils {
    String createFile(File file);
    void writeInFileBytes(String fileName, byte[] data);

}
