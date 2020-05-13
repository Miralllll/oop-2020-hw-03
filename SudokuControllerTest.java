import org.junit.jupiter.api.Test;

import javax.swing.text.BadLocationException;

import static org.junit.jupiter.api.Assertions.*;

class SudokuControllerTest {
    @Test
    public void testSudokuController0() {
        checkProcessesOn(Sudoku.easyGrid);
    }

    @Test
    public void testSudokuController1() {
        checkProcessesOn(Sudoku.mediumGrid);
    }

    @Test
    public void testSudokuController2() {
        checkProcessesOn(Sudoku.hardGrid);
    }

    @Test
    public void testSudokuController3() {
        checkProcessesOn(Sudoku.textToGrid(SudokuTest.sudokuTx1));
    }

    @Test
    public void testSudokuController4() {
        checkProcessesOn(Sudoku.textToGrid(SudokuTest.sudokuTx2));
    }

    @Test
    public void testSudokuController5() {
        checkProcessesOn(Sudoku.textToGrid(SudokuTest.generateAllZero()));
    }


    private void checkProcessesOn(int[][] grid) {
        SudokuFrame frame = new SudokuFrame();
        frame.setVisible(false);
        SudokuController controller = new SudokuController(frame);
        Sudoku sudoku = new Sudoku(grid);
        frame.setPuzzleText(sudoku.toString());
        frame.clickCheckButton();
        int solutionNum = sudoku.solve();
        String result = sudoku.getSolutionText() + SudokuController.SOLUTIONS_TG +
                +solutionNum;
        assertTrue(frame.getSolutionText().substring(0, result.length()).equals(result));
    }

    @Test
    public void testSudokuController6() {
        SudokuFrame frame = new SudokuFrame();
        frame.setVisible(false);
        SudokuController controller = new SudokuController(frame);
        Sudoku sudoku = new Sudoku(Sudoku.easyGrid);
        frame.setPuzzleText("");
        frame.setPuzzleText(sudoku.toString());
    }

    @Test
    public void testSudokuController7() throws BadLocationException {
        SudokuFrame frame = new SudokuFrame();
        frame.setVisible(false);
        frame.turnOffAuto();
        SudokuController controller = new SudokuController(frame);
        Sudoku sudoku = new Sudoku(Sudoku.easyGrid);
        frame.setPuzzleText(sudoku.toString());
        frame.removeText(4, 4);
        frame.clickCheckButton();
        assertTrue(frame.getSolutionText().equals(SudokuController.SOME_PROBLEM));
    }

    @Test
    public void testSudokuController8() {
        SudokuFrame frame = new SudokuFrame();
        frame.setVisible(false);
        frame.turnOffAuto();
        SudokuController controller = new SudokuController(frame);
        frame.setPuzzleText("6" + SudokuTest.sudokuTx1.substring(1));
        frame.clickCheckButton();
        assertTrue(frame.getSolutionText().equals(SudokuController.INVALID_BOARD));
    }
}