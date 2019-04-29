package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Slime extends Enemy{
    
    private BufferedImage[] sprites;
    
    public Slime(TileMap tm) {
        super(tm);
        
        moveSpeed = 0.3;
        maxSpeed = 0.3;
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
                            "/Resources/Sprites/Enemies/slimetest.png")
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
    }
    
    private void getNextPosition() {

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
        //Falling
        if(falling) {
            dy += fallSpeed;
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
        
        // update animation
        animation.update();
    }
    
    public void draw(Graphics2D g) {
        
        //if(notOnScreen()) return;
        
        setMapPosition();
        
        super.draw(g);
    }
}
