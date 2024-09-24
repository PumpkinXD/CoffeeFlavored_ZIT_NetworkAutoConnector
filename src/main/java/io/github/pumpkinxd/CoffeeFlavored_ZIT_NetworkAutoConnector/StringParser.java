package io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector;

import org.apache.commons.lang3.tuple.Pair;

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
}
