package com.aidancbrady.vocab;

import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public final class Utilities 
{
	public static int getLabelWidth(JLabel l)
	{ 
		FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
		
		return (int)(l.getFont().getStringBounds(l.getText(), context).getWidth());
	}
	
	public static ImageIcon scaleImage(ImageIcon img, int width, int height)
	{
		return new ImageIcon(img.getImage().getScaledInstance(width, height, Image.SCALE_FAST));
	}
}
