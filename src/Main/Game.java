package Main;

import javax.swing.JFrame;

public class Game {
    
    //Main method
    public static void main(String[] args) {
        
        //Creates new JFrame and adds the JPanel(GanePanel)
        JFrame window = new JFrame("Platformer");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
