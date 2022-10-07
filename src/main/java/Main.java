import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_16;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=Og8xKPNIE5Gvvn0B8hDGFt5F1hoi2pxMxmezuf5K";
    public static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {

        WorkerWithFiles workerWithFiles = new WorkerWithFiles();

        try(CloseableHttpClient httpClient = HttpClients.createDefault()){
            final HttpUriRequest request1 =
                    new HttpGet(REMOTE_SERVICE_URI);
            try(CloseableHttpResponse response1 = httpClient.execute(request1)){
                //получаем ответ на запрос и парсим его в объект Post
                Post post = mapper.readValue(response1.getEntity().getContent(), new TypeReference<Post>() {});
                System.out.println(post);

                //получаем из объекта url и инфромацию о типе медиа файла
                final String serviceURL = post.getUrl();
                String[] url = serviceURL.split("/");
                String fileName = url[url.length-1];
                String mediaType = post.getMedia_type();

                //делаем второй запрос, чтобы получить данные
                HttpGet request2 = new HttpGet(serviceURL);


                try(CloseableHttpResponse response2 = httpClient.execute(request2)){
                    if (mediaType.equals("image")){
                        File file = new File("src/main/" + fileName);
                        System.out.println(workerWithFiles.createFile(file));

                        byte[] data = response2.getEntity().getContent().readAllBytes();
                        workerWithFiles.writeInFileBytes(file.getPath(), data);
                    } else {
                        File file = new File("src/main/" + fileName);
                        System.out.println(workerWithFiles.createFile(file));

                        String linkImage = String.format("http://img.youtube.com/vi/%s/maxresdefault.jpg", fileName);

                        HttpGet requestForPreview = new HttpGet(linkImage);
                        CloseableHttpResponse responseForPreview = httpClient.execute(requestForPreview);

                        byte[] data = responseForPreview.getEntity().getContent().readAllBytes();
                        workerWithFiles.writeInFileBytes(file.getPath(), data);

                        responseForPreview.close();
                    }
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
