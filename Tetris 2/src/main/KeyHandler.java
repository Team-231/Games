package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean upPressed,downPressed,leftPressed,rightPressed,pausePressed,spacePressed,zPressed,rPressed;
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if(code == KeyEvent.VK_Z) {
            zPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE){
            if(pausePressed){
                pausePressed = false;
            }else{
                pausePressed = true;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}
}