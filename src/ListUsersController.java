import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ListUsersController implements Initializable {

    @FXML
    private VBox yPane = new VBox();

    @FXML
    private Label dataName;
    @FXML
    private Label dataLastName;
    @FXML
    private Label dataPhoneNumber;
    @FXML
    private Label dataEmail;
    @FXML
    private Label dataBalance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getUsers();
    }

    public void setDataName(String dataName){
        this.dataName.setText(dataName);
    }

    public void setDataLastName(String dataLastName){
        this.dataLastName.setText(dataLastName);
    }

    public void setDataPhoneNumber(String dataPhoneNumber){
        this.dataPhoneNumber.setText(dataPhoneNumber);
    }

    public void setDataEmail(String dataEmail){
        this.dataEmail.setText(dataEmail);
    }

    public void setDataBalance(String dataBalance){
        this.dataBalance.setText(dataBalance);
    }


    private void getUsers(){

        yPane.getChildren().clear();
    
        JSONObject obj = new JSONObject("{}");
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles", obj.toString(), (response) -> {

            JSONObject objResponse = new JSONObject(response);
            if (objResponse.getString("status").equals("OK")) {

                JSONArray JSONlist = objResponse.getJSONArray("profiles"); //Este devuelve un array de objetos json
                URL resource = this.getClass().getResource("./src/userItemView.fxml");

                for(int i = 0; i < JSONlist.length(); i++){ 
                    
                    // Get console information
                    JSONObject user = JSONlist.getJSONObject(i);
                    
                    System.out.println(user.getString("userName"));

                    try{
                        // Load template and set controller
                        FXMLLoader loader = new FXMLLoader(resource);
                        Parent itemTemplate = loader.load();
                        UserItemController userItemController = loader.getController();

                        System.out.println(user.getString("userName"));
                        System.out.println(user.getString("userLastName"));
                        System.out.println(user.getString("userPhoneNumber"));

                        // Fill template with console information
                        userItemController.setName("Name: " + user.getString("userName"));
                        userItemController.setLastName("Last Name: " + user.getString("userLastName"));
                        userItemController.setPhoneNumber("Phone Number: " + user.getString("userPhoneNumber"));
                        userItemController.setEmail("Email: " + user.getString("userEmail"));
                        userItemController.setBalance("Balance: " + user.getString("balance"));
                        yPane.getChildren().add(itemTemplate);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

        });
    }

}
