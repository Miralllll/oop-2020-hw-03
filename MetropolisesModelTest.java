import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetropolisesModelTest {
    @Test
    public void testEasy0() {
        MetropolisesModel mod = new MetropolisesModel();
        assertEquals(0, mod.getRowCount());
        assertEquals(3, mod.getColumnCount());
        assertEquals(String.class, mod.getColumnClass(0));
        assertEquals(false, mod.isCellEditable(0, 0));
    }

    @Test
    public void testEasy1() {
        MetropolisesModel mod = new MetropolisesModel();
        assertEquals(MetropolisesFrame.METROPOLIS, mod.getColumnName(0));
        assertEquals(MetropolisesFrame.CONTINENT, mod.getColumnName(1));
        assertEquals(MetropolisesFrame.POPULATION, mod.getColumnName(2));
    }

    @Test
    public void testMedium0() {
        MetropolisesModel mod = new MetropolisesModel();
        MetropolisContainer con = new MetropolisContainer(MetropolisContainerTest.METROPOLIS,
                MetropolisContainerTest.CONTINENT, MetropolisContainerTest.POPULATION);
        mod.addRow(con);
        assertEquals(1, mod.getRowCount());
        assertEquals(3, mod.getColumnCount());
        assertEquals(MetropolisContainerTest.METROPOLIS, mod.getValueAt(0, 0));
        assertEquals(MetropolisContainerTest.CONTINENT, mod.getValueAt(0, 1));
        assertEquals(MetropolisContainerTest.POPULATION, mod.getValueAt(0, 2));
    }

    @Test
    public void testMedium1() {
        MetropolisesModel mod = new MetropolisesModel();
        Assertions.assertThrows(Exception.class, () -> {
            mod.getValueAt(0, 0);
        });
        mod.addRow(new MetropolisContainer("New York", "North America", "21295000"));
        Assertions.assertThrows(Exception.class, () -> {
            mod.getValueAt(1, 0);
        });
        Assertions.assertThrows(Exception.class, () -> {
            mod.getValueAt(-1, 0);
        });
        Assertions.assertThrows(Exception.class, () -> {
            mod.getValueAt(0, 4);
        });
        Assertions.assertThrows(Exception.class, () -> {
            mod.getValueAt(0, -4);
        });
    }

    @Test
    public void testHard0() {
        MetropolisesModel mod = new MetropolisesModel();
        List<MetropolisContainer> rows = new ArrayList<>();
        rows.add(new MetropolisContainer("New York", "North America", "21295000"));
        rows.add(new MetropolisContainer("San Francisco", "North America", "5780000"));
        rows.add(new MetropolisContainer("New York", "North America", "21295000"));
        rows.add(new MetropolisContainer("San Jose", "North America", "7354555"));
        mod.setTable(rows);
        List<MetropolisContainer> rowsFromMod = mod.getTable();
        assertEquals(4, mod.getRowCount());
        assertEquals(3, mod.getColumnCount());
        assertEquals(rows.get(0), rowsFromMod.get(0));
        assertEquals(rows.get(1), rowsFromMod.get(1));
        assertEquals(rows.get(2), rowsFromMod.get(2));
        assertEquals(rows.get(3), rowsFromMod.get(3));
    }
}