package com.hack.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, Runnable {

    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    public static final Font main = new Font("Clear Sans", Font.PLAIN, 28);
    private Thread game;
    private boolean running;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
            BufferedImage.TYPE_INT_RGB);
    private GameBoard board;

    private long startTime;
    private long elapsed;
    private boolean set;

    public Game() {
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.addKeyListener(this);

        this.board = new GameBoard(WIDTH / 2 - GameBoard.BOARD_WIDTH / 2,
                HEIGHT - GameBoard.BOARD_HEIGHT - 10);
    }

    public void update() {
        if (Keyboard.pressed[KeyEvent.VK_SPACE]) {
            System.out.println("hit space");
        }
        if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
            System.out.println("hit right");
        }
        if (Keyboard.typed(KeyEvent.VK_UP)) {
            System.out.println("hit up");
        }
        if (Keyboard.typed(KeyEvent.VK_DOWN)) {
            System.out.println("hit down");
        }
        if (Keyboard.typed(KeyEvent.VK_LEFT)) {
            System.out.println("hit left");
        }
        this.board.update();
        Keyboard.update();
    }

    public void render() {
        Graphics2D d = (Graphics2D) this.image.getGraphics();
        d.setColor((Color.white));
        d.fillRect(0, 0, WIDTH, HEIGHT);
        this.board.render(d);
        d.dispose();

        Graphics2D g2d = (Graphics2D) this.getGraphics();
        g2d.drawImage(this.image, 0, 0, null);
        g2d.dispose();
    }

    @Override
    public void run() {
        int fps = 0;
        int updates = 0;
        long fpsTimer = System.currentTimeMillis();
        double nsPerUpdate = 1000000000.0 / 60;

        double then = System.nanoTime();
        double unprocessed = 0;

        while (this.running) {
            boolean shouldRender = false;
            double now = System.nanoTime();
            unprocessed += (now - then) / nsPerUpdate;
            then = now;

            while (unprocessed >= 1) {
                updates++;
                this.update();
                unprocessed--;
                shouldRender = true;
            }

            if (shouldRender) {
                fps++;
                this.render();
                shouldRender = false;
            }

            else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (System.currentTimeMillis() - fpsTimer > 1000) {
                System.out.printf("%d fps %d updates", fps, updates);
                System.out.println();
                fps = 0;
                updates = 0;
                fpsTimer += 1000;
            }
        }

    }

    public synchronized void start() {
        if (this.running) {
            return;
        }
        {
            this.running = true;
            this.game = new Thread(this, "game");
            this.game.start();
        }
    }

    public synchronized void stop() {
        if (!this.running) {
            return;
        }
        {
            this.running = false;
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Keyboard.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Keyboard.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
