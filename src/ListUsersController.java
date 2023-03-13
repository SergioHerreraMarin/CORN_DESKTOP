import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;


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
    @FXML
    private ComboBox userStatusCombobox;
    @FXML
    private ImageView dniFrontImageView;
    @FXML
    private ImageView dniBackImageView;

    ObservableList<String> userStatusComboboxOptions = 
    FXCollections.observableArrayList(
        "NO_VERFICAT",
        "A_VERIFICAR",
        "ACCEPTAT",
        "REBUTJAT"
    );


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userStatusCombobox.setItems(userStatusComboboxOptions);
        userStatusCombobox.getSelectionModel().selectFirst();
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

    public void setDniFront(Image image){
        dniFrontImageView.setImage(image);
        dniFrontImageView.setPreserveRatio(true);
    }

    public void setDniBack(Image image){
        dniBackImageView.setImage(image);
        dniBackImageView.setPreserveRatio(true);
    }

    public void setUserStatusComboboxDefaultOption(String option){
        userStatusCombobox.setValue(option); 
    }

    public void clearImages(){
        dniFrontImageView.setImage(null);
        dniBackImageView.setImage(null);
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
                            transactionItemController.setOrigin(transaction.getInt("origin"));
                        }

                        transactionItemController.setDestination(transaction.getInt("destination"));
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

    @FXML
    private void userStatusFilter(){
        
        List<String> choices = new ArrayList<>();
        choices.add("NO_VERFICAT");
        choices.add("A_VERIFICAR");
        choices.add("ACCEPTAT");
        choices.add("REBUTJAT");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("NO_VERFICAT", choices);
        dialog.setTitle("Filtrar per estat d'usuari");
        dialog.setHeaderText("Filtrar per estat d'usuari");
        dialog.setContentText("Estat de usuari:");

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()){
            yPane.getChildren().clear();
            System.out.println("Your choice: " + result.get());
            JSONObject obj = new JSONObject("{}");
            obj.put("status", result.get());

            UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles_by_status", obj.toString(), (response) -> {

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
    }


    @FXML
    private void userBalanceFilter(){

        Optional<Pair<String,String>> result = customDialog("Filtrar per balanç", "Filtrar usuaris per rang de balanç");
    
        if(result.isPresent()){
            yPane.getChildren().clear();
            JSONObject obj = new JSONObject("{}");
            obj.put("minBalance", result.get().getKey());
            obj.put("maxBalance", result.get().getValue());
            UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles_by_range_balance", obj.toString(), (response) -> {

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
    }


    @FXML
    private void userTransaccionsAmountFilter(){
        Optional<Pair<String,String>> result = customDialog("Filtrar per número de transaccions", "Filtrar usuaris per número de transaccions");
        
        if(result.isPresent()){
            yPane.getChildren().clear();
            JSONObject obj = new JSONObject("{}");
            obj.put("numMin", result.get().getKey());
            obj.put("numMax", result.get().getValue());

            UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_profiles_by_range_num_transactions", obj.toString(), (response) -> {

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
    }


    @FXML
    private void closeDesktop(){
        System.out.println("Cerrar app");
    }


    private Optional<Pair<String, String>> customDialog(String title, String header){

        Dialog<Pair<String,String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        
        ButtonType filterButton = new ButtonType("Filtrar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(filterButton, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField minBalance = new TextField();
        minBalance.setPromptText("Min");
        TextField maxBalance = new TextField();
        maxBalance.setPromptText("Max");

        grid.add(new Label("Min:"), 0, 0);
        grid.add(minBalance, 1, 0);
        grid.add(new Label("Max:"), 0, 1);
        grid.add(maxBalance, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == filterButton) {
                return new Pair<>(minBalance.getText(), maxBalance.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        return result;
    }


    @FXML
    private void modifyUserStatus(){

        JSONObject obj = new JSONObject("{}");
            obj.put("phone", dataPhoneNumber.getText());
            obj.put("status", userStatusCombobox.getValue());
            System.out.println(dataPhoneNumber.getText());
            System.out.println(userStatusCombobox.getValue());

        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/upload_status", obj.toString(), (response) -> {

            JSONObject objResponse = new JSONObject(response);
            if (objResponse.getString("status").equals("OK")) {
                System.out.println(objResponse.getString("message"));
            }else{
                System.out.println(objResponse.getString("message"));
            }
        });

    }


    @FXML
    private void clearFilters(){
        getUsers();
    }

}
//insert into Transactions(origin,destination,amount,token,accepted,timeSetup,timeStart,timeFinish) values (984752897, 661571197,'45',"abcde",false,NOW(),NOW(),NOW());