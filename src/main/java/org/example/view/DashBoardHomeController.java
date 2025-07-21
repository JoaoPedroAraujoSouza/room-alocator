package org.example.view;

import org.example.view.BaseDashboardController;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.models.Room;
import org.example.models.Classroom;
import org.example.models.TimeAllocation;
import org.example.service.RoomService;
import org.example.service.ClassroomService;
import org.example.service.TimeAllocationService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;
import javafx.scene.text.Text;

public class DashBoardHomeController extends BaseDashboardController {
    @FXML
    private VBox centerVBox;
    @FXML
    private HBox cardsHBox;
    @FXML
    private PieChart pieChart;
    @FXML
    private Text pieChartTitle;
    @FXML
    private Pane card1;
    @FXML
    private Pane card2;
    @FXML
    private Pane card3;

    private final RoomService roomService = new RoomService();
    private final ClassroomService classroomService = new ClassroomService();
    private final TimeAllocationService timeAllocationService = new TimeAllocationService();

    @FXML
    public void initialize() {
        setupCards();
        setupPieChart();
    }

    private void setupCards() {
        List<Room> rooms = roomService.getAll();
        List<Classroom> classrooms = classroomService.getAll();
        List<TimeAllocation> allocations = timeAllocationService.getAll();
        Set<Long> allocatedRoomIds = new HashSet<>();
        for (TimeAllocation alloc : allocations) {
            if (alloc.getRoom() != null) {
                allocatedRoomIds.add(alloc.getRoom().getId());
            }
        }
        int totalRooms = rooms.size();
        int allocatedRooms = allocatedRoomIds.size();
        int totalClassrooms = classrooms.size();
        int totalAllocations = allocations.size();

        card1.getChildren().clear();
        card2.getChildren().clear();
        card3.getChildren().clear();

        Color textColor = Color.WHITE;
        String fontFamily = "Bookman Old Style";

        Label l1Title = new Label("Rooms");
        l1Title.setFont(Font.font(fontFamily, 28));
        l1Title.setTextFill(textColor);
        l1Title.setAlignment(Pos.CENTER_LEFT);
        Label l1 = new Label(allocatedRooms + "/" + totalRooms + " Allocated");
        l1.setFont(Font.font(fontFamily, 38));
        l1.setTextFill(textColor);
        l1.setAlignment(Pos.CENTER_LEFT);

        Label l2Title = new Label("Classrooms");
        l2Title.setFont(Font.font(fontFamily, 28));
        l2Title.setTextFill(textColor);
        l2Title.setAlignment(Pos.CENTER_LEFT);
        Label l2 = new Label(String.valueOf(totalClassrooms));
        l2.setFont(Font.font(fontFamily, 38));
        l2.setTextFill(textColor);
        l2.setAlignment(Pos.CENTER_LEFT);

        Label l3Title = new Label("Allocations");
        l3Title.setFont(Font.font(fontFamily, 28));
        l3Title.setTextFill(textColor);
        l3Title.setAlignment(Pos.CENTER_LEFT);
        Label l3 = new Label(String.valueOf(totalAllocations));
        l3.setFont(Font.font(fontFamily, 38));
        l3.setTextFill(textColor);
        l3.setAlignment(Pos.CENTER_LEFT);

        VBox vbox1 = new VBox(12, l1Title, l1);
        vbox1.setAlignment(Pos.CENTER_LEFT);
        vbox1.setStyle("-fx-padding: 12px 0 0 20px;");
        VBox vbox2 = new VBox(12, l2Title, l2);
        vbox2.setAlignment(Pos.CENTER_LEFT);
        vbox2.setStyle("-fx-padding: 12px 0 0 20px;");
        VBox vbox3 = new VBox(12, l3Title, l3);
        vbox3.setAlignment(Pos.CENTER_LEFT);
        vbox3.setStyle("-fx-padding: 12px 0 0 20px;");

        card1.getChildren().add(vbox1);
        card2.getChildren().add(vbox2);
        card3.getChildren().add(vbox3);
    }

    private void setupPieChart() {
        List<Room> rooms = roomService.getAll();
        List<TimeAllocation> allocations = timeAllocationService.getAll();
        Set<Long> allocatedRoomIds = new HashSet<>();
        for (TimeAllocation alloc : allocations) {
            if (alloc.getRoom() != null) {
                allocatedRoomIds.add(alloc.getRoom().getId());
            }
        }
        int totalRooms = rooms.size();
        int allocatedRooms = allocatedRoomIds.size();
        int freeRooms = totalRooms - allocatedRooms;

        // Controle visual: só mostra o gráfico se houver salas
        if (rooms.isEmpty()) {
            pieChart.setVisible(false);
            if (pieChartTitle != null) pieChartTitle.setVisible(false);
            return;
        } else {
            pieChart.setVisible(true);
            if (pieChartTitle != null) pieChartTitle.setVisible(true);
        }

        PieChart.Data allocatedData = new PieChart.Data("Allocated Rooms", allocatedRooms);
        PieChart.Data freeData = new PieChart.Data("Free Rooms", freeRooms);
        pieChart.getData().clear();
        pieChart.getData().addAll(allocatedData, freeData);
        pieChart.setTitle(null);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setLegendSide(javafx.geometry.Side.RIGHT);
        pieChart.setStartAngle(90);
        pieChart.applyCss();
        if (!pieChart.getData().isEmpty()) {
            for (PieChart.Data data : pieChart.getData()) {
                if (data.getName().equals("Allocated Rooms")) {
                    data.getNode().setStyle("-fx-pie-color: #1976d2;");
                } else {
                    data.getNode().setStyle("-fx-pie-color: #b0bec5;");
                }
            }
        }
        Platform.runLater(() -> {
            pieChart.lookupAll(".chart-legend-item").forEach(item -> {
                String label = item.lookup(".label").toString();
                item.lookupAll(".label").forEach(l -> l.setStyle("-fx-text-fill: #222; -fx-font-weight: bold;"));
                item.lookupAll(".chart-legend-item-symbol").forEach(symbol -> {
                    if (label.contains("Allocated Rooms")) {
                        symbol.setStyle("-fx-background-color: #1976d2, transparent;");
                    } else {
                        symbol.setStyle("-fx-background-color: #b0bec5, transparent;");
                    }
                });
            });
            pieChart.lookupAll(".chart-pie-label").forEach(label -> label.setStyle("-fx-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;"));
        });
    }
}
