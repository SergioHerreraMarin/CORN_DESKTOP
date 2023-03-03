import org.json.JSONObject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

    public static UtilsWS socketClient;

    // public static int port = 3000;
    // public static String protocol = "http";
    // public static String host = "localhost";
    // public static String protocolWS = "ws";

    //Exemple de configuració per Railway
    public static int port = 443;
    public static String protocol = "https";
    public static String host = "cornapi-production-5680.up.railway.app";
    public static String protocolWS = "wss";

    // Camps JavaFX a modificar
    public static Label consoleName = new Label();
    public static Label consoleDate = new Label();
    public static Label consoleBrand = new Label();
    public static ImageView imageView = new ImageView(); 

    public static void main(String[] args) {

        // Iniciar app JavaFX   
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {

        final int windowWidth = 900;
        final int windowHeight = 600;

        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "listUsersView", "./src/listUsersView.fxml");
        UtilsViews.addView(getClass(), "userItemView", "./src/userItemView.fxml");
        UtilsViews.addView(getClass(), "transactionItemView", "./src/transactionItemView.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);
        
        stage.setScene(scene);
        stage.onCloseRequestProperty(); // Call close method when closing window
        stage.setTitle("JavaFX - NodeJS");
        stage.setMinWidth(windowWidth);
        stage.setMinHeight(windowHeight);
        stage.show();

        // Add icon only if not Mac
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:./assets/icon.png");
            stage.getIcons().add(icon);
        }

        // Iniciar WebSockets
        socketClient = UtilsWS.getSharedInstance(protocolWS + "://" + host + ":" + port);
        socketClient.onMessage((response) -> {
            
            // JavaFX necessita que els canvis es facin des de el thread principal
            Platform.runLater(()->{ 

            });
        });
    }

    @Override
    public void stop() { 
        socketClient.close();
        System.exit(1); // Kill all executor services
    }


    public static void addTransaction(){
        JSONObject obj = new JSONObject("{}");

        obj.put("user_id", "634269593");
        obj.put("amount", "2000");

        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/setup_payment", obj.toString(), (response) -> {
            JSONObject objresponse = new JSONObject(response);
            if(objresponse.getString("status").equals("OK")){
                System.out.println("Token: " + objresponse.getString("transaction_token"));
            }else{
                System.out.println(objresponse.getString("message"));
            }
        });
    }

    public static void addUser(){
        JSONObject obj = new JSONObject("{}");

        obj.put("phoneNumber", "634269593");
        obj.put("name", "Manolo");
        obj.put("lastName", "Garcia");
        obj.put("email", "mgarcia@gmail.com");
        obj.put("balance", "100");

        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/signup", obj.toString(), (response) -> {
            
            JSONObject objresponse = new JSONObject(response);
            System.out.println(objresponse);
            if(objresponse.getString("status").equals("OK")){
                System.out.println("VA");
            }else{
                //System.out.println("ERROR: " + objresponse.getString("message"));
                System.out.println("ERROR");
            }
        });
    }

}
