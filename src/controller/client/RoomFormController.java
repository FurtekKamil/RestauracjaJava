package controller.client;

import applikacja.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.dao.ReservationDAO;
import models.entity.Reservation;
import java.text.ParseException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RoomFormController
 *
 * Klasa kontrolera obsługująca formularz rezerwacyjny i potwierdzenie.
 *
 * @author Katarzyna Śliwa
 */

public class RoomFormController {

	@FXML
	private Label valNameLabel;

	@FXML
	private Label valSurnameLabel;

	@FXML
	private Label valPhoneLabel;

	public TextField name;
	public TextField surname;
	public TextField phoneNumber;

	String Name;
	String Surname;
	String PhoneNumber;
	public static Integer tableId = 0;
	public static Date date;
	public static String time;

	/**
	 * Rezerwacja po kliknięciu przycisku wraz z walidacją danych i wysłaniem danych do bazy
	 * @throws ParseException
	 */
	public void reserveOnAction() throws ParseException {

		//pobranie danych z textFieldow i walidacja

		if (!name.getText().isEmpty()){

			if(isValidName(name.getText())){
				Name = name.getText();
				valNameLabel.setText("");
			}else{
				valNameLabel.setText("Niepoprawnie wprowadzono imię.");
			}

		}
		else {
			valNameLabel.setText("Imię jest wymagane.");
		}

		if (!surname.getText().isEmpty()){

			if(isValidName(surname.getText())){
				Surname = surname.getText();
				valSurnameLabel.setText("");
			}else{
				valSurnameLabel.setText("Niepoprawnie wprowadzono nazwisko.");
			}

		}
		else {
			valSurnameLabel.setText("Nazwisko jest wymagane.");
		}

		if (!phoneNumber.getText().isEmpty()){

			if(isValidPhone(phoneNumber.getText())){
				PhoneNumber = phoneNumber.getText().replaceAll("\\s+","");
				valPhoneLabel.setText("");
			}else{
				valPhoneLabel.setText("Niepoprawnie wprowadzono numer telefonu.");
			}
		}
		else {
			valPhoneLabel.setText("Numer telefonu jest wymagany.");
		}

		//formatowanie daty
		String dt = date.toString()+" "+time;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(dt, formatter);

		//zapis do bazy danych
		if(!name.getText().isEmpty() && !surname.getText().isEmpty() && !phoneNumber.getText().isEmpty() && isValidName(name.getText()) && isValidName(surname.getText()) && isValidPhone(phoneNumber.getText())) {
			Reservation res = new Reservation(dateTime, Name, Surname, PhoneNumber, tableId);
			new ReservationDAO().save(res);

			SceneManager.renderScene("confirm");
		}
	}

	/**
	 * Metoda walidująca imię i nazwisko, czy posiada tylko litery
	 * @param notValName niezwalidowane imię/nazwisko
	 */
	public boolean isValidName(String notValName){
		String regex="[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*";
		return notValName.matches(regex);
	}

	/**
	 * Metoda walidująca numer telefonu, czy składa się z 6 cyfr, z bądz bez spacji po każdej 3
	 * @param notValPhone niezwalidowany numer telefonu
	 */
	public boolean isValidPhone(String notValPhone){
		String regex="(?<!\\w)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)";
		return notValPhone.matches(regex);
	}

	/**
	 * Powrot do sali po anulowaniu - formularz
	 */
	public void returnToRoomOnAction() {
		SceneManager.renderScene("room");
	}

	/**
	 * Powrót do klienta - potwierdzenie
	 */
	public void anulujOnAction2(ActionEvent actionEvent) {
		SceneManager.renderScene("client");
	}

	/**
	 * Wciśnięcie "ok" - potwierdzenie
	 */
	public void reserveOnAction2(ActionEvent actionEvent) {
		SceneManager.renderScene("room");
	}

}
