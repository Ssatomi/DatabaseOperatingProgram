package library.module;

import javafx.beans.property.SimpleStringProperty;

public class User 
{  
    private final SimpleStringProperty unum; 
    private final SimpleStringProperty uname; 
    private final SimpleStringProperty bcount;
    private final SimpleStringProperty tfine;
    private final SimpleStringProperty pswd;
   
    public User(String num, String name, String count,String fine,String psw) 
    {
    	this.unum = new SimpleStringProperty(num);
        this.uname = new SimpleStringProperty(name);
        this.bcount = new SimpleStringProperty(count);
        this.tfine = new SimpleStringProperty(fine);
        this.pswd = new SimpleStringProperty(psw);
    }  
   
    public String getUnum() {  
        return unum.get();  
    }  
    public void setUnum(String fName) {  
    	unum.set(fName);  
    }
    
    public String getUname() {  
        return uname.get();  
    }  
    public void setUname(String fName) {  
    	uname.set(fName);  
    }
          
    public String getBcount() {  
        return bcount.get();  
    }  
    public void setBcount(String fName) {  
    	bcount.set(fName);  
    }  
      
    public String getTfine() {  
        return tfine.get();  
    }  
    public void setTfine(String fName) {  
    	tfine.set(fName);  
    }
    
    public String getPswd() {  
        return pswd.get();  
    }  
    public void setPswd(String fName) {  
    	pswd.set(fName);  
    }  
}  