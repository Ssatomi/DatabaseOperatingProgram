package library.module;

import javafx.beans.property.SimpleStringProperty;

public class BookInfo 
{  
    private final SimpleStringProperty bookName; 
    private final SimpleStringProperty author; 
    private final SimpleStringProperty press;
    private final SimpleStringProperty ymd;
    private final SimpleStringProperty version;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty barCode;
    private final SimpleStringProperty store;
    private final SimpleStringProperty state;
   
    public BookInfo(String bname, String is, String auth,
    		String pre,String ym,String ver,String bar,String stor,String stat) 
    {
    	this.barCode = new SimpleStringProperty(bar);
        this.bookName = new SimpleStringProperty(bname);
        this.isbn = new SimpleStringProperty(is);
        this.author = new SimpleStringProperty(auth);  
        this.press = new SimpleStringProperty(pre);
        this.ymd = new SimpleStringProperty(ym);
        this.version = new SimpleStringProperty(ver);
        this.store = new SimpleStringProperty(stor);
        this.state = new SimpleStringProperty(stat);
    }  
   
    public String getBookName() {  
        return bookName.get();  
    }  
    public void setBookName(String fName) {  
    	bookName.set(fName);  
    }
    
    public String getBarCode() {  
        return barCode.get();  
    }  
    public void setBarCode(String fName) {  
    	barCode.set(fName);  
    }
          
    public String getAuthor() {  
        return author.get();  
    }  
    public void setAuthor(String fName) {  
    	author.set(fName);  
    }  
      
    public String getPress() {  
        return press.get();  
    }  
    public void setPress(String fName) {  
    	press.set(fName);  
    }
    
    public String getYmd() {  
        return ymd.get();  
    }  
    public void setYmd(String fName) {  
    	ymd.set(fName);  
    }
    
    public String getVersion() {  
        return version.get();  
    }  
    public void setVersion(String fName) {  
    	version.set(fName);  
    } 
    
    public String getIsbn() {  
        return isbn.get();  
    }  
    public void setIsbn(String fName) {  
    	isbn.set(fName);  
    }
    
    public String getState() {  
        return state.get();  
    }  
    public void setState(String fName) {  
    	state.set(fName);  
    } 
    
    public String getStore() {  
        return store.get();  
    }
    public void setStore(String fName) {  
    	store.set(fName);  
    } 
          
}  