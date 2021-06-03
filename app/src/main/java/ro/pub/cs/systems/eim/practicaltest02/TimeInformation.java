package ro.pub.cs.systems.eim.practicaltest02;

public class TimeInformation {
    private String hour, minute;

    public TimeInformation(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}
