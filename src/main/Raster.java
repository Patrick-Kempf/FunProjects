package main;

import java.util.LinkedList;
import java.util.Random;


public class Raster implements Runnable {
	public int pixelSize = 5;
	
	private int width;
	private int height;
	
	private int xMax;
	private int yMax;
	
	private Cell[][] raster;
	private Cell[] controller;
	
	private boolean autoTick;
	
	private boolean bereit = false;
	
	public Raster(int width, int height, boolean autoTick) {
		this.width = width;
		this.height = height;
		this.xMax = this.width - 1;
		this.yMax = this.height - 1;
		this.raster = new Cell[this.height][this.width];
		this.autoTick = autoTick;
	}

	public Raster(int width, int height) {
		this.width = width;
		this.height = height;
		this.xMax = this.width - 1;
		this.yMax = this.height - 1;
		this.raster = new Cell[this.height][this.width];
		this.autoTick = false;
	}
	
	public void erstelleZellen(int max, int marke) throws Exception {
		bereit = false;
		if(marke < max) {
			LinkedList<Cell> c = new LinkedList<Cell>();
			Random rnd = new Random();
			for(int i = 0; i < raster.length; i++) {
				for (int j = 0; j < raster[0].length; j++) {
					this.raster[i][j] = new Cell(marke < rnd.nextInt(max));
					c.add(raster[i][j]);
				}
			}
			this.controller = c.toArray(new Cell[c.size()]);
			Thread t = new Thread(this);
			t.start();
			while(t.isAlive()) {}
			bereit = true;
		} else {
			throw new Exception("Der Wert der Marke muss kleiner als max sein");
		}
	}
	
	public void erstelleZellen() {
		bereit = false;
		LinkedList<Cell> c = new LinkedList<Cell>();
		for(int i = 0; i < raster.length; i++) {
			for (int j = 0; j < raster[0].length; j++) {
				this.raster[i][j] = new Cell();
				this.raster[i][j].setX(j);
				this.raster[i][j].setY(i);
				c.add(raster[i][j]);
			}
		}
		this.controller = c.toArray(new Cell[c.size()]);
		Thread t = new Thread(this);
		t.start();
		while(t.isAlive()) {}
		bereit = true;
		
	}
	
	//raster[y][x]
	
