package nettrace;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import nettrace.TracerouteRunner;
import nettrace.TracerouteHop;
import javafx.beans.property.SimpleIntegerProperty;



public class UI extends Application {

    @Override
    public void start(Stage stage) {
    	// ----- UI Elements -----

        // Input field for domain/IP
        TextField inputField = new TextField();
        inputField.setPromptText("Enter domain or IP");
        
        // Button to trigger traceroute
        Button traceButton = new Button("Trace Route");

        // ----- TableView Setup -----
        TableView<TracerouteHop> tableView = new TableView<>();
        
        // Hop number column
        TableColumn<TracerouteHop, Number> hopCol = new TableColumn<>("Hop");
        hopCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getHopNumber()));
        hopCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.03));
        
        // Hostname column
        TableColumn<TracerouteHop, String> hostnameCol = new TableColumn<>("Hostname");
        hostnameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHostname()));
        hostnameCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));

        
        // IP Address column
        TableColumn<TracerouteHop, String> ipCol = new TableColumn<>("IP Address");
        ipCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIpAddress()));
        ipCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10));
        
        // Latency column with timeout handling
        TableColumn<TracerouteHop, String> latencyCol = new TableColumn<>("Latency (ms)");
        latencyCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getLatency() == -1 ? "Timeout" : String.valueOf(data.getValue().getLatency())
        ));
        
        // Location column: city, region, zip, country
        TableColumn<TracerouteHop, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(data -> new SimpleStringProperty(
            String.format("%s, %s, %s %s",
                safe(data.getValue().getCity()),
                safe(data.getValue().getRegion()),
                safe(data.getValue().getZip()),
                safe(data.getValue().getCountry()))
        ));
        locationCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.24));
        
        // ISP column
        TableColumn<TracerouteHop, String> ispCol = new TableColumn<>("ISP");
        ispCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getIsp() != null ? data.getValue().getIsp() : "unknown"
        ));
        ispCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        
        // Explanation column (firewall/timeout notes)
        TableColumn<TracerouteHop, String> noteCol = new TableColumn<>("Explanation");
        noteCol.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getNote() != null ? data.getValue().getNote() : ""
        ));
        noteCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));

        // Add columns to the table
        tableView.getColumns().addAll(hopCol, ipCol, hostnameCol, latencyCol, locationCol, ispCol, noteCol);
        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);


       
        // Row coloring based on latency
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(TracerouteHop hop, boolean empty) {
                super.updateItem(hop, empty);
                if (hop == null || empty) {
                    setStyle("");
                } else {
                    long latency = hop.getLatency();
                    if (latency == -1) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else if (latency <= 20) {
                        setStyle("-fx-background-color: lightgreen;");
                    } else if (latency <= 100) {
                        setStyle("-fx-background-color: khaki;");
                    } else {
                        setStyle("-fx-background-color: lightcoral;");
                    }
                }
            }
        });

        // Button action
        traceButton.setOnAction(e -> {
            String target = inputField.getText().trim();
            if (!target.isEmpty()) {
                tableView.getItems().clear();
                List<TracerouteHop> hops = TracerouteRunner.runTraceroute(target);
                tableView.getItems().addAll(hops);
            }
        });
        
        Label legend = new Label("Green < 20ms (Fast)   |   Yellow 20–100ms (Moderate)   |   Red >100ms or Timeout (Slow/Failed)");
        legend.setStyle("-fx-font-size: 12px; -fx-padding: 0 0 5 5;");

        // Layout and Stage Setup
        VBox layout = new VBox(10, inputField, traceButton, legend, tableView);
        layout.setStyle("-fx-padding: 10px");

        stage.setScene(new Scene(layout, 1000, 500));
        stage.setTitle("NetTrace – Visual Traceroute Tool");
        stage.show();
    }
    
 // Helper to format missing/null fields
    private String safe(String value) {
        return value != null && !value.isBlank() ? value : "unknown";
    }

}
