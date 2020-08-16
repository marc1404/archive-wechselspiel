package com.vornetran.marc.wechselspiel.data;

import java.awt.Color;

public class ComboCheckResult {

	private Color currentColor;
	private int combo;
	
	public ComboCheckResult(Color currentColor, int combo){
		this.currentColor = currentColor;
		this.combo = combo;
	}
	
	public Color getCurrentColor(){
		return currentColor;
	}
	
	public int getCombo(){
		return combo;
	}
	
}
