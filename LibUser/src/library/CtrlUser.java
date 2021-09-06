package library; 

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import library.Main;
import library.module.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrlUser implements Initializable
{
	static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=图书馆";
	static final String USER = "sa";
	static final String PASS = "chin654toyi";
    static Connection conn = null;
    static Statement stmt = null;
    static Statement stmt2 = null;
    ResultSet rs = null,rs2=null;
    String sql;
    
    ObservableList<BookInfo> book_table = FXCollections.observableArrayList();
    ObservableList<BookInfo> search_book_table = FXCollections.observableArrayList();
    ObservableList<BHistory> borrow_table = FXCollections.observableArrayList();
    ObservableList<Fine> fine_table = FXCollections.observableArrayList();

    
    @FXML
    private TextField txt_search_bname,txt_search_author,txt_search_press,
    txt_usernum,txt_user,txt_bcount,txt_totalfine;
    @FXML
    private PasswordField txt_pswd;
    @FXML
    private Tab tab_bhistory,tab_user,tab_fine,tab_borrow;
    @FXML
    private TableColumn<?, ?> col_bname,col_author,col_press,col_ymd,col_version,
    col_isbn,col_barcode,col_store,col_state,
    col_bcode,col_bnam,col_btime,col_stat,col_bnum,
    col_finenum,col_fine,col_sake,col_fstate,col_fbnum,col_fbname,col_fbtime,col_frtime;
    @FXML
    private TableView<BookInfo> table_book;
    @FXML
    private TableView<BHistory> table_bhistory;
    @FXML
    private TableView<Fine> table_fine;

    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    	
    	col_bname.setCellValueFactory(new PropertyValueFactory<>("bookName"));  
    	col_author.setCellValueFactory(new PropertyValueFactory<>("author"));  
    	col_press.setCellValueFactory(new PropertyValueFactory<>("press")); 
    	col_ymd.setCellValueFactory(new PropertyValueFactory<>("ymd"));
    	col_version.setCellValueFactory(new PropertyValueFactory<>("version"));  
    	col_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    	col_barcode.setCellValueFactory(new PropertyValueFactory<>("barCode"));
    	col_store.setCellValueFactory(new PropertyValueFactory<>("store"));
    	col_state.setCellValueFactory(new PropertyValueFactory<>("state"));
    	
    	System.out.println(col_bnum);
    	col_bcode.setCellValueFactory(new PropertyValueFactory<>("bCode"));
    	col_btime.setCellValueFactory(new PropertyValueFactory<>("bTime"));
    	col_bnam.setCellValueFactory(new PropertyValueFactory<>("bNam"));
    	col_stat.setCellValueFactory(new PropertyValueFactory<>("stat"));
    	col_bnum.setCellValueFactory(new PropertyValueFactory<>("bNum"));
    	
    	col_finenum.setCellValueFactory(new PropertyValueFactory<>("fineNum"));  
    	col_fine.setCellValueFactory(new PropertyValueFactory<>("fine")); 
    	col_sake.setCellValueFactory(new PropertyValueFactory<>("sake"));
    	col_fstate.setCellValueFactory(new PropertyValueFactory<>("fstate"));  
    	col_fbnum.setCellValueFactory(new PropertyValueFactory<>("fbnum"));
    	col_fbname.setCellValueFactory(new PropertyValueFactory<>("fbname"));
    	col_fbtime.setCellValueFactory(new PropertyValueFactory<>("fbtime"));
    	col_frtime.setCellValueFactory(new PropertyValueFactory<>("frtime"));
    }
    
    @FXML
    private void btn_renew_clicked(ActionEvent event) throws ParseException
    {
    	BHistory sel = table_bhistory.getSelectionModel().getSelectedItem();
    	if(sel==null)
    		return;
    	String bnum,statestr;
    	bnum = sel.getBNum();
    	statestr = sel.getStat();
    	if(statestr.equals("在借")==false)
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
         		"该条记录不是在借记录！", "警告", JOptionPane.WARNING_MESSAGE);
    	}
    	else
    	{
    		try
    		{
     	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
     	        stmt = conn.createStatement();
     	        
     	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
     	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
     	 	    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
     	 	 	 	 	    "该书过期未还，不可续借", "警告", JOptionPane.WARNING_MESSAGE);
     	        	}
     	        	else
     	        	{
     	     	        sql = "update dbo.借阅 set 借阅时间='"+df.format(new Date())+"' where 借阅编号="+bnum;
     	    	        stmt.executeUpdate(sql);
     	 	    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
         	 	 	 	 	    "续借成功，请在一个月内归还书籍", "提示", JOptionPane.INFORMATION_MESSAGE);
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
    		finally
    		{
    			tab_bhistory_sel(null);
    		}
    	}
    }
    
    
    @FXML
    private void tab_borrow_sel(Event event)
    {
    	if(tab_borrow.isSelected()==false)
    		return;
    	System.out.println("tab_borrow_sel");
    	book_table.clear();
		try
		{	
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        stmt2= conn.createStatement();
	         
 	        sql = "select * from dbo.图书 t,dbo.馆藏 t2 where t.索书号=t2.索书号"
 	       		+ " and t2.状态=1 order by t.索书号,t2.条码";
	        rs = stmt.executeQuery(sql);
	        String bname,author,press,ymd,version,isbn,barcode,store,state;
	        int stat;
	        while(rs.next())
	        {
	        	stat = rs.getInt("状态");
	        	bname = rs.getString("题名");
	        	isbn = rs.getString("索书号").trim();
	        	press = rs.getString("出版社");
	        	ymd = rs.getString("发行时间");
	        	version = rs.getString("版本");
	        	barcode = rs.getString("条码").trim();
	        	store = rs.getString("馆藏地点");
	        	System.out.println(press);
	        	if(bname!=null)
	        		bname = bname.trim();
	        	if(press!=null)
	        		press = press.trim();
	        	if(ymd!=null)
	        		ymd = ymd.trim();
	        	if(store!=null)
	        		store = store.trim();
	        	switch(stat)
	        	{
	        	case 1:state = "在馆";break;
	        	case 2:state = "借出";break;
	        	case 0:state = "损坏";break;
	        	default:state = "";break;
	        	}
	        	
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
	        	book_table.add(new BookInfo(bname,isbn,author,press, ymd, version,barcode,store,state));
	        }
	        table_book.setItems(book_table);
  	         
	        rs2.close();
  	        rs.close();
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
    private void tab_user_sel(Event event)
    {
    	if(tab_user.isSelected()==false)
    		return;
    	String usernum,user,bcount,totalfine;
    	usernum = Main.getUser();
    	txt_usernum.setText(usernum);
		try
		{	
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        sql = "select t.姓名 from dbo.读者 t where t.编号='"+usernum+"'";
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	user = rs.getString("姓名");
 	        	if(user!=null)
 	        		user = user.trim();
 	        	txt_user.setText(user);
 	        }
 	        sql = "select count(*) total from dbo.借阅 t where t.读者编号"
 	        		+ "='"+usernum+"' and t.状态=2";
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	bcount = rs.getString("total");
 	        	txt_bcount.setText(bcount);
 	        }
 	        sql = "select sum(t.罚金) total from dbo.罚单 t,dbo.借阅 t2 "
 	        		+ "where t.借阅编号=t2.借阅编号 and t2.读者编号='"+usernum+"' and t.状态=1";
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	totalfine = rs.getString("total");
 	        	System.out.println(totalfine);
 	        	if(totalfine==null)
 	        		totalfine = "0";
 	        	txt_totalfine.setText(totalfine);
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
    private void btn_fine_clicked(ActionEvent event)
    {
    	Fine sel = table_fine.getSelectionModel().getSelectedItem();
    	if(sel==null)
    	{
    		return;
    	}
    	String fnum;
    	boolean state=false;
    	fnum = sel.getFineNum();
		try
		{	
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        sql = "select t.状态 from dbo.罚单 t where t.罚单号="+fnum;
 	        rs = stmt.executeQuery(sql);
 	        if(rs.next())
 	        {
 	        	state = rs.getBoolean("状态");
 	        	System.out.println(state);
 	        }
 	        if(state==false)
 	        {
 	    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	            	"该项已交款！", "警告", JOptionPane.WARNING_MESSAGE);
 	        }
 	        else
 	        {
 	        	sql = "update dbo.罚单 set 状态=0 where 罚单号="+fnum;
 	        	stmt.executeUpdate(sql);
 	    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	            	"成功交款"+sel.getFine()+"元", "提示", JOptionPane.INFORMATION_MESSAGE);
 	    		tab_fine_sel(null);
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
    private void tab_fine_sel(Event event)
    {
    	if(tab_fine.isSelected()==false)
    		return;
		try
		{	
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
	         
 	        sql = "select t.罚单号,t.罚金,t.罚款原因,t.状态,t.借阅编号,t4.题名,"
 	        		+ "t3.借阅时间,t3.归还时间" + 
 	        		" from dbo.罚单 t,dbo.馆藏 t2,dbo.借阅 t3,dbo.图书 t4 " + 
 	        		"where t.借阅编号=t3.借阅编号 and t3.条码=t2.条码 and "
 	        		+ "t2.索书号=t4.索书号 and t3.读者编号='"+Main.getUser()+"'";
 	        System.out.println(sql);
	        rs = stmt.executeQuery(sql);
	        String finenum,fine,sakestr,statestr,bnum,bname,btime,rtime;
	        boolean sake,state;
	        fine_table.clear();
	        while(rs.next())
	        {
	        	finenum = rs.getString("罚单号");
	        	fine = rs.getString("罚金");
	        	sake = rs.getBoolean("罚款原因");
	        	state = rs.getBoolean("状态");
	        	bnum = rs.getString("借阅编号");
	        	bname = rs.getString("题名");
	        	if(bname!=null)
	        		bname = bname.trim();
	        	btime = rs.getString("借阅时间");
	        	rtime = rs.getString("归还时间");
	        	sakestr = sake?"延期":"破损";
	        	statestr = state?"待付":"已付";
	        	fine_table.add(new Fine(finenum,fine,sakestr,statestr,bnum,bname,btime,rtime));
	        }

	        table_fine.setItems(fine_table);
  	        
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
    private void btn_return_clicked(ActionEvent event)
    {
    	BHistory selhis = table_bhistory.getSelectionModel().getSelectedItem();
    	if(selhis==null)
    		return;
    	String bnum,statestr;
    	bnum = selhis.getBNum();
    	statestr = selhis.getStat();
    	if(statestr.equals("在借")==false)
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
         		"该条记录不是在借记录！", "警告", JOptionPane.WARNING_MESSAGE);
    	}
    	else
    	{
    		try
    		{
     	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
     	        stmt = conn.createStatement();
    	         
     	        sql = "update dbo.借阅 set 状态=3 where 借阅编号="+bnum;
    	        stmt.executeUpdate(sql);
 	    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	 	            "还书请求发送成功,等待管理员审核", "提示", JOptionPane.INFORMATION_MESSAGE);
      	        
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
    			tab_bhistory_sel(null);
    		}
    	}
    }
    
    @FXML
    private void tab_bhistory_sel(Event event)
    {
    	if(tab_bhistory.isSelected()==false)
    		return;
    	System.out.println("tab_bhistory_sel");
		try
		{
 	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
 	        stmt = conn.createStatement();
 	        stmt2= conn.createStatement();
	         
 	        sql = "select t.条码,t3.状态,t2.题名,t3.借阅时间,t3.借阅编号 from "
 	       		+ "dbo.馆藏 t,dbo.图书 t2,dbo.借阅 t3 " + 
 	       		"where t.条码=t3.条码 and t.索书号=t2.索书号 and t3.读者编号='"+Main.getUser()+"'";
	        rs = stmt.executeQuery(sql);
	        String barcode,statestr,bname,bnum,btime;
	        int state=0;
	        borrow_table.clear();
	        while(rs.next())
	        {
	        	barcode = rs.getString("条码");
	        	state = rs.getInt("状态");
	        	bname = rs.getString("题名");
	        	bnum = rs.getString("借阅编号");
	        	btime = rs.getString("借阅时间");
	        	statestr = state==1?"借书审核中":state==2?"在借":state==0?"借阅失败":
	        		state==3?"还书审核中":state==4?"已还":"";
	        	if(bname!=null)
	        	{
	        		bname = bname.trim();
	        	}
	        	borrow_table.add(new BHistory(bnum,barcode,bname,btime,statestr));
	        }
	        table_bhistory.setItems(borrow_table);
  	        
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
    private void btn_search_clicked(ActionEvent event)
    {
    	String in_bname,in_author,in_press;
    	in_bname = txt_search_bname.getText();
    	in_author = txt_search_author.getText();
    	in_press = txt_search_press.getText();
    	if(in_bname.isEmpty()&&in_author.isEmpty()&&in_press.isEmpty())
    	{
    		table_book.setItems(book_table);
    		return;
    	}
		try
		{
			search_book_table.clear();
			
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        sql = "select * from dbo.图书 t,dbo.馆藏 t2,dbo.著作 t3 where t.索书号=t2.索书号 "
	        		+ " and t3.索书号=t.索书号 and ( ";
	        boolean flag=false;
	        if(in_bname.isEmpty()==false)
	        {
	        	sql += "t.题名 like '%"+in_bname+"%' ";
	        	flag=true;
	        }
	        if(in_author.isEmpty()==false)
	        {
	        	if(flag)
	        	{
	        		sql += " and ";
	        	}
	        	flag=true;
	        	sql += "t3.作者 = '"+in_author+"' ";
	        }
	        if(in_press.isEmpty()==false)
	        {
	        	if(flag)
	        	{
	        		sql += " and ";
	        	}
	        	flag=true;
	        	sql += "t.出版社 like '"+in_press+"%' ";
	        }
	        sql += ") order by t2.条码,t.索书号,t3.作者";
	        
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
    private void btn_borrow_clicked(ActionEvent event)
    {
    	BookInfo selbook = table_book.getSelectionModel().getSelectedItem();
    	if(selbook==null)
    		return;
    	String barcode = selbook.getBarCode();
    	int state;
		try
		{
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        stmt = conn.createStatement();
	        stmt2 = conn.createStatement();
	        
	        sql = "select 状态 from dbo.馆藏 where 条码='"+barcode+"'";
	        System.out.println(sql);
	        rs = stmt.executeQuery(sql);
	        if(rs.next())
	        {
	        	state = rs.getInt("状态");
	        	
	        	switch(state)
	        	{
	        	case 1:
	        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        		sql = "insert into dbo.借阅(条码,读者编号,借阅时间,状态"
	        				+ ") values("+barcode+",'"+Main.getUser()+"',"
	        				+ "'"+df.format(new Date())+"',1)";
	        		System.out.println(sql);
	        		Statement stmt3 = conn.createStatement();
	        		stmt3.executeUpdate(sql);
	        		JOptionPane.showMessageDialog(new JFrame().getContentPane(),
	             		"借书请求发送成功\n等待管理员审核", "提示", JOptionPane.INFORMATION_MESSAGE);
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
	        
	        stmt2.close();
	        rs.close();
	        rs2.close();
	        stmt.close();
	        conn.close();
		}
    	catch(SQLException se){
    		se.printStackTrace();
	    }
    }
    
    @FXML
    private void btn_logout_clicked(ActionEvent event)
    {
    }
    
    @FXML
    private void btn_exit_clicked(ActionEvent event)
    {
    	Event.fireEvent(Main.getPrimaryStage(),
    			new WindowEvent(Main.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    
}
