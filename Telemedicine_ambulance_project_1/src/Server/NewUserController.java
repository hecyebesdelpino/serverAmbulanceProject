/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Patient.Users;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AdriCortellucci
 */
public class NewUserController implements Initializable {
    @FXML private Label error;
    @FXML private TextField userName;
    @FXML private TextField password1;
    @FXML private TextField password2;
    
    private UsersWindowController userController;
    private Users user;
    
    public boolean checkNoSpaces(String s){
        if (s == null) { // checks if the String is null
            return false;
        }
        if (s.equals("")) { 
            return false;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (Character.isSpaceChar(s.charAt(i))) {
                return false;
            }
        }
        return true;
    
    }
    
    public void initData(UsersWindowController u){
        this.userController=u;
    }
    
    @FXML
    public void create(ActionEvent event) throws IOException {
        error.setText("");
        if(!checkNoSpaces(userName.getText())){
            error.setText("Not a valid user");
        }else if(!password1.getText().equals(password2.getText())){
            error.setText("Passwords don´t match");
        }else if(!checkNoSpaces(password1.getText())){
            error.setText("Not a valid password");
        }else{
            user= new Users(userName.getText(), password1.getText());
            userController.addUser(user);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
            
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
