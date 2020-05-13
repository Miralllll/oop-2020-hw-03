import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Class: MetropolisesFrame
 * Extends JFrame and adds some labels, buttons... on it.
 * Finally, It builds the view of metropolis problem, where
 * a client can add new metropolis (with continent and population info)
 * in the database and then search it according to some inforamtion.
 */
public class MetropolisesFrame extends JFrame {

    private JTable table;
    private MetropolisesModel model;
    private JLabel metropolosLb;
    private JTextField metropolis;
    private JLabel continentLb;
    private JTextField continent;
    private JLabel populationLb;
    private JTextField population;
    private JButton add;
    private JButton search;
    private JComboBox<String> quantity;
    private JComboBox<String> match;
    private JLabel lb;

    /**
     * Sets the name of the metropolis frame.
     * Creates and adds every JObject according to their location on the frame.
     * This process is separated in three part: building north, east, center panels.
     *
     * @param ---
     */
    public MetropolisesFrame() {
        super("Metropolis Viewer");
        setUpNorthPanel();
        setUpEastPanel();
        setUpCenter();
        setUpSouth();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void setUpSouth() {
        lb = new JLabel("Something went wrong! Try again!");
        lb.setVisible(false);
        JPanel south = new JPanel();
        add(south, BorderLayout.SOUTH);
        south.add(lb);
    }

    /**
     * Initializes model and table which is depend on this model,
     * gets information from it. Also, initializes and adds on the JFrame
     * JScrollPane which is connected to the table and is graphical face of it.
     *
     * @param ---
     */
    private void setUpCenter() {
        model = new MetropolisesModel();
        table = new JTable(model);
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setPreferredSize(new Dimension(EAST_HEIGHT, EAST_HEIGHT));
        add(scrollTable, BorderLayout.CENTER);
    }

    /**
     * Initializes and adds east-panel, which is divided into the two part.
     * Calls methods to build this parts.
     *
     * @param ---
     */
    private void setUpEastPanel() {
        JPanel eastPanel = new JPanel();
        add(eastPanel, BorderLayout.EAST);
        eastPanel.setPreferredSize(new Dimension(EAST_WIDTH, EAST_HEIGHT));
        setUpTopEastPanel(eastPanel);
        SetUpLowEastPanel(eastPanel);
    }

    /**
     * Builds a lower side of the east panel, Adds two combo-boxes and panel for them.
     * Also creates some TitleBorder for this panel and box-layout for combos.
     *
     * @param panel
     */
    private void SetUpLowEastPanel(JPanel panel) {
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setBorder(new TitledBorder("Search Options"));
        panel.add(comboBoxPanel);
        setUpCommandsBox(comboBoxPanel);
        setUpQuantityCombo(comboBoxPanel);
        setUpMatchCombo(comboBoxPanel);
    }

    /**
     * Creates combo-box with options: Exact Match, Partial Match.
     * Allows the user to determine if the Metropolis and
     * Continent text fields contain substrings we are searching for or exact matches.
     *
     * @param panel
     */
    private void setUpMatchCombo(JPanel panel) {
        match = new JComboBox<>(MATCH_COMBO);
        panel.add(match);
        match.setMaximumSize(new Dimension(EAST_WIDTH, EAST_PART));
    }

    /**
     * Creates combo-box which allows the user to determine if we are looking for
     * metropolitan areas with populations greater
     * or smaller than the number entered in the Population Text Field
     *
     * @param panel
     */
    private void setUpQuantityCombo(JPanel panel) {
        quantity = new JComboBox<>(QUANTITY_COMBO);
        panel.add(quantity);
        panel.add(Box.createVerticalStrut(BOX_AREA));
        quantity.setMaximumSize(new Dimension(EAST_WIDTH, EAST_PART));
    }

    /**
     * Builds an upper side of the east panel, Adds two buttons and panel for them.
     *
     * @param panel
     */
    private void setUpTopEastPanel(JPanel panel) {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        panel.add(buttonPanel);
        setUpAdd(buttonPanel);
        setUpSearch(buttonPanel);
    }

    /**
     * Initializes add search and some box_areas for creating extra space
     * button and north side of the JFrame.
     *
     * @param panel
     */
    private void setUpSearch(JPanel panel) {
        panel.add(Box.createVerticalStrut(BOX_AREA));
        add = new JButton(ADD);
        panel.add(add);
        panel.add(Box.createVerticalStrut(BOX_AREA));
    }

    /**
     * Initializes add button and a box_area for creating extra space between buttons.
     *
     * @param panel
     */
    private void setUpAdd(JPanel panel) {
        panel.add(Box.createVerticalStrut(BOX_AREA));
        search = new JButton(SEARCH);
        panel.add(search);
    }

    /**
     * Adds some boxLayout on any panel. This layout organizes that every added objects on
     * this panel are symmetric about the y-axis.
     *
     * @param panel
     */
    private void setUpCommandsBox(JPanel panel) {
        BoxLayout commandsBox = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(commandsBox);
    }

    /**
     * Builds a north side of the frame, adds north panel, some JTextFields and JLabels.
     */
    private void setUpNorthPanel() {
        JPanel northPanel = new JPanel();
        add(northPanel, BorderLayout.NORTH);
        setUpMetropolis(northPanel);
        setUpContinent(northPanel);
        setUpPopulation(northPanel);
    }

    /**
     * Adds population label and text-field on the panel, where a client can enter
     * some Integer for expressing an amount of population.
     *
     * @param panel
     */
    private void setUpPopulation(JPanel panel) {
        populationLb = new JLabel(POPULATION + EXTENSION_FOR_LABEL);
        population = new JTextField(FIELD_SIZE);
        panel.add(populationLb);
        panel.add(population);
    }

    /**
     * Adds continent label and text-field on the panel, where a client can enter
     * some String for expressing a name of continent or a prefix of it.
     *
     * @param panel
     */
    private void setUpContinent(JPanel panel) {
        continentLb = new JLabel(CONTINENT + EXTENSION_FOR_LABEL);
        continent = new JTextField(FIELD_SIZE);
        panel.add(continentLb);
        panel.add(continent);
    }

    /**
     * Adds metropolis label and text-field on the panel, where a client can enter
     * some String for expressing a name of metropolis or a prefix of it.
     *
     * @param panel
     */
    private void setUpMetropolis(JPanel panel) {
        metropolosLb = new JLabel(METROPOLIS + EXTENSION_FOR_LABEL);
        metropolis = new JTextField(FIELD_SIZE);
        panel.add(metropolosLb);
        panel.add(metropolis);
    }

    /**
     * Gets two listeners from controller and adds them on the buttons.
     *
     * @param lis1, lis2
     */
    public void addNewListeners(MetropolisesController.addListener lis1, MetropolisesController.searchListener lis2) {
        add.addActionListener(lis1);
        search.addActionListener(lis2);
    }

    /**
     * Creates a container according to the user-entered information.
     *
     * @param type - describes for what we need them to use (for add operation
     *             or for search operation).
     */
    public MetropolisContainer getMetropolisInfo(GET_TYPE type) throws NumberFormatException {
        String metropolisTx = metropolis.getText();
        String continentTx = continent.getText();
        String populationTx = population.getText();
        long pop = -1;
        if (!populationTx.equals("")) pop = Long.parseLong(populationTx);
        if (type == GET_TYPE.FOR_SEARCH)
            if (populationTx.equals("") || pop >= 0)
                return new MetropolisContainer(metropolisTx, continentTx, populationTx,
                        (String) quantity.getSelectedItem(), (String) match.getSelectedItem());
        if (type == GET_TYPE.FOR_ADD)
            if (pop >= 0) return new MetropolisContainer(metropolisTx, continentTx, populationTx);
        return null;
    }

    /**
     * Gives a model an information about adding new row.
     * And makes it update the JTable too.
     *
     * @param con - container
     */
    public void addModelRow(MetropolisContainer con) {
        model.addRow(con);
        model.fireTableDataChanged();
    }

    /**
     * Gives a model an information about searching operation.
     * And makes a model update the JTable too.
     *
     * @param res - list of containers
     */
    public void setTable(List<MetropolisContainer> res) {
        model.setTable(res);
        model.fireTableDataChanged();
    }

    public void setContainer(MetropolisContainer cont) {
        this.metropolis.setText(cont.getMetropolis());
        this.continent.setText(cont.getContinent());
        this.population.setText(cont.getPopulation());
        if (cont.getQuantity() == QUANTITY_COMBO[0]) quantity.setSelectedIndex(0);
        else if (cont.getQuantity() == QUANTITY_COMBO[1]) quantity.setSelectedIndex(1);
        System.out.println(quantity.getSelectedItem());
        if (cont.getMatch() == MATCH_COMBO[0]) match.setSelectedIndex(0);
        else if (cont.getMatch() == MATCH_COMBO[1]) match.setSelectedIndex(1);
        System.out.println(match.getSelectedItem());
    }

    /**
     * Same as click add button
     */
    public void clickAdd() {
        add.doClick(1);
    }

    /**
     * Same as click search button
     */
    public void clickSearch() {
        search.doClick(1);
    }

    /**
     * Shows message in the north side of the window
     */
    public void showErrorMessage() {
        lb.setVisible(true);
    }

    /**
     * Makes message invisible
     */
    public void closeErrorMessage() {
        lb.setVisible(false);
    }

    /**
     * Returns everything that is displayed on the frame
     * Really important for testing controller
     */
    public List<MetropolisContainer> getAllRows() {
        return model.getTable();
    }

    /**
     * Initializes frame and brain, then controller.
     *
     * @param args
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        MetropolisesFrame fr = new MetropolisesFrame();
        MetropolisesBrain brain = new MetropolisesBrain();
        MetropolisesController con = new MetropolisesController(fr, brain);
    }

    public static final String METROPOLIS = "Metropolis", CONTINENT = "Continent", POPULATION = "Population";
    public static final String ADD = " Add ", SEARCH = " Search ";
    private static final String EXTENSION_FOR_LABEL = ": ";
    private static final int FIELD_SIZE = 15;
    private static final int EAST_WIDTH = 200, EAST_HEIGHT = 300, EAST_PART = 30;
    private static final int BOX_AREA = 1;
    public static final String[] QUANTITY_COMBO = {"Population Larger Than", "Population Smaller Than"};
    public static final String[] MATCH_COMBO = {"Exact Match", "Partial Match"};

    public enum GET_TYPE {FOR_ADD, FOR_SEARCH}

}
