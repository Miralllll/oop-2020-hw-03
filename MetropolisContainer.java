import java.util.ArrayList;
import java.util.List;

/**
 * MetropolisContainer is helper-object which saves all information from the
 * JTextFields and ComboBoxes. It has constructor and getter methods,
 * also some extra methods which translates combo-box commands into sqlLanguage.
 */
class MetropolisContainer {

    private String metropolis, continent, population;
    private String quantity, match;

    public MetropolisContainer(String metropolis, String continent, String population,
                               String quantity, String match) {
        this.metropolis = metropolis;
        this.continent = continent;
        this.population = population;
        this.quantity = quantity;
        this.match = match;
    }

    public MetropolisContainer(String metropolis, String continent, String population) {
        this(metropolis, continent, population, "", "");
    }

    public MetropolisContainer(List<String> ls) throws IndexOutOfBoundsException {
        this(ls.get(0), ls.get(1), ls.get(2), "", "");
    }

    /**
     * returns metropolis' name
     */
    public String getMetropolis() {
        return metropolis;
    }

    /**
     * returns continent's name
     */
    public String getContinent() {
        return continent;
    }

    /**
     * returns an amount of the population
     */
    public String getPopulation() {
        return population;
    }

    /**
     * returns command from population quantity comparator combo-box
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * returns command from metropolis-continent comparator combo-box
     */
    public String getMatch() {
        return match;
    }

    /**
     * returns metropolis, continent and population together as a list
     */
    public List<String> getAsListRow() {
        List<String> newRow = new ArrayList<>();
        newRow.add(metropolis);
        newRow.add(continent);
        newRow.add(population);
        return newRow;
    }

    /**
     * Checks if all fields had been empty when a client clicked a button
     */
    public boolean allFieldsEmpty() {
        return metropolis.equals("") && continent.equals("") && population.equals("");
    }

    /**
     * Standardised equal method
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MetropolisContainer)) return false;
        MetropolisContainer other = (MetropolisContainer) obj;
        return metropolis.equals(other.getMetropolis()) && continent.equals(other.getContinent())
                && population.equals(other.getPopulation()) && quantity.equals(other.getQuantity())
                && match.equals(other.getMatch());
    }

    /**
     * Checks if population field has been empty when a client clicked the search button.
     * If it was return "", else translates from combo-quantity-command to sql-command
     */
    public String SQLQuantity() {
        String result = "";
        if (population.equals("")) return result;
        result += MetropolisesFrame.POPULATION;
        if (quantity.equals(MetropolisesFrame.QUANTITY_COMBO[0]))
            result += MetropolisesBrain.SQL_LARGER_THAN + population;
        else if (quantity.equals(MetropolisesFrame.QUANTITY_COMBO[1]))
            result += MetropolisesBrain.SQL_SMALLER_THEN + population;
        else return "";
        return result;
    }

    /**
     * Checks if metropolis field has been empty when a client clicked the search button.
     * If it was return "", else translates from combo-march-command to sql-command
     */
    public String SQLMatchMetropolis() {
        String result = "";
        if (metropolis.equals("")) return result;
        result += MetropolisesFrame.METROPOLIS +
                MetropolisesBrain.SQL_LIKE + MetropolisesBrain.SQL_CHAR;
        if (match.equals(MetropolisesFrame.MATCH_COMBO[0])) result += metropolis + MetropolisesBrain.SQL_CHAR;
        else if (match.equals(MetropolisesFrame.MATCH_COMBO[1]))
            result += MetropolisesBrain.SQL_ANY_CHARS +
                    metropolis + MetropolisesBrain.SQL_ANY_CHARS + MetropolisesBrain.SQL_CHAR;
        else return "";
        return result;
    }

    /**
     * Checks if continent field has been empty when a client clicked the search button.
     * If it was return "", else translates from combo-march-command to sql-command
     */
    public String SQLMatchContinent() {
        String result = "";
        if (continent.equals("")) return result;
        result += MetropolisesFrame.CONTINENT +
                MetropolisesBrain.SQL_LIKE + MetropolisesBrain.SQL_CHAR;
        if (match.equals(MetropolisesFrame.MATCH_COMBO[0])) result += continent + MetropolisesBrain.SQL_CHAR;
        else if (match.equals(MetropolisesFrame.MATCH_COMBO[1]))
            result += MetropolisesBrain.SQL_ANY_CHARS +
                    continent + MetropolisesBrain.SQL_ANY_CHARS + MetropolisesBrain.SQL_CHAR;
        else return "";
        return result;
    }
}
