import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 * Class: SudokuFrame
 * Extends JFrame and adds some textAreas, buttons... on it.
 * It builds the view of sudoku problem, where
 * a user can add new puzzle in the puzzle text_area
 * and then click a check button, which displays solution
 * in the solution text_area.
 */
public class SudokuFrame extends JFrame {

    private JTextArea puzzle, solution;
    private JButton check;
    private JCheckBox auto;

    public SudokuFrame() {
        super("Sudoku Solver");
        setLayout(new BorderLayout(4, 4));
        addPuzzleArea();
        addSolutionArea();
        addSouthPanel();
        // setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Adds new panel with check button and auto-checkbox
     */
    private void addSouthPanel() {
        JPanel lowerPanel = new JPanel();
        add(lowerPanel, BorderLayout.SOUTH);
        addCheckButton(lowerPanel);
        addAutoCheckBox(lowerPanel);
    }

    /**
     * Initializes auto-checkbox and adds it on the panel
     */
    private void addAutoCheckBox(JPanel panel) {
        auto = new JCheckBox(AUTO_CHECK);
        auto.setSelected(true);
        panel.add(auto);
    }

    /**
     * Initializes check-button and adds it on the panel
     */
    private void addCheckButton(JPanel panel) {
        check = new JButton(CHECK);
        panel.add(check);
    }

    /**
     * Initializes solution area
     */
    private void addSolutionArea() {
        solution = new JTextArea(15, 20);
        solution.setBorder(new TitledBorder(SOLUTION));
        solution.setEditable(false);
        add(solution, BorderLayout.EAST);
    }

    /**
     * Initializes puzzle area
     */
    private void addPuzzleArea() {
        puzzle = new JTextArea(15, 20);
        puzzle.setBorder(new TitledBorder(PUZZLE));
        add(puzzle, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        // GUI Look And Feel
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SudokuFrame frame = new SudokuFrame();
        SudokuController controller = new SudokuController(frame);
    }

    /**
     * Gets two listeners from controller and adds them on the button and textArea.
     *
     * @param lis1, lis2
     */
    public void addNewListeners(SudokuController.puzzleListener lis1,
                                SudokuController.checkListener lis2) {
        puzzle.getDocument().addDocumentListener(lis1);
        check.addActionListener(lis2);
    }

    /**
     * @return true - if auto is selected, else false
     */
    public boolean isAutoSelected() {
        return auto.isSelected();
    }

    /**
     * @return text - from puzzle area, this text is entered by user
     */
    public String getPuzzleText() {
        return puzzle.getText();
    }

    /**
     * Sets/Displays test in the puzzle area
     */
    public void setPuzzleText(String text) {
        puzzle.setText(text);
    }

    /**
     * Displays text in the solution area
     */
    public void setSolutionText(String text) {
        solution.setText(text);
    }

    /**
     * Displays text in the puzzle area
     */
    public void removeText(int offs, int len) throws BadLocationException {
        puzzle.getDocument().remove(offs, len);
    }

    /**
     * @return text - from solution area, this text solution of the puzzle
     */
    public String getSolutionText() {
        return solution.getText();
    }

    /**
     * It is same action as clicking button.
     */
    public void clickCheckButton() {
        check.doClick(1);
    }

    /**
     * Makes auto-comboBox as unselected.
     */
    public void turnOffAuto() {
        auto.setSelected(false);
    }

    /**
     * Makes this JFrame invisible, or visible
     */
    public void setVisible(boolean vis) {
        super.setVisible(vis);
    }


    private static final String PUZZLE = "Puzzle", SOLUTION = "Solution";
    private static final String CHECK = "Check", AUTO_CHECK = "Auto Check";
}
