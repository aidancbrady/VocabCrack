package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Account;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.panels.LoginPanel;

public class LoginHandler 
{
	public static void login(LoginPanel panel)
	{
		String username = panel.usernameField.getText().trim();
		String password = panel.passwordField.getText().trim();
		
		if(username.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "No username entered.");
			return;
		}
		
		if(password.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "No password entered.");
			return;
		}
		
		if(!Utilities.isValidCredential(username, password))
		{
			JOptionPane.showMessageDialog(panel, "Invalid characters.");
			return;
		}
		
		panel.setLoggingIn(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LOGIN:" + username + "," + password);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoggingIn(false);
				
				String[] data = response[1].split(",");
				
				Account acct = new Account(username, data[0].trim(), password);
				acct.setGamesWon(Integer.parseInt(data[1]));
				acct.setGamesLost(Integer.parseInt(data[2]));
				
				VocabCrack.instance().account = acct;
				VocabCrack.instance().frame.menu.setAccountData();
				VocabCrack.instance().frame.openMenu();
				
				JOptionPane.showMessageDialog(panel, "Logged in successfully!");
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				panel.setLoggingIn(false);
				
				JOptionPane.showMessageDialog(panel, "Couldn't log in: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				panel.setLoggingIn(false);
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			panel.setLoggingIn(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
}
