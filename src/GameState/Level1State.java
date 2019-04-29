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
import Entity.Explosion.*;
import Audio.AudioPlayer;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private HUD hud;

    private AudioPlayer bgMusic;

    private int wave;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {

        tileMap = new TileMap(30);
        tileMap.loadTiles("/Resources/Tilesets/snowIceTiles.gif");
        tileMap.loadMap("/Resources/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        //New background
        bg = new Background("/Resources/Backgrounds/ice.jpg", 0.1);

        //Player
        player = new Player(tileMap);
        player.setPosition(100, 100);

        // Adds enemies
        populateEnemies();

        // Creates array list for explosion effect
        explosions = new ArrayList<Explosion>();

        // Creates hud
        hud = new HUD(player);

        // Adds background music
        bgMusic = new AudioPlayer("/Resources/Music/level1-1.mp3");
        bgMusic.play();

    }

    public void populateEnemies() {
        enemies = new ArrayList<Enemy>();

        Slime s;
        Point[] points = new Point[]{
            new Point(120, 100),
            new Point(150, 100),
            new Point(50, 100),
            new Point(500, 100)};

        for (int i = 0; i < points.length; i++) {
            s = new Slime(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
            wave = 1;
        }
    }

    public void populateNewWave1() {

        Slime s;
        Point[] points = new Point[]{
            new Point(120, 100),
            new Point(150, 100),
            new Point(50, 100),
            new Point(500, 100),
            new Point(100, 100),
            new Point(150, 100),
            new Point(200, 100),
            new Point(250, 100)};

        for (int i = 0; i < points.length; i++) {
            s = new Slime(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
    }
    
    public void populateNewWave2() {
        
        Slime s;
        Point[] points = new Point[]{
            new Point(129, 75),
            new Point(152, 190),
            new Point(52, 53),
            new Point(231, 59),
            new Point(300, 190),
            new Point(325, 190),
            new Point(350, 190),
            new Point(400, 57)};
        
        for (int i = 0; i < points.length; i++) {
            s = new Slime(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
    }
    
    public void populateNewWave3() {
        
        SlimeRed sr;
        Point[] points = new Point[]{
            new Point(62, 65),
            new Point(52, 65),
            new Point(92, 65),
            new Point(400, 65),
            new Point(425, 65),
            new Point(500, 65),
            new Point(302, 65),
            new Point(193, 65)};
        
        for (int i = 0; i < points.length; i++) {
            sr = new SlimeRed(tileMap);
            sr.setPosition(points[i].x, points[i].y);
            enemies.add(sr);
        }
    }
    
    public void populateNewWave4() {
        
        Ghost gh;
        Point[] points = new Point[]{
            new Point(200, 65),
            new Point(183, 65),
            new Point(129, 65),
            new Point(551, 65),
            new Point(425, 65),
            new Point(320, 65),
            new Point(150, 65),
            new Point(389, 65)};
        
        for (int i = 0; i < points.length; i++) {
            gh = new Ghost(tileMap);
            gh.setPosition(points[i].x, points[i].y);
            enemies.add(gh);
        }
    }
        

    public void update() {

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
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(), e.gety()));
            }
        }

        // update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }

        if (enemies.size() == 0) {
            switch(wave) {
                case 1:
                    populateNewWave1();
                    wave += 1;
                    break;
                case 2:
                    populateNewWave1();
                    populateNewWave2();
                    wave += 1;
                    break;
                case 3:
                    populateNewWave1();
                    populateNewWave2();
                    populateNewWave3();
                    wave += 1;
                    break;
                case 4:
                    populateNewWave1();
                    populateNewWave2();
                    populateNewWave3();
                    populateNewWave4();
                    wave += 1;
                    break;
            }
        }

        if (player.getDead()) {
            bgMusic.stop();
            gsm.setState(GameStateManager.GAMEOVERSTATE);
        }

    }

    public void draw(java.awt.Graphics2D g) {

        // draw bg
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        // draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        // draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
            explosions.get(i).draw(g);
        }

        // draw hud
        hud.draw(g);

    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_A) {
            player.setLeft(true);
        }
        if (k == KeyEvent.VK_D) {
            player.setRight(true);
        }
        if (k == KeyEvent.VK_W) {
            player.setUp(true);
        }
        if (k == KeyEvent.VK_S) {
            player.setDown(true);
        }
        if (k == KeyEvent.VK_SPACE) {
            player.setJumping(true);
        }
        if (k == KeyEvent.VK_Q) {
            player.setStriking();
        }
        if (k == KeyEvent.VK_E) {
            player.setFiring();
        }
    }

    public void keyReleased(int k) {
        if (k == KeyEvent.VK_A) {
            player.setLeft(false);
        }
        if (k == KeyEvent.VK_D) {
            player.setRight(false);
        }
        if (k == KeyEvent.VK_W) {
            player.setUp(false);
        }
        if (k == KeyEvent.VK_S) {
            player.setDown(false);
        }
        if (k == KeyEvent.VK_SPACE) {
            player.setJumping(false);
        }
    }

}
