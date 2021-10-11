public class Board {
    private int occupied;
    private final int[][] loc;
    // To make operation of finding the winner O(1) time, these are added. 
    private int[] rowSum;
    private int[] colSum;
    private int diagSum;
    private int revDiagSum;

    public Board(int n) {
        this.occupied = 0;          // board is empty initially
        this.loc = new int[n][n];
        this.rowSum = new int[n];
        this.colSum = new int[n];
        this.diagSum = 0;
        this.revDiagSum = 0;
    }

    public int getRowSum(int r) {
        return rowSum[r];
    }

    public int getColSum(int r) {
        return colSum[r];
    }

    public int getDiagSum(int r) {
        return diagSum;
    }
    
    public int getRevDiagSum(int r) {
        return revDiagSum;
    }

    public boolean filled() {
        return occupied == loc.length * loc.length;
    }

    public void set(int x, int y, int val) {
        if(val == 0)    
            occupied -= 1; // we clearing the loc[x][y]
        else
            occupied += 1;
        loc[x][y] = val;
    }

    public int get(int x, int y) {
        return loc[x][y];
    }

    public void show() {
        int n = loc.length;
        for(int i = 0; i < n; i++) {
            System.out.print("|");
            for(int j = 0; j < n; j++) {
                String val = (loc[i][j] == 0 ? "_" : (loc[i][j] == 1 ? "X" : "O")); 
                System.out.print(" " + val + " |");
            }
            System.out.println();
        }
    }

}
