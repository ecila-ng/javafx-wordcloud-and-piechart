/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

/**
 * FXML Controller class
 *
 * @author baotr
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField addField;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button quitButton;

    @FXML
    private PieChart myPieChart;

    @FXML
    private AnchorPane myCloud;

    @FXML
    public void updatePie(ActionEvent e) {
        Button clicked = (Button) e.getSource();
        boolean matchFound = false;

        //if the add button was clicked with text in the text field
        if (clicked == addButton) {
            if (addField.getText().length() > 0) {
                for (PieChart.Data w : myPieChart.getData()) {
                    if (w.getName().equals(addField.getText())) {
                        popupWindow("Duplicate text!");
                        matchFound = true;
                        //Do something?
                    }
                }
                if (!matchFound) {
                    addStuffs();
                }
            } else //If there is no text
            {
                popupWindow("No text was entered!");
            }
        }
        addField.setText(""); //reset text after added
    }

    //This function adds pie and cloud
    private void addStuffs() {

        //make a new slice of pie
        PieChart.Data newSlice = new PieChart.Data(addField.getText(), 1);
        //add the slice to the pie
        myPieChart.getData().add(newSlice);
        myPieChart.setLabelsVisible(false);

        //make a new piece of cloud
        Label newWord = new Label();
        newWord.setText(addField.getText());
        newWord.setFont(new Font(newWord.getFont().getName(), 30));

        //Random color for the word
        List<Color> temp = new ArrayList<>();
        temp.add(Color.web("#EE4035"));
        temp.add(Color.web("#F37736"));
        temp.add(Color.web("#FDF498"));
        temp.add(Color.web("#7BC043"));
        temp.add(Color.web("#0392CF"));
        Random random = new Random();
        newWord.setTextFill(temp.get(random.nextInt(temp.size())));

        placeWord(newWord);

        //if the pie slice is clicked on, add points
        newSlice.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            int newPieValue = ((int) newSlice.getPieValue()) + 1;
            newSlice.setPieValue(newPieValue);

            double currFontSize = newWord.getFont().getSize();
            newWord.setFont(new Font(newWord.getFont().getName(), currFontSize + 2));
            placeWord(newWord);
        });

        //if the word is clicked on in cloud, add points
        newWord.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            int newPieValue = ((int) newSlice.getPieValue()) + 1;
            newSlice.setPieValue(newPieValue);

            double currFontSize = newWord.getFont().getSize();
            newWord.setFont(new Font(newWord.getFont().getName(), currFontSize + 2));
            placeWord(newWord);
        });
    }

    //This function clears everything off the scene
    public void clearAll(ActionEvent e) {
        Button clicked = (Button) e.getSource();

        if (clicked == clearButton) {
            myPieChart.getData().clear();
            myCloud.getChildren().clear();
            addField.setText("");
        }
    }

    public void quit(ActionEvent e) {
        Button clicked = (Button) e.getSource();

        //if the Quit button was clicked
        if (clicked == quitButton) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Quit?");
            alert.setContentText("Are you sure you want to quit?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            } else if (result.get() == ButtonType.CANCEL) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //This function creates pop-up window 
    private void popupWindow(String string) {
        Scene scene = new Scene(new Group(new Text(25, 25, string)));

        Stage myStage = new Stage();

        myStage.setTitle("Oops");
        myStage.setScene(scene);
        myStage.sizeToScene();
        myStage.setMinWidth(225.00);
        myStage.setMinHeight(150.00);
        myStage.showAndWait();
        VBox box = new VBox();
        myStage.setScene(new Scene(box));
    }

    //This function adds word into the cloud
    public void placeWord(Label newWord) {

        boolean intersects;
        intersects = causesIntersection(newWord);
        //System.out.println("Start: " + intersects);
        double radius = 1;
        double angle = 0;
        double middleX = myCloud.getWidth() / 2;
        double middleY = myCloud.getHeight() / 2;

        if (intersects == false) {
            double xPos = middleX + radius * Math.cos(angle);
            double yPos = middleY + radius * Math.sin(angle);
            newWord.setLayoutX(xPos);
            newWord.setLayoutY(yPos);
            newWord.setRotate(0);
            //System.out.println(":" + middleX + ":" + middleY + ":" + xPos + ":" + yPos);
            myCloud.getChildren().add(newWord);
        } else if (intersects == true) {
            while (true) {
                //System.out.println(": " + intersects);
                radius += 0.5;
                angle += 0.1;

                double xPos = middleX + radius * Math.cos(angle);
                double yPos = middleY + radius * Math.sin(angle);

                //System.out.println("r: " + radius + " a:" + angle);
                newWord.setLayoutX(xPos);
                newWord.setLayoutY(yPos);
                newWord.setRotate(90);

                //myCloud.getChildren().add(newWord);
                intersects = causesIntersection(newWord);
                System.out.println(": " + intersects);
                if (intersects == false) {

                    newWord.setLayoutX(xPos);
                    newWord.setLayoutY(yPos);
                    myCloud.getChildren().add(newWord);
                    break;
                }
            }
        }
    }

    //This function checks if two words are touching each other
    public boolean causesIntersection(Label newWord) {
        boolean flag = false;
        List<String> temp = new ArrayList<>();
        //Looping through every other words in the cloud
        for (Node n : myCloud.getChildren()) {
            Label oldWord = (Label) n;

            Bounds bound1 = newWord.getBoundsInParent();
            Bounds bound2 = oldWord.getBoundsInParent();
            System.out.println(newWord + ".vs" + oldWord);
            flag = (bound2.intersects(bound1) && (oldWord.equals(newWord) == false));
            temp.add(Boolean.toString(flag));
        }
        System.out.println("Result: " + temp);

        //Make sure it is not touching ANY other words
        if (temp.contains("true")) {
            return true;
        } else {
            return false;
        }
    }
}
