package library; 

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import library.Main;
import library.module.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrlAdmin implements Initializable
{
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=图书馆";
	static final String USER = "sa";
	static final String PASS = "chin654toyi";
    static Connection conn = null;
    static Statement stmt = null;
    static Statement stmt2 = null;
    ResultSet rs = null,rs2 = null;
    String sql;
    ObservableList<String> ob_version = FXCollections.observableArrayList();
    ObservableList<String> ob_isbn = FXCollections.observableArrayList();
    
    ObservableList<BookInfo> search_book_table = FXCollections.observableArrayList();
    ObservableList<BookInfo> cut_book_table = FXCollections.observableArrayList();
    
    ObservableList<User> user_table = FXCollections.observableArrayList();
    ObservableList<User> search_user_table = FXCollections.observableArrayList();
    
    ObservableList<BorrowInfo> borrow_table = FXCollections.observableArrayList();
    ObservableList<BorrowInfo> return_table = FXCollections.observableArrayList();
    
    ObservableList<BrStat> br_table = FXCollections.observableArrayList();
    ObservableList<Rank> rank_table = FXCollections.observableArrayList();
    
    
    @FXML
    public ComboBox<String> com_version,com_isbn;
    @FXML
    public DatePicker datepicker,datebegin,dateend;
    @FXML
    Tab tab_borrow,tab_return,tab_usersearch,tab_booksearch,tab_addbook,tab_view,tab_br,tab_rank;
    @FXML
    private TextField txt_bname,txt_press,txt_author,txt_search,
    txt_store,txt_user,txt_usearch,txt_pswd;
    @FXML
    private TableColumn<?, ?> col_bname,col_author,col_press,col_ymd,col_version,
    col_isbn,col_barcode,col_store,col_state,col_unum,col_uname,col_bcount,col_tfine,col_pswd;
    @FXML
    private TableColumn<?, ?> col_bnum,col_bcode,col_bnam,col_usernum,col_user,col_btime,
    col_bnum1,col_bcode1,col_bnam1,col_usernum1,col_user1,col_btime1
    ,col_bt,col_bs,col_rt,col_rs,col_rbname,col_risbn,col_rtotal;
    @FXML
    private TableView<BookInfo> table_book;
    @FXML
    private TableView<BorrowInfo> table_borrow,table_return;
    @FXML
    private ChoiceBox<String> choice_view;
    @FXML
    private TableView<User> table_user;
    @FXML
    private TableView<BrStat> table_br;
    @FXML
    private TableView<Rank> table_rank;
   
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
    	datepicker.setValue(LocalDate.now());
    	dateend.setValue(LocalDate.now());
    	datebegin.setValue(LocalDate.now());
    	
    	col_rbname.setCellValueFactory(new PropertyValueFactory<>("rabname"));  
    	col_risbn.setCellValueFactory(new PropertyValueFactory<>("raisbn"));  
    	col_rtotal.setCellValueFactory(new PropertyValueFactory<>("ratotal")); 
    	
    	col_bt.setCellValueFactory(new PropertyValueFactory<>("btotal"));  
    	col_bs.setCellValueFactory(new PropertyValueFactory<>("bsucc"));  
    	col_rt.setCellValueFactory(new PropertyValueFactory<>("rtotal")); 
    	col_rs.setCellValueFactory(new PropertyValueFactory<>("rsucc"));
    	
    	col_unum.setCellValueFactory(new PropertyValueFactory<>("unum"));  
    	col_uname.setCellValueFactory(new PropertyValueFactory<>("uname"));  
    	col_bcount.setCellValueFactory(new PropertyValueFactory<>("bcount")); 
    	col_tfine.setCellValueFactory(new PropertyValueFactory<>("tfine"));
    	col_pswd.setCellValueFactory(new PropertyValueFactory<>("pswd"));  
    	
    	col_bname.setCellValueFactory(new PropertyValueFactory<>("bookName"));  
    	col_author.setCellValueFactory(new PropertyValueFactory<>("author"));  
    	col_press.setCellValueFactory(new PropertyValueFactory<>("press")); 
    	col_ymd.setCellValueFactory(new PropertyValueFactory<>("ymd"));
    	col_version.setCellValueFactory(new PropertyValueFactory<>("version"));  
    	col_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    	col_barcode.setCellValueFactory(new PropertyValueFactory<>("barCode"));
    	col_store.setCellValueFactory(new PropertyValueFactory<>("store"));
    	col_state.setCellValueFactory(new PropertyValueFactory<>("state"));
    	
    	col_bnum.setCellValueFactory(new PropertyValueFactory<>("borrowNum"));
    	col_bcode.setCellValueFactory(new PropertyValueFactory<>("bCode"));  
    	col_bnam.setCellValueFactory(new PropertyValueFactory<>("bNam"));
    	col_usernum.setCellValueFactory(new PropertyValueFactory<>("userNum"));
    	col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
    	col_btime.setCellValueFactory(new PropertyValueFactory<>("bTime"));
    	
    	col_bnum1.setCellValueFactory(new PropertyValueFactory<>("borrowNum"));
    	col_bcode1.setCellValueFactory(new PropertyValueFactory<>("bCode"));  
    	col_bnam1.setCellValueFactory(new PropertyValueFactory<>("bNam"));
    	col_usernum1.setCellValueFactory(new PropertyValueFactory<>("userNum"));
    	col_user1.setCellValueFactory(new PropertyValueFactory<>("user"));
    	col_btime1.setCellValueFactory(new PropertyValueFactory<>("bTime"));
    	
    	choice_view.setItems(FXCollections.observableArrayList("所有记录","在馆","借出","损坏"));
    	choice_view.getSelectionModel().select(0);
    	
    	choice_view.getSelectionModel().selectedItemProperty().addListener
    	(
    			(observable,oldValue,newValue)-> 
    			{
    				cut_book_table.clear();
    	        	int sel = choice_view.getSelectionModel().getSelectedIndex();
    	        	
    	    		try
    	    		{
    	     	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
    	     	        stmt = conn.createStatement();
    	     	        stmt2 = conn.createStatement();
        	        	if(sel==0)
        	        	{
            		        sql = "select * from dbo.图书 t left join dbo.馆藏 t2 on t.索书号=t2.索书号"
            		        		+ " order by t.索书号,t2.条码";
        	        	}
        	        	else if(sel==1)
        	        	{
            		        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号 "
            		        		+ "and t2.状态=1 order by t.索书号,t2.条码";
        	        	}
        	        	else if(sel==2)
        	        	{
            		        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号 "
            		        		+ "and t2.状态=2 order by t.索书号,t2.条码";
        	        	}
        	        	else
        	        	{
            		        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号 "
            		        		+ "and t2.状态=0 order by t.索书号,t2.条码";
        	        	}
        		        rs = stmt.executeQuery(sql);
        		        String bname,author,press,ymd,version,isbn,barcode,store,state;
        		        while(rs.next())
        		        {
        		        	state = rs.getString("状态");
        		        	bname = rs.getString("题名");
        		        	isbn = rs.getString("索书号").trim();
        		        	press = rs.getString("出版社");
        		        	ymd = rs.getString("发行时间");
        		        	version = rs.getString("版本");
        		        	barcode = rs.getString("条码");
        		        	store = rs.getString("馆藏地点");
        		        	System.out.println(press);
        		        	if(bname!=null)
        		        		bname = bname.trim();
        		        	if(press!=null)
        		        		press = press.trim();
        		        	if(ymd!=null)
        		        		ymd = ymd.trim();
        		        	if(barcode!=null)
        		        		barcode = barcode.trim();
        		        	if(store!=null)
        		        		store = store.trim();
        		        	System.out.println("stat="+state);
        		        	if(state==null)
        		        		state = "";
        		        	else if(state.equals("0"))
        		        		state = "损坏";
        		        	else if(state.equals("1"))
        		        		state = "在馆";
        		        	else if(state.equals("2"))
        		        		state = "借出";
        		        	else
        		        		state = "";
        		        	
        		        	sql = "select distinct 作者 from dbo.著作 where '"+isbn+"'=索书号";
        		        	rs2 = stmt2.executeQuery(sql);
        		        	author = "";
        		        	String tmpau;
        		        	while(rs2.next())
        		        	{
        		        		if(author.isEmpty()==false)
        		        		{
        		        			author += ";";
        		        		}
        		        		tmpau = rs2.getString("作者");
        		        		if(tmpau!=null)
        		        			author += tmpau.trim();
        		        	}
        		        	cut_book_table.add(new BookInfo(bname,isbn,author,press, ymd, version,barcode,store,state));
        		        }
        		        table_book.setItems(cut_book_table);
    	      	        
    	      	        rs.close();
    	      	        rs2.close();
    	      	        stmt2.close();
    	      	        stmt.close();
    	      	        conn.close();
    	    		}
    	        	catch(SQLException se)
    	    		{
    	        		se.printStackTrace();
    	    	    }
    		    }
    	);

    	for(int i=1;i<=20;i++)
    	{
    		ob_version.add(Integer.toString(i));
    	}
    	ob_version.add("<空>");
    	com_version.setItems(ob_version);
    	com_version.getSelectionModel().select(0);
    }
    
    @FXML
    void tab_view_sel(Event event)
    {
    	
    }
    @FXML
    void tab_br_sel(Event event)
    {
    	String time_begin=null,time_end=null;
    	
    	
		LocalDate datetmp = datebegin.getValue();
		LocalDate datetmp2 = dateend.getValue();
		br_table.clear();
		if(datetmp==null||datetmp2==null)
		{
			time_begin = LocalDate.now().toString();
			time_end = LocalDate.now().toString();
		}
		else
		{
			time_begin = datetmp.toString();
			time_end = datetmp2.toString();
		}
		time_begin += " 00:00:00";
		time_end += " 23:59:59";
		System.out.println(time_begin+time_end);
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        sql = "select count(*) total,t.状态 state from dbo.借阅 t where t.借阅时间 "
 	        		+ "between '" +time_begin+"' and '"+time_end+  
 	        		"' group by t.状态";
 	        rs = stmt.executeQuery(sql);
 	        int total,state;
 	        int []d = new int[5];
 	        while(rs.next())
 	        {
 	        	total = rs.getInt("total");
 	        	state = rs.getInt("state");
 	        	System.out.println(total+" "+state);
 	        	d[state] = total;
 	        }
 	        br_table.add(new BrStat(Integer.toString(d[0]+d[1]+d[2]+d[3]+d[4]),Integer.toString(d[2]+d[3]+d[4])
 	        		,Integer.toString(d[3]+d[4]),Integer.toString(d[4])));
 	        table_br.setItems(br_table);
  	        
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
    void tab_rank_sel(Event event)
    {
    	String time_begin=null,time_end=null;
    	
    	
		LocalDate datetmp = datebegin.getValue();
		LocalDate datetmp2 = dateend.getValue();
		rank_table.clear();
		if(datetmp==null||datetmp2==null)
		{
			time_begin = LocalDate.now().toString();
			time_end = LocalDate.now().toString();
		}
		else
		{
			time_begin = datetmp.toString();
			time_end = datetmp2.toString();
		}
		time_begin += " 00:00:00";
		time_end += " 23:59:59";
		System.out.println(time_begin+time_end);
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        sql = "select count(*) total, t3.题名,t3.索书号 from dbo.借阅 t,dbo.馆藏 t2,dbo.图书 t3 " + 
 	        		"where t.条码=t2.条码 and t2.索书号=t3.索书号 and t.借阅时间 between '" +
 	        		time_begin+"' and '"+time_end+"' "+
 	        		"group by t3.索书号,t3.题名 order by total desc";
 	        rs = stmt.executeQuery(sql);
 	        int total;
 	        String bname,isbn;
 	        while(rs.next())
 	        {
 	        	total = rs.getInt("total");
 	        	bname = rs.getString("题名");
 	        	isbn = rs.getString("索书号");
 	        	rank_table.add(new Rank(bname,isbn,Integer.toString(total)));
 	        }
 	        
 	        table_rank.setItems(rank_table);
  	        
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
    private void tab_addbook_sel(Event event)
    {
    	System.out.println("tab_addbook_sel");
    	if(tab_addbook.isSelected()==false)
    		return;
    	try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        
 	        sql = "select 索书号 from dbo.图书";
 	        rs = stmt.executeQuery(sql);
 	        String ISBN;
 	        ob_isbn.clear();
 	        while(rs.next())
 	        {
 	        	ISBN = rs.getString("索书号").trim();
 	        	System.out.println("isbn="+ISBN);
 	        	ob_isbn.add(ISBN);
 	        }
  	        
  	        rs.close();
  	        stmt.close();
  	        conn.close();
  	        com_isbn.setItems(ob_isbn);
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_udelete_clicked(ActionEvent event)
    {
    	User sel = table_user.getSelectionModel().getSelectedItem();
    	if(sel==null)
    		return;
    	String unum = sel.getUnum();
    	try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        boolean flag = false;
 	        sql = "select count(*) total from dbo.借阅 t where t.读者编号"
	 	        		+ "='"+unum+"' and t.状态=2";
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	if(rs.getInt("total")>0)
 	        	{
 	        		flag = true;
 	    	        JOptionPane.showMessageDialog(new JFrame().getContentPane(),
 	    	        		"该用户还有书未还，不可注销","警告",JOptionPane.WARNING_MESSAGE);
 	        	}
 	        }
 	        sql = "select sum(t.罚金) total from dbo.罚单 t,dbo.借阅 t2 "
	 	        		+ "where t.借阅编号=t2.借阅编号 and t2.读者编号='"+unum+"' and t.状态=1";
	        rs = stmt.executeQuery(sql);
	        if(rs.next())
	        {
	        	if(rs.getInt("total")>0)
	        	{
	        		flag = true;
	     	        JOptionPane.showMessageDialog(new JFrame().getContentPane(),
	     	        		"该用户有罚金未交，不可删除","警告",JOptionPane.WARNING_MESSAGE);
	        	}
	        }
 	        sql = "delete from dbo.读者 where 编号='"+unum+"'";
 	        if(flag==false)
 	        {
 	 	        stmt.executeUpdate(sql);
 	 	        JOptionPane.showMessageDialog(new JFrame().getContentPane(),
 	 	        		"删除成功","提示",JOptionPane.INFORMATION_MESSAGE);
 	        }
  	        
  	        rs.close();
  	        stmt.close();
  	        conn.close();
  	        tab_usersearch_sel(null);
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_ualter_clicked(ActionEvent event)
    {
    	User sel = table_user.getSelectionModel().getSelectedItem();
    	if(sel==null)
    		return;
    	String unum,uname,pswd;
    	unum = sel.getUnum();
    	uname = sel.getUname();
    	pswd = sel.getPswd();
    	Stage alterstage = new Stage();
    	alterstage.setTitle("修改用户信息");
		Pane root = new Pane();
		TextField tunum = new TextField(unum);
		tunum.setLayoutX(100);
		tunum.setLayoutY(20);
		tunum.setDisable(true);
		TextField tuname = new TextField(uname);
		tuname.setLayoutX(100);
		tuname.setLayoutY(70);
		TextField tpswd = new TextField(pswd);
		tpswd.setLayoutX(100);
		tpswd.setLayoutY(120);
		Label lunum = new Label("用户编号");
		lunum.setLayoutX(40);
		lunum.setLayoutY(20);
		Label luname = new Label("用户姓名");
		luname.setLayoutX(40);
		luname.setLayoutY(70);
		Label lpswd = new Label("登录密码");
		lpswd.setLayoutX(40);
		lpswd.setLayoutY(120);
		Button btnok = new Button("确定");
		btnok.setLayoutX(50);
		btnok.setLayoutY(170);
		btnok.setPrefWidth(200);
		btnok.setOnAction(
			(ActionEvent e)->
			{
				String newuname,newpswd;
				newuname = tuname.getText().trim();
				newpswd = tpswd.getText().trim();
				
				try
				{
		 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
		 	        stmt = conn.createStatement();
		 	        sql = "update dbo.读者 set 姓名='"+newuname+"',密码='"+newpswd+"' "
		 	        		+ "where 编号="+unum;
		 	        System.out.println(sql);
		 	        stmt.executeUpdate(sql);
		 	        JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	 	 	 	 	"修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
		 	        tab_usersearch_sel(null);
		  	        
		  	        rs.close();
		  	        stmt.close();
		  	        conn.close();
				}
		    	catch(SQLException se)
				{
		    		se.printStackTrace();
			    }
				alterstage.close();
			}
		);
		root.getChildren().add(luname);
		root.getChildren().add(lpswd);
		root.getChildren().add(lunum);
		root.getChildren().add(tuname);
		root.getChildren().add(tpswd);
		root.getChildren().add(tunum);
		root.getChildren().add(btnok);
		
		Scene scene = new Scene(root,300,200);
		alterstage.setScene(scene);
		alterstage.show();
    }
    
    @FXML
    private void btn_usearch_clicked(ActionEvent event)
    {
    	String index = txt_usearch.getText();
    	if(index.isEmpty())
    	{
    		table_user.setItems(user_table);
    		return;
    	}
		try
		{
			search_user_table.clear();
			
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        stmt2 = conn.createStatement();
	        
	        sql = " select * from dbo.读者 t where t.编号 like '%"+index+"%' or t.姓名 like '%"+index+"%'";
	        System.out.println(sql);
	        rs = stmt.executeQuery(sql);
	        
	        String unum,uname,pswd;
	        int tfine=0,bcount=0;
	        while(rs.next())
	        {
	        	unum = rs.getString("编号");
	        	uname = rs.getString("姓名");
	        	pswd = rs.getString("密码");
	        	if(unum!=null)
	        		unum = unum.trim();
	        	if(uname!=null)
	        		uname = uname.trim();
	        	if(pswd!=null)
	        		pswd = pswd.trim();
 	 	        sql = "select count(*) total from dbo.借阅 t where t.读者编号"
 	 	        		+ "='"+unum+"' and t.状态=2";
 	 	        rs2 = stmt2.executeQuery(sql);
 	 	        if(rs2.next())
 	 	        {
 	 	        	bcount = rs2.getInt("total");
 	 	        }
 	 	        
 	 	        sql = "select sum(t.罚金) total from dbo.罚单 t,dbo.借阅 t2 "
 	 	        		+ "where t.借阅编号=t2.借阅编号 and t2.读者编号='"+unum+"' and t.状态=1";
 	 	        rs2 = stmt2.executeQuery(sql);
 	 	        System.out.println(sql);
 	 	        if(rs2.next())
 	 	        {
 	 	        	tfine = rs2.getInt("total");
 	 	        	System.out.println(tfine);
 	 	        }
 	 	        search_user_table.add(new User(unum,uname,Integer.toString(bcount),Integer.toString(tfine),pswd));
	        }
	        table_user.setItems(search_user_table);
	        
	        stmt.close();
	        conn.close();
		}
    	catch(SQLException se){
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void tab_usersearch_sel(Event event)
    {
    	if(tab_usersearch.isSelected()==false)
    		return;
    	try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        stmt2 = conn.createStatement();
 	        sql = "select * from dbo.读者 t";
 	        rs = stmt.executeQuery(sql);
 	        String unum,uname,pswd;
 	        int tfine=0,bcount=0;
 	        user_table.clear();
 	        while(rs.next())
 	        {
 	        	uname = rs.getString("姓名");
 	        	unum = rs.getString("编号").trim();
 	        	pswd = rs.getString("密码").trim();
 	        	if(uname!=null)
 	        		uname = uname.trim();
 	        	
 	 	        sql = "select count(*) total from dbo.借阅 t where t.读者编号"
 	 	        		+ "='"+unum+"' and t.状态=2";
 	 	        rs2 = stmt2.executeQuery(sql);
 	 	        if(rs2.next())
 	 	        {
 	 	        	bcount = rs2.getInt("total");
 	 	        }
 	 	        
 	 	        sql = "select sum(t.罚金) total from dbo.罚单 t,dbo.借阅 t2 "
 	 	        		+ "where t.借阅编号=t2.借阅编号 and t2.读者编号='"+unum+"' and t.状态=1";
 	 	        rs2 = stmt2.executeQuery(sql);
 	 	        System.out.println(sql);
 	 	        if(rs2.next())
 	 	        {
 	 	        	tfine = rs2.getInt("total");
 	 	        	System.out.println(tfine);
 	 	        }
 	 	        user_table.add(new User(unum,uname,Integer.toString(bcount),Integer.toString(tfine),pswd));
 	        }
 	        table_user.setItems(user_table);
  	        
  	        rs.close();
  	        rs2.close();
  	        stmt2.close();
  	        stmt.close();
  	        conn.close();
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void tab_booksearch_sel(Event event)
    {
    	if(tab_booksearch.isSelected()==false)
    		return;
    	
    	cut_book_table.clear();
    	int sel = choice_view.getSelectionModel().getSelectedIndex();
    	
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        stmt2 = conn.createStatement();
        	if(sel==0)
        	{
		        sql = "select * from dbo.图书 t left join dbo.馆藏 t2 on t.索书号=t2.索书号"
		        		+ " order by t.索书号,t2.条码";
        	}
        	else if(sel==1)
        	{
		        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号 "
		        		+ "and t2.状态=1 order by t.索书号,t2.条码";
        	}
        	else if(sel==2)
        	{
		        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号 "
		        		+ "and t2.状态=2 order by t.索书号,t2.条码";
        	}
        	else
        	{
		        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号 "
		        		+ "and t2.状态=0 order by t.索书号,t2.条码";
        	}
	        rs = stmt.executeQuery(sql);
	        String bname,author,press,ymd,version,isbn,barcode,store,state;
	        while(rs.next())
	        {
	        	state = rs.getString("状态");
	        	bname = rs.getString("题名");
	        	isbn = rs.getString("索书号").trim();
	        	press = rs.getString("出版社");
	        	ymd = rs.getString("发行时间");
	        	version = rs.getString("版本");
	        	barcode = rs.getString("条码");
	        	store = rs.getString("馆藏地点");
	        	System.out.println(press);
	        	if(bname!=null)
	        		bname = bname.trim();
	        	if(press!=null)
	        		press = press.trim();
	        	if(ymd!=null)
	        		ymd = ymd.trim();
	        	if(barcode!=null)
	        		barcode = barcode.trim();
	        	if(store!=null)
	        		store = store.trim();
	        	System.out.println("stat="+state);
	        	if(state==null)
	        		state = "";
	        	else if(state.equals("0"))
	        		state = "损坏";
	        	else if(state.equals("1"))
	        		state = "在馆";
	        	else if(state.equals("2"))
	        		state = "借出";
	        	else
	        		state = "";
	        	
	        	sql = "select distinct 作者 from dbo.著作 where '"+isbn+"'=索书号";
	        	rs2 = stmt2.executeQuery(sql);
	        	author = "";
	        	String tmpau;
	        	while(rs2.next())
	        	{
	        		if(author.isEmpty()==false)
	        		{
	        			author += ";";
	        		}
	        		tmpau = rs2.getString("作者");
	        		if(tmpau!=null)
	        			author += tmpau.trim();
	        	}
	        	cut_book_table.add(new BookInfo(bname,isbn,author,press, ymd, version,barcode,store,state));
	        }
	        table_book.setItems(cut_book_table);
  	        
  	        rs.close();
  	        rs2.close();
  	        stmt2.close();
  	        stmt.close();
  	        conn.close();
		}
    	catch(SQLException se)
		{
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_approve_return_clicked(ActionEvent event) throws ParseException
    {
    	BorrowInfo sel = table_return.getSelectionModel().getSelectedItem();
    	if(sel==null)
    		return;
    	String bnum;
    	bnum = sel.getBorrowNum();
    	Object[] options = {"正常","损坏"}; 
    	String selected = JOptionPane.showInputDialog(null,"请鉴定图书是否损坏","提示", 
    	JOptionPane.INFORMATION_MESSAGE,null,options,options[0]).toString();
    	System.out.println(selected);
    	
    	boolean broken = false,delayed = false;
    	int []fine = new int[2];
    	if(selected.equals("损坏"))
    	{
    		broken = true;
    	}
    	
    	try
    	{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        
 	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
 	        Date datenow = sdf.parse(sdf.format(new Date())),dateddl;
 	        Calendar now = Calendar.getInstance(),ddl=Calendar.getInstance();
 	        now.setTime(datenow);
 	        
 	        String datestr;
 	        sql = "select t.借阅时间 from dbo.借阅 t where t.借阅编号='"+bnum+"'";
 	        System.out.println(sql);
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	datestr = rs.getString("借阅时间");
 	        	datestr = datestr.substring(0, 10);
 	        	dateddl = sdf.parse(datestr);
 	        	ddl.setTime(dateddl);
 	        	ddl.add(Calendar.DAY_OF_YEAR,30);
 	        	System.out.println(sdf.format(now.getTime())+" "+sdf.format(ddl.getTime()));
 	        	if(now.compareTo(ddl)>0)
 	        	{
 	        		delayed = true;
 	        	}
 	        }
 	        
 	        String msg="";
 	        if(broken)
 	        {
 	        	fine[0] = 5;
 	        	msg += "书籍损坏，罚款"+fine[0]+"元\n";
 	        	sql = "insert into dbo.罚单(罚款原因,状态,借阅编号) values"
 		        		+ "(0,1,"+bnum+")";
 	        	System.out.println(sql);
 	        	stmt.executeUpdate(sql);
 	        }
 	        if(delayed)
 	        {
 	        	fine[1] = 1;
 	        	msg += "未在一个月内归还，罚款"+fine[1]+"元\n";
 	        	sql = "insert into dbo.罚单(罚款原因,状态,借阅编号) values"
 		        		+ "(0,1,"+bnum+")";
 	        	System.out.println(sql);
 	        	stmt.executeUpdate(sql);
 	        }
 	        msg += "共计罚款"+(fine[0]+fine[1])+"元";

 	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sql = "update dbo.借阅 set 状态=4,归还时间='"+df.format(new Date())
				+"' where 借阅编号="+bnum+"";
			System.out.println(sql);
			stmt.executeUpdate(sql);
 	        rs.close();
 	        stmt.close();
 	        conn.close();
 	        
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	         			msg, "提示", JOptionPane.INFORMATION_MESSAGE);
 	        
     		tab_return_sel(null);
    	}
    	catch(SQLException se)
    	{
    		se.printStackTrace();
    	}
    }

    @FXML
    private void tab_return_sel(Event event)
    {
    	if(tab_return.isSelected()==false)
    		return;
    	String bnum;
    	String bname,barcode,usernum,user,btime;
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
	        
	        sql = "select t.借阅编号,t.读者编号,t.借阅时间,t2.姓名,t3.题名,t.条码"
	        		+ " from dbo.借阅 t,dbo.读者 t2,dbo.图书 t3,dbo.馆藏 t4"
	        		+" where t.条码=t4.条码 and t4.索书号=t3.索书号"
	        		+ " and t.读者编号=t2.编号 and t.状态=3 order by t.借阅时间";
	        System.out.println(sql);
	        rs = stmt.executeQuery(sql);
	        return_table.clear();
	        while(rs.next())
	        {
	        	bnum = rs.getString("借阅编号");
	        	bname = rs.getString("题名");
	        	barcode = rs.getString("条码").trim();
	        	usernum = rs.getString("读者编号").trim();
	        	user = rs.getString("姓名");
	        	btime = rs.getString("借阅时间");
	        	if(bname!=null)
	        		bname = bname.trim();
	        	if(user!=null)
	        		user = user.trim();
	        	return_table.add(new BorrowInfo(bnum,barcode,bname,usernum,user,btime));
	        }
	        table_return.setItems(return_table);
  	        
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
    private void tab_borrow_sel(Event event)
    {
    	if(tab_borrow.isSelected()==false)
    		return;
    	System.out.println("tab_borrow_sel");
    	String bnum;
    	String bname,barcode,usernum,user,btime;
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
	         
	        sql = "select t.借阅编号,t.读者编号,t.借阅时间,t2.姓名,t3.题名,t.条码"
	        		+ " from dbo.借阅 t,dbo.读者 t2,dbo.图书 t3,dbo.馆藏 t4"
	        		+" where t.条码=t4.条码 and t4.索书号=t3.索书号"
	        		+ " and t.读者编号=t2.编号 and t.状态=1 order by t.借阅时间";
	        System.out.println(sql);
	        rs = stmt.executeQuery(sql);
	        borrow_table.clear();
	        while(rs.next())
	        {
	        	bnum = rs.getString("借阅编号");
	        	bname = rs.getString("题名");
	        	barcode = rs.getString("条码").trim();
	        	usernum = rs.getString("读者编号").trim();
	        	user = rs.getString("姓名");
	        	btime = rs.getString("借阅时间");
	        	if(bname!=null)
	        		bname = bname.trim();
	        	if(user!=null)
	        		user = user.trim();
	        	borrow_table.add(new BorrowInfo(bnum,barcode,bname,usernum,user,btime));
	        }
	        table_borrow.setItems(borrow_table);
  	        
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
    private void btn_approve_clicked(ActionEvent event) throws ParseException
    {
    	boolean succeed = false;
    	String bnum,barcode,usernum;
    	BorrowInfo selb = table_borrow.getSelectionModel().getSelectedItem();
    	System.out.println(selb);
    	if(selb==null)
    		return;
    	bnum = selb.getBorrowNum();
    	barcode = selb.getBCode();
    	usernum = selb.getUserNum();
    	try
    	{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        sql = "select count(*) total from dbo.读者 t,dbo.借阅 t2 where t.编号=t2.读者编号" + 
 	        		" and t.编号='"+usernum+"' and t2.状态=2";
 	        System.out.println(sql);
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	int total = rs.getInt("total");
 	        	if(total>=3)
 	        	{
 	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	         			"借书册数已达3本\n不可再借！", "警告", JOptionPane.WARNING_MESSAGE);
 	        		return;
 	        	}
 	        }
 	        
 	        sql = "select sum(t.罚金) total from dbo.罚单 t,dbo.借阅 t2" + 
 	        		" where t.借阅编号=t2.借阅编号 and t.状态=1 and t2.读者编号='"+usernum+"'";
 	        System.out.println(sql);
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	int total = rs.getInt("total");
 	        	if(total>0)
 	        	{
 	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	         			"有罚金未交，不可借书！", "警告", JOptionPane.WARNING_MESSAGE);
 	 	        	return;
 	        	}
 	        }
 	        
 	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
 	        Date datenow = sdf.parse(sdf.format(new Date())),dateddl;
 	        Calendar now = Calendar.getInstance(),ddl=Calendar.getInstance();
 	        now.setTime(datenow);
 	        
 	        String datestr;
 	        sql = "select t.借阅时间 from dbo.借阅 t where t.读者编号='"+usernum+"' "
 	        		+ "and t.状态=2";
 	        System.out.println(sql);
 	        rs = stmt.executeQuery(sql);
 	        while(rs.next())
 	        {
 	        	datestr = rs.getString("借阅时间");
 	        	datestr = datestr.substring(0, 10);
 	        	dateddl = sdf.parse(datestr);
 	        	ddl.setTime(dateddl);
 	        	ddl.add(Calendar.DAY_OF_YEAR,30);
 	        	System.out.println(sdf.format(now.getTime())+" "+sdf.format(ddl.getTime()));
 	        	if(now.compareTo(ddl)>0)
 	        	{
 	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	         			"有书过期未还，不可借书！", "警告", JOptionPane.WARNING_MESSAGE);
 	        		return;
 	        	}
 	        }
 	        
 	        sql = "select t.状态 from dbo.馆藏 t where t.条码='"+barcode+"'";
 	        System.out.println(sql);
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	int state = rs.getInt("状态");
 	        	switch(state)
 	        	{
 	        	case 1:
 	        		succeed = true;
 	        		break;
 	        	case 2:
 	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	         			"该书已借出！", "警告", JOptionPane.WARNING_MESSAGE);
 	        		break;
 	        	case 0:
 	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	         			"该书已损坏！", "警告", JOptionPane.WARNING_MESSAGE);
 	        		break;
 	        	default:break;
 	        	}
 	        }
 	        
    	}
    	catch(SQLException se)
    	{
    		se.printStackTrace();
    	}
    	finally
    	{
    		try
    		{
    			int nextstate = succeed?2:0;
    			sql = "update dbo.借阅 set 状态="+nextstate+" where 借阅编号="+bnum+"";
    			System.out.println(sql);
    			stmt.executeUpdate(sql);
     	        rs.close();
     	        stmt.close();
     	        conn.close();
    		}
    		catch(SQLException se)
    		{
    			se.printStackTrace();
    		}
    		finally
    		{
    			if(succeed)
    			{
 	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	         		"审核通过", "提示", JOptionPane.INFORMATION_MESSAGE);
    			}
    		}
    		tab_borrow_sel(null);
    	}
    }
    
    
    @FXML
    private void btn_ok_clicked(ActionEvent event) throws SQLException
    {
    	String author,press,bname,isbn,version,ymd,store;
    	int version_i = com_version.getSelectionModel().getSelectedIndex();
    	
    	author = txt_author.getText();
    	press = txt_press.getText();
    	store = txt_store.getText();
    	bname = txt_bname.getText();
    	version = com_version.getSelectionModel().getSelectedItem();
    	isbn = com_isbn.getSelectionModel().getSelectedItem();
    	ymd = datepicker.getValue().toString();
    	
    	if(isbn==null||isbn.isEmpty())
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
         			"索书号不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	else
    	{
    		isbn = isbn.trim();
    		isbn = "'"+isbn+"'";
    	}
    	
    	if(store.isEmpty())
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
         			"馆藏地点不能为空！", "警告", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	
    	if(bname.isEmpty())
    	{
    		bname = "NULL";
    	}
    	else
    	{
    		bname = "'"+bname+"'";
    	}
    	
    	if(press.isEmpty())
    	{
    		press = "NULL";
    	}
    	else
    	{
    		press = "'"+press+"'";
    	}
    	
    	if(store.isEmpty())
    	{
    		store = "NULL";
    	}
    	else
    	{
    		store = "'"+store+"'";
    	}
    	
    	if(version_i==ob_version.size()-1)
    	{
    		version = "NULL";
    	}
    	
		try
		{
	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        
        	sql = "select * from dbo.图书 where 索书号="+isbn;
	        rs = stmt.executeQuery(sql);
	        if(!rs.next())//索书号还不存在
	        {
     	        sql = "insert into dbo.图书(题名,索书号,出版社,"
     	         		+ "发行时间,版本) " + 
     	         		"values ("+bname+","+isbn+","+press+",'"+ymd+"',"+version+")";
     	        System.out.println(sql);
     	        stmt.executeUpdate(sql);
     	        
		        String tmpauthor = author;
		        int tmp = 0;
	        	for(;tmp!=-1;tmpauthor = author)
	        	{
	        		tmp = author.indexOf(";");
	        		if(tmp!=-1)
	        		{
	        			tmpauthor = author.substring(0,tmp);
	            		if( tmp+1 < author.length())
	            			author = author.substring(tmp+1);
	            		else
	            			tmp = -1;
	        		}
	        		
	    	        sql = "insert into dbo.著作(索书号,作者) "+
	     	         		"values ("+isbn+",'"+tmpauthor+"')";
	    	        stmt.executeUpdate(sql);
	    	        System.out.println(sql);
	        	}
	        }
	        
	        sql = "insert into dbo.馆藏(索书号,馆藏地点,"
 	         		+ "状态) " + 
 	         		"values ("+isbn+","+store+",1)";
	        stmt.executeUpdate(sql);
	        System.out.println(sql);
	        
 	        stmt.close();
 	        conn.close();
		}
    	catch(SQLException se)
		{
         	// 处理 JDBC 错误
         	se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_search_clicked(ActionEvent event)
    {
    	String index = txt_search.getText();
    	if(index.isEmpty())
    	{
    		table_book.setItems(cut_book_table);
    		return;
    	}
		try
		{
			search_book_table.clear();
			
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        
	        sql = "select * from (dbo.图书 t left join dbo.馆藏 t2 on t.索书号=t2.索书号) "
	        		+ "left join dbo.著作 t3 on t3.索书号=t.索书号 where (t.索书号 like'%"+index
	        		+"%' or t.题名 like '%"+index+"%' or t2.条码 like'%"+index+"%' or t3.作者 like "
	        		+ "'%"+index+"%') order by t2.条码,t.索书号,t3.作者";
	        System.out.println(sql);
	        rs = stmt.executeQuery(sql);
	        
	        String bname,tmpauthor,press,ymd,version,isbn,barcode="",store,state,old="",author="";
	        while(rs.next())
	        {
	        	barcode = rs.getString("条码");
	        	tmpauthor = rs.getString("作者");
	        	if(barcode.equals(old))
	        	{
	        		if(author.isEmpty()==false)
	        		{
	        			author += ";";
	        		}
	        		author += tmpauthor.trim();
	        		old = barcode;
	        		search_book_table.get(search_book_table.size()-1).setAuthor(author);
	        		continue;
	        	}
	        	else
	        	{
		        	old = barcode;
		        	barcode = rs.getString("条码");
		        	tmpauthor = rs.getString("作者");
		        	bname = rs.getString("题名");
		        	press = rs.getString("出版社");
		        	ymd = rs.getString("发行时间");
		        	version = rs.getString("版本");
		        	isbn = rs.getString("索书号");
		        	store = rs.getString("馆藏地点");
		        	state = rs.getString("状态");
		        	if(isbn!=null)
		        		isbn = isbn.trim();
		        	if(bname!=null)
		        		bname = bname.trim();
		        	if(press!=null)
		        		press = press.trim();
		        	if(store!=null)
		        		store = store.trim();
		        	if(barcode!=null)
		        		barcode = barcode.trim();
		        	
		        	if(state==null)
		        		state = "";
		        	else if(state.equals("0"))
		        		state = "损坏";
		        	else if(state.equals("1"))
		        		state = "在馆";
		        	else if(state.equals("2"))
		        		state = "借出";
		        	else
		        		state = "";
		        	
		        	search_book_table.add(new BookInfo(bname,isbn,tmpauthor,press, ymd, version,barcode,store,state));
	        	}
	        }
	        table_book.setItems(search_book_table);
	        
	        stmt.close();
	        conn.close();
		}
    	catch(SQLException se){
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void isbn_key_released(Event event)
    {
    	System.out.println("isbn_key_released");
    	String isbn = com_isbn.getSelectionModel().getSelectedItem();
    	System.out.println(isbn);
    	if(isbn!=null)
    	{
    		isbn = isbn.trim();
    	}
    	com_version.setDisable(false);
    	txt_bname.setDisable(false);
    	txt_press.setDisable(false);
    	txt_author.setDisable(false);
    	datepicker.setDisable(false);
    }

    @FXML
    private void com_isbn_action(Event event)
    {
    	System.out.println("com_isbn_action");
    	System.out.println(com_isbn.getSelectionModel().getSelectedIndex());
    	if(com_isbn.getSelectionModel().getSelectedIndex()==-1)
    		return;
    	String isbn = com_isbn.getSelectionModel().getSelectedItem();
    	
    	if(isbn.indexOf("'")>-1)
    	{
    		return;
    	}
    	System.out.println(isbn);
    	String bname,version,author,press,date;
		try
		{
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        stmt2 = conn.createStatement();
	        sql = "select * from dbo.图书 t left join dbo.馆藏 t2 on t.索书号=t2.索书号"
	        		+ " where t.索书号='"+isbn+"'";
	        rs = stmt.executeQuery(sql);
	        
	        if(rs.next())
	        {
	        	bname = rs.getString("题名");
	        	version = rs.getString("版本");
	        	press = rs.getString("出版社");
	        	date = rs.getString("发行时间");
	        	if(bname!=null)
	        		bname = bname.trim();
	        	if(press!=null)
	        		press = press.trim();
	        	
	        	sql = "select distinct 作者 from dbo.著作 where '"+isbn+"'=索书号";
	        	rs2 = stmt2.executeQuery(sql);
	        	author = "";
	        	while(rs2.next())
	        	{
	        		if(author.isEmpty()==false)
	        		{
	        			author += ";";
	        		}
	        		author += rs2.getString("作者").trim();
	        	}
	        	
	        	txt_author.setText(author);
	        	txt_bname.setText(bname);
	        	txt_press.setText(press);
	        	int a,b;
	        	a = date.indexOf("-");
	        	b = date.lastIndexOf("-");
	        	datepicker.setValue(LocalDate.of(Integer.parseInt(date.substring(0,4))
	        			,Integer.parseInt(date.substring(a+1,b)),Integer.parseInt(date.substring(b+1))));
	        	com_version.getSelectionModel().select(Integer.parseInt(version)-1);
	       
	        	com_version.setDisable(true);
	        	txt_bname.setDisable(true);
	        	txt_press.setDisable(true);
	        	txt_author.setDisable(true);
	        	datepicker.setDisable(true);
	        }
	        else
	        {
	        	com_version.setDisable(false);
	        	txt_bname.setDisable(false);
	        	txt_press.setDisable(false);
	        	txt_author.setDisable(false);
	        	datepicker.setDisable(false);
	        }
	        
	        stmt2.close();
	        rs2.close();
	        stmt.close();
	        conn.close();
		}
    	catch(SQLException se){
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_delete_clicked(ActionEvent event)
    {
    	BookInfo sel = table_book.getSelectionModel().getSelectedItem();
    	if(sel==null)
    	{
    		return;
    	}
    	String isbn,bar;
    	isbn = sel.getIsbn();
    	bar = sel.getBarCode();
    	System.out.println(isbn+bar);
    	
    	try
		{
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        
	        if(bar!=null)
	        {
	        	sql = "select 状态 from dbo.馆藏 where 条码='"+bar+"'";
	        	rs = stmt.executeQuery(sql);
	        	if(rs.next())
	        	{
	        		int state = rs.getInt("状态");
	        		if(state==2)
	        		{
		         		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
			           	   "该书已借出,暂不可删除！", "警告", JOptionPane.WARNING_MESSAGE);
	        		}
	        		else
	        		{
	    	        	sql = "delete from dbo.馆藏 where 条码='"+bar+"'";
	    	        	stmt.executeUpdate(sql);
	             		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	    	           	    "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	        		}
	        	}
	        }
	        else
	    	{
	    		sql = "select * from dbo.馆藏 t where t.索书号='"+isbn+"'";
	    		rs = stmt.executeQuery(sql);
	    		if(rs.next())
	    		{
	         		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	           	         "该记录已有馆藏图书,不可删除\n请刷新后重试", "警告", JOptionPane.WARNING_MESSAGE);
	    		}
	    		else
	    		{
	    			sql = "delete from dbo.图书 where 索书号='"+isbn+"'";
	    			stmt.executeUpdate(sql);
	         		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	    	           	    "删除成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	    		}
	    	}

	        stmt.close();
	        conn.close();
	        
	        tab_booksearch_sel(null);
		}
    	catch(SQLException se){
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_clr_clicked(ActionEvent event)
    {
    	txt_bname.clear();
    	com_version.getSelectionModel().select(0);
    }
    
    @FXML
    private void btn_logout_clicked(ActionEvent event)
    {
    	Main.setLoginScene();
    }
    
    @FXML
    private void btn_userok_clicked(ActionEvent event)
    {
    	String user,pswd;
    	user = txt_user.getText();
    	pswd = txt_pswd.getText();
    	if(user.equals("")||pswd.equals(""))
    	{
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
    	         "信息填写不全！", "警告", JOptionPane.WARNING_MESSAGE);
     		return;
    	}
    	try
		{
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
        	sql = "insert into dbo.读者(姓名,密码) values('"+user+"','"+pswd+"')";
        	stmt.executeUpdate(sql);
     		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
	       	         "录入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
	        
	        stmt.close();
	        conn.close();
		}
    	catch(SQLException se){
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
