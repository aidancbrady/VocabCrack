package com.aidancbrady.vocab.frames;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.file.WordListHandler;
import com.aidancbrady.vocab.tex.AvatarHandler;

public class GameDetailsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public AvatarPanel avatar = new AvatarPanel();
	
	public Game game = Game.DEFAULT;
	
	public JButton playButton;
	public JButton closeButton;
	
	public JLabel namesLabel;
	public JLabel scoreLabel;
	
	public JList<String> scoreList;
	
	public GameDetailsFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("User Details");
		setSize(280, 400);
		setLayout(null);
		setAlwaysOnTop(true);
		
		namesLabel = new JLabel(game.user + " vs " + game.opponent);
		namesLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
		namesLabel.setVisible(true);
		namesLabel.setSize(280, 40);
		namesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(namesLabel)/2F), 120);
		add(namesLabel);
		
		scoreLabel = new JLabel(game.getUserScore() + " - " + game.getOpponentScore());
		scoreLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		scoreLabel.setVisible(true);
		scoreLabel.setSize(200, 40);
		scoreLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(scoreLabel)/2F), 160);
		add(scoreLabel);
		
		scoreList = new JList<String>();
		scoreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scoreList.setVisible(true);
		scoreList.setFocusable(true);
		scoreList.setEnabled(true);
		scoreList.setSelectionInterval(1, 1);
		((DefaultListCellRenderer)scoreList.getCellRenderer()).setHorizontalAlignment(JLabel.CENTER);
		JScrollPane scroll = new JScrollPane(scoreList);
		scroll.setSize(new Dimension(80, 120));
		scroll.setLocation(100, 200);
		add(scroll);
		
		playButton = new JButton("Play");
		playButton.setSize(120, 30);
		playButton.setLocation(8, 340);
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(game.userTurn)
				{
					if(WordListHandler.loadList(game.listURL, GameDetailsFrame.this))
					{
						frame.openGame(game);
						setVisible(false);
					}
					else {
						JOptionPane.showMessageDialog(GameDetailsFrame.this, "Unable to download word list.");
					}
				}
				else {
					JOptionPane.showMessageDialog(GameDetailsFrame.this, "It is not your turn!");
				}
			}
		});
		add(playButton);
		
		closeButton = new JButton("Close");
		closeButton.setSize(120, 30);
		closeButton.setLocation(152, 340);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				setVisible(false);
				frame.toFront();
			}
		});
		add(closeButton);
		
		add(avatar = new AvatarPanel());
		
		setVisible(true);
		setResizable(false);
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		if(!visible)
		{
			game = Game.DEFAULT;
			setAccountData();
		}
	}
	
	public void updateScoreList()
	{
		int maxLength = Math.max(game.userPoints.size(), game.opponentPoints.size());
		
		Vector<String> vec = new Vector<String>();
		
		for(int i = 0; i < maxLength; i++)
		{
			String userScore = i <= game.userPoints.size()-1 ? game.userPoints.get(i).toString() : "N/A";
			String opponentScore = i <= game.opponentPoints.size()-1 ? game.opponentPoints.get(i).toString() : "N/A";
			
			vec.add(userScore + " to " + opponentScore);
		}
		
		scoreList.setListData(vec);
	}
	
	public void open(Game g)
	{
		game = g;
		
		setAccountData();
		
		setVisible(true);
	}
	
	public void setAccountData()
	{		
		avatar.repaint();
		
		namesLabel.setText(game.user + " vs " + game.opponent);
		namesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(namesLabel)/2F), 120);
		
		scoreLabel.setText(game.getUserScore() + " - " + game.getOpponentScore());
		scoreLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(scoreLabel)/2F), 160);
		
		updateScoreList();
		
		repaint();
	}
	
	public class AvatarPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		public AvatarPanel()
		{
			setSize(280, 400);
			setVisible(true);
			setLayout(null);
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			try {
				AvatarHandler.downloadAvatar(VocabCrack.instance().account).draw(g, 30, 26, 80, 80);
			} catch(Exception e) {}
			
			try {
				AvatarHandler.downloadAvatar(game.opponentEmail).draw(g, 170, 26, 80, 80);
			} catch(Exception e) {}
		}
	}
}
