/****************************************************************************
  * Fish.java
  * Jennifer Mou & Cece Tsui
  * 2015 May
  * CS230 Final Project
  * 
  * We coded the class together. We both talked about the implementation of all the 
  *    methods and how each method would work in relation to the class. When writing
  *    the technical report, we established some pseudocode for each. However,
  *    Jennifer took a larger role in coding the implementation of the class.
  * 
  * The Fish class will be a representation of the fish-computer players on the game board. 
  * It will keep track of where the fish is on the game board, whether or not it has been eaten, 
  *   its size, and the picture of the fish. 
  * This class will make it so that it all the information is stored easily within the class and 
  *   can be retrieved simply with a method
  * **************************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Fish {
  //--------------------------------------------------------------
  //Instance Variables
  //--------------------------------------------------------------
  private int placement_r; //Fish placement on game board (row value)
  private int placement_c; //Fish placement on game board (column value)
  private int size; //the bigger the int value, the bigger the fish is
  private ImageIcon fishPic; //stores image that will represent fish on the GUI of the gamebaord
  
  //Array containing String names of all the fish images ordered from smallest to largest
  private final String[] FISHES = {"fish_1.gif", "fish_2.gif", "fish_3.gif","shark.gif"}; 
  
  //--------------------------------------------------------------
  //CONSTRUCTOR: Takes in integer variable for size, row value, and 
  //   column value. Sets size and image of fish depending on size
  //   place fish in gameboard based on provided arguments.
  //--------------------------------------------------------------
  public Fish(int size, int row, int col) {
    this.size = size;
    placement_r = row;
    placement_c = col;
    fishPic =  new ImageIcon(FISHES[(this.size - 1)%FISHES.length]);
  }
  
  //--------------------------------------------------------------
  //CONSTRUCTOR: Takes in integer variable for size.
  //   Set size of fish; the placement of the fish is set to be off the 
  //   gamebaord. FishPic is initialized to be an image dependent on the 
  //   given size.  
  //--------------------------------------------------------------
  public Fish(int size) {
    this.size = size;
    placement_r = -1;
    placement_c = -1;
    fishPic = new ImageIcon(FISHES[this.size-1]);
  }
  
  //--------------------------------------------------------------
  //GETTERS AND SETTERS
  //--------------------------------------------------------------
  
  //SETTER METHOD: set new image 
  public void setImage(ImageIcon pic) { //to use in PacFish class
    fishPic = pic;
  }
  
  //GETTER METHOD: returns ImageIcon of the fish
  public ImageIcon getImage() {
    return fishPic;
  }
  
  //SETTER METHOD: takes in new location of fish and set new placement of fish on the game board - row
  public void setPlacementR(int newPlacement) {
    placement_r = newPlacement;
  }
  
  //GETTER METHOD: returns placement of fish on the game board - row value
  public int getPlacementR() {
    return placement_r;
  }
  
  //SETTER METHOD: takes in new location of fish and set new placement of fish on the game board - column
  public void setPlacementC(int newPlacement) {
    placement_c = newPlacement;
  }
  
  //GETTER METHOD: returns placement of fish on the game board - column value
  public int getPlacementC() {
    return placement_c;
  }
 
  //SETTER METHOD: set size of fish
  public void setSize(int s) {
    size = s;
  }
  
  //GETTER METHOD: returns size of fish 
  public int getSize() {
    return size;
  }
  
  //--------------------------------------------------------------
  //Main method: Used for preliminary testing
  //--------------------------------------------------------------
  public static void main(String[] args) {
    Fish f = new Fish(2,6,9);
    System.out.println(f.getPlacementR());
    System.out.println(f.getPlacementC());
    System.out.println(f.getSize());
  }
}