package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD {
    
    private Player player;
    private BufferedImage image;
    private Font font;
    
    // Sets player object equal to this player and loads in HUG spirte.
    public HUD(Player p) {
        player = p;
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream(
            "/Resources/HUD/hud.gif"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    // Draws the HUD onto the game and calculates the health and fire to display
    public void draw(Graphics2D g) {
        g.drawImage(image, 0, 10, null);
        g.setFont(font);
        g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
        g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 45);
                
    }
    
}
