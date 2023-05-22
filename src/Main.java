public class Main {
    public static void main(String[] args) throws Exception {
        WeatherResponse response = WeatherAPIExample.getWeather("Vendelsö");


        System.out.println("Timezone ID: " + response.name());
        System.out.println("Temperature: " + response.temperature() + "°C");
        System.out.println("Time: " + response.time());

    }
}

