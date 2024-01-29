package solver;

// Start Main class
public class Main{
  // Start Main Method
  public static void main(String[] args) {
   
    // Initialize Board object
    Board board = new Board();

    // Initialize Solver
    Solver mySolver = new Solver();

    // Print the initial board to solve on screen
    System.out.println("");
    System.out.println("Starting Board:");
    System.out.print(board);;
  
    // Solve the board if possible, and print the result
    if (mySolver.multithreadedSolveBoard(board)) {
      System.out.println(""); 
    }
    else {
      System.out.println("");
      System.out.println("Unsolvable board :(");
      System.out.println("");
    }
  }
}