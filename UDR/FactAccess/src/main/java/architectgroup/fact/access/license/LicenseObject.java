package architectgroup.fact.access.license;

import java.io.Serializable;
import java.util.Date;

public class LicenseObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private KeyStatus status;
    private String name;
    private String hostId;
    private Date expiration;
    private String version;

    public LicenseObject() {
        status = KeyStatus.KEY_INVALID;
        name = "";
        hostId = "";
        expiration = new Date();
        version = "";
    }

    public LicenseObject(KeyStatus status, String name, String hostId, Date expiration, String version) {
        this.status = status;
        this.name = name;
        this.hostId = hostId;
        this.expiration = expiration;
        this.version = version;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the hostId
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * @param hostId the hostId to set
     */
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    /**
     * @return the expiration
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * @param expiration the expiration to set
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return this.getName() + "#" + this.getHostId() + "#" + this.getVersion() + "#" + this.getExpiration();
    }

    public String getFileName() {
        return "udr-license";
    }

    public byte[] toBytes() {
        return this.toString().getBytes();
    }

    public KeyStatus getStatus() {
        return status;
    }

    public void setStatus(KeyStatus status) {
        this.status = status;
    }
}
