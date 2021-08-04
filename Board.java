import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    int[] board;
    int[][] boardInput;
    int n;

    // create a board from an n-by-n array of tiles
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        n = tiles[0].length;

        if (n < 2 || n > 128)
        {
            throw new IllegalArgumentException("Input board size must be in the range of 2 x 2 to 128 x 128");
        } 
        if (tiles[1].length != n) {
            throw new IllegalArgumentException("Input board dimensions must match (n * m, where n = m)");
        }

        boardInput = tiles;
        board = new int[n * n];
        int index = 0;
        
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                // store tiles board contents in board array and setup goalBoard array for later comparison
                board[index] = tiles[i][j];
                index++;
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder toString = new StringBuilder();

        toString.append("" + n + "\n");

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                toString.append(board[i * n + j]);

                if (j < n - 1)
                {
                    toString.append(" ");
                }
            }
            toString.append("\n");
        }

        return toString.toString();
    }

    // board dimension n
    public int dimension()
    {
        return n;
    }

    // number of tiles out of place
    public int hamming()
    {
        int count = 0;

        for (int i = 0; i < n * n; i++)
        {
            // does not include blank tile in the hamming count
            if (board[i] != i + 1)
            {
                if (board[i] == 0)
                {
                    continue;
                }
                count++;
            }
        }

        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int row, col, rowGoal, colGoal;
        int count = 0;

        for (int i = 0; i < n * n; i++)
        {
            // determine where each integer should be relative to placement in board array
            if (board[i] == 0)
            {
                rowGoal = n - 1;
                colGoal = n - 1;
            }
            else
            {
                rowGoal = (int) (board[i] - 0.5) / n;
                colGoal = (int) board[i] - 1 - rowGoal * n;
            }
            row = -2;
            col = -2;
            
            // if the value is already correctly positioned, skip to next iteration, do not increment count
            if (board[i] == i + 1 || board[i] == 0)
            {
                System.out.println("Skip: i = " + i + " board[i] = " + board[i]);
                continue;
            }
            else
            {
                // determine where each integer is actually found
                for (int j = 0; j < n * n; j++)
                {
                    if (board[j] == i + 1)
                    {
                        row = (int) (board[j] - 0.5) / n;
                        col = (int) board[j] - 1 - row * n;
                        break;
                    }
                }
            }

            System.out.print("i: " + i + " board[i]: " + board[i] + " - should be in row/col " + rowGoal + "/" + colGoal + ". It is in row/col " + row + "/" + col + "\t\t");

            // compute total difference
            int diff = Math.abs(rowGoal - row) + Math.abs(colGoal - col);
            count += diff;
            System.out.print(" -> increment Manhattan distance by " + diff + "\n");
        }

        return count;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == null)
        {
            return false;
        }
        
        if (this.getClass() != y.getClass())
        {
            return false;
        }

        Board b = (Board) y;

        if (this.dimension() != b.dimension())
        {
            return false;
        }

        for (int i = 0; i < n * n; i++)
        {
            if (this.board[i] != b.board[i])
            {
                return false;
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board>
    {
        private List<Board> neighbors = new ArrayList<>();

        public Iterator<Board> iterator()
        {          
            // find location of blank square
            int index;
            for (index = 0; index < n * n; index++)
            {
                if (board[index] == 0)
                {
                    break;
                }
            }

            int[] possibleMoves = new int[4];
            // top, right, bottom, left (NESW)
            for (int i = 0; i < 4; i++)
            {
                possibleMoves[i] = 1;
            }

            /**
             * Total possible moves
             * index - 1
             * index + 1
             * index - n
             * index + n
             * Accounts for top, bottom, left and right (irrespectively)
             */

            // top row
            if ((int) (index - 0.5) / n == 0)
            {
                // remove index - n from possible moves
                possibleMoves[0] = 0;
            }
            // bottom row
            if ((int) (index - 0.5) / n == n - 1)
            {
                // remove index + n from possible moves
                possibleMoves[2] = 0;
            }
            // left side
            if ((index % n) == 0)
            {
                // remove index - 1 from possible moves
                possibleMoves[3] = 0;
            }
            // right side
            if ((index % n) == n - 1)
            {
                // remove index + 1 from possible moves
                possibleMoves[1] = 0;
            }

            int m = 0;
            for (int i = 0; i < 4; i++)
            {
                if (possibleMoves[i] == 1)
                {
                    if (i == 0)
                    {
                        m = -n;
                    }
                    if (i == 1)
                    {
                        m = 1;
                    }
                    if (i == 2)
                    {
                        m = n;
                    }
                    if (i == 3)
                    {
                        m = -1;
                    }
                    neighbors.add(twin(index, m));
                }
            }
            
            return this.neighbors.iterator();
        }

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(int index, int dir)
    {
        if (dir == 0)
        {
            throw new IllegalArgumentException("Improperly initialized direction variable.");
        }

        Board b = new Board(boardInput);
        
        int swap = b.board[index + dir];
        b.board[index] = swap;
        b.board[index + dir] = 0;

        return b;
    }

    // unit testing
    public static void main(String[] args)
    {
        int[][] test1 = {   
                            { 8, 3, 1 },
                            { 5, 4, 7 },
                            { 6, 2, 0 }
                        };

        Board b = new Board(test1);

        System.out.println("Board size is " + b.n + " x " + b.n + ".");
        System.out.println("Hamming: \t\t" + b.hamming());
        System.out.println("Manhattan distance: \t" + b.manhattan());
        System.out.println(b.toString());

        int[][] test2 = {   
                            { 8, 3, 1 },
                            { 5, 4, 7 },
                            { 6, 2, 0 }
                        };

        Board b2 = new Board(test2);

        System.out.println(b.equals(b2));

        for (Board testBoard : b.neighbors())
        {
            System.out.println(testBoard.toString() + "\n");
        }

        int[][] test3 = {   
            { 8, 3, 1 },
            { 5, 4, 7 },
            { 6, 0, 2 }
        };

        Board b3 = new Board(test3);

        for (Board testBoard : b3.neighbors())
        {
            System.out.println("Neighbor : \n" + testBoard.toString() + "\n");
        }
    }
}
