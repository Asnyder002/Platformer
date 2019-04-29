package Main;

import GameState.GameStateManager;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

//The JPanel and what it displays
public class GamePanel extends JPanel implements Runnable, KeyListener {
    
    //Dimensions
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;
    
    //Game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;
    
    //Image
    private BufferedImage image;
    private Graphics2D g;
    
    //Manages the state of the game
    private GameStateManager gsm;
    
    //Constructor 
    public GamePanel() {
        
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
        
    }
    
    //Makes container displayable by connecting to native screen resource.
    //Called internally by toolkit, do not call
    public void addNotify() {
        
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
        
    }
    
    //Creates a BufferedImage class and gets a grpahic and set it to g
    //Set running flag to true and creates new Game State Manager
    private void init() {
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;
        
        gsm = new GameStateManager();
        
    }
    
    //Called when addNotify is implicitly called abd the thread starts.
    public void run() {
        
        //Calls initialize method
        init();
        
        //Variables for game loop
        long start;
        long elapsed;
        long wait;
        
        //game loop
        while(running) {
            start = System.nanoTime();
            
            update();
            draw();
            drawToScreen();
            
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            
            if(wait < 0) wait = 5;
            
            try {
                Thread.sleep(wait);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    //Updates game current state in array
    private void update() {
        gsm.update();
    }
    
    //Pulls the correct image from gamestate.
    private void draw() {
        gsm.draw(g);
    }
    
    //Using getGraphics places the image you pulled from draw and places it in
    //a new variable and puts it on the screen and then gets rid of it from 
    //memory
    private void drawToScreen() {
        
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
        
    }
    
    public void keyTyped(KeyEvent key) {
        
    }
    
    //Nethod called from manager that calls the method from current state.
    public void keyPressed(KeyEvent key) {
        gsm.keyPressed(key.getKeyCode());
    }
    //Nethod called from manager that calls the method from current state.
    public void keyReleased(KeyEvent key) {
        gsm.keyReleased(key.getKeyCode());
    }
}
