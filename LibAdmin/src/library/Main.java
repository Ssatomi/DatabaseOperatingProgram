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
	private Pane rootpane = null,loginpane=null;
	private static Scene rootscene = null,loginscene=null;
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    ObservableList<String> ob_admin = FXCollections.observableArrayList();
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			primaryStage.setTitle("图书管理系统-管理员");
			primarystage = primaryStage;
			rootpane = FXMLLoader.load(getClass().getResource("view/admin.fxml"));
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
	
	public static void setAdminScene()
	{
		primarystage.setScene(rootscene);
	}
	
	public static Stage getPrimaryStage()
	{
		return primarystage;
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
