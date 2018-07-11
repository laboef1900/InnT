package caas.slutz.ffhs.ch.caas;

public class EmployeeTask {
    private String email, dateOrdered, description, hours;

    public EmployeeTask() {
    }

    public EmployeeTask(String email, String dateOrdered, String description, String hours) {
        this.email = email;
        this.dateOrdered = dateOrdered;
        this.description = description;
        this.hours = hours;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

}
