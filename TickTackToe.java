import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class TickTackToe {

    private static int numberOfEndStatesSearched = 0;

    public static void main(String[] args) {
        // Create board and initialize it with space values.
        char[][] board = initBoard();
        boolean[] boardState = new boolean[board.length * board.length];
        Arrays.fill(boardState, false);
        int boardFreeSpace = board.length * board.length;
        Scanner input = new Scanner(System.in);

        System.out.println("\nOponents to play against...\n\n" + 
                        "1 - Random ai.\n" +
                        "2 - Minimax ai without optimization.\n" +
                        "3 - Minimax ai with optimization.\n");
        System.out.print("Select an oponent: ");

        int aiSelection = input.nextInt();

        printBoard(board);
    
        while (true) {
            System.out.print("Enter your placement (1-9): ");

            int humanPosition = input.nextInt();
            board = addSymbolToBoard(board, 'X', humanPosition);
            boardState[humanPosition - 1] = true;
            boardFreeSpace--;
            
            if (checkWin(board) != 'f') {
                printWin(board, checkWin(board));
                break;
            }

            if (boardFreeSpace == 0) {
                printBoard(board);
                System.out.println("It is a draw!\n");
                break;
            }

            int aiPosition = aiSelection(aiSelection, board, boardState, boardFreeSpace);
            board = addSymbolToBoard(board, 'O', aiPosition);
            boardState[aiPosition - 1] = true;
            boardFreeSpace--;

            System.out.println(Integer.toString(numberOfEndStatesSearched) + " end states were searched.");
            numberOfEndStatesSearched = 0;

            printBoard(board);

            if (checkWin(board) != 'f') {
                printWin(board, checkWin(board));
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
                cellCode++;
            }
        }
        return board;
    }

    public static void printWin(char[][] board, char winner) {
        printBoard(board);
        System.out.println(winner + " Wins!\n");
    }

    public static int minimax(char[][] board, boolean[] boardState, int boardFreeSpace, int depth, boolean isMaximizer, boolean pruning, int alpha, int beta) {
        char winner = checkWin(board);
        if (winner != 'f' || boardFreeSpace == 0) {
            numberOfEndStatesSearched++;
            if (winner == 'O') { // AI
                return 10 - depth;
            }
            else if (winner == 'X') { // Human
                return -10 + depth;
            }
            else {
                return 0;
            }
        }
        int[] freeSpaceIndexes = getFreeSpaceIndexes(boardState, boardFreeSpace);
        int bestPosition = freeSpaceIndexes[0] + 1;
        if (isMaximizer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < freeSpaceIndexes.length; i++) {
                board = addSymbolToBoard(board, 'O', freeSpaceIndexes[i] + 1);
                boardState[freeSpaceIndexes[i]] = true;
                int eval = minimax(board, boardState, boardFreeSpace - 1, depth + 1, false, pruning, alpha, beta);
                board = removeSymbolFromBoard(board, freeSpaceIndexes[i] + 1);
                boardState[freeSpaceIndexes[i]] = false;
                if (eval > maxEval) {
                    maxEval = eval;
                    bestPosition = freeSpaceIndexes[i] + 1;
                }
                alpha = Math.max(alpha, eval);
                if (pruning && beta <= alpha) {
                    break;
                }
            }
            if (depth == 0) {
                return bestPosition;
            }
            return (int) maxEval;
        }
        else {
            double minEval = Double.POSITIVE_INFINITY;
            for (int i = 0; i < freeSpaceIndexes.length; i++) {
                board = addSymbolToBoard(board, 'X', freeSpaceIndexes[i] + 1);
                boardState[freeSpaceIndexes[i]] = true;
                int eval = minimax(board, boardState, boardFreeSpace - 1, depth + 1, true, pruning, alpha, beta);
                board = removeSymbolFromBoard(board, freeSpaceIndexes[i] + 1);
                boardState[freeSpaceIndexes[i]] = false;
                minEval = Math.min((double) eval, minEval);
                beta = Math.min(eval, beta);
                if (pruning && beta <= alpha) {
                    break;
                }
            }
            return (int) minEval;
        }
    }

    public static int randomAI(char[][] board, boolean[] boardState, int boardFreeSpace) {
        int[] freeSpaceCells = getFreeSpaceIndexes(boardState, boardFreeSpace); 
        int randomCellIndex = new Random().nextInt(freeSpaceCells.length);
        return freeSpaceCells[randomCellIndex] + 1;

    }

    public static int[] getFreeSpaceIndexes(boolean[] boardState, int boardFreeSpace) {
        int[] freeSpaceIndexes = new int[boardFreeSpace];
        for (int i = 0; i < boardState.length; i++) {
            if (boardState[i] == false) {
                freeSpaceIndexes = arrayAppend(freeSpaceIndexes, i);
            }
        }
        return freeSpaceIndexes;
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

    // To find board cell from user input 1 - 9.
    // First subtract 1 from input.
    // For i divide by three and floor the result.
    // For j mod the input. 
    public static char[][] addSymbolToBoard(char[][] board, char playerSymbol, int position) {
        int iBoardPosition = (int) Math.floor((position - 1) / 3);
        int jBoardPosition = (position - 1) % 3;
        board[iBoardPosition][jBoardPosition] = playerSymbol;

        return board;
    }

    public static char[][] removeSymbolFromBoard(char[][] board, int position) {
        int iBoardPosition = (int) Math.floor((position - 1) / 3);
        int jBoardPosition = (position - 1) % 3;
        board[iBoardPosition][jBoardPosition] = (char) (49 + position - 1);
        
        return board;
    }

    public static char checkWin(char[][] board) {
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
                return startingSymbol;
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
                return startingSymbol;
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
                return startingSymbol;
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
                return startingSymbol;
            }
        }

        return 'f';
    }

    public static int aiSelection(int aiSelection, char[][] board, boolean[] boardState, int boardFreeSpace) {
        if (aiSelection == 1) {
            return randomAI(board, boardState, boardFreeSpace);
        } 
        else if (aiSelection == 2) {
            return minimax(board, boardState, boardFreeSpace, 0, true, false, 0, 0);
        }
        else {
            return minimax(board, boardState, boardFreeSpace, 0, true, true, (int) Double.NEGATIVE_INFINITY, (int) Double.POSITIVE_INFINITY);
        }
    }

    public static void printBoard(char[][] board) {
        // Generate formatted board.
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
        // To find board cell from user input 1 - 9.
        // For i the first row is 1 and the rest are determined by modding the result of i - 1 by 4.
        // For j the first column is 3 and the rest are determined by modding the result of j - 3 by 8.
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
