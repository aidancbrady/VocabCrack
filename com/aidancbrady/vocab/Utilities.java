package com.aidancbrady.vocab;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;

public final class Utilities 
{
	public static int getLabelWidth(JLabel l)
	{ 
		FontRenderContext context = new FontRenderContext(new AffineTransform(), true, true);
		
		return (int)(l.getFont().getStringBounds(l.getText(), context).getWidth());
	}
}
