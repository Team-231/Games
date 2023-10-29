package com.hack.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameBoard {

    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final int startingTiles = 2;
    private Tile[][] board;
    private boolean dead;
    private boolean won;
    private BufferedImage gameBoard;
    private BufferedImage finalBoard;
    private int x;
    private int y;

    private static int SPACING = 10;
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.HEIGHT;

    private boolean hasStarted;

    public GameBoard(int x, int y) {
        this.x = x;
        this.y = y;
        this.board = new Tile[ROWS][COLS];
        this.gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        this.finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        this.createBoardImage();
        this.start();
    }

    private void createBoardImage() {
        Graphics2D d = (Graphics2D) this.gameBoard.getGraphics();
        d.setColor(Color.darkGray);
        d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        d.setColor(Color.lightGray);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = SPACING + SPACING * col + Tile.WIDTH * col;
                int y = SPACING + SPACING * row + Tile.WIDTH * row;
                d.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH,
                        Tile.ARC_HEIGHT);

            }
        }

    }

    private void start() {
        for (int i = 0; i < this.startingTiles; i++) {
            this.spawnRandom();
        }
    }

    private void spawnRandom() {
        Random random = new Random();
        boolean notValid = true;

        while (notValid) {
            int location = random.nextInt(ROWS * COLS);
            int row = location / ROWS;
            int col = location % COLS;
            Tile current = this.board[row][col];
            if (current == null) {
                int value = random.nextInt(10) < 9 ? 2 : 4;
                Tile tile = new Tile(value, this.getTileX(col),
                        this.getTileY(row));
                this.board[row][col] = tile;
                notValid = false;
            }
        }
    }

    private int getTileX(int col) {
        return SPACING + col * Tile.WIDTH + col * SPACING;
    }

    private int getTileY(int row) {
        return SPACING + row * Tile.HEIGHT + row * SPACING;
    }

    public void render(Graphics2D d) {
        Graphics2D g2d = (Graphics2D) this.finalBoard.getGraphics();
        g2d.drawImage(this.gameBoard, 0, 0, null);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = this.board[row][col];
                if (current == null) {
                    continue;
                }
                current.render(g2d);
            }
        }

        d.drawImage(this.finalBoard, this.x, this.y, null);
        g2d.dispose();
    }

    public void update() {
        this.checkKeys();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = this.board[row][col];
                if (current == null) {
                    continue;
                }
                current.update();
                this.resetPosition(current, row, col);
                if (current.getValue() == 2048) {
                    this.won = true;
                }
            }
        }
    }

    private void resetPosition(Tile current, int row, int col) {
        if (current == null) {
            return;
        }

        int x = this.getTileX(col);
        int y = this.getTileY(row);

        int distX = current.getX() - x;
        int distY = current.getY() - y;

        if (Math.abs(distX) < Tile.SLIDE_SPEED) {
            current.setX(current.getX() - distX);
        }

        if (Math.abs(distY) < Tile.SLIDE_SPEED) {
            current.setY(current.getY() - distY);
        }

        if (distX < 0) {
            current.setX(current.getX() + Tile.SLIDE_SPEED);
        }
        if (distY < 0) {
            current.setY(current.getY() + Tile.SLIDE_SPEED);
        }
        if (distX > 0) {
            current.setX(current.getX() - Tile.SLIDE_SPEED);
        }
        if (distY > 0) {
            current.setY(current.getY() - Tile.SLIDE_SPEED);
        }
    }

    private boolean move(int row, int col, int horizontalDirection,
            int verticalDirection, Direction dir) {
        boolean canMove = false;

        Tile current = this.board[row][col];
        if (current == null) {
            return false;
        }
        boolean move = true;
        int newCol = col;
        int newRow = row;
        while (move) {
            newCol += horizontalDirection;
            newRow += verticalDirection;
            if (this.checkOutOfBounds(dir, newRow, newCol)) {
                break;
            }
            if (this.board[newRow][newCol] == null) {
                this.board[newRow][newCol] = current;
                this.board[newRow - verticalDirection][newCol
                        - horizontalDirection] = null;
                this.board[newRow][newCol]
                        .setSlideTo(new Point(newRow, newCol));
                canMove = true;
            } else if (this.board[newRow][newCol].getValue() == current
                    .getValue() && this.board[newRow][newCol].canCombine()) {
                this.board[newRow][newCol].setCanCombine(false);
                this.board[newRow][newCol]
                        .setValue(this.board[newRow][newCol].getValue() * 2);
                canMove = true;
                this.board[newRow - verticalDirection][newCol
                        - horizontalDirection] = null;
                this.board[newRow][newCol]
                        .setSlideTo(new Point(newRow, newCol));
                // add to score
            } else {
                move = false;
            }
        }

        return canMove;
    }

    private boolean checkOutOfBounds(Direction dir, int row, int col) {
        if (dir == Direction.LEFT) {
            return col < 0;
        } else if (dir == Direction.RIGHT) {
            return col > COLS - 1;
        } else if (dir == Direction.UP) {
            return row < 0;
        } else if (dir == Direction.DOWN) {
            return row > ROWS - 1;
        }
        return false;
    }

    private void moveTiles(Direction dir) {
        boolean canMove = false;
        int horizontalDirection = 0;
        int verticalDirection = 0;

        if (dir == Direction.LEFT) {
            horizontalDirection = -1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    } else {
                        this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    }
                }
            }
        }

        else if (dir == Direction.RIGHT) {
            horizontalDirection = 1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = COLS - 1; col >= 0; col--) {
                    if (!canMove) {
                        canMove = this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    } else {
                        this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    }
                }
            }
        }

        else if (dir == Direction.UP) {
            verticalDirection = -1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    } else {
                        this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    }
                }
            }
        }

        else if (dir == Direction.DOWN) {
            verticalDirection = 1;
            for (int row = ROWS - 1; row >= 0; row--) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    } else {
                        this.move(row, col, horizontalDirection,
                                verticalDirection, dir);
                    }
                }
            }
        } else {
            System.out.println(dir + "is not a valid direction.");
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = this.board[row][col];
                if (current == null) {
                    continue;
                }
                current.setCanCombine(true);
            }
        }

        if (canMove) {
            this.spawnRandom();
            this.checkDead();
        }

    }

    private void checkDead() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (this.board[row][col] == null) {
                    return;
                }
                if (this.checkSurroundingTiles(row, col,
                        this.board[row][col])) {
                    return;
                }
            }
        }

        this.dead = true;
        // setHighScore(score);
    }

    private boolean checkSurroundingTiles(int row, int col, Tile current) {
        if (row > 0) {
            Tile check = this.board[row - 1][col];
            if (check == null) {
                return true;
            }
            if (current.getValue() == check.getValue()) {
                return true;
            }
        }
        if (row < ROWS - 1) {
            Tile check = this.board[row + 1][col];
            if (check == null) {
                return true;
            }
            if (current.getValue() == check.getValue()) {
                return true;
            }
        }
        if (col > 0) {
            Tile check = this.board[row][col - 1];
            if (check == null) {
                return true;
            }
            if (current.getValue() == check.getValue()) {
                return true;
            }
        }
        if (col < COLS - 1) {
            Tile check = this.board[row][col + 1];
            if (check == null) {
                return true;
            }
            if (current.getValue() == check.getValue()) {
                return true;
            }
        }
        return false;
    }

    private void checkKeys() {
        if (Keyboard.typed(KeyEvent.VK_LEFT)) {
            this.moveTiles(Direction.LEFT);
            if (!this.hasStarted) {
                this.hasStarted = true;
            }
        }
        if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
            this.moveTiles(Direction.RIGHT);
            if (!this.hasStarted) {
                this.hasStarted = true;
            }
        }
        if (Keyboard.typed(KeyEvent.VK_UP)) {
            this.moveTiles(Direction.UP);
            if (!this.hasStarted) {
                this.hasStarted = true;
            }
        }
        if (Keyboard.typed(KeyEvent.VK_DOWN)) {
            this.moveTiles(Direction.DOWN);
            if (!this.hasStarted) {
                this.hasStarted = true;
            }
        }
    }

}
