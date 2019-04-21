package GameState;

import java.awt.*;
import TileMap.*;
import java.util.HashSet;
import java.util.Set;
import Main.GamePanel;
import Entity.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Entity.Enemies.*;

public class Level1State extends GameState{
    
    private TileMap tileMap;
    private Background bg;
    
    private Player player;
    
    private ArrayList<Enemy> enemies;
    
    private HUD hud;
    
    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }
    
    public void init() {
        
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Resources/Tilesets/snowIceTiles.gif");
        tileMap.loadMap("/Resources/Maps/newSnow.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);
        
        //New background
        bg = new Background("/Resources/Backgrounds/ice.jpg", 0.1);
        
        //Player
        player = new Player(tileMap);
        player.setPosition(100, 100);
        
        enemies = new ArrayList<Enemy>();
        
        Slugger s;
        s = new Slugger(tileMap);
        s.setPosition(100, 100);
        enemies.add(s);
        
        hud = new HUD(player);
        
    }
    
    public void update(){
        
        //Updates player
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getx(),
                GamePanel.HEIGHT / 2 - player.gety());
        
        //set background
        bg.setPosition(tileMap.getx(), tileMap.gety());
        
        // Attack enemies
        player.checkAttack(enemies);
        
        //update all enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
            if(enemies.get(i).isDead()) {
                enemies.remove(i);
                i--;
            }
        }
        
    }
    
    public void draw(java.awt.Graphics2D g){
        
        //draw bg
        bg.draw(g);
        
        //draw tilemap
        tileMap.draw(g);
        
        //draw player
        player.draw(g);
        
        //draw enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        
        //draw hug
        hud.draw(g);
    
    }
    
    public void keyPressed(int k){
        if(k == KeyEvent.VK_A) player.setLeft(true);
	if(k == KeyEvent.VK_D) player.setRight(true);
        if(k == KeyEvent.VK_W) player.setUp(true);
        if(k == KeyEvent.VK_S) player.setDown(true);
        if(k == KeyEvent.VK_SPACE) player.setJumping(true);
        if(k == KeyEvent.VK_Q) player.setStriking();
        if(k == KeyEvent.VK_E) player.setFiring();
    }
    
    public void keyReleased(int k){
        if(k == KeyEvent.VK_A) player.setLeft(false);
        if(k == KeyEvent.VK_D) player.setRight(false);
        if(k == KeyEvent.VK_W) player.setUp(false);
        if(k == KeyEvent.VK_S) player.setDown(false);
        if(k == KeyEvent.VK_SPACE) player.setJumping(false);
    }
    
}
