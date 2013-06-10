package architectgroup.fact.dto;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class BuildDto implements Serializable
{
	protected int id;
    protected String name;
    protected String description;
	protected Date startTime;
	protected Date endTime;
	protected String status;
	protected String version;
    protected long sizeInByte;
    protected int numberOfFixedIssue;
    protected int numberOfOpenIssue;
    protected int numberOfNewIssue;
    protected String type;
    protected String createdBy;
    protected String zipTree;           // This is for load the zipTree for faster reading

    public BuildDto() {
	}

	public int getId() {
		return id;
	}

	public void setId(int buildId) {
		this.id = buildId;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public java.lang.String getVersion() {
		return version;
	}

	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public long getSize() {
        return sizeInByte;
    }

    public void setSize(long sizeInByte) {
        this.sizeInByte = sizeInByte;
    }

    public int getNumberOfFixedIssue() {
        return numberOfFixedIssue;
    }

    public void setNumberOfFixedIssue(int numberOfFixedIssue) {
        this.numberOfFixedIssue = numberOfFixedIssue;
    }

    public int getNumberOfOpenIssue() {
        return numberOfOpenIssue;
    }

    public void setNumberOfOpenIssue(int numberOfOpenIssue) {
        this.numberOfOpenIssue = numberOfOpenIssue;
    }

    public int getNumberOfNewIssue() {
        return numberOfNewIssue;
    }

    public void setNumberOfNewIssue(int numberOfNewIssue) {
        this.numberOfNewIssue = numberOfNewIssue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getZipTree() {
        return zipTree;
    }

    public void setZipTree(String zipTree) {
        this.zipTree = zipTree;
    }
}
