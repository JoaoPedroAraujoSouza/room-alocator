package org.example;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.example.models.Classroom;
import org.example.models.Room;
import org.example.models.Shift;
import org.example.models.Subject;
import org.example.models.Teacher;
import org.example.models.TimeAllocation;
import org.example.models.TimeBlock;
import org.example.models.UnavailabityPeriod;
import org.example.service.ClassroomService;
import org.example.service.RoomService;
import org.example.service.SubjectService;
import org.example.service.TeacherService;
import org.example.service.TimeAllocationService;
import org.example.service.TimeBlockService;
import org.example.service.UnavailabityPeriodService;

//Esse código é apenas um teste para verificar se as classes estão funcionando corretamente, sua branch será deletada após a entrega do dia 19
public class Main {
    public static void main(String[] args) {

        //room test
        RoomService roomService = new RoomService();
        try {
            //create
            Room room = new Room(
                    1L, // id
                    UUID.randomUUID(), //uuid
                    "Room 101", //name
                    "Building A", //localization
                    30, // capacity
                    new ArrayList<>(), //resources
                    new ArrayList<>(), //unavailabilityPeriods
                    new ArrayList<>()  //timeAllocations
            );
            roomService.add(room);
            System.out.println("Created: " + room);

            //read
            Optional<Room> fetched = roomService.getById(1L);
            fetched.ifPresent(r -> System.out.println("Fetched: " + r));

            //update
            if (fetched.isPresent()) {
                Room toUpdate = fetched.get();
                toUpdate.setCapacity(40);
                roomService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }

            //delete
            roomService.deleteById(1L);
            System.out.println("Deleted room with id 1");

        } catch (IOException e) {
            e.printStackTrace();
        }


        //classroom test
        ClassroomService classroomService = new ClassroomService();

        try {
            //create
            Classroom classroom = new Classroom(1L, UUID.randomUUID(), "2025-1", Shift.MORNING, 30, null, new ArrayList<>());
            classroomService.add(classroom);
            System.out.println("Created: " + classroom);
            //read
            Optional<Classroom> fetched = classroomService.getById(1L);
            fetched.ifPresent(c -> System.out.println("Fetched: " + c));
            //update
            if (fetched.isPresent()) {
                Classroom toUpdate = fetched.get();
                toUpdate.setMaxStudentsCapacity(35);
                classroomService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }
            //delete
            classroomService.deleteById(1L);
            System.out.println("Deleted classroom with id 1");
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();

        }

        //subject test
        SubjectService subjectService = new SubjectService();
        try {
            //create
            Subject subject = new Subject(
                    1L, UUID.randomUUID(), "Math", "Mathematics", 60
            );
            subjectService.add(subject);
            System.out.println("Created: " + subject);
            //read
            Optional<Subject> fetched = subjectService.getById(1L);
            fetched.ifPresent(s -> System.out.println("Fetched: " + s));
            //update
            if (fetched.isPresent()) {
                Subject toUpdate = fetched.get();
                toUpdate.setHourlyLoad(80);
                subjectService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }
            //delete
            subjectService.deleteById(1L);
            System.out.println("Deleted subject with id 1");
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //teacher test
        TeacherService teacherService = new TeacherService();

        try {
            //create
            Teacher teacher = new Teacher(
                    1L, UUID.randomUUID(), "Joao", "12345678900", "Joao@pessoa.com"
            );
            teacherService.add(teacher);
            System.out.println("Created: " + teacher);
            //read
            Optional<Teacher> fetched = teacherService.getById(1L);
            fetched.ifPresent(t -> System.out.println("Fetched: " + t));
            //update
            if (fetched.isPresent()) {
                Teacher toUpdate = fetched.get();
                toUpdate.setEmail("Joao.aquele@pessoa.com");
                teacherService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }
            //delete
            teacherService.deleteById(1L);
            System.out.println("Deleted teacher with id 1");
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //time block test
        TimeBlockService timeBlockService = new TimeBlockService();

        try {
            //create
            TimeBlock block = new TimeBlock(
                    1L, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)
            );
            timeBlockService.add(block);
            System.out.println("Created: " + block);
            //read
            Optional<TimeBlock> fetched = timeBlockService.getById(1L);
            fetched.ifPresent(b -> System.out.println("Fetched: " + b));
            //update
            if (fetched.isPresent()) {
                TimeBlock toUpdate = fetched.get();
                toUpdate.setEndTime(LocalTime.of(11, 0));
                timeBlockService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }
            //delete
            timeBlockService.deleteById(1L);
            System.out.println("Deleted time block with id 1");
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //time allocation
        TimeAllocationService timeAllocationService = new TimeAllocationService();

        try {
            //create
            TimeAllocation allocation = new TimeAllocation(
                    1L, UUID.randomUUID(), null, null, null
            );
            timeAllocationService.add(allocation);
            System.out.println("Created: " + allocation);
            //read
            Optional<TimeAllocation> fetched = timeAllocationService.getById(1L);
            fetched.ifPresent(a -> System.out.println("Fetched: " + a));
            //update
            if (fetched.isPresent()) {
                TimeAllocation toUpdate = fetched.get();
                // Example: set a new UUID
                toUpdate.setUuid(UUID.randomUUID());
                timeAllocationService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }
            //delete
            timeAllocationService.deleteById(1L);
            System.out.println("Deleted time allocation with id 1");
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //unavailabity period
        UnavailabityPeriodService unavailabilityService = new UnavailabityPeriodService();

        try {
            //create
            UnavailabityPeriod period = new UnavailabityPeriod(
                    1L, UUID.randomUUID(), LocalTime.of(8, 0), LocalTime.of(10, 0), "Maintenance"
            );
            unavailabilityService.add(period);
            System.out.println("Created: " + period);
            //read
            Optional<UnavailabityPeriod> fetched = unavailabilityService.getById(1L);
            fetched.ifPresent(p -> System.out.println("Fetched: " + p));
            //update
            if (fetched.isPresent()) {
                UnavailabityPeriod toUpdate = fetched.get();
                toUpdate.setReason("Extended Maintenance");
                unavailabilityService.update(toUpdate);
                System.out.println("Updated: " + toUpdate);
            }
            //delete
            unavailabilityService.deleteById(1L);
            System.out.println("Deleted unavailability period with id 1");
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}