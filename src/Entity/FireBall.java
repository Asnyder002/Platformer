package Entity;

import java.awt.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FireBall extends MapObject {
    
    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    
    // Constructor that invokes super class constructor from MapObject. 
    public FireBall(TileMap tm, boolean right) {
        
        super(tm);
        
        // Sets acutal and flag boolean =
        facingRight = right;
        
        // Sets move speed and actual direction right or left
        moveSpeed = 3.8;
        if(right){
            dx = moveSpeed;
        }
        else {
            dx = -moveSpeed;
        }
        
        // Size for reading in sprite(width/height) and
        // collsion size
        width = 30;
        height = 30;
        cwidth = 14;
        cheight = 14;
        
        //Loading Sprite
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Resources/Sprites/Player/"
                            + "fireball.gif"));
            
            sprites = new BufferedImage[4];
            for(int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                    i * width,
                    0,
                    width,height);
            }
            
            hitSprites = new BufferedImage[3];
            for(int i = 0; i < hitSprites.length; i++) {
                hitSprites[i] = spritesheet.getSubimage(
                    i * width,
                    height,
                    width,
                    height);
            }
            
            // Creates new Animation object and sets up the frames.
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // If fireball already hit do nothing. Otherwise, sets up hitanimation
    // and sets hit boolean to true.
    public void setHit() {
        if(hit){
            return;
        }
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dx = 0;
    }
    
    // For removing current fireball object.
    public boolean shouldRemove() {
        return remove;
    }
    
    public void update() {
        
        // Checks for collision and set the current position of fireball
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        // If fireball isn't moving and hit isn't true calls setHit function.
        if(dx == 0 && !hit) {
            setHit();
        }
        
        // Plays animation and if it has hit something and then animation has
        // played once then remove fireball
        animation.update();
        if(hit && animation.hasPlayedOnce()) {
            remove = true;
        }
    }
    
    // Draws the fireball.
    public void draw(Graphics2D g) {
        
        setMapPosition();
        
        super.draw(g);
        
    }
    
}
