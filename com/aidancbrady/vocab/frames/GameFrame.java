package com.aidancbrady.vocab.frames;

import javax.swing.JFrame;

import com.aidancbrady.vocab.Game;

public class GameFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public Game game = Game.DEFAULT;
	
	public GameFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		setSize(280, 400);
		setLayout(null);
		setAlwaysOnTop(true);
	}
	
	public void initGame(Game g)
	{
		game = g;
		
		setTitle("Game with " + (game.isRequest ? game.getRequestOpponent() : game.opponent));
		
		setVisible(true);
	}
}
