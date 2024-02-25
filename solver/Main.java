package solver;


public class Main{
  public static void main(String[] args) {

    // Initialize board and solver
    Board board = new Board();
    Solver solver = new Solver();

    // Print the initial board
    System.out.println("\nStarting Board:");
    System.out.print(board);

    // Solve the board and print the result
    if (solver.multithreadedSolveBoard(board)) {
      System.out.println("");
    }
    else {
      System.out.println("\nUnsolvable board :(\n");
    }
  }
}