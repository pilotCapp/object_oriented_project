package objprosjekt;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class JourneyPlanController {

    @FXML
    private Ellipse NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio, Reykjavik, Moscow,
            BuenosAires, Beijing, MexicoCity, Bogota, Sydney;

    @FXML
    private AnchorPane anker;
    @FXML
    private Canvas panel;
    @FXML
    private Button btnLast, btnNext, btnAdd, btnDelete;
    @FXML
    private Label txtCurrentJourney;

    private JourneyPlan travelPlan;
    private static Hashtable<String, Ellipse> knapper;

    @FXML
    public void initialize() {
        openHash();
        travelPlan = new JourneyPlan();
        write();
        pinCheck();
        draw();
    }

    private void openHash() {
        knapper = new Hashtable<>();
        knapper.put("NewYork", NewYork);
        knapper.put("Oslo", Oslo);
        knapper.put("CapeTown", CapeTown);
        knapper.put("Mumbai", Mumbai);
        knapper.put("Paris", Paris);
        knapper.put("London", London);
        knapper.put("Madrid", Madrid);
        knapper.put("LosAngeles", LosAngeles);
        knapper.put("Toronto", Toronto);
        knapper.put("Rio", Rio);
        knapper.put("Reykjavik", Reykjavik);
        knapper.put("Moscow", Moscow);
        knapper.put("BuenosAires", BuenosAires);
        knapper.put("Beijing", Beijing);
        knapper.put("MexicoCity", MexicoCity);
        knapper.put("Bogota", Bogota);
        knapper.put("Sydney", Sydney);
    }

    @FXML
    private void nextJourney() {
        travelPlan.next();
        refresh();
    }

    @FXML
    private void lastJourney() {
        travelPlan.last();
        refresh();
    }

    @FXML
    private void newJourney() {
        travelPlan.addJourney();
        refresh();
    }

    @FXML
    private void deleteJourney() {
        travelPlan.removeJourney();
        refresh();
    }

    @FXML
    private void pinEvent(MouseEvent e) {
        travelPlan.pinPressed(((Ellipse) e.getSource()).getId(), knapper);
        refresh();
    }

    private void refresh() {
        write();
        pinCheck();
        draw();
        travelPlan.save();
    }

    private void pinCheck() {
        Ellipse[] btnArray = { NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio,
                Reykjavik, Moscow, BuenosAires, Beijing, MexicoCity, Bogota, Sydney };
        for (Ellipse k : btnArray) {
            if (travelPlan.getCurrentJourney().isIn(k.getId())) {
                k.setFill(Color.GREEN);
            } else {
                k.setFill(Color.RED);
            }
        }
    }

    private void write() {
        Ellipse[] btnArray = { NewYork, Oslo, CapeTown, Mumbai, Paris, London, Madrid, LosAngeles, Toronto, Rio,
                Reykjavik, Moscow, BuenosAires, Beijing, MexicoCity, Bogota, Sydney };
        String tekst = "Reise: " + (travelPlan.getIndex(travelPlan.getCurrentJourney()) + 1) + "\nByer:\n";
        for (int k = 0; k < travelPlan.getCurrentJourney().getSize(); k++) {
            tekst += (k + 1) + ": " + travelPlan.getCurrentJourney().getCity(k) + "\n";
        }
        if (travelPlan.getCurrentJourney().getSize() > 1) {
            tekst += "Total Reiseavstand " + travelPlan.getCurrentJourney().distance(knapper) + "Km";
        } else {
            tekst += "Total Reiseavstand " + 0 + "Km";
        }
        txtCurrentJourney.setText(tekst);
    }

    private void draw() {
        GraphicsContext mal = panel.getGraphicsContext2D();
        mal.clearRect(0, 0, panel.getWidth(), panel.getHeight());
        mal.beginPath();
        mal.setLineWidth(3);
        mal.setStroke(Color.RED);
        for (int k = 0; k < travelPlan.getCurrentJourney().getSize() - 1; k++) {
            mal.moveTo(toEllipse(travelPlan.getCurrentJourney().getCity(k)).getParent().getLayoutX() - 6,
                    toEllipse(travelPlan.getCurrentJourney().getCity(k)).getParent().getLayoutY() + 25);
            mal.lineTo(toEllipse(travelPlan.getCurrentJourney().getCity(k + 1)).getParent().getLayoutX() - 6,
                    toEllipse(travelPlan.getCurrentJourney().getCity(k + 1)).getParent().getLayoutY() + 25);

        }
        mal.stroke();
    }

    private Ellipse toEllipse(String navn) {
        if (knapper.get(navn) != null) {
            return (knapper.get(navn));
        } else {
            throw new IllegalArgumentException("den knappen finnes ikke (controller)");
        }
    }
}
