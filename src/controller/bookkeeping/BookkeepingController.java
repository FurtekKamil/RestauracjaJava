package controller.bookkeeping;

import applikacja.Dialogs;
import applikacja.SceneManager;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import models.dao.OrderDAO;
import models.entity.Orders;
import models.entity.Orders_Items;


import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class BookkeepingController implements Initializable {

    @FXML
    public ImageView logoutOnlyIcon;
    @FXML
    public ImageView graphIcon;
    @FXML
    public ImageView refreshIcon;
    @FXML
    public ImageView pdfIcon;
    @FXML
    public Button refreshButton;
    @FXML
    public Button pdfButton;
    @FXML
    public TableView<Orders> transactionsTable = new TableView<>();
    @FXML
    public TableColumn<Orders, Number> tableNumberColumn;
    @FXML
    public TableColumn<Orders, String> dateColumn;
    @FXML
    public TableColumn<Orders, String> hourColumn;
    @FXML
    public TableColumn<Orders, Double> finalPriceColumn;
    @FXML
    public TableColumn<Orders, String> waiterColumn;
    @FXML
    public DatePicker fromDatePicker;
    @FXML
    public DatePicker toDatePicker;
    @FXML
    private ObservableList<Orders> orders;


//    public BookkeepingController() {
//
//    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Konfigurowanie odpowiednich sciezek dla poszczegolnych ikon
        logoutOnlyIcon.setImage(new Image("icons/kitchen/logOut.png"));
        graphIcon.setImage(new Image("icons/bookkeeping/graph-icon.png"));
        refreshIcon.setImage(new Image("icons/bookkeeping/refresh.png"));
        pdfIcon.setImage(new Image("icons/bookkeeping/pdf-icon.png"));


        //Inicjujemy tablicę z wartościami z danego miesiąca
        fromDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        toDatePicker.setValue(LocalDate.now());
        orders = FXCollections.observableList(new OrderDAO().findAllWithinDates(fromDatePicker.getValue().toString(), toDatePicker.getValue().toString()));

        //Dodanie ograniczeń do datepickerow
        this.fromDatePickerAddOnClickEventHandler();
        this.toDatePickerAddEventOnClickHander();

        //Odnośnik do sceny Ksiegowosc-Wykresy
        this.graphIconAddOnClickEvent();

        //Dodawanie popupu po kliknięciu przycisku wyloguj
        this.logoutOnlyIconOnClickEvent();

        //Wypełnianie tabeli
        this.getReceiptsList();

        //Dodawanie podpowiedzi na hoverze tabeli
        this.setUpTableTooltip();

        //Dodawanie Eventu on double click na istniejący wiersz
        this.transactionsTableOnHoverEvent();

        //Dodawanie Eventu on click dla zmiany zakresu wyswietlania danych w tabeli
        this.refreshButtonAddOnClickEventHandler();
    }

    private void toDatePickerAddEventOnClickHander() {
        toDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 || date.compareTo(fromDatePicker.getValue()) < 0);
            }
        });
    }

    private void fromDatePickerAddOnClickEventHandler() {
        fromDatePicker.setDayCellFactory( param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date,empty);
                setDisable(empty || date.compareTo(toDatePicker.getValue())> 0 || date.compareTo(LocalDate.now()) > 0);
            }
        });
    }

    private void refreshButtonAddOnClickEventHandler() {
        refreshButton.setOnMouseClicked(click -> {
            orders = FXCollections.observableList(new OrderDAO().findAllWithinDates(fromDatePicker.getValue().toString(), toDatePicker.getValue().toString()));
            this.getReceiptsList();
        });
    }

    private void graphIconAddOnClickEvent() {
        graphIcon.setOnMouseClicked(mouseEvent -> SceneManager.renderScene("bookkeepingGraphs"));
    }

    private void logoutOnlyIconOnClickEvent() {
        logoutOnlyIcon.setOnMouseClicked(mouseEvent -> {
            Optional<ButtonType> result = Dialogs.confirmLogOutDialog();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneManager.renderScene("login");
            }
        });
    }

    private void transactionsTableOnHoverEvent() {
        transactionsTable.setRowFactory(tv -> {
            TableRow<Orders> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Orders clickedRow = row.getItem();
                    showReceiptDetails(clickedRow);
//                    printRow(clickedRow);
//                    addNewReceipt(clickedRow);
                }
            });
            return row;
        });
    }

    private void setUpTableTooltip() {
        Tooltip ttTransactionTable = new Tooltip("Kliknij podwójnie dowolny wiersz aby pokazać\nszczegóły danego zamówienia");
        ttTransactionTable.setFont(new Font("Arial", 16));
        ttTransactionTable.setGraphic(new ImageView(new Image("icons/bookkeeping/receipt-white.png")));
        transactionsTable.setTooltip(ttTransactionTable);
    }

    public void getReceiptsList() {
        // Pobieramy wszystkie wiersze, dla każdego wiersza w zapytaniu sumujemy wartość kosztów i wypisujemy w osobnej kolumnie
        this.transactionsTable.setItems(orders);
        this.tableNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTableNumber()));
        this.waiterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFirstName()+ " " +cellData.getValue().getUser().getLastName()));
        this.finalPriceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Double>(returnTotalCost(cellData.getValue().getOrdersItems())));
        this.dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(returnTokenizedTime(cellData.getValue().getTimeForPlacingTheOrder().toString(),"date")));
        this.hourColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(returnTokenizedTime(cellData.getValue().getTimeForPlacingTheOrder().toString(), "hour")));

    }

    private String returnTokenizedTime(String untokenizedDate, String hourDate) {
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer token = new StringTokenizer(untokenizedDate);
        while (token.hasMoreElements()) {
            tokens.add(token.nextToken());
        }
        if(hourDate == "hour")
        return tokens.get(1);
        else if (hourDate =="date")
            return tokens.get(0);
        else return "";
    }


    private double returnTotalCost(List<Orders_Items> ordersItems) {
        double totalSum = 0.0;

        for(Orders_Items item : ordersItems){
            totalSum += item.getFood().getPrice()*item.getAmount();
        }

        return totalSum;
    }


    public static void showReceiptDetails(Orders order) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resteuracja Goździk");
        alert.setHeaderText("PARAGON FISKALNY");
        String contextText = "";
        Double sumPln = 0d;
        contextText += order.getTimeForPlacingTheOrder().toString() + "\n";

        if (order.getOrdersItems().size() > 0) {
            contextText += "\n" + order.getOrdersItems().get(0).getFood().getName() + "    " + order.getOrdersItems().get(0).getAmount() + " x " + order.getOrdersItems().get(0).getFood().getPrice();
            sumPln += order.getOrdersItems().get(0).getFood().getPrice() * order.getOrdersItems().get(0).getAmount();

            if (order.getOrdersItems().size() > 1) {
                for (int i = 1; i < order.getOrdersItems().size(); i++) {
                    contextText += "\n" + order.getOrdersItems().get(i).getFood().getName() + "    " + order.getOrdersItems().get(i).getAmount() + " x " + order.getOrdersItems().get(i).getFood().getPrice();
                    sumPln += order.getOrdersItems().get(i).getFood().getPrice() * order.getOrdersItems().get(i).getAmount();
                }
            }
        }
        else
            contextText+="\n\nNie zakupiono żadnego produktu";

        if(sumPln > 0d)
            contextText+= "\n\nSUMA PLN: " +  sumPln;
//            contextText+= "\nSUMA PLN: " +  this.returnTotalCost(order.getOrdersItems());

        alert.setContentText(contextText);
        ImageView icon = new ImageView("icons/bookkeeping/receipt.png");
        icon.setPreserveRatio(true);
        icon.setFitHeight(50);

        alert.setGraphic(icon);
        alert.showAndWait();
    }


}
