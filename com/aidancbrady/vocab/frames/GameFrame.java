package com.aidancbrady.vocab.frames;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.aidancbrady.vocab.Game;

public class GameFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public Game game = Game.DEFAULT;
	
	public Timer timer = new Timer();
	
	public JLabel statusLabel;
	
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
	}
	
	public void onSpace()
	{
		
	}
	
	public void exitGame()
	{
		game = Game.DEFAULT;
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		frame.openMenu();
		
		setVisible(false);
	}
	
	public void initGame(Game g)
	{
		game = g;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		
		setVisible(true);
	}
}
