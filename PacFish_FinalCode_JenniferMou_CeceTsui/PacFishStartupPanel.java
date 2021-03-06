/****************************************************************************
  * PacFishStartupPanel.java
  * Jennifer Mou & Cece Tsui
  * 2015 May
  * CS230 Final Project
  * 
  * We coded the class together. We both talked about the implementation of all the 
  *    methods and how each method would work in relation to the class. When writing
  *    the technical report, we established some pseudocode for each. However,
  *    Jennifer took a larger role in coding the implementation of the class.
  * 
  * This class creates the first GUI that appears when the game is launched. 
  * It shows a GUI that allows the user to choose the difficulty level they want to play. 
  * After pressing the difficulty, it launches another GUI that shows the game board (GameBoardPanel).
  * **************************************************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PacFishStartupPanel extends JPanel {
  //--------------------------------------------------------------
  //Instance Variables
  //--------------------------------------------------------------
  private JButton easy, medium, hard, quitButton;
  private final ImageIcon WELCOME_WORDS = new ImageIcon("welcome.gif");
  private final ImageIcon WELCOME_PIC = new ImageIcon("welcomeImage.gif");
  
  //-----------------------------------------------------------------
  //CONSTRUCTOR: Sets up the main GUI components (Buttons, messages,
  //   images, listeners, etc).
  //-----------------------------------------------------------------
  public PacFishStartupPanel() {
    setSize(400,300);
    setBackground(new Color(202, 246, 245));
    
    easy = new JButton("Easy");
    medium = new JButton("Medium");
    hard = new JButton("Hard");
    
    //Add listeners for buttons
    ButtonListener listener = new ButtonListener();
    easy.addActionListener(listener);
    medium.addActionListener(listener);
    hard.addActionListener(listener);
    
    /**CREATING GUI FROM TOP TO BOTTOM**/
    
    //Upper panel in GUI - shows the welcome message
    JPanel welcomeMessage = new JPanel();
    
    //helloLabel - welcomes to pacFish
    JLabel welcomeLabel = new JLabel();
    welcomeLabel.setIcon(WELCOME_WORDS);
    welcomeMessage.add(welcomeLabel);
    
    //Middle panel of GUI - shows welcome picture
    JPanel image = new JPanel();
    
    //welcomePicLabel - picture of pacFish
    JLabel welcomePicLabel = new JLabel();
    welcomePicLabel.setIcon(WELCOME_PIC);
    image.add(welcomePicLabel);
    
    /**DIVIDING BOTTOM SECTION INTO TWO PARTS**/ //(for aesthetic purposes)
    
    
    //Choose level button panel
    JPanel chooseLevels = new JPanel();
    chooseLevels.setLayout(new BoxLayout(chooseLevels, BoxLayout.LINE_AXIS));
    chooseLevels.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    chooseLevels.add(Box.createHorizontalGlue());
    chooseLevels.add(easy);
    chooseLevels.add(Box.createRigidArea(new Dimension(10, 0)));
    chooseLevels.add(medium);
    chooseLevels.add(Box.createRigidArea(new Dimension(10, 0)));
    chooseLevels.add(hard);
    
    //Quit button on bottom right hand corner
    JPanel quit = new JPanel();
    quitButton = new JButton("Quit");
    quitButton.addActionListener(listener);
    quit.setLayout(new BoxLayout(quit, BoxLayout.LINE_AXIS));
    quit.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    quit.add(Box.createHorizontalGlue());
    quit.add(quitButton); 
    
    /**COMBINE THE TWO BOTTOM PARTS INTO ONE SECTION**/
    
    //Bottom panel of GUI - has all the buttons
    JPanel bottom = new JPanel();
    bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
    bottom.add(chooseLevels);
    bottom.add(Box.createVerticalGlue());
    bottom.add(quit);

    /**PUT EVERYTHING TOGETHER INTO ONE MAIN PANEL**/
    setLayout(new BorderLayout(10,10));
    add(welcomeMessage, BorderLayout.NORTH);
    add(image, BorderLayout.CENTER);
    add(bottom, BorderLayout.SOUTH);
  }
  
  //--------------------------------------------------------------
  //ButtonListener: Represents an action listener for the buttons.
  //--------------------------------------------------------------
  private class ButtonListener implements ActionListener   {
    //--------------------------------------------------------------
    //actionPerformed: Determines which button was pressed and creates 
    //   the appropriate level of pacFish game.
    //--------------------------------------------------------------
    public void actionPerformed (ActionEvent event)   {
      if (event.getSource() == quitButton) {
        System.exit(0);
      } else {      
        int level = 0; //represents dimension of gameboard that will be created
        String levelname = "";
        if (event.getSource() == easy) { // We want to know which button was clicked!
          level = 25;
          levelname = "EASY";
        } else if (event.getSource() == medium) {
          level = 20;
          levelname = "MEDIUM";
        } else if (event.getSource() == hard) {
          level = 15;
          levelname = "HARD";
        }
        
        // Create new maze game with specified level of difficulty
        Window w = SwingUtilities.getWindowAncestor(PacFishStartupPanel.this); //Get existing window
        w.setVisible(false); //Get rid of existing window
        JFrame gameWindow = new JFrame("PacFish Game: Level " + levelname); ///Create new game with specified difficulty
        gameWindow.getContentPane().add(new GamePanel(level));  
        gameWindow.pack();
        gameWindow.setVisible(true);
      }
    }
  }
}

