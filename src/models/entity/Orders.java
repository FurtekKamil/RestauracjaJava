package models.entity;


import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Orders implements Serializable {

    @Serial
    private static final long serialVersionUID = 2110916600652987191L;


    @Column(name = "order_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;

    @Column(name = "tableNumber")
    private int tableNumber;

    @Column(name = "date")
    private Timestamp timeForPlacingTheOrder;

    @Column(name = "finish")
    private boolean finish;

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
//    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
//    private List<Food> foodList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private Users user;


    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Orders_Items> ordersItems;



    public Orders() {
    }

    public Orders(int order_id, int tableNumber, Timestamp timeForPlacingTheOrder, Users user, List<Orders_Items> ordersItems) {
        this.order_id = order_id;
        this.tableNumber = tableNumber;
        this.timeForPlacingTheOrder = timeForPlacingTheOrder;
        this.user = user;
        this.ordersItems = ordersItems;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Timestamp getTimeForPlacingTheOrder() {
        return timeForPlacingTheOrder;
    }

    public void setTimeForPlacingTheOrder(Timestamp timeForPlacingTheOrder) {
        this.timeForPlacingTheOrder = timeForPlacingTheOrder;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Orders_Items> getOrdersItems() {
        return ordersItems;
    }

    public void setOrdersItems(List<Orders_Items> ordersItems) {
        this.ordersItems = ordersItems;
    }
}
