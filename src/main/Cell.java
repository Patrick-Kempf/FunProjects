package main;

public class Cell implements Runnable{

	private Cell top;
	private Cell right;
	private Cell bottom;
	private Cell left;
	private Cell topLeft;
	private Cell topRight;
	private Cell bottomRight;
	private Cell bottomLeft;
	
	private boolean lebend;
	private boolean naechsteGeneration;
	private int x;
	private int y;
	
	public Cell(boolean lebendig) {
		this.lebend = lebendig; 
		this.naechsteGeneration = this.lebend;
	}
	
	public Cell() {
		this.lebend = Math.random() > 0.5;
		this.naechsteGeneration = this.lebend;
	}

	@Override
	public void run() {
		int nachbarn = 0;
		
		
		if(topLeft.isLebend()) { nachbarn++; }
		
		if(top.isLebend()) { nachbarn++; }
		
		if(topRight.isLebend()) { nachbarn++; }
		
		if(left.isLebend()) { nachbarn++; }
		
		if(right.isLebend()) { nachbarn++; }
		
		if(bottomLeft.isLebend()) { nachbarn++; }
		
		if(bottom.isLebend()) { nachbarn++; }
		
		if(bottomRight.isLebend()) { nachbarn++; }
		
		
		//System.out.println(nachbarn + "\n");
		if(!this.lebend) {
			if(nachbarn == 3) {
				this.naechsteGeneration = true;
			}
			
			
		}else {
			if(nachbarn < 2) {
				this.naechsteGeneration = false;
			}
			
			if(nachbarn >= 2 && nachbarn <= 3) {
				this.naechsteGeneration = true;
			}
			
			if(nachbarn > 3){
				this.naechsteGeneration = false;
		
			}
		}
	}
		
 	public boolean NaechsteGeneration() {
		this.lebend = this.naechsteGeneration;
		return naechsteGeneration;
	}

	public void setLebend(boolean lebend) {
		this.lebend = lebend;
	}

	public Cell getTop() {
		return top;
	}

	public void setTop(Cell top) {
		this.top = top;
	}

	public Cell getRight() {
		return right;
	}

	public void setRight(Cell right) {
		this.right = right;
	}

	public Cell getBottom() {
		return bottom;
	}

	public void setBottom(Cell bottom) {
		this.bottom = bottom;
	}

	public Cell getLeft() {
		return left;
	}

	public void setLeft(Cell left) {
		this.left = left;
	}

	public Cell getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Cell topLeft) {
		this.topLeft = topLeft;
	}

	public Cell getTopRight() {
		return topRight;
	}

	public void setTopRight(Cell topRight) {
		this.topRight = topRight;
	}

	public Cell getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Cell bottomRight) {
		this.bottomRight = bottomRight;
	}

	public Cell getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Cell bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isLebend() {
		return lebend;
	}
}
