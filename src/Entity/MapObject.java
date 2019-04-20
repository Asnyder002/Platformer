
package Entity;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;
import java.awt.Rectangle;

public abstract class MapObject {
    
    // Tile
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;
    
    // Posistions and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    
    // Dimensions (reading in sprites)
    protected int width;
    protected int height;
    
    // Collision box
    protected int cwidth;
    protected int cheight;
    
    // Collision four cornor method
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;
    
    // Animations
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;
    
    // Movement. What the object is actually doing.
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;
    
    // Movement attributes, physics
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;
    
    // Constructor
    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tm.getTileSize();
        
    }
    
    // Creates two rectangles and tells if the two have collided
    public boolean intersects(MapObject o) {
        Rectangle r1 = getRectangle();             
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }
    
    // Creates rectangle
    public Rectangle getRectangle() {
        return new Rectangle(
                (int)x - cwidth,
                (int)y - cheight,
                cwidth,
                cheight
        );
    }
    
    // The - 1 is for not stepping into the next col/row
    public void calculateCorners(double x, double y) {
	int leftTile = (int)(x - cwidth / 2) / tileSize;
	int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
	int topTile = (int)(y - cheight / 2) / tileSize;
	int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
        
	if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
		leftTile < 0 || rightTile >= tileMap.getNumCols()) {
		topLeft = topRight = bottomLeft = bottomRight = false;
		return;
	}
        
        // Figures type of tiles for corners
	int tl = tileMap.getType(topTile, leftTile);
	int tr = tileMap.getType(topTile, rightTile);
	int bl = tileMap.getType(bottomTile, leftTile);
	int br = tileMap.getType(bottomTile, rightTile);
	topLeft = tl == Tile.BLOCKED;
	topRight = tr == Tile.BLOCKED;
	bottomLeft = bl == Tile.BLOCKED;
	bottomRight = br == Tile.BLOCKED;
}
    
    // Checks for blocked or normal tile.
    public void checkTileMapCollision() {
        //Current Positions
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;
        
        //Destination Posistions
        xdest = x + dx;
        ydest = y + dy;
        
        //Temp values for orginial x and y
        xtemp = x;
        ytemp = y;
        
        // Y direction up
        calculateCorners(x, ydest);
        if(dy < 0) {
            if(topLeft || topRight) {
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            }
            else {
                ytemp += dy;
            }
        }
        
        // Y direction down
        if(dy > 0) {
            if(bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            }
            else {
                ytemp += dy;
            }
        }
        
        // X direction left
        calculateCorners(xdest, y);
        if(dx < 0) {
            if(topLeft || bottomLeft) {
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2;
            }
            else {
                xtemp += dx;
            }
        }
        
        // X direction right
        if(dx > 0){
            if(topRight || bottomRight) {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            }
            else {
                xtemp += dx;
            }
        }
        
        // Falling off map
        if(!falling) {
            calculateCorners(x, ydest + 1);
            if(!bottomLeft && !bottomRight) {
                falling = true;
            }
        }
    }
    
    public int getx() {
        return (int)x;
    }
    
    public int gety() {
        return (int)y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getCWidth() {
        return cwidth;
    }
    
    public int getCHeight() {
        return cheight;
    }
    
    // Regular position. Where the player is globally.
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    // Directional position
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    // Map position. Where the player is relative to how far the tileMap moved
    public void setMapPosition() {
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }
    
    public void setLeft(boolean b) {
        left = b;
    }
    
    public void setRight(boolean b) {
        right = b;
    }
    
    public void setUp(boolean b) {
        up = b;
    }
    
    public void setDown(boolean b) {
        down = b;
    }
    
    public void setJumping(boolean b) {
        jumping = b;
    }
    
    //Checks object if on screen or not
    public boolean notOnScreen() {
        return x + xmap + width < 0 ||
               x + xmap - width > GamePanel.WIDTH ||
               y + ymap + height < 0 ||
               y + ymap - height > GamePanel.HEIGHT;
    }
    
    // Draws object 
    public void draw(java.awt.Graphics2D g) {
        if(facingRight) {
            g.drawImage(
                animation.getImage(),
                (int)(x + xmap - width / 2),
                (int)(y + ymap - height / 2),
                null);
        }
        else {
            g.drawImage(
                animation.getImage(),
                (int)(x + xmap - width / 2 + width),
                (int)(y + ymap - height / 2),
                -width,
                height,
                null);
        }
    }
}
