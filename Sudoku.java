import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
    // Provided grid data for main/testing
    // The instance variable strategy is up to you.

    // Provided easy 1 6 grid
    // (can paste this text into the GUI too)
    public static final int[][] easyGrid = Sudoku.stringsToGrid(
            "1 6 4 0 0 0 0 0 2",
            "2 0 0 4 0 3 9 1 0",
            "0 0 5 0 8 0 4 0 7",
            "0 9 0 0 0 6 5 0 0",
            "5 0 0 1 0 2 0 0 8",
            "0 0 8 9 0 0 0 3 0",
            "8 0 9 0 4 0 2 0 0",
            "0 7 3 5 0 9 0 0 1",
            "4 0 0 0 0 0 6 7 9");


    // Provided medium 5 3 grid
    public static final int[][] mediumGrid = Sudoku.stringsToGrid(
            "530070000",
            "600195000",
            "098000060",
            "800060003",
            "400803001",
            "700020006",
            "060000280",
            "000419005",
            "000080079");

    // Provided hard 3 7 grid
    // 1 solution this way, 6 solutions if the 7 is changed to 0
    public static final int[][] hardGrid = Sudoku.stringsToGrid(
            "3 7 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 7 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 7 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1");


    public static final int SIZE = 9;  // size of the whole 9x9 puzzle
    public static final int PART = 3;  // size of each 3x3 part
    public static final int MAX_SOLUTIONS = 100;
    public static final HashSet<Integer> SUDOKU_NUMBERS
            = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    public static final String VALUES = "1 2 3 4 5 6 7 8 9";
    private int[][] grid;
    private int[][] firstSolution;
    private ArrayList<Spot> spots;
    private int solutionsNum;
    private long solutionTime;
    private boolean solved;

    // Provided various static utility methods to
    // convert data formats to int[][] grid.

    /**
     * Returns a 2-d grid parsed from strings, one string per row.
     * The "..." is a Java 5 feature that essentially
     * makes "rows" a String[] array.
     * (provided utility)
     *
     * @param rows array of row strings
     * @return grid
     */
    public static int[][] stringsToGrid(String... rows) {
        int[][] result = new int[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            result[row] = stringToInts(rows[row]);
        }
        return result;
    }

    /**
     * Given a single string containing 81 numbers, returns a 9x9 grid.
     * Skips all the non-numbers in the text.
     * (provided utility)
     *
     * @param text string of 81 numbers
     * @return grid
     */
    public static int[][] textToGrid(String text) {
        int[] nums = stringToInts(text);
        if (nums.length != SIZE * SIZE)
            throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
        int[][] result = new int[SIZE][SIZE];
        int count = 0;
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++) {
                result[row][col] = nums[count];
                count++;
            }
        return result;
    }

    /**
     * Given 9x9 grid, returns a single string containing 81 numbers.
     *
     * @param grid of integers
     * @return string containing 81 numbers
     */
    public static String gridToText(int[][] grid) {
        StringBuilder text = new StringBuilder("");
        // if grid is null or if some spot is not valid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++)
                text.append(grid[i][j] + " ");
            text.append("\n");
        }
        return text.toString();
    }

    /**
     * Given a string containing digits, like "1 23 4",
     * returns an int[] of those digits {1 2 3 4}.
     * (provided utility)
     *
     * @param string string containing ints
     * @return array of ints
     */
    public static int[] stringToInts(String string) {
        int[] a = new int[string.length()];
        int found = 0;
        for (int i = 0; i < string.length(); i++)
            if (Character.isDigit(string.charAt(i))) {
                a[found] = Integer.parseInt(string.substring(i, i + 1));
                found++;
            }
        int[] result = new int[found];
        System.arraycopy(a, 0, result, 0, found);
        return result;
    }

    /**
     * Class: Spot
     * Saves information of the spare point: rowIndex - r, ColumnIndex - c
     * And validCounter which describes the amount of integers that could be written
     * in this spot now.
     */
    private class Spot implements Comparable {

        private int r, c;
        private int validCounter;

        public Spot(int row, int column) {
            r = row;
            c = column;
            updatePossibleValuesNum();
        }

        /**
         * @return - the amount of integers that could be written in this spot
         */
        public int getPossibleValuesNum() {
            return validCounter;
        }

        /**
         * @param value - sets this value in this spare spot
         */
        public void set(int value) {
            grid[r][c] = value;
        }

        /**
         * Gets numbers from 1...9 and removes number which can be already
         * found in the same row, column or square. Numbers which was not removed
         * are possible values for this spot.
         *
         * @return possible Values (hashset)
         */
        private HashSet<Integer> getPossibleValues() {
            HashSet<Integer> possibleValues = new HashSet<Integer>(SUDOKU_NUMBERS);
            int currPartFirstX = r / PART * PART, currPartFirstY = c / PART * PART;
            for (int i = 0; i < SIZE; i++) {
                possibleValues.remove(grid[r][i]);
                possibleValues.remove(grid[i][c]);
                possibleValues.remove(grid[currPartFirstX + i / PART][currPartFirstY + i % PART]);
            }
            return possibleValues;
        }

        /**
         * If grid has been changed after creating this spot, this method should be called.
         * It updates an amount of PossibleValues - validCounter (refresh)
         */
        public void updatePossibleValuesNum() {
            validCounter = getPossibleValuesNum();
        }

        /**
         * Compares two Spot according to the validCounter (an amount of PossibleValues)
         *
         * @param o - other Spot object
         * @return integer - positive if first spot is more and
         * negative if second is more, 0 ==
         */
        @Override
        public int compareTo(Object o) {
            Spot otherSpot = (Spot) o;
            return validCounter - otherSpot.getPossibleValuesNum();
        }
    }


    // Provided -- the deliverable main().
    // You can edit to do easier cases, but turn in
    // solving hardGrid.
    public static void main(String[] args) {
        Sudoku sudoku;
        sudoku = new Sudoku(hardGrid);

        System.out.println(sudoku); // print the raw problem
        int count = sudoku.solve();
        System.out.println("solutions:" + count);
        System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
        System.out.println(sudoku.getSolutionText());
    }


    /**
     * Sets up based on the given ints.
     */
    public Sudoku(int[][] ints) {
        validSudoku(ints);
        grid = cloneArray(ints);
        solutionsNum = 0;
        solutionTime = 0;
        solved = false;
    }

    /**
     * clones the matrix (ints)
     *
     * @param ints - some matrix type array
     * @return - clone version of this matrix
     */
    private int[][] cloneArray(int[][] ints) {
        int[][] grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(ints[i], 0, grid[i], 0, SIZE);
        return grid;
    }

    /**
     * It is not important method, but ... it is my personal addition.
     * In N^2 iterates every row, column, square and checks if there are all
     * integers different (0 could be).
     *
     * @param ints - input from user
     */
    private void validSudoku(int[][] ints) {
        if (ints.length != SIZE || ints[0].length != SIZE)
            throw new IllegalArgumentException("Invalid array length!");
        HashSet<Integer> currRow = new HashSet<>(), currCol = new HashSet<>(),
                currPart = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            int currFirstValR = i / PART * 3, currFirstValC = i % PART * 3;
            currRow.clear();
            currCol.clear();
            currPart.clear();
            for (int j = 0; j < SIZE; j++) {
                tryToAdd(ints, currFirstValC + j / 3, currFirstValR + j % 3, currPart);
                tryToAdd(ints, i, j, currRow);
                tryToAdd(ints, j, i, currCol);
            }
        }
    }

    /**
     * If current integer is in the list already throws exception
     *
     * @param ints @param rIndex  @param cIndex  @param currSet
     */
    private void tryToAdd(int[][] ints, int rIndex, int cIndex, HashSet<Integer> currSet) {
        if (ints[rIndex][cIndex] != 0 && !currSet.add(ints[rIndex][cIndex]))
            throw new IllegalArgumentException("Invalid numbers in sudoku table!");
    }

    /**
     * Additional constructor which calls the main contractor.
     *
     * @param text
     */
    public Sudoku(String text) {
        this(textToGrid(text));
    }

    /**
     * Prints board here
     *
     * @return - string of this board.
     */
    @Override
    public String toString() {
        return gridToText(grid);
    }

    /**
     * Solves the puzzle, invoking the underlying recursive search.
     */
    public int solve() {
        if (solved) return solutionsNum;
        initSpotList();
        long startTime = System.currentTimeMillis();
        actualSolution(0);
        solutionTime = System.currentTimeMillis() - startTime;
        solved = true;
        return solutionsNum;
    }

    /**
     * Method is recursive.
     * If the number of solutions is more than 100, recursion stops.
     * If our current index (spotIndex) == spots.size(), we solved sudoku,
     * so clone our current grid, if it is the first (solution),
     * increase solutionNum ++ and stop this path of recursion.
     * else----
     * Get possible values for current spot. Iterate them, try all of them with
     * set this value in the grid call recursion for new grid and
     * set 0 again for back-tracking.
     *
     * @param spotIndex - index in the spotList (spare spots)
     */
    private void actualSolution(int spotIndex) {
        if (solutionsNum >= MAX_SOLUTIONS) return;
        if (spotIndex == spots.size()) {
            if (solutionsNum == 0) firstSolution = cloneArray(grid);
            solutionsNum++;
            return;
        }
        Spot curr = spots.get(spotIndex);
        HashSet<Integer> possibleValues = curr.getPossibleValues();
        for (Integer i : possibleValues) {
            curr.set(i);
            actualSolution(spotIndex + 1);
        }
        curr.set(0); // backtracking
    }

    /**
     * Initializes a list of the spots (spare grid points)
     */
    private void initSpotList() {
        spots = new ArrayList<>();
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (grid[i][j] == 0) spots.add(new Spot(i, j));
        Collections.sort(spots);
    }

    /**
     * @return string - if sudoku is solved, string is solution,
     * else some message
     */
    public String getSolutionText() {
        if (solved) return gridToText(firstSolution);
        return "Solution not found!";
    }

    /**
     * @return time (integer) - solution time
     */
    public long getElapsed() {
        return solutionTime;
    }
}
