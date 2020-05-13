import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {
    // more examples
    public static final String sudokuTx1 = "0 6 0 0 0 0 0 0 4" +
            "1 0 0 0 0 0 8 0 0" +
            "0 0 0 0 0 7 0 0 0" +
            "0 0 2 0 0 0 0 0 0" +
            "7 0 0 0 0 0 2 0 0" +
            "5 0 8 0 0 1 0 0 0" +
            "0 0 0 0 0 8 0 9 0" +
            "0 2 7 0 0 0 0 8 0" +
            "0 0 0 0 0 0 0 0 0";

    // more examples
    public static final String sudokuTx2 = "2 5 1 7 6 3 9 8 4" +
            "6 9 4 2 8 1 5 3 7" +
            "8 3 7 5 9 4 2 1 6" +
            "1 2 3 4 5 9 6 7 8" +
            "5 4 6 3 7 8 1 2 9" +
            "7 8 9 1 2 6 3 4 5" +
            "3 6 5 8 1 7 4 9 2" +
            "4 7 2 9 3 5 8 6 1" +
            "9 1 8 6 4 2 7 5 3";

    @Test
    public void runMain() {
        Sudoku.main(new String[]{});
    }

    @Test
    public void testValidAndInvalid() {
        Sudoku sudoku = new Sudoku(sudokuTx1);
        Assertions.assertThrows(Exception.class, () -> {
            new Sudoku(sudokuTx1.substring(0, sudokuTx1.length() - 2) + "8");
        });
        Assertions.assertThrows(Exception.class, () -> {
            new Sudoku(sudokuTx1.substring(0, sudokuTx1.length() / 2));
        });
        Assertions.assertThrows(Exception.class, () -> {
            int[][] sudoArr = {{1, 3, 4, 5, 6, 7, 8, 9, 10}};
            new Sudoku(sudoArr);
        });
        Assertions.assertThrows(Exception.class, () -> {
            int[][] sudoArr = {{1}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}};
            new Sudoku(sudoArr);
        });
    }

    @Test
    public void testAlreadySolved() {
        Sudoku sudoku = new Sudoku(Sudoku.hardGrid);
        sudoku.solve();
        sudoku.solve(); // solve few times
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    @Test
    public void testNeverSolved() {
        Sudoku sudoku = new Sudoku(Sudoku.hardGrid);
        Assertions.assertThrows(Exception.class, () -> {
            new Sudoku(sudoku.getSolutionText());
        });
    }

    @Test
    public void testSudokuEasy() {
        Sudoku sudoku1 = new Sudoku(Sudoku.easyGrid);
        Sudoku sudoku2 = new Sudoku(Sudoku.easyGrid);
        assertEquals(1, sudoku1.solve());
        assertEquals(1, sudoku2.solve());
        assertTrue(sudoku1.getSolutionText().equals(sudoku2.getSolutionText()));
        assertTrue(sudoku1.toString().equals(sudoku2.toString()));
    }

    @Test
    public void testSudokuMedium() {
        Sudoku sudoku1 = new Sudoku(Sudoku.mediumGrid);
        Sudoku sudoku2 = new Sudoku(mixRows(Sudoku.mediumGrid, 0, 2));
        assertTrue(sudoku1.getSolutionText().equals(sudoku2.getSolutionText()));
        Sudoku sudoku3 = new Sudoku(mixRows(Sudoku.mediumGrid, 7, 8));
        assertTrue(sudoku1.getSolutionText().equals(sudoku3.getSolutionText()));
        Sudoku sudoku4 = new Sudoku(mixRows((Sudoku.textToGrid(sudoku2.toString())), 5, 4));
        assertTrue(sudoku1.getSolutionText().equals(sudoku4.getSolutionText()));
    }

    Random rand = new Random();

    private int[][] mixRows(int[][] grid, int a, int b) {
        int[][] result = new int[grid.length][grid[0].length];
        for (int j = 0; j < grid[0].length; j++) {
            result[a][j] = grid[b][j];
            result[b][j] = grid[a][j];
        }
        return result;
    }

    @Test
    public void testSudokuHard() {
        Sudoku sudoku1 = new Sudoku(Sudoku.hardGrid);
        Sudoku sudoku2 = new Sudoku(mixColumn(Sudoku.mediumGrid, 0, 1));
        assertTrue(sudoku1.getSolutionText().equals(sudoku2.getSolutionText()));
        Sudoku sudoku3 = new Sudoku(mixColumn(Sudoku.mediumGrid, 6, 8));
        assertTrue(sudoku1.getSolutionText().equals(sudoku3.getSolutionText()));
        Sudoku sudoku4 = new Sudoku(mixColumn((Sudoku.textToGrid(sudoku2.toString())), 5, 4));
        assertTrue(sudoku1.getSolutionText().equals(sudoku4.getSolutionText()));
    }

    private int[][] mixColumn(int[][] grid, int a, int b) {
        int[][] result = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            result[i][a] = grid[i][b];
            result[i][b] = grid[i][a];
        }
        return result;
    }

    @Test
    public void testSudoku0() {
        Sudoku sudoku = new Sudoku(sudokuTx1);
        assertEquals(Sudoku.MAX_SOLUTIONS, sudoku.solve());
        // Solution is valid
        assertFalse(sudoku.getSolutionText().contains("0"));
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    @Test
    public void testSudoku1() {
        // test one this sudoku solution - 1
        String sudokuWith1Sol = "0 0" + sudokuTx2.substring(4);
        Sudoku sudoku = new Sudoku(sudokuWith1Sol);
        assertEquals(1, sudoku.solve());
        // Solution is valid
        assertFalse(sudoku.getSolutionText().contains("0"));
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    @Test
    public void testSudoku2() {
        // test one this sudoku solution - 1
        String sudokuWith1Sol = sudokuTx2.substring(0, 146) + "0 0 " + sudokuTx2.substring(149);
        Sudoku sudoku = new Sudoku(sudokuWith1Sol);
        assertEquals(1, sudoku.solve());
        // Solution is valid
        assertFalse(sudoku.getSolutionText().contains("0"));
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    @Test
    public void testSudoku3() {
        // test one this sudoku solution - 1
        String sudokuWith1Sol = "0 0 0 0 0 0 0 0 0" +
                "0 0 0 0 0 0 0 0 0" + sudokuTx2.substring(34);
        Sudoku sudoku = new Sudoku(sudokuWith1Sol);
        assertEquals(4, sudoku.solve()); // easy to calculate
        // Solution is valid
        assertFalse(sudoku.getSolutionText().contains("0"));
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    @Test
    public void testSudoku4() {
        // test one this sudoku solution - 1
        String sudokuWith1Sol = sudokuTx2.substring(0, 119) + "0 0 0 0 0 0 0 0 0" +
                "0 0 0 0 0 0 0 0 0";
        Sudoku sudoku = new Sudoku(sudokuWith1Sol);
        assertEquals(2, sudoku.solve()); // easy to calculate
        // Solution is valid
        assertFalse(sudoku.getSolutionText().contains("0"));
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    @Test
    public void testSudoku5() {
        String sudokuTx = generateAllZero();
        Sudoku sudoku = new Sudoku(sudokuTx);
        assertEquals(Sudoku.MAX_SOLUTIONS, sudoku.solve());
        assertEquals(1, new Sudoku(sudoku.getSolutionText()).solve());
    }

    public static String generateAllZero() {
        String result = "";
        for (int i = 0; i < Sudoku.SIZE * Sudoku.SIZE; i++) result += "0";
        return result;
    }
}