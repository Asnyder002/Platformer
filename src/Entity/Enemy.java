package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
    
    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    
    protected boolean flinching;
    protected long flinchTimer;
    
    // Constructor that invokes constructor from MapObject
    public Enemy(TileMap tm) {
        super(tm);
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public int getDamage() {
        return damage;
    }
    
    // If character is dead or flinching do nothing. Otherwise, remove damage 
    // taken from health. If damage was fatal it is dead otherwise it will
    // flinch
    public void hit(int damage) {
        if(dead || flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }
    
    public void update() {
        
    }
}
