package org.acabativa.rc.patrimonio.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SimpleHttpClient {

    private static final Logger LOGGER = Logger.getLogger(SimpleHttpClient.class.getName());

    private final String contentType;
    private final String authorization;
    private final String openAiBeta = "assistants=v2";
    private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    public SimpleHttpClient(String contentType, String authorization) {
        this.contentType = contentType;
        this.authorization = authorization;
    }

    public String fetchHttpResponse(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            
            HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<>() {
                @Override
                public String handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
                    int status = response.getCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new IOException("Unexpected response status: " + status);
                    }
                }
            };
            
            LOGGER.info("Sending request to: " + url);
            String responseBody = httpClient.execute(request, responseHandler);
            LOGGER.info("Received response successfully");
            return responseBody;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Request to " + url + " failed", e);
            return null;
        }
    }

    public String sendPostRequest(String url, Object data) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.addHeader("User-Agent", this.userAgent);
            request.addHeader("Content-Type", this.contentType);
            request.addHeader("Authorization", this.authorization);
            request.addHeader("OpenAI-Beta", this.openAiBeta);
            
            if( data != null ){
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(data);
                LOGGER.info("Adding json to request: " + json);
                request.setEntity(new StringEntity(json));
            }
            
            HttpClientResponseHandler<String> responseHandler = response -> {
                int status = response.getCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new IOException("Unexpected response status: " + status);
                }
            };
            
            LOGGER.info("Sending POST request to: " + url);
            String responseBody = httpClient.execute(request, responseHandler);
            LOGGER.info("Received response successfully");
            return responseBody;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "POST request to " + url + " failed", e);
            return null;
        }
    }

    public String sendGetRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("User-Agent", this.userAgent);
            request.addHeader("Content-Type", this.contentType);
            request.addHeader("Authorization", this.authorization);
            request.addHeader("OpenAI-Beta", this.openAiBeta);
            
            HttpClientResponseHandler<String> responseHandler = response -> {
                int status = response.getCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new IOException("Unexpected response status: " + status);
                }
            };
            
            LOGGER.info("Sending POST request to: " + url);
            String responseBody = httpClient.execute(request, responseHandler);
            LOGGER.info("Received response successfully");
            return responseBody;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "POST request to " + url + " failed", e);
            return null;
        }
    }

    

}
