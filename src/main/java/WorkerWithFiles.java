import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorkerWithFiles implements FileUtils{
    @Override
    public String createFile(File file) {
        try {
            if (file.createNewFile()) {
                return String.format("Был создан файл %s в директории %s \n", file.getName(), file.getParent());
            } else {
                return String.format("Не получилось создать файл %s \n", file.getName());
            }
        } catch (IOException exception) {
            return exception.getMessage();
        }
    }

    @Override
    public void writeInFileBytes(String fileName, byte[] data) {
        try(BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(fileName))){
            bw.write(data);
            bw.flush();
        } catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }
}
