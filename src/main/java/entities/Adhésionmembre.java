package entities;
import java.util.Objects;
public class Adhésionmembre {

    private int id;
    private String name;
    private String description;
    private double price;
    private int duration;
    // Add more attributes as needed

    public Adhésionmembre(int id, String name, String description, double price, int durationInMonths) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = durationInMonths;
    }

    // Getters and setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDurationInMonths(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adhésionmembre that = (Adhésionmembre) o;
        return id == that.id &&
                Double.compare(that.price, price) == 0 &&
                duration == that.duration &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration);
    }

    @Override
    public String toString() {
        return "Adhésionmembre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", durationInMonths=" + duration +
                '}';
    }

}
