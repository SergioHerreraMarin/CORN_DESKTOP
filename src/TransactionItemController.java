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


    public void setOrigin(int origin) {
        this.origin.setText(Integer.toString(origin));
    }
    public void setDestination(int destination) {
        this.destination.setText(Integer.toString(destination));
    }
    public void setAmount(String amount) {
        this.amount.setText(amount);
    }
    public void setTimeFinish(String timeFinish) {
        this.timeFinish.setText(timeFinish);
    }


}
