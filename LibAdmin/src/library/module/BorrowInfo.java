package library.module;

import javafx.beans.property.SimpleStringProperty;

public class BorrowInfo 
{  
    private final SimpleStringProperty borrowNum; 
    private final SimpleStringProperty bCode; 
    private final SimpleStringProperty bNam;
    private final SimpleStringProperty userNum;
    private final SimpleStringProperty user;
    private final SimpleStringProperty bTime;
   
    public BorrowInfo(String bnum, String bcode, String bname,
    		String unum,String use,String bt) 
    {
    	this.borrowNum = new SimpleStringProperty(bnum);
        this.bCode = new SimpleStringProperty(bcode);
        this.bNam = new SimpleStringProperty(bname);
        this.userNum = new SimpleStringProperty(unum);  
        this.user = new SimpleStringProperty(use);
        this.bTime = new SimpleStringProperty(bt);
    }  
   
    public String getBorrowNum() {  
        return borrowNum.get();  
    }  
    public void setBorrowNum(String fName) {  
    	borrowNum.set(fName);  
    }
    
    public String getBCode() {  
        return bCode.get();  
    }  
    public void setBCode(String fName) {  
    	bCode.set(fName);  
    }
          
    public String getBNam() {  
        return bNam.get();  
    }  
    public void setBNam(String fName) {  
    	bNam.set(fName);  
    }  
      
    public String getUserNum() {  
        return userNum.get();  
    }  
    public void setUserNum(String fName) {  
    	userNum.set(fName);  
    }
    
    public String getUser() {  
        return user.get();  
    }  
    public void setUser(String fName) {  
    	user.set(fName);  
    }
    
    public String getBTime() {  
        return bTime.get();  
    }  
    public void setBTime(String fName) {  
    	bTime.set(fName);  
    }     
}