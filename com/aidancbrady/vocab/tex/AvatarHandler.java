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
	
	public static Texture downloadAvatar(Account acct) throws Exception
	{
		if(!gravatars.containsKey(acct.email))
		{
			if(!downloading.contains(acct.email))
			{
				new AvatarDownload(acct).start();
			}
			
			return defaultAvatar;
		}
		else {
			return gravatars.get(acct.email);
		}
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
		public Account acct;
		
		public AvatarDownload(Account a)
		{
			acct = a;
			setDaemon(true);
		}
		
		@Override
		public void run()
		{
			downloading.add(acct.email);
			
			try {
				gravatar.setSize(256);
				
				byte[] download = gravatar.download(acct.email);
				
				if(download != null && download.length > 0)
				{
					Texture tex = new Texture(ImageIO.read(new ByteArrayInputStream(download)));
					gravatars.put(acct.email, tex);
					
					VocabCrack.instance().frame.menu.setAccountData();
				}
			} catch(Exception e) {}
			
			downloading.remove(acct.email);
		}
	}
}
