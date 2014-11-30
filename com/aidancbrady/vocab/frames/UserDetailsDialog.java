package com.aidancbrady.vocab.frames;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.aidancbrady.vocab.Account;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.net.FriendHandler;
import com.aidancbrady.vocab.net.GameHandler;
import com.aidancbrady.vocab.tex.AvatarHandler;

public class UserDetailsDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat fmt = new SimpleDateFormat("");
	
	public VocabFrame frame;
	
	public AvatarPanel avatar = new AvatarPanel();
	
	public Account acct = Account.DEFAULT;
	
	public JButton newGameButton;
	public JButton closeButton;
	
	public JLabel usernameLabel;
	
	public JLabel winsLabel;
	public JLabel lossesLabel;
	public JLabel loginLabel;
	
	public UserDetailsDialog(VocabFrame f)
	{
		super(f, "User Details");
		
		frame = f;
		
		setTitle("User Details");
		setSize(280, 400);
		setLayout(null);
		setAlwaysOnTop(true);
		
		usernameLabel = new JLabel(acct.username);
		usernameLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		usernameLabel.setVisible(true);
		usernameLabel.setSize(200, 40);
		usernameLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(usernameLabel)/2F), 184);
		add(usernameLabel);
		
		winsLabel = new JLabel("Games Won: " + acct.gamesWon);
		winsLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		winsLabel.setVisible(true);
		winsLabel.setSize(200, 40);
		winsLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(winsLabel)/2F), 224);
		add(winsLabel);
		
		lossesLabel = new JLabel("Games Lost: " + acct.gamesLost);
		lossesLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		lossesLabel.setVisible(true);
		lossesLabel.setSize(200, 40);
		lossesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(lossesLabel)/2F), 254);
		add(lossesLabel);
		
		loginLabel = new JLabel("Last Login: N/A");
		loginLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		loginLabel.setVisible(true);
		loginLabel.setSize(200, 40);
		loginLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(loginLabel)/2F), 284);
		add(loginLabel);
		
		newGameButton = new JButton("New Game");
		newGameButton.setSize(120, 30);
		newGameButton.setLocation(8, 340);
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(JOptionPane.showConfirmDialog(UserDetailsDialog.this, "Start a game with " + acct.username + "?", "Confirm Game", JOptionPane.YES_NO_OPTION) == 0)
				{
					if(GameHandler.confirmGame(acct.username, frame.friends))
					{
						frame.openNewGame(acct.username, frame.friends);
						
						setVisible(false);
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
		
		cal.setTimeInMillis(acct.lastLogin);
		
		usernameLabel.setText(acct.username);
		usernameLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(usernameLabel)/2F), 184);
		
		winsLabel.setText("Games Won: " + acct.gamesWon);
		winsLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(winsLabel)/2F), 224);
		
		lossesLabel.setText("Games Lost: " + acct.gamesLost);
		lossesLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(lossesLabel)/2F), 254);
		
		loginLabel.setText("Last Login: " + getLoginText());
		loginLabel.setLocation(140-(int)((float)Utilities.getLabelWidth(loginLabel)/2F), 284);
		
		repaint();
	}
	
	public String getLoginText()
	{
		Calendar currentCal = Calendar.getInstance();
		
		int diffYears = currentCal.get(Calendar.YEAR)-cal.get(Calendar.YEAR);
		int diffMonths = (diffYears*12) + currentCal.get(Calendar.MONTH)-cal.get(Calendar.MONTH);
		
		long diffMillis = currentCal.getTimeInMillis()-cal.getTimeInMillis();
		long diffSeconds = diffMillis/1000;
		long diffMinutes = diffSeconds/60;
		long diffHours = diffMinutes/60;
		long diffDays = diffHours/24;
		
		if(diffSeconds < 60)
		{
			return "seconds ago";
		}
		else if(diffMinutes == 1)
		{
			return "a minute ago";
		}
		else if(diffMinutes < 60)
		{
			return diffMinutes + " minutes ago";
		}
		else if(diffHours == 1)
		{
			return "an hour ago";
		}
		else if(diffHours < 72)
		{
			return diffHours + " hours ago";
		}
		else if(diffDays < 31)
		{
			return diffDays + " days ago";
		}
		else if(diffMonths < 12)
		{
			return diffMonths + " months ago";
		}
		else if(diffYears == 1)
		{
			return "a year ago";
		}
		else {
			return diffYears + " years ago";
		}
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
				AvatarHandler.downloadAvatar(acct).draw(g, 76, 40, 128, 128);
			} catch(Exception e) {}
		}
	}
}
