package rs.devana.labs.studentinfo.domain.models.notification;

import java.util.Calendar;
import java.util.Date;

public class Notification {
    int id;
    String description;
    Calendar expiresAt;

    public Notification(int id, String description, Calendar expiresAt) {
        this.id = id;
        this.description = description;
        this.expiresAt = expiresAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Calendar expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "id: " + id + "  Description: " + description;
    }

}
