package Entity;

import java.awt.image.BufferedImage;

public class Animation {
    
    // Stores animations
    private BufferedImage[] frames;
    private int currentFrame;
    
    // Timer between frames
    private long startTime;
    private long delay;
    
    // Tells if animation has played already(looped)
    private boolean playedOnce;
    
    // Constructor 
    public Animation() {
        playedOnce = false;
    }
    
    // Sets up the frame animation
    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        playedOnce = false;
    }
    
    // Sets delay time for animation
    public void setDelay(long d) {
        delay = d;
    }
    
    // For manual frame number
    public void setFrame(int i) {
        currentFrame = i;
    }
    
    // Logic to move to next frame. If delay is -1 no animation is playing.
    // Calculates how long since last frame came up in miliseconds.
    // When that time is greater than delay it moves to next frame until max
    // frames are reached.
    public void update() {
        if(delay == -1)
            return;
        
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }
    
    //gets current frame number
    public int getFrame() {
        return currentFrame;
    }
    
    //Gets the image that is needed to draw
    public BufferedImage getImage() {
        return frames[currentFrame];
    }
    
    // Returns if animation has played one time or not
    public boolean hasPlayedOnce() {
        return playedOnce;
    }
}
