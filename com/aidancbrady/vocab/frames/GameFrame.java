package com.aidancbrady.vocab.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;

public class GameFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public Game game = Game.DEFAULT;
	
	public int wordIndex = -1;
	public boolean activeWord;
	
	public Timer timer = new Timer();
	
	public float transparency;
	
	public JLabel answerLabel;
	public JLabel statusLabel;
	
	public JLabel userLabel;
	public JLabel opponentLabel;
	
	public GameFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		setSize(512, 512);
		setLayout(null);
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyCode() == 32)
				{
					onSpace();
				}
			}
		});
		
		answerLabel = new JLabel();
		answerLabel.setFont(new Font("Helvetica", Font.BOLD, 28));
		answerLabel.setVisible(false);
		answerLabel.setSize(200, 40);
		answerLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(answerLabel)/2F), 180);
		add(answerLabel);
		
		statusLabel = new JLabel();
		statusLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		statusLabel.setVisible(false);
		statusLabel.setSize(200, 40);
		statusLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(statusLabel)/2F), 240);
		add(statusLabel);
		
		userLabel = new JLabel(VocabCrack.instance().account.username + ": " + game.getUserScore());
		userLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		userLabel.setVisible(true);
		userLabel.setSize(200, 40);
		userLabel.setLocation(16, 16);
		add(userLabel);
		
		opponentLabel = new JLabel(game.isRequest ? game.getRequestOpponent() : game.opponent + ": " + game.getOpponentScore());
		opponentLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		opponentLabel.setVisible(true);
		opponentLabel.setSize(200, 40);
		opponentLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 16);
		add(opponentLabel);
	}
	
	public void updateLabels()
	{
		userLabel.setText(VocabCrack.instance().account.username + ": " + game.getUserScore());
		
		opponentLabel.setText(game.isRequest ? game.getRequestOpponent() : game.opponent + ": " + game.getOpponentScore());
		opponentLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 16);
		
		if(wordIndex == -1)
		{
			answerLabel.setText("Round " + (game.userPoints.size()+1));
			answerLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(answerLabel)/2F), 180);
			answerLabel.setVisible(true);
			
			statusLabel.setText("Press spacebar to start");
			statusLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(statusLabel)/2F), 240);
			statusLabel.setForeground(new Color(0, 0, 0, 0.5F));
			statusLabel.setVisible(true);
		}
	}
	
	public void onSpace()
	{
		
	}
	
	public void exitGame()
	{
		game = Game.DEFAULT;
		wordIndex = -1;
		activeWord = false;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		frame.openMenu();
		
		setVisible(false);
	}
	
	public void initGame(Game g)
	{
		game = g;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		
		setVisible(true);
		
		new TimerThread().start();
		
		updateLabels();
	}
	
	public class TimerThread extends Thread
	{
		@Override
		public void run()
		{
			while(isVisible())
			{
				transparency += 0.005;
				float trans = ((float)Math.sin(transparency%Math.PI))*(0.75F);
				
				statusLabel.setForeground(new Color(0, 0, 0, 0.25F + trans));
				
				try {
					Thread.sleep(1);
				} catch(Exception e) {}
			}
		}
	}
}
