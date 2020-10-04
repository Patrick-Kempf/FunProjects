package umr.dbs.oop.cornwaysGameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame {

    private int cellSize = 20;
    private boolean[][] field;
    private Painter panel;

    public Window(boolean[][] field, String title, int cellSize) {
        this.cellSize = cellSize;
        this.field = field;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(cellSize * this.field[0].length, cellSize * this.field.length);
        this.setTitle(title);
        this.setResizable(false);
        this.panel = new Painter(this.getWidth(), this.getHeight());
        this.panel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.add(this.panel);
        this.pack();
        this.setVisible(true);
    }
    
    public void update() {
        this.panel.repaint();
    }

    class Painter extends JPanel {  //used to draw an image and paint it on the window
        private BufferedImage img;
        private Graphics2D g;

        public Painter(int width, int height) {
            GraphicsConfiguration conf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            this.img = conf.createCompatibleImage(width, height);
            this.g = img.createGraphics();
        }

        public void refresh() {
            this.g.setColor( Color.WHITE );
            this.g.fillRect(0,0, this.img.getWidth(), this.img.getHeight());
            this.g.setColor(Color.black);

            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    if(field[i][j]) {

                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    g.drawLine(0, i * cellSize, img.getWidth(), i * cellSize);
                }
            }

            for (int i = 0; i < field[0].length; i++) {
                g.drawLine(i * cellSize, 0,i * cellSize, img.getHeight());
            }
        }
        
        public boolean saveFrame(String path, String type) {
            BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D iG2 = image.createGraphics();
            paint(iG2);
            try{
                return ImageIO.write(image, type, new File(path.endsWith(path) ? path : path + "." + type));
            } catch (Exception e) {
                e.printStackTrace();
            }
	    }
        
        @Override
        public void paint(Graphics g) {
            refresh();
            super.paint(g);
            g.drawImage(this.img, 0, 0, null);
        }
    }

}
