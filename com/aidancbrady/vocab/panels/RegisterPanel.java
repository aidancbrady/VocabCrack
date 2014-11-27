package com.aidancbrady.vocab.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.aidancbrady.vocab.VocabCrack;
import com.aidancbrady.vocab.frames.VocabFrame;
import com.aidancbrady.vocab.net.RegisterHandler;
import com.aidancbrady.vocab.tex.Texture;

public class RegisterPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public VocabFrame frame;
	
	public JTextField usernameField;
	public JTextField emailField;
	public JTextField passwordField;
	public JTextField confirmField;
	
	public JProgressBar progressBar;
	
	public JButton registerButton;
	public JButton backButton;
	
	public RegisterPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setVisible(true);
		setLayout(null);
		
		backButton = new JButton("Back");
		backButton.setSize(60, 30);
		backButton.setLocation(16, 16);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.openLogin();
			}
		});
		add(backButton);
		
		JLabel login = new JLabel("Register a new account!");
		login.setVisible(true);
		login.setFont(new Font("Helvetica", Font.BOLD, 14));
		login.setSize(200, 40);
		login.setLocation(60, 140);
		add(login);
		
		JLabel user = new JLabel("Username:");
		user.setVisible(true);
		user.setSize(200, 40);
		user.setLocation(41, 192);
		add(user);
		
		JLabel em = new JLabel("Email:");
		em.setVisible(true);
		em.setSize(200, 40);
		em.setLocation(69, 232);
		add(em);
		
		JLabel pass = new JLabel("Password:");
		pass.setVisible(true);
		pass.setSize(200, 40);
		pass.setLocation(44, 272);
		add(pass);
		
		JLabel confirm = new JLabel("Confirm:");
		confirm.setVisible(true);
		confirm.setSize(200, 40);
		confirm.setLocation(52, 312);
		add(confirm);
		
		usernameField = new JTextField();
		usernameField.setFocusable(true);
		usernameField.setText("");
		usernameField.setSize(new Dimension(240, 28));
		usernameField.setLocation(110, 200);
		add(usernameField);
		
		emailField = new JTextField();
		emailField.setFocusable(true);
		emailField.setText("");
		emailField.setSize(new Dimension(240, 28));
		emailField.setLocation(110, 240);
		add(emailField);
		
		passwordField = new JPasswordField();
		passwordField.setFocusable(true);
		passwordField.setText("");
		passwordField.setSize(new Dimension(240, 28));
		passwordField.setLocation(110, 280);
		add(passwordField);
		
		confirmField = new JPasswordField();
		confirmField.setFocusable(true);
		confirmField.setText("");
		confirmField.setSize(new Dimension(240, 28));
		confirmField.setLocation(110, 320);
		confirmField.addActionListener(new RegisterButtonListener());
		add(confirmField);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setSize(240, 40);
		progressBar.setLocation(80, 360);
		progressBar.setVisible(false);
		add(progressBar);
		
		registerButton = new JButton("Register");
		registerButton.setSize(300, 60);
		registerButton.setLocation(50, 426);
		registerButton.addActionListener(new RegisterButtonListener());
		add(registerButton);
		
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
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		logo.draw(g, 50, 50, 300, 46);
	}
	
	public void setRegistering(boolean loggingIn)
	{
		progressBar.setVisible(loggingIn);
		registerButton.setEnabled(!loggingIn);
	}
	
	public class RegisterButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			RegisterHandler.register(RegisterPanel.this);
		}
	}
}
