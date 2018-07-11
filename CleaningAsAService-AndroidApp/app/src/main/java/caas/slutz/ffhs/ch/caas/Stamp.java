package caas.slutz.ffhs.ch.caas;

public class Stamp {
    private String ID, startTime, endTime;

    public Stamp() {
    }

    public Stamp(String title, String genre, String year) {
        this.ID = title;
        this.startTime = genre;
        this.endTime = year;
    }

    public String getID() {
        return ID;
    }

    public void setID(String name) {
        this.ID = name;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String year) {
        this.endTime = year;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
