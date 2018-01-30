package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;





import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Painter extends JPanel implements Runnable, ImageObserver{
	private boolean einmal = true;
	private Graphics gp;
	private Raster r;
	private BufferedImage i;
	private int ps;
	
	
	
	
	
	public Painter(Raster r, int width, int height, int pixelSize) {
		this.r = r;
		this.ps = pixelSize;
	}
	
	public Painter(Raster r, int width, int height) {
		this.r = r;
		this.ps = r.pixelSize;
	}

	@Override
	public void run() {
		boolean[][] zellen = new boolean[1][1];
		
		
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage img = gfxConf.createCompatibleImage(r.getWidth() * r.pixelSize, r.getHeight() * r.pixelSize);
		Graphics2D g = img.createGraphics();
			
			while(true) {
				
				try {
					zellen = r.toBooleanArray();
					//System.out.println(r.toString('#', ' '));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				g.setColor( Color.WHITE );
				g.fillRect(0, 0, img.getWidth(), img.getHeight());
				g.setColor(Color.BLACK);
				
				for (int i = 0; i < zellen.length; i++) {
					for (int j = 0; j < zellen[0].length; j++) {
						if(zellen[i][j]) {
							
							g.fillRect(j * ps, i * ps, ps, ps);
						}
						g.drawLine(0, i * ps, img.getWidth(), i * ps);
					}
				}
				
				for (int i = 0; i < zellen[0].length; i++) {
					g.drawLine(i * ps, 0,i * ps, img.getHeight());
				}
				
				i = img;
				
				repaint();
				r.tick();
				

			}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(i, 0, 0, null);
	}
	
	//@SuppressWarnings({"unused"})
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(einmal) {
			this.gp = g;
			this.einmal = !this.einmal;
			new Thread(this).start();
		}
		gp.drawImage(i, 50, 50, null);
	}
	
}
