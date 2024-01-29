# Multithreaded Sudoku Solver
# Project Overview

This project is separated into 2 main components: 
1) a single-threaded, recursive Sudoku-solving algorithm;
2) a multithreaded umbrella method which organizes the entire program and lets threads make use of the powerful single-threaded solving algorithm.

The code is factored into 4 files:
  * Main.java
  * Solver.java (holds most of the logic)
  * Board.java (holds the board, printing, duplication, and validation methods)
  * Constants.java

## Single-Threaded Recursive Algorithm

The single-threaded algorithm is encapsulated in a method which recursively calls itself until the board is completely solved, or it is deemed that it cannot be solved at all.

This algorithm iterates over each and every cell of the board, stopping at the first empty cell it encounters (reprensented by a 0 in the Sudoku board array).

![Starting Board](https://github.com/RealMaximeCaloz/Multithreaded-Sudoku-Solver/blob/923aaa282b0b6b0391153eec9a5bc03448b2fa20/startingboard.png)

On the empty cell, the algorithm tests the numbers from 1 to 9 to see if there is a valid guess (if the guess is not already in the same row, column, or 3x3 grid as the empty cell).

On the first valid guess, the algorithm sets the empty cell to the valid guess (e.g. 3). The method will then recursively call itself, and iterate through the modified board (initial board + new guess).
The recursive algorithm will keep making new guesses in a structured way, until the the board is solved, or the algorithm ends up finding none of the numbers from 1 to 9 are valid for a given cell.

In the case the board cannot find any valid guesses for a given empty cell, it will employ backtracking, and erase the last guess; instead of the last guess, the algorithm will guess one of the remaining possible valid numbers.
After the previous guess has been corrected, the algorithm will attempt to solve the next empty cell again.

This backtracking process will keep occuring until the entire Sudoku board is completely solved.

## Multithreaded Umbrella Method

The multithreaded umbrella method is an encapsulating method which starts the Sudoku board solving process, starts a thread pool, and allocates jobs to different threads.

This method starts by reading the board until it reaches the first empty cell. At the first empty cell, the list of valid guesses will be found, containing numbers from 1 to 9.
For each valid guess, a new job calling the single-threading recursive algorithm will be submitted to the thread pool.

Effectively, multiple threads can attempt to solve the Sudoku board at the same time in multithreaded parallel processing, with each thread starting with a different initial guess for the very first empty cell.

In the end, the result of the job that solved the board is returned, and printed for the user.

![Final Board](https://github.com/RealMaximeCaloz/Multithreaded-Sudoku-Solver/blob/923aaa282b0b6b0391153eec9a5bc03448b2fa20/finalboard.png)

# Installation
1. Clone this repository:
```
$ git clone https://github.com/RealMaximeCaloz/Multithreaded-Sudoku-Solver.git
``` 

# How to Run
1. Input your Sudoku board in the `defaultBoard` array at the top of `Board.java`. Use `0` to represent empty cells.
2. Run `Main.java`.
3. The result will be printed in your terminal.
