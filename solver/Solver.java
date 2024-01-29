package solver;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solver {
    public Solver(){
    }

    // Single-threaded recursive board-solving algorithm that each thread uses
    private boolean solveBoard(Board board) {
        int[][] boardData = board.getBoardData();
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int column = 0; column < Constants.GRID_SIZE; column++) {
                if (boardData[row][column] == 0) {
                    for (int numberToTry = 1; numberToTry <= Constants.GRID_SIZE; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, column)) {
                            boardData[row][column] = numberToTry;
                            if (solveBoard(board)) {
                                return true;
                            }
                            else {
                                boardData[row][column] = 0;
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
        System.out.print(board);
        return true;
    }  

    // Umbrella multithreading method which creates an executorService thread pool.
    public boolean multithreadedSolveBoard(Board board) {
        // Create a fixed-size thread pool with a specified number of threads, along with a Future
        ExecutorService executorService = Executors.newFixedThreadPool(4);   
        ArrayList<Future<Boolean>> futureResults = new ArrayList<>();

        boolean zeroIsFound = false;
        // Create a new job if a given number-guess is valid for the board's first empty cell,
        // for every number from 1 to 9 (therefore, maximum 9 jobs)
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int column = 0; column < Constants.GRID_SIZE; column++) {
                if (board.getBoardData()[row][column] == 0) {
                    zeroIsFound = true;
                    for (int numberToTry = 1; numberToTry <= Constants.GRID_SIZE; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, column)) {
                            Board boardCopy = board.clone();
                            boardCopy.setBoardTile(row,column,numberToTry);

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
            System.out.print(board);
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
            }
            catch(InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return false;
    }


    // Umbrella method which verifies all placement constraints for a number-guess in a given sudoku cell
    private boolean isValidPlacement(Board board, int number, int row, int column) {
        return !board.isNumberInRow(number, row) &&
        !board.isNumberInColumn(number, column) &&
        !board.isNumberInBox(number, row, column);
    }

    // Method which checks if a given sudoku board has been solved
    public boolean checkBoard(Board board) {
        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            for (int j = 0; j< Constants.GRID_SIZE;j++){
                int tempNumber = board.getBoardTile(i,j);
                // Current cell checked is set to 0, so isValidPlacement does not see a duplicate number where there is none
                board.setBoardTile(i,j,0);
                if(!isValidPlacement(board, board.getBoardTile(i,j),i,j)){
                    // Current cell is set back to its value
                    board.setBoardTile(i, j, tempNumber);
                    return false;
                }
                board.setBoardTile(i, j, tempNumber);
            }
        }
        return true;
    }
}