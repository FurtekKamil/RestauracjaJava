package controller.client;

import applikacja.Main;
import applikacja.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import models.dao.ReservationDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * ReserveController
 *
 * Klasa kontrolera obsługująca salę.
 *
 * @author Katarzyna Śliwa
 */

public class ReserveController {

    ObservableList<String> hourList = FXCollections.observableArrayList("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"); //lista z godzinami pracy restauracji

    ObservableList<String> rightHourList = FXCollections.observableArrayList();     //aktualna lista godzin do wyświetlenia zależna od bieżącej godziny
    String correctId;                                                               //id stolika (numer po obcięciu)

    @FXML
    private ComboBox<String> hourBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private AnchorPane tablePane;

    /**
     * Metoda zapewniająca różny powrót z widoku sali, zależnie od tego czy jesteśmy zalogowani jako klient czy kelner
     */
    @FXML
    void returnReserve() {

        if (Main.logged.equals("kelner")) {
            SceneManager.renderScene("hall");
        } else {
            SceneManager.renderScene("client");
        }

    }

    /**
     * Metoda inicjalizująca widok sali
     */
    @FXML
    private void initialize() {

        int nowTime = LocalDateTime.now().getHour();    //pobranie aktualnej godziny
        nowTime++;                                      //zaokrąglenie do pełnej godziny i zamiana na string
        String nowTimeStr = nowTime + ":00";

        for (int i = 0; i < hourList.size(); i++) {         //szukanie czy aktualna godzina znajduje się w ramach godzin pracy restauracji

            if (nowTimeStr.equals(hourList.get(i))) {       //warunek jeżeli mieści się w godzinach pracy

                datePicker.setValue(LocalDate.now());       //ustawienie daty domyślnej na datę dzisiejszą

                datePicker.setDayCellFactory(picker -> new DateCell() {     //wyłączenie możliwości rezerwacji na datę wcześniejszą od dzisiejszej
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now();

                        setDisable(empty || date.compareTo(today) < 0);
                    }
                });

                hourBox.setValue(nowTimeStr);                   //ustawienie czasu na godzinę obecną

                for (int y = i; y < hourList.size(); y++) {
                    rightHourList.add(hourList.get(y));
                }

                hourBox.setItems(rightHourList);                //wypełnienie ChoiceBoxa odpowiednimi danymi

                break;

            } else if (nowTime <= 24 && nowTime > 22) {                     //przypadek jeśli godzina jest poza godzinami pracy, od godziny 22 do 24

                datePicker.setValue(LocalDate.now().plusDays(1));           //ustawienie daty na dzień następny

                datePicker.setDayCellFactory(picker -> new DateCell() {     //wyłączenie możliwości rezerwacji na datę wcześniejszą od dnia następnego
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now().plusDays(1);

                        setDisable(empty || date.compareTo(today) < 0);
                    }
                });

                hourBox.setValue("10:00");              //ustawienie godziny na 10 dnia następnego

            } else {                                    //przypadek jeśli godzina jest poza godzinami pracy, od godziny 24 do 10

                datePicker.setValue(LocalDate.now());   //ustawienie daty na dzień następny

                datePicker.setDayCellFactory(picker -> new DateCell() {     //wyłączenie możliwości rezerwacji na datę wcześniejszą od dnia następnego
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now();

                        setDisable(empty || date.compareTo(today) < 0);
                    }
                });

