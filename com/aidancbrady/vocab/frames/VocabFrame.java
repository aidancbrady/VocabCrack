package com.aidancbrady.vocab.frames;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.panels.FriendsPanel;
import com.aidancbrady.vocab.panels.GamesPanel;
import com.aidancbrady.vocab.panels.LoginPanel;
import com.aidancbrady.vocab.panels.MenuPanel;
import com.aidancbrady.vocab.panels.RegisterPanel;

public class VocabFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	public LoginPanel login;
	public RegisterPanel register;
	public MenuPanel menu;
	public FriendsPanel friends;
	public GamesPanel games;
	
	public NewFriendFrame newFrame;
	public DetailsFrame detailsFrame;
	
	public VocabFrame()
	{
		setTitle("VocabCrack");
		setSize(400, 600);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(this);
		
		add(login = new LoginPanel(this));
		add(register = new RegisterPanel(this)).setVisible(false);
		add(menu = new MenuPanel(this)).setVisible(false);
		add(friends = new FriendsPanel(this)).setVisible(false);
		add(games = new GamesPanel(this)).setVisible(false);
	}
	
	public void openLogin()
	{
		login.setVisible(true);
		register.setVisible(false);
		menu.setVisible(false);
		friends.setVisible(false);
		games.setVisible(false);
	}
	
	public void openRegister()
	{
		login.setVisible(false);
		register.setVisible(true);
		menu.setVisible(false);
		friends.setVisible(false);
		games.setVisible(false);
	}
	
	public void openMenu()
	{
		login.setVisible(false);
		register.setVisible(false);
		menu.setVisible(true);
		friends.setVisible(false);
		games.setVisible(false);
	}
	
	public void openFriends()
	{
		login.setVisible(false);
		register.setVisible(false);
		menu.setVisible(false);
		friends.setVisible(true);
		games.setVisible(false);
	}
	
	public void openGames()
	{
		login.setVisible(false);
		register.setVisible(false);
		menu.setVisible(false);
		friends.setVisible(false);
		games.setVisible(true);
	}
	
	public void openNewFriend()
	{
		if(newFrame == null)
		{
			newFrame = new NewFriendFrame(this);
		}
		else {
			newFrame.setVisible(true);
			newFrame.toFront();
		}
	}
	
	public void openDetails(String username)
	{
		if(detailsFrame == null)
		{
			detailsFrame = new DetailsFrame(this);
		}
		else {
			detailsFrame.toFront();
		}
		
		detailsFrame.open(username);
	}
	
	public void openDetails(Game g)
	{
		
	}
	
	@Override
	public void windowOpened(WindowEvent e)
	{
		
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) 
	{
		
	}

	@Override
	public void windowIconified(WindowEvent e) 
	{
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) 
	{
		
	}

	@Override
	public void windowActivated(WindowEvent e) 
	{
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) 
	{
		
	}
}
