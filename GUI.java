import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI{
  private static final int WINDOW_WIDTH = 600;
  private static final int WINDOW_HEIGHT = 500;
  private static final int roboDimensions = 50;
  JFrame frame;
  //store Inputs
  JTextField[] text;

  //panel for the robot to move in
  JPanel panel;

  //Labels for stats
  JLabel locationLab;
  JLabel oriLab;

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

    //Panel to contain the statistics of the
    JPanel stats = new JPanel();
    stats.setPreferredSize(new Dimension(100, 500));
    frame.getContentPane().add(BorderLayout.NORTH, new JLabel("World"));
    frame.getContentPane().add(BorderLayout.EAST, stats);

    stats.add(new JLabel("Statistics"));
    locationLab = new JLabel("Location: \n[" + (robot.getX() + 1) + ", " +
                              (8 - robot.getY()) + "]");
    stats.add(locationLab);
    oriLab = new JLabel("Direction faced: \n" + robot.directions[robot.getOrientation()*2]);
    stats.add(oriLab);

    world.createWorld(robot.getX(), robot.getY());
    frame.addKeyListener(new LeftListener());
    frame.addKeyListener(new RightListener());
    frame.addKeyListener(new MoveListener());
    //create a panel for the world for the robot to move in

    panel = new JPanel();
    panel.add(new RobotDraw());
    panel.setBorder(BorderFactory.createLineBorder(Color.RED));
    frame.getContentPane().add(BorderLayout.CENTER, panel);

    frame.setVisible(true);
    robot.printMoves();
  }

  private class RobotDraw extends JPanel {
    protected void paintComponent(Graphics g){
      super.paintComponent(g);
      g.fillRect(robot.getX() *50, robot.getY()*50, 50, 50);
      g.setColor(Color.BLACK);
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
  }
  /** Event Handlesrs **/
  class SubmitListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      try{
        robot.setX(Integer.parseInt(text[0].getText()) - 1);
        robot.setY(8 - Integer.parseInt(text[1].getText()));
      }catch(NumberFormatException e){
      }
      if(robot.getX() >= 0 && robot.getX() < 8 &&
         robot.getY() >= 0 && robot.getY() < 8){
        for(int i = 0; i < robot.directions.length; i++){
          if((text[2].getText().charAt(0)) == robot.directions[i]){
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
