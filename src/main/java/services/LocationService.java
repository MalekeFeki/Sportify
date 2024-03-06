package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LocationService {

    public static String getUserCity() {
        try {
            // Get the user's public IP address
            String ipAddress = getPublicIPAddress();
            System.out.println("ipAddress"+ipAddress);
            // Use an IP-based location service (replace this URL with a reliable IP geolocation service)
            String locationInfo = getLocationInfoByIP(ipAddress);
            System.out.println("locationInfo"+locationInfo);
            // Extract the city from the location information
            String city = extractCityFromLocationInfo(locationInfo);
           // city="sfax";
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
        // Replace this URL with a reliable IP geolocation service
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
        // Parse the JSON response or use any other method to extract the city information
        // This is a simplified example assuming a JSON response
        return locationInfo.contains("city") ? locationInfo.split("\"city\":")[1].split("\"")[1] : "Unknown";
    }

    public static void main(String[] args) {
        String userCity = getUserCity();
        System.out.println("User's City: " + userCity);
    }
}
