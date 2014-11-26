package com.aidancbrady.vocab.frames;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.aidancbrady.vocab.Account;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.net.FriendHandler;
import com.aidancbrady.vocab.net.GameHandler;
import com.aidancbrady.vocab.tex.AvatarHandler;

public class UserDetailsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public AvatarPanel avatar = new AvatarPanel();
	
	public Account acct = Account.DEFAULT;
	
	public JButton newGameButton;
	public JButton closeButton;
	
	public JLabel usernameLabel;
	
	public JLabel winsLabel;
	public JLabel lossesLabel;
	
	public UserDetailsFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("User Details");
		setSize(280, 400);
		setLayout(null);
		setAlwaysOnTop(true);
		
		usernameLabel = new JLabel(acct.username);
		usernameLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		usernameLabel.setVisible(true);
		usernameLabel.setSize(200, 40);
		usernameLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(usernameLabel)/2F), 200);
		add(usernameLabel);
		
		winsLabel = new JLabel("Games Won: " + acct.gamesWon);
		winsLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		winsLabel.setVisible(true);
		winsLabel.setSize(200, 40);
		winsLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(winsLabel)/2F), 240);
		add(winsLabel);
		
		lossesLabel = new JLabel("Games Lost: " + acct.gamesLost);
		lossesLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		lossesLabel.setVisible(true);
		lossesLabel.setSize(200, 40);
		lossesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(lossesLabel)/2F), 270);
		add(lossesLabel);
		
		newGameButton = new JButton("New Game");
		newGameButton.setSize(120, 30);
		newGameButton.setLocation(8, 340);
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(JOptionPane.showConfirmDialog(UserDetailsFrame.this, "Start a game with " + acct.username + "?", "Confirm Game", JOptionPane.YES_NO_OPTION) == 0)
				{
					if(GameHandler.confirmGame(acct.username, frame.friends))
					{
						setVisible(false);
						//New game
					}
					else {
						JOptionPane.showMessageDialog(frame.friends, "Unable to authenticate");
					}
				}

			}
		});
		add(newGameButton);
		
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
			acct = Account.DEFAULT;
			setAccountData();
		}
	}
	
	public void open(String username)
	{
		FriendHandler.getInfo(username, this);
		
		setAccountData();
		
		setVisible(true);
	}
	
	public void setAccountData()
	{		
		avatar.repaint();
		
		usernameLabel.setText(acct.username);
		usernameLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(usernameLabel)/2F), 200);
		
		winsLabel = new JLabel("Games Won: " + acct.gamesWon);
		winsLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(winsLabel)/2F), 200);
		
		lossesLabel = new JLabel("Games Lost: " + acct.gamesLost);
		lossesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(lossesLabel)/2F), 200);
		
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
				AvatarHandler.downloadAvatar(acct).draw(g, 76, 56, 128, 128);
			} catch(Exception e) {}
		}
	}
}
