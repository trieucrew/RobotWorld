import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class World {
  private static final int WORLD_WIDTH = 8;
  private static final int WORLD_HEIGHT = 8;
  private int[][] grid;

  private int robotX;
  private int robotY;

  World(){
    this.grid = new int[WORLD_WIDTH][WORLD_HEIGHT];
  }

  public void createWorld (int robotX, int robotY){
    for (int i = 0; i < WORLD_WIDTH; i++){
      for (int j = 0; j < WORLD_HEIGHT; j++){
        this.grid[i][j] = 0;
        if((this.robotX = robotX) == i && (this.robotY = robotY) == j){
          this.grid[i][j] = 1;
        }
      }
    }
  }

  public void updateWorld(int x, int y){
    this.grid[robotX][robotY] = 0;
    this.grid[this.robotX = x][this.robotY = y] = 1;
  }
}
