package nettrace;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;


// Responsible for executing a traceroute command and collecting hop data.
public class TracerouteRunner {
	
	/**
     * Runs a system-level traceroute or tracert command based on OS.
     * Parses the output to extract hop IPs, latency, and fills geo/ISP info.
     *
     * @param host The domain or IP to trace.
     * @return List of TracerouteHop objects containing metadata for each hop.
     */
    public static List<TracerouteHop> runTraceroute(String host) {
        List<TracerouteHop> hops = new ArrayList<>();
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = os.contains("win")
                ? "tracert -d " + host         // -d skips DNS resolution for speed
                : "traceroute -n " + host;     // -n skips DNS resolution on Unix

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            int hopNum = 0;

            while ((line = reader.readLine()) != null) {
                String cleaned = line.trim();
                if (cleaned.isEmpty() || !Character.isDigit(cleaned.charAt(0))) continue;

                hopNum++;
                String ip = extractIP(cleaned);
                String hostName = DNSResolver.resolveHostname(ip); // Reverse DNS lookup
                //String hostName = ip; // Placeholder: reverse DNS optional
                long latency = extractLatency(cleaned);

                TracerouteHop hop = new TracerouteHop(hopNum, ip, hostName, latency);
                
                // Add student-friendly explanation
                if (ip.equals("*")) {
                    hop.setNote("No response – hop may be protected by a firewall or dropped by destination network.");
                } else if (latency == -1) {
                    hop.setNote("Latency unavailable – host responded without timing info.");
                } else {
                    hop.setNote("Hop responded normally.");
                }

                IPGeoLocator.enrichHop(hop);  // Fills in country, region, ISP
                hops.add(hop);
            }

        } catch (Exception e) {
            System.out.println("Traceroute failed: " + e.getMessage());
        }
        return hops;
    }

    // Extracts the first IPv4 address found on a line.    
    private static String extractIP(String line) {
        String[] parts = line.split(" ");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                return part;
            }
        }
        return "*"; // fallback if IP not found
    }
    
    // Attempts to extract and average latency values (e.g. <1 ms, 23 ms)
    private static long extractLatency(String line) {
        try {
            // Match latency values like: 12 ms, <1 ms, 45 ms
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+|<\\d+)\\s*ms").matcher(line);
            List<Long> latencies = new ArrayList<>();

            while (matcher.find()) {
                String match = matcher.group(1).replace("<", "").trim();
                latencies.add(Long.parseLong(match));
            }

            if (!latencies.isEmpty()) {
                long sum = 0;
                for (long l : latencies) sum += l;
                return sum / latencies.size(); // Average of found latencies
            }
        } catch (Exception e) {
            System.out.println("Latency parse error: " + e.getMessage());
        }

        return -1; // Returns -1 if not found
    }

}
