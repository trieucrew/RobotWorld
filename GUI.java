import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI{
  private String MOVES = "";
  private static final int WINDOW_WIDTH = 550;
  private static final int WINDOW_HEIGHT = 570;
  private static final int ROBO_DIMENSIONS = 50;
  JFrame frame;
  //store Inputs
  JTextField[] text;

  //panel for the robot to move in
  JPanel panel;

  //Labels for stats
  JLabel locationLab;
  JLabel oriLab;
  JLabel movesLab;

  Robot robot;
  World world;

  GUI(){
    robot = new Robot();
    world = new World();
  }

  public void startingPos(){
    frame = new JFrame();
    frame.setMinimumSize((new Dimension(150,200)));
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    String[] labels = {"Starting x:", "Starting y:", "Orientation:"};
    text = new JTextField[labels.length];

    JPanel panel = new JPanel();
    frame.getContentPane().add(BorderLayout.CENTER, panel);

    //create the labels and textfields
    for(int i = 0; i < labels.length; i++){
      JLabel labs = new JLabel(labels[i], JLabel.TRAILING);
      panel.add(labs);
      text[i] = new JTextField(5);
      labs.setLabelFor(text[i]);
      panel.add(text[i]);
    }

    JButton submit = new JButton("Submit");
    submit.addActionListener(new SubmitListener());
    frame.getContentPane().add(BorderLayout.SOUTH, submit);
    frame.setVisible(true);
  }

  public void startGame(){
    frame = new JFrame("Game");
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

    //list of previous moves
    JPanel movesPanel = new JPanel();
    JScrollPane scroll = new JScrollPane(movesPanel);
    scroll.setPreferredSize(new Dimension(550,50));
    movesLab = new JLabel("Moves: ");
    movesPanel.add(movesLab);
    frame.getContentPane().add(BorderLayout.SOUTH, scroll);

    //Panel to contain the statistics of the
    JPanel stats = new JPanel();
    stats.setPreferredSize(new Dimension(100, 500));
    frame.getContentPane().add(BorderLayout.EAST, stats);
    stats.add(new JLabel("Statistics"));
    locationLab = new JLabel("Location: \n[" + (robot.getX() + 1) + ", " +
                              (8 - robot.getY()) + "]");
    stats.add(locationLab);
    oriLab = new JLabel("Direction faced: \n" + robot.directions[robot.getOrientation()*2]);
    stats.add(oriLab);

    //create a panel for the world for the robot to move in
    panel = new JPanel();
    world.createWorld(robot.getX(), robot.getY());
    panel.add(new RobotDraw());
    panel.setBorder(BorderFactory.createLineBorder(Color.RED));
    frame.getContentPane().add(BorderLayout.CENTER, panel);

    //Listeners for moving the robot
    frame.addKeyListener(new LeftListener());
    frame.addKeyListener(new RightListener());
    frame.addKeyListener(new MoveListener());
    frame.setVisible(true);
  }

  private class RobotDraw extends JPanel {
    protected void paintComponent(Graphics g){
      super.paintComponent(g);
      //points for the triangle
      int NandSPoint = robot.getX()*ROBO_DIMENSIONS + ROBO_DIMENSIONS/2;
      int EandWPoint = robot.getY()*ROBO_DIMENSIONS + ROBO_DIMENSIONS/2;
      int width = robot.getX()*ROBO_DIMENSIONS + ROBO_DIMENSIONS;
      int height = robot.getY()*ROBO_DIMENSIONS + ROBO_DIMENSIONS;
      int originX = robot.getX()*ROBO_DIMENSIONS;
      int originY = robot.getY()*ROBO_DIMENSIONS;
      switch(robot.getOrientation()){
        case 0: g.fillPolygon(new int[]{NandSPoint, originX, width},
                              new int[]{originY, height, height}, 3);
                break;
        case 1: g.fillPolygon(new int[]{originX, originX, width},
                              new int[]{originY, height, EandWPoint}, 3);
                break;
        case 2: g.fillPolygon(new int[]{NandSPoint, originX, width},
                              new int[]{height, originY, originY}, 3);
                break;
        case 3: g.fillPolygon(new int[]{originX, width, width},
                              new int[]{EandWPoint, height, originY}, 3);
                break;
        default: break;
      }
      //g.fillRect(robot.getX() *50, robot.getY()*50, 50, 50);
      //g.setColor(Color.BLACK);
    }
    @Override
    public Dimension getPreferredSize(){
      return new Dimension(400, 400);
    }
  }

  void updateStats(){
    locationLab.setText("Location: [" + (robot.getX() + 1) + ", " +
                              (8 - robot.getY()) + "]");
    oriLab.setText("Direction faced: " + robot.directions[robot.getOrientation()*2]);
    movesLab.setText("Moves: "+ MOVES);
  }
  /** Event Handlesrs **/
  class SubmitListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      try{
        robot.setX(Integer.parseInt(text[0].getText()) - 1);
        robot.setY(8 - Integer.parseInt(text[1].getText()));
      }catch(NumberFormatException e){
      }
      //robot can not go out of bounds
      if(robot.getX() >= 0 && robot.getX() < 8 &&
         robot.getY() >= 0 && robot.getY() < 8){
        for(int i = 0; i < robot.directions.length; i++){
          //can only submit if valid orientation
          if((text[2].getText().charAt(0)) == robot.directions[i]
              && text[2].getText().length() == 1){
            robot.setOrientation(i/2);
            robot.printMoves();
            frame.dispose();
            GUI.this.startGame();
          }
        }
      }
    }
  }

  class LeftListener implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_L){
        MOVES += "L, ";
        System.out.println("L");
        robot.updatePos(-1);
        robot.printMoves();
        panel.repaint();
        GUI.this.updateStats();
      }
    }
    @Override
    public void keyTyped (KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){}
  }

  class RightListener implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_R){
        MOVES += "R, ";
        System.out.println("R");
        robot.updatePos(1);
        robot.printMoves();
        panel.repaint();
        GUI.this.updateStats();
      }
    }
    @Override
    public void keyTyped (KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){}
  }

  class MoveListener implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_M){
        MOVES += "M, ";
        System.out.println("M");
        robot.updatePos(0);
        world.updateWorld(robot.getX(), robot.getY());
        robot.printMoves();
        panel.repaint();
        GUI.this.updateStats();
      }
    }
    @Override
    public void keyTyped (KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){}
  }
}
