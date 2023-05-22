import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherAPIExample {
    private static final String ACCESS_KEY = "4bd29899edb6fb0f3e0e032dcc9ea7e3";
    private static final String LOCATION = "Stockholm";

    public static String getTimezoneID() {
        String timezone_id = null;
        try {
            String apiUrl = "http://api.weatherstack.com/current?access_key=" + ACCESS_KEY + "&query=" + LOCATION;
            String responseJson = getJsonResponse(apiUrl);
            timezone_id = parseTimezoneIDFromJson(responseJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timezone_id;
    }

    public static int getTemperature() {
        int temperature = 0;
        try {
            String apiUrl = "http://api.weatherstack.com/current?access_key=" + ACCESS_KEY + "&query=" + LOCATION;
            String responseJson = getJsonResponse(apiUrl);
            System.out.println("responseJson = " + responseJson);
            temperature = parseTemperatureFromJson(responseJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temperature;
    }

    public static int getTime() {
        int localtime = 0;
        try {
            String apiUrl = "http://api.weatherstack.com/current?access_key=" + ACCESS_KEY + "&query=" + LOCATION;
            String responseJson = getResponse();

            localtime = parseTimeFromJson(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localtime;
    }
    static String getResponse() throws Exception {

        String Url = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(Url))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();


    }



    private static String getJsonResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } else {
            System.out.println("Error: " + responseCode);
        }

        connection.disconnect();
        return null;
    }

    private static String getFieldFromJson(String json, String field) {
        String regex = "\"" + field +  "\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;

    }
    private static String parseTimezoneIDFromJson(String json) {
        String timezone_id = null;
        String regex = "\"timezone_id\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            timezone_id = matcher.group(1);
        }

        return timezone_id;
    }

    private static int parseTemperatureFromJson(String json) {
        int temperature = 0;
        String regex = "\"temperature\":(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String temperatureString = matcher.group(1);
            temperature = Integer.parseInt(temperatureString);
        }

        return temperature;
    }

    private static int parseTimeFromJson(String json) {
        int localtime = 0;
        String regex = "\"localtime\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String timeString = matcher.group(1);
            String[] timeParts = timeString.split(" ")[1].split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
            localtime = hours;
        }

        return localtime;
    }






}
