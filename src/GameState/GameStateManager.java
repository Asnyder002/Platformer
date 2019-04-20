package GameState;

import java.util.ArrayList;

//Class that controls the states of the game
public class GameStateManager {
    
    //Variables for arrayList
    private ArrayList<GameState> gameStates;
    private int currentState;
    
    //States of the game in arrayList
    public static final int MENUSTATE = 0;
    public static final int LEVEl1STATE = 1;
    
    //Constructor that creates arrayList
    public GameStateManager() {
        
        gameStates = new ArrayList<GameState>();
        
        //Sets current state to menu and populates gamestates
        currentState = MENUSTATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));
        
    }
    
    //Method for setting current state and initializes it.
    public void setState(int state) {
        
        currentState = state;
        gameStates.get(currentState).init();
        
    }
    
    //Updatae from current state.
    public void update() {
        
        gameStates.get(currentState).update();
        
    }
    
    //Pulls graphic from current state.
    public void draw(java.awt.Graphics2D g) {
        
        gameStates.get(currentState).draw(g);
        
    }
    
    //Key fuction when pressed for current state.
    public void keyPressed(int k) {
        gameStates.get(currentState).keyPressed(k);
    }
    
    //Key function when released for current state.
    public void keyReleased(int k) {
        gameStates.get(currentState).keyReleased(k);
    }
}
