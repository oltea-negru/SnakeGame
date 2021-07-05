import javax.swing.*;

/**
 * Class for the game's frame
 */
public class GameFrame extends JFrame {

    /**
     * Constructor where the panel is added as well as a title and other characteristics
     */
    GameFrame(){
        this.add(new GamePanel());
        //name of the game
        this.setTitle("Snake Game");
        //exits
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame cannot be resized
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        //game is centered on user's screen
        this.setLocationRelativeTo(null);
    }

    /**
     * Main method for starting the game
     * @param args
     */
    public static void main(String[] args) {
        new GameFrame();
    }
}
