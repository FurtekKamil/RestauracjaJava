package controller.kitchen;

import applikacja.Dialogs;
import applikacja.SceneManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import models.dao.FoodDAO;
import models.entity.Food;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.DoubleStringConverter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static applikacja.Dialogs.changeFoodTypeDialog;

public class EditMenuController implements Initializable {


    @FXML
    public ComboBox<String> foodTypeComboBox;
    @FXML
    public TextField foodName;
    @FXML
    public TextField foodPrice;
    @FXML
    public Button confirmButton;
    @FXML
    public Button cancelButton;


    private List<Food> foodList = new ArrayList<>();

    private final List<String> foodTypeList = new ArrayList<>();


    private List<Food> appetizerList = new ArrayList<>();
    private List<Food> soupList = new ArrayList<>();
    private List<Food> mainDishList = new ArrayList<>();
    private List<Food> dessertList = new ArrayList<>();
    private List<Food> drinkList = new ArrayList<>();

    @FXML
    public TableView<Food> foodTableView;
    @FXML
    public TableColumn<Food, String> foodNameColumn;
    @FXML
    public TableColumn<Food, Double> foodPriceColumn;
    @FXML
    public Label errorLabel;

    @FXML
    public Button allButton;
    @FXML
    public Button appetizerButton;
    @FXML
    public Button soupButton;
    @FXML
    public Button mainDishButton;
    @FXML
    public Button drinkButton;
    @FXML
    public Button dessertButton;
    @FXML
    public ImageView logoutIcon;
    @FXML
    public ImageView orderIcon;
    @FXML
    public ImageView menuIcon;

    private void enableAllButtons() {
        allButton.setDisable(false);
        appetizerButton.setDisable(false);
        soupButton.setDisable(false);
        mainDishButton.setDisable(false);
        drinkButton.setDisable(false);
        dessertButton.setDisable(false);
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

    public void drinkButtonPressed() {
        createTable(getFoodTypeList(FoodArray.DRINK));
        enableAllButtons();
        drinkButton.setDisable(true);
    }

    public void dessertButtonPressed() {
        createTable(getFoodTypeList(FoodArray.DESSERT));
        enableAllButtons();
        dessertButton.setDisable(true);
    }

    public void allButtonPressed() {
        createTable(getFoodTypeList(FoodArray.ALL));
        enableAllButtons();
        allButton.setDisable(true);
    }

    enum FoodArray {
        ALL,
        APPETIZER,
        SOUP,
        MAIN_DISH,
        DESSERT,
        DRINK
    }

    public EditMenuController() {
        foodTypeList.add("Przystawka");
        foodTypeList.add("Zupa");
        foodTypeList.add("Danie Główne");
        foodTypeList.add("Deser");
        foodTypeList.add("Napój");

        foodList = new FoodDAO().findAll();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allButton.fire();
        allButton.setDisable(true);

        //loading images to scene - start
        orderIcon.setImage(new Image("icons/kitchen/order.png"));
        menuIcon.setImage(new Image("icons/kitchen/menu.png"));
        logoutIcon.setImage(new Image("icons/kitchen/logOut.png"));

        logoutIcon.setOnMouseClicked(mouseEvent -> {
            Optional<ButtonType> result = Dialogs.confirmLogOutDialog();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneManager.renderScene("login");
            }
        });

        orderIcon.setOnMouseClicked(mouseEvent -> SceneManager.renderScene("kitchen"));

        //loading images to scene - end


        //setting combo box -start
        this.foodTypeComboBox.setItems(FXCollections.observableArrayList(foodTypeList));
        //setting combo box -end

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
        //setting appetizer Table - start
        this.foodTableView.setItems(FXCollections.observableArrayList(foodTypeList));
        this.foodNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        this.foodPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        //setting sorting - start
        this.foodNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.foodTableView.getSortOrder().add(foodNameColumn);
        this.foodTableView.sort();
        //setting sorting - end

        this.foodNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.foodPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        //setting appetizer Table - end


        // Context Menu (to pod prawym przyciskiem myszy w tabeli) - start
        foodTableView.setRowFactory(
                tableView -> {
                    final TableRow<Food> row = new TableRow<>();
                    final ContextMenu rowMenu = new ContextMenu();
                    MenuItem removeItem = new MenuItem("Usuń");
                    MenuItem changeType = new MenuItem("Zmień typ dania");

                    removeItem.setOnAction(event -> {
                        for (int i = 0; i < foodList.size(); i++) {
                            if (foodList.get(i).getFood_id() == row.getItem().getFood_id()) {
                                try {
                                    new FoodDAO().delete(foodList.get(i));
                                    foodList.remove(i);
                                    foodTableView.getItems().remove(row.getItem());
                                    break;
                                } catch (Exception e) {
                                    Dialogs.errorInRemoving(e);
                                }
                            }
                        }
                    });

                    changeType.setOnAction(event -> {
                        String result = changeFoodTypeDialog();
                        if (result != null) {
                            for (Food food : foodList) {
                                if (food.getFood_id() == row.getItem().getFood_id()) {
                                    food.setType(translateToEnglishFoodType(result));
                                    new FoodDAO().update(food);
                                    refreshCurrentTable();
                                    break;
                                }
                            }

                        }
                    });

                    rowMenu.getItems().addAll(removeItem, changeType);

                    // only display context menu for non-empty rows:
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(rowMenu));
                    return row;
                });
        // Context Menu (to pod prawym przyciskiem myszy w tabeli) - end


    }

    public void nameOnEditCommit(TableColumn.CellEditEvent<Food, String> foodStringCellEditEvent) {
        Food selectedItem = foodStringCellEditEvent.getTableView().getItems().get(foodStringCellEditEvent.getTablePosition().getRow());
        selectedItem.setName(foodStringCellEditEvent.getNewValue());
        new FoodDAO().update(selectedItem);
    }


    public void priceOnEditCommit(TableColumn.CellEditEvent<Food, Double> foodDoubleCellEditEvent) {
        Food selectedItem = foodDoubleCellEditEvent.getTableView().getItems().get(foodDoubleCellEditEvent.getTablePosition().getRow());
        selectedItem.setPrice(foodDoubleCellEditEvent.getNewValue());
        new FoodDAO().update(selectedItem);
    }


    public void confirmOnAction(ActionEvent actionEvent) {

        if (!foodTypeComboBox.getSelectionModel().isEmpty() &&
                !foodName.getText().trim().isEmpty() &&
                !foodPrice.getText().trim().isEmpty()) {
            final Food addedFood = new Food(
                    foodName.getText(),
                    Math.round(Double.parseDouble(foodPrice.getText()) * 100) / 100.0,
                    translateToEnglishFoodType(foodTypeComboBox.getSelectionModel().getSelectedItem())
            );
            new FoodDAO().save(addedFood);
            foodList.add(addedFood);
            refreshCurrentTable();
            clearInputData();
            errorLabel.setVisible(false);
        } else {
            errorLabel.setVisible(true);
        }
    }

    private String translateToEnglishFoodType(String selectedItem) {

        return switch (selectedItem) {
            case "Przystawka" -> "appetizer";
            case "Zupa" -> "soup";
            case "Danie Główne" -> "mainDish";
            case "Deser" -> "dessert";
            case "Napój" -> "drink";
            default -> selectedItem;
        };

    }


    private void clearInputData() {
        foodPrice.clear();
        foodName.clear();
        foodTypeComboBox.getSelectionModel().clearSelection();
        errorLabel.setVisible(false);

    }

    public void cancelOnAction(ActionEvent actionEvent) {
        clearInputData();
        errorLabel.setVisible(false);

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
