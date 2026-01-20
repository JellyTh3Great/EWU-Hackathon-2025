package nettrace;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * Looks up geolocation and ISP information for a given IP address
 * using the IP-API.com free JSON API.
 */
public class IPGeoLocator {
	
	 /**
     * Populates the TracerouteHop with country, region, city, zip, ISP, and lat/lon.
     * Uses http://ip-api.com/json/{ip} as the data source.
     *
     * @param hop The hop object to enrich with location and network info.
     */
    public static void enrichHop(TracerouteHop hop) {
        try {
            String ip = hop.getIpAddress();
            if (ip.equals("*")) return; // Skip timeouts or unreachable hops

            URL url = new URL("http://ip-api.com/json/" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject obj = new JSONObject(jsonResponse.toString());
            
            // Extract fields
            String country = obj.optString("country");
            String region = obj.optString("regionName");
            String isp = obj.optString("isp");
            String city = obj.optString("city");
            String zip = obj.optString("zip");

            // Assign to the hop
            hop.setGeoInfo(country, region, isp);
            hop.setCity(city);
            hop.setZip(zip);
        } catch (Exception e) {
            System.out.println("Geo lookup failed for hop: " + hop.getIpAddress());
        }
    }
}
