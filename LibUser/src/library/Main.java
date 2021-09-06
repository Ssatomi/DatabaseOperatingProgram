package library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application 
{
	static Stage primarystage = null;
	private Pane rootpane = null;
	private Pane loginpane = null;
	private static Scene rootscene = null;
	private static Scene loginscene = null;
	static String user = "大哥牛";
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    ObservableList<String> ob_admin = FXCollections.observableArrayList();
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			primaryStage.setTitle("图书管理系统-用户");
			primarystage = primaryStage;
			rootpane = FXMLLoader.load(getClass().getResource("view/user.fxml"));
			loginpane = FXMLLoader.load(getClass().getResource("view/login.fxml"));
			rootscene = new Scene(rootpane);
			loginscene = new Scene(loginpane);
			primarystage.setScene(loginscene);
			primarystage.show();
			
			Class.forName(JDBC_DRIVER);
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void setLoginScene()
	{
		primarystage.setScene(loginscene);
	}
	
	public static void setUserScene()
	{
		primarystage.setScene(rootscene);
	}
	
	public static Stage getPrimaryStage()
	{
		return primarystage;
	}
	
	public static void setUserNum(String usernum)
	{
		user = usernum;
	}
	
	public static String getUser()
	{
		return user;
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
