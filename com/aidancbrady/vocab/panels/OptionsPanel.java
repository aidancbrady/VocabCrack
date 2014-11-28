package com.aidancbrady.vocab.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.frames.VocabFrame;
import com.aidancbrady.vocab.net.PasswordHandler;

public class OptionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public JButton backButton;
	public JButton submitButton;
	
	public JTextField currentField;
	public JTextField passwordField;
	public JTextField confirmField;
	
	public OptionsPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
		
		JLabel title = new JLabel("Options");
		title.setFont(new Font("Helvetica", Font.BOLD, 14));
		title.setVisible(true);
		title.setSize(200, 40);
		title.setLocation(200-(int)((float)Utilities.getLabelWidth(title)/2F), 16);
		add(title);
		
		JLabel passwordTitle = new JLabel("Change Password");
		passwordTitle.setFont(new Font("Helvetica", Font.BOLD, 14));
		passwordTitle.setVisible(true);
		passwordTitle.setSize(200, 40);
		passwordTitle.setLocation(30, 60);
		add(passwordTitle);
		
		JLabel current = new JLabel("Current:");
		current.setVisible(true);
		current.setSize(200, 40);
		current.setLocation(36, 93);
		add(current);
		
		JLabel newPass = new JLabel("New:");
		newPass.setVisible(true);
		newPass.setSize(200, 40);
		newPass.setLocation(56, 133);
		add(newPass);
		
		JLabel confirm = new JLabel("Confirm:");
		confirm.setVisible(true);
		confirm.setSize(200, 40);
		confirm.setLocation(32, 173);
		add(confirm);
		
		backButton = new JButton("Back");
		backButton.setSize(60, 30);
		backButton.setLocation(16, 16);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.openMenu();
			}
		});
		add(backButton);
		
		submitButton = new JButton("Submit");
		submitButton.setSize(120, 30);
		submitButton.setLocation(213, 212);
		submitButton.addActionListener(new SubmitListener());
		add(submitButton);
		
		currentField = new JPasswordField();
		currentField.setFocusable(true);
		currentField.setText("");
		currentField.setSize(new Dimension(240, 28));
		currentField.setLocation(90, 100);
		add(currentField);
		
		passwordField = new JPasswordField();
		passwordField.setFocusable(true);
		passwordField.setText("");
		passwordField.setSize(new Dimension(240, 28));
		passwordField.setLocation(90, 140);
		add(passwordField);
		
		confirmField = new JPasswordField();
		confirmField.setFocusable(true);
		confirmField.setText("");
		confirmField.setSize(new Dimension(240, 28));
		confirmField.setLocation(90, 180);
		confirmField.addActionListener(new SubmitListener());
		add(confirmField);
		
		JLabel updateTitle = new JLabel("Update Information");
		updateTitle.setFont(new Font("Helvetica", Font.BOLD, 14));
		updateTitle.setVisible(true);
		updateTitle.setSize(200, 40);
		updateTitle.setLocation(30, 240);
		add(updateTitle);
	}
	
	public void updateProgress(int bytes)
	{
		
	}
	
	public class SubmitListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			PasswordHandler.changePassword(OptionsPanel.this);
		}
	}
}
