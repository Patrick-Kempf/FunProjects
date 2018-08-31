package cube;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Drawer extends JFrame {
	private static final long serialVersionUID = 7335798253391908137L;
	public boolean animationLoop = true;
	JPanel panel;
	Cube4D cube;
	
	public Drawer(Cube4D p, int width, int height){
		//panel = p;
		cube = p;
		this.setSize(new Dimension(width, height));
		cube.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.add(cube);
		this.setTitle(p.getClass().getSimpleName());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(cube.isSTRGDown && arg0.isControlDown()) {
					cube.isSTRGDown = false;
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.isControlDown()) {
					cube.isSTRGDown = true;
				}
				
			}
		});
		this.setVisible(true);
	}

	public void animate() {
		while(this.animationLoop)
			this.repaint();
	}
	
}
