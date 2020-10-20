import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Restaurant;
import model.Server;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowBillController implements Initializable {
    static Restaurant restaurant0520;
    static Integer tableNumber;
    static Server server;
    @FXML
    TextArea billTxt;
    @FXML
    Label tableNumLabel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    try {
    server = (Server) restaurant0520.getEmployee("S:" + 0);
    tableNumLabel.setText(tableNumber.toString());
    billTxt.setText(server.printBillFx(tableNumber));
    }catch (NullPointerException ex) {
    }



    }

    public void setTableNumber(Integer num) {
        ShowBillController.tableNumber = num;
    }
    public void setRestaurant0520(Restaurant restaurant){ restaurant0520 = restaurant; }

}
