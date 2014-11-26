package com.aidancbrady.vocab.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.frames.VocabFrame;
import com.aidancbrady.vocab.net.GameHandler;
import com.aidancbrady.vocab.tex.AvatarHandler;

public class GamesPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public JTextField searchField;
	
	public JList<Game> gamesList;
	
	public JButton gamesButton;
	public JButton pastButton;
	public JButton backButton;
	public JButton refreshButton;
	public JButton searchButton;
	public JButton newButton;
	public JButton playButton;
	
	public JLabel title;
	
	public boolean dataLoaded = false;
	
	public boolean mode = true;
	
	public Vector<Game> displayedGames = new Vector<Game>();
	public Vector<Game> displayedPast = new Vector<Game>();
	
	public Vector<Game> displayedList = new Vector<Game>();
	
	public boolean isLoading;
	
	public GamesPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
		
		title = new JLabel("Games List");
		title.setFont(new Font("Helvetica", Font.BOLD, 14));
		title.setVisible(true);
		title.setSize(200, 40);
		title.setLocation(150, 16);
		add(title);
		
		searchField = new JTextField();
		searchField.setFocusable(true);
		searchField.setText("");
		searchField.setSize(new Dimension(240, 28));
		searchField.setLocation(40, 70);
		searchField.addActionListener(new SearchListener());
		add(searchField);
		
		gamesList = new JList<Game>();
		gamesList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					if(mode)
					{
						openDetails();
					}
					else {
						accept();
					}
				}
			}
		});
		gamesList.addMouseListener(new PopupListener());
		
		gamesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gamesList.setBorder(new TitledBorder(new EtchedBorder(), "Games"));
		gamesList.setVisible(true);
		gamesList.setFocusable(true);
		gamesList.setEnabled(true);
		gamesList.setSelectionInterval(1, 1);
		gamesList.setCellRenderer(new GameCellRenderer());
		gamesList.setToolTipText("Games in progress and past games");
		JScrollPane onlinePane = new JScrollPane(gamesList);
		onlinePane.setSize(new Dimension(400, 250));
		onlinePane.setLocation(0, 100);
		add(onlinePane);
		
		backButton = new JButton("Back");
		backButton.setSize(60, 30);
		backButton.setLocation(16, 16);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.openMenu();
			}
		});
		add(backButton);
		
		refreshButton = new JButton("Refresh");
		refreshButton.setSize(80, 30);
		refreshButton.setLocation(300, 16);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				GameHandler.updateData(GamesPanel.this);
			}
		});
		add(refreshButton);
		
		searchButton = new JButton("Search");
		searchButton.setSize(80, 30);
		searchButton.setLocation(280, 69);
		searchButton.addActionListener(new SearchListener());
		add(searchButton);
		
		gamesButton = new JButton("Games");
		gamesButton.setSize(160, 30);
		gamesButton.setLocation(40, 360);
		gamesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				title.setLocation(150, 16);
				title.setText("Games List");
				gamesButton.setEnabled(false);
				pastButton.setEnabled(true);
				mode = true;
				resetList();
				
				GameHandler.updateData(GamesPanel.this);
			}
		});
		gamesButton.setEnabled(false);
		add(gamesButton);
		
		pastButton = new JButton("Past Games");
		pastButton.setSize(160, 30);
		pastButton.setLocation(200, 360);
		pastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				title.setLocation(150, 16);
				title.setText("Past Games");
				gamesButton.setEnabled(true);
				pastButton.setEnabled(false);
				mode = false;
				resetList();
				
				GameHandler.updateData(GamesPanel.this);
			}
		});
		add(pastButton);
		
		newButton = new JButton("New Game");
		newButton.setSize(320, 30);
		newButton.setLocation(40, 390);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.openFriends();
				JOptionPane.showMessageDialog(GamesPanel.this, "Create a new game using your friends list.");
			}
		});
		add(newButton);
		
		playButton = new JButton("Play");
		playButton.setSize(240, 60);
		playButton.setLocation(80, 440);
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				newGame();
			}
		});
		add(playButton);
		
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
	
	public void setLoggingIn(boolean loggingIn)
	{
		gamesButton.setEnabled(!loggingIn);
	}
	
	public class PopupListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			if(e.isPopupTrigger())
			{
				openPopup(e);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if(e.isPopupTrigger())
			{
				openPopup(e);
			}
		}
		
		private void openPopup(MouseEvent e)
		{
			if(displayedList.isEmpty())
			{
				return;
			}
			
			int row = gamesList.locationToIndex(e.getPoint());
			gamesList.setSelectedIndex(row);
			
			if(row >= 0)
			{
				int type = 0;
				
				if(!isLoading)
				{
					Game g = displayedList.get(row);
					
					if(mode)
					{
						if(g.isRequest)
						{
							type = g.activeRequested ? 2 : 3;
						}
						else {
							type = 0;
						}
					}
					else {
						type = 1;
					}
		
					PopupMenu menu = new PopupMenu(type);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}
	
	public void setLoading(boolean loading)
	{
		isLoading = loading;
		
		searchButton.setEnabled(!loading);
		
		if(loading)
		{
			gamesButton.setEnabled(false);
			pastButton.setEnabled(false);
		}
		else {
			gamesButton.setEnabled(!mode);
			pastButton.setEnabled(mode);
		}
	}
	
	public void resetList()
	{
		searchField.setText("");
		
		if(mode)
		{
			displayedList = (Vector<Game>)displayedGames;
		}
		else {
			displayedList = (Vector<Game>)displayedPast;
		}
		
		gamesList.setListData(displayedList);
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		if(!visible)
		{
			displayedGames.clear();
			displayedPast.clear();
			displayedList.clear();
			searchField.setText("");
			
			dataLoaded = false;
			
			gamesButton.setEnabled(false);
			pastButton.setEnabled(true);
			
			mode = true;
			
			gamesList.setListData(displayedList);
		}
		else {
			GameHandler.updateData(GamesPanel.this);
		}
	}
	
	public class SearchListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String query = searchField.getText().trim().toLowerCase();
			
			if(!isLoading && displayedList != null)
			{
				displayedList.clear();
				
				if(mode)
				{
					if(!query.isEmpty())
					{
						for(Game g : displayedGames)
						{
							String opponent = g.isRequest ? g.getRequestOpponent() : g.opponent;
							
							if(opponent.toLowerCase().contains(query))
							{
								displayedList.add(g);
							}
						}
					}
					else {
						displayedList = (Vector<Game>)displayedGames.clone();
					}
				}
				else {
					if(!query.isEmpty())
					{
						for(Game g : displayedPast)
						{
							if(g.opponent.toLowerCase().contains(query))
							{
								displayedList.add(g);
							}
						}
					}
					else {
						displayedList = (Vector<Game>)displayedPast.clone();
					}
				}
			}
		}
	}
	
	public void openDetails()
	{
		if(!gamesList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Game g = displayedList.get(gamesList.getSelectedIndex());
			
			if(mode)
			{
				if(!g.isRequest)
				{
					frame.openDetails(g);
				}
			}
		}
	}
	
	public void play()
	{
		if(!gamesList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			String name = displayedList.get(gamesList.getSelectedIndex()).opponent;
			
			if(mode)
			{
				//Play
			}
		}
	}
	
	public void newGame()
	{
		if(!gamesList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			String name = displayedList.get(gamesList.getSelectedIndex()).opponent;
			
			if(!mode)
			{
				if(JOptionPane.showConfirmDialog(GamesPanel.this, "Start a game with " + name + "?", "Confirm Game", JOptionPane.YES_NO_OPTION) == 0)
				{
					if(GameHandler.confirmGame(name, this))
					{
						//New game
					}
					else {
						JOptionPane.showMessageDialog(this, "Unable to authenticate");
					}
				}
			}
		}
	}
	
	public void delete(int type)
	{
		if(!gamesList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Game g = displayedList.get(gamesList.getSelectedIndex());
			
			String msg = type == 0 ? "Are you sure you want to delete your game with " + g.opponent + "?" : 
				(type == 1 ? "Are you sure you want to remove your past game with " + g.opponent + "?" : 
					(type == 2 ? "Are you sure you want to cancel your request to " + g.getRequestOpponent() + "?" : 
						"Are you sure you want to ignore " + g.getRequesterName() + "'s game request?"));
			
			if(JOptionPane.showConfirmDialog(GamesPanel.this, msg, "Confirm Deletion", JOptionPane.YES_NO_OPTION) == 0)
			{
				if(type == 1)
				{
					GameHandler.deleteGame(!g.isRequest ? g.opponent : g.getRequestOpponent(), type, GamesPanel.this, displayedPast.indexOf(g));
				}
				else {
					GameHandler.deleteGame(!g.isRequest ? g.opponent : g.getRequestOpponent(), type, GamesPanel.this);
				}
			}
		}
	}
	
	public void accept()
	{
		if(!gamesList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Game g = displayedList.get(gamesList.getSelectedIndex());
			
			if(mode && g.isRequest && !g.activeRequested)
			{
				if(JOptionPane.showConfirmDialog(GamesPanel.this, "Accept request from " + g.getRequesterName() + "?", "Confirm Request", JOptionPane.YES_NO_OPTION) == 0)
				{
					GameHandler.acceptRequest(g.getRequesterName(), GamesPanel.this);
				}
			}
		}
	}
	
	public class GameCellRenderer extends JLabel implements ListCellRenderer<Game>
	{
		private static final long serialVersionUID = 1L;
		
		private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

		public GameCellRenderer() 
		{
			setOpaque(true);
			setIconTextGap(12);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Game> list, Game value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if(mode)
			{
				if(value.isRequest)
				{
					setText(value.getRequestOpponent() + " - 0 to 0 - awaiting approval");
				}
				else {
					String msg = value.isTied() ? "tied " : (value.getWinning().equals(value.user) ? "winning " : "losing ");
					msg.concat(value.getUserScore() + " to " + value.getOpponentScore());
					setText(value.user + " - " + msg + " - " + (value.userTurn ? "your turn!" : "opponent's turn"));
				}
			}
			else {
				String msg = value.getWinning().equals(value.user) ? "won " : "lost ";
				msg.concat(value.getUserScore() + " to " + value.getOpponentScore());
				setText(value.user + " - " + msg);
			}
			
			try {
				setIcon(Utilities.scaleImage(new ImageIcon(AvatarHandler.downloadAvatar(value.opponentEmail).img), 32, 32));
			} catch(Exception e) {}
			
			if(isSelected) 
			{
				setBackground(HIGHLIGHT_COLOR);
				setForeground(Color.white);
			} 
			else {
				setBackground(Color.white);
				setForeground(Color.black);
			}
			
			return this;
		}
	}
	
	public class PopupMenu extends JPopupMenu
	{
		private static final long serialVersionUID = 1L;
		
		JMenuItem play = new JMenuItem("Play");
		JMenuItem details = new JMenuItem("Details");
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem remove = new JMenuItem("Remove");
		JMenuItem cancel = new JMenuItem("Cancel");
		JMenuItem accept = new JMenuItem("Accept");
		JMenuItem ignore = new JMenuItem("Ignore");
		
		public PopupMenu(int type)
		{
			play.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					play();
				}
			});
			details.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					openDetails();
				}
			});
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					delete(0);
				}
			});
			newGame.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					newGame();
				}
			});
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					delete(1);
				}
			});
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					delete(2);
				}
			});
			accept.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					accept();
				}
			});
			ignore.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					delete(3);
				}
			});
			
			if(type == 0 /*Active*/)
			{
				add(play);
				add(details);
				add(delete);
			}
			else if(type == 1 /*Past*/)
			{
				add(newGame);
				add(remove);
			}
			else if(type == 2 /*Request*/)
			{
				add(cancel);
			}
			else if(type == 3 /*Requested*/)
			{
				add(accept);
				add(ignore);
			}
		}
	}
}
