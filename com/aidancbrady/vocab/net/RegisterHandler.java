package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.panels.RegisterPanel;

public class RegisterHandler 
{
	public static void register(RegisterPanel panel)
	{
		String username = panel.usernameField.getText().trim();
		String email = panel.emailField.getText().trim();
		String password = panel.passwordField.getText().trim();
		
		if(username.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "No username entered.");
			return;
		}
		
		if(email.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "No email entered.");
			return;
		}
		
		if(password.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "No password entered.");
			return;
		}
		
		if(!password.equals(panel.confirmField.getText().trim()))
		{
			JOptionPane.showMessageDialog(panel, "Passwords don't match.");
			return;
		}
		
		if(!Utilities.isValidCredential(username, email, password))
		{
			JOptionPane.showMessageDialog(panel, "Invalid characters.");
			return;
		}
		
		panel.setRegistering(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("REGISTER:" + username + "," + email + "," + password);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setRegistering(false);
				
				VocabCrack.instance().frame.openLogin();
				
				JOptionPane.showMessageDialog(panel, "Successfully created account '" + username + "'");
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				panel.setRegistering(false);
				
				JOptionPane.showMessageDialog(panel, "Couldn't register: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				panel.setRegistering(false);
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			panel.setRegistering(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
}
