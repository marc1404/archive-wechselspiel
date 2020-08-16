package com.vornetran.marc.wechselspiel.start;

import java.awt.EventQueue;

import com.vornetran.marc.wechselspiel.gui.FrameHelper;
import com.vornetran.marc.wechselspiel.gui.LookAndFeel;

public class Start {

	public static void main(String[] args){
		LookAndFeel.tryAndSetNativeLookAndFeel();
		
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				FrameHelper.showGameFrame();
			}
		});
	}
	
}
