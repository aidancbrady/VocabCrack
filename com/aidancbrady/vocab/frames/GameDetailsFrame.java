package com.aidancbrady.vocab.frames;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.net.GameHandler;
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
	
	public GameDetailsFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("User Details");
		setSize(280, 400);
		setLayout(null);
		setAlwaysOnTop(true);
		
		namesLabel = new JLabel(game.user + " vs " + game.opponent);
		namesLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		namesLabel.setVisible(true);
		namesLabel.setSize(200, 40);
		namesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(namesLabel)/2F), 200);
		add(namesLabel);
		
		scoreLabel = new JLabel(game.getUserScore() + " - " + game.getOpponentScore());
		scoreLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		scoreLabel.setVisible(true);
		scoreLabel.setSize(200, 40);
		scoreLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(scoreLabel)/2F), 240);
		add(scoreLabel);
		
		playButton = new JButton("Play");
		playButton.setSize(120, 30);
		playButton.setLocation(8, 340);
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{

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
	
	public void open(String username)
	{
		GameHandler.getInfo(username, this);
		
		setAccountData();
		
		setVisible(true);
	}
	
	public void setAccountData()
	{		
		avatar.repaint();
		
		namesLabel = new JLabel(game.user + " vs " + game.opponent);
		namesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(namesLabel)/2F), 200);
		
		scoreLabel = new JLabel(game.getUserScore() + " - " + game.getOpponentScore());
		scoreLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(scoreLabel)/2F), 200);
		
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
				AvatarHandler.downloadAvatar(VocabCrack.instance().account).draw(g, 30, 56, 80, 80);
			} catch(Exception e) {}
			
			try {
				AvatarHandler.downloadAvatar(VocabCrack.instance().account).draw(g, 180, 56, 80, 80);
			} catch(Exception e) {}
		}
	}
}