	@Override
	public void run() {	//hier wird die vermaschung stadtfinden
		
		//cell (0|0)
		raster[0][0].setTopLeft(raster[yMax][xMax]);
		raster[0][0].setTop(raster[yMax][0]);
		raster[0][0].setTopRight(raster[yMax][1]);
		raster[0][0].setLeft(raster[0][xMax]);
		raster[0][0].setRight(raster[0][1]);
		raster[0][0].setBottomLeft(raster[1][xMax]);
		raster[0][0].setBottom(raster[1][0]);
		raster[0][0].setBottomRight(raster[1][1]);

		
		//cell (yMax|0)
		raster[yMax][0].setTopLeft(raster[yMax - 1][xMax]);
		raster[yMax][0].setTop(raster[yMax - 1][0]);
		raster[yMax][0].setTopRight(raster[yMax - 1][1]);
		raster[yMax][0].setLeft(raster[yMax][xMax]);
		raster[yMax][0].setRight(raster[yMax][1]);
		raster[yMax][0].setBottomLeft(raster[0][xMax]);
		raster[yMax][0].setBottom(raster[0][0]);
		raster[yMax][0].setBottomRight(raster[0][1]);
		
		//cell (0|xMax)
		raster[0][xMax].setTopLeft(raster[yMax][xMax - 1]);
		raster[0][xMax].setTop(raster[yMax][xMax]);
		raster[0][xMax].setTopRight(raster[yMax][0]);
		raster[0][xMax].setLeft(raster[0][xMax - 1]);
		raster[0][xMax].setRight(raster[0][0]);
		raster[0][xMax].setBottomLeft(raster[1][xMax - 1]);
		raster[0][xMax].setBottom(raster[1][xMax]);
		raster[0][xMax].setBottomRight(raster[1][0]);
		
		//cell (yMax|xMax)
		raster[yMax][xMax].setTopLeft(raster[yMax - 1][xMax - 1]);
		raster[yMax][xMax].setTop(raster[yMax - 1][xMax]);
		raster[yMax][xMax].setTopRight(raster[yMax - 1][0]);
		raster[yMax][xMax].setLeft(raster[yMax][xMax - 1]);
		raster[yMax][xMax].setRight(raster[yMax][0]);
		raster[yMax][xMax].setBottomLeft(raster[0][xMax - 1]);
		raster[yMax][xMax].setBottom(raster[0][xMax]);
		raster[yMax][xMax].setBottomRight(raster[0][0]);
		
		for(int i = 1; i < raster[0].length - 1; i++) {
			//first Line
			raster[0][i].setTopLeft(raster[yMax][i - 1]);
			raster[0][i].setTop(raster[yMax][i]);
			raster[0][i].setTopRight(raster[yMax][i + 1]);
			raster[0][i].setLeft(raster[0][i - 1]);
			raster[0][i].setRight(raster[0][i + 1]);
			raster[0][i].setBottomLeft(raster[1][i - 1]);
			raster[0][i].setBottom(raster[1][i]);
			raster[0][i].setBottomRight(raster[1][i + 1]);
			
			//last Line
			raster[yMax][i].setTopLeft(raster[yMax - 1][i - 1]);
			raster[yMax][i].setTop(raster[yMax - 1][i]);
			raster[yMax][i].setTopRight(raster[yMax - 1][i + 1]);
			raster[yMax][i].setLeft(raster[yMax][i - 1]);
			raster[yMax][i].setRight(raster[yMax][i + 1]);
			raster[yMax][i].setBottomLeft(raster[0][i - 1]);
			raster[yMax][i].setBottom(raster[0][i]);
			raster[yMax][i].setBottomRight(raster[0][i + 1]);
		}
		
		for (int i = 1; i < raster.length - 1; i++) {
			//first Colum
			raster[i][0].setTopLeft(raster[i - 1][xMax]);
			raster[i][0].setTop(raster[i - 1][0]);
			raster[i][0].setTopRight(raster[i - 1][1]);
			raster[i][0].setLeft(raster[i][xMax]);
			raster[i][0].setRight(raster[i][1]);
			raster[i][0].setBottomLeft(raster[i + 1][xMax]);
			raster[i][0].setBottom(raster[i + 1][0]);
			raster[i][0].setBottomRight(raster[i + 1][1]);
			
			//last Colum
			raster[i][xMax].setTopLeft(raster[i - 1][xMax - 1]);
			raster[i][xMax].setTop(raster[i - 1][xMax]);
			raster[i][xMax].setTopRight(raster[i - 1][0]);
			raster[i][xMax].setLeft(raster[i][xMax - 1]);
			raster[i][xMax].setRight(raster[i][0]);
			raster[i][xMax].setBottomLeft(raster[i + 1][xMax - 1]);
			raster[i][xMax].setBottom(raster[i + 1][xMax]);
			raster[i][xMax].setBottomRight(raster[i + 1][0]);
		}
		
		for(int i = 1; i < raster.length - 1; i++) {
			for (int j = 1; j < raster[0].length - 1; j++) {
				raster[i][j].setTopLeft(raster[i - 1][j - 1]);
				raster[i][j].setTop(raster[i - 1][j]);
				raster[i][j].setTopRight(raster[i - 1][j + 1]);
				raster[i][j].setLeft(raster[i][j - 1]);
				raster[i][j].setRight(raster[i][j + 1]);
				raster[i][j].setBottomLeft(raster[i + 1][j - 1]);
				raster[i][j].setBottom(raster[i + 1][j]);
				raster[i][j].setBottomRight(raster[i + 1][j + 1]);
				
			}
		}
		
	}

	@SuppressWarnings({"unused"})
	public void tick() {
		if(bereit) {
			for(int i = 0; i < controller.length; i++) {
				if(i != controller.length - 1) {
					new Thread(controller[i]).start();
			
				} else {
					Thread t = new Thread(controller[i]);
					t.start();
					
					int a = 0;
					while(t.isAlive()){ a += 1; }
				}
			}
		}
	}
	
	public boolean[][] toBooleanArray() throws Exception {
		if(bereit) {
			if(this.autoTick)
				tick();
			
			boolean[][] a = new boolean[height][width];
			for(int i = 0; i < raster.length; i++) {
				for (int j = 0; j < raster[0].length; j++) {
					a[i][j] = raster[i][j].NaechsteGeneration();
				}
			}
			return a;
		} else {
			throw new Exception("Keine Zellen vorhanden");
		}
	}
	
	public char[][] toCharArray(char lebend, char tot) throws Exception {
		if(bereit) {
			if(this.autoTick)
				tick();
			
			char[][] a = new char[height][width];
			for(int i = 0; i < raster.length; i++) {
				for (int j = 0; j < raster[0].length; j++) {
					if(raster[i][j].NaechsteGeneration()){ 
						a[i][j] = lebend;
					} else {
						a[i][j] = tot;
					}
				}
			}
			return a;
		} else {
			throw new Exception("Keine Zellen vorhanden");
		}
	}

	public String toString(char lebend, char tot) throws Exception {
		if(bereit) {		
			if(this.autoTick)
				tick();
			
			String a = "";
			for(int i = 0; i < raster.length; i++) {
				for (int j = 0; j < raster[0].length; j++) {
					if(raster[i][j].NaechsteGeneration()){ 
						a += lebend;
					} else {
						a += tot;
					}
				}
				a += "\n";
			}
			return a;
		} else {
			throw new Exception("Keine Zellen vorhanden");
		}
	
	}

	
	public boolean isAutoTick() {
		return autoTick;
	}

	
	public void setAutoTick(boolean autoTick) {
		this.autoTick = autoTick;
	}

	
	public int getWidth() {
		return width;
	}

	
	public int getHeight() {
		return height;
	}	
}
