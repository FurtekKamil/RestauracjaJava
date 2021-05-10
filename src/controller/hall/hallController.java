package controller.hall;

import applikacja.Dialogs;
import applikacja.SceneManager;
import applikacja.StateOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.dao.FoodDAO;
import models.dao.OrderDAO;
import models.dao.ReservationDAO;
import models.entity.Orders;
import models.entity.Reservation;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class hallController {

    public Button logOutButton;
    public Button makeOrderButton;
    public Button currentOrderButton;
    public Button reservationButton;

    public TableView<Reservation> Rezerwacje;   //Tabpane id=0 Dot Rezerwacji <- ReservationDAO
    public TableColumn<Reservation,String> Imie;
    public TableColumn<Reservation,String> Nazwisko;
    public TableColumn<Reservation,String> Telefon;
    public TableColumn<Reservation,Integer> id_stolika;
    public TableColumn<Reservation,String> Data;

    public TableView<StateOrder> Zamowienia;        //Tabpane id=1 Dot zamówień <- Klasa pomocnicza StateOrder
    public TableColumn<StateOrder,Integer> nrofTab;
    public TableColumn<StateOrder,Boolean> state;
    public TableColumn<StateOrder,Double> amout;
    public TabPane tabpane;

    Timer time = new Timer();

    DecimalFormat f = new DecimalFormat("##.00");

    TimerTask tt = new TimerTask() { //task odświeżający tablice orderów, wyłączenie programu na siłę nie skutkuje przerwaniem tego timera i przy następnej iteracji wystąpi błąd który jest wyłapywany
        @Override
        public void run() {
            try {
                Zamowienia.setItems(getOrders());
            }catch(Exception e){
                System.out.println("Zakonczono sesje");
                System.exit(0);
            }
        }
    };


    ObservableList<Reservation> getReservation() {

        List<Reservation> reservations = new ReservationDAO().findAll();
        ObservableList<Reservation> observableReserv = FXCollections.observableArrayList();
        observableReserv.addAll(reservations);

        return observableReserv;
    }

    ObservableList<StateOrder> getOrders() {
        AtomicReference<Double> detalAmount = new AtomicReference<>((double) 0);
        AtomicReference<String> state = new AtomicReference<>("");

        List<Orders> orders = new OrderDAO().findAllNotFinished();

        ObservableList<StateOrder> stateOrders = FXCollections.observableArrayList();

        orders.forEach(order -> {
            detalAmount.set(0.0);
            state.set("W przygotowaniu");

            order.getOrdersItems().forEach(orderItem->{
                detalAmount.updateAndGet(v -> (v + (new FoodDAO().findById(orderItem.getFood().getFood_id()).getPrice())*orderItem.getAmount()));

                if(orderItem.getFinish()){
                    //state.set(true);
                    state.set("Gotowe");
                }
                else{
                    //state.set(false);
                    state.set("W przygotowaniu");
                }

            });
            StateOrder states = new StateOrder(order.getTableNumber(),state.toString(),(f.format(Double.parseDouble(detalAmount.toString()))+" zł"),order.getOrder_id());
            stateOrders.add(states);
        });

        return stateOrders;
    }



        public void initialize(){

        Imie.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Nazwisko.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Telefon.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        id_stolika.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        Data.setCellValueFactory(new PropertyValueFactory<>("date"));

        Rezerwacje.setItems(getReservation());

        nrofTab.setCellValueFactory(new PropertyValueFactory<>("nr_table"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        amout.setCellValueFactory(new PropertyValueFactory<>("amount"));

        Zamowienia.setItems(getOrders());

        time.schedule(tt,10000,10000);

    }



    public void logOutOnAction() {

        Optional<ButtonType> result = Dialogs.confirmLogOutDialog();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SceneManager.renderScene("login");
        }

        time.cancel();
    }

    public void makeOrderOnAction() {

        SceneManager.renderScene("roomOrders");
    }

    public void removeReservation() {

        if(tabpane.getSelectionModel().getSelectedIndex()==0){
            if(Rezerwacje.getSelectionModel().getSelectedItem() != null){
                Reservation reservation = Rezerwacje.getSelectionModel().getSelectedItem();
                new ReservationDAO().delete(reservation);
                Rezerwacje.setItems(getReservation());
            }else {
                System.out.println("puste");
            }
        }else{
            if(Zamowienia.getSelectionModel().getSelectedItem() != null) {
                StateOrder orders = Zamowienia.getSelectionModel().getSelectedItem();
                Orders order = new OrderDAO().findById(orders.getId_order());
                order.setFinish(true);
                new OrderDAO().update(order);
                Zamowienia.setItems(getOrders());
            }else{
                System.out.println("puste");
            }

        }
    }

    public void reservationOnAction() {
        SceneManager.renderScene("room");
    }

}


