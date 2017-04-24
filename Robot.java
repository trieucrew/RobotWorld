import java.awt.*;

public class Robot{
  public final static char[] directions = {'N', 'n', 'E', 'e', 'S', 's', 'W', 'w'};

  private static int x_pos;
  private static int y_pos;
  private static int orientation;

  public Robot(){
    this.x_pos = 0;
    this.y_pos = 0;
    this.orientation = 0;
  }

  public Robot(int x, int y, int orientation){
    this.x_pos = x;
    this.y_pos = y;
    this.orientation = orientation;
  }

  /** N-0, E-1, S-2, W-3 **/
  void updatePos(int move){
    switch(move){
      case -1:  orientation = (orientation + 3) % 4;
                break;
      case 0:   this.nestedUpdate();
                break;
      case 1:   orientation = (orientation + 1) % 4;
                break;
      default:  break;
    }
  }

  void nestedUpdate(){
    switch(orientation){
      case 0:   if(y_pos - 1 >= 0) y_pos -= 1;
                break;
      case 1:   if(x_pos + 1 < 8) x_pos += 1;
                break;
      case 2:   if(y_pos + 1 < 8) y_pos += 1;
                break;
      case 3:   if(x_pos - 1 >= 0) x_pos -= 1;
                break;
    }
  }

  void printMoves(){
      System.out.println("Inputs" +
                        "\nX: " + (x_pos + 1) +
                        "\nY: " + (8 - y_pos) +
                        "\nOrientation: " + directions[2*orientation]);
  }

  /** GETTER AND SETTER METHODS **/
  public int getX(){
    return x_pos;
  }

  public int getY(){
    return y_pos;
  }

  public int getOrientation(){
    return orientation;
  }

  public void setX(int x_pos){
    this.x_pos = x_pos;
  }

  public void setY(int y_pos){
    this.y_pos = y_pos;
  }

  public void setOrientation(int orientation){
    this.orientation = orientation;
  }
}
