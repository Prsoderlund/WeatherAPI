import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherAPIExample {
    private static final String ACCESS_KEY = "4bd29899edb6fb0f3e0e032dcc9ea7e3";
    private static final String LOCATION = "Stockholm";
    private static String apiUrl = "http://api.weatherstack.com/current?access_key=" + ACCESS_KEY + "&query=%s";

    public static String getTimezoneID(String responseJson) {
        String timezone_id = null;
        try {
            timezone_id = parseTimezoneIDFromJson(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timezone_id;
    }

    public static int getTemperature(String responseJson) {
        int temperature = 0;
        try {
            temperature = parseTemperatureFromJson(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temperature;
    }

    public static String getTime(String responseJson) {
        try {
            return parseTimeFromJson(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static WeatherResponse getWeather(String location) throws Exception {
        location = URLEncoder.encode(location);
        System.out.println("location = " + location);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format(apiUrl, location)))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("status = " + status);
        System.out.println("response = " + response.body());
        return parse(response.body());

    }


    private static String getFieldFromJson(String json, String field) {
        String regex = "\"" + field + "\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            regex = "\"" + field + "\":(\\d+)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(json);
            return matcher.find() ? matcher.group(1) : null;
        }

    }

    public static WeatherResponse parse(String json) {
        String timezoneId = WeatherAPIExample.getTimezoneID(json);
        int temperature = WeatherAPIExample.getTemperature(json);
        String time = WeatherAPIExample.getTime(json);
        return new WeatherResponse(timezoneId, time, temperature);

    }

    private static String parseTimezoneIDFromJson(String json) {
        return getFieldFromJson(json, "timezone_id");
    }

    private static int parseTemperatureFromJson(String json) {
        String value = getFieldFromJson(json, "temperature");
        return value != null ? Integer.parseInt(value) : 0;

    }

    private static String parseTimeFromJson(String json) {
        int localtime = 0;
        String timeString = getFieldFromJson(json, "localtime");
        return timeString.split(" ")[1];

    }


}
