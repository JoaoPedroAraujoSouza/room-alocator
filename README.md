# Room Allocation System for an Educational Institution

## Description

This system aims to manage the allocation of classrooms for groups in an educational institution. The goal is to avoid scheduling conflicts, ensure the availability of adequate infrastructure, and organize class schedules. The system should allow the registration of rooms, groups, teachers, courses, and schedules.

Each room has a capacity and available resources (such as a projector, digital whiteboard, etc.). Groups are associated with courses and teachers and must be allocated to specific time slots and available rooms. The system must avoid conflicts such as: two events in the same room at the same time or a teacher assigned to two groups simultaneously.

Reports on room usage, availability by time slot, and group distribution per room should be provided, with export options in PDF or CSV.

## Functional Requirements

### 1. Room Management

- **REQ01**: Allow room management, including name, location, maximum capacity, and available resources.
- **REQ02**: Allow marking rooms as unavailable during specific periods (e.g., maintenance).

### 2. Teacher and Course Management

- **REQ03**: Allow teacher management, including name, CPF (Tax ID), email, and the courses they teach.
- **REQ04**: Allow course management with name, weekly workload, and description.

### 3. Group Management

- **REQ05**: Allow group management, including a unique ID, semester, shift, and student list.
- **REQ06**: Each group must be linked to a course and a responsible teacher.

### 4. Schedule Definition

- **REQ07**: Allow registration of time blocks (e.g., 08h–10h, 10h–12h) and weekdays.
- **REQ08**: Ensure that a group is allocated only in available time slots compatible with the course workload.
- **REQ09**: Prevent a teacher or room from being allocated to more than one group at the same time.

### 5. Room Allocation

- **REQ10**: Allow the allocation of rooms to groups for specific time slots, considering minimum capacity and required resources.
- **REQ11**: Validate room availability before confirming the allocation.
- **REQ12**: Allow modification of a group's allocation, as long as there is no conflict with another group at the same time.

### 6. Queries and Checks

- **REQ13**: Allow checking the weekly schedule of each room, showing occupied and free time slots.
- **REQ14**: Allow checking the weekly schedule of each teacher and each group.
- **REQ15**: Automatically notify scheduling conflicts when attempting to save inconsistent allocations.

### 7. Reports and Statistics

- **REQ16**: Generate reports on room usage by period, including occupancy rates and most used time slots.
- **REQ17**: Generate reports on group distribution by shift, teacher, or course.
- **REQ18**: Allow exporting reports in **PDF** and **CSV**, with organized columns, groupings, and totals.

### 8. Rules and Constraints

- **REQ19**: The room assigned to a group must have a capacity equal to or greater than the number of students in the group.
- **REQ20**: A room marked as unavailable cannot be assigned to any group during the specified period.
- **REQ21**: Teachers cannot be assigned to more than one group in the same time block.

## Possible APIs/Libraries to Be Used

- **JavaFX** – System graphical interface.
- **JDBC / Hibernate** – Data persistence and relationship management.
- **iText / JasperReports** – Report export in PDF.
- **Apache POI** – Generation of CSV or Excel spreadsheets with schedules or room usage.
- **JUnit / Mockito** – Testing of allocation conflict logic.
- **Java Time API** – Handling dates, times, and intervals.

## Group Members with Full Names

- João Pedro de Araújo Souza - joao.pedroaraujosouza@ufrpe.br  
- Mario Jorge - mario.jorgeandrade@ufrpe.br  
- Luiz Maranhão - luiz.maranhao@ufrpe.br  
- Pedro Roma - pedro.roma@ufrpe.br
