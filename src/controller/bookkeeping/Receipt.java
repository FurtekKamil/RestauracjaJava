package controller.bookkeeping;

public class Receipt {
    Integer id;
    String items;
    String amounts;
    String itemTotalPrice;
    Float finalPrice;
    String date;

    public Receipt(Integer id, String items, String amounts, String itemTotalPrice, Float finalPrice, String date) {
        this.id = id;
        this.items = items;
        this.amounts = amounts;
        this.itemTotalPrice = itemTotalPrice;
        this.finalPrice = finalPrice;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }
}
