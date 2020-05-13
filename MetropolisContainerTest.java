import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MetropolisContainerTest {

    public static final String METROPOLIS = "Mumbai", CONTINENT = "Asia", POPULATION = "20400000";

    @Test
    public void testContainerEasy0() {
        MetropolisContainer con = new MetropolisContainer(METROPOLIS,
                "", "");
        assertFalse(con.allFieldsEmpty());
        assertEquals(METROPOLIS, con.getMetropolis());
        assertEquals("", con.getContinent());
        assertEquals("", con.getPopulation());
        String[] arr = new String[]{METROPOLIS, "", ""};
        assertTrue(Arrays.deepEquals(arr, con.getAsListRow().toArray()));
    }

    @Test
    public void testContainerEasy1() {
        MetropolisContainer con1 = new MetropolisContainer(Arrays.asList("", "", ""));
        assertTrue(con1.allFieldsEmpty());
        MetropolisContainer con3 = new MetropolisContainer("", CONTINENT, "");
        assertFalse(con3.allFieldsEmpty());
        MetropolisContainer con4 = new MetropolisContainer("", "", POPULATION);
        assertFalse(con4.allFieldsEmpty());
    }

    @Test
    public void testContainerEasy2() {
        MetropolisContainer con1 = new MetropolisContainer(METROPOLIS, CONTINENT, "");
        assertFalse(con1.allFieldsEmpty());
        assertEquals(METROPOLIS, con1.getMetropolis());
        assertEquals(CONTINENT, con1.getContinent());
        assertEquals("", con1.getPopulation());
        String[] arr = new String[]{METROPOLIS, CONTINENT, ""};
        assertTrue(Arrays.deepEquals(arr, con1.getAsListRow().toArray()));
    }

    @Test
    public void testContainerMedium0() {
        MetropolisContainer con = new MetropolisContainer("", "", "",
                MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0]);
        assertEquals(MetropolisesFrame.QUANTITY_COMBO[0], con.getQuantity());
        assertEquals(MetropolisesFrame.MATCH_COMBO[0], con.getMatch());
        assertFalse(con.equals(new MetropolisContainer(METROPOLIS, CONTINENT, "")));
        assertFalse(con.equals(new MetropolisContainer("", CONTINENT, "")));
        assertFalse(con.equals(new MetropolisContainer("", "", POPULATION)));
        assertFalse(con.equals(new MetropolisContainer("", "", "",
                "", "")));
        assertFalse(con.equals(new MetropolisContainer("", "", "",
                MetropolisesFrame.QUANTITY_COMBO[0], "")));
    }

    @Test
    public void testContainerMedium1() {
        MetropolisContainer con = new MetropolisContainer("", "", "",
                MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0]);
        assertTrue(con.equals(con));
        assertFalse(con.equals(null));
        assertFalse(con.equals(3));
        assertTrue(con.equals(new MetropolisContainer("", "", "",
                MetropolisesFrame.QUANTITY_COMBO[0], MetropolisesFrame.MATCH_COMBO[0])));
    }

    @Test
    public void testContainerHard0() {
        MetropolisContainer con1 = new MetropolisContainer("", "", "");
        assertEquals("", con1.SQLQuantity());
        assertEquals("", con1.SQLMatchContinent());
        assertEquals("", con1.SQLMatchMetropolis());
    }

    @Test
    public void testContainerHard1() {
        MetropolisContainer con1 = new MetropolisContainer(METROPOLIS, "", POPULATION,
                MetropolisesFrame.QUANTITY_COMBO[0], "");
        assertEquals(MetropolisesFrame.POPULATION + MetropolisesBrain.SQL_LARGER_THAN
                + POPULATION, con1.SQLQuantity());
        assertEquals("", con1.SQLMatchMetropolis());
        MetropolisContainer con2 = new MetropolisContainer("", CONTINENT, POPULATION,
                MetropolisesFrame.QUANTITY_COMBO[1], "");
        assertEquals(MetropolisesFrame.POPULATION + MetropolisesBrain.SQL_SMALLER_THEN
                + POPULATION, con2.SQLQuantity());
        assertEquals("", con2.SQLMatchContinent());
    }

    @Test
    public void testContainerHard2() {
        MetropolisContainer con1 = new MetropolisContainer(METROPOLIS, "", POPULATION,
                "", MetropolisesFrame.MATCH_COMBO[0]);
        assertEquals(MetropolisesFrame.METROPOLIS + MetropolisesBrain.SQL_LIKE
                + MetropolisesBrain.SQL_CHAR
                + METROPOLIS + MetropolisesBrain.SQL_CHAR, con1.SQLMatchMetropolis());
        assertEquals("", con1.SQLQuantity());
        MetropolisContainer con2 = new MetropolisContainer(METROPOLIS, "", "",
                "", MetropolisesFrame.MATCH_COMBO[1]);
        assertEquals(MetropolisesFrame.METROPOLIS + MetropolisesBrain.SQL_LIKE
                + MetropolisesBrain.SQL_CHAR + MetropolisesBrain.SQL_ANY_CHARS + METROPOLIS
                + MetropolisesBrain.SQL_ANY_CHARS
                + MetropolisesBrain.SQL_CHAR, con2.SQLMatchMetropolis());
    }

    @Test
    public void testContainerHard3() {
        MetropolisContainer con1 = new MetropolisContainer("", CONTINENT, POPULATION,
                "", MetropolisesFrame.MATCH_COMBO[0]);
        assertEquals(MetropolisesFrame.CONTINENT + MetropolisesBrain.SQL_LIKE
                        + MetropolisesBrain.SQL_CHAR + CONTINENT + MetropolisesBrain.SQL_CHAR,
                con1.SQLMatchContinent());
        assertEquals("", con1.SQLQuantity());
        MetropolisContainer con2 = new MetropolisContainer("", CONTINENT, "",
                "", MetropolisesFrame.MATCH_COMBO[1]);
        assertEquals(MetropolisesFrame.CONTINENT + MetropolisesBrain.SQL_LIKE
                + MetropolisesBrain.SQL_CHAR + MetropolisesBrain.SQL_ANY_CHARS + CONTINENT +
                MetropolisesBrain.SQL_ANY_CHARS + MetropolisesBrain.SQL_CHAR, con2.SQLMatchContinent());
    }
}