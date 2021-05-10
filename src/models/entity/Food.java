package models.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Food")
public class Food {


    @Column(name = "food_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int food_id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "type")
    private String type;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "order_id")
//    private Orders orders;

    @OneToMany(mappedBy = "food", fetch = FetchType.EAGER)
    private List<Orders_Items> ordersItems;



    public Food() {
    }

    public Food(String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Orders_Items> getOrdersItems() {
        return ordersItems;
    }

    public void setOrdersItems(List<Orders_Items> ordersItems) {
        this.ordersItems = ordersItems;
    }

    @Override
    public String toString() {
        return "Food{" +
                "food_id=" + food_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}
