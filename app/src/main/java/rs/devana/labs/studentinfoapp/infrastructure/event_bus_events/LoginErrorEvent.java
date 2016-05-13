package rs.devana.labs.studentinfoapp.infrastructure.event_bus_events;

public class LoginErrorEvent {
    private int errorCode;
    private String errorMessage;

    public LoginErrorEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        String errorMsg;
        switch (errorCode){
            case 103: errorMsg = "Адреса не постоји у бази";
                break;
            case 141: errorMsg = "Потребно је да прво активирате Ваш налог";
                break;
            default: errorMsg = "Грешка приликом пријаве";
                break;
        }
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
