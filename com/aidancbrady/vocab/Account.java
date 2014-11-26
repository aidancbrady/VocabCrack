package com.aidancbrady.vocab;

public class Account 
{
	public static final Account DEFAULT = new Account("Guest", "guest@test.com", "password");
	
	public String username;
	
	public String email;
	
	public String password;
	
	public int gamesWon;
	
	public int gamesLost;
	
	public Account(String user, String em, String pass)
	{
		username = user;
		email = em;
		password = pass;
	}
	
	public Account setUsername(String user)
	{
		username = user.trim();
		
		return this;
	}
	
	public Account setEmail(String em)
	{
		email = em;
		
		return this;
	}
	
	public Account setPassword(String pass)
	{
		password = pass.trim();
		
		return this;
	}
	
	public Account setGamesWon(int won)
	{
		gamesWon = won;
		
		return this;
	}
	
	public Account setGamesLost(int lost)
	{
		gamesLost = lost;
		
		return this;
	}
	
	public double getWinRatio()
	{
		return (double)gamesWon/(double)gamesLost;
	}
}
