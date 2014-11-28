package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.panels.OptionsPanel;

public class PasswordHandler 
{
	public static void changePassword(OptionsPanel panel)
	{
		String current = panel.currentField.getText().trim();
		String password = panel.passwordField.getText().trim();
		
		if(current.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "Please enter your current password.");
			return;
		}
		
		if(password.isEmpty())
		{
			JOptionPane.showMessageDialog(panel, "No password entered.");
			return;
		}
		
		if(current.equals(password))
		{
			JOptionPane.showMessageDialog(panel, "New password can't match existing.");
			return;
		}
		
		if(!password.equals(panel.confirmField.getText().trim()))
		{
			JOptionPane.showMessageDialog(panel, "Passwords don't match.");
			return;
		}
		
		if(!Utilities.isValidCredential(current, password))
		{
			JOptionPane.showMessageDialog(panel, "Invalid characters.");
			return;
		}
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("CHANGEPASS:" + VocabCrack.instance().account.username + ":" + current + ":" + password);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				
				JOptionPane.showMessageDialog(panel, "Successfully updated password, logging out.");
				
				VocabCrack.instance().logout();
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				
				JOptionPane.showMessageDialog(panel, "Couldn't register: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
}
