package com.aidancbrady.vocab.panels;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.VocabFrame;
import com.aidancbrady.vocab.tex.Texture;

public class MenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public VocabFrame frame;
	
	public JButton gamesButton;
	public JButton friendsButton;
	public JButton optionsButton;
	public JButton logoutButton;
	
	public MenuPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setVisible(true);
		setLayout(null);
		
		gamesButton = new JButton("Games");
		gamesButton.setSize(300, 60);
		gamesButton.setLocation(50, 160);
		gamesButton.addActionListener(new GamesButtonListener());
		add(gamesButton);
		
		friendsButton = new JButton("Friends");
		friendsButton.setSize(300, 60);
		friendsButton.setLocation(50, 240);
		friendsButton.addActionListener(new FriendsButtonListener());
		add(friendsButton);
		
		optionsButton = new JButton("Options");
		optionsButton.setSize(300, 60);
		optionsButton.setLocation(50, 320);
		optionsButton.addActionListener(new OptionsButtonListener());
		add(optionsButton);
		
		logoutButton = new JButton("Logout");
		logoutButton.setSize(300, 60);
		logoutButton.setLocation(50, 400);
		logoutButton.addActionListener(new LogoutButtonListener());
		add(logoutButton);
		
		JLabel version = new JLabel("v1.0");
		version.setFont(new Font("Helvetica", Font.BOLD, 14));
		version.setVisible(true);
		version.setSize(200, 40);
		version.setLocation(340, 520);
		add(version);
		
		JLabel copyright = new JLabel("© aidancbrady, 2014");
		copyright.setVisible(true);
		copyright.setSize(200, 40);
		copyright.setLocation(30, 520);
		add(copyright);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		logo.draw(g, 50, 50, 300, 46);
	}
	
	public class GamesButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
	
	public class FriendsButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openFriends();
		}
	}
	
	public class OptionsButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
	
	public class LogoutButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			VocabCrack.instance().account = null;
			frame.openLogin();
			
			JOptionPane.showMessageDialog(MenuPanel.this, "Logged out successfully.");
		}
	}
}
