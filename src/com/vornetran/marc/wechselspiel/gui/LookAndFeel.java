package com.vornetran.marc.wechselspiel.gui;

import javax.swing.UIManager;

public class LookAndFeel {

	public static void tryAndSetNativeLookAndFeel(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
