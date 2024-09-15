package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Networking {

    public static Pair<Integer, String> TryConnect2_networkcheck_kde_org() {
        var url = "http://networkcheck.kde.org/";

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet(url);

            HttpClientResponseHandler<Pair<Integer, String>> responseHandler = response -> {
                int responseCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                return Pair.of(responseCode, responseBody);
            };

            return httpClient.execute(get, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Pair<String, String> extractURLAndParams(String script) {
        String urlRegex = "http://[^/]+/eportal";
        String paramsRegex = "\\?(.*)'";

        Pattern urlPattern = Pattern.compile(urlRegex);
        Matcher urlMatcher = urlPattern.matcher(script);

        Pattern paramsPattern = Pattern.compile(paramsRegex);
        Matcher paramsMatcher = paramsPattern.matcher(script);

        if (urlMatcher.find() && paramsMatcher.find()) {
            return Pair.of(urlMatcher.group(), paramsMatcher.group(1));
        } else {
            return null;
        }
    }


    public static Pair<String, String> extractRSAPublicKeyExponentAndRSAPublicKeyModulus(Pair<String, String> URLAndParams) {
        var url = URLAndParams.getLeft() + "/InterFace.do?method=pageInfo";
        var params = "queryString=" + URLAndParams.getRight();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            post.setEntity(new StringEntity(params));

            HttpClientResponseHandler<Pair<String, String>> responseHandler = response -> {
                int responseCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                var ky = mapper.readValue(responseBody, KeyInfo.class);
                if (ky != null) {
                    return Pair.of(ky.getPublicKeyExponent(), ky.getPublicKeyModulus());
                }
                return null;
            };

            return httpClient.execute(post, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String PostLoginRequest(String id, String encryptedPwc,Pair<String, String> URLAndParams){
        var url = URLAndParams.getLeft() + "/InterFace.do?method=login";
        var params = "queryString=" + URLAndParams.getRight();
        return "";
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KeyInfo {
        private String publicKeyModulus;
        private String publicKeyExponent;

        public String getPublicKeyModulus() {
            return publicKeyModulus;
        }

        public void setPublicKeyModulus(String publicKeyModulus) {
            this.publicKeyModulus = publicKeyModulus;
        }

        public String getPublicKeyExponent() {
            return publicKeyExponent;
        }

        public void setPublicKeyExponent(String publicKeyExponent) {
            this.publicKeyExponent = publicKeyExponent;
        }
    }

}
