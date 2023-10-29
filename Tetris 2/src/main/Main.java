package main;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
    public static void main(String[] args){
        JFrame board = new JFrame("Tetris Screen");
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setResizable(false);

        GamePanel gp = new GamePanel();
        board.add(gp);
        board.pack();

        board.setLocationRelativeTo(null);
        board.setVisible(true);

        gp.launchGame();

    }
}
