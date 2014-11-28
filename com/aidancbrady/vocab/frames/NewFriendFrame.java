package com.aidancbrady.vocab.frames;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.vocab.net.FriendHandler;

public class NewFriendFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public JTextField searchField;
	
	public JList<String> friendsList;
	
	public JProgressBar progressBar;
	
	public JButton closeButton;
	public JButton searchButton;
	public JButton addButton;
	
	public Vector<String> displayedList = new Vector<String>();

	public NewFriendFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("New Friend");
		setSize(300, 500);
		setLayout(null);
		setAlwaysOnTop(true);
		
		JLabel titleLabel = new JLabel("New Friend");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		titleLabel.setSize(300, 50);
		titleLabel.setLocation(112, 6);
		add(titleLabel);
		
		searchField = new JTextField();
		searchField.setFocusable(true);
		searchField.setText("");
		searchField.setSize(new Dimension(220, 28));
		searchField.setLocation(2, 70);
		searchField.addActionListener(new SearchListener());
		add(searchField);
		
		searchButton = new JButton("Search");
		searchButton.setSize(80, 30);
		searchButton.setLocation(220, 69);
		searchButton.addActionListener(new SearchListener());
		add(searchButton);
		
		friendsList = new JList<String>();
		friendsList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					sendRequest();
				}
			}
		});
		
		friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendsList.setBorder(new TitledBorder(new EtchedBorder(), "Friends"));
		friendsList.setVisible(true);
		friendsList.setFocusable(true);
		friendsList.setEnabled(true);
		friendsList.setSelectionInterval(1, 1);
		friendsList.setToolTipText("Users that match your search query");
		JScrollPane scroll = new JScrollPane(friendsList);
		scroll.setSize(new Dimension(300, 250));
		scroll.setLocation(0, 100);
		add(scroll);
		
		addButton = new JButton("Add Friend");
		addButton.setSize(150, 30);
		addButton.setLocation(2, 356);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				sendRequest();
			}
		});
		add(addButton);
		
		closeButton = new JButton("Close");
		closeButton.setSize(150, 30);
		closeButton.setLocation(148, 356);
		closeButton.addActionListener(new CloseListener());
		add(closeButton);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setSize(180, 40);
		progressBar.setLocation(60, 400);
		progressBar.setVisible(false);
		add(progressBar);
		
		setVisible(true);
		setResizable(false);
	}
	
	public void setLoading(boolean loading)
	{
		progressBar.setVisible(loading);
		searchButton.setEnabled(!loading);
	}
	
	public class SearchListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			FriendHandler.updateSearch(NewFriendFrame.this);
		}
	}
	
	public void sendRequest()
	{
		if(!friendsList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			String name = displayedList.get(friendsList.getSelectedIndex());
			
			if(JOptionPane.showConfirmDialog(NewFriendFrame.this, "Are you sure you want to send a friend request to " + name + "?", "Confirm Request", JOptionPane.YES_NO_OPTION) == 0)
			{
				FriendHandler.sendRequest(name, this);
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		if(!visible)
		{
			searchField.setText("");
			displayedList.clear();
			friendsList.setListData(displayedList);
		}
	}
	
	public class CloseListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			setVisible(false);
			frame.toFront();
		}
	}
}
