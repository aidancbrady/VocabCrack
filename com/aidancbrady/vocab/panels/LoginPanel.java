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
import com.aidancbrady.vocab.net.LoginHandler;
import com.aidancbrady.vocab.tex.Texture;

public class LoginPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public VocabFrame frame;
	
	public JTextField usernameField;
	public JTextField passwordField;
	
	public JProgressBar progressBar;
	
	public JButton loginButton;
	public JButton registerButton;
	
	public LoginPanel(VocabFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setVisible(true);
		setLayout(null);
		
		JLabel login = new JLabel("Login now!");
		login.setVisible(true);
		login.setFont(new Font("Helvetica", Font.BOLD, 14));
		login.setSize(200, 40);
		login.setLocation(60, 140);
		add(login);
		
		JLabel user = new JLabel("Username:");
		user.setVisible(true);
		user.setSize(200, 40);
		user.setLocation(40, 192);
		add(user);
		
		JLabel pass = new JLabel("Password:");
		pass.setVisible(true);
		pass.setSize(200, 40);
		pass.setLocation(44, 232);
		add(pass);
		
		usernameField = new JTextField();
		usernameField.setFocusable(true);
		usernameField.setText("");
		usernameField.setSize(new Dimension(240, 28));
		usernameField.setLocation(110, 200);
		add(usernameField);
		
		passwordField = new JPasswordField();
		passwordField.setFocusable(true);
		passwordField.setText("");
		passwordField.setSize(new Dimension(240, 28));
		passwordField.setLocation(110, 240);
		passwordField.addActionListener(new LoginButtonListener());
		add(passwordField);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setSize(240, 40);
		progressBar.setLocation(80, 280);
		progressBar.setVisible(false);
		add(progressBar);
		
		loginButton = new JButton("Login");
		loginButton.setSize(300, 60);
		loginButton.setLocation(50, 360);
		loginButton.addActionListener(new LoginButtonListener());
		add(loginButton);
		
		registerButton = new JButton("Register");
		registerButton.setSize(300, 60);
		registerButton.setLocation(50, 426);
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.openRegister();
			}
		});
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
	
	public void setLoggingIn(boolean loggingIn)
	{
		progressBar.setVisible(loggingIn);
		loginButton.setEnabled(!loggingIn);
	}
	
	public class LoginButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			LoginHandler.login(LoginPanel.this);
		}
	}
}
