package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface StringParser {

    public static String getMacFromQueryString(String queryString) {
        String pattern = "mac=([^&]*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(queryString);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    public static Pair<String, String> extractURLAndQueryString(String script) {
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

    public static Triple<String, String, String> parseLoginRespondString(String loginRespondString) {
        if (loginRespondString != null && !loginRespondString.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            loginResponse logininfo = null;
            try {
                logininfo = mapper.readValue(loginRespondString, loginResponse.class);
            } catch (JsonProcessingException ignore) {
                return null;
            }
            if (logininfo != null) {
                return Triple.of(logininfo.getUserIndex(), logininfo.getResult(), logininfo.getMessage());
            }
            return null;
        } else return null;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    class loginResponse {
        private String result;
        private String userIndex;
        private String message;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getUserIndex() {
            return userIndex;
        }

        public void setUserIndex(String userIndex) {
            this.userIndex = userIndex;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
