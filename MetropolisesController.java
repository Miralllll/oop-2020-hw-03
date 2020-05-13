import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * It is Controller part of the MVC pattern. It has both view and brain and exchanges information
 * between them when it is needed (button is clicked ...).
 */
public class MetropolisesController {

    private MetropolisesFrame view;
    private MetropolisesBrain brain;

    /**
     * Gets two important part of the program and connects them with some logic.
     * Creates and adds add-search listeners on the view.
     *
     * @param view  - visual side
     * @param brain - mind, database side
     */
    public MetropolisesController(MetropolisesFrame view, MetropolisesBrain brain) {
        this.view = view;
        this.brain = brain;
        view.addNewListeners(new addListener(), new searchListener());
    }

    public class addListener implements ActionListener {
        /**
         * After pressing add button, gets information from text-fields and saves them
         * in the container-con. Method gives the brain this information for adding it
         * in the sql-database and the view for adding it on the model-base and JFrame.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            MetropolisContainer con = view.getMetropolisInfo(MetropolisesFrame.GET_TYPE.FOR_ADD);
            try {
                brain.addRow(con);
                view.addModelRow(con);
                view.closeErrorMessage();
            } catch (Exception throwables) {
                // throwables.printStackTrace();
                view.showErrorMessage();
            }
        }
    }

    public class searchListener implements ActionListener {
        /**
         * After pressing search button, gets information from text-fields, combo-boxes and
         * saves them in the container-con. Method gives the brain this information for searching it
         * in the sql-database and then returning found-info. Method gives this info to the view.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                MetropolisContainer con = view.getMetropolisInfo(MetropolisesFrame.GET_TYPE.FOR_SEARCH);
                List<MetropolisContainer> res = brain.getRows(con);
                view.setTable(res);
                view.closeErrorMessage();
            } catch (Exception throwables) {
                // throwables.printStackTrace();
                view.showErrorMessage();
            }
        }
    }
}
