package com.hack.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public static final int SLIDE_SPEED = 20;
    public static final int ARC_WIDTH = 15;
    public static final int ARC_HEIGHT = 15;

    private int value;
    private BufferedImage tileImage;
    private Color background;
    private Color text;
    private Font font;
    private Point slideTo;
    private int x;
    private int y;

    private boolean canCombine = true;

    public Tile(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.slideTo = new Point(x, y);
        this.tileImage = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_ARGB);
        this.drawImage();
    }

    private void drawImage() {
        Graphics2D d = (Graphics2D) this.tileImage.getGraphics();
        if (this.value == 2) {
            this.background = new Color(0xe9e9e9);
            this.text = new Color(0x000000);
        } else if (this.value == 4) {
            this.background = new Color(0xe6daab);
            this.text = new Color(0x000000);
        } else if (this.value == 8) {
            this.background = new Color(0xf79d3d);
            this.text = new Color(0xffffff);
        } else if (this.value == 16) {
            this.background = new Color(0xf28007);
            this.text = new Color(0xffffff);
        } else if (this.value == 32) {
            this.background = new Color(0xf55e3b);
            this.text = new Color(0xffffff);
        } else if (this.value == 64) {
            this.background = new Color(0xff0000);
            this.text = new Color(0xffffff);
        } else if (this.value == 128) {
            this.background = new Color(0xe9de84);
            this.text = new Color(0xffffff);
        } else if (this.value == 256) {
            this.background = new Color(0xf6e873);
            this.text = new Color(0xffffff);
        } else if (this.value == 512) {
            this.background = new Color(0xf5e455);
            this.text = new Color(0xffffff);
        } else if (this.value == 1024) {
            this.background = new Color(0xf7e12c);
            this.text = new Color(0xffffff);
        } else if (this.value == 2048) {
            this.background = new Color(0xffe400);
            this.text = new Color(0xffffff);
        } else {
            this.background = Color.black;
            this.text = Color.white;
        }

        d.setColor(new Color(0, 0, 0, 0));
        d.fillRect(0, 0, WIDTH, HEIGHT);

        d.setColor(this.background);
        d.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        d.setColor(this.text);

        if (this.value <= 64) {
            this.font = Game.main.deriveFont(36f);
        } else {
            this.font = Game.main;
        }
        d.setFont(this.font);

        int drawX = WIDTH / 2
                - DrawUtils.getMessageWidth("" + this.value, this.font, d) / 2;
        int drawY = HEIGHT / 2
                + DrawUtils.getMessageHeight("" + this.value, this.font, d) / 2;
        d.drawString("" + this.value, drawX, drawY);
        d.dispose();
    }

    public void update() {

    }

    public void render(Graphics2D d) {
        d.drawImage(this.tileImage, this.x, this.y, null);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
        this.drawImage();
    }

    public boolean canCombine() {
        return this.canCombine;
    }

    public void setCanCombine(boolean canCombine) {
        this.canCombine = canCombine;
    }

    public Point getSlideTo() {
        return this.slideTo;
    }

    public void setSlideTo(Point slideTo) {
        this.slideTo = slideTo;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
