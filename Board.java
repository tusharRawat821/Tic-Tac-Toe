/** */
public class Board {
    private int occupied;
    private final int[][] loc;
    private final int size;


    public Board(int n) {
        this.occupied = 0;          // board is empty initially
        this.size = n;
        this.loc = new int[n][n];
    }

    public int getSize() {
        return size;
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

        System.out.print("\n\n--------- Game Board ---------\n\n");

        for(int i = 0; i < n; i++) {
            System.out.print("      |");
            for(int j = 0; j < n; j++) {
                String val = (loc[i][j] == 0 ? "_" : (loc[i][j] == 1 ? "X" : "O")); 
                System.out.print(" " + val + " |");
            }
            System.out.print("\n\n");
        }

        System.out.print("\n--------- Game Board ---------\n\n");

    }

}
