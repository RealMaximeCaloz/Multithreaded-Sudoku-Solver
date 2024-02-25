package solver;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Board implements Cloneable{
    private int[][] boardData;

    public Board(){
        // Set very difficult board to solve, near impossible for humans to solve 
        int[][] defaultBoard = {
            {0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 0, 0, 3},
            {0, 7, 4, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 2},
            {0, 8, 0, 0, 4, 0, 0, 1, 0},
            {6, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 7, 8, 0},
            {5, 0, 0, 0, 0, 9, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 4, 0}
        };
        // Set the board configuration to our defaultBoard
        this.boardData = defaultBoard;
    }

    // Secondary constructor
    public Board(int[][] boardData){
        this.boardData = boardData;
    }

    // Getter for entire boardData
    public int[][] getBoardData() {
        return this.boardData;
    }

    // Setter for entire boardData
    public void setBoardData(int[][] boardData) {
        this.boardData = boardData;
    }

    // Getter for specific tile on Sudoku Board
    public int getBoardTile(int row, int col){
        return this.boardData[row][col];
    }

    // Setter for specific tile on Sudoku Board
    public void setBoardTile(int row, int col, int value){
        this.boardData[row][col] = value;
    }

    // Method to print the board at any time
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                stringBuilder.append("-----------\n");
            }
            for (int column = 0; column < Constants.GRID_SIZE; column++) {
                if (column % 3 == 0 && column != 0) {
                    stringBuilder.append("|");
                }
                stringBuilder.append(this.boardData[row][column]);
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    // Method to create copy of the Board
    public Board clone(){
        int[][] deepCopyBoard = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
        for (int row=0; row<Constants.GRID_SIZE;row++){
            for (int col=0;col<Constants.GRID_SIZE;col++){
                deepCopyBoard[row][col] = this.boardData[row][col];
            }
        }
        Board boardToReturn = new Board(deepCopyBoard);
        return boardToReturn;
    }

    public boolean isNumberInRow(int number, int row) {
        return Arrays.stream(this.boardData[row])
                .anyMatch(n -> n == number);
    }

    public boolean isNumberInColumn(int number, int column) {
        return IntStream.range(0, Constants.GRID_SIZE)
                .anyMatch(i -> this.boardData[i][column] == number);
    }

    // Method to verify if a number-guess is already present in a given sudoku 3x3 box grid
    public boolean isNumberInBox(int number, int row, int column) {
        int topLeftRowLocalBox = row - row % 3;
        int topLeftColumnLocalBox = column - column % 3;

        for (int i = topLeftRowLocalBox; i < topLeftRowLocalBox + 3; i++) {
            for (int j = topLeftColumnLocalBox; j < topLeftColumnLocalBox + 3; j++) {
                if (this.boardData[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }
}