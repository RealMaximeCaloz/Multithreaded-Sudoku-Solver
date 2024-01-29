package solver;

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
    public int getBoardTile(int i, int j){ 
        return this.boardData[i][j];
    }

    // Setter for specific tile on Sudoku Board
    public void setBoardTile(int i, int j, int value){ 
        this.boardData[i][j] = value; 
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
        // Method which creates a deep copy of the initial board to solve,
        // to feed this copy into a separate thread
        int[][] deepCopyBoard = new int[9][9];
            for (int i=0; i<9;i++){
                for (int j=0;j<9;j++){
                    deepCopyBoard[i][j] = this.boardData[i][j];
                }
            }
            Board boardToReturn = new Board(deepCopyBoard);
            return boardToReturn;
    }

    // Method to verify if a number-guess is already present in a given sudoku row
    public boolean isNumberInRow(int number, int row) {
        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            if (this.boardData[row][i] == number) {
                return true;
            }
        }
        return false;
    }
    
    // Method to verify if a number-guess is already present in a given sudoku column
    public boolean isNumberInColumn(int number, int column) {
        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            if (this.boardData[i][column] == number) {
                return true;
            }
        }
        return false;
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