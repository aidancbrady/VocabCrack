package com.aidancbrady.vocab.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;

public class GameFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private static Random rand = new Random();
	
	public VocabFrame frame;
	
	public Game game = Game.DEFAULT;
	
	public int wordIndex = -1;
	public boolean activeWord;
	public boolean correct;
	
	public float transparency;
	
	public JLabel answerLabel;
	public JLabel statusLabel;
	
	public JLabel userLabel;
	public JLabel opponentLabel;
	
	public JLabel wordLabel;
	
	public JLabel def1Label;
	public JLabel def2Label;
	public JLabel def3Label;
	public JLabel def4Label;
	
	public GameFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		setSize(512, 512);
		setLayout(null);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setBackground(Color.LIGHT_GRAY);
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
		answerLabel.setSize(300, 40);
		answerLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(answerLabel)/2F), 180);
		add(answerLabel);
		
		statusLabel = new JLabel();
		statusLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		statusLabel.setVisible(false);
		statusLabel.setSize(300, 40);
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
	
	public void updateGame()
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
			statusLabel.setVisible(true);
			
			getContentPane().setBackground(Color.LIGHT_GRAY);
		}
		else if(!activeWord)
		{
			answerLabel.setText(correct ? "Correct" : "Incorrect");
			answerLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(answerLabel)/2F), 180);
			answerLabel.setVisible(true);
			
			statusLabel.setText("Press spacebar to continue");
			statusLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(statusLabel)/2F), 240);
			statusLabel.setVisible(true);
			
			if(correct)
			{
				getContentPane().setBackground(Color.GREEN);
			}
			else {
				getContentPane().setBackground(Color.RED);
			}
		}
		else {
			getContentPane().setBackground(Color.LIGHT_GRAY);
			
			answerLabel.setVisible(false);
			statusLabel.setVisible(false);
		}
	}
	
	public void clearDefs()
	{
		def1Label.setVisible(false);
		def2Label.setVisible(false);
		def3Label.setVisible(false);
		def4Label.setVisible(false);
	}
	
	public void onSpace()
	{
		if(wordIndex == -1)
		{
			wordIndex = 1;
			activeWord = false;
			
			updateGame();
		}
		else if(wordIndex > 0 && wordIndex < 10 && !activeWord)
		{
			activeWord = true;
			
			updateGame();
		}
	}
	
	public void exitGame()
	{
		game = Game.DEFAULT;
		wordIndex = -1;
		activeWord = false;
		
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
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
		
		updateGame();
	}
	
	public List<String> createDefinitions(int correct)
	{
		List<String> definitions = new ArrayList<String>();
		
		while(definitions.size() < 4)
		{
			String def = VocabCrack.instance().loadedList.get(rand.nextInt(VocabCrack.instance().loadedList.size()));
			
			if(!def.equals(getCurrentWord().split("|")[1]))
			{
				definitions.add(def);
			}
		}
		
		definitions.set(correct, getCurrentWord().split("|")[1]);
		
		return definitions;
	}
	
	public String getCurrentWord()
	{
		return game.activeWords.get(wordIndex);
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

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(JOptionPane.showConfirmDialog(this, "Are you sure you want to exit this game? Your progress will be reset.", "Confirm Exit", JOptionPane.YES_NO_OPTION) == 0)
		{
			exitGame();
		}
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
