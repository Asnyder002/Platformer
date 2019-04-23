package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.util.Random;

public class Ghost extends Enemy{
    
    private BufferedImage[] sprites;
    
    public Ghost(TileMap tm) {
        super(tm);
        
        moveSpeed = 0.6;
        maxSpeed = 0.9;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        
        width = 30;
        height = 30;
        cwidth = 15;
        cheight = 15;
        
        health = maxHealth = 2;
        damage = 1;
        
        // Loading sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Resources/Sprites/Enemies/ghostTest.png")
            );
            
            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);
        
        right = true;
        facingRight = true;
        down = true;
    }
    
    private void getNextPosition() {

        Random rand = new Random();
        int num = rand.nextInt(2);
        
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        
        if (down) {
            dy += moveSpeed;
            if (dy > moveSpeed) {
                dy = moveSpeed;
            }
        } else if (up) {
            dy -= moveSpeed;
            if (dy < -moveSpeed) {
                dy = -moveSpeed;
            }
        }
        
        
    }
    
    public void update() {
        
        // Update posistion
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        // Check Flinching
        if(flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400) {
                flinching = false;
            }
        }
        
        // If hits wall move other direction
        if(right && dx == 0) {
            right = false;
            left = true;
            facingRight = false;
        }
        else if(left && dx == 0) {
            right = true;
            left = false;
            facingRight = true;
        }
        
        if(down && dy == 0) {
            down = false;
            up = true;
        }
        else if(up && dy == 0) {
            up = false;
            down = true;
        }
        
        
        
        // update animation
        animation.update();
    }
    
    public void draw(Graphics2D g) {
        
        //if(notOnScreen()) return;
        
        setMapPosition();
        
        super.draw(g);
    }
}
