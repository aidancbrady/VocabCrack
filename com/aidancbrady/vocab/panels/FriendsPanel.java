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
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.vocab.Account;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.frames.VocabFrame;
import com.aidancbrady.vocab.net.FriendHandler;
import com.aidancbrady.vocab.net.GameHandler;
import com.aidancbrady.vocab.tex.AvatarHandler;

public class FriendsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public JTextField searchField;
	
	public JList<Account> friendsList;
	
	public JButton friendsButton;
	public JButton requestsButton;
	public JButton backButton;
	public JButton refreshButton;
	public JButton searchButton;
	public JButton newButton;
	public JButton newGameButton;
	
	public JLabel title;
	
	public boolean dataLoaded = false;
	
	public boolean mode = true;
	
	public Vector<Account> displayedFriends = new Vector<Account>();
	public Vector<Account> displayedRequests = new Vector<Account>();
	
	public Vector<Account> displayedList = new Vector<Account>();
	
	public boolean isLoading;
	
	public FriendsPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
		
		title = new JLabel("Friends List");
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
		
		friendsList = new JList<Account>();
		friendsList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event))
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
		friendsList.addMouseListener(new PopupListener());
		
		friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendsList.setBorder(new TitledBorder(new EtchedBorder(), "Friends"));
		friendsList.setVisible(true);
		friendsList.setFocusable(true);
		friendsList.setEnabled(true);
		friendsList.setSelectionInterval(1, 1);
		friendsList.setCellRenderer(new AccountCellRenderer());
		friendsList.setToolTipText("Friends that have you on their list");
		JScrollPane scroll = new JScrollPane(friendsList);
		scroll.setSize(new Dimension(400, 250));
		scroll.setLocation(0, 100);
		add(scroll);
		
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
				FriendHandler.updateData(FriendsPanel.this);
			}
		});
		add(refreshButton);
		
		searchButton = new JButton("Search");
		searchButton.setSize(80, 30);
		searchButton.setLocation(280, 69);
		searchButton.addActionListener(new SearchListener());
		add(searchButton);
		
		friendsButton = new JButton("Friends");
		friendsButton.setSize(160, 30);
		friendsButton.setLocation(40, 360);
		friendsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				title.setLocation(150, 16);
				title.setText("Friends List");
				friendsButton.setEnabled(false);
				requestsButton.setEnabled(true);
				mode = true;
				resetList();
				
				FriendHandler.updateData(FriendsPanel.this);
			}
		});
		friendsButton.setEnabled(false);
		add(friendsButton);
		
		requestsButton = new JButton("Requests");
		requestsButton.setSize(160, 30);
		requestsButton.setLocation(200, 360);
		requestsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				title.setLocation(140, 16);
				title.setText("Friend Requests");
				friendsButton.setEnabled(true);
				requestsButton.setEnabled(false);
				mode = false;
				resetList();
				
				FriendHandler.updateData(FriendsPanel.this);
			}
		});
		add(requestsButton);
		
		newButton = new JButton("New Friend");
		newButton.setSize(320, 30);
		newButton.setLocation(40, 390);
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.openNewFriend();
			}
		});
		add(newButton);
		
		newGameButton = new JButton("New Game");
		newGameButton.setSize(240, 60);
		newGameButton.setLocation(80, 440);
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				newGame();
			}
		});
		add(newGameButton);
		
		JLabel version = new JLabel("v" + VocabCrack.VERSION);
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
		friendsButton.setEnabled(!loggingIn);
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
			
			int row = friendsList.locationToIndex(e.getPoint());
			friendsList.setSelectedIndex(row);
			
			if(row >= 0)
			{
				int type = 0;
				
				if(!isLoading)
				{
					if(mode)
					{
						type = displayedList.get(row).isRequest ? 2 : 0;
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
			friendsButton.setEnabled(false);
			requestsButton.setEnabled(false);
		}
		else {
			friendsButton.setEnabled(!mode);
			requestsButton.setEnabled(mode);
		}
	}
	
	public void resetList()
	{
		searchField.setText("");
		
		if(mode)
		{
			displayedList = (Vector<Account>)displayedFriends.clone();
		}
		else {
			displayedList = (Vector<Account>)displayedRequests.clone();
		}
		
		friendsList.setListData(displayedList);
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		if(!visible)
		{
			displayedFriends.clear();
			displayedRequests.clear();
			displayedList.clear();
			searchField.setText("");
			
			dataLoaded = false;
			
			friendsButton.setEnabled(false);
			requestsButton.setEnabled(true);
			
			mode = true;
			
			requestsButton.setText("Requests");
			
			friendsList.setListData(displayedList);
		}
		else {
			FriendHandler.updateData(FriendsPanel.this);
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
						for(Account a : displayedFriends)
						{
							if(a.username.toLowerCase().contains(query))
							{
								displayedList.add(a);
							}
						}
					}
					else {
						displayedList = (Vector<Account>)displayedFriends.clone();
					}
				}
				else {
					if(!query.isEmpty())
					{
						for(Account a : displayedRequests)
						{
							if(a.username.toLowerCase().contains(query))
							{
								displayedList.add(a);
							}
						}
					}
					else {
						displayedList = (Vector<Account>)displayedRequests.clone();
					}
				}
			}
			
			friendsList.setListData(displayedList);
		}
	}
	
	public void openDetails()
	{
		if(!friendsList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Account a = displayedList.get(friendsList.getSelectedIndex());
			
			if(mode)
			{
				if(!a.isRequest)
				{
					frame.openUserDetails(a.username);
				}
			}
		}
	}
	
	public void newGame()
	{
		if(!friendsList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Account a = displayedList.get(friendsList.getSelectedIndex());
			
			if(mode)
			{
				if(!a.isRequest)
				{
					if(JOptionPane.showConfirmDialog(this, "Start a game with " + a.username + "?", "Confirm Game", JOptionPane.YES_NO_OPTION) == 0)
					{
						if(GameHandler.confirmGame(a.username, this))
						{
							frame.openNewGame(a.username, this);
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(FriendsPanel.this, "User " + a.username + " has not yet accepted your request.");
				}
			}
		}
	}
	
	public void delete(int type)
	{
		if(!friendsList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Account a = displayedList.get(friendsList.getSelectedIndex());
			
			String msg = type == 0 ? "Are you sure you want to delete " + a.username + "?" : (type == 1 ? "Are you sure you want to ignore " + a.username + "'s request?" : 
				"Are you sure you want to cancel your request to " + a.username + "?");
			
			if(JOptionPane.showConfirmDialog(FriendsPanel.this, msg, "Confirm Deletion", JOptionPane.YES_NO_OPTION) == 0)
			{
				FriendHandler.deleteFriend(a.username, type, FriendsPanel.this);
			}
		}
	}
	
	public void accept()
	{
		if(!friendsList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			Account a = displayedList.get(friendsList.getSelectedIndex());
			
			if(!mode)
			{
				if(JOptionPane.showConfirmDialog(FriendsPanel.this, "Accept request from " + a.username + "?", "Confirm Request", JOptionPane.YES_NO_OPTION) == 0)
				{
					FriendHandler.acceptRequest(a.username, FriendsPanel.this);
				}
			}
		}
	}
	
	public class AccountCellRenderer extends JLabel implements ListCellRenderer<Account>
	{
		private static final long serialVersionUID = 1L;
		
		private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

		public AccountCellRenderer() 
		{
			setOpaque(true);
			setIconTextGap(12);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Account> list, Account value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if(mode)
			{
				if(value.isRequest)
				{
					setText(value.username + " (Requested)");
				}
				else {
					setText(value.username);
				}
			}
			else {
				setText(value.username);
			}
			
			try {
				setIcon(Utilities.scaleImage(new ImageIcon(AvatarHandler.downloadAvatar(value).img), 32, 32));
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
		
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem details = new JMenuItem("Details");
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem accept = new JMenuItem("Accept");
		JMenuItem ignore = new JMenuItem("Ignore");
		JMenuItem cancel = new JMenuItem("Cancel");
		
		public PopupMenu(int type)
		{
			newGame.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					newGame();
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
			
			if(type == 0 /*Friend*/)
			{
				add(newGame);
				add(details);
				add(delete);
			}
			else if(type == 1 /*Request*/)
			{
				add(accept);
				add(ignore);
			}
			else if(type == 2 /*Requested*/)
			{
				add(cancel);
			}
		}
	}
}
