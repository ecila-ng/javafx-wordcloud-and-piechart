/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private PieChart myPieChart;
    
    @FXML
    private AnchorPane myCloud;
    
      
    @FXML
    public void updatePie(ActionEvent e){
        
        Button clicked = (Button) e.getSource();
        
        boolean matchFound = false;
        
        //if the enter button was pressed for the add field
        if(clicked == addButton) {            
            if(addField.getText().length() > 0)
            {
                    for ( PieChart.Data w : myPieChart.getData() )
                    {
                        if (w.getName().equals(addField.getText()) )
                        {
                            //If it was duplicate
                            matchFound = true;
                            
                            //DO SOMETHING
                        }
                    }
                    if(!matchFound)
                    {
                        addStuffs();
                    }
            }
            else 
                //IF THERE IS NO TEXT
                popupWindow( "No text was entered!");
            
        } 
            addField.setText(""); //reset text
            
            
}
    //This adds pie and cloud
    private void addStuffs()
    {
        //make a new slice of pie
        PieChart.Data newSlice = new PieChart.Data(addField.getText(), 1);
        //add the slice to the pie
        myPieChart.getData().add(newSlice);
        myPieChart.setLabelsVisible(false);
        
        //make a new piece of cloud
        Label newWord = new Label();
        newWord.setText(addField.getText());        
        placeWord(newWord);
        
        //if the pie slice is clicked on, add votes
        newSlice.getNode().addEventHandler( MouseEvent.MOUSE_CLICKED,
                
                new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent e)
                    {
                        //add a vote and use magic to force it into an int
                        int newPieValue = ((int)newSlice.getPieValue())+2;
                        newSlice.setPieValue(newPieValue);
                                                
                    }
                }
        );
        
        
    }
    
    public void clearAll(ActionEvent e) {
        Button clicked = (Button) e.getSource();
        
        //if the clear button was clicked
        if(clicked == clearButton) {     
        myPieChart.getData().clear();
        addField.setText("");
    }}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

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
    
    
    public void placeWord(Label newWord)    {
        
        //double currFontSize = newWord.getFont().getSize();  
        //newWord.setFont(new Font(newWord.getFont().getName(), currFontSize+2));
        
        boolean intersects = false;        
        double radius=1;        
        double angle=0;        
        double middleX = myCloud.getWidth()/2;        
        double middleY = myCloud.getHeight()/2;        
        do {            
            double xPos = middleX + radius*Math.cos(angle);            
            double yPos = middleY + radius*Math.sin(angle);                   
            newWord.setLayoutX(xPos);            
            newWord.setLayoutY(yPos);            
            newWord.setRotate(0);            
            //intersects = causesIntersection(newWord);            
            if (intersects) {                
                newWord.setRotate(90);                
                //intersects = causesIntersection(newWord);            
            }            
            radius+=0.5;            
            angle+=0.1;    
            
            myCloud.getChildren().add(newWord);
        }       
        while (intersects);    
    }
    
}
