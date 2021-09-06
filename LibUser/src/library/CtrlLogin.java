package library; 

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import library.Main;

public class CtrlLogin implements Initializable
{
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=ͼ���";
	static final String USER = "sa";
	static final String PASS = "chin654toyi";
    static Connection conn = null;
    static Statement stmt = null;
    static Statement stmt2 = null;
    ResultSet rs = null,rs2=null;
    String sql;
    @FXML
    private TextField txt_uname;
    @FXML
    private PasswordField txt_pswd;
    @FXML
    private ComboBox<String> com_user;
    
    ObservableList<String> user_table = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
	        sql = "select * from dbo.���� t";
	        rs = stmt.executeQuery(sql);
	        if(rs.next())
	        {
	        	user_table.add(rs.getString("���"));
	        	txt_uname.setText(rs.getString("����"));
	        }
	        while(rs.next())
	        {
	        	user_table.add(rs.getString("���"));
	        }
	        com_user.setItems(user_table);
	        if(user_table.size()>0)
	        	com_user.getSelectionModel().select(0);
  	         
  	        rs.close();
  	        stmt.close();
  	        conn.close();
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void com_user_action(Event event)
    {
    	String unum = com_user.getSelectionModel().getSelectedItem();
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
	        sql = "select t.���� from dbo.���� t where t.���='"+unum+"'";
	        rs = stmt.executeQuery(sql);
	        if(rs.next())
	        {
	        	txt_uname.setText(rs.getString("����").trim());
	        }
  	         
  	        rs.close();
  	        stmt.close();
  	        conn.close();
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_login_clicked(ActionEvent event)
    {
    	String account,pswd;
    	account = com_user.getSelectionModel().getSelectedItem();
    	pswd = txt_pswd.getText();
    	if(account.equals(""))
    	{
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	         	"������д�˺ţ�", "����", JOptionPane.WARNING_MESSAGE);
     		return;
    	}
    	if(pswd.equals(""))
    	{
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	         "������д���룡", "����", JOptionPane.WARNING_MESSAGE);
         	return;
    	}

		try
		{	
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
	        sql = "select t.���� from dbo.���� t where t.���='"+account+"'";
	        rs = stmt.executeQuery(sql);
	        if(rs.next())
	        {
	        	if(pswd.trim().equals(rs.getString("����").trim()))
	        	{
	        		Main.setUserNum(account);
	        		Main.setUserScene();
	        	}
	        	else
	        	{
	         		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	           	         "�˺Ż��������", "����", JOptionPane.WARNING_MESSAGE);
	        	}
	        }
  	         
  	        rs.close();
  	        stmt.close();
  	        conn.close();
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_exit_clicked(ActionEvent event)
    {
    	Event.fireEvent(Main.getPrimaryStage(),
    			new WindowEvent(Main.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    
}
