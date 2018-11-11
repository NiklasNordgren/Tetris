import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {

	private Display display;
	private TetrisBoard tetrisBoard;
	private KeyManager keyManager;
	
	private BufferStrategy bs;
	private Graphics g;
	
	private boolean running = false;
	private Thread thread;

	public Game() {

	}
	
	private void initiate() {
		
		keyManager = new KeyManager();
		
		display = new Display();
		display.getFrame().addKeyListener(keyManager);
		
		tetrisBoard = new TetrisBoard(this);
		
	}
	
	private void update() {
		tetrisBoard.update();
	}
	
	
	private void render() {
		
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear screen
		g.clearRect(0, 0, display.getFrame().getWidth(), display.getFrame().getHeight());
		//Draw
	
		tetrisBoard.render(g);
		
		//End Drawing
		bs.show();
		g.dispose();
	}
	
	@Override
	public void run() {
		
		initiate();
		
		int fps = 60;
		double timePerUpdate = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();

		while(running) {
			
			now = System.nanoTime();
			delta += (now - lastTime) / timePerUpdate;
			lastTime = now;
			
			if(delta >= 1) {
				update();
				render();
				
				delta--;
			}
			
		}
		
		stop();
	}

	public synchronized void start() {
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return display.getFrame().getWidth();
	}

	public int getHeight() {
		return display.getFrame().getHeight();
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

}
