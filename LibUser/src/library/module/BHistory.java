package library.module;

import javafx.beans.property.SimpleStringProperty;

public class BHistory 
{  
    private final SimpleStringProperty bNum; 
    private final SimpleStringProperty bCode; 
    private final SimpleStringProperty bNam;
    private final SimpleStringProperty bTime;
    private final SimpleStringProperty stat;
   
    public BHistory(String bnum, String bcode, String bname,String bt,String sta) 
    {
    	this.bNum = new SimpleStringProperty(bnum);
        this.bCode = new SimpleStringProperty(bcode);
        this.bNam = new SimpleStringProperty(bname);
        this.bTime = new SimpleStringProperty(bt);  
        this.stat = new SimpleStringProperty(sta);
    }  
   
    public String getBNum() {
        return bNum.get();  
    }  
    public void setBNum(String fName) {
    	bNum.set(fName);  
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
      
    public String getStat() {  
        return stat.get();  
    }  
    public void setStat(String fName) {
    	stat.set(fName);  
    }
    
    public String getBTime() {  
        return bTime.get();  
    }  
    public void setBTime(String fName) {
    	bTime.set(fName);  
    }     
}