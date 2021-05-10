package applikacja;

import java.util.concurrent.atomic.AtomicReference;

public class StateOrder {
    int nr_table;
    String state;
    String amount;
    int id_order;

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public StateOrder(int nr_table, String state, String amount, int id_order) {
        this.nr_table = nr_table;
        this.state = state;
        this.amount = amount;
        this.id_order = id_order;
    }


    public int getNr_table() {
        return nr_table;
    }

    public void setNr_table(int nr_table) {
        this.nr_table = nr_table;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}