                hourBox.setValue("10:00");      //ustawienie godziny na 10 dnia następnego
            }
        }

        dateChanged();          //aktualizacja stolików i listy godzin do wyboru

    }

    /**
     * Metoda wylicza numer klikniętego stolika i przekazuje do metody reserve
     * @param event zdarzenie kliknięcia na stolik
     */
    @FXML
    void reserveOnTableClicked(MouseEvent event) throws IOException {
        Shape shape = (Shape) event.getSource();
        String id = shape.getId();
        correctId = id.substring(1);
        int tabNum = Integer.parseInt(correctId);

        reserve(tabNum);
    }

    /**
     * Metoda pobierająca id stolika, datę i godzinę rezerwacji i wywołująca widok formularza rezerwacji
     * @param tabNum numer stolika do zarezerwowania
     */
    void reserve(int tabNum) {

        RoomFormController.tableId = tabNum;                                            //pobranie id stolika

        java.sql.Date dateFromPicker = java.sql.Date.valueOf(datePicker.getValue());    //pobranie daty
        RoomFormController.date = dateFromPicker;

        String hourFromPicker = (String) hourBox.getValue();                            //pobranie godziny
        RoomFormController.time = hourFromPicker;

        SceneManager.renderScene("reserve");
    }

    /**
     * Metoda zwracająca numery (String) stolików zajętych w danych ramach czasowych
     * @param resDate data rezerwacji
     * @param resHour godzina rezerwacji
     * @return lista numerów stolików zajętych (w postaci String)
     */
    ArrayList<String> reservedTables(java.sql.Date resDate, String resHour) {

        String dt = resDate.toString() + " " + resHour;                                         //przygotowanie datetime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dt, formatter).minusHours(1);

        String date1 = dateTime.format(formatter);                                              //pierwsza data
        String date2 = dateTime.plusHours(2).format(formatter);                                 //druga data (większa o 2 godziny)

        ReservationDAO resdao = new ReservationDAO();
        ArrayList<String> tbList = resdao.findByDate(date1, date2);       //wyszukanie w bazie id stolików zajętych w okresie pomiędzy dwiema datami

        return tbList;
    }

    /**
     * Metoda przechwytująca stoliki z widoku i przeszukująca po je pokolei w poszukiwaniu stolików zarezerwowanych
     * @param tableNums numery stolików zarezerwowanych
     * @return lista stolików zarezerwowanych (obiektów z widoku sali)
     */
    ArrayList<Shape> findTables(ArrayList<String> tableNums) {

        ObservableList<Node> children = tablePane.getChildren();
        ArrayList<Shape> shapeList = new ArrayList<>();

        for (String tableNum : tableNums) {

            for (int i = 0; i < children.size(); i++) {

                if (children.get(i) instanceof Shape) {

                    Shape sh = (Shape) children.get(i);
                    if (sh.getId() != null) {

                        if (sh.getId().equals(tableNum)) {
                            shapeList.add(sh);
                        }

                    }

                }

            }

        }

        return shapeList;

    }

    /**
     * Metoda kolorująca stoliki na podany kolor i blokująca na nich możliwość rezerwacji
     * @param color kolor do pomalowania stolików
     */
    void colourTables(ArrayList<String> tableNums, Color color) {
        ArrayList<Shape> tables = findTables(tableNums);

        for (Shape t : tables) {
            t.setFill(color);
            t.setDisable(true);
        }
    }

    /**
     * Aktualizacja wszystkich stolików i powrót do możliwości rezerwacji ich
     * @param tables lista stolików
     * @param color kolor do pomalowania stolików
     */
    void clearAllTables(ArrayList<Shape> tables, Color color) {
        for (Shape t : tables) {
            t.setFill(color);
            t.setDisable(false);
        }
    }

    /**
     * Metoda wybierająca wszystkie stoliki (pomija np krzesła)
     * @return lista wszystkich stolików
     */
    ArrayList<Shape> getAllTables() {

        ObservableList<Node> children = tablePane.getChildren();
        ArrayList<Shape> shapeList = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {

            if (children.get(i) instanceof Shape) {
                Shape sh = (Shape) children.get(i);

                if (sh.getId() != null) {
                    shapeList.add(sh);
                }
            }
        }

        return shapeList;

    }

    /**
     * Metoda pobierająca datę i godzinę i wywołująca metodę wyszukującą stoliki
     */
    void sendColorOfTables() {

        java.sql.Date dateFromPicker = java.sql.Date.valueOf(datePicker.getValue());
        String hourFromPicker = hourBox.getValue();

        ArrayList<String> tbs = reservedTables(dateFromPicker, hourFromPicker);

        colourTables(tbs, javafx.scene.paint.Color.web("#2f2f2f"));                 //kolorowanie stolików zajętych na czarno

    }

    /**
     * Wywołująca się kiedy zmieniona jest data, aktualizująca stoliki i malująca stoliki zarezerwowane
     */
    @FXML
    void dateChanged() {

        clearAllTables(getAllTables(), javafx.scene.paint.Color.DODGERBLUE);            //aktualizacja stolikow

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if ((LocalDateTime.now().format(formatter)).equals(datePicker.getValue().format(formatter))) {      //jeżeli data wybrana to data dzisiejsza

            if(LocalDateTime.now().getHour()<10){              //godzina przed 10
                try {
                    hourBox.setItems(hourList);                //obcinamy listę dostępnych godzin
                    sendColorOfTables();                       //aktualizacja stolików
                } catch (Exception e) {
                    hourBox.setValue(rightHourList.get(0));
                }
            } else {                                                //godzina normalna - obcinamy listę dostępnych godzin

                try {
                    hourBox.setItems(rightHourList);
                    sendColorOfTables();                            //aktualizacja stolików
                } catch (Exception e) {
                    hourBox.setValue(rightHourList.get(0));
                }
            }

        } else {                                                 //godzina po 22
            try {
                hourBox.setItems(hourList);                      //listę dostępnych godzin ustawiamy na pełną
                sendColorOfTables();                             //aktualizacja stolików
            } catch (Exception e) {
                hourBox.setValue(hourList.get(0));
            }
        }

    }


}
