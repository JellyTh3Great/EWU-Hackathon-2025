package nettrace;
/**
 * Represents a single hop in a traceroute.
 * Stores IP, hostname, latency, geolocation, ISP, and annotations.
 */

public class TracerouteHop {
    private String ipAddress;
    private String hostname;
    private int hopNumber;
    private long latency; // in milliseconds
    private String country;
    private String region;
    private String isp;
    private double latitude;
    private double longitude;
    private String note;  // new field for explanation
    private String city;
    private String zip;
    
    // Conducts a new traceroute hop.
    public TracerouteHop(int hopNumber, String ipAddress, String hostname, long latency) {
        this.hopNumber = hopNumber;
        this.ipAddress = ipAddress;
        this.hostname = hostname;
        this.latency = latency;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setGeoInfo(String country, String region, String isp) {
        this.country = country;
        this.region = region;
        this.isp = isp;
    }

    /**
     * Returns a readable string representation of the hop.
     */
    @Override
    public String toString() {
        return String.format(
            "Hop %d: %s (%s) - %d ms [%s, %s, %s]",
            hopNumber,
            hostname != null ? hostname : "unknown",
            ipAddress,
            latency,
            country != null ? country : "unknown",
            region != null ? region : "unknown",
            isp != null ? isp : "unknown"
        );
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }
    public int getHopNumber() {
        return hopNumber;
    }

    public long getLatency() {
        return latency;
    }

    public String getHostname() {
        return hostname;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getIsp() {
        return isp;
    }
    
    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }
    
    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
