package main;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import tetrimino.*;

public class PlayManager {

    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    Tetrimino currentTetrimino;
    final int TETRIMINO_START_X;
    final int TETRIMINO_START_Y;
    Tetrimino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
    public static ArrayList<Tetrimino> sevenBag = new ArrayList<>();

    public static int dropInterval = 60;

    boolean gameOver;
    int level = 1;
    int score = 0;
    int lines = 0;
    int countThing = 10;


    public PlayManager(){
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        TETRIMINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        TETRIMINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        currentTetrimino = pickMino();
        currentTetrimino.setXY(TETRIMINO_START_X, TETRIMINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
    private Tetrimino pickMino() {
        Tetrimino mino = null;
        if(sevenBag.size() == 0){
            sevenBag.add(new L_Mino());
            sevenBag.add(new J_Mino());
            sevenBag.add(new Line_Mino());
            sevenBag.add(new Square_Mino());
            sevenBag.add(new Z_Mino());
            sevenBag.add(new S_Mino());
            sevenBag.add(new T_Mino());
        }
        int i = new Random().nextInt(sevenBag.size());
        mino = sevenBag.get(i);
        sevenBag.remove(i);
        return mino;
    }
    public void update(){
        if(currentTetrimino.active == false){
            staticBlocks.add(currentTetrimino.b[0]);
            staticBlocks.add(currentTetrimino.b[1]);
            staticBlocks.add(currentTetrimino.b[2]);
            staticBlocks.add(currentTetrimino.b[3]);

            if(currentTetrimino.b[0].x == TETRIMINO_START_X && currentTetrimino.b[0].y == TETRIMINO_START_Y){
                gameOver = true;
            }
            currentTetrimino.deactivating = false;

            currentTetrimino = nextMino;
            currentTetrimino.setXY(TETRIMINO_START_X,TETRIMINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);

            checkDelete();

        }else {
            currentTetrimino.update();
        }
    }
    private void checkDelete(){
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;
        while(x< right_x && y < bottom_y) {
            for(int i = 0; i < staticBlocks.size();i++){
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    blockCount ++;
                }
            }

            x+= Block.SIZE;

            if (x == right_x) {

                if(blockCount == 12){
                    for(int i = staticBlocks.size()-1;i > -1; i--){
                        if(staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }
                    lineCount++;
                    lines++;
                    if((lines) >= countThing && dropInterval > 1){
                        level ++;
                        countThing+=10;
                        if(dropInterval > 10){
                            dropInterval -= 10;
                        }else{
                            dropInterval -= 1;
                        }
                    }
                    for(int i = 0; i < staticBlocks.size(); i++ ){
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }

        }

        if(lineCount > 3){
            score += 2000 * level;
        }else if (lineCount > 2){
            score += 900 * level;
        }else if (lineCount > 1){
            score += 400 * level;
        }else if (lineCount > 0){
            score += 100 * level;
        }

    }
    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x -4, top_y -4, WIDTH+8, HEIGHT+8);
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Color.darkGray);
        g2.drawLine(left_x+30,HEIGHT + 50,left_x+30, top_y);
        g2.drawLine(left_x+60,HEIGHT + 50,left_x+60, top_y);
        g2.drawLine(left_x+90,HEIGHT + 50,left_x+90, top_y);
        g2.drawLine(left_x+120,HEIGHT + 50,left_x+120, top_y);
        g2.drawLine(left_x+150,HEIGHT + 50,left_x+150, top_y);
        g2.drawLine(left_x+180,HEIGHT + 50,left_x+180, top_y);
        g2.drawLine(left_x+210,HEIGHT + 50,left_x+210, top_y);
        g2.drawLine(left_x+240,HEIGHT + 50,left_x+240, top_y);
        g2.drawLine(left_x+270,HEIGHT + 50,left_x+270, top_y);
        g2.drawLine(left_x+300,HEIGHT + 50,left_x+300, top_y);
        g2.drawLine(left_x+330,HEIGHT + 50,left_x+330, top_y);

        g2.drawLine(left_x ,HEIGHT + 20,left_x+360, HEIGHT + 20);
        g2.drawLine(left_x ,HEIGHT - 10,left_x+360, HEIGHT - 10);
        g2.drawLine(left_x ,HEIGHT - 40,left_x+360, HEIGHT - 40);
        g2.drawLine(left_x ,HEIGHT - 70,left_x+360, HEIGHT - 70);
        g2.drawLine(left_x ,HEIGHT - 100,left_x+360, HEIGHT - 100);
        g2.drawLine(left_x ,HEIGHT - 130,left_x+360, HEIGHT - 130);
        g2.drawLine(left_x ,HEIGHT - 160,left_x+360, HEIGHT - 160);
        g2.drawLine(left_x ,HEIGHT - 190,left_x+360, HEIGHT - 190);
        g2.drawLine(left_x ,HEIGHT - 220,left_x+360, HEIGHT - 220);
        g2.drawLine(left_x ,HEIGHT - 250,left_x+360, HEIGHT - 250);
        g2.drawLine(left_x ,HEIGHT - 280,left_x+360, HEIGHT - 280);
        g2.drawLine(left_x ,HEIGHT - 310,left_x+360, HEIGHT - 310);
        g2.drawLine(left_x ,HEIGHT - 340,left_x+360, HEIGHT - 340);
        g2.drawLine(left_x ,HEIGHT - 370,left_x+360, HEIGHT - 370);
        g2.drawLine(left_x ,HEIGHT - 400,left_x+360, HEIGHT - 400);
        g2.drawLine(left_x ,HEIGHT - 430,left_x+360, HEIGHT - 430);
        g2.drawLine(left_x ,HEIGHT - 460,left_x+360, HEIGHT - 460);
        g2.drawLine(left_x ,HEIGHT - 490,left_x+360, HEIGHT - 490);
        g2.drawLine(left_x ,HEIGHT - 520,left_x+360, HEIGHT - 520);


        g2.setColor(Color.white);
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x,y,200,200);

        g2.setFont(new Font("Comic sans", Font.BOLD, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        g2.drawRect(x,top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("Level: " + level,x,y); y+= 70;
        g2.drawString("Lines: " + lines,x,y); y+= 70;
        g2.drawString("Score: " + score, x, y);
        if(currentTetrimino != null){
            currentTetrimino.draw(g2);
        }

        nextMino.draw(g2);

        for(int i = 0; i < staticBlocks.size(); i++){
            staticBlocks.get(i).draw(g2);
        }
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver){
            g2.setColor(Color.red);
            x = left_x + 20;
            y = top_y + 320;
            g2.drawString("GAME OVER", x , y);
        }
        if(KeyHandler.pausePressed){
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }



        x = 135;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman",Font.BOLD,60));
        g2.drawString("Tetris", x, y);

    }
}
