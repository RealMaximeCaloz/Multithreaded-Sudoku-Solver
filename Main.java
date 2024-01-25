// Import relevant libraries for ArrayLists and Multithreading
import java.util.ArrayList;
import java.util.concurrent.*;

// Start Main class
public class Main{

  // Set the Grid Size of the Sudoku Board
  private static final int GRID_SIZE = 9;
  
  // Start Main Method
  public static void main(String[] args) {
   
    // Set very difficult board to solve, near impossible for humans to solve 
    int[][] board = 
    {   {0, 2, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 6, 0, 0, 0, 0, 3},
        {0, 7, 4, 0, 8, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 3, 0, 0, 2},
        {0, 8, 0, 0, 4, 0, 0, 1, 0},
        {6, 0, 0, 5, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 7, 8, 0},
        {5, 0, 0, 0, 0, 9, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 4, 0}};

    // Print the initial board to solve on screen
    System.out.println("");
    System.out.println("Starting Board:");
    printBoard(board);
  
    // Solve the board and print the result
    if (multithreadedSolveBoard(board)) {
      System.out.println(""); 
    }
    else {
      System.out.println("");
      System.out.println("Unsolvable board :(");
      System.out.println("");
    }
  }
  
  // Method to print the board at any time
  private static void printBoard(int[][] board) {
    for (int row = 0; row < GRID_SIZE; row++) {
      if (row % 3 == 0 && row != 0) {
        System.out.println("-----------");
      }
      for (int column = 0; column < GRID_SIZE; column++) {
        if (column % 3 == 0 && column != 0) {
          System.out.print("|");
        }
        System.out.print(board[row][column]);
      }
      System.out.println();
    }
    System.out.println();
  }

  // Method to verify if a number-guess is already present in a given sudoku row
  private static boolean isNumberInRow(int[][] board, int number, int row) {
    for (int i = 0; i < GRID_SIZE; i++) {
      if (board[row][i] == number) {
        return true;
      }
    }
    return false;
  }
  
  // Method to verify if a number-guess is already present in a given sudoku column
  private static boolean isNumberInColumn(int[][] board, int number, int column) {
    for (int i = 0; i < GRID_SIZE; i++) {
      if (board[i][column] == number) {
        return true;
      }
    }
    return false;
  }
 
  // Method to verify if a number-guess is already present in a given sudoku 3x3 box grid
  private static boolean isNumberInBox(int[][] board, int number, int row, int column) {
    int topLeftRowLocalBox = row - row % 3;
    int topLeftColumnLocalBox = column - column % 3;
    
    for (int i = topLeftRowLocalBox; i < topLeftRowLocalBox + 3; i++) {
      for (int j = topLeftColumnLocalBox; j < topLeftColumnLocalBox + 3; j++) {
        if (board[i][j] == number) {
          return true;
        }
      }
    }
    return false;
  }
  
  // Umbrella method which verifies all placement constraints for a number-guess in a given sudoku cell
  private static boolean isValidPlacement(int[][] board, int number, int row, int column) {
        return !isNumberInRow(board, number, row) &&
        !isNumberInColumn(board, number, column) &&
        !isNumberInBox(board, number, row, column);
  }

  // Method which checks if a given sudoku board has been solved
  private static boolean checkBoard(int[][] board) {
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j<GRID_SIZE;j++){
        int tempNumber = board[i][j];
        board[i][j]=0;
        if(!isValidPlacement(board, board[i][j],i,j)){
          board[i][j] = tempNumber;
          return false;
        }
        board[i][j] = tempNumber;
      }
      
    }
    return true;
  }

  // Method which creates a deep copy of the initial board to solve,
  // to feed this copy into a separate thread
  private static int[][] deepCopyBoard(int[][]board){
    int[][] deepCopyBoard = new int[9][9];
    for (int i=0; i<9;i++){
      for (int j=0;j<9;j++){
        deepCopyBoard[i][j] = board[i][j];
      }
      
    }
    return deepCopyBoard;
  }

  // Single-threaded board solving algorithm that each thread uses
  private static boolean solveBoard(int[][] board) {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int column = 0; column < GRID_SIZE; column++) {
        if (board[row][column] == 0) {
          for (int numberToTry = 1; numberToTry <= GRID_SIZE; numberToTry++) {
            if (isValidPlacement(board, numberToTry, row, column)) {
              board[row][column] = numberToTry;
              
              if (solveBoard(board)) {
                return true;
              }
              else {
                board[row][column] = 0;
              }
            }
          }
          return false;
        }
      }
    }
    System.out.println("");
    System.out.println("Solved successfully!");
    System.out.println("");
    System.out.println("Solved Board:");
    printBoard(board);
    return true;
  }  

  // Umbrella multithreading method which creates an executorService thread pool.
  private static boolean multithreadedSolveBoard(int[][] board) {
    // Create a fixed-size thread pool with a specified number of threads, along with a Future
    ExecutorService executorService = Executors.newFixedThreadPool(4);   
    ArrayList<Future<Boolean>> futureResults = new ArrayList<>();
    
    boolean zeroIsFound = false;
    // Create a new job if a given number-guess is valid for the board's first empty cell,
    // for every number from 1 to 9 (therefore, maximum 9 jobs)
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int column = 0; column < GRID_SIZE; column++) {
        if (board[row][column] == 0) {
          zeroIsFound = true;
          for (int numberToTry = 1; numberToTry <= GRID_SIZE; numberToTry++) {
            if (isValidPlacement(board, numberToTry, row, column)) {
              int[][]boardCopy = deepCopyBoard(board);
              boardCopy[row][column]=numberToTry;

              futureResults.add(executorService.submit(() -> solveBoard(boardCopy)));
            }
          }
          break;
        }
      }
      if (zeroIsFound){
        break;
      }
    }

    // If the board is already solved, print it and shutdown the thread pool
    if (!zeroIsFound && checkBoard(board)){
        printBoard(board);
        executorService.shutdown();
        return true;
    }
    
    // Check all future results. If one has solved a board, retrieve it and shutdown the thread pool.
    for (int i=0; i<futureResults.size();i++){
      try{
          // Obtain the result of the task done by the thread that solved the board
          boolean result = futureResults.get(i).get();
          if (result==true){
            executorService.shutdown();
            return true;
          }
      }catch(InterruptedException | ExecutionException e){
      e.printStackTrace();
      }
    }
       executorService.shutdown();
       return false;
  }
}