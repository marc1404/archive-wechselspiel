package com.vornetran.marc.wechselspiel.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import com.vornetran.marc.wechselspiel.start.Start;

public class FrameHelper {

	public static void showGameFrame(){
		JFrame frame = new JFrame("DHBW Karlsruhe: Wechselspiel");
		GamePanel gamePanel = new GamePanel();
		
		frame.add(gamePanel);
		setIcon(frame);
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		startRepaintTimer(frame);
	}
	
	private static void setIcon(JFrame frame){
		URL iconURL = Start.class.getResource("/resources/dhbw.png");
		ImageIcon icon = new ImageIcon(iconURL);
		
		frame.setIconImage(icon.getImage());
	}
	
	private static void startRepaintTimer(final JFrame frame){
		Timer timer = new Timer(1, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				frame.repaint();
			}	
		});
		
		timer.setRepeats(true);
		timer.start();
	}
	
}
