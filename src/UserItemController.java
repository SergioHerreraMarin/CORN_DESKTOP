import java.util.Base64;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class UserItemController {
       
    @FXML
    private Label name;
    @FXML
    private Label lastName;
    @FXML
    private Label phoneNumber;
    
    private String email;
    private String balance;
    private String userStatus;
    private String lastStatusModified;
    private Image dniFront;
    private Image dniBack;

    public void setName(String name){
        this.name.setText(name);
    }

    public void setLastName(String lastName){
        this.lastName.setText(lastName);
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber.setText(phoneNumber);
    }   

    public void setEmail(String email){
        this.email = email;
    }

    public void setBalance(String balance){
        this.balance = balance;
    }

    public void setStatus(String userStatus){
        this.userStatus = userStatus;
    }

    public void setLastStatusModified(String lastStatusModified){
        this.lastStatusModified = lastStatusModified;
    }


    @FXML
    private void handleMenuAction() {
        
        JSONObject obj = new JSONObject("{}");
        obj.put("phone", phoneNumber.getText());

        ListUsersController listUsersController = (ListUsersController) UtilsViews.getController("listUsersView");

        UtilsHTTP.sendPOST(Main.protocol + "://" + Main.host + ":" + Main.port + "/api/get_id", obj.toString(), (response) -> {
            
            JSONObject objResponse = new JSONObject(response);

            if(objResponse.getString("status").equals("OK")){
                String base64Front = objResponse.getString("imageFront");
                String base64Back = objResponse.getString("imageBack");

                byte[] decodedBytesFront = Base64.getDecoder().decode(base64Front);
                byte[] decodedBytesBack= Base64.getDecoder().decode(base64Back);

                dniFront = new Image(new java.io.ByteArrayInputStream(decodedBytesFront));
                dniBack = new Image(new java.io.ByteArrayInputStream(decodedBytesBack));
                listUsersController.setDniFront(dniFront);
                listUsersController.setDniBack(dniBack);
            }else{
                System.out.println(objResponse.getString("message"));
            }
        });

        listUsersController.clearImages();
        listUsersController.setDataName(name.getText());
        listUsersController.setDataLastName(lastName.getText());
        listUsersController.setDataPhoneNumber(phoneNumber.getText());
        listUsersController.setDataEmail(email);
        listUsersController.setDataBalance(balance);
        listUsersController.setUserStatus(userStatus);
        listUsersController.setLastStatusModified(lastStatusModified);
        listUsersController.getTransactionsUser(phoneNumber.getText());
        listUsersController.setUserStatusComboboxDefaultOption(userStatus);
    }

}
