package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.NewFriendFrame;
import com.aidancbrady.vocab.VocabCrack;
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
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
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
				
				Vector<String> vec = new Vector<String>();
				
				for(int i = 1; i < response.length; i++)
				{
					vec.add(response[i]);
				}
				
				for(int i = 1; i < response1.length; i++)
				{
					vec.add(response1[i] + " (Requested)");
				}
				
				panel.displayedFriends = vec;
				panel.displayedList = vec;
				
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
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LREQUESTS:" + VocabCrack.instance().account.username);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				Vector<String> vec = new Vector<String>();
				
				for(int i = 1; i < response.length; i++)
				{
					vec.add(response[i]);
				}
				
				panel.displayedRequests = vec;
				panel.displayedList = vec;
				
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
	
	public static void deleteFriend(String friend, int type, FriendsPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("DELFRIEND:" + VocabCrack.instance().account.username + ":" + friend + ":" + type);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				if(type == 0)
				{
					panel.displayedFriends.remove(friend);
					panel.displayedList.remove(friend);
				}
				else if(type == 1)
				{
					panel.displayedRequests.remove(friend);
					panel.displayedList.remove(friend);
				}
				else if(type == 2)
				{
					String name = friend.concat(" (Requested)");
					
					panel.displayedFriends.remove(name);
					panel.displayedList.remove(name);
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
	
	public static void updateSearch(NewFriendFrame frame)
	{
		String query = frame.searchField.getText().trim();
		
		if(query.isEmpty())
		{
			return;
		}
		
		if(query.contains(",") || query.contains(":"))
		{
			JOptionPane.showMessageDialog(frame, "Invalid characters.");
			return;
		}
		
		frame.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
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
	
	public static void sendRequest(String friend, NewFriendFrame frame)
	{
		frame.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("FRIENDREQ:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				frame.setLoading(false);
				
				frame.frame.friends.displayedFriends.remove(friend + " (Requested)");
				frame.frame.friends.displayedFriends.add(friend + " (Requested)");
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
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("REQCONF:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				panel.displayedFriends.add(friend);
				panel.displayedRequests.remove(friend);
				panel.resetList();
				
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
	
	public static void getInfo(String friend, FriendsPanel panel)
	{
		
	}
}