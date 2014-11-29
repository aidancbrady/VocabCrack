package com.aidancbrady.vocab.frames;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.panels.ActiveGamePanel;
import com.aidancbrady.vocab.panels.FriendsPanel;
import com.aidancbrady.vocab.panels.GamesPanel;
import com.aidancbrady.vocab.panels.LoginPanel;
import com.aidancbrady.vocab.panels.MenuPanel;
import com.aidancbrady.vocab.panels.NewGamePanel;
import com.aidancbrady.vocab.panels.OptionsPanel;
import com.aidancbrady.vocab.panels.RegisterPanel;

public class VocabFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	public List<JPanel> panels = new ArrayList<JPanel>();
	
	public LoginPanel login;
	public RegisterPanel register;
	public MenuPanel menu;
	public FriendsPanel friends;
	public GamesPanel games;
	public NewGamePanel newGame;
	public OptionsPanel options;
	public ActiveGamePanel activeGame;
	
	public NewFriendFrame newFrame;
	public UserDetailsFrame userDetailsFrame;
	public GameDetailsFrame gameDetailsFrame;
	public GameFrame gameFrame;
	
	public VocabFrame()
	{
		setTitle("VocabCrack");
		setSize(400, 600);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(this);
		
		addPanel(login = new LoginPanel(this)).setVisible(true);
		addPanel(register = new RegisterPanel(this)).setVisible(false);
		addPanel(menu = new MenuPanel(this)).setVisible(false);
		addPanel(friends = new FriendsPanel(this)).setVisible(false);
		addPanel(games = new GamesPanel(this)).setVisible(false);
		addPanel(newGame = new NewGamePanel(this)).setVisible(false);
		addPanel(options = new OptionsPanel(this)).setVisible(false);
		addPanel(activeGame = new ActiveGamePanel(this)).setVisible(false);
		
		//openGame(Game.DEFAULT);
		
		Utilities.loadData();
	}
	
	public JPanel addPanel(JPanel panel)
	{
		add(panel);
		panels.add(panel);
		
		return panel;
	}
	
	public void open(JPanel panel)
	{
		for(JPanel p : panels)
		{
			p.setVisible(false);
		}
		
		panel.setVisible(true);
	}
	
	public void openLogin()
	{
		open(login);
	}
	
	public void openRegister()
	{
		open(register);
	}
	
	public void openMenu()
	{
		open(menu);
	}
	
	public void openFriends()
	{
		open(friends);
	}
	
	public void openGames()
	{
		open(games);
	}
	
	public void openNewGame(String acct, JPanel panel)
	{		
		open(newGame);
		
		newGame.initInfo(acct, panel);
	}
	
	public void openOptions()
	{
		open(options);
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
	
	public void openUserDetails(String username)
	{
		if(userDetailsFrame == null)
		{
			userDetailsFrame = new UserDetailsFrame(this);
		}
		else {
			userDetailsFrame.toFront();
		}
		
		userDetailsFrame.open(username);
	}
	
	public void openGameDetails(Game g)
	{
		if(gameDetailsFrame == null)
		{
			gameDetailsFrame = new GameDetailsFrame(this);
		}
		else {
			gameDetailsFrame.toFront();
		}
		
		gameDetailsFrame.open(g);
	}
	
	public void openGame(Game g)
	{
		open(activeGame);
		
		if(gameFrame == null)
		{
			gameFrame = new GameFrame(this);
		}
		else {
			gameFrame.toFront();
		}
		
		gameFrame.initGame(g);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}
