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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.aidancbrady.vocab.Game;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.tex.Texture;

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
	
	public DefComponent def0;
	public DefComponent def1;
	public DefComponent def2;
	public DefComponent def3;
	
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
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				
			}

			@Override
			public void mousePressed(MouseEvent e) 
			{
				
			}

			@Override
			public void mouseReleased(MouseEvent e) 
			{
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
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
		
		opponentLabel = new JLabel((game.isRequest ? game.getRequestOpponent() : game.opponent) + ": " + game.getOpponentScore());
		opponentLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		opponentLabel.setVisible(true);
		opponentLabel.setSize(200, 40);
		opponentLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 16);
		add(opponentLabel);
		
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
	}
	
	public void updateGame()
	{
		userLabel.setText(VocabCrack.instance().account.username + ": " + game.getUserScore());
		
		opponentLabel.setText((game.isRequest ? game.getRequestOpponent() : game.opponent) + ": " + game.getOpponentScore());
		opponentLabel.setLocation(496-Utilities.getLabelWidth(opponentLabel), 16);
		
		if(wordIndex == -1)
		{
			updateDefs(false);
			
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
			updateDefs(true);
			initDefs(rand.nextInt(4));
			
			getContentPane().setBackground(Color.LIGHT_GRAY);
			
			answerLabel.setVisible(false);
			statusLabel.setVisible(false);
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
	
	public class DefComponent extends JComponent implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		
		public int index;
		
		public Texture texture;
		public Texture highlight = Texture.load("highlight.png");
		
		public JLabel defLabel;
		
		public boolean isHovering;
		public boolean correct;
		
		public DefComponent(int i)
		{
			index = i;
			texture = Texture.load("def" + i + ".png");
			
			defLabel = new JLabel();
			defLabel.setSize(300, 40);
			defLabel.setLocation(4, 4);
			defLabel.setForeground(Color.WHITE);
			defLabel.setVisible(true);
			
			addMouseListener(this);
			setVisible(false);
		}
		
		@Override
		public void setSize(int width, int height)
		{
			super.setSize(width+8, height+8);
		}
		
		public void setText(String text)
		{
			defLabel.setText(text);
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
			if(correct)
			{
				System.out.println("CORRECT");
			}
			else {
				System.out.println("INCORRECT");
			}
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
		}
	}
}
