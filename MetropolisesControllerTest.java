import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MetropolisesControllerTest {

    private MetropolisesFrame view;
    private MetropolisesBrain brain;
    private MetropolisesController cont;
    private String saveOldDBName;

    @BeforeAll
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        view = new MetropolisesFrame();
        view.setVisible(false);
        saveOldDBName = MyDataBaseINFO.MY_DB_NAME;
        // creating new database and metropolises table
        brain = new MetropolisesBrain();
        brain.removeDataBase();
        MyDataBaseINFO.MY_DB_NAME = "NewDataBase";
        brain.recreateDataBase();
        cont = new MetropolisesController(view, brain);
    }

    @AfterAll
    void tearDown() throws SQLException, ClassNotFoundException {
        brain.removeDataBase();
        brain.isConnection(false);
    }

    @Test
    public void testControllerHard0() throws SQLException {
        MetropolisContainer met = new MetropolisContainer("Mumbai", "Asia", "20400000");
        assertEquals(0, brain.getAllRows().size());
        view.setContainer(met);
        view.clickAdd();
        view.setContainer(met);
        view.clickAdd();
        view.setContainer(met);
        view.clickAdd();
        List<MetropolisContainer> rows = brain.getAllRows();
        assertEquals(3, rows.size());
        assertEquals(3, MetropolisesBrainTest.findRowInRows(rows, met));
    }

    @Test
    public void testControllerHard1() throws SQLException {
        MetropolisContainer met = new MetropolisContainer("", "", "");
        assertEquals(3, brain.getAllRows().size());
        view.setContainer(met);
        view.clickAdd();
        assertEquals(3, brain.getAllRows().size()); // nothing happened could be better to print
    }

    @Test
    public void testControllerHard2() throws SQLException {
        addForView("San Francisco", "North America", "5780000");
        addForView("San Jose", "North America", "7354555");
        addForView("London", "Europe", "8580000");
        addForView("Rome", "Europe", "2715000");
        searchForView("San", "", "", MetropolisesFrame.QUANTITY_COMBO[0],
                MetropolisesFrame.MATCH_COMBO[1]);
        List<MetropolisContainer> ls = view.getAllRows();
        assertEquals(2, ls.size());
        assertTrue(ls.contains(new MetropolisContainer("San Jose", "North America", "7354555")));
        assertTrue(ls.contains(new MetropolisContainer("San Francisco", "North America", "5780000")));
    }

    @Test
    public void testControllerHard3() throws SQLException {
        addForView("London", "Europe", "8580000");
        addForView("Rome", "Europe", "2715000");
        searchForView("Rome", "Europe", "", MetropolisesFrame.QUANTITY_COMBO[0],
                MetropolisesFrame.MATCH_COMBO[0]);
        List<MetropolisContainer> ls1 = view.getAllRows();
        assertEquals(2, ls1.size());
        searchForView("London", "Europe", "", MetropolisesFrame.QUANTITY_COMBO[0],
                MetropolisesFrame.MATCH_COMBO[0]);
        List<MetropolisContainer> ls2 = view.getAllRows();
        assertEquals(2, ls2.size());
    }

    @Test
    public void testControllerHard4() throws SQLException {
        searchForView("", "Europe", "0", MetropolisesFrame.QUANTITY_COMBO[0],
                MetropolisesFrame.MATCH_COMBO[1]);
        List<MetropolisContainer> ls1 = view.getAllRows();
        assertEquals(4, ls1.size());
        searchForView("", "Europe", "2715000", MetropolisesFrame.QUANTITY_COMBO[0],
                MetropolisesFrame.MATCH_COMBO[0]);
        List<MetropolisContainer> ls2 = view.getAllRows();
        assertEquals(2, ls2.size());
    }

    @Test
    public void testControllerHard5() throws SQLException {
        List<MetropolisContainer> old = view.getAllRows();
        searchForView("", "", "-4", MetropolisesFrame.QUANTITY_COMBO[0],
                MetropolisesFrame.MATCH_COMBO[1]);
        assertEquals(old.size(), view.getAllRows().size());
    }

    private void addForView(String m, String c, String p) {
        MetropolisContainer met = new MetropolisContainer(m, c, p);
        view.setContainer(met);
        view.clickAdd();
    }

    private void searchForView(String m, String c, String p, String q, String mt) {
        MetropolisContainer met = new MetropolisContainer(m, c, p, q, mt);
        view.setContainer(met);
        view.clickSearch();
    }
}