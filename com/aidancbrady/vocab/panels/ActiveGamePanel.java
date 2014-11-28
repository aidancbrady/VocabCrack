package com.aidancbrady.vocab.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.frames.VocabFrame;

public class ActiveGamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public JButton exitButton;
	
	public ActiveGamePanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
		
		exitButton = new JButton("Friends");
		exitButton.setSize(160, 30);
		exitButton.setLocation(40, 360);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
			}
		});
		exitButton.setEnabled(false);
		add(exitButton);
		
		JLabel version = new JLabel("v" + VocabCrack.VERSION);
		version.setFont(new Font("Helvetica", Font.BOLD, 14));
		version.setVisible(true);
		version.setSize(200, 40);
		version.setLocation(340, 520);
		add(version);
		
		JLabel copyright = new JLabel("© aidancbrady, 2014");
		copyright.setVisible(true);
		copyright.setSize(200, 40);
		copyright.setLocation(30, 520);
		add(copyright);
	}
}
