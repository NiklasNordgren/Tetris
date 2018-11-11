import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shape {

	private BufferedImage block;
	private int[][] coordinates;
	private TetrisBoard tetrisBoard;

	private int deltaX = 0;
	private int x, y;

	private boolean collision = false;
	private boolean xMove = true;

	private int color;

	private long time, lastTime;
	private int speed = 500000000;

	public Shape(BufferedImage block, int[][] coordinates, TetrisBoard tetrisBoard, int color) {
		this.block = block;
		this.coordinates = coordinates;
		this.tetrisBoard = tetrisBoard;
		this.color = color;

		x = 4;
		y = 0;
	}

	public void update() {

		if(collision)
			collision();
		else {
			moveX();
			moveY();
		}

	}

	public void render(Graphics g) {

		for(int row = 0; row < coordinates.length; row++) {
			for(int col = 0; col < coordinates[row].length; col++) {
				if(coordinates[row][col] != 0)
					g.drawImage(block, tetrisBoard.getxStart() + col * tetrisBoard.getBLOCK_SIZE() + x * tetrisBoard.getBLOCK_SIZE(),
							tetrisBoard.getyStart() + row * tetrisBoard.getBLOCK_SIZE() + y * tetrisBoard.getBLOCK_SIZE(), null);
			}
		}

	}

	private void moveX() {

		if(!(x + deltaX + coordinates[0].length > 10) && !(x + deltaX < 0)) {

			for(int row = 0; row < coordinates.length; row++)
				for(int col = 0; col < coordinates[row].length; col++)
					if(coordinates[row][col] != 0)
					{
						if(tetrisBoard.getBoard()[y + row][x + deltaX + col] != 0)
							xMove = false;
					}

			if(xMove)
				x += deltaX;

		}

		deltaX = 0;
		xMove = true;

	}

	private void moveY() {

		time += System.nanoTime() - lastTime;

		if(y + 1 + coordinates.length <= 20) {

			for(int row = 0; row < coordinates.length; row++)
				for(int col = 0; col < coordinates[row].length; col++)
					if(coordinates[row][col] != 0)
					{
						if(tetrisBoard.getBoard()[y + row + 1][col + x] != 0)
							collision = true;
					}

			if(time > speed) {
				y++;
				time = 0;
			}

		}else {
			collision = true;
		}

		lastTime = System.nanoTime();

	}

	private void collision() {

		for(int row = 0; row < coordinates.length; row++)
			for(int col = 0; col < coordinates[row].length; col++)
				if(coordinates[row][col] != 0)
					tetrisBoard.getBoard()[y + row][x + col] = color;

		checkLine();
		tetrisBoard.createNewShape();
	}


	private void checkLine(){
		int height = tetrisBoard.getBoard().length - 1;

		for(int i = height; i > 0; i--){

			int counter = 0;
			for(int j = 0; j < tetrisBoard.getBoard()[0].length; j++){

				if(tetrisBoard.getBoard()[i][j] != 0)
					counter ++;

				tetrisBoard.getBoard()[height][j] = tetrisBoard.getBoard()[i][j];

			}
			if(counter < tetrisBoard.getBoard()[0].length)
				height --;

		}

	}

	public void rotate() {

		if(collision)
			return;

		int[][] rotatedMatrix = null;

		rotatedMatrix = getTranspose(coordinates);

		rotatedMatrix = getReverseMatrix(rotatedMatrix);

		if(x + rotatedMatrix[0].length > 10 || y + rotatedMatrix.length > 20)
			return;

		for(int row = 0; row < rotatedMatrix.length; row++)
			for(int col = 0; col < rotatedMatrix[0].length; col++)
				if(tetrisBoard.getBoard()[y + row][x + col] != 0)
					return;

		coordinates = rotatedMatrix;

	}

	private int[][] getTranspose(int[][] matrix){
		int[][] newMatrix = new int[matrix[0].length][matrix.length];

		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[0].length; j++)
				newMatrix[j][i] = matrix[i][j];

		return newMatrix;

	}

	private int[][] getReverseMatrix(int[][] matrix){
		int middle = matrix.length / 2;

		for(int i = 0; i < middle; i++){
			int[] m = matrix[i];
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = m;
		}
		
		return matrix;
		
	}
	
	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}
	
	public void moveYFaster(boolean tof) {
		if(tof)
			speed = 50000000;
		else
			speed = 500000000;
	}

	public BufferedImage getBlock() {
		return block;
	}

	public int[][] getCoordinates() {
		return coordinates;
	}

	public int getColor() {
		return color;
	}

}
