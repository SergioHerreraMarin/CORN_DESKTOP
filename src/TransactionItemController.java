import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TransactionItemController {
    
    @FXML
    private Label origin;
    @FXML
    private Label destination;
    @FXML
    private Label amount;
    @FXML
    private Label timeFinish;


    public void setOrigin(String origin) {
        this.origin.setText(origin);
    }
    public void setDestination(String destination) {
        this.destination.setText(destination);
    }
    public void setAmount(String amount) {
        this.amount.setText(amount);
    }
    public void setTimeFinish(String timeFinish) {
        this.timeFinish.setText(timeFinish);
    }


}
