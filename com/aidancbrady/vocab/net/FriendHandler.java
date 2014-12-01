package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Account;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.frames.NewFriendDialog;
import com.aidancbrady.vocab.frames.UserDetailsDialog;
import com.aidancbrady.vocab.panels.FriendsPanel;

public class FriendHandler 
{
	public static void updateData(FriendsPanel panel)
	{
		updateFriends(panel);
		updateRequests(panel);
		
		panel.dataLoaded = true;
	}
	
	private static void updateFriends(FriendsPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LFRIENDS:" + VocabCrack.instance().account.username);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				String[] response1 = reader.readLine().trim().split(":");
				
				socket.close();
				panel.setLoading(false);
				
				Vector<Account> vec = new Vector<Account>();
				
				for(int i = 1; i < response.length; i++)
				{
					String[] split = response[i].split(",");
					vec.add(new Account(split[0], false).setEmail(split[1]));
				}
				
				for(int i = 1; i < response1.length; i++)
				{
					vec.add(new Account(response1[i], true));
				}
				
				panel.displayedFriends = (Vector<Account>)vec.clone();
				panel.resetList();
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Couldn't access results: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			panel.setLoading(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
	
	private static void updateRequests(FriendsPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LREQUESTS:" + VocabCrack.instance().account.username);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				Vector<Account> vec = new Vector<Account>();
				
				for(int i = 1; i < response.length; i++)
				{
					String[] split = response[i].split(",");
					vec.add(new Account(split[0], false).setEmail(split[1]));
				}
				
				panel.displayedRequests = (Vector<Account>)vec.clone();
				panel.resetList();
				
				if(panel.displayedRequests.size() > 0)
				{
					panel.requestsButton.setText("Requests (" + panel.displayedRequests.size() + ")");
				}
				else {
					panel.requestsButton.setText("Requests");
				}
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Couldn't access results: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			panel.setLoading(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
	
	public static void deleteFriend(String friend, int type, FriendsPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("DELFRIEND:" + VocabCrack.instance().account.username + ":" + friend + ":" + type);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				if(type == 0 || type == 2)
				{
					for(Iterator<Account> iter = panel.displayedFriends.iterator(); iter.hasNext();)
					{
						Account acct = iter.next();
						
						if(acct.username.equals(friend))
						{
							iter.remove();
							break;
						}
					}
					
					panel.resetList();
				}
				else if(type == 1)
				{
					for(Iterator<Account> iter = panel.displayedRequests.iterator(); iter.hasNext();)
					{
						Account acct = iter.next();
						
						if(acct.username.equals(friend))
						{
							iter.remove();
							break;
						}
					}
					
					panel.resetList();
				}
				
				JOptionPane.showMessageDialog(panel, "Successfully deleted user " + friend);
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Couldn't process request: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			panel.setLoading(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			e.printStackTrace();
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
	
	public static void updateSearch(NewFriendDialog frame)
	{
		String query = frame.searchField.getText().trim();
		
		if(query.contains(",") || query.contains(":"))
		{
			JOptionPane.showMessageDialog(frame, "Invalid characters.");
			return;
		}
		
		frame.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LUSERS:" + VocabCrack.instance().account.username + ":" + frame.searchField.getText().trim());
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				frame.setLoading(false);
				
				frame.displayedList.clear();
				
				if(response.length > 1)
				{
					String[] users = response[1].split(",");
					
					for(String s : users)
					{
						frame.displayedList.add(s);
					}
				}
				
				frame.friendsList.setListData(frame.displayedList);
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				frame.setLoading(false);
				
				JOptionPane.showMessageDialog(frame, "Couldn't access results: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				frame.setLoading(false);
				
				JOptionPane.showMessageDialog(frame, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			frame.setLoading(false);
			JOptionPane.showMessageDialog(frame, "Couldn't connect to server: " + e.getLocalizedMessage());
			e.printStackTrace();
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
	
	public static void sendRequest(String friend, NewFriendDialog frame)
	{
		frame.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("FRIENDREQ:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				frame.setLoading(false);
				
				for(Iterator<Account> iter = frame.frame.friends.displayedFriends.iterator(); iter.hasNext();)
				{
					Account acct = iter.next();
					
					if(acct.username.equals(friend))
					{
						iter.remove();
						break;
					}
				}
				
				frame.frame.friends.displayedFriends.add(new Account(friend, true));
				frame.frame.friends.resetList();
				
				frame.setVisible(false);
				frame.frame.toFront();
				
				JOptionPane.showMessageDialog(frame, "Sent request to user " + friend);
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				frame.setLoading(false);
				
				JOptionPane.showMessageDialog(frame, "Couldn't process request: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				frame.setLoading(false);
				
				JOptionPane.showMessageDialog(frame, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			frame.setLoading(false);
			JOptionPane.showMessageDialog(frame, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
	
	public static void acceptRequest(String friend, FriendsPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("REQCONF:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				panel.displayedFriends.add(new Account(friend, false));
				
				for(Iterator<Account> iter = panel.displayedRequests.iterator(); iter.hasNext();)
				{
					Account acct = iter.next();
					
					if(acct.username.equals(friend))
					{
						iter.remove();
						break;
					}
				}
				
				panel.resetList();
				
				if(panel.displayedRequests.size() > 0)
				{
					panel.requestsButton.setText("Requests (" + panel.displayedRequests.size() + ")");
				}
				else {
					panel.requestsButton.setText("Requests");
				}
				
				JOptionPane.showMessageDialog(panel, "You are now friends with " + friend + "!");
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Couldn't process request: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				panel.setLoading(false);
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			panel.setLoading(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
	
	public static void getInfo(String friend, UserDetailsDialog frame)
	{
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.serverIP, VocabCrack.serverPort), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("GETINFO:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				
				int won = Integer.parseInt(response[2]);
				int lost = Integer.parseInt(response[3]);
				long login = Long.parseLong(response[4]);
				
				frame.acct = new Account(friend, response[1], "password").setGamesWon(won).setGamesLost(lost).setLastLogin(login);
				
				frame.setAccountData();
				
				return;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				
				JOptionPane.showMessageDialog(frame, "Couldn't process request: " + response[1]);
				
				return;
			}
			else {
				socket.close();
				
				JOptionPane.showMessageDialog(frame, "Unable to parse response");
				
				return;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
	}
}
