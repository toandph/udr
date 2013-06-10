package architectgroup.fact.dto;

import java.util.Date;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/18/13
 * Time: 2:50 PM
 */
public class HistoryDto {
    private int id;
    private String user;
    private String status;
    private String comment;
    private int issueId;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
