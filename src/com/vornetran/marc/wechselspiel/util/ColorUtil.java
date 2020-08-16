package com.vornetran.marc.wechselspiel.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorUtil {

	private static Color[] colors;
	private static Random random = new Random();
	static {
		List<Color> tempColors = new ArrayList<Color>();
		
		tempColors.add(Color.BLUE);
		tempColors.add(Color.CYAN);
		tempColors.add(Color.GREEN);
		tempColors.add(Color.MAGENTA);
		tempColors.add(Color.ORANGE);
		tempColors.add(Color.PINK);
		tempColors.add(Color.RED);
		tempColors.add(Color.WHITE);
		tempColors.add(Color.YELLOW);
		tempColors.add(new Color(153, 51, 102));
		
		colors = tempColors.toArray(new Color[tempColors.size()]);
	}
	
	public static Color getRandomColor(){
		return colors[random.nextInt(colors.length)];
	}
	
}
