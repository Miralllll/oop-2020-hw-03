import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * It is the table model, which contains data for the view (MetropolisesFrame)
 */

public class MetropolisesModel extends AbstractTableModel {

    private List<String> columnNames;
    private List<List<String>> tableDB;

    /**
     * Constructor initializes columnNames and tableDB
     *
     * @param ---
     */
    public MetropolisesModel() {
        columnNames = new ArrayList<>();
        tableDB = new ArrayList<List<String>>();
        columnNames.add(MetropolisesFrame.METROPOLIS);
        columnNames.add(MetropolisesFrame.CONTINENT);
        columnNames.add(MetropolisesFrame.POPULATION);
    }

    /**
     * Returns number of rows from tableDB
     *
     * @return number of rows
     */
    @Override
    public int getRowCount() {
        return tableDB.size();
    }

    /**
     * Returns number of columns
     *
     * @return number of columns ddddddddd
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Returns our model-table type
     *
     * @param i - column index
     * @return which class objects are in the column
     */
    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    /**
     * Returns if the cells are editable
     *
     * @param row - row index
     * @param col - column index
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * Returns value from the tableDB
     *
     * @param rowIndex    - row index
     * @param columnIndex - column index
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<String> row = tableDB.get(rowIndex);
        return row.get(columnIndex);
    }

    /**
     * Returns all column names
     *
     * @param index - column index
     * @return - returns column names as a list
     */
    @Override
    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    /**
     * If a client clicked on the add button, only new row should be displayed
     *
     * @param con - container(information) from view
     */
    public void addRow(MetropolisContainer con) {
        tableDB.clear();
        tableDB.add(con.getAsListRow());
    }

    /**
     * If a client clicked on the search button, all founded information should be displayed
     *
     * @param res - container(information) from view
     */
    public void setTable(List<MetropolisContainer> res) {
        tableDB.clear();
        for (int i = 0; i < res.size(); i++)
            tableDB.add(res.get(i).getAsListRow());
    }

    /**
     * Model's tableDB into the list od containers
     *
     * @result res - container(information) from here
     */
    public List<MetropolisContainer> getTable() {
        List<MetropolisContainer> ls = new ArrayList<>();
        for (int i = 0; i < tableDB.size(); i++) {
            ls.add(new MetropolisContainer(tableDB.get(i)));
        }
        return ls;
    }
}
