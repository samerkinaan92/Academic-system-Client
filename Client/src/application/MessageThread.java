package application;

import javafx.scene.control.Label;

public class MessageThread extends Thread {
	public ClientConnection client;

	
	public MessageThread(ClientConnection Client){
		this.client=Client;
	}	
	/*This thread is waiting until the client get a message from the server,
	 * When the server is sending the message the static flag ClientConsole.answerFromServer will change to false*/
	public void run() {
		synchronized (this) { 
			while(!client.getAnswerFromServer());
        	client.setAnswerFromServer(false);	//message received from server
			notify();
		}
	}
	
	
}
