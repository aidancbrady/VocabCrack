package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.panels.GamesPanel;

public class GameHandler 
{
	public static void updateData(GamesPanel panel)
	{
		updateGames(panel);
		updatePast(panel);
		
		panel.dataLoaded = true;
	}
	
	public static void updateGames(GamesPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LGAMES:" + VocabCrack.instance().account.username);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				String[] response1 = reader.readLine().trim().split(":");
				
				socket.close();
				panel.setLoading(false);
				
				Vector<Game> vec = new Vector<Game>();
				
				for(int i = 1; i < response.length; i++)
				{
					Game g = Game.readDefault(VocabCrack.instance().account.username, response[i], ',');
					g.opponentEmail = response[++i];
					vec.add(g);
				}
				
				for(int i = 1; i < response1.length; i++)
				{
					Game g = Game.readRequest(VocabCrack.instance().account.username, response1[i], ',');
					g.isRequest = true;
					g.opponentEmail = response1[++i];
					vec.add(g);
				}
				
				panel.displayedGames = vec;
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
	
	public static void updatePast(GamesPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("LPAST:" + VocabCrack.instance().account.username);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				Vector<Game> vec = new Vector<Game>();
				
				for(int i = 1; i < response.length; i++)
				{
					Game g = Game.readDefault(VocabCrack.instance().account.username, response[i], ',');
					g.opponentEmail = response[++i];
					vec.add(g);
				}
				
				panel.displayedPast = vec;
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
	
	public static void acceptRequest(String friend, GamesPanel panel)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("GAMEREQCONF:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				Game found = null;
				
				for(Iterator<Game> iter = panel.displayedGames.iterator(); iter.hasNext();)
				{
					Game g = iter.next();
					String opponent = g.isRequest ? g.getRequestOpponent() : g.opponent;
					
					if(opponent.equals(friend))
					{
						found = g;
						iter.remove();
						break;
					}
				}
				
				panel.displayedGames.add(found.convertToActive());
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
	
	public static void deleteGame(String friend, int type, GamesPanel panel, Integer... index)
	{
		panel.setLoading(true);
		
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("DELFRIEND:" + VocabCrack.instance().account.username + ":" + friend + ":" + type + (type == 1 ? index[0] : ""));
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				panel.setLoading(false);
				
				if(type == 0 || type == 2 || type == 3)
				{
					for(Iterator<Game> iter = panel.displayedGames.iterator(); iter.hasNext();)
					{
						Game g = iter.next();
						String opponent = g.isRequest ? g.getRequestOpponent() : g.opponent;
						
						if(opponent.equals(friend))
						{
							iter.remove();
							break;
						}
					}
					
					panel.resetList();
				}
				else if(type == 1 /*Past*/)
				{
					panel.displayedPast.remove(index[0]);
					panel.resetList();
				}
				
				JOptionPane.showMessageDialog(panel, "Successfully deleted game or request with " + friend);
				
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
	
	public static boolean confirmGame(String friend, JPanel panel)
	{
		Socket socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(VocabCrack.SERVER_IP, VocabCrack.SERVER_PORT), 5000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println("CONFGAME:" + VocabCrack.instance().account.username + ":" + friend);
			writer.flush();
			
			String[] response = reader.readLine().trim().split(":");
			
			if(response[0].equals("ACCEPT"))
			{
				socket.close();
				
				return true;
			}
			else if(response[0].equals("REJECT"))
			{
				socket.close();
				
				JOptionPane.showMessageDialog(panel, "Couldn't process request: " + response[1]);
				
				return false;
			}
			else {
				socket.close();
				
				JOptionPane.showMessageDialog(panel, "Unable to parse response");
				
				return false;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
		}
		
		return false;
	}
}
