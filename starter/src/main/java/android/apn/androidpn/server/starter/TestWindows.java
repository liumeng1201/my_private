package android.apn.androidpn.server.starter;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import android.apn.androidpn.server.xmpp.push.NotificationManager;

public class TestWindows extends Frame implements ActionListener {

    TextField inputApiKey, inputTitle, inputMessage, inputUri;
    String strApiKey, strTitle, strMessage, strUri;
    Button btnSend;
    
    TestWindows(String s) {
    	super(s);
    	setLayout(new FlowLayout());
    	btnSend = new Button("推送消息");
    	add(btnSend);
    	inputApiKey = new TextField("1234567890");
    	add(inputApiKey);
    	inputTitle = new TextField("input title");
    	add(inputTitle);
    	inputMessage = new TextField("input message");
    	add(inputMessage);
    	inputUri = new TextField("input uri");
    	add(inputUri);
    	
    	btnSend.addActionListener(this);
    	
    	setBounds(100, 100, 400, 400);
    	setVisible(true);
    	validate();
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnSend) {
			strApiKey = inputApiKey.getText().toString();
			strTitle = inputTitle.getText().toString();
			strMessage = inputMessage.getText().toString();
			strUri = inputUri.getText().toString();
			
			NotificationManager nm = new NotificationManager();
			nm.sendBroadcast(strApiKey, strTitle, strMessage, strUri);
		}
	}

}
