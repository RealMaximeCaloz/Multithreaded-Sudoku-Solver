package solver;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solver {
    private boolean isSolved = false;

    public Solver(){
    }

    // Single-threaded recursive board-solving algorithm that each thread uses
    private boolean solveBoard(Board board) {
        int[][] boardData = board.getBoardData();
        for (int cell = 0; cell < Constants.GRID_SIZE*Constants.GRID_SIZE; cell++) {
            int row = cell / Constants.GRID_SIZE;
            int column = cell % Constants.GRID_SIZE;
            if (boardData[row][column] ==  0 && !placeNumber(board, row, column)) {
                return false;
            }
        }
        if (!isSolved){
            System.out.println("Solved successfully!\n");
            System.out.println("Solved Board:");
            System.out.print(board);
            isSolved = true;
        }
        return true;
    }  

    private boolean placeNumber(Board board, int row, int column){
        int[][]boardData = board.getBoardData();
        for(int numberToTry = 1; numberToTry <= Constants.GRID_SIZE; numberToTry++){
            if (isValidPlacement(board, numberToTry, row, column)){
                boardData[row][column] = numberToTry;
                if (solveBoard(board)){
                    return true;
                }else{
                    boardData[row][column] = 0;
                }
            }
        }
        return false;
    }

    public boolean multithreadedSolveBoard(Board board) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ArrayList<Future<Boolean>> futureResults = new ArrayList<>();
        boolean zeroIsFound = findAndSolveEmptyCells(board, executorService, futureResults);

        if (!zeroIsFound && checkBoard(board)) {
            System.out.println("Solved successfully!\n");
            System.out.println("Solved Board:");
            System.out.print(board);
            executorService.shutdown();
            return true;
        }
        return processFutureResults(executorService, futureResults);
    }


    private boolean findAndSolveEmptyCells(Board board, ExecutorService executorService, ArrayList<Future<Boolean>> futureResults) {
        boolean zeroIsFound = false;
        for (int row =  0; row < Constants.GRID_SIZE && !zeroIsFound; row++) {
            for (int column =  0; column < Constants.GRID_SIZE && !zeroIsFound; column++) {
                if (board.getBoardData()[row][column] ==  0) {
                    zeroIsFound = true;
                    for (int numberToTry =  1; numberToTry <= Constants.GRID_SIZE; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, column)) {
                            Board boardCopy = board.clone();
                            boardCopy.setBoardTile(row, column, numberToTry);
                            futureResults.add(executorService.submit(() -> solveBoard(boardCopy)));
                        }
                    }
                }
            }
        }
        return zeroIsFound;
    }

    private boolean processFutureResults(ExecutorService executorService, ArrayList<Future<Boolean>> futureResults) {
        for (Future<Boolean> futureResult : futureResults) {
            try {
                if (futureResult.get()) {
                    executorService.shutdown();
                    return true;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return false;
    }

    // Umbrella method which verifies all placement constraints for a number-guess in a given sudoku cell
    private boolean isValidPlacement(Board board, int number, int row, int column) {
        boolean notInRow = !board.isNumberInRow(number, row);
        boolean notInColumn = !board.isNumberInColumn(number, column);
        boolean notInBox = !board.isNumberInBox(number, row, column);

        return notInRow && notInColumn && notInBox;
    }

    public boolean checkBoard(Board board) {
        for (int row =  0; row < Constants.GRID_SIZE; row++) {
            for (int col =  0; col < Constants.GRID_SIZE; col++) {
                int storedNumber = board.getBoardTile(row, col);
                board.setBoardTile(row, col,  0);
                if (!isValidPlacement(board, board.getBoardTile(row, col), row, col)) {
                    board.setBoardTile(row, col, storedNumber);
                    return false;
                }
                board.setBoardTile(row, col, storedNumber);
            }
        }
        return true;
    }
}