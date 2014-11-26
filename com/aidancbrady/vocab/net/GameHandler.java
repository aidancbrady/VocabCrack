package com.aidancbrady.vocab.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Account;
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
		
	}
	
	public static void deleteGame(String friend, int type, GamesPanel panel)
	{
		
	}
}
