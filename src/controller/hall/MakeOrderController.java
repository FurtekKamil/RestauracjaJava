package controller.hall;

import applikacja.Main;
import applikacja.SceneManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import models.dao.*;
import models.entity.*;

import java.sql.Savepoint;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MakeOrderController{

    public static int id_stolika=59;

    public Double vOfOrder = 0.0;
    public Button backButton;
    public Button confirmOrderButton;
    public Button allButton;
    public Button appetizerButton;
    public Button soupButton;
    public Button mainDishButton;
    public Button dessertButton;
    public Button drinkButton;

    public TableColumn<Food,String> foodOnOrder;
    public TableColumn<Food,Double> foodPriceOnOrder;
    public TableView<Food> orderList;
    public Text valueOfOrder;
    public Text id_table;

    private List<Food> foodList = new ArrayList<>();

    private List<Food> appetizerList = new ArrayList<>();
    private List<Food> soupList = new ArrayList<>();
    private List<Food> mainDishList = new ArrayList<>();
    private List<Food> dessertList = new ArrayList<>();
    private List<Food> drinkList = new ArrayList<>();

    public TableView<Food> foodTableView;
    public TableColumn<Food,String> foodNameColumn;
    public TableColumn<Food,Double> foodPriceColumn;

    private final List<String> foodTypeList = new ArrayList<>();

    ObservableList<Food> listOrder = FXCollections.observableArrayList();

    DecimalFormat f = new DecimalFormat("##.00");

    public void backAction(ActionEvent actionEvent) {
        SceneManager.renderScene("hall");
    }


    public MakeOrderController() {
        foodTypeList.add("appetizer");
        foodTypeList.add("soup");
        foodTypeList.add("mainDish");
        foodTypeList.add("dessert");
        foodTypeList.add("drink");

        foodList = new FoodDAO().findAll();

    }

    public void confirmOrderOnAction(ActionEvent actionEvent) {

        Map<Food, Long> counted = orderList.getItems().stream()
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));


        List<Orders_Items> lista = new ArrayList<>();
        Orders order = new Orders(0,id_stolika, Timestamp.valueOf(LocalDateTime.now()), new UsersDAO().findById(Main.loggedID),lista);
        new OrderDAO().save(order);

        counted.forEach((orderLists,key)->{
            System.out.println(orderLists.getName()+" ilosc "+key);
            Orders_Items itemOfOrder = new Orders_Items(0,key.intValue(),false,order,orderLists);
            lista.add(itemOfOrder);
            new Orders_ItemsDAO().save(itemOfOrder);
        });

        order.setOrdersItems(lista);
        new OrderDAO().update(order);

        Users user = new UsersDAO().findById(Main.loggedID);
        Reservation reservation = new Reservation(LocalDateTime.now(),user.getFirstName(),user.getLastName(),"000000000",id_stolika);
        new ReservationDAO().save(reservation);

        SceneManager.renderScene("hall");

    }


    private void enableAllButtons() {
        allButton.setDisable(false);
        appetizerButton.setDisable(false);
        soupButton.setDisable(false);
        mainDishButton.setDisable(false);
        drinkButton.setDisable(false);
        dessertButton.setDisable(false);
    }

    public void allButtonPressed() {
        createTable(getFoodTypeList(FoodArray.ALL));
        enableAllButtons();
        allButton.setDisable(true);
    }

    public void appetizerButtonPressed() {
        createTable(getFoodTypeList(FoodArray.APPETIZER));
        enableAllButtons();
        appetizerButton.setDisable(true);
    }

    public void soupButtonPressed() {
        createTable(getFoodTypeList(FoodArray.SOUP));
        enableAllButtons();
        soupButton.setDisable(true);
    }

    public void mainDishButtonPressed() {
        createTable(getFoodTypeList(FoodArray.MAIN_DISH));
        enableAllButtons();
        mainDishButton.setDisable(true);
    }

    public void dessertButtonPressed() {
        createTable(getFoodTypeList(FoodArray.DESSERT));
        enableAllButtons();
        dessertButton.setDisable(true);
    }

    public void drinkButtonPressed() {
        createTable(getFoodTypeList(FoodArray.DRINK));
        enableAllButtons();
        drinkButton.setDisable(true);
    }

    public void addToOrder() {
        if (foodTableView.getSelectionModel().getSelectedItem() == null){
            System.out.println("puste");
        }else {
            Food food = foodTableView.getSelectionModel().getSelectedItem();
            orderList.getItems().add(food);
            this.vOfOrder += food.getPrice();
            valueOfOrder.setText(this.f.format(vOfOrder) + " zł");
        }
    }

    enum FoodArray {
        ALL,
        APPETIZER,
        SOUP,
        MAIN_DISH,
        DESSERT,
        DRINK
    }

    public void initialize() {
        foodOnOrder.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodPriceOnOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
        allButton.fire();
        allButton.setDisable(true);
        id_table.setText(String.valueOf(id_stolika));
    }

    private List<Food> getFoodTypeList(FoodArray listType) {

        switch (listType) {
            case ALL -> {
                return foodList;
            }
            case APPETIZER -> {
                appetizerList.clear();
                for (Food food : foodList) {
                    if (food.getType().equals("appetizer")) {
                        appetizerList.add(food);
                    }
                }
                return appetizerList;
            }
            case SOUP -> {
                soupList.clear();
                for (Food food : foodList) {
                    if (food.getType().equals("soup")) {
                        soupList.add(food);
                    }
                }
                return soupList;
            }
            case MAIN_DISH -> {
                mainDishList.clear();
                for (Food food : foodList) {
                    if (food.getType().equals("mainDish")) {
                        mainDishList.add(food);
                    }
                }
                return mainDishList;
            }
            case DESSERT -> {
                dessertList.clear();
                for (Food food : foodList) {
                    if (food.getType().equals("dessert")) {
                        dessertList.add(food);
                    }
                }
                return dessertList;
            }
            case DRINK -> {
                drinkList.clear();
                for (Food food : foodList) {
                    if (food.getType().equals("drink")) {
                        drinkList.add(food);
                    }
                }
                return drinkList;
            }
            default -> throw new IllegalStateException("Unexpected value: " + listType);
        }
    }



    private void createTable(List<Food> foodTypeList) {

        this.foodTableView.setItems(FXCollections.observableArrayList(foodTypeList));
        this.foodNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        this.foodPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        this.foodNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.foodTableView.getSortOrder().add(foodNameColumn);
        this.foodTableView.sort();

        this.foodNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.foodPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    }
    private void refreshCurrentTable() {
        if (allButton.isDisable()) {
            allButtonPressed();
        } else if (appetizerButton.isDisable()) {
            appetizerButtonPressed();
        } else if (soupButton.isDisable()) {
            soupButtonPressed();
        } else if (mainDishButton.isDisable()) {
            mainDishButtonPressed();
        } else if (drinkButton.isDisable()) {
            drinkButtonPressed();
        } else if (dessertButton.isDisable()) {
            dessertButtonPressed();
        }
    }

}