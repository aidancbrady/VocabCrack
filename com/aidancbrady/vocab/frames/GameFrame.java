package com.aidancbrady.vocab.frames;

import javax.swing.JFrame;

public class GameFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public GameFrame(VocabFrame f)
	{
		frame = f;
		
		setTitle("User Details");
		setSize(280, 400);
		setLayout(null);
		setAlwaysOnTop(true);
	}
}
