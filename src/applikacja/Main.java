package applikacja;

import javafx.application.Application;
import javafx.stage.Stage;
import models.dao.HibernateUtil;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends Application {
    public static String logged = "";
    public static int loggedID;

    @Override
    public void init() throws Exception {
        super.init();
        try {
            HibernateUtil.OpenConnection("hibernate.cfg.xml");
            System.out.println("Database found...");
        } catch (Exception e) {
            //create database
            try {
                System.out.println("Creating database");
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                String mysqlUrl = "jdbc:mysql://localhost:3306";
                Connection con = DriverManager.getConnection(mysqlUrl, "root", "test");

                ScriptRunner sr = new ScriptRunner(con);
                String filePath = new File("").getAbsolutePath();
                Reader reader = new BufferedReader(new FileReader(filePath + "/database/databaseCreateScript.sql"));
                sr.runScript(reader);
                con.close();

                HibernateUtil.OpenConnection("hibernate.cfg.xml");
                System.out.println("Successfully connected to new Database");
            } catch (Exception exception) {
                System.out.println("Something went wrong");
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        HibernateUtil.CloseConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


//        List<Orders> ordersList = new OrderDAO().findAll();
//        ordersList.forEach(order -> {
//            System.out.println(order.toString());
//            List<Food> foodList = order.getFoodList();
//            foodList.forEach(food -> System.out.println(food.toString()));
//        });


        primaryStage.setTitle("Restauracja");

        SceneManager.setStage(primaryStage);

        SceneManager.addScene("login", "fxml/login.fxml");
        SceneManager.addScene("mainMenu", "fxml/mainMenu.fxml");
        SceneManager.addScene("admin", "fxml/administrator/admin_menu.fxml");
        SceneManager.addScene("client", "fxml/client/client.fxml");
        SceneManager.addScene("bookkeeping", "fxml/bookkeeping/bookkeeping.fxml");
        SceneManager.addScene("bookkeepingGraphs", "fxml/bookkeeping/bookkeepingGraphs.fxml");
        SceneManager.addScene("kitchen", "fxml/kitchen/kitchen.fxml");

        SceneManager.addScene("room", "fxml/client/room.fxml");
        SceneManager.addScene("reserve", "fxml/client/roomForm.fxml");
        SceneManager.addScene("hall", "fxml/hall/hall.fxml");
        SceneManager.addScene("makeOrder", "fxml/hall/makeOrder.fxml");
        SceneManager.addScene("currentOrders", "fxml/hall/currentOrders.fxml");
        SceneManager.addScene("hallReservation", "fxml/hall/hallReservation.fxml");
        SceneManager.addScene("addUser", "fxml/administrator/addUser.fxml");
        SceneManager.addScene("confirm", "fxml/client/roomConfirmation.fxml");
        SceneManager.addScene("menu", "fxml/client/menu.fxml");

        SceneManager.addScene("roomOrders", "fxml/hall/roomOrders.fxml");


        //TODO: Rollback lines bellow
        SceneManager.renderScene("login");
//        SceneManager.renderScene("kitchen");

    }


}
