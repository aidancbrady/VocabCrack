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

import com.aidancbrady.vocab.Updater.UpdateThread;
import com.aidancbrady.vocab.Utilities;
import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.frames.VocabFrame;
import com.aidancbrady.vocab.net.PasswordHandler;

public class OptionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VocabFrame frame;
	
	public JButton backButton;
	public JButton submitButton;
	public JButton updateButton;
	
	public JTextField currentField;
	public JTextField passwordField;
	public JTextField confirmField;
	
	public JLabel latestLabel;
	public JLabel versionLabel;
	public JLabel progressLabel;
	
	public boolean updated;
	
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
		
		latestLabel = new JLabel("Latest Version: N/A");
		latestLabel.setVisible(true);
		latestLabel.setSize(200, 40);
		latestLabel.setLocation(32, 268);
		add(latestLabel);
		
		versionLabel = new JLabel("Version: " + VocabCrack.VERSION);
		versionLabel.setVisible(true);
		versionLabel.setSize(200, 40);
		versionLabel.setLocation(32, 298);
		add(versionLabel);
		
		updateButton = new JButton("Update");
		updateButton.setSize(120, 30);
		updateButton.setLocation(26, 380);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new UpdateThread(OptionsPanel.this).start();
			}
		});
		updateButton.setEnabled(false);
		add(updateButton);
		
		progressLabel = new JLabel("Version: " + VocabCrack.VERSION);
		progressLabel.setVisible(false);
		progressLabel.setSize(200, 40);
		progressLabel.setLocation(32, 298);
		add(progressLabel);
	}
	
	public void updateProgress(int bytes)
	{
		progressLabel.setText(bytes + " bytes downloaded...");
	}
	
	public void updateData()
	{
		if(Utilities.dataLoaded)
		{
			String info = Utilities.isLatestVersion() ? " - up-to-date!" : " - outdated!";
			latestLabel.setText("Latest Version: " + Utilities.latestVersion);
			versionLabel.setText("Version: " + VocabCrack.VERSION + info);
			
			if(!Utilities.isLatestVersion())
			{
				updateButton.setEnabled(true);
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		
		updateData();
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
