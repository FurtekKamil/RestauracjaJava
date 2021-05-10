package controller.kitchen;


import applikacja.Dialogs;
import applikacja.SceneManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import models.dao.OrderDAO;
import models.dao.Orders_ItemsDAO;
import models.entity.Food;
import models.entity.Orders;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.entity.Orders_Items;

import java.net.URL;
import java.util.*;

public class KitchenController implements Initializable {

    @FXML
    public ImageView orderImage;
    @FXML
    public ImageView menuIcon;
    @FXML
    public ImageView logoutOnlyIcon;
    @FXML
    public TableView<Orders> ordersTableView = new TableView<>();
    @FXML
    public TableColumn<Orders, Number> tableNumberColumn;
    @FXML
    public TableColumn<Orders, String> orderTimeColumn;
    @FXML
    public TableColumn<Orders, String> foodAmountColumn;

    @FXML
    public TableView<Orders_Items> foodTableView;
    @FXML
    public TableColumn<Orders_Items, String> foodNameColumn;
    @FXML
    public TableColumn<Orders_Items, ImageView> mealReadyColumn;
    @FXML
    public TableColumn<Orders_Items, Number> foodAmountColumnInFoodTable;
    @FXML
    public Button readyMealButton;
    @FXML
    public Button updateButton;
    @FXML
    public ImageView updateIcon;

    private ObservableList<Orders> orders;
    private ObservableList<Orders> updatedOrders;



    /*

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), actionEvent -> {
        System.out.println("Refresh");
    }));

      timeline.setCycleCount(Animation.INDEFINITE);
      timeline.play();

    */

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), actionEvent -> {
        updatedOrders = FXCollections.observableList(new OrderDAO().findAllInProgress());
        if (orders.size() != updatedOrders.size()) {
            updateButton.setVisible(true);
        } else { //taka sama długość różne warotści
            List<Integer> ordersId = new ArrayList<>();
            List<Integer> updatedOrdersId = new ArrayList<>();
            for (int i = 0; i < orders.size(); i++) {
                ordersId.add(orders.get(i).getOrder_id());
                updatedOrdersId.add(updatedOrders.get(i).getOrder_id());
            }
            if (!ordersId.equals(updatedOrdersId)) {
                updateButton.setVisible(true);
            }
        }
    }));

    public KitchenController() {
        updatedOrders = FXCollections.observableList(new OrderDAO().findAllInProgress());
        orders = FXCollections.observableList(new OrderDAO().findAllInProgress());
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneManager.addScene("editMenu", "fxml/kitchen/editMenu.fxml");
        //loading images to scene - start
        orderImage.setImage(new Image("icons/kitchen/order.png"));
        menuIcon.setImage(new Image("icons/kitchen/menu.png"));
        logoutOnlyIcon.setImage(new Image("icons/kitchen/logOut.png"));
        updateIcon.setImage(new Image("icons/kitchen/download.png"));
        //loading images to scene - end

        logoutOnlyIcon.setOnMouseClicked(mouseEvent -> {
            Optional<ButtonType> result = Dialogs.confirmLogOutDialog();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneManager.renderScene("login");
            }
        });

        menuIcon.setOnMouseClicked(mouseEvent -> SceneManager.renderScene("editMenu"));

        createOrdersTable();

        //setting sorting -start
        this.orderTimeColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.ordersTableView.getSortOrder().add(orderTimeColumn);
        this.ordersTableView.sort();
        //setting sorting -end


    }

    private void createOrdersTable() {
        this.ordersTableView.setItems(orders);
        this.tableNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTableNumber()));
        this.orderTimeColumn.setCellValueFactory(cellData -> {
            ArrayList<String> tokens = new ArrayList<>();
            StringTokenizer token = new StringTokenizer(cellData.getValue().getTimeForPlacingTheOrder().toString(), " ");
            while (token.hasMoreElements()) {
                tokens.add(token.nextToken());
            }
            return new ReadOnlyStringWrapper(tokens.get(1));
        });
        this.foodAmountColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                String.format("%d/%d",
                        cellData.getValue().getOrdersItems().stream()
                                .filter(Orders_Items::getFinish)
                                .count(),
                        cellData.getValue().getOrdersItems().size())));


        this.ordersTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelection, newSelection) -> {
            if (oldSelection != newSelection && observableValue.getValue() != null) {
                createFoodTable(observableValue.getValue().getOrdersItems());
                readyMealButton.setVisible(false);
            }
        });

        this.ordersTableView.getSelectionModel().selectFirst();
    }

    private ImageView createImageView(String path) {
        ImageView image = new ImageView(path);
        image.setPreserveRatio(true);
        image.setFitWidth(20);
        return image;
    }

    private void createFoodTable(List<Orders_Items> orders_itemsList) {

        this.foodTableView.setItems(FXCollections.observableArrayList(orders_itemsList));
        this.foodNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFood().getName()));
        this.foodAmountColumnInFoodTable.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()));
        this.mealReadyColumn.setCellValueFactory(cellData -> cellData.getValue().getFinish() ?
                new SimpleObjectProperty<>(createImageView("icons/kitchen/checkYes.png"))
                : new SimpleObjectProperty<>(createImageView("icons/kitchen/checkNo.png"))
        );

        this.foodTableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldSelection, newSelection) ->
        {
            readyMealButton.setVisible(true);
            setButtonText(observableValue.getValue());
            readyMealButton.setOnAction(actionEvent -> {
                observableValue.getValue().setFinish(!observableValue.getValue().getFinish());
                observableValue.getValue().setFinish(true);
                new Orders_ItemsDAO().update(observableValue.getValue());
                this.ordersTableView.refresh();
                this.foodTableView.refresh();
                setButtonText(observableValue.getValue());
            });
        }));

        //setting sorting -start
        this.foodNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        this.foodTableView.getSortOrder().add(foodNameColumn);
        this.foodTableView.sort();
        //setting sorting -end
    }

    private void setButtonText(Orders_Items value) {
        if (value == null) {
            readyMealButton.setVisible(false);
        } else if (value.getFinish()) {
            readyMealButton.setText("Cofnij");
        } else {
            readyMealButton.setText("Danie Gotowe");
        }
    }

    public void updateOrdersList(ActionEvent actionEvent) {
        updateButton.setVisible(false);
        orders = updatedOrders;
        createOrdersTable();
    }
}
