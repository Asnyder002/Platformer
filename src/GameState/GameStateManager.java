package GameState;

import java.util.ArrayList;

//Class that controls the states of the game
public class GameStateManager {
    
    //Variables for arrayList
    private GameState[] gameStates;
    private int currentState;
    
    //States of the game in arrayList
    public static final int NUMGAMESTATES = 3;
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    public static final int GAMEOVERSTATE = 2;
    
    //Constructor that creates game state
    public GameStateManager() {
        
        gameStates = new GameState[NUMGAMESTATES];
        
        //Sets current state to menu and populates gamestates
        currentState = MENUSTATE;
        loadState(currentState);
        
    }
    
    // Loads in correct state when needed
    private void loadState(int state) {
        if(state == MENUSTATE) {
            gameStates[state] = new MenuState(this);
        }
        if(state == LEVEL1STATE) {
            gameStates[state] = new Level1State(this);
        }
        if(state == GAMEOVERSTATE) {
            gameStates[state] = new GameOverState(this);
        }
    }
    
    // Unloads state to free memory
    public void unloadState(int state) {
        gameStates[state] = null;
    }
    
    //Method for setting current state and initializes it.
    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
        //gameStates[currentState].init();
        
    }
    
    //Updatae from current state.
    public void update() {
        try {
            gameStates[currentState].update();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Pulls graphic from current state.
    public void draw(java.awt.Graphics2D g) {
        try {
            gameStates[currentState].draw(g);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //Key fuction when pressed for current state.
    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }
    
    //Key function when released for current state.
    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }
}
