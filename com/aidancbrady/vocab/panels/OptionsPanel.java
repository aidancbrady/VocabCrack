package com.aidancbrady.vocab.panels;

import javax.swing.JPanel;

import com.aidancbrady.vocab.frames.VocabFrame;

public class OptionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public OptionsPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
	}
	
	public void updateProgress(int bytes)
	{
		
	}
}
