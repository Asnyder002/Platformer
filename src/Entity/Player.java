package Entity;

import TileMap.*;
import Audio.AudioPlayer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {

    //Variables
    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    //fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    private ArrayList<FireBall> fireBalls;

    //scratch
    private boolean striking;
    private int strikeDamage;
    private int strikeRange;

    //Animations;
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
        2, 4, 1, 2, 5, 4, 3
    };

    //Animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int DYING = 4;
    private static final int FIREBALL = 5;
    private static final int STRIKING = 6;

    private HashMap<String, AudioPlayer> sfx;

    public Player(TileMap tm) {
        super(tm);

        //reading in the tilesheet height
        width = 30;
        height = 30;

        //actual height drawn
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        fire = maxFire = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        fireBalls = new ArrayList<FireBall>();

        strikeDamage = 3;
        strikeRange = 40;

        //Loads in spritesheet
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Player/blackMage.gif"));

            sprites = new ArrayList<BufferedImage[]>();

            for (int i = 0; i < 7; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {

                    if (i != STRIKING) {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height);
                    } else {
                        bi[j] = spritesheet.getSubimage(
                                j * width * 2,
                                i * height,
                                width * 2,
                                height);
                    }
                }

                sprites.add(bi);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/Resources/SFX/jump.mp3"));
        sfx.put("strike", new AudioPlayer("/Resources/SFX/strike.mp3"));
        sfx.put("fireball", new AudioPlayer("/Resources/SFX/fireball.mp3"));
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public void setFiring() {
        firing = true;
    }

    public void setStriking() {
        striking = true;
    }
    
    public boolean getDead() {
        return dead;
    }

    public void checkAttack(ArrayList<Enemy> enemies) {

        // Loops through enemies
        for (int i = 0; i < enemies.size(); i++) {

            Enemy e = enemies.get(i);

            // Strike attack
            if (striking) {
                if (facingRight) {
                    if (e.getx() > x
                            && e.getx() < x + strikeRange
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(strikeDamage);
                    }
                } else {
                    if (e.getx() < x
                            && e.getx() > x - strikeRange
                            && e.gety() > y - height / 2
                            && e.gety() < y + height / 2) {
                        e.hit(strikeDamage);
                    }
                }
            }
            // Fireball attack
            for (int j = 0; j < fireBalls.size(); j++) {
                if (fireBalls.get(j).intersects(e)) {
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            // Check enemy collision
            if (intersects(e)) {
                hit(e.getDamage());
            }

        }
    }

    public void hit(int damage) {
        if (flinching) {
            return;
        }
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        if (health == 0) {
            dead = true;
        }
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    private void getNextPosition() {

        // movement
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
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // Can't move while attacking only in the air
        if ((currentAction == STRIKING || currentAction == FIREBALL)
                && !(jumping || falling)) {
            dx = 0;
        }

        // Jumping
        if (jumping && !falling) {
            sfx.get("jump").play();
            dy = jumpStart;
            falling = true;
        }

        // Falling
        if (falling) {

            dy += fallSpeed;

            if (dy > 0) {
                jumping = false;
            }
            if (dy < 0 && !jumping) {
                dy += stopJumpSpeed;
            }

            if (dy > maxFallSpeed) {
                dy = maxFallSpeed;
            }
        }
    }

    public void update() {

        //Update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //Checking if attack has stopped
        if (currentAction == STRIKING) {
            if (animation.hasPlayedOnce()) {
                striking = false;
            }
        }
        if (currentAction == FIREBALL) {
            if (animation.hasPlayedOnce()) {
                firing = false;
            }
        }
        if (currentAction == DYING) {
            if (animation.hasPlayedOnce()) {
                
            }
        }

        //Fireball attack
        fire += 1;
        if (fire > maxFire) {
            fire = maxFire;
        }
        if (firing && currentAction != FIREBALL) {
            if (fire > fireCost) {
                sfx.get("fireball").play();
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);

            }
        }

        // Updating Fireballs
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()) {
                fireBalls.remove(i);
                i--;
            }
        }

        // check done flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1000) {
                flinching = false;
            }
        }

        // Set animation
        if (!dead) {
            if (striking) {
                if (currentAction != STRIKING) {
                    sfx.get("strike").play();
                    currentAction = STRIKING;
                    animation.setFrames(sprites.get(STRIKING));
                    animation.setDelay(50);
                    width = 60;
                }
            } else if (firing) {
                if (currentAction != FIREBALL) {
                    currentAction = FIREBALL;
                    animation.setFrames(sprites.get(FIREBALL));
                    animation.setDelay(50);
                    width = 30;
                }
            } else if (dy > 0) {

                if (currentAction != FALLING) {
                    currentAction = FALLING;
                    animation.setFrames(sprites.get(FALLING));
                    animation.setDelay(150);
                    width = 30;
                }
            } else if (dy < 0) {
                if (currentAction != JUMPING) {
                    currentAction = JUMPING;
                    animation.setFrames(sprites.get(JUMPING));
                    animation.setDelay(-1);
                    width = 30;
                }
            } else if (left || right) {
                if (currentAction != WALKING) {
                    currentAction = WALKING;
                    animation.setFrames(sprites.get(WALKING));
                    animation.setDelay(40);
                    width = 30;
                }
            } else {
                if (currentAction != IDLE) {
                    currentAction = IDLE;
                    animation.setFrames(sprites.get(IDLE));
                    animation.setDelay(400);
                    width = 30;
                }
            }
        } else {
            if (currentAction != DYING) {
                currentAction = DYING;
                animation.setFrames(sprites.get(DYING));
                animation.setDelay(400);
                width = 30;
            }
        }

        animation.update();

        if (currentAction != STRIKING && currentAction != FIREBALL) {
            if (right) {
                facingRight = true;
            }
            if (left) {
                facingRight = false;
            }
        }
    }

    public void draw(Graphics2D g) {

        setMapPosition();

        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).draw(g);
        }

        //Drawing player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }
        }
        super.draw(g);
    }
}
