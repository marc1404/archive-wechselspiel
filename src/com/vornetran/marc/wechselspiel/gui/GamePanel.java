package com.vornetran.marc.wechselspiel.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import com.vornetran.marc.wechselspiel.Wechselspiel;
import com.vornetran.marc.wechselspiel.data.Constants;
import com.vornetran.marc.wechselspiel.data.Field;
import com.vornetran.marc.wechselspiel.data.Point2D;
import com.vornetran.marc.wechselspiel.logic.Spielfeld;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Graphics g;
	private int fieldPixelWidth;
	private int fieldPixelHeight;
	private int fontSize;
	private int lastMouseX;
	private int lastMouseY;
	
	public GamePanel(){
		this.setDoubleBuffered(true);
		
		
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				Point2D point = getFieldPosByMousePos(x, y);
				
				if(point != null){
					Wechselspiel.getWechselspiel().getSpielfeld().clickField(point.getX(), point.getY());
				}
			}
			
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		
		this.addMouseMotionListener(new MouseMotionListener(){
			public void mouseDragged(MouseEvent e){}

			@Override
			public void mouseMoved(MouseEvent e){
				lastMouseX = e.getX();
				lastMouseY = e.getY();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		this.g = g;
		int fieldSizePlusOne = Constants.FIELD_SIZE + 1;
		this.fieldPixelWidth = this.getWidth() / fieldSizePlusOne;
		this.fieldPixelHeight = this.getHeight() / fieldSizePlusOne;
		
		updateFontSize();
		paintWhite();
		paintRaster();
		paintPointsAndTime();
		paintFieldLabels();
		paintFields();
		paintSelectedField();
	}
	
	private void updateFontSize(){
		this.fontSize = Math.min(fieldPixelWidth, fieldPixelHeight) / 2;
	}
	
	private void paintWhite(){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	private void paintRaster(){
		int width = this.getWidth();
		int height = this.getHeight();
		
		g.setColor(Color.BLACK);
		
		for(int i = 1; i <= Constants.FIELD_SIZE; i++){
			int vX = i * fieldPixelWidth;
			int hY = i * fieldPixelHeight;
			
			g.drawLine(vX, 0, vX, height);
			g.drawLine(0, hY, width, hY);
		}
	}
	
	private void paintFieldLabels(){
		int hY = (int)Math.round(fieldPixelHeight * 0.65);
		int vX = (int)Math.round(fieldPixelWidth * 0.4);
		
		setFont(Font.BOLD, fontSize);
		
		for(int i = 1; i <= Constants.FIELD_SIZE; i++){
			int hX = i * fieldPixelWidth + (int)Math.round(fieldPixelWidth * 0.4);
			int vY = i * fieldPixelHeight + (int)Math.round(fieldPixelHeight * 0.65);
			
			g.drawString(getHorizontalFieldLabel(i - 1), hX, hY);
			g.drawString(String.valueOf(i), vX, vY);
		}
	}
	
	private String getHorizontalFieldLabel(int i){
		return String.valueOf(Constants.HORIZONTAL_FIELD_LABELS.charAt(i));
	}
	
	private void paintPointsAndTime(){
		setFontSize((int)Math.round(fontSize * 0.3) + 1);
		paintPoints();
		paintTime();
	}
	
	private void paintPoints(){
		int points = Wechselspiel.getWechselspiel().getPoints();
		String label = "Punkte: ";
		int labelWidth = g.getFontMetrics().stringWidth(label);
		int fontHeight = g.getFontMetrics().getHeight();
		
		setFontStyle(Font.BOLD);
		g.drawString(label, 5, fontHeight);
		setFontStyle(Font.PLAIN);
		g.drawString(String.valueOf(points), 5 + labelWidth + 5, fontHeight);
	}
	
	private void paintTime(){
		int time = Wechselspiel.getWechselspiel().getTime();
		String label = "Zeit: ";
		int labelWidth = g.getFontMetrics().stringWidth(label);
		int fontHeight = g.getFontMetrics().getHeight();
		
		setFontStyle(Font.BOLD);
		g.drawString(label, 5, fontHeight * 2);
		setFontStyle(Font.PLAIN);
		g.drawString(String.valueOf(time) + "s", 5 + labelWidth + 5, fontHeight * 2);
	}
	
	private void setFontStyle(int style){
		setFont(style, g.getFont().getSize());
	}
	
	private void setFontSize(int size){
		setFont(g.getFont().getStyle(), size);
	}
	
	private void setFont(int style, int size){
		g.setFont(new Font("Arial", style, size));
	}
	
	private void paintFields(){
		Field[][] fields = Wechselspiel.getWechselspiel().getSpielfeld().getFields();
		
		for(int y = 0; y < fields.length; y++){
			for(int x = 0; x < fields.length; x++){
				Field field = fields[x][y];
				int fX = (x + 1) * fieldPixelWidth + 1;
				int fY = (y + 1) * fieldPixelHeight + 1;
				int width = fieldPixelWidth - 1;
				int height = fieldPixelHeight - 1;
				
				if(x == fields.length - 1){
					width += 10;
				}
				
				if(y == fields.length - 1){
					height += 10;
				}
				
				field.paint(g, fX, fY, width, height);
			}
		}
	}
	
	private void paintSelectedField(){
		Spielfeld spielfeld = Wechselspiel.getWechselspiel().getSpielfeld();
		Field[][] fields = spielfeld.getFields();
		
		for(int y = 0; y < fields.length; y++){
			for(int x = 0; x < fields.length; x++){
				Field field = fields[x][y];
				
				if(spielfeld.isSelectedField(field)){
					Graphics2D g2d = (Graphics2D)g;
					Stroke oldStroke = g2d.getStroke();
					
					g2d.setStroke(new BasicStroke(4));
					paintFieldBorder(x, y);
					paintNeighborBorder(fields, field, x - 1, y);
					paintNeighborBorder(fields, field, x + 1, y);
					paintNeighborBorder(fields, field, x, y - 1);
					paintNeighborBorder(fields, field, x, y + 1);
					
					Point2D point = getFieldPosByMousePos(lastMouseX, lastMouseY);
					
					if(point != null){
						int pX = point.getX();
						int pY = point.getY();
						Field mouseField = fields[pX][pY];
						
						if(mouseField.isNeighbor(field)){
							if(spielfeld.canSwitchFields(field, mouseField)){
								Point2D p1 = getFieldMiddlePointByIndex(x, y);
								Point2D p2 = getFieldMiddlePointByIndex(pX, pY);
								
								g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
							}
						}
					}
					
					g2d.setStroke(oldStroke);
					
					return;
				}
			}
		}
	}
	
	private void paintNeighborBorder(Field[][] fields, Field field, int x, int y){
		if(x < 0 || y < 0 || x >= fields.length || y >= fields.length){
			return;
		}
		
		Spielfeld spielfeld = Wechselspiel.getWechselspiel().getSpielfeld();
		
		if(spielfeld.canSwitchFields(field, fields[x][y])){
			paintFieldBorder(x, y);
		}
	}
	
	private void paintFieldBorder(int x, int y){
		int fX = (x + 1) * fieldPixelWidth;
		int fY = (y + 1) * fieldPixelHeight;
		
		g.setColor(Color.BLACK);
		g.drawRect(fX, fY, fieldPixelWidth, fieldPixelHeight);
	}
	
	private Point2D getFieldPosByMousePos(int x, int y){
		if(x > fieldPixelWidth && y > fieldPixelHeight){
			int fX = (x - fieldPixelWidth) / fieldPixelWidth;
			int fY = (y - fieldPixelHeight) / fieldPixelHeight;
			
			return new Point2D(fX, fY);
		}
		
		return null;
	}
	
	private Point2D getFieldMiddlePointByIndex(int x, int y){
		return new Point2D((x + 1) * fieldPixelWidth + fieldPixelWidth / 2, (y + 1) * fieldPixelHeight + fieldPixelHeight / 2);
	}
	
}
