package objprosjekt;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JourneyPlanController {

    @FXML
    private Ellipse NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio, Reykjavik, Moscow,
            BuenosAires, Beijing;

    @FXML
    private AnchorPane anker;
    @FXML
    private Canvas panel;
    @FXML
    private Button btnLast, btnNext, btnAdd, btnDelete;
    @FXML
    private Label txtCurrentJourney;

    private JourneyPlan travelPlan;
    private Journey currentJourney;

    public JourneyPlanController() {
        travelPlan = new JourneyPlan();
        currentJourney = travelPlan.getJourney(0);
        // write();
    }

    @FXML
    private void nextJourney() {
        if (travelPlan.getSize() > travelPlan.getIndex(currentJourney) + 1) {// -1
            currentJourney = travelPlan.getJourney(travelPlan.getIndex(currentJourney) + 1);// -1
        }
        refresh();
    }

    @FXML
    private void lastJourney() {
        if (travelPlan.getIndex(currentJourney) >= 1) {
            currentJourney = travelPlan.getJourney(travelPlan.getIndex(currentJourney) - 1);
        }
        refresh();
    }

    @FXML
    private void newJourney() {
        travelPlan.addJourney(new Journey());
        currentJourney = travelPlan.getJourney(travelPlan.getSize() - 1);// -1
        refresh();
    }

    @FXML
    private void deleteJourney() {
        if (travelPlan.getSize() > 1) {
            int a = travelPlan.getIndex(currentJourney);
            travelPlan.removeJourney(currentJourney);
            currentJourney = travelPlan.getJourney(a);
            refresh();
        } else {
            currentJourney.clear();
            refresh();
        }

    }

    @FXML
    private void pinEvent(MouseEvent e) {
        if (currentJourney.isIn(((Ellipse) e.getSource()))) {
            currentJourney.removeCity(((Ellipse) e.getSource()));
            ((Ellipse) e.getSource()).setFill(Color.RED);
        } else {
            currentJourney.addCity(((Ellipse) e.getSource()));
            ((Ellipse) e.getSource()).setFill(Color.GREEN);
        }
        refresh();
    }

    private void refresh() {
        write();
        pinCheck();
    }

    private void pinCheck() {
        Ellipse[] btnArray = { NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio,
                Reykjavik, Moscow, BuenosAires, Beijing };
        List<Ellipse> btnList = Arrays.asList(btnArray);
        for (Ellipse k : btnList) {
            if (currentJourney.isIn(k)) {
                k.setFill(Color.GREEN);
            } else {
                k.setFill(Color.RED);
            }
        }
    }

    private void write() {
        String tekst = "Reise: " + (travelPlan.getIndex(currentJourney) + 1) + "\nByer:\n";
        for (int k = 0; k < currentJourney.getSize(); k++) {
            tekst += (k + 1) + ": " + currentJourney.getCity(k).getId() + "\n";
        }
        tekst += "Total Reiseavstand " + currentJourney.distance() + "Km";
        txtCurrentJourney.setText(tekst);
    }

}
