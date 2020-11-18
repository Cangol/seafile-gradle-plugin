package mobi.cangol.plugin.seafile;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gradle.internal.impldep.org.junit.platform.commons.logging.Logger;
import org.gradle.internal.impldep.org.junit.platform.commons.logging.LoggerFactory;

import java.io.FileInputStream;
import java.nio.charset.Charset;

class UploadClient {
    private static final Logger log = LoggerFactory.getLogger(UploadClient.class);
    private UploadPluginExtension extension;
    private static UploadClient seaClient;
    static UploadClient init(UploadPluginExtension extension){
        if(seaClient==null){
            seaClient=new UploadClient(extension);
        }
        return seaClient;
    }

    public UploadClient(UploadPluginExtension extension){
        this.extension=extension;
    }

    String upload(String link,String destDir,String destFileName,String srcFilePath) {
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(link);
        try {
            httpPost.setHeader("Authorization", "Token "+extension.getToken());
            httpPost.setHeader("contentType", "multipart/form-data");
            MultipartEntity entity=new MultipartEntity();
            entity.addPart("parent_dir", new StringBody(destDir, Charset.forName("UTF-8")));
            entity.addPart("replace", new StringBody("1", Charset.forName("UTF-8")));
            entity.addPart("file",new InputStreamBody(new FileInputStream(srcFilePath),destFileName));
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                String result = response.getEntity().toString();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    String createDir(String destDir) {
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(extension.getServer()+"/api2/repos/"+extension.getRepo()+"/dir/?p="+destDir);
        try {
            httpPost.setHeader("Authorization", "Token "+extension.getToken());
            httpPost.setHeader("contentType", "application/json");
            StringEntity entity=new StringEntity("operation=mkdir","UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                String result = response.getEntity().toString();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    String getUploadLink(String destDir) {
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(extension.getServer()+"/api2/repos/"+extension.getRepo()+"/upload-link/?p="+destDir);
        try {
            httpGet.setHeader("Authorization", "Token "+extension.getToken());
            httpGet.setHeader("contentType", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                String result = response.getEntity().toString();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }
}
