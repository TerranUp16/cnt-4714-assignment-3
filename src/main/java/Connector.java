
/*Name:Robert Schwyzer
  Course:CNT 4714 – Fall 2020
  Assignment title: Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC
  Date: Sunday November 1, 2020
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class Connector {
  Connection conn = null;
  String error = "";

  public Connector(String dbString, String username, String password) {
    registerInstance();
    try {
      conn = DriverManager.getConnection(dbString + "&user=" + username + "&password=" + password);
    } catch (SQLException ex) {
      error+=ex.toString();
    }
  }

  public void registerInstance() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
    } catch (Exception ex) {
      error+=ex.toString();
    }
  }

  public String getError() {
    return error;
  }

  public boolean isSelect(String q) {
    Pattern selectPattern = Pattern.compile("^(EXPLAIN[\s]+|ANALYZE[\s]+){0,1}SELECT[\s]+", Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
    Pattern showPattern = Pattern.compile("^SHOW[\s]+", Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
    Matcher matcher = selectPattern.matcher(q);
    Matcher showMatcher = showPattern.matcher(q);
    return matcher.find() || showMatcher.find();
  }

  public FullResults querySelect(String q) {
    error = "";
    try (Statement stmt = conn.createStatement()) {
      try (ResultSet rs = stmt.executeQuery(q)) {
        try {
          ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
          int columns = md.getColumnCount();
          List<String[]> rows = new ArrayList<>();
          while (rs.next()) {
            String[] row = new String[columns];
            for (int i = 0; i < columns; i++) {
              row[i] = rs.getString(i+1);
            }
            rows.add(row);
          }
          return new FullResults(rows, md);
        } catch (SQLException sqlMDEx) {
          error+=sqlMDEx.toString();
          return null;
        }
      } catch (SQLException sqlEx) {
        error+=sqlEx.toString();
        return null;
      }
    } catch (SQLException ex) {
      error+=ex.toString();
      return null;
    }
  }

  public int queryOther(String q) {
    error="";
    try (Statement stmt = conn.createStatement()) {
      return stmt.executeUpdate(q);
    } catch (SQLException ex) {
      error+=ex.toString();
      return -1;
    }
  }
}