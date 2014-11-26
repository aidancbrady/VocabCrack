package com.aidancbrady.vocab.tex;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import jgravatar.Gravatar;

import com.aidancbrady.vocab.Account;

public class AvatarHandler 
{
	public static File avatarDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "VocabServer" + File.separator + "Data");
	
	private static Map<String, Texture> gravatars = new HashMap<String, Texture>();
	
	private static final Gravatar gravatar = new Gravatar();
	
	public static Texture downloadAvatar(Account acct) throws Exception
	{
		if(!gravatars.containsKey(acct.email))
		{
			gravatar.setSize(256);
			
			byte[] download = gravatar.download(acct.email);
			
			if(download != null && download.length > 0)
			{
				Texture tex = new Texture(ImageIO.read(new ByteArrayInputStream(download)));
				gravatars.put(acct.email, tex);
				
				return tex;
			}
			
			return null;
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
}
