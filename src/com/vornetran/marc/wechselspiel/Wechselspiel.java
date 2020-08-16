package com.vornetran.marc.wechselspiel;

import javax.swing.JOptionPane;

import com.vornetran.marc.wechselspiel.logic.GameTimer;
import com.vornetran.marc.wechselspiel.logic.Spielfeld;

public class Wechselspiel {

	private static final Wechselspiel instance = new Wechselspiel();
	private final Spielfeld spielfeld = new Spielfeld();
	private GameTimer time = new GameTimer(120);
	private int points = 0;
	private boolean exitGameCalled = false;
	
	public static Wechselspiel getWechselspiel(){
		return instance;
	}
	
	public Spielfeld getSpielfeld(){
		return spielfeld;
	}
	
	public int getPoints(){
		return points;
	}
	
	public int getTime(){
		int remainingSeconds = time.getRemainingSeconds();
		
		if(remainingSeconds == 0){
			if(!exitGameCalled){
				exitGameCalled = true;
				
				exitGame();
			}
		}
		
		return remainingSeconds;
	}
	
	public void addPoints(int points){
		this.points += points;
		
		this.time.addSeconds(points / 10);
	}
	
	private void exitGame(){
		JOptionPane.showMessageDialog(null, "<html>Die Zeit ist abgelaufen!<br>Deine Punktzahl beträgt: " + points, "Spiel beendet!", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
	
}
