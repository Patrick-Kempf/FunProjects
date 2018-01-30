package main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Fenster extends JFrame {

	JPanel panel;
	
	public Fenster(Raster r){
		panel = new Painter(r, this.getWidth(), this.getHeight());
		panel.setPreferredSize(new Dimension(r.getWidth() * r.pixelSize - 10, r.getHeight() * r.pixelSize - 10));
		this.add(panel);
	}
		
	public static void main(String[] args) throws Exception {
		for (String string : args) {
			System.out.println(string);
		}
		Raster r = new Raster(30,30);
		r.pixelSize = 20;
		//r.erstelleZellen(100, 2);
		r.erstelleZellen();
		Fenster zf = new Fenster(r);
		zf.setTitle("Cornway's Game of Live");
		zf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		zf.pack();
		zf.setResizable(false);
		zf.setVisible(true);
	}
	
}
