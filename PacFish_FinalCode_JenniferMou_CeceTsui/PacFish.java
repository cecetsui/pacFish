/****************************************************************************
  * PacFish.java
  * Jennifer Mou & Cece Tsui
  * 2015 May
  * CS230 Final Project
  * 
  * We coded the class together. We both talked about the implementation of all the 
  *    methods and how each method would work in relation to the class. When writing
  *    the technical report, we established some pseudocode for each. However,
  *    Jennifer took a larger role in coding the implementation of the class.
  * 
  * PacFish class inherits from the Fish class. 
  * This will be a representation of the PacFish player (otherwise the main player) in the game. 
  * It will not only keep track of the placement and size, as in its parent class, but it will also keep track of
  * the number of fish that it has eaten and be able to change its size (to show that it levels up).
  * **************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PacFish extends Fish {
  //--------------------------------------------------------------
  //Instance Variables
  //--------------------------------------------------------------
  private int numFishEaten; //keeps track of number of fish PacFish has eaten 
  private final ImageIcon PACFISH_IMAGE= new ImageIcon("pacfish.gif"); //picture of player pacFish
  
  //--------------------------------------------------------------
  //CONSTRUCTOR: Takes in integer values representing row and column 
  //   placement. Initialize values accordingly (utilizing constructor 
  //   from parent class)
  //--------------------------------------------------------------
  public PacFish(int row, int col) {
    super(1,row,col);
    setImage(PACFISH_IMAGE);
    numFishEaten = 0;
  }
  
  //--------------------------------------------------------------
  //GETTER METHOD: returns number of fish pacFish has eaten
  //--------------------------------------------------------------
  public int getNumFishEaten() {
    return numFishEaten;
  }
  
  //--------------------------------------------------------------
  //increaseNumFishEaten: this method increments the value of numFishEaten
  //--------------------------------------------------------------
  public void increaseNumFishEaten() {
    numFishEaten++;
  }

  //--------------------------------------------------------------
  //levelUp: Sets size of PacFish as it gets bigger/levels up and 
  //    sets numFishEaten to 0 as it did not eat any of the 
  //    target after leveling up.
  //--------------------------------------------------------------
  public void levelUp() {
    setSize(getSize()+1);
    numFishEaten = 0;
  }
}
