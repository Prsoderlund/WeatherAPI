import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String timezoneId = WeatherAPIExample.getTimezoneID();
        int temperature = WeatherAPIExample.getTemperature();
        int time = WeatherAPIExample.getTime();
       // int weather_code = Integer.parseInt(WeatherAPIExample.getResponse());

        System.out.println("Timezone ID: " + timezoneId);
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Time: " + time + ":00");
       // System.out.println("Weather code" + weather_code);

    }
}

