import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TetrisBoard {

	private final int BLOCK_SIZE = 40;

	private final int BOARD_WIDTH_IN_PIXELS = 400;
	private final int BOARD_HEIGHT_IN_PIXELS = 800;

	private final int BOARD_WIDTH_IN_BLOCKS = 10;
	private final int BOARD_HEIGHT_IN_BLOCKS = 20;

	private Game game;

	private int[][] board = new int[BOARD_HEIGHT_IN_BLOCKS][BOARD_WIDTH_IN_BLOCKS];
	private Shape[] shapes = new Shape[7];

	private Shape currentShape;

	private int xStart, yStart, xEnd, yEnd;
	
	private boolean gameOver;

	private BufferedImage imageBlocks;

	public TetrisBoard(Game game) {
		this.game = game;

		//Start and end coordinates for the Tetrisboard
		xStart = game.getWidth() / 2 - (BOARD_WIDTH_IN_PIXELS / 2);
		xEnd = game.getWidth() / 2 + (BOARD_WIDTH_IN_PIXELS / 2);
		yStart = game.getHeight() / 2 - (BOARD_HEIGHT_IN_PIXELS / 2);
		yEnd = game.getHeight() / 2 + (BOARD_HEIGHT_IN_PIXELS / 2);

		try {
			imageBlocks = ImageIO.read(TetrisBoard.class.getResource("/blocks.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		shapes[0] = new Shape(imageBlocks.getSubimage(0, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{1, 1, 1, 1} //I-shape
		}, this, 1);

		shapes[1] = new Shape(imageBlocks.getSubimage(BLOCK_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{1, 1, 0}, 
			{0, 1, 1}	//Z-shape
		}, this, 2);

		shapes[2] = new Shape(imageBlocks.getSubimage(BLOCK_SIZE * 2, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{0, 1, 1}, 
			{1, 1, 0}	//S-shape
		}, this, 3);

		shapes[3] = new Shape(imageBlocks.getSubimage(BLOCK_SIZE * 3, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{0, 0, 1}, 
			{1, 1, 1}	//J-shape
		}, this, 4);

		shapes[4] = new Shape(imageBlocks.getSubimage(BLOCK_SIZE * 4, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{1, 1, 1}, 
			{0, 0, 1}	//L-shape
		}, this, 5);

		shapes[5] = new Shape(imageBlocks.getSubimage(BLOCK_SIZE * 5, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{1, 1, 1}, 
			{0, 1, 0}	//T-shape
		}, this, 6);

		shapes[6] = new Shape(imageBlocks.getSubimage(BLOCK_SIZE * 6, 0, BLOCK_SIZE, BLOCK_SIZE), new int[][] {
			{1, 1}, 
			{1, 1}	//O-shape
		}, this, 7);

		createNewShape();
	
	}

	public void update() {

		currentShape.update();
		
		if(gameOver)
			System.exit(0);

	}

	public void render(Graphics g) {

		g.fillRect(xStart, yStart, BOARD_WIDTH_IN_PIXELS, BOARD_HEIGHT_IN_PIXELS);

		currentShape.render(g);
		
		for(int row = 0; row < board.length; row++)
			for(int col = 0; col < board[row].length; col++)
				if(board[row][col] != 0)
					g.drawImage(imageBlocks.getSubimage((board[row][col]-1)*BLOCK_SIZE, 0, BLOCK_SIZE, BLOCK_SIZE),
					xStart + col * BLOCK_SIZE, yStart + row * BLOCK_SIZE, null);
		
		
		g.setColor(Color.white);

		for(int i = 0; i < BOARD_HEIGHT_IN_BLOCKS; i++) {

			g.drawLine(xStart, yStart + (BLOCK_SIZE * i), xEnd, yStart + (BLOCK_SIZE * i));
		}

		for(int i = 0; i < BOARD_WIDTH_IN_BLOCKS; i++) {

			g.drawLine(xStart + (i * BLOCK_SIZE), yStart, xStart + (i * BLOCK_SIZE), yEnd);
		}

	}

	public void createNewShape(){

		int index = (int)(Math.random()*shapes.length);

		Shape shape = new Shape(shapes[index].getBlock(), shapes[index].getCoordinates(),
				this, shapes[index].getColor());

		currentShape = shape;
		game.getKeyManager().setCurrentShape(currentShape);

		for(int row = 0; row < currentShape.getCoordinates().length; row++)
			for(int col = 0; col < currentShape.getCoordinates()[row].length; col++)
				if(currentShape.getCoordinates()[row][col] != 0){

					if(board[row][col + 3] != 0)
						gameOver = true;
				}

	}

	public int getBLOCK_SIZE() {
		return BLOCK_SIZE;
	}

	public int getxStart() {
		return xStart;
	}

	public int getyStart() {
		return yStart;
	}

	public int getxEnd() {
		return xEnd;
	}

	public int getyEnd() {
		return yEnd;
	}

	public Shape getCurrentShape() {
		return currentShape;
	}

	public int[][] getBoard(){
		return board;
	}

}
