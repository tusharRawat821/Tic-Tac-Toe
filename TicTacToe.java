import java.util.Scanner;

/**
 * This class contains the main logic for TicTacToe game.
 */
public class TicTacToe {
    private int turn;
    private Board board;
    private int winner;

    private static final int MAX;
    private static final int MIN;

    private static Scanner scan;

    // To make operation of finding the winner in O(1) time, these are added. 
    private final int[] rowSum, colSum;
    private int diagSum;
    private int revDiagSum;

    public TicTacToe(int n) {
        board = new Board(n);       // init an empty Board of size n X n
        turn = 0;                   // init turn to '0' (not decided)
        winner = 0;                 // 0 -> draw | 1 -> Player won | -1 -> PC won
        
        this.rowSum = new int[n];
        this.colSum = new int[n];
        this.diagSum = 0;
        this.revDiagSum = 0;
    }

    static {
        scan = new Scanner(System.in);
        MAX = +1;
        MIN = -1;
    }

    public static void play() {

        System.out.print("Enter the Tic-Tac-Toe board dimension(must be >= 3): ");
        int n = scan.nextInt();  scan.nextLine();
        validateDimension(n);
        System.out.print("\nWant to go first (press y/n): ");
        String choice = scan.nextLine();
        validateChoice(choice);
        
        // initialize the board.
        TicTacToe ticTacToe = new TicTacToe(n);
        // setting the turn
        if(choice.equals("y"))
            ticTacToe.turn = 1;
        else
            ticTacToe.turn = -1;

        // Game is not over untill there's no winner && board is not filled.
        while(ticTacToe.winner == 0 && !ticTacToe.board.filled()) {
            // showBoard
            ticTacToe.board.show();
            
            // showTurn
            ticTacToe.showTurn();

            int[] move = null;
            if(ticTacToe.turn == 1) {
                // get player's move as an input.
                move = ticTacToe.playersMove();
            }
            else {
                // decide optimal move for computer using min-max algo.
                move = ticTacToe.computersMove();
            }
            
            // play 'move' on board.
            ticTacToe.playMove(move);

            // switch turn
            ticTacToe.turn = (ticTacToe.turn == 1 ? -1 : 1);
        }

        // show last state of the board
        ticTacToe.board.show();

        // Game is Over, print winner.
        ticTacToe.showWinner();

        return;
    }

    private void showWinner() {
        if(winner == 0) 
            System.out.println("It's a Draw! ");
        else
            System.out.println((winner == 1 ? "Player" : "Computer") + " wins! ");
    }

    private int[] computersMove() {

        // TODO: alpha-beta optimization.
        // int alpha = Integer.MIN_VALUE;
        // int beta = Integer.MAX_VALUE;

        int n = board.getSize();

        // move = [x, y, score]
        int[] move = minMax(-1, n); // computer optimally plays 'O' at (move[0], move[1]) with score move[2], using min-max algo.
        System.out.println("Computer plays 'O' at (" + move[0] + ", " + move[1] + ")");
        return new int[] {move[0], move[1]};
    }
    
    /**
     * This is a min-max algorithm, which decides the optimal move taken by computer assuming that player take the optimal
     * move always.
     * @return score (-1 if computer can win, +1 if only player can win, else 0 if game can be a draw)
     */
    private int[] minMax(int player, final int n) {
        // At max level --> human's turn | At min level --> Computer's turn
        // System.out.println("------computer deciding ---- ");
        // board.show();
        // System.out.println("------computer deciding ---- ");

        int best[] = null;
        if(player == MIN) {
            best = new int[] {-1, -1, Integer.MAX_VALUE};
        }
        else if(player == MAX) {
            best = new int[] {-1, -1, Integer.MIN_VALUE};
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(board.get(i, j) == 0) {

                    int win = tryMove(i, j, player);

                    int []score = null;
                    if((win != 0) || board.filled()) {
                        // if this is the last move we can play in game, score is calculated.
                        score = new int[] {i, j, win};
                    }
                    else {
                        score = minMax(-1 * player, n);
                    }

                    undoMove(i, j, player);

                    if(player == MIN) {
                        if(score[2] < best[2]) {
                            best[0] = i;
                            best[1] = j;
                            best[2] = score[2];
                        }
                    }
                    else if(player == MAX) {
                        if(score[2] > best[2]) {
                            best[0] = i;
                            best[1] = j;
                            best[2] = score[2];
                        }
                    }
                }
            }
        }
        
        // return the optimal move(with score) player(=computer/human) can play by making a move.
        return best;
    }

    private int tryMove(int i, int j, int player) {
        int n = board.getSize();
        board.set(i, j, player);
        rowSum[i] += player;
        colSum[j] += player;
        if(i == j)
            diagSum += player;
        if(i + j == n - 1)
            revDiagSum += player;
        if(Math.abs(rowSum[i]) == n || Math.abs(colSum[j]) == n || Math.abs(diagSum) == n || Math.abs(revDiagSum) == n) {
            return player;
        }
        return 0;
    }

    private int undoMove(int i, int j, int player) {
        int n = board.getSize();
        board.set(i, j, 0);
        rowSum[i] -= player ;
        colSum[j] -= player;
        if(i == j)
            diagSum -= player;
        if(i + j == n - 1)
            revDiagSum -= player;
        return 0;
    }

    private int[] playersMove() {
        int[] move = new int[2];
        System.out.print("Enter place(x y) where you want to play 'X': ");
        int x = scan.nextInt();
        int y = scan.nextInt();
        int n = board.getSize();
        if(x < 0 || x >= n || y < 0 || y >= n) {
            throw new IllegalArgumentException("Moved out of Bound! ");
        }
        else if(this.board.get(x, y) != 0) {
            throw new IllegalArgumentException("Square is already occupied! ");
        }
        move[0] = x;
        move[1] = y;
        return move;
    }

    /**
     * Place 'X'/'O' on location (move[0], move[1]) 
     * @param move
     * @return winner after this move, 
     */
    private int playMove(int[] move) {
        int n = this.board.getSize();
        int x = move[0];
        int y = move[1];

        int play = turn;
        board.set(x, y, play);
        rowSum[x] += play;
        colSum[y] += play;
        if(x == y)
            diagSum += play;
        if(x + y == n - 1)
            revDiagSum += play;
        

        // if this is a win move. Decide winner and return.
        if(Math.abs(rowSum[x]) == n || Math.abs(colSum[y]) == n || Math.abs(diagSum) == n || Math.abs(revDiagSum) == n) {
            winner = play; 
        }
        return winner;
    }

    private void showTurn() {
        if(this.turn == 1) 
            System.out.print("Player's turn. ");
        else 
            System.out.print("Computer's turn. ");
    }

    private static boolean validateDimension(int n) {
        if(n < 3)
            throw new IllegalArgumentException("Invalid board size, must be >= 3");
        return true;
    }

    private static boolean validateChoice(String n) {
        if(!n.equals("y") && !n.equals("n"))
            throw new IllegalArgumentException("Invalid choice, must be 'y/n' ");
        return true;
    }
}
