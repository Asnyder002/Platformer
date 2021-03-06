package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import TileMap.Background;

//Subclass of GameState that show the menu of the game.
public class GameOverState extends GameState {
    
    //Variables
    private Background bg;
    
    //Spot in menu(array)
    private int currentChoice = 0;
    
    //Array with the options for the menu.
    private String[] options = {
        "Play Again",
        "Help",
        "Quit"
    };
    
    private Font font;
    
    //Constructor 
    public GameOverState(GameStateManager gsm) {
        
        //Sets argument to this current class.
        this.gsm = gsm;
        
        //Sets up background image and menu look.
        try {
            bg = new Background("/Resources/Backgrounds/gameOver.jpg", 1);
            bg.setVector(-.01, 0);
            font = new Font("Arial", Font.PLAIN, 12);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    public void init() {
        
    }
    
    //Method call from Background class.
    public void update() {
        bg.update();
    }
    
    //Actually places background and option on screen.
    public void draw(Graphics2D g) {
        
        //draw bg
        bg.draw(g);
        
        //draw menu options
        g.setFont(font);
        for(int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(Color.RED);
            }
            else {
                g.setColor(Color.YELLOW);
            }
            g.drawString(options[i], 135, 165 + i * 15);
        }
    }
    
    //What happens when menu items are selected.
    private void select() {
        if(currentChoice == 0) {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 1) {
            //help
        }
        if(currentChoice == 2) {
            System.exit(0);
        }
    }
    
    //What happens when enter, up, and down are pressed.
    public void keyPressed(int k) {
        
        if(k == KeyEvent.VK_ENTER) {
            select();
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                currentChoice = 0;
            }
        }
        
    }
    
    public void keyReleased(int k) {
        
    }
    
    
}
