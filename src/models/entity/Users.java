package models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class Users implements Serializable {


    @Serial
    private static final long serialVersionUID = 6417029697479553724L;


    @Column(name = "users_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int users_id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Orders> ordersList = new ArrayList<Orders>();

    public Users() {
    }

    public Users(String firstName, String lastName, String login, String password, String role, ArrayList<Orders> ordersList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        setRole(role);
        this.ordersList = ordersList;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(UsersRole role) {
        switch (role) {
            case ADMIN -> this.role = "admin";
            case HALL -> this.role = "hall";
            case BOOKKEEPER -> this.role = "bookkeeper";
            case CLIENT -> this.role = "client";
            case KITCHEN -> this.role = "kitchen";
        }
    }

    public ArrayList<Orders> getOrdersList() {
        return (ArrayList<Orders>) ordersList;
    }

    public void setOrdersList(ArrayList<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    @Override
    public String toString() {
        return "Users{" +
                "users_id=" + users_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
