package library; 

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.*;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import library.Main;

public class CtrlLogin implements Initializable
{
    @FXML
    private TextField txt_account;
    @FXML
    private PasswordField txt_pswd;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
    	txt_account.setText("admin");
    }
    
    @FXML
    private void btn_login_clicked(ActionEvent event)
    {
    	String account,pswd;
    	account = txt_account.getText();
    	pswd = txt_pswd.getText();
    	if(account.equals(""))
    	{
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	         	"«Îœ»ÃÓ–¥’À∫≈£°", "æØ∏Ê", JOptionPane.WARNING_MESSAGE);
     		return;
    	}
    	if(pswd.equals(""))
    	{
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	         "«Îœ»ÃÓ–¥√‹¬Î£°", "æØ∏Ê", JOptionPane.WARNING_MESSAGE);
         	return;
    	}
    	if(account.equals("admin")&&pswd.equals("666"))
    	{
    		Main.setAdminScene();
    	}
    	else
    	{
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	         "’À∫≈ªÚ√‹¬Î¥ÌŒÛ£°", "æØ∏Ê", JOptionPane.WARNING_MESSAGE);
         	return;
    	}
    }
    
    @FXML
    private void btn_exit_clicked(ActionEvent event)
    {
    	Event.fireEvent(Main.getPrimaryStage(),
    			new WindowEvent(Main.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    
}
