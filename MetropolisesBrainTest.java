import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MetropolisesBrainTest {

    private Connection conn;
    MetropolisesBrain br;

    @BeforeAll
    void setUp() throws ClassNotFoundException, SQLException, IOException {

        br = new MetropolisesBrain();
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + MyDataBaseINFO.DB_SERVER
                        + "/?useUnicode=true&useJDBC" +
                        "CompliantTimezoneShift=true" +
                        "&useLegacyDatetimeCode=false&serverTimezone=UTC",
                MyDataBaseINFO.USERNAME,
                MyDataBaseINFO.PASSWORD);
        Statement stm = conn.createStatement();
        stm.execute("USE " + MyDataBaseINFO.MY_DB_NAME);
    }

    @AfterAll
    void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    public void testBrainEasy0() throws SQLException {
        int oldRowCount = br.getAllRows().size();
        MetropolisContainer met = new MetropolisContainer(MetropolisContainerTest.METROPOLIS,
                MetropolisContainerTest.CONTINENT, MetropolisContainerTest.POPULATION);
        br.addRow(met);
        assertEquals(oldRowCount + 1, oldRowCount = br.getAllRows().size());
        br.addRow(met);
        assertEquals(oldRowCount + 1, br.getAllRows().size());
        List<MetropolisContainer> rows = br.getAllRows();
        assertTrue(rows.contains(met));
    }

    @Test
    public void testBrainMedium0() throws SQLException {
        int oldRowCount = br.getAllRows().size();
        MetropolisContainer met = new MetropolisContainer("New York",
                "North America", "212950");
        int oldContainerCount = findRowInRows(br.getAllRows(), met);
        br.addRow(met);
        assertEquals(oldContainerCount + 1, findRowInRows(br.getAllRows(), met));
    }

    public static int findRowInRows(List<MetropolisContainer> rows, MetropolisContainer met) {
        int count = 0;
        for (int i = 0; i < rows.size(); i++)
            if (rows.get(i).equals(met)) count++;
        return count;
    }

    @Test
    public void testBrainMedium1() throws SQLException {
        Assertions.assertThrows(Exception.class, () -> {
            br.addRow(new MetropolisContainer("New York",
                    "North America", ""));
        });
        br.addRow(new MetropolisContainer("New York",
                "North America", "-5"));
    }

    @Test
    public void testBrainHard0() throws SQLException {
        List<MetropolisContainer> before = br.getRows(
                new MetropolisContainer("NewYork",
                        "", "",
                        MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0]));
        MetropolisContainer con = new MetropolisContainer("NewYork",
                "NorthAmerica", "212950");
        br.addRow(con);
        List<MetropolisContainer> after = br.getRows(
                new MetropolisContainer("NewYork",
                        "", "",
                        MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0]));
        assertEquals(before.size() + 1, after.size());
        minusSets(after, before);
        assertEquals(1, after.size());
        assertTrue(con.equals(after.get(0)));
        minusSets(before, after);
        List<MetropolisContainer> ls = new ArrayList<>();
        ls.add(new MetropolisContainer("NewYork",
                "", ""));
        minusSets(after, ls);
    }

    private void minusSets(List<MetropolisContainer> ls1, List<MetropolisContainer> ls2) {
        for (int i = 0; i < ls2.size(); i++)
            if (ls1.contains(ls2.get(i)))
                ls1.remove(ls1.indexOf(ls2.get(i)));
    }

    @Test
    public void testBrainHard1() throws SQLException {
        List<MetropolisContainer> searchRes = br.getRows(
                new MetropolisContainer("",
                        "", "", MetropolisesFrame.QUANTITY_COMBO[0],
                        MetropolisesFrame.MATCH_COMBO[0]));
        minusSets(searchRes, br.getAllRows());
        assertEquals(0, searchRes.size());
    }

    @Test
    public void testBrainHard3() throws SQLException {
        br.addRow(new MetropolisContainer("New York",
                "North America", "212950"));
        List<MetropolisContainer> searchRes1 = br.getRows(
                new MetropolisContainer("New York",
                        "", "",
                        MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0]));
        assertTrue(correctMetropolisExactMatch("New York", searchRes1));
        assertFalse(correctMetropolisExactMatch("NewYork", searchRes1));
        List<MetropolisContainer> searchRes2 = br.getRows(
                new MetropolisContainer("",
                        "North America", "",
                        MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0]));
        assertTrue(correctContinentExactMatch("North America", searchRes2));
        assertFalse(correctContinentExactMatch("NorthAmerica", searchRes2));
        List<MetropolisContainer> searchRes3 = br.getRows(
                new MetropolisContainer("",
                        "", "0",
                        MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[1]));
        assertTrue(correctMorepopulation("0", searchRes3));
        assertFalse(correctMorepopulation("212950", searchRes3));
    }

    private boolean correctMorepopulation(String p, List<MetropolisContainer> searched) {
        BigInteger pop = new BigInteger(p);
        for (int i = 0; i < searched.size(); i++)
            if (new BigInteger(searched.get(i).getPopulation()).compareTo(pop) <= 0)
                return false;
        return true;
    }

    private boolean correctMetropolisExactMatch(String m, List<MetropolisContainer> searched) {
        for (int i = 0; i < searched.size(); i++)
            if (!searched.get(i).getMetropolis().equals(m)) return false;
        return true;
    }

    private boolean correctContinentExactMatch(String c, List<MetropolisContainer> searched) {
        for (int i = 0; i < searched.size(); i++)
            if (!searched.get(i).getContinent().equals(c)) return false;
        return true;
    }

    @Test
    public void testBrainHard4() {
        Assertions.assertThrows(Exception.class, () -> {
            br.getRows(new MetropolisContainer("", "", "-fdfs"));
        });
    }

    @Test
    public void testBrainHard5() throws SQLException, ClassNotFoundException {
        br.isConnection(false);
        Assertions.assertThrows(Exception.class, () -> {
            br = new MetropolisesBrain() {
                @Override
                public void setConnection() throws ClassNotFoundException {
                    Class.forName("Jgarkalaooo");
                }
            };
        });
    }

    @Test
    public void testBrainHard6() throws SQLException, ClassNotFoundException, IOException {
        String saveOldName = MyDataBaseINFO.MY_DB_NAME;
        MyDataBaseINFO.MY_DB_NAME = "IJIJIJ";
        br.isConnection(false);
        br = new MetropolisesBrain();
        //br.removeDataBase(); // it need more time to test this parts
        //br.recreateDataBase(); // you can uncomment them
        //br.recreateDataBase();
        br.removeDataBase();
        MyDataBaseINFO.MY_DB_NAME = saveOldName;
        br.isConnection(false);
        br = new MetropolisesBrain();
    }

    @Test
    public void testBrainHard7() throws SQLException, ClassNotFoundException, IOException {
        String saveOldName = MyDataBaseINFO.MY_DB_NAME;
        MyDataBaseINFO.MY_DB_NAME = "PIJIIIJIII";
        br.isConnection(false);
        br = new MetropolisesBrain();
        //br.removeDataBase();
        //br.recreateDataBase();
        //br.removeDataBase();
        MyDataBaseINFO.MY_DB_NAME = saveOldName;
        br.isConnection(false);
        br = new MetropolisesBrain();
    }
}