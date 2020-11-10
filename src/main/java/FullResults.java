
/*Name:Robert Schwyzer
  Course:CNT 4714 – Fall 2020
  Assignment title: Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC
  Date: Sunday November 1, 2020
*/
import java.sql.ResultSet;
import java.util.List;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class FullResults {
  List<String[]> results;
  ResultSetMetaData metadata;

  public FullResults(List<String[]> r, ResultSetMetaData m) {
    results = r;
    metadata = m;
  }

  public List<String[]> getRows() {
    return results;
  }

  public ResultSetMetaData getMetadata() {
    return metadata;
  }
}
