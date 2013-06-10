package architectgroup.fact.dto;

import java.io.Serializable;
import java.util.Date;

public class ProjectDto implements Serializable
{
	protected int id;
	protected String name;
	protected String description;
	protected String status;
	protected Date createdTime;
	protected String createdBy;
    protected int totalOfBuilds;
    protected UserDto user;
    protected BuildDto lastBuild;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getTotalOfBuilds() {
        return totalOfBuilds;
    }

    public void setTotalOfBuilds(int totalOfBuilds) {
        this.totalOfBuilds = totalOfBuilds;
    }

    public BuildDto getLastBuild() {
        return lastBuild;
    }

    public void setLastBuild(BuildDto lastBuild) {
        this.lastBuild = lastBuild;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
