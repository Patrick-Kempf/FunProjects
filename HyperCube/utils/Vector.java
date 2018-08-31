package utils;

import java.util.Arrays;
import java.util.function.Function;

public class Vector {
	private double[] components;
	private static Function<double[], Double> specialNorm = new Function<double[], Double>() {
		@Override
		public Double apply(double[] arg0) {
			double sum = 0;
			for (double d : arg0) {
				sum += d * d;
			}
			return Math.sqrt(sum);
		}
	};

	public Vector(Vector v) {
		this.components = Arrays.copyOf(this.components, this.components.length);
	}
	
	public Vector(double... components) {
		this.components = components;
	}
	
	public Vector(double v, int dimensions) {
		this.components = new double[dimensions];
		Arrays.fill(this.components, v);
	}
	
	public static Vector functionConstructor(int dimensions, Function<Integer, Double> filler) {
		double[] vector = new double[dimensions];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = filler.apply(i);
		}
		return new Vector(vector);
	}
	
	public static Vector unitVector(int n, int dimensions) {
		double[] v = new double[dimensions];
		v[n] = 1;
		return new Vector(v);
	}
	
	public static Vector zero(int dimensions) {
		double[] c = new double[dimensions];
		for (int i = 0; i < c.length; i++) {
			c[i] = 0;
		}
		return new Vector(c);
	}

	public static Vector one(int dimensions) {
		double[] c = new double[dimensions];
		for (int i = 0; i < c.length; i++) {
			c[i] = 1;
		}
		return new Vector(c);
	}
	
	public int getDimenion() {
		return this.components.length;
	}

	public double norm() {
		double sum = 0;
		for (double d : this.components) {
			sum += d * d;
		}
		return Math.sqrt(sum);
	}
	
	public double norm(double n) {
		double sum = 0;
		for (double d : this.components) {
			sum += Math.pow(d, n);
		}
		return Math.pow(sum, 1 / n);
	}

	public double ownNorm() {
		return Vector.specialNorm.apply(this.components);
	}
	
	public Vector add(Vector v) {
		double[] c = new double[ensureDimensionality(this, v)];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] + v.components[i];
		}
		return new Vector(c);
	}
	
	public Vector add(double v) {
		double[] c = new double[this.components.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] + v;
		}
		return new Vector(c);
	}
	
	public Vector sub(Vector v) {
		double[] c = new double[ensureDimensionality(this, v)];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] - v.components[i];
		}
		return new Vector(c);
	}
	
	public Vector sub(double v) {
		double[] c = new double[this.components.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] - v;
		}
		return new Vector(c);
	}
	
	public Vector mul(double s) {
		double[] c = new double[this.components.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] * s;
		}
		return new Vector(c);
	}
	
	public Vector mul(Vector v) {
		double[] c = new double[ensureDimensionality(this, v)];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] * v.components[i];
		}
		return new Vector(c);
	}
	
	public Vector div(double s) {
		double[] c = new double[this.components.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] / s;
		}
		return new Vector(c);
	}
	
	public Vector div(Vector v) {
		double[] c = new double[ensureDimensionality(this, v)];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] / v.components[i];
		}
		return new Vector(c);
	}
	
	public double dot(Vector v) {
		ensureDimensionality(this, v);
		double dot = 0;
		for (int i = 0; i < this.components.length; i++) {
			dot += this.components[i] * v.components[i];
		}
		return dot;
	}
	
	public Vector cross(Vector... v) {
		
		if(this.getDimenion() == 2 && v.length <= 1) {
			return new Vector(this.components[0], -this.components[1]);
		} else if(this.getDimenion() == 3 && v.length == 1) {
			return new Vector(this.components[1] * v[0].components[2] - this.components[2] * v[0].components[1],
							  this.components[2] * v[0].components[0] - this.components[0] * v[0].components[2],
							  this.components[0] * v[0].components[1] - this.components[1] * v[0].components[0]);
		} else {

			v = Arrays.copyOf(v, v.length + 1);
			v[v.length - 1] = this.clone();
			return Vector.crossProduct(v);
		}
	}
	
	private static Vector crossProduct(Vector... vectors) {
		int dim = vectors[0].getDimenion();
		
		for (int i = 1; i < vectors.length; i++) {
			dim = ensureDimensionality(vectors[i - 1], vectors[i]);
		}
		
		
		
		if(vectors.length != (dim - 1))
			throw new DimensionException("The crossproduct in R^" + dim + " is only defined for " + (dim - 1) + " Vectors. Given " + vectors.length + ".");
		
		throw new RuntimeException("Not yet supported");
		
	}
	
	public Vector negate() {
		return this.mul(-1);
	}
	
	public Vector normalized() {
		double norm = this.norm();
		double[] c = new double[this.components.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = this.components[i] / norm;
		}
		return new Vector(c);
	}
	
	public double angelTo(Vector v) {
		return Math.acos(this.dot(v) / (this.norm() * v.norm()));
	}
	
	public boolean isOrtogonalTo(Vector v) {
		return 0 == this.dot(v);
	}
	
	public double getNthCmoponent(int n) {
		return this.components[0];
	}
	
	public Vector setNthComponent(int n, double v) {
		this.components[n] = v;
		return this;
	}
	
	public double[] getComponents() {
		return components;
	}

	public void setComponents(double[] components) {
		this.components = components;
	}

	public Function<double[], Double> getNorm() {
		return Vector.specialNorm;
	}

	public void setNorm(Function<double[], Double> norm) {
		Vector.specialNorm = norm;
	}

	public double distanceTo(Vector v) {
		return this.sub(v).norm();
	}
	
	public double[] heading() {
		double[] h = new double[this.components.length];
		Vector unit;
		for (int i = 0; i < h.length; i++) {
			unit = Vector.unitVector(i, this.components.length);
			h[i] = this.angelTo(unit);
		}
		return h;
	}
	
	public Vector apply(Matrix m) {
		return m.mult(this);
	}
	
	public Matrix toMatrix() {
		double[][] matrix = new double[this.components.length][1];
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][0] = this.components[i];
		}
		return new Matrix(matrix);
	}
	
	public double get(int i) {
		if(i < 0 || i >= this.components.length) throw new DimensionException("Requested index is not used in this vectorspace. " + i);
		return this.components[i];
	}
	
	//Overwritten methods from Object
	@Override
	public Vector clone() {
		return new Vector(Arrays.copyOf(this.components, this.components.length));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector))
			return false;
		Vector other = (Vector) obj;
		if (!Arrays.equals(this.components, other.components))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Vector");
		sb.append(this.components.length);
		sb.append("D(");
		for (int i = 0; i < this.components.length; i++) {
			if(i == (this.components.length - 1))
				sb.append(this.components[i]);
			else {
				sb.append(this.components[i]);
				sb.append(", ");
			}
		}
		sb.append(')');
		return sb.toString();
	}
	
	
	public static double[] toDegree(double[] radiants) {
		double[] r = new double[radiants.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = Math.toDegrees(radiants[i]);
		}
		return r;
	}
	
	private static int ensureDimensionality(Vector a, Vector b) {
		if(a.getDimenion() != b.getDimenion()) {
			throw new DimensionException("Vectors are not in the same Vectorspace: dim(a)=" + a.getDimenion() + ", dim(b)=" + b.getDimenion());
		} else {
			return a.getDimenion();
		}
	}

	@SuppressWarnings("serial")
	private static class DimensionException extends RuntimeException {
		public DimensionException(String message) {
			super(message);
		}
	}
}