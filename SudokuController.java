import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * It is Controller part of the MVC pattern.
 */
public class SudokuController {

    private SudokuFrame view;

    public SudokuController(SudokuFrame view) {
        this.view = view;
        view.addNewListeners(new puzzleListener(), new checkListener());
    }

    public class checkListener implements ActionListener {
        /**
         * After pressing check button, sudoku should call solve.
         */
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            solve();
        }
    }

    public class puzzleListener implements DocumentListener {

        /**
         * If puzzle is added on the FieldArea and auto is selected,
         * sudoku should call solve automatically
         */
        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
        }

        /**
         * Checks if auto is selected, if is - solves puzzle
         */
        public void update() {
            if (view.isAutoSelected()) {
                solve();
            }
        }
    }

    /**
     * Gets puzzle from the textArea, then creates sudoku objects and call solve,
     * which returns solution-string, prints this solution and solution-time too.
     */
    public void solve() {
        try {
            String puzzle = view.getPuzzleText();
            Sudoku sudo = new Sudoku(Sudoku.textToGrid(puzzle));
            int solutionNum = sudo.solve();
            String text = sudo.getSolutionText();
            text += SOLUTIONS_TG + solutionNum + "\n";
            text += ELAPSED_TG + sudo.getElapsed() + "\n";
            view.setSolutionText(text);
        } catch (IllegalArgumentException i) {
            view.setSolutionText(INVALID_BOARD);
        } catch (Exception e) {
            view.setSolutionText(SOME_PROBLEM);
        }
    }

    public static final String SOLUTIONS_TG = "solutions: ", ELAPSED_TG = "elapsed: ";
    public static final String INVALID_BOARD = "Invalid Sudoku Board!";
    public static final String SOME_PROBLEM = "Some problems started popping up!";
}
