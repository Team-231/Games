package tetrimino;

import java.awt.*;

public class Square_Mino extends Tetrimino{
    public Square_Mino(){
        create(Color.yellow);
    }
    public void setXY(int x, int y){
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y + Block.SIZE;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;

    }
    public void getDirection1(){
        //squares don't rotate lol
    }
    public void getDirection2(){
    }
    public void getDirection3(){
    }
    public void getDirection4(){
    }
}
