package cube;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import utils.Matrix;
import utils.Vector;

public class Cube4D extends JPanel{

	private static final long serialVersionUID = 908331095397217877L;
	private List<Vector> cube;
	private List<Edge> edges;
	
	private  double deltaDeltaAngle = 0.01;
	
	private double angle = 0;
	private double deltaAngle = 0;
	private double deltaHangle = 0;
	private double hAngle = 0;
	private double deltaXangle;
	private double xAngle;
	
	public boolean isSTRGDown = false;
	
	private final Matrix UNITY;
	
	public Cube4D() {
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				deltaAngle = 0;
				deltaHangle = 0;
				deltaXangle = 0;
			}
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation() < 0) {
					if(arg0.isShiftDown())
						deltaHangle -= deltaDeltaAngle;
					else if(isSTRGDown)
						deltaXangle -= deltaDeltaAngle;
					else
						deltaAngle -= deltaDeltaAngle;
				}  else {
					if(arg0.isShiftDown())
						deltaHangle += deltaDeltaAngle;
					else if(isSTRGDown)
						deltaXangle += deltaDeltaAngle;
					else
						deltaAngle += deltaDeltaAngle;
				}
			}
		});
		
		
		this.cube = new ArrayList<Vector>();
		this.edges = new ArrayList<Edge>();
		
		final int DIMENIONS = 6;
		
		for (int i = 0; i < (2 << DIMENIONS); i++) {
			double[] v = new double[DIMENIONS];
			
			// Bit-voodo zum erstellen aller vektoren.
			v[0] = ((i & 1) == 1) ? 1 : -1;
			v[1] = (((i >>> 1) & 1) == 1) ? 1 : -1;
			v[2] = (((i >>> 2) & 1) == 1) ? 1 : -1;
			v[3] = (((i >>> 3) & 1) == 1) ? 1 : -1;
			v[4] = (((i >>> 4) & 1) == 1) ? 1 : -1;
			v[5] = (((i >>> 4) & 1) == 1) ? 1 : -1;
			this.cube.add(new Vector(v));
		}
		
		this.UNITY = Matrix.unityMatrix(DIMENIONS);
		
		for (int i = 0; i < this.cube.size(); i++) {
			for (int j = 0; j < this.cube.size(); j++) {
				Edge e = new Edge(this.cube.get(i), this.cube.get(j));
				if(!this.edges.contains(e)) {
					this.edges.add(e);
				}
			}
		}
		this.edges = this.edges.parallelStream().filter(x -> x.len() == 2).collect(Collectors.toList());
		
	
	}
	
	public List<Vector> getCube() {
		return this.cube;
	}
	
	public List<Edge> getEdges() {
		return this.edges;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		final int size = this.getWidth() / 2;
		final Vector CENTER = new Vector(this.getWidth() / 2.0, this.getHeight() / 2.5);
		angle = (angle + deltaAngle) % 360;
		hAngle = (hAngle + deltaHangle) % 360;
		xAngle = (xAngle + deltaXangle) % 360;
		//angle += 0.2;
		
		double sin = Math.sin(Math.toRadians(angle));
		double cos = Math.cos(Math.toRadians(angle));
		double s = Math.sin(Math.toRadians(-40));
		double c = Math.cos(Math.toRadians(-40));
		double ss = Math.sin(Math.toRadians(hAngle));
		double cc = Math.cos(Math.toRadians(hAngle));
		double sss = Math.sin(Math.toRadians(xAngle));
		double ccc = Math.cos(Math.toRadians(xAngle));
		
		Matrix temp0 = new Matrix(new double[][] {
			{1, 0, 0, 0, 0,0},
			{0, 0, -1, 0, 0,0},
			{0, 1, 0, 0, 0,0},
			{0,0,0,1, 0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1},
		});
		
		Matrix temp1 = new Matrix(new double[][] {
			{1, 0, 0, 0, 0,0},
			{0, c, -s, 0, 0,0},
			{0, s, c, 0 ,0,0},
			{0,0,0,1,0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1}
		});
		
		Matrix temp2 = new Matrix(new double[][] {
			{c, 0, s, 0,0,0},
			{0, 1, 0, 0,0,0},
			{-s, 0, c, 0,0,0},
			{0, 0, 0, 1,0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1}
		});
		
		
		Matrix rot2 = new Matrix( new double[][] {	// w-achse
			{1, 0, 0, 0,0,0},
			{0, 1, 0, 0,0,0},
			{0, 0, cos, -sin,0,0},
			{0, 0, sin, cos,0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1}
		});

		
		Matrix rot3 = new Matrix( new double[][] {	// z-achse
			{cc,-ss, 0, 0,0,0},
			{ss,cc,0,0,0,0},
			{0,0,1,0, 0,0},
			{0,0,0,1,0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1}
		});
	
		Matrix rot4 = new Matrix( new double[][] {	// z-achse
			{1,0,0,0,0,0},
			{0,ccc,-sss,0,0,0},
			{0,sss,ccc,0,0,0},
			{0,0,0,1,0,0},
			{0,0,0,0,1,0},
			{0,0,0,0,0,1}
		});
				
		Matrix rot = Matrix.mult(temp1, temp2, temp0, rot2, rot3, rot4);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2));
		List<Edge> e = this.edges.stream().map(x -> x.apply(rot)).map(x -> x.project(2, 2, 2, 3).mul(size)).collect(Collectors.toList());
		
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.setColor(Color.WHITE);
		Edge temp;
		
		g2d.setBackground(Color.getHSBColor( (float)(angle / 360f), 0.5f, 0.4f));
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		//draw!!!!
		for (Edge edge : e) {
			temp = edge.add(CENTER);
			final int CORNERWIDTH = (int)((BasicStroke)g2d.getStroke()).getLineWidth() + 3;
			float len = (float)temp.len();
			//float hue = map(len, 8, 658, 0, 360); //4D
			float hue = map((float)temp.len(), 0, 658, 0, 360); //6D
			max = len > max ? len : max;
			min = len < min ? len : min;
			
			g2d.setColor(Color.getHSBColor(hue / 360, 1, 1));

			g2d.drawLine((int)temp.getA().get(0), (int)temp.getA().get(1), (int)temp.getB().get(0), (int)temp.getB().get(1));
			g2d.fillOval((int)temp.getA().get(0) - (CORNERWIDTH / 2), (int)temp.getA().get(1) - (CORNERWIDTH / 2), CORNERWIDTH, CORNERWIDTH);
			g2d.fillOval((int)temp.getB().get(0) - (CORNERWIDTH / 2), (int)temp.getB().get(1) - (CORNERWIDTH / 2), CORNERWIDTH, CORNERWIDTH);
			
			
		}
		//System.out.println("MinLength: " + min + ", MaxLength: " + max);
	}
	float max = 0;
	float min = Float.POSITIVE_INFINITY;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// erstellt automatisch eine Übergangsmatrix von 4d zu 2d.
	// l4D ist der abstand der lichtquelle auf der w achse zum objekt
	// l3d ist der abstand der lichtquelle auf der z achse zum objekt
	public static Matrix transFormationMatrix2D(Vector v, double l4D, double l3D) {
		if(v.getDimenion() == 4)
			return Matrix.functionConstructer(2, 4, (y, x) -> x == y ? (1/((l4D - v.get(3))*(l3D - v.get(2)))) : 0);
		else return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
	  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	public static Matrix transformationMatrixND(Vector v, double... ds) {
		if(ds.length != v.getDimenion() - 2) throw new RuntimeException("Wrong ammount of lightdistances. Given: " + ds.length + ", needed:" + (v.getDimenion() - 2));
		double value = 1;
		
		for (int i = 0; i < ds.length; i++) {
			value *= ds[i] - v.get(v.getDimenion() - i - 1);
		}
		final double V = 1 / value;
		Matrix m = Matrix.functionConstructer(2, v.getDimenion(), (y, x) -> x == y ? V : 0);
		//System.out.println(m);
		return m;
	}
	
	
	
	public static void main(String[] args) {
		//System.out.println(new Cube4D().getCube());
		//new Cube4D().paintComponent(null);
		Drawer d = new Drawer(new Cube4D(), 1010, 1010);
		d.animate();
	}
	
	
	
	
	
	static class Edge {
		private Vector a;
		private Vector b;
		
		public Edge(Vector a, Vector b) {
			this.a = a;
			this.b = b;
		}
		
		public double len() {
			return a.distanceTo(b);
		}

		
		
		public Vector getA() {
			return a;
		}

		public Vector getB() {
			return b;
		}

		public Edge project(double...ds) {
			//transformationMatrixND
			Vector a = this.a.apply(transformationMatrixND(this.a, ds));//transFormationMatrix2D(this.a, l4D, l3D));
			Vector b = this.b.apply(transformationMatrixND(this.b, ds));//transFormationMatrix2D(this.b, l4D, l3D));
			return new Edge(a, b);
		}
		
		@Override
		public String toString() {
			return "Edge(" + this.a + " -> " + this.b + ", length:" + this.len() + ")";
		}
		
		public Edge mul(double d) {
			return new Edge(this.a.mul(d), this.b.mul(d));
		}
		
		public Edge add(Vector v) {
			return new Edge(this.a.add(v), this.b.add(v));
		}
		
		public Edge apply(Matrix m) {
			Vector a = this.a.apply(m);
			Vector b = this.b.apply(m);
			return new Edge(a, b);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * ((a == null) ? 0 : a.hashCode()) + prime * ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Edge))
				return false;
			Edge other = (Edge) obj;
			return (this.a.equals(other.a) && this.b.equals(other.b)) || (this.a.equals(other.b) && this.b.equals(other.a));
		}
	}
	
}
