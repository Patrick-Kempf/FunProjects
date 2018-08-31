package utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;


public class Matrix {

	private double[][] matrix;
	private boolean transposed;
	
	
	
	
	//Constructors
	public Matrix(int rows, int columns, double value) {
		this.matrix = new double[rows][columns];
		for (double[] ds : matrix) {
			Arrays.fill(ds, value);
		}
	}
	
	public Matrix(double[][] m) {
		for (int i = 1; i < m.length; i++) {
			if(m[i - 1].length != m[i].length) {
				throw new ArrayStoreException("Row " + i + " has a worng length.");
			}
		}
		this.matrix = m;	
	}
	
	/**
	 * Copyconstructor
	 * @param m
	 */
	public Matrix(Matrix m) {
		double[][] mat = new double[this.matrix.length][this.matrix[0].length];
		for (int i = 0; i < mat.length; i++) {
				mat[i] = Arrays.copyOf(this.matrix[i], this.matrix[0].length);
		}
		this.matrix = mat;
		this.transposed = m.transposed;
	}
	
	/**
	 * private copy constructor
	 * @param m
	 * @param transposed
	 */
	private Matrix(double[][] m, boolean transposed) {
		for (int i = 1; i < m.length; i++) {
			if(m[i - 1].length != m[i].length) {
				throw new ArrayStoreException("Row " + i + " has a worng length.");
			}
		}
		this.matrix = m;
		this.transposed = transposed;
	}
	
	/**
	 * Generates a matrix containing a magic square.
	 * @param sideLength
	 */
	public Matrix(int sideLength) {
		this.matrix = Matrix.MagicSquare(sideLength).getMatrix();
	}
	
