package com.aidancbrady.vocab.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Game.GameType;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.file.WordDataHandler;
import com.aidancbrady.vocab.file.WordListHandler;
import com.aidancbrady.vocab.frames.VocabFrame;
import com.aidancbrady.vocab.tex.Texture;

public class NewGamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public VocabFrame frame;
	
	/** true = FriendsPanel, false = GamesPanel */
	public boolean reference;
	
	public GameType selectedType;
	public String listIdentifier = null;
	public Vector<String> displayedList = new Vector<String>();
	public String opponent = "Guest";
	
	public JButton loadButton;
	public JButton refreshButton;
	public JButton continueButton;
	public JButton backButton;
	
	public JLabel titleLabel;
	public JLabel typeLabel;
	public JLabel listLabel;
	
	public JToggleButton singleButton;
	public JToggleButton threeButton;
	public JToggleButton fiveButton;
	
	public JList<String> wordListsList;
	
	public NewGamePanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
		
		titleLabel = new JLabel("New Game");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		titleLabel.setVisible(true);
		titleLabel.setSize(200, 40);
		titleLabel.setLocation(200-(int)((float)Utilities.getLabelWidth(titleLabel)/2F), 10);
		add(titleLabel);
		
		typeLabel = new JLabel("Select a game type");
		typeLabel.setVisible(true);
		typeLabel.setSize(200, 40);
		typeLabel.setLocation(200-(int)((float)Utilities.getLabelWidth(typeLabel)/2F), 110);
		add(typeLabel);
		
		listLabel = new JLabel("Word list: " + (listIdentifier != null ? listIdentifier : "none"));
		listLabel.setVisible(true);
		listLabel.setSize(200, 40);
		listLabel.setLocation(200-(int)((float)Utilities.getLabelWidth(listLabel)/2F), 370);
		add(listLabel);
		
		backButton = new JButton("Back");
		backButton.setSize(60, 30);
		backButton.setLocation(16, 16);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(reference)
				{
					frame.openFriends();
				}
				else {
					frame.openGames();
				}
			}
		});
		add(backButton);
		
		loadButton = new JButton("Load");
		loadButton.setSize(110, 30);
		loadButton.setLocation(93, 340);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				loadList(true);
			}
		});
		add(loadButton);
		
		refreshButton = new JButton("Refresh");
		refreshButton.setSize(110, 30);
		refreshButton.setLocation(197, 340);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				refreshList();
			}
		});
		add(refreshButton);
		
		wordListsList = new JList<String>();
		wordListsList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event))
				{
					loadList(true);
				}
			}
		});
		wordListsList.addMouseListener(new PopupListener());
		
		wordListsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wordListsList.setBorder(new TitledBorder(new EtchedBorder(), "Word Lists"));
		wordListsList.setVisible(true);
		wordListsList.setFocusable(true);
		wordListsList.setEnabled(true);
		wordListsList.setToolTipText("Add new word lists by adding them to your 'Word Lists' folder");
		wordListsList.setSelectionInterval(1, 1);
		JScrollPane scroll = new JScrollPane(wordListsList);
		scroll.setSize(new Dimension(200, 160));
		scroll.setLocation(100, 180);
		add(scroll);
		
		continueButton = new JButton("Continue");
		continueButton.setSize(300, 60);
		continueButton.setLocation(50, 440);
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(selectedType != null)
				{
					if(!VocabCrack.instance().loadedList.isEmpty())
					{
						Game g = new Game(VocabCrack.instance().account.username, opponent, true);
						g.setGameType(selectedType);
						g.activeWords = WordDataHandler.createWordSet();
						g.listIdentifier = listIdentifier;
						
						frame.openGame(g);
					}
					else {
						JOptionPane.showMessageDialog(NewGamePanel.this, "No word list loaded.");
					}
				}
				else {
					JOptionPane.showMessageDialog(NewGamePanel.this, "Please select a game type.");
				}
			}
		});
		add(continueButton);
		
		singleButton = new JToggleButton("Single");
		singleButton.setSize(120, 50);
		singleButton.setLocation(20, 60);
		singleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(singleButton.getModel().isSelected())
				{
					threeButton.setSelected(false);
					fiveButton.setSelected(false);
					
					selectedType = GameType.SINGLE;
				}
				else {
					selectedType = null;
				}
				
				updateInfo();
			}
		});
		add(singleButton);
		
		threeButton = new JToggleButton("Best of 3");
		threeButton.setSize(120, 50);
		threeButton.setLocation(140, 60);
		threeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(threeButton.getModel().isSelected())
				{
					singleButton.setSelected(false);
					fiveButton.setSelected(false);
					
					selectedType = GameType.BEST_OF_3;
				}
				else {
					selectedType = null;
				}
				
				updateInfo();
			}
		});
		add(threeButton);
		
		fiveButton = new JToggleButton("Best of 5");
		fiveButton.setSize(120, 50);
		fiveButton.setLocation(260, 60);
		fiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(fiveButton.getModel().isSelected())
				{
					singleButton.setSelected(false);
					threeButton.setSelected(false);
					
					selectedType = GameType.BEST_OF_5;
				}
				else {
					selectedType = null;
				}
				
				updateInfo();
			}
		});
		add(fiveButton);
		
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
	
	public void updateInfo()
	{
		titleLabel.setText("Game with " + opponent);
		titleLabel.setLocation(200-(int)((float)Utilities.getLabelWidth(titleLabel)/2F), 10);
		
		if(selectedType != null)
		{
			typeLabel.setText("Game type: " + selectedType.getDescription());
		}
		else {
			typeLabel.setText("Select a game type");
		}
		
		typeLabel.setLocation(200-(int)((float)Utilities.getLabelWidth(typeLabel)/2F), 110);
		
		listLabel.setText("Word list: " + (listIdentifier != null ? listIdentifier : "none"));
		listLabel.setLocation(200-(int)((float)Utilities.getLabelWidth(listLabel)/2F), 370);
		
		wordListsList.setListData(displayedList);
	}
	
	public void initInfo(String acct, JPanel panel)
	{
		opponent = acct;
		reference = panel == frame.friends ? true : false;
		
		updateInfo();
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		if(visible)
		{
			displayedList = WordListHandler.listIdentifiers();
			loadList(false);
		}
		else {
			selectedType = null;
			listIdentifier = null;
			displayedList.clear();
			opponent = "Guest";
			
			singleButton.setSelected(false);
			threeButton.setSelected(false);
			fiveButton.setSelected(false);
		}
		
		updateInfo();
	}
	
	public void loadList(boolean dialog)
	{
		if(!wordListsList.isSelectionEmpty() && !displayedList.isEmpty())
		{
			String id = displayedList.get(wordListsList.getSelectedIndex());
			
			if(listIdentifier == null || !listIdentifier.equals(id))
			{
				if(WordListHandler.loadWordList(id, NewGamePanel.this))
				{
					listIdentifier = id;
					
					if(dialog)
					{
						JOptionPane.showMessageDialog(NewGamePanel.this, "Loaded '" + id + "' word list");
						updateInfo();
					}
				}
			}
		}
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
			
			int row = wordListsList.locationToIndex(e.getPoint());
			wordListsList.setSelectedIndex(row);
			
			if(row >= 0)
			{
				PopupMenu menu = new PopupMenu();
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	public void refreshList()
	{
		displayedList = WordListHandler.listIdentifiers();
		updateInfo();
	}
	
	public class PopupMenu extends JPopupMenu
	{
		private static final long serialVersionUID = 1L;
		
		JMenuItem load = new JMenuItem("Load");
		JMenuItem delete = new JMenuItem("Delete");
		
		public PopupMenu()
		{
			load.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					loadList(true);
				}
			});
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if(!wordListsList.isSelectionEmpty() && !displayedList.isEmpty())
					{
						String id = displayedList.get(wordListsList.getSelectedIndex());
						
						if(id.equals(listIdentifier))
						{
							JOptionPane.showMessageDialog(NewGamePanel.this, "Can't delete the loaded word list, load another first.");
							return;
						}
						else if(id.equals("Default"))
						{
							JOptionPane.showMessageDialog(NewGamePanel.this, "Can't delete the 'Default' word list.");
							return;
						}
						
						WordListHandler.deleteList(id);
						refreshList();
						JOptionPane.showMessageDialog(NewGamePanel.this, "Successfully deleted word list '" + id + ".'");
					}
				}
			});
			
			add(load);
			add(delete);
		}
	}
}
