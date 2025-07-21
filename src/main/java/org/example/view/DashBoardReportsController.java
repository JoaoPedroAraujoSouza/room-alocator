package org.example.view;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.example.models.Classroom;
import org.example.models.Room;
import org.example.models.Subject;
import org.example.models.TimeAllocation;
import org.example.service.ClassroomService;
import org.example.service.RoomService;
import org.example.service.SubjectService;
import org.example.service.TimeAllocationService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DashBoardReportsController extends BaseDashboardController {
    @FXML
    private TableView<ReportRow> reportTable;
    @FXML
    private TableColumn<ReportRow, String> dayColumn;

    @FXML
    private TableView<RoomUsageRow> roomUsageTable;
    @FXML
    private TableColumn<RoomUsageRow, String> roomNameColumn;
    @FXML
    private TableColumn<RoomUsageRow, String> occupancyRateColumn;
    @FXML
    private TableColumn<RoomUsageRow, String> mostUsedSlotColumn;
    @FXML
    private TableColumn<RoomUsageRow, String> totalAllocationsColumn;

    @FXML
    private TableView<ClassroomDistributionRow> classroomDistributionTable;
    @FXML
    private TableColumn<ClassroomDistributionRow, String> distributionValueColumn;
    @FXML
    private TableColumn<ClassroomDistributionRow, String> classroomCountColumn;
    @FXML
    private TableColumn<ClassroomDistributionRow, String> allocationCountColumn;
    @FXML
    private ComboBox<String> distributionTypePicker;

    private final TimeAllocationService timeAllocationService = new TimeAllocationService();
    private final RoomService roomService = new RoomService();
    private final ClassroomService classroomService = new ClassroomService();
    private final SubjectService subjectService = new SubjectService();
    private final Map<String, TableColumn<ReportRow, String>> timeColumns = new HashMap<>();
    private ObservableList<ClassroomDistributionRow> allDistributionRows = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTimeColumns();
        setupWeeklyAllocationsTable();
        setupRoomUsageTable();
        setupClassroomDistributionTable();
        setupDistributionTypePicker();
    }

    private void setupDistributionTypePicker() {
        distributionTypePicker.getItems().addAll("Teacher", "Shift", "Subject");
        distributionTypePicker.setValue("Teacher");
        distributionTypePicker.setOnAction(e -> filterClassroomDistribution());
    }

    private void filterClassroomDistribution() {
        String selectedType = distributionTypePicker.getValue();
        updateClassroomDistributionTable(selectedType);
        updateColumnHeader(selectedType);
    }

    private void updateColumnHeader(String selectedType) {
        if ("Teacher".equals(selectedType)) {
            distributionValueColumn.setText("Teacher");
        } else if ("Shift".equals(selectedType)) {
            distributionValueColumn.setText("Shift");
        } else if ("Subject".equals(selectedType)) {
            distributionValueColumn.setText("Subject");
        }
    }

    private void setupWeeklyAllocationsTable() {
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));

        List<TimeAllocation> allAllocations = timeAllocationService.getAll();

        Set<String> customTimeSlots = new HashSet<>();
        for (TimeAllocation allocation : allAllocations) {
            if (allocation.getTimeBlock() != null) {
                List<String> timeSlots = getIntersectingTimeSlots(allocation.getTimeBlock().getStartTime(), allocation.getTimeBlock().getEndTime());
                for (String slot : timeSlots) {
                    if (!timeColumns.containsKey(slot)) {
                        customTimeSlots.add(slot);
                    }
                }
            }
        }

        for (String customSlot : customTimeSlots) {
            addCustomTimeColumn(customSlot);
        }

        Map<DayOfWeek, Map<String, List<TimeAllocation>>> groupedByDayAndTime = groupAllocationsByDayAndTime(allAllocations);

        ObservableList<ReportRow> rows = FXCollections.observableArrayList();
        for (DayOfWeek day : DayOfWeek.values()) {
            ReportRow row = new ReportRow(day.toString());
            Map<String, List<TimeAllocation>> dayAllocs = groupedByDayAndTime.getOrDefault(day, new HashMap<>());

            for (String timeSlot : timeColumns.keySet()) {
                List<TimeAllocation> timeAllocs = dayAllocs.getOrDefault(timeSlot, Collections.emptyList());
                String allocsText = timeAllocs.stream()
                        .map(a -> {
                            String classroom = a.getClassroom() != null ? a.getClassroom().getSemester() : "N/A";
                            String room = a.getRoom() != null ? a.getRoom().getName() : "N/A";
                            return String.format("%s - %s", room, classroom);
                        })
                        .collect(Collectors.joining("; "));
                row.setTimeSlotAllocation(timeSlot, allocsText.isEmpty() ? "Empty" : allocsText);
            }
            rows.add(row);
        }
        reportTable.setItems(rows);
    }

    private void setupRoomUsageTable() {
        roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        occupancyRateColumn.setCellValueFactory(new PropertyValueFactory<>("occupancyRate"));
        mostUsedSlotColumn.setCellValueFactory(new PropertyValueFactory<>("mostUsedSlot"));
        totalAllocationsColumn.setCellValueFactory(new PropertyValueFactory<>("totalAllocations"));

        List<Room> rooms = roomService.getAll();
        List<TimeAllocation> allAllocations = timeAllocationService.getAll();

        ObservableList<RoomUsageRow> rows = FXCollections.observableArrayList();

        for (Room room : rooms) {
            List<TimeAllocation> roomAllocations = allAllocations.stream()
                    .filter(a -> a.getRoom() != null && a.getRoom().getId() == room.getId())
                    .collect(Collectors.toList());

            double occupancyRate = (double) roomAllocations.size() / 25.0 * 100.0;

            Map<String, Long> slotUsage = roomAllocations.stream()
                    .filter(a -> a.getTimeBlock() != null)
                    .collect(Collectors.groupingBy(
                            a -> getIntersectingTimeSlots(a.getTimeBlock().getStartTime(), a.getTimeBlock().getEndTime()).get(0),
                            Collectors.counting()
                    ));

            String mostUsedSlot = slotUsage.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("None");

            rows.add(new RoomUsageRow(
                    room.getName(),
                    String.format("%.1f%%", occupancyRate),
                    mostUsedSlot,
                    String.valueOf(roomAllocations.size())
            ));
        }

        roomUsageTable.setItems(rows);
    }

    private void setupClassroomDistributionTable() {
        distributionValueColumn.setCellValueFactory(new PropertyValueFactory<>("distributionValue"));
        classroomCountColumn.setCellValueFactory(new PropertyValueFactory<>("classroomCount"));
        allocationCountColumn.setCellValueFactory(new PropertyValueFactory<>("allocationCount"));

        updateClassroomDistributionTable("Teacher");
        updateColumnHeader("Teacher");
    }

    private void updateClassroomDistributionTable(String selectedType) {
        List<Classroom> classrooms = classroomService.getAll();
        List<TimeAllocation> allAllocations = timeAllocationService.getAll();

        allDistributionRows.clear();

        if ("Shift".equals(selectedType)) {
            Map<String, List<Classroom>> shiftDistribution = classrooms.stream()
                    .collect(Collectors.groupingBy(c -> c.getShift() != null ? c.getShift().toString() : "Not Assigned"));

            for (Map.Entry<String, List<Classroom>> entry : shiftDistribution.entrySet()) {
                String shift = entry.getKey();
                List<Classroom> shiftClassrooms = entry.getValue();
                long allocationCount = allAllocations.stream()
                        .filter(a -> a.getClassroom() != null && shiftClassrooms.contains(a.getClassroom()))
                        .count();

                String classroomsList = shiftClassrooms.stream()
                        .map(Classroom::getSemester)
                        .collect(Collectors.joining(", "));

                allDistributionRows.add(new ClassroomDistributionRow(
                        shift,
                        classroomsList,
                        String.valueOf(allocationCount)
                ));
            }
        }

        if ("Teacher".equals(selectedType)) {
            Map<String, List<Classroom>> teacherDistribution = classrooms.stream()
                    .filter(c -> c.getResponsibleTeacher() != null)
                    .collect(Collectors.groupingBy(c -> c.getResponsibleTeacher().getName()));

            for (Map.Entry<String, List<Classroom>> entry : teacherDistribution.entrySet()) {
                String teacher = entry.getKey();
                List<Classroom> teacherClassrooms = entry.getValue();
                long allocationCount = allAllocations.stream()
                        .filter(a -> a.getClassroom() != null && teacherClassrooms.contains(a.getClassroom()))
                        .count();

                String classroomsList = teacherClassrooms.stream()
                        .map(Classroom::getSemester)
                        .collect(Collectors.joining(", "));

                allDistributionRows.add(new ClassroomDistributionRow(
                        teacher,
                        classroomsList,
                        String.valueOf(allocationCount)
                ));
            }
        }

        if ("Subject".equals(selectedType)) {
            try {
                List<Subject> subjects = subjectService.getAll();
                for (Subject subject : subjects) {

                    List<Classroom> subjectClassrooms = classrooms.stream()
                            .filter(c -> c.getResponsibleTeacher() != null)
                            .collect(Collectors.toList());

                    if (!subjectClassrooms.isEmpty()) {
                        long allocationCount = allAllocations.stream()
                                .filter(a -> a.getClassroom() != null && subjectClassrooms.contains(a.getClassroom()))
                                .count();

                        String classroomsList = subjectClassrooms.stream()
                                .map(Classroom::getSemester)
                                .collect(Collectors.joining(", "));

                        allDistributionRows.add(new ClassroomDistributionRow(
                                subject.getName(),
                                classroomsList,
                                String.valueOf(allocationCount)
                        ));
                    }
                }
            } catch (Exception e) {
            }
        }

        classroomDistributionTable.setItems(allDistributionRows);
    }

    private void setupTimeColumns() {
        List<String> timeSlots = Arrays.asList(
                "08:00-10:00", "10:00-12:00", "14:00-16:00", "16:00-18:00", "18:00-20:00"
        );

        for (String timeSlot : timeSlots) {
            TableColumn<ReportRow, String> column = new TableColumn<>(timeSlot);
            column.setPrefWidth(160);
            column.setCellValueFactory(new PropertyValueFactory<>(timeSlot.replace(":", "").replace("-", "")));
            timeColumns.put(timeSlot, column);
            reportTable.getColumns().add(column);
        }
    }

    private void addCustomTimeColumn(String timeSlot) {
        if (!timeColumns.containsKey(timeSlot)) {
            TableColumn<ReportRow, String> column = new TableColumn<>(timeSlot);
            column.setPrefWidth(160);
            column.setCellValueFactory(cellData -> {
                ReportRow row = cellData.getValue();
                return new javafx.beans.property.SimpleStringProperty(row.getCustomTimeSlot(timeSlot));
            });
            timeColumns.put(timeSlot, column);
            reportTable.getColumns().add(column);
        }
    }

    private Map<DayOfWeek, Map<String, List<TimeAllocation>>> groupAllocationsByDayAndTime(List<TimeAllocation> allocations) {
        Map<DayOfWeek, Map<String, List<TimeAllocation>>> result = new HashMap<>();

        for (TimeAllocation allocation : allocations) {
            if (allocation.getTimeBlock() == null) continue;

            DayOfWeek day = allocation.getTimeBlock().getDayOfWeek();
            List<String> timeSlots = getIntersectingTimeSlots(allocation.getTimeBlock().getStartTime(), allocation.getTimeBlock().getEndTime());

            for (String timeSlot : timeSlots) {
                result.computeIfAbsent(day, k -> new HashMap<>())
                        .computeIfAbsent(timeSlot, k -> new ArrayList<>())
                        .add(allocation);
            }
        }

        return result;
    }

    private List<String> getIntersectingTimeSlots(LocalTime start, LocalTime end) {
        if (start == null || end == null) return Collections.singletonList("Unknown");

        Map<String, LocalTime[]> timeSlots = Map.of(
                "08:00-10:00", new LocalTime[]{LocalTime.of(8, 0), LocalTime.of(10, 0)},
                "10:00-12:00", new LocalTime[]{LocalTime.of(10, 0), LocalTime.of(12, 0)},
                "14:00-16:00", new LocalTime[]{LocalTime.of(14, 0), LocalTime.of(16, 0)},
                "16:00-18:00", new LocalTime[]{LocalTime.of(16, 0), LocalTime.of(18, 0)},
                "18:00-20:00", new LocalTime[]{LocalTime.of(19, 0), LocalTime.of(22, 0)}
        );

        List<String> intersectingSlots = new ArrayList<>();
        for (Map.Entry<String, LocalTime[]> entry : timeSlots.entrySet()) {
            String slotName = entry.getKey();
            LocalTime slotStart = entry.getValue()[0];
            LocalTime slotEnd = entry.getValue()[1];

            if ((end.isBefore(slotStart) && start.isAfter(slotEnd))) {
                intersectingSlots.add(slotName);
            }
        }

        if (intersectingSlots.isEmpty()) {
            String customSlot = String.format("%02d:%02d-%02d:%02d", start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
            return Collections.singletonList(customSlot);
        }

        return intersectingSlots;
    }
    public static class ReportRow {
        private final String dayOfWeek;
        private final Map<String, String> timeSlotAllocations = new HashMap<>();

        public ReportRow(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getDayOfWeek() { return dayOfWeek; }

        public void setTimeSlotAllocation(String timeSlot, String allocation) {
            timeSlotAllocations.put(timeSlot, allocation);
        }

        public String get08001000() { return timeSlotAllocations.getOrDefault("08:00-10:00", "Empty"); }
        public String get10001200() { return timeSlotAllocations.getOrDefault("10:00-12:00", "Empty"); }
        public String get14001600() { return timeSlotAllocations.getOrDefault("14:00-16:00", "Empty"); }
        public String get16001800() { return timeSlotAllocations.getOrDefault("16:00-18:00", "Empty"); }
        public String get19002200() { return timeSlotAllocations.getOrDefault("18:00-20:00", "Empty"); }

        public String getCustomTimeSlot(String timeSlot) {
            return timeSlotAllocations.getOrDefault(timeSlot, "Empty");
        }
    }

    public static class RoomUsageRow {
        private final String roomName;
        private final String occupancyRate;
        private final String mostUsedSlot;
        private final String totalAllocations;

        public RoomUsageRow(String roomName, String occupancyRate, String mostUsedSlot, String totalAllocations) {
            this.roomName = roomName;
            this.occupancyRate = occupancyRate;
            this.mostUsedSlot = mostUsedSlot;
            this.totalAllocations = totalAllocations;
        }

        public String getRoomName() { return roomName; }
        public String getOccupancyRate() { return occupancyRate; }
        public String getMostUsedSlot() { return mostUsedSlot; }
        public String getTotalAllocations() { return totalAllocations; }
    }

    public static class ClassroomDistributionRow {
        private final String distributionValue;
        private final String classroomCount;
        private final String allocationCount;

        public ClassroomDistributionRow(String distributionValue, String classroomCount, String allocationCount) {
            this.distributionValue = distributionValue;
            this.classroomCount = classroomCount;
            this.allocationCount = allocationCount;
        }

        public String getDistributionValue() { return distributionValue; }
        public String getClassroomCount() { return classroomCount; }
        public String getAllocationCount() { return allocationCount; }
    }

    @FXML
    private void handleWeeklyAllocationsExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(reportTable.getScene().getWindow());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                document.add(new Paragraph("Weekly Allocations Report").setBold().setFontSize(18));

                float[] columnWidths = new float[reportTable.getColumns().size()];
                Arrays.fill(columnWidths, 1);
                Table pdfTable = new Table(UnitValue.createPercentArray(columnWidths));
                pdfTable.setWidth(UnitValue.createPercentValue(100));


                for (TableColumn<ReportRow, ?> column : reportTable.getColumns()) {
                    pdfTable.addHeaderCell(column.getText());
                }

                for (ReportRow item : reportTable.getItems()) {
                    pdfTable.addCell(item.getDayOfWeek());
                    for (TableColumn<ReportRow, ?> column : reportTable.getColumns().subList(1, reportTable.getColumns().size())) {
                        String cellValue = (String) column.getCellObservableValue(item).getValue();
                        pdfTable.addCell(cellValue);
                    }
                }

                document.add(pdfTable);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported successfully to PDF.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export report to PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRoomUsageExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(roomUsageTable.getScene().getWindow());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                document.add(new Paragraph("Room Usage Report").setBold().setFontSize(18));

                Table pdfTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 3, 2}));
                pdfTable.setWidth(UnitValue.createPercentValue(100));

                pdfTable.addHeaderCell("Room");
                pdfTable.addHeaderCell("Occupancy Rate");
                pdfTable.addHeaderCell("Most Used Time Slot");
                pdfTable.addHeaderCell("Total Allocations");

                for (RoomUsageRow item : roomUsageTable.getItems()) {
                    pdfTable.addCell(item.getRoomName());
                    pdfTable.addCell(item.getOccupancyRate());
                    pdfTable.addCell(item.getMostUsedSlot());
                    pdfTable.addCell(item.getTotalAllocations());
                }

                document.add(pdfTable);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported successfully to PDF.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export report to PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClassroomDistributionExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(classroomDistributionTable.getScene().getWindow());

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                document.add(new Paragraph("Classroom Distribution Report").setBold().setFontSize(18));

                Table pdfTable = new Table(UnitValue.createPercentArray(new float[]{2, 3, 2}));
                pdfTable.setWidth(UnitValue.createPercentValue(100));


                pdfTable.addHeaderCell(distributionValueColumn.getText());
                pdfTable.addHeaderCell(classroomCountColumn.getText());
                pdfTable.addHeaderCell(allocationCountColumn.getText());

                for (ClassroomDistributionRow item : classroomDistributionTable.getItems()) {
                    pdfTable.addCell(item.getDistributionValue());
                    pdfTable.addCell(item.getClassroomCount());
                    pdfTable.addCell(item.getAllocationCount());
                }

                document.add(pdfTable);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported successfully to PDF.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export report to PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleWeeklyAllocationsExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(reportTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                List<String> headers = reportTable.getColumns().stream()
                        .map(TableColumn::getText)
                        .collect(Collectors.toList());
                writer.println(String.join(";", headers));

                for (ReportRow item : reportTable.getItems()) {
                    List<String> row = new ArrayList<>();
                    row.add(item.getDayOfWeek());
                    for (TableColumn<ReportRow, ?> column : reportTable.getColumns().subList(1, reportTable.getColumns().size())) {
                        String cellValue = (String) column.getCellObservableValue(item).getValue();
                        row.add("\"" + cellValue.replace("\"", "\"\"") + "\"");
                    }
                    writer.println(String.join(";", row));
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported successfully to CSV.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export report to CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleRoomUsageExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(roomUsageTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Room;Occupancy Rate;Most Used Time Slot;Total Allocations");

                for (RoomUsageRow item : roomUsageTable.getItems()) {
                    writer.printf("\"%s\";%s;\"%s\";%s%n",
                            item.getRoomName(),
                            item.getOccupancyRate(),
                            item.getMostUsedSlot(),
                            item.getTotalAllocations());
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported successfully to CSV.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export report to CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleClassroomDistributionExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(classroomDistributionTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                String header = String.format("%s;%s;%s",
                        distributionValueColumn.getText(),
                        classroomCountColumn.getText(),
                        allocationCountColumn.getText());
                writer.println(header);

                for (ClassroomDistributionRow item : classroomDistributionTable.getItems()) {
                    writer.printf("\"%s\";\"%s\";%s%n",
                            item.getDistributionValue(),
                            item.getClassroomCount(),
                            item.getAllocationCount());
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported successfully to CSV.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export report to CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}