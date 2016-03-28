package rs.devana.labs.studentinfoapp.domain.models.notification;

import java.util.Calendar;

public class Notification implements NotificationInterface{
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

    @Override
    public String getAdditionalInfo() {
        return "Глобални догађај";
    }
}
