package com.aidancbrady.vocab.tex;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import jgravatar.Gravatar;

import com.aidancbrady.vocab.Account;
import com.aidancbrady.vocab.VocabCrack;

public class AvatarHandler 
{
	public static File avatarDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "VocabCrack" + File.separator + "Temp");
	
	private static Texture defaultAvatar = Texture.load("user.png");
	
	private static Map<String, Texture> gravatars = new HashMap<String, Texture>();
	
	private static Set<String> downloading = new HashSet<String>();
	
	private static final Gravatar gravatar = new Gravatar();
	
	public static Texture downloadAvatar(String email) throws Exception
	{
		if(email == null || email.isEmpty())
		{
			return defaultAvatar;
		}
		
		if(!gravatars.containsKey(email))
		{
			if(!downloading.contains(email))
			{
				new AvatarDownload(email).start();
			}
			
			return defaultAvatar;
		}
		else {
			return gravatars.get(email);
		}
	}
	
	public static Texture downloadAvatar(Account acct) throws Exception
	{
		return downloadAvatar(acct.email);
	}
	
	public static boolean hasAvatar(Account acct)
	{
		return gravatars.containsKey(acct.email);
	}
	
	private static File getAvatarFile(Account acct)
	{
		return new File(avatarDir, acct.username + ".jpg");
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
	
	public static class AvatarDownload extends Thread
	{
		public String email;
		
		public AvatarDownload(String em)
		{
			email = em;
			setDaemon(true);
		}
		
		@Override
		public void run()
		{
			downloading.add(email);
			
			try {
				gravatar.setSize(256);
				
				byte[] download = gravatar.download(email);
				
				if(download != null && download.length > 0)
				{
					Texture tex = new Texture(ImageIO.read(new ByteArrayInputStream(download)));
					gravatars.put(email, tex);
					
					VocabCrack.instance().frame.menu.setAccountData();
					VocabCrack.instance().frame.friends.repaint();
					VocabCrack.instance().frame.friends.friendsList.repaint();
					VocabCrack.instance().frame.detailsFrame.setAccountData();
				}
			} catch(Exception e) {}
			
			downloading.remove(email);
		}
	}
}
