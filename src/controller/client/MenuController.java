package controller.client;

import applikacja.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.dao.FoodDAO;
import models.entity.Food;

import java.util.List;

/**
 * MenuController
 *
 * Klasa kontrolera obsługująca menu.
 *
 * @author Katarzyna Śliwa
 */

public class MenuController {

    /**
     * Metoda renderująca panel klienta/wracająca do panelu klienta.
     */
    @FXML
    void returnOnActionMenu() {
        SceneManager.renderScene("client");
    }

    @FXML
    public TableView<Food> mainDishes;

    @FXML
    public TableColumn<Food, String> name;

    @FXML
    public TableColumn<Food, Double> cost;

    @FXML
    private TableView<Food> soups;

    @FXML
    private TableColumn<Food, String> name2;

    @FXML
    private TableColumn<Food, Double> cost2;

    @FXML
    private TableView<Food> appetizers;

    @FXML
    private TableColumn<Food, String> name3;

    @FXML
    private TableColumn<Food, Double> cost3;

    @FXML
    private TableView<Food> drinks;

    @FXML
    private TableColumn<Food, String> name4;

    @FXML
    private TableColumn<Food, Double> cost4;

    @FXML
    private TableView<Food> desserts;

    @FXML
    private TableColumn<Food, String> name5;

    @FXML
    private TableColumn<Food, Double> cost5;

    /**
     * Metoda zwracająca listę produktów podzielonych na kategorie.
     * @param type kategoria z której interesują nas produkty
     * @return listę obserwowalną z produktami interesującej nas kategorii
     */
    ObservableList<Food> getProductList(String type) {

        List<Food> foods = new FoodDAO().findByType(type);                          //zwraca listę produktów o określonym typie z bazy

        ObservableList<Food> observableFood = FXCollections.observableArrayList();

        foods.forEach(food -> {
            observableFood.add(food);                                               //przepisanie produktów ze zwykłej listy ma obserwowalną
        });

        return observableFood;
    }

    /**
     * Metoda inicjalizująca, załadowująca listy produktów o określonych typach do osobnych tabel
     */
    public void initialize() {

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost.setCellValueFactory(new PropertyValueFactory<>("price"));
        mainDishes.setItems(getProductList("main_dish"));                       //załadowanie do tabeli dań głównych

        name2.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost2.setCellValueFactory(new PropertyValueFactory<>("price"));
        soups.setItems(getProductList("soup"));                                 //załadowanie do tabeli zup

        name3.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost3.setCellValueFactory(new PropertyValueFactory<>("price"));
        appetizers.setItems(getProductList("appetizer"));                       //załadowanie do tabeli przystawek

        name4.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost4.setCellValueFactory(new PropertyValueFactory<>("price"));
        drinks.setItems(getProductList("drink"));                               //załadowanie do tabeli napojów

        name5.setCellValueFactory(new PropertyValueFactory<>("name"));
        cost5.setCellValueFactory(new PropertyValueFactory<>("price"));
        desserts.setItems(getProductList("dessert"));                           //załadowanie do tabeli deserów

    }

}