	/**
	 * Generates a Matrix with the size rowCount x columnCount, using the given filler function. The funcion gets the current row and column index.
	 * The fillerfuncion gets calld rowCount * columnCount-Times
	 * 
	 * @param rowCount
	 * @param columnCount
	 * @param filler
	 * @return
	 */
	public static Matrix functionConstructer(int rowCount, int columnCount, BiFunction<Integer, Integer, Double> filler) {
		double[][] matrix = new double[rowCount][columnCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				matrix[i][j] = filler.apply(i, j);
			}
		}
		return new Matrix(matrix);
	}
	
	/**
	 * Returns the unitymatrix for a given sidelength. The returnd matrix will be a sqared Matrix.
	 * @param sideLength
	 * @return
	 */
	public static Matrix unityMatrix(int sideLength) {
		double[][] matrix = new double[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				if(i == j)
					matrix[i][j] = 1;
			}
		}
		return new Matrix(matrix);
	}
	
	
	
	
	
	
	
	
	
	
	//Class Methods
	
	/**
	 * Transpose the matrix.
	 * Warning! this is the only mutable Method
	 * @return this
	 */
	public Matrix transpose() {
		this.transposed = !this.transposed;
		return this;
	}
	
	/**
	 * Transpose a new Matrix my copying the Matrix
	 * @return new Transposed matrix
	 */
	public Matrix copyTranspose() {
		double[][] m = new double[this.matrix.length][this.matrix[0].length];
		for (int i = 0; i < m.length; i++) {
			m[i] = Arrays.copyOf(this.matrix[i], this.matrix[0].length);
		}
		return new Matrix(m, !this.transposed);
	}
	
	
	/**
	 * Performs Matrixaddition
	 * @param m
	 * @return
	 */
	public Matrix add(Matrix m) {
		if(m.getColumnCount() != this.getColumnCount() || m.getRowCount() != this.getRowCount())
			throw new DimensionError("row- and columncount need to be the same.");
		
		double[][] res = new double[this.getRowCount()][this.getColumnCount()];
		for (int i = 0; i < this.getRowCount(); i++) {
			for (int j = 0; j < this.getColumnCount(); j++) {
				res[i][j] = this.get(i, j) + m.get(i, j);
			}
		}
		return new Matrix(res);
	}
	
	/**
	 * Adds a constant value to the Matrix
	 * @param n
	 * @return
	 */
	public Matrix add(double n) {
		double[][] m = new double[this.matrix.length][this.matrix[0].length];
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = this.matrix[i][j] + n;
			}
		}
		return new Matrix(m, this.transposed);
	}
	
	/**
	 * Performs matrixmultiplycation.
	 * @param m
	 * @return
	 */
	public Matrix mult(Matrix m) {
		int[] d = ensueMultDimensionality(this, m);
		final int COLS = this.getColumnCount();
		double[][] mat = new double[d[0]][d[1]];
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				for (int k = 0; k < COLS; k++) {
					mat[i][j] += this.get(i, k) * m.get(k, j);
				}
			}
		}
		
		return new Matrix(mat);
	}
	
	/**
	 * Multipies the matrix with the Vector.
	 * @param v
	 * @return Transformed Vector
	 */
	public Vector mult(Vector v) {
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		if(v.getDimenion() != COLS)
			throw new DimensionError("Dimensionality of the Vector and the column-count of the Matrix doesn't fit. dim(v)=" + v.getDimenion() + ", Matrix columns:" + this.getRowCount());
		
		double[] picture = new double[ROWS];
		
		double sum;
		for (int i = 0; i < ROWS; i++) {
			sum = 0;
			for (int j = 0; j < COLS; j++) {
				sum += this.get(i, j) * v.get(j);
			}
			picture[i] = sum;
		}
		return new Vector(picture);
	}
	
	/**
	 * Multipiey a matrix with a constant value.
	 * @param n
	 * @return Scaled matrix
	 */
	public Matrix mult(double n) {
		double[][] m = new double[this.matrix.length][this.matrix[0].length];
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = this.matrix[i][j] * n;
			}
		}
		return new Matrix(m, this.transposed);
	}

	/**
	 * Performs a Hadamardmultiplication with the given Matrix.
	 * @param m
	 * @return Hadamardproduct
	 */
	public Matrix multHadamard(Matrix m) {
		int[] d = ensureSameDimensionality(this, m);
		double[][] mat = new double[d[0]][d[1]];
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				mat[i][j] = this.get(i, j) * m.get(i, j);
			}
		}
		return new Matrix(mat);
	}
	
	/**
	 * Performs the Frobenius inner product with the given matrix.
	 * @param m
	 * @return Frobenius inner product
	 */
	public double multScalar(Matrix m) {
		int[] d = ensureSameDimensionality(this, m);
		double sum = 0;
		
		for (int i = 0; i < d[0]; i++) {
			for (int j = 0; j < d[1]; j++) {
				sum += this.get(i, j) * m.get(i, j);
			}
		}
		return sum;
	}
	
	/**
	 * Performs matrixmultiplication n times
	 * @param n
	 * @return
	 */
	public Matrix pow(int n) {
		Matrix m = this.clone();
		for (int i = 0; i < n - 1; i++) {
			m = m.mult(this);
		}
		return m;
	}
	
	/**
	 * Performs matrixmultiplication with an n >= 2 matricies.
	 * Uses the assoziativ law of matricies to reduct the effort of matrixmultiplication.
	 * The result should be the same as performing matixmultiplication iteratively.
	 * By using large ammounts of matricies or using large matricies, roundingerrors are very likely to occur.
	 * The order of multiplication is the same as given as prameter.
	 * 
	 * Performs "cheap" multiplications first.
	 * 
	 * @param matricies to multipy
	 * @return product of all matricies
	 */
	public static Matrix mult(Matrix... matricies) {
		if(matricies.length == 0) return null;
		else if(matricies.length == 1) return matricies[0];
		else if(matricies.length == 2) return matricies[0].mult(matricies[1]);
		List<Matrix> matrixList = new LinkedList<Matrix>();
		
		for (Matrix matrix : matricies) {
			matrixList.add(matrix);
		}
		
		//using the assioaziative law i multiply in a way to reduce the ammount of steps in matixmult
		
		while(matrixList.size() > 1) {
			int index = Matrix.findCheapedPair(matrixList);
			Matrix m1 = matrixList.remove(index);
			Matrix m2 = matrixList.remove(index);
			
			if(index < matrixList.size())
				matrixList.add(index, m1.mult(m2));
			else
				matrixList.add(m1.mult(m2));
		}

		return matrixList.get(0);
	}

	/**
	 * Searches the "cheapest" multiplication by using a heutistic.
	 * @param ms
	 * @return
	 */
	private static int findCheapedPair(List<Matrix> ms) {
		if(ms.size() <= 1) throw new RuntimeException("Internal Error. At least two maticies are requred!");
		int cheapesFirstIndex = 0;
		double cheapestCost = Double.MAX_VALUE;
		Matrix m1;
		Matrix m2;
		for (int i = 0; i < ms.size() - 1; i++) {
			m1 = ms.get(i);
			m2 = ms.get(i + 1);
			
			double tempCost = (m1.getColumnCount() * 1.0) * (m1.getRowCount() * 1.0) * (m2.getColumnCount() * 1.0) * (m2.getRowCount() * 1.0);
			
			if(tempCost < cheapestCost) {
				cheapestCost = tempCost;
				cheapesFirstIndex = i;
			}
			
		}
		return cheapesFirstIndex;
	}
	
	/**
	 * Performs matrixsubtraction.
	 * @param m
	 * @return Difference betreen the two matricies
	 */
	public Matrix subtract(Matrix m) {
		if(m.getColumnCount() != this.getColumnCount() || m.getRowCount() != this.getRowCount())
			throw new DimensionError("row- and columncount need to be the same. Given " + this.getRowCount() + "x" + this.getColumnCount() + " and " + m.getRowCount() + "x" + m.getColumnCount());
		
		double[][] res = new double[this.getRowCount()][this.getColumnCount()];
		for (int i = 0; i < this.getRowCount(); i++) {
			for (int j = 0; j < this.getColumnCount(); j++) {
				res[i][j] = this.get(i, j) - m.get(i, j);
			}
		}
		return new Matrix(res);
	}
	
	/**
	 * Substracts a constant value form the Matrix.
	 * @param n
	 * @return
	 */
	public Matrix subtract(double n) {
		double[][] m = new double[this.matrix.length][this.matrix[0].length];
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = this.matrix[i][j] - n;
			}
		}
		return new Matrix(m, this.transposed);
	}
	
	/**
	 * Devides every value of the matrix my the given scalar.
	 * @param n
	 * @return
	 */
	public Matrix divide(double n) {
		double[][] m = new double[this.matrix.length][this.matrix[0].length];
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				m[i][j] = this.matrix[i][j] / n;
			}
		}
		return new Matrix(m, this.transposed);
	}
	
	/**
	 * If the given matrix is inverable, it becomes inverted an afterwards multiplies with this.
	 * @param m
	 * @return
	 */
	public Matrix divide(Matrix m) {
		if(m.isInverable()) {
			return this.mult(m.invert());
		} else {
			return null;
		}
	}
	
	/**
	 * Trys to invert the Matrix using the Gaussian algorithem.
	 * @return inverse of this, if and only if the matrix is inveratble.
	 */
	public Matrix invert() {
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		if(ROWS != COLS)
			throw new DimensionError("Matrix need to be a squarematrix. Given: " + ROWS + "x" + COLS);
		//TODO maybe contains issues (hope not...)
		Matrix invertionMatrix = new Matrix(Matrix.columnConcut(Matrix.arrClone(this.matrix), Matrix.unityMatrix(ROWS).matrix));
		invertionMatrix = Matrix.gaussianAlgorithem(invertionMatrix);
		
		double[][] inverted = new double[ROWS][COLS];
		for (int i = 0; i < inverted.length; i++) {
			for (int j = 0; j < inverted[0].length; j++) {
				inverted[i][j] = invertionMatrix.matrix[i][j + COLS];
			}
		}
		
		if(!Matrix.isInverted(invertionMatrix.matrix)) throw new RuntimeException("Matrix is not invertable");
		return new Matrix(inverted);
	}
	
	/**
	 * Checks whether the given double matrix is sucsessfully inverted. On the lefthandside there has to be the unityMatrix of sidelength of rowCount
	 * @param matrix
	 * @return true if the matrix is successfully inverted
	 */
	private static boolean isInverted(double[][] matrix) {
		if((matrix.length << 1) != matrix[0].length) return false;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if(i == j && matrix[i][j] != 1) return false;
				else if(i != j && matrix[i][j] != 0) return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns true if the Matrix is invertable.
	 * This is the case if every row of the Matrix is linear independent aka. the Rank is maximal aka. the determinante is NOT zero.
	 * @return true whether the determinante is not zero aka. the matrix is inverable.
	 */
	public boolean isInverable() {
		return this.determinate() != 0;
	}
	
	/**
	 * Calculates the determinante using the static method to calculate the Determinante.
	 * @return determinante of the matrix.
	 */
	public double determinate() {
		return Matrix.determinante(this);
	}
	
	/**
	 * Calculates the determinante using the lapacedevelopement for rowCount > 3.
	 * Otherwise it uses the sarrus formular. If the matrix consists only of one row and one column the only value repesents the determinante.
	 * @param m
	 * @return
	 */
	public static double determinante(Matrix m) {
		final int ROWS = m.getRowCount(), COLS = m.getColumnCount();
		if(ROWS != COLS) throw new DimensionError("Determinante is only defined for sqared matricies. Given " + ROWS + "x" + COLS);
		
			 if(ROWS == 0) return Double.NaN;
		else if(ROWS == 1) return m.get(0, 0);
		else if(ROWS == 2) return m.get(0, 0) * m.get(1, 1) - m.get(0, 1) * m.get(1, 0);
		else if(ROWS == 3) return m.get(0, 0) * m.get(1, 1) * m.get(2, 2) +
								  m.get(0, 1) * m.get(1, 2) * m.get(2, 0) + 
								  m.get(0, 2) * m.get(1, 0) * m.get(2, 1) -
								  m.get(0, 2) * m.get(1, 1) * m.get(2, 0) -
								  m.get(0, 1) * m.get(1, 0) * m.get(2, 2) -
								  m.get(0, 0) * m.get(1, 2) * m.get(2, 1);
		else {
			double sum = 0;
			for (int i = 0; i < COLS; i++) {
				sum += m.get(0, i) * Matrix.lapaceSign(0, i) * Matrix.determinante(Matrix.lapaceSubMatrix(m, 0, i));
			}
			return sum;
		}
	}
	
	/**
	 * Calculates the sing using the current coordinates for the use in the laplacedevelopement.
	 * @param y
	 * @param x
	 * @return +-1
	 */
	private static int lapaceSign(int y, int x) {
		if((x + y) % 2 == 0)
			return 1;
		else
			return -1;
	}
	
	/**
	 * Generates the submatrix for the laplacedevelopement, my utelizing the current position of the developement.
	 * @param m
	 * @param y
	 * @param x
	 * @return matrix with the row y and the column x removed
	 */
	private static Matrix lapaceSubMatrix(Matrix m, int y, int x) {
		if(y < 0 || y >= m.getRowCount() || x < 0 || x >= m.getColumnCount()) throw new DimensionError("Internal Error " + m.getRowCount() + "x" + m.getColumnCount() + ", x=" + x + ", y=" + y);
		double[][] mat = new double[m.getRowCount() - 1][m.getColumnCount() - 1];
		
		int xCounter = 0;
		int yCounter = 0;
		
		final int ROWS = m.getRowCount(), COLS = m.getColumnCount();
		
		int i = 0;
		int j = 0;
		
		if(y == 0) i = 1;
		
		for (;i < ROWS; i++) {
			if(x == 0) j = 1; else j = 0; 
			for (;j < COLS; j++) {
				mat[yCounter][xCounter] = m.get(i, j);
				if(j + 1 == x) j++;
				xCounter++;
			}
			xCounter = 0;
			yCounter++;
			if(i + 1 == y) i++;
		}
		return new Matrix(mat);
	}
	
	/**
	 * Calculates the trace aka the sum of the main diagnogal of the matrix.
	 * @return trace of the matrix.
	 */
	public double trace() {
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		if(ROWS != COLS) throw new DimensionError("Trace is only defined for squared matricies. Given: " + ROWS + "x" + COLS);
		
		double sum = 0;
		for (int i = 0; i < ROWS; i++) {
			sum += this.get(i, i);
		}
		return sum;
	}
	
	/**
	 * Calculatrs the rank of the Matrix. The rank is the number of the not zero row in the reduces step form.
	 * @return rank of the matrix
	 */
	public int rank() {
		Matrix m = Matrix.gaussianAlgorithem(this);
		final int ROWS = m.getRowCount(), COLS = m.getColumnCount();
		boolean rowIsEmpty = true;
		int rankCounter = 0;
		for (int i = 0; i < ROWS; i++) {
			rowIsEmpty = true;
			for (int j = 0; j < COLS; j++) {
				rowIsEmpty &= (m.get(i, j) == 0);
			}
			if(!rowIsEmpty)
				rankCounter++;
		}
		return rankCounter;
	}
	
	/**
	 * Determents whether the Matrix is a magic sqare.
	 * @return true if all row, column and diagnoal sums are equal.
	 */
	public boolean isMagicSquare() {
		return Matrix.isMagicSquare(this);
	}
	
	public double magicSum() {
		if(this.isMagicSquare()) {
			double sum = 0;
			final int ROWS = this.getRowCount();
			for (int i = 0; i < ROWS; i++) {
				sum += this.get(i, i);
			}
			return sum;
		}
		return Double.NaN;
	}
	
	public Vector[] toVectorArray() {
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		double[] vector;
		Vector[] vectors = new Vector[COLS];
		
		for (int i = 0; i < COLS; i++) {
			vector = new double[ROWS];
			for (int j = 0; j < ROWS; j++) {
				vector[j] = this.get(j, i);
			}
			vectors[i] = new Vector(vector);
		}
		return vectors;
	}
	
	/**
	 * Returns the rowcount of the Matrix. It respects whether or not the matrix is transposed.
	 * @return rowcount
	 */
	public int getRowCount() {
		if(this.transposed) {
			return this.matrix[0].length;
		} else {
			return this.matrix.length;
		}
	}

	/**
	 * Returns the columncount of the Matrix. It respects whether or not the matrix is transposed.
	 * @return columncount
	 */
	public int getColumnCount() {
		if(!this.transposed) {
			return this.matrix[0].length;
		} else {
			return this.matrix.length;
		}
	}
	
	/**
	 * Reutrn the number at the position y, x. It respects whether or not the matrix is transposed.
	 * @param y
	 * @param x
	 * @return value at the position y, x
	 */
	public double get(int y, int x) {
		if(this.transposed)
			return this.matrix[x][y];
		else
			return this.matrix[y][x];
	}
	
	/**
	 * Calculates the Remainder of the Matrix with a given value.
	 * @param n
	 * @return
	 */
	public Matrix mod(double n) {
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		double[][] mod = new double[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				mod[i][j] = Matrix.goodMod(this.get(i, j), n);
			}
		}
		return new Matrix(mod);
	}
	
	/**
	 * Calculates the Mathematical correct remainder between n and m
	 * @param n
	 * @param m
	 * @return (n % m < 0) ? (n % m) + m : (n % m);
	 */
	public static double goodMod(double n, double m) {
		double mod = n % m;
		if(m < 0)
			mod += m;
		return mod;
	}
	
	/**
	 * Returns the matrix as a two-dimenional double array.
	 * @return matrix as double array
	 */
	public double[][] getMatrix() {
		double[][] matrix = new double[this.getRowCount()][this.getColumnCount()];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = this.get(i, j);
			}
		}
		return matrix;
	}
	
	
	
	
	
	/**
	 * Generates a magic square using the fitting algorithem for the given sidelength.
	 * @param sideLength
	 * @return
	 */
	public static Matrix MagicSquare(int sideLength) {
		if(sideLength == 1) {
			return new Matrix(new double[][] {{1}});
		} else if(sideLength == 2) {
			throw new IllegalArgumentException("Can not construct a magic square for the given sidelength");
		} else if(sideLength % 2 != 0) {
			return Matrix.magicSquarOdd(sideLength);
		} else if(sideLength % 4 == 0) {
			return Matrix.magicSquareDoubleEvenOrder(sideLength);
		} else if(sideLength % 4 == 2){
			return Matrix.magicSquareSinglyEvenOrder(sideLength);
		} else {
			throw new IllegalArgumentException("Can not construct a magic square for the given sidelength");
		}
	}
	
	/**
	 * Generates Magic squares for odd sidelengths
	 * @param sideLength
	 * @return
	 */
	private static Matrix magicSquarOdd(int sideLength) {
		Matrix i = Matrix.functionConstructer(sideLength, sideLength, (y, x) -> x + 1.0);
		Matrix j = Matrix.functionConstructer(sideLength, sideLength, (y, x) -> y + 1.0);
		Matrix a = i.add(j).add((sideLength - 3) / 2).mod(sideLength);
		Matrix b = i.add(j.mult(2)).subtract(2).mod(sideLength);
		return a.mult(sideLength).add(b).add(1);
	}
	
	
	/**
	 * Generates Magic squares for even sidelengths with (sidelength % 4 == 2)
	 * @param sideLength
	 * @return
	 */
	private static Matrix magicSquareSinglyEvenOrder(final int sideLength) {
        if (sideLength < 6 || (sideLength - 2) % 4 != 0)
            throw new IllegalArgumentException("base must be a positive "
                    + "multiple of 4 plus 2");
 
        int size = sideLength * sideLength;
        int halfN = sideLength / 2;
        int subSquareSize = size / 4;
 
        double[][] subSquare = Matrix.magicSquarOdd(halfN).getMatrix();
        int[] quadrantFactors = {0, 2, 3, 1};
        double[][] result = new double[sideLength][sideLength];
 
        for (int r = 0; r < sideLength; r++) {
            for (int c = 0; c < sideLength; c++) {
                int quadrant = (r / halfN) * 2 + (c / halfN);
                result[r][c] = subSquare[r % halfN][c % halfN];
                result[r][c] += quadrantFactors[quadrant] * subSquareSize;
            }
        }
 
        int nColsLeft = halfN / 2;
        int nColsRight = nColsLeft - 1;
 
        for (int r = 0; r < halfN; r++)
            for (int c = 0; c < sideLength; c++) {
                if (c < nColsLeft || c >= sideLength - nColsRight
                        || (c == nColsLeft && r == nColsLeft)) {
 
                    if (c == 0 && r == nColsLeft)
                        continue;
 
                    double tmp = result[r][c];
                    result[r][c] = result[r + halfN][c];
                    result[r + halfN][c] = tmp;
                }
            }
 
        return new Matrix(result);
    }
	
	/**
	 * Generates Magic squares for even sidelengths with (sidelength % 4 == 0)
	 * @param sideLength
	 * @return
	 */
	private static Matrix magicSquareDoubleEvenOrder(int sideLength) {
		double[][] arr = Matrix.functionConstructer(sideLength, sideLength, (y, x) -> (x + y * sideLength) + 1.0 ).getMatrix();
		int i, j;
		
        // change value of Array elements
        // at fix location as per rule 
        // (n*n+1)-arr[i][j]
        // Top Left corner of Matrix 
        // (order (n/4)*(n/4))
        for ( i = 0; i < sideLength/4; i++)
            for ( j = 0; j < sideLength/4; j++)
                arr[i][j] = (sideLength*sideLength + 1) - arr[i][j];
     
        // Top Right corsideLengther of Matrix 
        // (order (sideLength/4)*(sideLength/4))
        for ( i = 0; i < sideLength/4; i++)
            for ( j = 3 * (sideLength/4); j < sideLength; j++)
                arr[i][j] = (sideLength*sideLength + 1) - arr[i][j];
      
        // Bottom Left corsideLengther of Matrix
        // (order (sideLength/4)*(sideLength/4))
        for ( i = 3 * sideLength/4; i < sideLength; i++)
            for ( j = 0; j < sideLength/4; j++)
                arr[i][j] = (sideLength*sideLength+1) - arr[i][j];
     
        // Bottom Right corsideLengther of Matrix 
        // (order (sideLength/4)*(sideLength/4))
        for ( i = 3 * sideLength/4; i < sideLength; i++)
            for ( j = 3 * sideLength/4; j < sideLength; j++)
                arr[i][j] = (sideLength*sideLength + 1) - arr[i][j];
    
        // CesideLengthtre of Matrix (order (sideLength/2)*(sideLength/2))
        for ( i = sideLength/4; i < 3 * sideLength/4; i++)
            for ( j = sideLength/4; j < 3 * sideLength/4; j++)
                arr[i][j] = (sideLength*sideLength + 1) - arr[i][j];
        
        return new Matrix(arr);
	}
	
	/**
	 * Clones a two-dimensional array.
	 * @param arr
	 * @return
	 */
	private static double[][] arrClone(double[][] arr) {
		double[][] a = new double[arr.length][arr[0].length];
		
		for (int i = 0; i < a.length; i++) {
			a[i] = Arrays.copyOf(arr[i], arr[i].length);
		}
		return a;
	}

	/**
	 * Performs the equals operation of two matricies. The returnd value is the maximum diviation of two corosponding values of the matrix.
	 * Returns Double.POSITIVE_INFINITY if the matricies doesn't fit to each other.
	 * Return 0.0 of the matricies are euqal
	 * @param o
	 * @return
	 */
	public double equalsMaxDeviation(Object o) {
		if(o == null) return Double.POSITIVE_INFINITY;
		if(o == this) return 0;
		if(!(o instanceof Matrix)) return Double.POSITIVE_INFINITY;
		Matrix m = (Matrix) o;
		if(this.getColumnCount() != m.getColumnCount() || this.getRowCount() != m.getRowCount()) return Double.POSITIVE_INFINITY;
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		double maxDiviation = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				double div = Math.abs(this.get(i, j) - m.get(i, j));
				if(maxDiviation < div)
					maxDiviation = div;
				if(Double.isNaN(this.get(i, j)) && Double.isNaN(m.get(i, j) )) {
					return Double.POSITIVE_INFINITY;
				}
			}
		}
		return maxDiviation;
	}
	
	/**
	 * Performs the equals-operation by using double comparison with a threshold of 0.0000001
	 * @param o
	 * @return
	 */
	public boolean equalsDoubleCompare(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(!(o instanceof Matrix)) return false;
		Matrix m = (Matrix) o;
		if(this.getColumnCount() != m.getColumnCount() || this.getRowCount() != m.getRowCount()) return false;
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if(Math.abs(this.get(i, j) - m.get(i, j)) > 0.0000001)
					if(!(Double.isNaN(this.get(i, j)) && Double.isNaN(m.get(i, j))))
						return false;
			}
		}
		return true;
	}
	
	/**
	 * Performs the inharitated equals operation. It compares the exact values of the numbers.
	 * @parm o
	 * @return true if the two matricies are the equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(!(o instanceof Matrix)) return false;
		Matrix m = (Matrix) o;
		if(this.getColumnCount() != m.getColumnCount() || this.getRowCount() != m.getRowCount()) return false;
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if(this.get(i, j) != m.get(i, j))
					if(!(Double.isNaN(this.get(i, j)) && Double.isNaN(m.get(i, j))))
						return false;
			}
		}
		return true;
	}
	
	/**
	 * Clones the Matrix
	 * @return new Instance of Matrix with the same values as the current Matrix.
	 */
	@Override
	public Matrix clone() {
		double[][] m = new double[this.matrix.length][this.matrix[0].length];
		for (int i = 0; i < m.length; i++) {
				m[i] = Arrays.copyOf(this.matrix[i], this.matrix[0].length);
		}
		return new Matrix(m, this.transposed);
	}
	
	/**
	 * Converts the matrix to a String
	 * @return matrix as a string with "\t" seperated columns and "\n" seperated rows.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final int ROWS = this.getRowCount(), COLS = this.getColumnCount();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				sb.append(this.get(i, j));
				sb.append("\t");
			}
			if(i < this.matrix.length - 1)
				sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * determents whether a given matrix is a magic sqare.
	 * @param m
	 * @return
	 */
	public static boolean isMagicSquare(Matrix m) {
		final int ROWS = m.getRowCount(), COLS = m.getColumnCount();
		if(ROWS != COLS) return false;
		double[] sums = new double[2 * ROWS + 2];
		for (int i = 0; i < ROWS; i++) {
			sums[sums.length - 1] += m.get(i, i);
			sums[sums.length - 2] += m.get(i, COLS - i - 1);
			for (int j = 0; j < COLS; j++) {
				sums[i] += m.get(i, j);
				sums[ROWS + i] += m.get(j, i);
			}
		}
		
		for (int i = 1; i < sums.length; i++) {
			if(sums[i - 1] != sums[i])
				return false;
		}
		return true;
	}
	
	
	//Gaussian algorithem and the stuff to make my life easy...
	/**
	 * Performs the gaussioan Algorithem on the given matrix to convert it to the reduces step form.
	 * @param m
	 * @return a new matrix in reduced step form.
	 */
	public static Matrix gaussianAlgorithem(Matrix m) {
	    
		final int COLS = m.getColumnCount(), ROWS = m.getRowCount();
		double[][] a = m.getMatrix();
		int row = 0;

	    for (int col = 0; col < COLS - 1; ++col) {
	        if (a[row][col] != 0) {
	            for (int i = COLS - 1; i > col - 1; --i) {
	                a[row][i] /= a[row][col];
	            }

	            for (int i = 0; i < ROWS; ++i) {
	                for (int j = COLS - 1; j > col - 1; --j) {
	                	if(i != row)
	                		a[i][j] -= a[i][col] * a[row][j];
	                }
	            }

	            row++;
	            if(row >= ROWS)
	            	break;
	        }
	    }
	    return new Matrix(a);
	}
	
	
	/**
	 * Swaps two given rows.
	 * @param Matrix m
	 * @param i
	 * @param j
	 * @return New Matrix with the rows i and j swapped
	 */
	public static Matrix swapRow(Matrix m, int i, int j) {
		if(!m.transposed)
			return new Matrix(Matrix.swapRow(m.matrix, i, j));
		return new Matrix(Matrix.swapColumn(m.matrix, i, j));
	}
	
	private static double[][] swapRow(double[][] matrix, int i, int j) {
		if(i < 0 || i >= matrix.length || j < 0 || j >= matrix.length) throw new IndexOutOfBoundsException("Given i or j are not in range of the Matrix" + i + " " + j);
		
		double[][] m = new double[matrix.length][matrix[0].length];
		for (int k = 0; k < m.length; k++) {
			m[i] = Arrays.copyOf(matrix[k], matrix[0].length);
		}
		
		double[] temp = m[i];
		m[i] = m[j];
		m[j] = temp;
		return m;
	}
	
	/**
	 * Swaps two given columns.
	 * @param Matrix m
	 * @param i
	 * @param j
	 * @return New Matrix with the columns i and j swapped
	 */
	public static Matrix swapColumn(Matrix m, int i, int j) {
		if(!m.transposed)
			return new Matrix(Matrix.swapColumn(m.matrix, i, j));
		return new Matrix(Matrix.swapRow(m.matrix, i, j));
	}
	
	private static double[][] swapColumn(double[][] matrix, int i, int j) {
		if(i < 0 || i >= matrix[0].length || j < 0 || j >= matrix[0].length) throw new IndexOutOfBoundsException("Given i or j are not in range of the Matrix" + i + " " + j);
		
		double[][] m = new double[matrix.length][matrix[0].length];
		for (int k = 0; k < m.length; k++) {
			for (int l = 0; l < m[0].length; l++) {
				if(l == i)
					m[k][j] = matrix[k][l];
				else if(l == j)
					m[k][i] = matrix[k][l];
				else
					m[k][l] = matrix[k][l];
			}
		}
		return m;
	}
	
	@SuppressWarnings("unused")
	private static double[][] rowConcut(double[][] arr1, double[][] arr2) {
		double[][] res = new double[arr1.length + arr2.length][0];
		for (int i = 0; i < res.length; i++) {
			if(i < arr1.length)
				res[i] = Arrays.copyOf(arr1[i], arr1[i].length);
			else
				res[i] = Arrays.copyOf(arr2[i - arr1.length], arr2[i - arr1.length].length);
		}
		return res;
	}
	
	private static double[][] columnConcut(double[][] arr1, double[][] arr2) {
		double[][] res = new double[(int)Math.max(arr1.length, arr2.length)][0];
		for (int i = 0; i < res.length; i++) {
			if(i < arr1.length && i < arr2.length)
				res[i] = Matrix.concut(arr1[i], arr2[i]);
			else if(i >= arr1.length && i < arr2.length)
				res[i] = Arrays.copyOf(arr2[i], arr2[i].length);
			else if(i < arr1.length && i >= arr2.length)
				res[i] = Arrays.copyOf(arr1[i], arr1[i].length);
		}
		return res;
	}
	
	private static double[] concut(double[] arr1, double[] arr2) {
		double[] res = Arrays.copyOf(arr1, arr1.length + arr2.length);
		for (int i = arr1.length; i < res.length; i++) {
			res[i] = arr2[i - arr1.length];
		}
		return res;
	}
	
	//end of gaussian algorithem stuff
	
	/**
	 * Converts a Matrix to a Vector.
	 * If the Matrix contains more than one column, the first column will be converted to the Vector
	 * @param m
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Vector matrixToVector(Matrix m) {
		final int ROWS = m.getRowCount();
		double[] vector = new double[ROWS];
		for (int i = 0; i < ROWS; i++) {
			vector[i] = m.get(i, 0);
		}
		return new Vector(vector);
	}
	
	/**
	 * Converts a given Vector to a n x 1 marix.
	 * @param v
	 * @return v as matrix
	 */
	@SuppressWarnings("unused")
	private static Matrix vectorToColumnMatrix(Vector v) {
		double[][] matrix = new double[v.getDimenion()][1];
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][0] = v.get(i);
		}
		return new Matrix(matrix);
	}
	
	/**
	 * Converts a given Vector to a n x 1 marix.
	 * @param v
	 * @return v as matrix
	 */
	@SuppressWarnings("unused")
	private static Matrix vectorToRowMatrix(Vector v) {
		double[][] matrix = new double[1][v.getDimenion()];
		for (int i = 0; i < matrix[0].length; i++) {
			matrix[0][i] = v.get(i);
		}
		return new Matrix(matrix);
	}
	
	/**
	 * Ensures the dimensionality for both matricies for matrix multiplication
	 * m2.getColumnCount() == m2.getRowCount()
	 * 
	 * Otherwise it throws an DimensionError.
	 * 
	 * @param m1
	 * @param m2
	 * @return int[]. Dimensions of the resulting matrix after matrix multiplication of m1 and m2 (new int[] {m1.getRowCount(), m2.getColumnCount()})
	 */
	private static int[] ensueMultDimensionality(Matrix m1, Matrix m2) {
		if(m1.getColumnCount() != m2.getRowCount())
			throw new DimensionError("The columncount of m1 and the rowcount of m2 need to be the same. Given: " + m1.getRowCount() + "x" + m1.getColumnCount() + " and " + m2.getRowCount() + "x" + m2.getColumnCount());
		return new int[] {m1.getRowCount(), m2.getColumnCount()};
	}
	
	/**
	 * ensures the dimensionality for both matricies for matrix addition, substraction etc.
	 * m1.getColumnCount() == m2.getColumnCount() && m1.getRowCount() == m2.getRowCount()
	 * 
	 * Otherwise it throws an DimensionError.
	 * 
	 * @param m1
	 * @param m2
	 * @return new int[] {m1.getRowCount(), m1.getColumnCount()}
	 */
	private static int[] ensureSameDimensionality(Matrix m1, Matrix m2) {
		if(m1.getColumnCount() != m2.getColumnCount() || m1.getRowCount() != m2.getRowCount())
			throw new DimensionError("The dimensions of the matricies need to be the same. Given: " + m1.getRowCount() + "x" + m1.getColumnCount() + " and " + m2.getRowCount() + "x" + m2.getColumnCount());
		return new int[] {m1.getRowCount(), m1.getColumnCount()};
	}
	
	/**
	 * Repesents Excepions in terms of Matrix dimensionality.
	 * @author Schule
	 */
	@SuppressWarnings("serial")
	private static class DimensionError extends RuntimeException {
		public DimensionError(String msg) {
			super(msg);
		}
	}
	
}
