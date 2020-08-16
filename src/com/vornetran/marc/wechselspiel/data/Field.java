package com.vornetran.marc.wechselspiel.data;

import java.awt.Color;
import java.awt.Graphics;

public class Field {

	private int x;
	private int y;
	private Color color;
	private boolean shouldBeRemoved = false;
	
	public Field(int x, int y, Color color){
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void switchWith(Field field){
		int hX = x;
		int hY = y;
		this.x = field.getX();
		this.y = field.getY();
		
		field.setX(hX);
		field.setY(hY);
	}
	
	public boolean isNeighbor(Field field){
		int diffX = Math.abs(field.getX() - x);
		int diffY = Math.abs(field.getY() - y);
		
		return diffX == 1 && diffY == 0 || diffX == 0 && diffY == 1;
	}
	
	public void paint(Graphics g, int x, int y, int width, int height){
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	
	public boolean hasSameColor(Color color){
		return this.color == color;
	}
	
	public boolean hasSameColor(Field field){
		return hasSameColor(field.getColor());
	}
	
	public void shouldBeRemoved(){
		this.shouldBeRemoved = true;
	}
	
	public boolean shouldThisBeRemoved(){
		return shouldBeRemoved;
	}
	
}
