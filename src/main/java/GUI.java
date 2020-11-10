
/*Name:Robert Schwyzer
  Course:CNT 4714 – Fall 2020
  Assignment title: Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC
  Date: Sunday November 1, 2020
*/
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class GUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    var connWrapper = new Object() { Connector conn; };

    // Primary grid is just a list of rows
    GridPane mainGrid = new GridPane();
    mainGrid.setHgap(10);
    mainGrid.setVgap(10);
    mainGrid.setPadding(new Insets(10));
    mainGrid.getStyleClass().add(JMetroStyleClass.BACKGROUND);

    ColumnConstraints mainCol = new ColumnConstraints();
    mainCol.setPercentWidth(100);

    mainGrid.getColumnConstraints().addAll(mainCol);

    // Top grid provides a 50% split for "Enter Database Information" and "Enter An
    // SQL Command"
    GridPane topGrid = new GridPane();
    topGrid.setVgap(15);
    topGrid.setHgap(30);
    topGrid.setPadding(new Insets(10));
    topGrid.getStyleClass().add(JMetroStyleClass.BACKGROUND);

    ColumnConstraints top1 = new ColumnConstraints();
    top1.setPercentWidth(50);

    ColumnConstraints top2 = new ColumnConstraints();
    top2.setPercentWidth(50);

    topGrid.getColumnConstraints().addAll(top1, top2);

    // Database grid provides an input form for connection information
    GridPane databaseGrid = new GridPane();
    databaseGrid.setVgap(25);
    databaseGrid.getStyleClass().add(JMetroStyleClass.BACKGROUND);

    ColumnConstraints db1 = new ColumnConstraints();
    db1.setPercentWidth(25);

    ColumnConstraints db2 = new ColumnConstraints();
    db2.setPercentWidth(75);

    databaseGrid.getColumnConstraints().addAll(db1, db2);

    // Header
    Text dbHeader = new Text("Enter Database Information");
    dbHeader.setFont(Font.font("System", FontWeight.BOLD, Font.getDefault().getSize()));
    topGrid.add(dbHeader, 0, 0, 1, 1);

    // JDBC Driver
    Label jdbcDriverLabel = new Label("JDBC Driver");
    ComboBox<String> jdbcDriverInput = new ComboBox<>();
    jdbcDriverInput.maxWidth(Double.MAX_VALUE);
    jdbcDriverInput.getItems().addAll("mysql-connector-java v8.0.22");
    jdbcDriverInput.getSelectionModel().selectFirst();

    databaseGrid.add(jdbcDriverLabel, 0, 0, 1, 1);
    databaseGrid.add(jdbcDriverInput, 1, 0, 1, 1);

    // Databse Selection
    Label databaseLabel = new Label("Database ...");
    ComboBox<String> databaseInput = new ComboBox<>();
    databaseInput.maxWidth(Double.MAX_VALUE);
    databaseInput.getItems().addAll("jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC");
    databaseInput.getSelectionModel().selectFirst();

    databaseGrid.add(databaseLabel, 0, 1, 1, 1);
    databaseGrid.add(databaseInput, 1, 1, 1, 1);

    // Username Entry
    Label usernameLabel = new Label("Username");
    TextField usernameInput = new TextField();

    databaseGrid.add(usernameLabel, 0, 2, 1, 1);
    databaseGrid.add(usernameInput, 1, 2, 1, 1);

    // Password Entry
    Label passwordLabel = new Label("Password");
    PasswordField passwordInput = new PasswordField();

    databaseGrid.add(passwordLabel, 0, 3, 1, 1);
    databaseGrid.add(passwordInput, 1, 3, 1, 1);
    
    topGrid.add(databaseGrid, 0, 1, 1, 1);

    Text sqlHeader = new Text("Enter an SQL Command");
    sqlHeader.setFont(Font.font("System", FontWeight.BOLD, Font.getDefault().getSize()));
    topGrid.add(sqlHeader, 1, 0, 1, 1);

    TextArea sqlInput = new TextArea();
    topGrid.add(sqlInput, 1, 1, 1, 1);

    mainGrid.add(topGrid, 0, 0, 1, 1);

    // Button and status grid
    GridPane buttonGrid = new GridPane();
    buttonGrid.setVgap(15);
    buttonGrid.setHgap(15);
    buttonGrid.setPadding(new Insets(10));
    buttonGrid.getStyleClass().add(JMetroStyleClass.BACKGROUND);

    ColumnConstraints buttonCols = new ColumnConstraints();
    buttonCols.setPercentWidth(20);

    buttonGrid.getColumnConstraints().addAll(buttonCols, buttonCols, buttonCols, buttonCols, buttonCols);

    TextField status = new TextField("No Connection Now");
    status.setEditable(false);
    status.setStyle("-fx-text-fill: red");

    buttonGrid.add(status, 0, 0, 2, 1);

    Button connectToDatabaseButton = new Button("Connect To Database");
    connectToDatabaseButton.setMaxWidth(Double.MAX_VALUE);
    connectToDatabaseButton.setStyle("-fx-text-fill: skyblue");
    
    buttonGrid.add(connectToDatabaseButton, 2, 0, 1, 1);

    Button clearSQLButton = new Button("Clear SQL Command");
    clearSQLButton.setMaxWidth(Double.MAX_VALUE);
    clearSQLButton.setStyle("-fx-text-fill: red");

    buttonGrid.add(clearSQLButton, 3, 0, 1, 1);

    Button executeSQLButton = new Button("Execute SQL Command");
    executeSQLButton.setMaxWidth(Double.MAX_VALUE);
    executeSQLButton.setStyle("-fx-text-fill: lightgreen");
    executeSQLButton.setDisable(true);

    buttonGrid.add(executeSQLButton, 4, 0, 1, 1);

    mainGrid.add(buttonGrid, 0, 1, 1, 1);

    GridPane results = new GridPane();
    results.setHgap(10);
    results.setVgap(10);
    results.setPadding(new Insets(10));
    ColumnConstraints resultsCol = new ColumnConstraints();
    resultsCol.setPercentWidth(100);
    results.getColumnConstraints().addAll(resultsCol);

    Text sqlExecutionResultLabel = new Text("SQL Execution Result Window");
    sqlExecutionResultLabel.setFont(Font.font("System", FontWeight.BOLD, Font.getDefault().getSize()));

    results.add(sqlExecutionResultLabel, 0, 0, 1, 1);

    TableView<String[]> table = new TableView<>();
    //TextArea sqlResults = new TextArea();
    //sqlResults.setEditable(false);

    results.add(table, 0, 1, 1, 4);

    Button clearResultsButton = new Button("Clear Result Window");
    clearResultsButton.setMaxWidth(Double.MAX_VALUE);
    clearResultsButton.setStyle("-fx-text-fill: yellow");

    results.add(clearResultsButton, 0, 5, 1, 1);

    mainGrid.add(results, 0, 2, 1, 1);

    /******BUTTONS******/

    // Connect to Database
    connectToDatabaseButton.setOnAction(event -> {
      String dbString = databaseInput.getValue();
      connWrapper.conn = new Connector(dbString, usernameInput.getText(), passwordInput.getText());

      if (connWrapper.conn.getError() != "") {
        executeSQLButton.setDisable(true);
        status.setText("No Connection Now");
        status.setStyle("-fx-text-fill: red");
        Alert connectError = new Alert(AlertType.ERROR);
        connectError.setTitle("Database Connection Failed");
        connectError.setContentText(connWrapper.conn.getError());
        connectError.setResizable(true);
        connectError.showAndWait();
      } else {
        status.setText("Connected to " + dbString);
        status.setTooltip(new Tooltip("Connected to " + dbString));
        status.setStyle("-fx-text-fill: green");
        executeSQLButton.setDisable(false);
      }
    });

    // Clear SQL Command
    clearSQLButton.setOnAction(event -> sqlInput.clear());

    // Execute SQL Command
    executeSQLButton.setOnAction(event -> {
      String query = sqlInput.getText();
      table.getColumns().clear();
      if (connWrapper.conn.isSelect(query)) {
        FullResults result = connWrapper.conn.querySelect(query);

        if (connWrapper.conn.getError() != "") {
          Alert queryError = new Alert(AlertType.ERROR);
          queryError.setTitle("Query Error");
          queryError.setContentText(connWrapper.conn.getError());
          queryError.setResizable(true);
          queryError.showAndWait();
        } else {
          // Output results to table
          try {
            int columns = result.getMetadata().getColumnCount();

            for (int i = 0; i < columns; i++) {
              final int colI = i;
              TableColumn<String[],String> col = new TableColumn<>(result.getMetadata().getColumnLabel(i+1));
              col.setCellValueFactory(p -> {
                String[] theRow = p.getValue();
                return new ReadOnlyObjectWrapper<String>(theRow[colI]);
              });
              table.getColumns().add(col);
            }

            List<String[]> rows = result.getRows();
            table.setItems(FXCollections.observableArrayList(rows));
          } catch (SQLException ex) {
            Alert metaError = new Alert(AlertType.ERROR);
            metaError.setTitle("Metadata Error");
            metaError.setContentText(ex.toString());
            metaError.setResizable(true);
            metaError.showAndWait();
          }
        }
      } else {
        int result = connWrapper.conn.queryOther(query);

        if (result < 0) {
          Alert queryError = new Alert(AlertType.ERROR);
          queryError.setTitle("Query Error");
          queryError.setContentText(connWrapper.conn.getError());
          queryError.setResizable(true);
          queryError.showAndWait();
        }
      }
    });

    clearResultsButton.setOnAction(event -> table.getColumns().clear());
    
    Scene scene = new Scene(mainGrid); // Add top-level element
    JMetro jMetro = new JMetro(Style.DARK);
    jMetro.setScene(scene);

    primaryStage.setTitle("Project Three: Two-Tier Client-Server Application Development With MySQL and JDBC");
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}