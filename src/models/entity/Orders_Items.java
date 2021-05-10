package models.entity;

import javax.persistence.*;

@Entity
@Table(name = "Orders_Items")
public class Orders_Items {

    @Column(name = "order_item_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_item_id;

    @Column(name = "amount")
    private int amount;

    @Column(name = "finish")
    private Boolean finish;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id")
    private Food food;


    public Orders_Items() {
    }

    public Orders_Items(int order_item_id, int amount, Boolean finish, Orders orders, Food food) {
        this.order_item_id = order_item_id;
        this.amount = amount;
        this.finish = finish;
        this.orders = orders;
        this.food = food;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
