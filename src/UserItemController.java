import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserItemController {
       
    @FXML
    private Label name;
    @FXML
    private Label lastName;
    @FXML
    private Label phoneNumber;

    private String email;
    private String balance;


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

    
    @FXML
    private void handleMenuAction() {
        ListUsersController listUsersController = (ListUsersController) UtilsViews.getController("listUsersView");
        listUsersController.setDataName(name.getText());
        listUsersController.setDataLastName(lastName.getText());
        listUsersController.setDataPhoneNumber(phoneNumber.getText());
        listUsersController.setDataEmail(email);
        listUsersController.setDataBalance(balance);
    }

}
