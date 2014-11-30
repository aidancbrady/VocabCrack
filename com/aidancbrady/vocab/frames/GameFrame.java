package com.aidancbrady.vocab.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Game.GameType;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.file.WordDataHandler;
import com.aidancbrady.vocab.file.WordListHandler;
import com.aidancbrady.vocab.net.GameHandler;
import com.aidancbrady.vocab.tex.Texture;

public class GameFrame extends JDialog implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private static Random rand = new Random();
	
	private static final Texture wordTexture = Texture.load("resources" + File.separator + "texture" + File.separator + "word.png");
	
	public TimerThread timer;
	
	public VocabFrame frame;
	
	public Game game = Game.DEFAULT;
	
	public int amountCorrect = 0;
	
	public int time = 30;
	
	public int wordIndex = -1;
	public boolean activeWord;
	public boolean correct;
	public boolean complete;
	
	public float transparency;
	
	public JLabel answerLabel;
	public JLabel statusLabel;
	
	public JLabel userLabel;
	public JLabel opponentLabel;
	public JLabel typeLabel;
	
	public JLabel remainingLabel;
	public JLabel timerLabel;
	
	public JLabel wordLabel;
	
	public DefComponent def0;
	public DefComponent def1;
	public DefComponent def2;
	public DefComponent def3;
	
	public WordComponent wordComp;
	
	public GameFrame(VocabFrame f)
	{
		super(f);
		
		frame = f;
		
		setTitle("Game with " + Utilities.getRemoteUser(game));
		setSize(512, 580);
		setLayout(null);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setResizable(false);
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
		
		opponentLabel = new JLabel(Utilities.getRemoteUser(game) + ": " + game.getOpponentScore());
		opponentLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		opponentLabel.setVisible(true);
		opponentLabel.setSize(200, 40);
		opponentLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 16);
		add(opponentLabel);
		
		typeLabel = new JLabel();
		typeLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		typeLabel.setVisible(true);
		typeLabel.setSize(300, 40);
		typeLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(typeLabel)/2F), 6);
		add(typeLabel);
		
		remainingLabel = new JLabel("Remaining: 9 (0 correct)");
		remainingLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		remainingLabel.setVisible(false);
		remainingLabel.setSize(300, 40);
		remainingLabel.setLocation(16, 512);
		add(remainingLabel);
		
		timerLabel = new JLabel("Timer: 30s");
		timerLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
		timerLabel.setVisible(false);
		timerLabel.setSize(200, 40);
		timerLabel.setLocation(496-Utilities.getLabelWidth(timerLabel), 512);
		add(timerLabel);
		
		def0 = new DefComponent(0);
		def0.setSize(384, 48);
		def0.setLocation(60, 160);
		add(def0);
		
		def1 = new DefComponent(1);
		def1.setSize(384, 48);
		def1.setLocation(60, 240);
		add(def1);
		
		def2 = new DefComponent(2);
		def2.setSize(384, 48);
		def2.setLocation(60, 320);
		add(def2);
		
		def3 = new DefComponent(3);
		def3.setSize(384, 48);
		def3.setLocation(60, 400);
		add(def3);
		
		wordComp = new WordComponent();
		wordComp.setSize(200, 100);
		wordComp.setLocation(156, 48);
		add(wordComp);
		
		wordLabel = new JLabel();
		wordLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		wordLabel.setVisible(false);
		wordLabel.setSize(512, 40);
		wordLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(wordLabel)/2F), 340);
		add(wordLabel);
	}
	
	public void updateGame()
	{
		userLabel.setText(VocabCrack.instance().account.username + ": " + game.getUserScore());
		
		opponentLabel.setText(Utilities.getRemoteUser(game) + ": " + game.getOpponentScore());
		opponentLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 16);
	
		typeLabel.setText(GameType.values()[game.gameType].getDescription());
		typeLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(typeLabel)/2F), 6);
		
		if(wordIndex == -1)
		{
			updateDefs(false);
			wordComp.setVisible(false);
			
			remainingLabel.setVisible(false);
			timerLabel.setVisible(false);
			
			wordLabel.setVisible(false);
			
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
			updateDefs(false);
			wordComp.setVisible(false);
			
			remainingLabel.setVisible(false);
			timerLabel.setVisible(false);
			
			answerLabel.setText(correct ? "Correct" : "Incorrect");
			answerLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(answerLabel)/2F), 180);
			answerLabel.setVisible(true);
			
			statusLabel.setText("Press spacebar to continue");
			statusLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(statusLabel)/2F), 240);
			statusLabel.setVisible(true);
			
			String[] split = game.activeWords.get(wordIndex-1).split(WordListHandler.splitter.toString());
			
			wordLabel.setText(split[0] + ": " + split[1]);
			wordLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(wordLabel)/2F), 340);
			wordLabel.setVisible(true);
			
			if(correct)
			{
				getContentPane().setBackground(Color.GREEN);
			}
			else {
				getContentPane().setBackground(Color.RED);
			}
		}
		else {
			if(wordIndex < 10)
			{
				updateDefs(true);
				initDefs(rand.nextInt(4));
				wordComp.setVisible(true);
				wordComp.setWord(getCurrentWord().split(WordListHandler.splitter.toString())[0]);
				
				wordLabel.setVisible(false);
				
				(timer = new TimerThread()).start();
				
				remainingLabel.setVisible(true);
				
				timerLabel.setVisible(true);
				timerLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 512);
				
				answerLabel.setVisible(false);
				statusLabel.setVisible(false);
				
				getContentPane().setBackground(Color.LIGHT_GRAY);
			}
			else {
				wordLabel.setVisible(false);
				
				game.userPoints.add(amountCorrect);
				
				answerLabel.setText("Your score: " + amountCorrect + "/10");
				answerLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(answerLabel)/2F), 180);
				answerLabel.setVisible(true);
				
				statusLabel.setText("Press spacebar to exit");
				statusLabel.setLocation(256-(int)((float)Utilities.getLabelWidth(statusLabel)/2F), 240);
				statusLabel.setVisible(true);
				
				getContentPane().setBackground(Color.LIGHT_GRAY);
				
				if(game.isRequest)
				{
					GameHandler.newGame(this);
				}
				else {
					GameHandler.compGame(this);
				}
			}
		}
	}
	
	public void updateDefs(boolean visible)
	{
		def0.setVisible(visible);
		def1.setVisible(visible);
		def2.setVisible(visible);
		def3.setVisible(visible);
	}
	
	public void initDefs(int correct)
	{
		List<String> defs = createDefinitions(correct);
		
		def0.setCorrect(correct == 0);
		def0.setText(defs.get(0));
		
		def1.setCorrect(correct == 1);
		def1.setText(defs.get(1));
		
		def2.setCorrect(correct == 2);
		def2.setText(defs.get(2));
		
		def3.setCorrect(correct == 3);
		def3.setText(defs.get(3));
	}
	
	public void onSpace()
	{
		if(wordIndex == -1)
		{
			wordIndex = 0;
			activeWord = true;
			
			updateGame();
		}
		else if(wordIndex > 0 && !activeWord)
		{
			activeWord = true;
			
			updateGame();
		}
		else if(wordIndex == 10 && activeWord)
		{			
			if(game.hasWinner())
			{
				String score = null;
				
				if(game.getWinner().equals(game.user))
				{
					score = game.getUserScore() + " to " + game.getOpponentScore();
					
					if(game.gameType == GameType.SINGLE.ordinal())
					{
						score = game.userPoints.get(0) + " to " + game.opponentPoints.get(0);
					}
					
					JOptionPane.showMessageDialog(this, "You beat " + game.opponent + " " + score + "!");
				}
				else {
					score = game.getOpponentScore() + " to " + game.getUserScore();
					
					if(game.gameType == GameType.SINGLE.ordinal())
					{
						score = game.opponentPoints.get(0) + " to " + game.userPoints.get(0);
					}
					
					JOptionPane.showMessageDialog(this, game.opponent + " beat you " + score + "!");
				}
			}
			else {
				boolean roundOver = game.userPoints.size() == game.opponentPoints.size();
				int index = game.userPoints.size()-1;
				String score = game.getUserScore() + " to " + game.getOpponentScore();
				
				if(roundOver)
				{
					String points = game.userPoints.get(index) + " to " + game.opponentPoints.get(index);
					
					if(game.userPoints.get(index) > game.opponentPoints.get(index))
					{
						JOptionPane.showMessageDialog(this, "You won the round " + points + "! The game record is now " + score + ".");
					}
					else if(game.userPoints.get(index) == game.opponentPoints.get(index))
					{
						JOptionPane.showMessageDialog(this, "You tied the round " + points + "! The game record is now " + score + ".");
					}
					else {
						JOptionPane.showMessageDialog(this, "You lost the round " + points + "! The game record is now " + score + ".");
					}
				}
				else {
					JOptionPane.showMessageDialog(this, "You completed the round with " + game.userPoints.get(index) + " points! The game record is now " + score + ".");
				}
			}
			
			exitGame();
		}
	}
	
	public void onAnswer(boolean b)
	{
		correct = b;
		activeWord = false;
		wordIndex++;
		time = 30;
		timerLabel.setText("Timer: 30s");
		timerLabel.setLocation(496-Utilities.getLabelWidth(timerLabel), 512);
		
		if(correct)
		{
			amountCorrect++;
			
			String prev = game.activeWords.get(wordIndex-1).split(WordListHandler.splitter.toString())[0];
			VocabCrack.instance().learnedWords.add(prev);
			WordDataHandler.save();
		}
		
		remainingLabel.setText("Remaining: " + (10-(wordIndex+1)) + " (" + amountCorrect + " correct)");
		
		if(wordIndex == 10)
		{
			remainingLabel.setText("Remaining: 9 (0 correct)");
			complete = true;
		}
		
		if(timer != null)
		{
			try {
				timer.interrupt();
			} catch(Exception e) {}
		}
		
		updateGame();
	}
	
	public void exitGame()
	{
		game = Game.DEFAULT;
		wordIndex = -1;
		activeWord = false;
		complete = false;
		correct = false;
		amountCorrect = 0;
		time = 30;
		
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
		setTitle("Game with " + Utilities.getRemoteUser(game));
		
		if(timer != null)
		{
			try {
				timer.interrupt();
			} catch(Exception e) {}
		}
		
		updateGame();
		
		setVisible(false);
		frame.openGames();
	}
	
	public void initGame(Game g)
	{
		game = g;
		
		setTitle("Game with " + Utilities.getRemoteUser(game));
		
		if(game.userPoints.size() == game.opponentPoints.size())
		{
			game.activeWords = WordDataHandler.createWordSet();
		}
		
		setVisible(true);
		
		new VisualsThread().start();
		
		updateGame();
	}
	
	public List<String> createDefinitions(int correct)
	{
		List<String> definitions = new ArrayList<String>();
		
		while(definitions.size() < 4)
		{
			String def = VocabCrack.instance().loadedList.get(rand.nextInt(VocabCrack.instance().loadedList.size())).split(WordListHandler.splitter.toString())[1];
			
			if(!def.equals(getCurrentWord().split(WordListHandler.splitter.toString())[1]))
			{
				definitions.add(def);
			}
		}
		
		definitions.set(correct, getCurrentWord().split(WordListHandler.splitter.toString())[1]);
		
		return definitions;
	}
	
	public String getCurrentWord()
	{
		return game.activeWords.get(wordIndex);
	}
	
	public class VisualsThread extends Thread
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
	
	public class TimerThread extends Thread
	{
		public boolean dead;
		
		@Override
		public void run()
		{
			while(isVisible() && !dead)
			{
				try {
					Thread.sleep(1000);
				} catch(Exception e) {}
				
				if(dead)
				{
					return;
				}
				
				time--;
				
				if(time < 0)
				{
					time = 30;
					onAnswer(false);
					timerLabel.setText("Timer: 30s");
					timerLabel.setLocation(496-Utilities.getLabelWidth(timerLabel), 512);
					
					break;
				}
				
				timerLabel.setText("Timer: " + time + "s");
				timerLabel.setLocation(496-Utilities.getLabelWidth(timerLabel), 512);
			}
		}
		
		@Override
		public void interrupt()
		{
			setDead(true);
			
			super.interrupt();
		}
		
		public void setDead(boolean d)
		{
			dead = d;
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
	
	public class DefComponent extends JComponent implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		
		public int index;
		
		public Texture texture;
		public Texture highlight = Texture.load("resources" + File.separator + "texture" + File.separator + "highlight.png");
		
		public String text;
		
		public boolean isHovering;
		public boolean correct;
		
		public DefComponent(int i)
		{
			index = i;
			texture = Texture.load("resources" + File.separator + "texture" + File.separator + "def" + i + ".png");
			
			addMouseListener(this);
			setVisible(false);
		}
		
		@Override
		public void setSize(int width, int height)
		{
			super.setSize(width+8, height+8);
		}
		
		public void setText(String s)
		{
			text = s;
		}
		
		public void setCorrect(boolean b)
		{
			correct = b;
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			onAnswer(correct);
		}

		@Override
		public void mouseEntered(MouseEvent e) 
		{
			isHovering = true;
			
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) 
		{
			isHovering = false;
			
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			if(isHovering)
			{
				highlight.draw(g, 0, 0, getWidth(), getHeight());
			}
			
			texture.draw(g, 4, 4, getWidth()-8, getHeight()-8);
			
			if(text != null)
			{
				g.setColor(Color.WHITE);
				g.setFont(new Font("Helvetica", Font.BOLD, 14));
				g.drawString(text, 12, 32);
			}
		}
	}
	
	public class WordComponent extends JComponent
	{
		private static final long serialVersionUID = 1L;
		
		public Font font = new Font("Helvetica", Font.BOLD, 24);
		public String word;
		
		public void setWord(String s)
		{
			word = s;
			
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			wordTexture.draw(g, 0, 0, getWidth(), getHeight());
			
			if(word != null)
			{
				g.setColor(Color.BLACK);
				g.setFont(font);
				g.drawString(word, (getWidth()/2)-(Utilities.getWidth(font, word)/2), 56);
			}
		}
	}
}
