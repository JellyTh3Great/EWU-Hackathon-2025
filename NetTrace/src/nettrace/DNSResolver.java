package nettrace;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility class to perform reverse DNS lookups from IP to hostname.
 */
public class DNSResolver {

    /**
     * Attempts to resolve the hostname from an IP address.
     * Returns the IP itself if hostname can't be resolved.
     */
    public static String resolveHostname(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            return ipAddress; // fallback if hostname cannot be resolved
        }
    }
}
