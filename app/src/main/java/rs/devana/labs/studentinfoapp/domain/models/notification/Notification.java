package rs.devana.labs.studentinfoapp.domain.models.notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Notification implements NotificationInterface{
    int id;
    String description, additionalInfo;
    Calendar expiresAt, arrived;

    public Notification(int id, String description, Calendar expiresAt, Calendar arrived, String additionalInfo) {
        this.id = id;
        this.description = description;
        this.expiresAt = expiresAt;
        this.arrived = arrived;
        this.additionalInfo = additionalInfo;
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

    public Calendar getArrived() {
        return arrived;
    }

    public void setArrived(Calendar arrived) {
        this.arrived = arrived;
    }

    @Override
    public String toString() {
        return getAdditionalInfo() + "\n"
                + description + "\n"
                + new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(expiresAt.getTime());
    }

    @Override
    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
