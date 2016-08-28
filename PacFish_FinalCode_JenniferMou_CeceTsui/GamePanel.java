/****************************************************************************
  * GamePanel.java
  * Jennifer Mou & Cece Tsui
  * 2015 May
  * CS230 Final Project
  * 
  * We coded the class together. We both talked about the implementation of all the 
  *    methods and how each method would work in relation to the class. When writing
  *    the technical report, we established some pseudocode for each. However,
  *    Cece took a larger role in coding the implementation of the class.
  * 
  * The GamePanel class will create the game board on the GUI. 
  * It will create the game board using a grid of labels (a 2D array), as well as add necessary labels 
  *   and buttons for the player to utilize. It will also initialize all the necessary components of the game, 
  *   such as a LinkedList of all the fish the PacFish should eat and the Stack that keeps track of what 
  *   the PacFish can eat. 
  * This class will be the one to move the PacFish dependent upon the user’s keyboard input (only the arrow keys), 
  *   change the size of the PacFish if it levels up, and handles the game if it is over. 
  *   All other fish in the tank will move according to a timer.
  * **************************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import javafoundations.*;

public class GamePanel extends JPanel{
  //--------------------------------------------------------------
  //Instance Variables
  //--------------------------------------------------------------
  
  //Components of the game
  private int rowsAndColumns; //The size of the game board.
  private PacFish pacFish; //the player object
  private LinkedStack<Fish> whatCanBeEaten; //Keeps track of what size the PacFish can and cannot eat
  private Fish prey; //keep track of the fish the PacFish can eat
  private LinkedList<Fish> fishInTank, shark; //Store all of the fish in the tank and all the sharks that PacFish must avoid
  private JLabel[][] game; //2D array that stores all of the labels. It represents the game board (aka, the tank)
  private final int NUM_OF_EACH_TYPE = 5;
  private final int SMALLEST_FISH_SIZE = 1;
  private final int BIGGEST_FISH_SIZE = 3;
  
  //Images used for the GUI
  private final ImageIcon TITLE_IMAGE = new ImageIcon("title.gif");
  private final ImageIcon FISHHOOK_IMAGE = new ImageIcon("hook.gif");
  private final ImageIcon CONGRATS_IMAGE = new ImageIcon("congrats.gif");
  private final ImageIcon LOSE_IMAGE = new ImageIcon("lose.gif");
  
  private javax.swing.Timer timer; //move other fish in the tank acording to Timer
  private boolean startTimer; //keep track of whether the timer has started already
  
  //Components of GUI
  private final int CELL_SIZE = 30; //size of labels on the game board; ensures all labels on grid will have the same size
  private JButton startOverButton, quitButton;
  private JLabel countLabel, statusLabel, targetPicLabel, titleLabel, targetWordsLabel, avoidLabel;
  private JPanel gameBoard, information;
  private DirectionListener movementListener; // Listens for keyboard events
  
  //--------------------------------------------------------------
  //CONSTRUCTOR: Takes in integer value that represents what level the user chose.
  //   Initializes values and builds the GUI based on the level 
  //   (game board gets smaller as the difficulty increases) 
  //--------------------------------------------------------------
  public GamePanel(int level) { 
    
    /**GUI COMPONENTS**/
    rowsAndColumns = level; //gameboard size
    
    setLayout(new BorderLayout());
    
    //Create gameBoard panel
    gameBoard = new JPanel();
    gameBoard.setBackground(new Color(154, 225, 255));
    gameBoard.setLayout(new GridLayout(rowsAndColumns,rowsAndColumns));
    
    //make the gameboard using 2D array
    game = new JLabel[rowsAndColumns][rowsAndColumns];
    for (int r = 0; r < rowsAndColumns; r++) {
      for (int c = 0; c < rowsAndColumns; c++) {
        //for each cell in the 2D array, create an empty JLabel
        game[r][c] = new JLabel();
        //Set Size so all are same
        game[r][c].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        game[r][c].setMinimumSize(new Dimension(CELL_SIZE, CELL_SIZE));
        game[r][c].setMaximumSize(new Dimension(CELL_SIZE,CELL_SIZE));
        //Add cell to game board using grid layout
        gameBoard.add(game[r][c]);
      }
    }
    
    add(gameBoard, BorderLayout.CENTER);
    
    //Create information panel side (where buttons/instructions will be)
    information = new JPanel();
    information.setLayout(new BorderLayout());
    information.setPreferredSize(new Dimension(TITLE_IMAGE.getIconWidth(),CELL_SIZE*rowsAndColumns));
    information.setMaximumSize(new Dimension(TITLE_IMAGE.getIconWidth(), CELL_SIZE*rowsAndColumns));
    information.setBackground(Color.white);
    
    //Upper Panel in information panel - contains all the writing
    JPanel info1 = new JPanel();
    info1.setLayout(new BoxLayout(info1, BoxLayout.Y_AXIS));
    info1.setBackground(Color.white);
    
    //Create and add title of game
    titleLabel = new JLabel();
    titleLabel.setIcon(TITLE_IMAGE);
    info1.add(titleLabel);
    info1.add(Box.createRigidArea(new Dimension(0,5)));
    
    //Create and add label that tells target fish
    targetWordsLabel = new JLabel("<html><font face=\"Comic Sans MS\">Your Target Fish:</font></html>");
    targetWordsLabel.setMaximumSize(new Dimension(TITLE_IMAGE.getIconWidth(), 50)); //Set width as title image so that the width is the same as
                                                                                //   the title image above for aesthetic purposes (same for labels below)
    targetWordsLabel.setHorizontalAlignment(JLabel.CENTER);
    info1.add(targetWordsLabel);
    
    //Create and add label that shows picture of target fish
    targetPicLabel = new JLabel();
    targetPicLabel.setMaximumSize(new Dimension(TITLE_IMAGE.getIconWidth(),350));
    targetPicLabel.setHorizontalAlignment(JLabel.CENTER);
    info1.add(targetPicLabel);
    
    //Create and add label that tells user what they need to avoid.
    avoidLabel = new JLabel("<html><font face=\"Comic Sans MS\">Avoid all other fish!</font></html>");
    avoidLabel.setMaximumSize(new Dimension(TITLE_IMAGE.getIconWidth(),50));
    avoidLabel.setHorizontalAlignment(JLabel.CENTER);
    info1.add(avoidLabel);
    
    info1.add(Box.createRigidArea(new Dimension(0,10))); //Space between next label
    
    //Create and add label that tells user how many of the target fish they have eaten (starts off as 0 since pacFish has not eaten yet)
    countLabel = new JLabel("<html><font face=\"Comic Sans MS\" size=\"10\">0/5</font></html>");
    countLabel.setMaximumSize(new Dimension(TITLE_IMAGE.getIconWidth(),50));
    countLabel.setHorizontalAlignment(JLabel.CENTER);
    info1.add(countLabel);
    
    info1.add(Box.createRigidArea(new Dimension(0,5))); //Space between next label
    
    //Create and add label that is initially the instructions. It later informs user of leveling up
    statusLabel = new JLabel("<html><font face=\"Comic Sans MS\">Welcome to PacFish!<br><br>Using your arrow keys, navigate around the tank to try " +
                             "to eat your target fish (pictured above) while avoiding all other fish and fish hooks. As you level up, sharks will also appear. Watch out " + 
                             "for them as well! You can use the space bar to pause the game if you need to.<br>After eating five of the target fish, you will " +
                             "level up and be able to eat the next type. Keep in mind your goal is to be the last fish standing! Goodluck!</font></html>");
    statusLabel.setHorizontalAlignment(JLabel.CENTER);
    info1.add(statusLabel);
    
    information.add(info1,BorderLayout.NORTH);
    
    //Bottom Panel in information panel - contains all the buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.white);
   
    //StartOver Button
    startOverButton = new JButton("Start Over");
    startOverButton.addActionListener(new ButtonListener());
    buttonPanel.add(startOverButton);
    
    //Quit Button
    quitButton = new JButton("Quit");
    quitButton.addActionListener(new ButtonListener());
    buttonPanel.add(quitButton);
    information.add(buttonPanel,BorderLayout.SOUTH);
    
    add(information, BorderLayout.EAST);
    
    //Add KeyListener to listen for keyboard events, i.e., arrow keys
    movementListener = new DirectionListener();
    addKeyListener(movementListener);
    setFocusable(true); //Allows panel to listen to KeyListener
    
    /**GAME COMPONENTS**/
    
    //Create the player (user) pacFish
    pacFish = new PacFish(0,0);
    game[0][0].setIcon(pacFish.getImage()); //Add on gameboard
    
    //Add all fish pacFish should eat on gameboard (5 of each type (3 types))
    fishInTank = new LinkedList<Fish>(); //LinkedList that stores all fish in tank
    for (int i = SMALLEST_FISH_SIZE; i <= BIGGEST_FISH_SIZE; i++) {
      for (int j = 0; j < NUM_OF_EACH_TYPE; j++) {
        Fish addFish = new Fish(i);
        randomlyPlaceComponent(addFish, addFish.getImage());
        fishInTank.add(addFish);
      }
    }

    shark = new LinkedList<Fish>(); //LinkedList that stores all the fish in the tank
    
    //Add all fish hooks into the tank (number of fish hooks is always five less than rowsAndColumns)
    for (int i = 0; i < rowsAndColumns-5; i++) {
      randomlyPlaceComponent(null, FISHHOOK_IMAGE);
    }
    
    //Create stack that keeps track of what pacFish eats; Smallest size popped first.
    whatCanBeEaten = new LinkedStack<Fish>();
    for (int k = BIGGEST_FISH_SIZE; k >= SMALLEST_FISH_SIZE ; k--) {
      whatCanBeEaten.push(new Fish(k));
    }
    prey = whatCanBeEaten.pop();
    
    //Show user what first prey is
    targetPicLabel.setIcon(prey.getImage());
    
    //Create Timer Listener to have fish moving every 200 milliseconds
    TimerListener t_listener = new TimerListener();
    timer = new javax.swing.Timer(200,t_listener);
    startTimer = false; //Does not start right away
  }
  
  //--------------------------------------------------------------
  //HELPER METHOD (moveFish): takes in Fish object, and integer values representing 
  //   the change in row and column value of the Fish
  //   movement in the game board. It will move the Fish accordingly.
  //--------------------------------------------------------------
  private void moveFish(Fish fish, int row, int col) {
    //Get new values of where the fish will move to
    int r = fish.getPlacementR() + row;
    int c = fish.getPlacementC() + col;
    
    //Move the fish -- set previous placement to null & new to image
    game[fish.getPlacementR()][fish.getPlacementC()].setIcon(null);
    fish.setPlacementR(r);
    fish.setPlacementC(c);
    game[r][c].setIcon(fish.getImage());
  }
  
  //--------------------------------------------------------------
  //moveAllFish: This method takes in a LinkedList of Fish --> it traverses the list. 
  //   For each fish, it randomly generates a movement. It will then check if the new placement of the fish is 
  //   within the game board and will not overlap another fish, shark, or fish hook. 
  //   If so, it will call the helper method moveFish to move this individual fish. 
  //   It will also check if the new placement is where PacFish is. 
  //   If they do overlap and the size of the fish is bigger than the PacFish, we handle game over. 
  //--------------------------------------------------------------
  public void moveAllFish(LinkedList<Fish> fishToEat) {
    for (int i = 0; i < fishToEat.size(); i++) {
      Fish fish = fishToEat.get(i);
      Random rand = new Random();
      //Get numeric value corresponding to how fish should move
      int move = rand.nextInt(4);
      int newR = 0;
      int newC = 0;
      if (move == 0) { //Up
        newR++;
      } else if (move == 1) { //Down
        newR--;
      } else if (move == 2) { //Left
        newC--;
      } else { //Right
        newC++;
      }
      //Make sure new placement is within the gameboard
      if (fish.getPlacementR() + newR >= 0 && fish.getPlacementR() + newR < rowsAndColumns) {
        if (fish.getPlacementC() + newC >= 0 && fish.getPlacementC() + newC < rowsAndColumns) {
          //Make sure the new placement would not overlap an object
          if (game[fish.getPlacementR() + newR][fish.getPlacementC() + newC].getIcon() == null) {
            moveFish(fish, newR, newC);
          } else {
            //If it would, chekck if it is the pacFish and if the fish's size is bigger. If so, the game would be over.
            if (game[fish.getPlacementR() + newR][fish.getPlacementC() + newC].getIcon() == pacFish.getImage()) {
              if(fish.getSize() > pacFish.getSize()) {
                handleGameOver(0);
              }
            }
          }
        }
      }
    }
  }
  
  //--------------------------------------------------------------
  //movePacFish: This method takes in integer values representing the change in row and column value.
  //   Moves player pacFish accordingly
  //--------------------------------------------------------------
  public void movePacFish(int row, int col) {
    //Update pacFish's location based on what key user pressed 
    int newR = pacFish.getPlacementR() + row;
    int newC = pacFish.getPlacementC() + col;
    
    //If the new Placements are within the gameboard
    if (newR >= 0 && newR < rowsAndColumns) {
      if (newC >= 0 && newC < rowsAndColumns) { 
        //If it collides with a fish
        if (game[newR][newC].getIcon() != null && game[newR][newC].getIcon() != FISHHOOK_IMAGE) {
          for (int i = 0; i < fishInTank.size(); i ++) { //loop through the linked list
            Fish check = fishInTank.get(i);
            //For each fish, check if the pacfish's new coordinate values is equal to the coordinate value of the fish
            //    (it is the fish trying to be eaten)
            if (check.getPlacementR() == newR && check.getPlacementC() == newC) {
              //compare sizes and see who gets eaten
              if (check.getSize() == pacFish.getSize()) {//if pacfish's size equals to the size of the fish it collided with
                fishInTank.remove(check);//pacfish eats the fish it ran into
                moveFish(pacFish, row, col); //pacfish can move to new position in gamebaord
                pacFish.increaseNumFishEaten();
                countLabel.setText("<html><font face=\"Comic Sans MS\"size=\"10\">" + pacFish.getNumFishEaten() + "/5</font></html>");
                break; //No longer needs to loop through
              } else { //if pacfish collided with a bigger fish
                handleGameOver(0); //game is over
              }
            }
          }
          //If it collides with a fish hook, game is over
        } else if (game[newR][newC].getIcon() == FISHHOOK_IMAGE) {
          handleGameOver(0);
        } else {
          //No collision - move pacFish
          moveFish(pacFish,row,col);
        }
      }
    }
  }
  
  //--------------------------------------------------------------
  //handleGameOver: Handles when the game is over. There are two cases: 
  //   if the PacFish wins by eating all the fish in the tank (parameter = 1), 
  //   or if the PacFish loses by dying (parameter = 0). It will remove PacFish
  //   from the tank,changes the labels accordingly, and stop all movement in
  //   the tank.
  //--------------------------------------------------------------
  public void handleGameOver(int loseOrWin) {
    //If pacFish loses
    if (loseOrWin == 0) { 
      targetPicLabel.setIcon(LOSE_IMAGE);
      statusLabel.setText("<html><font face=\"Comic Sans MS\"><b>Game Over. You Lose.</b></font></html>");
      //Make pacFish disappear from gameboard (died)
      game[pacFish.getPlacementR()][pacFish.getPlacementC()].setIcon(null);
    } else { //If pacFish wins
      targetPicLabel.setIcon(CONGRATS_IMAGE);
      statusLabel.setText("<html><font face=\"Comic Sans MS\"><b>Congratulations! You win!</b></font></html>"); 
    }
    removeKeyListener(movementListener);  // Disable keys so pacFish cannot move
    //Remove labels
    targetWordsLabel.setText("");
    countLabel.setText("");
    avoidLabel.setText("");
    //Stop all fish in tank from moving
    timer.stop();
  }
  
  //--------------------------------------------------------------
  // handleSizeChange: If PacFish eats all of the fish it can eat at the time in the tank, it will level up. 
  // The status and count label changes to represent the new level. A new prey is selected, 
  //   and at each level after level 1, sharks are added to the tank. 
  //--------------------------------------------------------------
  public void handleSizeChange() {
    //If pacFish has eaten all of the type that it should eat
    if(pacFish.getNumFishEaten() % 5 == 0 && pacFish.getNumFishEaten() != 0) { 
      //Level up (increase size)
      pacFish.levelUp();
      statusLabel.setText("<html><font face=\"Comic Sans MS\" size=\"5\">You Leveled Up to Level " + pacFish.getSize() + "!</font></html>");
      countLabel.setText("<html><font face=\"Comic Sans MS\" size=\"10\">" + pacFish.getNumFishEaten() + "/5</font></html>");
      
      //Get new prey (target)
      prey = whatCanBeEaten.pop();
      targetPicLabel.setIcon(prey.getImage());
      
      //As level up, add shark in amount of how many fish are left in the tank
      for (int i = 0; i < fishInTank.size(); i++) {
        Fish addShark = new Fish(4);
        randomlyPlaceComponent(addShark, addShark.getImage());
        shark.add(addShark);
      }
    }
  }
  
  //--------------------------------------------------------------
  //HELPER METHOD (randomlyPlaceComponent): Places the image on the
  //   gameboard randomly and attaches the placement to the image's 
  //   Fish object if there is a Fish object to attach it to (such
  //   as fish vs.fish hook.)
  //   For example, when placing a fish hook, the first parameter
  //   will be null since there is no object attached to it. But,
  //   for a Fish, we must keep track of the Fish's placement, and so
  //   both arguments will be filled.
  //--------------------------------------------------------------
  private void randomlyPlaceComponent(Fish place, ImageIcon image) {
      Random rand = new Random();
      //Find random row and column (placement)
      int r = rand.nextInt(rowsAndColumns);
      int c = rand.nextInt(rowsAndColumns);
      //While the placement already has an object
      while (game[r][c].getIcon() != null) {
        //Get new random placement
        r = rand.nextInt(rowsAndColumns);
        c = rand.nextInt(rowsAndColumns);
      }
      //If there is a Fish object to attach placement to, set it
      if (place != null) {
        place.setPlacementC(c);
        place.setPlacementR(r);
      }
      //Set the image on the gameboard
      game[r][c].setIcon(image);
  }
  
  
  
  //--------------------------------------------------------------
  //  DirectionListener: Represents the listener for keyboard activity.
  //--------------------------------------------------------------
  private class DirectionListener implements KeyListener {
    //--------------------------------------------------------------
    // keyPressed: Responds to the user pressing arrow keys by adjusting the
    //   pacFish location accordingly.
    //   It also handles when the game is paused or unpaused. 
    //   It also checks if the tank does not have any more fish.
    //--------------------------------------------------------------
    public void keyPressed (KeyEvent event)   { 
      //Make sure valid key is pressed
      if(event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN
           || event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT) {
        //If spacebar is pressed (pause)
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
          if (startTimer == true) {
            //If it is not paused, pause
            timer.stop();
            startTimer = false;
            //If it is paused, unpause
          } else if (startTimer == false) {
            timer.start();
            startTimer = true;
          }
        } else {
          if (event.getKeyCode() == KeyEvent.VK_UP) {  // Up arrow pressed
            movePacFish(-1,0);
          } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {  // Down arrow pressed
            movePacFish(1,0);
          } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {  // Left arrow pressed
            movePacFish(0,-1);
          } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {  // Right arrow pressed
            movePacFish(0,1);
          }
          //Deal with beginning of game: fish only start moving when user presses a button
          //  When paused, the game can also start again by pressing the arrow key
          if (startTimer == false) {
            timer.start();
            startTimer = true;
          } 
          //After pacFish is done eating, handleSizeChange if applicable (ate all of the target fish (checked for within handleSizeChange))
          if (fishInTank.size() != 0) {
            handleSizeChange();
          } else { //if there are no more fish in the tank, end the game
            timer.stop();
            handleGameOver(1);
          }
        }
      }
    }
    
    //--------------------------------------------------------------
    //  Provide empty definitions for unused event methods.
    //--------------------------------------------------------------
    public void keyTyped (KeyEvent event) {}
    public void keyReleased (KeyEvent event) {}
  }
  
  //--------------------------------------------------------------
  //  TimerListener: Listener that will perform actions after given
  //     milliseconds (we indicated 200 milliseconds)
  //--------------------------------------------------------------
  private class TimerListener implements ActionListener
  {
    //--------------------------------------------------------------
    //actionPerformed: Determines what to do every tick of the timer.
    //--------------------------------------------------------------
    public void actionPerformed (ActionEvent event)
    {
      //move all of the fish -- shark and fish
      moveAllFish(fishInTank);
      moveAllFish(shark);
    }
  }
  
  //--------------------------------------------------------------
  //  ButtonListener: Represents the listener for button activity.
  //--------------------------------------------------------------
  private class ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      if (event.getSource() == quitButton) {
        System.exit(0);
      } else if (event.getSource() == startOverButton) {
        //With start over, close existing window
        Window w = SwingUtilities.getWindowAncestor(GamePanel.this);
        w.setVisible(false);
        //Create new frame with the PacFishStartupPanel to begin new game
        JFrame startupWindow = new JFrame();
        startupWindow.getContentPane().add(new PacFishStartupPanel());
        startupWindow.pack();
        startupWindow.setVisible(true);
      }
    }
  }
  
  //--------------------------------------------------------------
  // Main method: Used for preliminary testing
  //--------------------------------------------------------------
  public static void main (String[] args) {
    JFrame frame = new JFrame ("Test");
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
    frame.getContentPane().add(new GamePanel(25));
    
    frame.pack();
    frame.setVisible(true);
  }
}


