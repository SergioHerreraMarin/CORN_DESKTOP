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
    private VBox transactionsyPane = new VBox();
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
    @FXML
    private Label dataUserStatus;
    @FXML
    private Label dataLastStatusModified;

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

    public void setUserStatus(String userStatus){
        this.dataUserStatus.setText(userStatus);
    }

    public void setLastStatusModified(String lastStatusModified){
        this.dataLastStatusModified.setText(lastStatusModified);
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
                        System.out.println(user.getString("userStatus"));
                        System.out.println(user.getString("userStatusModifyTime"));
                        
                        // Fill user item 
                        userItemController.setName(user.getString("userName"));
                        userItemController.setLastName(user.getString("userLastName"));
                        userItemController.setPhoneNumber(user.getString("userPhoneNumber"));
                        userItemController.setEmail(user.getString("userEmail"));
                        userItemController.setBalance(user.getString("userBalance"));
                        userItemController.setStatus(user.getString("userStatus"));
                        userItemController.setLastStatusModified(dateFormat(user.getString("userStatusModifyTime")));
                        yPane.getChildren().add(itemTemplate);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getTransactionsUser(String phoneNumber){

        transactionsyPane.getChildren().clear();

        JSONObject obj = new JSONObject("{}");
        obj.put("phone", phoneNumber);
        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/transaccions", obj.toString(), (response) -> {

            JSONObject objResponse = new JSONObject(response);
            if (objResponse.getString("status").equals("OK")) {

                JSONArray JSONlist = objResponse.getJSONArray("transactions"); //Este devuelve un array de objetos json
                URL resource = this.getClass().getResource("./src/transactionItemView.fxml");

                for(int i = 0; i < JSONlist.length(); i++){ 
                    JSONObject transaction = JSONlist.getJSONObject(i);
                    
                    try{
                        FXMLLoader loader = new FXMLLoader(resource);
                        Parent itemTemplate = loader.load();
                        TransactionItemController transactionItemController = loader.getController();
                        if(transaction.getInt("accepted") == 1){
                            transactionItemController.setOrigin(transaction.getString("origin"));
                        }else{
                            transactionItemController.setOrigin("");
                        }
                        transactionItemController.setDestination(transaction.getString("destination"));
                        transactionItemController.setAmount(transaction.getString("amount"));
                        transactionItemController.setTimeFinish(dateFormat(transaction.getString("timeFinish")));
                        transactionsyPane.getChildren().add(itemTemplate);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String dateFormat(String date){
        String newDateFormat = "";
        newDateFormat = date.replace('T', ' ');
        newDateFormat = newDateFormat.replace(".000Z", "");
        return newDateFormat;
    }

}
//insert into Transactions(origin,destination,amount,token,accepted,timeSetup,timeStart,timeFinish) values (984752897, 661571197,'45',"abcde",false,NOW(),NOW(),NOW());