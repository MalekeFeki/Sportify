package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LocationService {

    public static String getUserCity() {
        try {
            String ipAddress = getPublicIPAddress();
            System.out.println("ipAddress"+ipAddress);
            String locationInfo = getLocationInfoByIP(ipAddress);
            System.out.println("locationInfo"+locationInfo);
            String city = extractCityFromLocationInfo(locationInfo);
            System.out.println("city from method"+city);

            return city;
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    private static String getPublicIPAddress() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        return in.readLine();
    }

    private static String getLocationInfoByIP(String ipAddress) throws IOException {
        URL url = new URL("https://ipinfo.io/" + ipAddress + "/json");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static String extractCityFromLocationInfo(String locationInfo) {
        return locationInfo.contains("city") ? locationInfo.split("\"city\":")[1].split("\"")[1] : "Unknown";
    }

    public static void main(String[] args) {
        String userCity = getUserCity();
        System.out.println("User's City: " + userCity);
    }
}
