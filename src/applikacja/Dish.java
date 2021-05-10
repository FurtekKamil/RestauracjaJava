package applikacja;

public class Dish {
    public String name;
    public Double cost;
    public String type;


    public Dish(String name, Double cost, String type) {
        this.name = name;
        this.cost = cost;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Double getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void setType(String type) {
        this.type = type;
    }
}
