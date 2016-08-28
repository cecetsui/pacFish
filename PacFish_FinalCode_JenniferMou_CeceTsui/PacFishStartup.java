/****************************************************************************
  * PacFishStartup.java
  * Jennifer Mou & Cece Tsui
  * 2015 May
  * CS230 Final Project
  * 
  * We coded the class together. 
  * 
  * This class runs the entire PacFish game. 
  * It launches the PacFishStartupPanel, and the user interacts with that.
  * **************************************************************************/
import javax.swing.JFrame;

public class PacFishStartup {
   //-----------------------------------------------------------------
   //  Creates and displays the main program frame.
   //-----------------------------------------------------------------
   public static void main (String[] args) {
      JFrame frame = new JFrame ("PacFish Game Startup Window");
      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

      frame.getContentPane().add(new PacFishStartupPanel());

      frame.pack();
      frame.setVisible(true);
   }
}
