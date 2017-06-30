package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ViewTeacherController.techertInfo;
import Entity.claSS;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * ParentViewInfoController - Controller for presenting the student information for the principal.
 * @author Or cohen
 * */
public class ParentViewInfoController implements Initializable{
	
	/**ClassCB - Class list combo box*/
    @FXML
    private ComboBox<String> ClassCB;
    
    /**SelectBtn - Select button*/
    @FXML
    private Button SelectBtn;
    
    /**ParentTable - Parent table view*/
    @FXML
    private TableView<ParentInfo> ParentTable;

    /**CildrenVL - Student ID*/
    @FXML
    private ListView<String> CildrenVL;

    /**IdCollum - Parent ID column*/
    @FXML
    private TableColumn<ParentInfo, String> IdCollum;

    @FXML
    private TableColumn<ParentInfo, String> AddressCollum;
    
    @FXML
    private TableColumn<ParentInfo, String> NameCollum;

    @FXML
    private TableColumn<ParentInfo, String> MailCollum;

    @FXML
    private TableColumn<ParentInfo, String> TelephoneCollum;

    @FXML
    private TableColumn<ParentInfo, String> isBlockedCollum;
     

    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		IdCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("id"));
		NameCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("name"));
		MailCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("mail"));
		isBlockedCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("isBlocked"));
		TelephoneCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("telephone"));
		AddressCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("address"));
		
	}

	/**
	 * ParentInfo  - Special class which created for the Parent Student relationship table view
	 * */
    public static class ParentInfo{

	    
        private StringProperty id;
        public void setId(String value) { idProperty().set(value); }
        public String getId() { return idProperty().get(); }
        public StringProperty idProperty() { 
            if (id == null) id = new SimpleStringProperty(this, "id");
            return id; 
        } 
        
    	private StringProperty name;
        public void setName(String value) { nameProperty().set(value); }
        public String getName() { return nameProperty().get(); }
        public StringProperty nameProperty() { 
            if (name == null) name = new SimpleStringProperty(this, "name");
            return name; 
        }
        
        private StringProperty address;
        public void setAddress(String value) { addressProperty().set(value); }
        public String getAddress() { return addressProperty().get(); }
        public StringProperty addressProperty() { 
            if (address == null) address = new SimpleStringProperty(this, "address");
            return address; 
        }
        
        private StringProperty mail;
        public void setMail(String value) { mailProperty().set(value); }
        public String getMail() { return mailProperty().get(); }
        public StringProperty mailProperty() { 
            if (mail == null) mail = new SimpleStringProperty(this, "mail");
            return mail; 
        }
        
        private StringProperty telephone;
        public void setTelephone(String value) { telephoneProperty().set(value); }
        public String getTelephone() { return telephoneProperty().get(); }
        public StringProperty telephoneProperty() { 
            if (telephone == null) telephone = new SimpleStringProperty(this, "telephone");
            return telephone; 
        } 
        
        
        private StringProperty isBlocked;
        public void setisBlocked(String value) { isBlockedProperty().set(value); }
        public String getisBlocked() { return isBlockedProperty().get(); }
        public StringProperty isBlockedProperty() { 
            if (isBlocked == null) isBlocked = new SimpleStringProperty(this, "isBlocked");
            return isBlocked; 
        }
        
        public ParentInfo(String id, String name, String mail, String address, String telephone, String isBlocked){
        	setId(id);
        	setName(name);
        	setisBlocked(isBlocked);
        	setMail(mail);
        	setAddress(address);
        	setTelephone(telephone);
        }
    }





    @FXML
    void BtnClick(ActionEvent event) {

    }

    @FXML
    void ChoosedParent(MouseEvent event) {

    }

    @FXML
    void ChoosedClass(ActionEvent event) {
    	ParentTable.setDisable(false);
    }
	
	
	
}
