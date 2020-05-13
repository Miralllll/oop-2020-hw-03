import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Everything what should be done by program in the metropolis sql-base is here
 */
public class MetropolisesBrain {

    private Connection conn;

    public MetropolisesBrain() throws SQLException, ClassNotFoundException, IOException {
        isConnection(true); // creates connection
        addDataBase();
    }

    private void addDataBase() throws SQLException, IOException {
        try {
            Statement statement = conn.createStatement();
            statement.execute("USE " + MyDataBaseINFO.MY_DB_NAME);
        } catch (SQLException e) {
            recreateDataBase();
            readFile();
        }
    }

    /**
     * Initialize connection between sql-base and brain
     */
    public void setConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + MyDataBaseINFO.DB_SERVER + "/?useUnicode=true&useJDBC" +
                        "CompliantTimezoneShift=true" +
                        "&useLegacyDatetimeCode=false&serverTimezone=UTC",
                MyDataBaseINFO.USERNAME, MyDataBaseINFO.PASSWORD);
    }

    /**
     * Inserts new row in the data-base
     */
    public void addRow(MetropolisContainer con) throws SQLException, NumberFormatException {
        String statementTx = "INSERT INTO " + MyDataBaseINFO.TABLE_NAME + " VALUES (?, ?, ?);";
        PreparedStatement st = conn.prepareStatement(statementTx);
        st.setString(1, con.getMetropolis());
        st.setString(2, con.getContinent());
        st.setLong(3, Long.parseLong(con.getPopulation()));
        st.executeUpdate();
    }

    /**
     * Gets a list of the rows in the data-base which is asked from controller
     */
    public List<MetropolisContainer> getRows(MetropolisContainer con) throws SQLException {
        String statementTx = "SELECT * FROM " + MyDataBaseINFO.TABLE_NAME;
        List<MetropolisContainer> ls = new ArrayList<>();
        if (!con.allFieldsEmpty()) statementTx += " WHERE " + createWhereCommand(con) + " ;";
        System.out.println(statementTx);
        PreparedStatement st = conn.prepareStatement(statementTx);
        ResultSet res = st.executeQuery();
        while (res.next())
            ls.add(new MetropolisContainer(res.getString(1), res.getString(2),
                    Long.toString(res.getLong(3))));
        return ls;
    }

    /**
     * Creates command according to container information
     */
    private String createWhereCommand(MetropolisContainer con) throws RuntimeException {
        String mRes = con.SQLMatchMetropolis(), cRes = con.SQLMatchContinent(),
                qRes = con.SQLQuantity(), result = "";
        if (!qRes.equals("")) result += qRes + " AND ";
        if (!mRes.equals("")) result += mRes + " AND ";
        if (!cRes.equals("")) result += cRes + " AND ";
        System.out.println(result);
        return result.substring(0, result.length() - " AND ".length());
    }

    /**
     * Returns every row from the table in the form of metropolisContainer
     */
    public List<MetropolisContainer> getAllRows() throws SQLException {
        String statementTx = "SELECT * FROM " + MyDataBaseINFO.TABLE_NAME;
        List<MetropolisContainer> ls = new ArrayList<>();
        PreparedStatement st = conn.prepareStatement(statementTx);
        ResultSet res = st.executeQuery();
        while (res.next()) ls.add(new MetropolisContainer(res.getString(1), res.getString(2),
                Long.toString(res.getLong(3))));
        return ls;
    }

    /**
     * Creates sql-database-metropolis
     */
    public void recreateDataBase() throws SQLException {
        Statement stm = conn.createStatement();
        stm.execute(dropDataBaseSQLStatement());
        stm.execute("CREATE DATABASE " + MyDataBaseINFO.MY_DB_NAME);
        stm.execute("USE " + MyDataBaseINFO.MY_DB_NAME);
        stm.execute(dropTableSQLStatement());
        stm.execute(createTableSQLStatement());
    }

    /**
     * Removes database and its table too
     */
    public void removeDataBase() throws SQLException {
        Statement stm = null;
        stm = conn.createStatement();
        stm.execute(dropTableSQLStatement());
        stm.execute(dropDataBaseSQLStatement());
    }

    /**
     * Returns only a SQL statement
     */
    private String dropDataBaseSQLStatement() {
        return "DROP DATABASE IF EXISTS " + MyDataBaseINFO.MY_DB_NAME;
    }

    /**
     * Returns only a SQL statement
     */
    private String dropTableSQLStatement() {
        return "DROP TABLE IF EXISTS " + MyDataBaseINFO.TABLE_NAME;
    }

    /**
     * returns sql-statement which creates table
     */
    private String createTableSQLStatement() {
        return "CREATE TABLE " + MyDataBaseINFO.TABLE_NAME + "(" + MetropolisesFrame.METROPOLIS + " CHAR(64), "
                + MetropolisesFrame.CONTINENT + " CHAR(64), " + MetropolisesFrame.POPULATION + " BIGINT);";
    }

    /**
     * It is for closing connection after using it
     */
    public void isConnection(boolean connection) throws SQLException, ClassNotFoundException {
        if (!connection) conn.close();
        else setConnection();
    }


    // It's code of reading SQL code and running it from here.
    private void readFile() throws IOException, SQLException {
        Statement statement = conn.createStatement();
        BufferedReader bf = new BufferedReader(new FileReader(MyDataBaseINFO.FILE_NAME));
        String line = "";
        while (true){
            String curr = bf.readLine();
            if (curr == null) break;
            line += curr;
            if (line.contains(";")) {
                statement.execute(line);
                line = "";
            }
        }
    }

    public static final String SQL_LARGER_THAN = " > ", SQL_SMALLER_THEN = " < ";
    public static final String SQL_LIKE = " LIKE ", SQL_CHAR = "'", SQL_ANY_CHARS = "%";
}
