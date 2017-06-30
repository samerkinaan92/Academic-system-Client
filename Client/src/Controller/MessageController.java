package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import Entity.Message;
import Entity.User;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This is the controller class for: "Message.fxml"
 * @author Idan Agam
 * */

public class MessageController implements Initializable {
	
	/** ListView for user messages */
	@FXML
    private ListView<String> messageList;

    @FXML
    private Label headerLabel;
    
    /** TextField for filtering list results */
    @FXML
    private TextField filter;
    
    /** Button to delete message from DB */
    @FXML
    private Button deleteBtn;
    
    /** Button to show message */
    @FXML
    private Button openBtn;
    
    Alert Infomsg = new Alert(AlertType.INFORMATION);
    Alert Errmsg = new Alert(AlertType.ERROR);
    
    /** ArrayList to store all user messages from DB */
    private ArrayList<Message> messageArr;
    
    /** ArrayList to store all user messages (id, from, title, date) */
    private ArrayList<String> organizedMessageList;
    
    //-------------------------------------------------------------------------------------------------------------------
    
    /**
     * Get organized message list
     * @param messageArr User message list from DB
     * @return organized message list
     */
    private ArrayList<String> getMessageList(ArrayList<Message> messageArr){ 
    	
    	User user;
    	
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < messageArr.size(); i++){
			user = User.getUserInfo(String.valueOf(messageArr.get(i).getFrom()));
			String node = "ID: (" + messageArr.get(i).getID() + ")\tFrom: " + user.getName() + " (" +
					user.getID() + ")" + "\tTitle: " + messageArr.get(i).getTitle() + "\tDate: " +
					messageArr.get(i).getSendTime();
			temp.add(node);
		}
		return temp;	
	}

    /**
     * Filter user message results in ListView
     * @param e
     */
    public void filter(ActionEvent e){
    	
    	ArrayList<String> temp = new  ArrayList<String>();
    	
    	for (int i = 0; i < organizedMessageList.size(); i++)
    		if (organizedMessageList.get(i).toLowerCase().contains(filter.getText().toLowerCase()))
    			temp.add(organizedMessageList.get(i));
    	
    	messageList.setItems(FXCollections.observableArrayList(temp));
    }
    
    //-------------------------------------------------------------------------------------------------------------------
    
    /**
     * Get all undeleted messages from DB and load result to ListView.
     * @param e
     */
    public void refresh(ActionEvent e){
   
    	messageArr = Message.getUserMessages(Main.user.getID());
		organizedMessageList = getMessageList(messageArr);
		Collections.sort(organizedMessageList);
		messageList.setItems(FXCollections.observableArrayList(organizedMessageList));
    }
    
    /**
     * Delete the selected message from DB.
     * @param e
     */
    public void delete(ActionEvent e){
    	
    	String msgID = messageList.getSelectionModel().getSelectedItem();
    	msgID = msgID.substring(msgID.indexOf('(')+1, msgID.indexOf(')'));
    	int flag = Message.deleteMsg(msgID);
    	
    	if (flag > 0){
    		Infomsg.setTitle("Delete Message Succeeded");
    		Infomsg.setHeaderText(null);
    		Infomsg.setContentText("Message has been deleted from data base");
    		Infomsg.showAndWait();
    	}
    	else{
    		
    		Errmsg.setTitle("Delete Message failed");
    		Errmsg.setHeaderText(null);
    		Errmsg.setContentText("Message could not been deleted from data base");
    		Errmsg.showAndWait();
    	}
    	
    	refresh(e);
    	deleteBtn.setDisable(true);
    	openBtn.setDisable(true);
    	messageList.getSelectionModel().clearSelection();
    }
    
    /**
     * Open new window to view message
     * @param e
     */
    public void open(ActionEvent e){
    	
    	String selected = messageList.getSelectionModel().getSelectedItem();
    	int id = Integer.parseInt(selected.substring(selected.indexOf('(')+1, selected.indexOf(')')));
    	Message message = null;
    	for (int i = 0; i < messageArr.size(); i++){
    		if (messageArr.get(i).getID() == id){
    			message = messageArr.get(i);
    		}
    	}
    	
    	if (message != null){
    		Infomsg.setTitle("Message");
    		Infomsg.setHeaderText(message.getTitle());
    		Infomsg.setContentText(message.getMsg());    		
    		Infomsg.showAndWait();
    	}
    	
    	deleteBtn.setDisable(true);
    	openBtn.setDisable(true);
    	messageList.getSelectionModel().clearSelection();
    }
    
    //-------------------------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		headerLabel.setText(Main.user.getName());
		refresh(new ActionEvent());
		
		messageList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		
			    	if (messageList.getSelectionModel().getSelectedItem() == null){
			    		deleteBtn.setDisable(true);
				    	openBtn.setDisable(true);
			    	}
			    	else{
				    	deleteBtn.setDisable(false);
				    	openBtn.setDisable(false);
			    	}
			    }
			});
	}
}