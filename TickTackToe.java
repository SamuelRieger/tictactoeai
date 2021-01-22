import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class TickTackToe {


    public static void main(String[] args) {
        // Create board and initialize it with space values.
        char[][] board = initBoard();
        boolean[] boardState = new boolean[board.length * board.length];
        Arrays.fill(boardState, false);
        int boardFreeSpace = board.length * board.length;


        // To find board cell from user input 1 - 9.
        // First subtract 1 from input.
        // For i divide by three and floor the result.
        // For j mod the input. 

        printBoard(board);
    
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter your placement (1-9):");

            int humanPosition = input.nextInt();
            board = addSymbolToBoard(board, 'X', humanPosition);
            boardState[humanPosition - 1] = true;
            boardFreeSpace--;

            if (checkWin(board) == true) {
                break;
            }

            int aiPosition = aiMove(board, boardState, boardFreeSpace);
            board = addSymbolToBoard(board, 'O', aiPosition);
            boardState[aiPosition - 1] = true;
            boardFreeSpace--;

            printBoard(board);

            if (checkWin(board) == true) {
                break;
            }
        }
    }

    public static void initGame(String[] args) {
        // Init game.
    }

    public static char[][] initBoard() {
        // Initializes the board.
        char[][] board = new char[3][3];
        int cellCode = 49;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = (char) cellCode;
                System.out.println((char) cellCode);
                cellCode++;
            }
        }
        return board;
    }

    public static char[][] addSymbolToBoard(char[][] board, char playerSymbol, int position) {
        int iBoardPosition = (int) Math.floor((position - 1) / 3);
        int jBoardPosition = (position - 1) % 3;
        board[iBoardPosition][jBoardPosition] = playerSymbol;

        return board;
    }

    public static boolean checkWin(char[][] board) {
        // Check horizontal
        for (int i = 0; i < board.length; i++) {
            char startingSymbol = board[i][0];
            boolean win = true;
            if (startingSymbol == ' ') {
                continue;
            }
            for (int j = 1; j < board.length; j++) {
                if (board[i][j] != startingSymbol) {
                    win = false;
                    break;
                }
            }
            if (win) {
                printWin(board, startingSymbol);
                return true;
            }
        }

        // Check verticle
        for (int i = 0; i < board.length; i++) {
            char startingSymbol = board[0][i];
            boolean win = true;
            if (startingSymbol == ' ') {
                continue;
            }
            for (int j = 1; j < board.length; j++) {
                if (board[j][i] != startingSymbol) {
                    win = false;
                    break;
                }
            }
            if (win) {
                printWin(board, startingSymbol);
                return true;
            }
        }

        // Check diagonal left to right
        char startingSymbol = board[0][0];
        boolean win = true;
        if (startingSymbol != ' ') {
            for (int i = 1; i < board.length; i++) {
                if (board[i][i] != startingSymbol) {
                    win = false;
                }
            }
            if (win) {
                printWin(board, startingSymbol);
                return true;
            }
        }
        // Check diagonal right to left
        startingSymbol = board[0][board.length - 1];
        win = true;
        if (startingSymbol != ' ') {
            for (int i = 1; i < board.length; i++) {
                if (board[i][board.length - i - 1] != startingSymbol) {
                    win = false;
                }
            }
            if (win) {
                printWin(board, startingSymbol);
                return true;
            }
        }

        return false;
    }

    public static void printWin(char[][] board, char winner) {
        printBoard(board);
        System.out.println(winner + " Wins!");
    }

    public static int aiMove(char[][] board, boolean[] boardState, int boardFreeSpace) {
        int[] freeSpaceCells = new int[boardFreeSpace];
        // Make free space cells array.
        for (int i = 0; i < boardState.length; i++) {
            if (boardState[i] == false) {
                freeSpaceCells = arrayAppend(freeSpaceCells, i);
            }
        }


        int randomCellIndex = new Random().nextInt(freeSpaceCells.length);
        return freeSpaceCells[randomCellIndex] + 1;

    }

    public static int[] arrayAppend(int[] freeSpaceCells, int numToAdd) {
        for (int i = 0; i < freeSpaceCells.length; i++) {
            if (freeSpaceCells[i] == 0) {
                freeSpaceCells[i] = numToAdd;
                break;
            }
        }
        return freeSpaceCells;
    }

    public static void printBoard(char[][] board) {
        // Generate formatted board.
        // char[][] formattedBoard = {
        //     {' ', '|', ' ', '|', ' '},
        //     {'-', '+', ' ', '+', '-'},
        //     {' ', '|', ' ', '|', ' '},
        //     {'-', '+', ' ', '+', '-'},
        //     {' ', '|', ' ', '|', ' '},
        // };
        char[][] formattedBoard = {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'-', '-', '-', '-', '-', '-', '-', '+', '-', '-', '-', '-', '-', '-', '-', '+', '-', '-', '-', '-', '-', '-', '-'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'-', '-', '-', '-', '-', '-', '-', '+', '-', '-', '-', '-', '-', '-', '-', '+', '-', '-', '-', '-', '-', '-', '-'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        };

        // Insert data from board into formatted board.
        int iBoardCell = 0;
        int jBoardCell = 0;
        for (int i = 0; i < formattedBoard.length; i++) {
            if (i == 1 || (i - 1) % 4 == 0) {
                jBoardCell = 0;
                for (int j = 0; j < formattedBoard[i].length; j++) {
                    if (j == 3 || (j - 3) % 8 == 0) {
                        formattedBoard[i][j] = board[iBoardCell][jBoardCell]; 
                        jBoardCell++;
                    }
                }
                iBoardCell++;
            }
        }

        // Print formatted board.
        System.out.println();
        for (int i = 0; i < formattedBoard.length; i++) {
            for (int j = 0; j < formattedBoard[i].length; j++) {
                System.out.print(formattedBoard[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}