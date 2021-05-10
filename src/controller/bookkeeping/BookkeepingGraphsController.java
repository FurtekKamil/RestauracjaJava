package controller.bookkeeping;

import applikacja.Dialogs;
import applikacja.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookkeepingGraphsController implements Initializable {
    @FXML
    public ImageView logoutOnlyIcon;
    @FXML
    public ImageView bookkeepingIcon;
    @FXML
    public BarChart<String,Number> barChart;
    @FXML
    public LineChart<String,Number> lineChart;
    @FXML
    public AreaChart<String,Number> areaChart;
    @FXML
    public DatePicker datePickerFrom;
    @FXML
    public DatePicker datePickerTo;
    @FXML
    public TitledPane barChartPane;
    @FXML
    public TitledPane lineChartPane;
    @FXML
    public TitledPane areaChartPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logoutOnlyIcon.setImage(new Image("icons/kitchen/logOut.png"));
        bookkeepingIcon.setImage(new Image("icons/bookkeeping/bookkeeping.png"));


        bookkeepingIcon.setOnMouseClicked(mouseEvent -> SceneManager.renderScene("bookkeeping"));
        logoutOnlyIcon.setOnMouseClicked(mouseEvent -> {
            Optional<ButtonType> result = Dialogs.confirmLogOutDialog();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SceneManager.renderScene("login");
            }
        });

        XYChart.Series set1 = new XYChart.Series<>();
        set1.getData().add(new XYChart.Data("Styczen",5000));
        set1.getData().add(new XYChart.Data("Luty",8000));
        set1.getData().add(new XYChart.Data("Marzec",2000));
        set1.getData().add(new XYChart.Data("Kwiecien",1000));

        XYChart.Series set2 = new XYChart.Series<>();
        set2.getData().add(new XYChart.Data("Styczen",5000));
        set2.getData().add(new XYChart.Data("Luty",8000));
        set2.getData().add(new XYChart.Data("Marzec",2000));
        set2.getData().add(new XYChart.Data("Kwiecien",1000));

        XYChart.Series set3 = new XYChart.Series<>();
        set3.getData().add(new XYChart.Data("Styczen",5000));
        set3.getData().add(new XYChart.Data("Luty",8000));
        set3.getData().add(new XYChart.Data("Marzec",2000));
        set3.getData().add(new XYChart.Data("Kwiecien",1000));


        barChart.getData().addAll(set1);
        lineChart.getData().addAll(set2);
        areaChart.getData().addAll(set3);


    }
    }
