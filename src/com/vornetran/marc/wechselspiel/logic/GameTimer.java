package com.vornetran.marc.wechselspiel.logic;

public class GameTimer {

	private int seconds;
	private long startedAt = System.currentTimeMillis();
	
	public GameTimer(int seconds){
		this.seconds = seconds;
	}
	
	public void addSeconds(int seconds){
		this.seconds += seconds;
	}
	
	public int getRemainingSeconds(){
		long diff = seconds - (System.currentTimeMillis() - this.startedAt) / 1000;
		
		if(diff < 0){
			diff = 0;
		}
		
		return (int)diff;
	}
	
}
