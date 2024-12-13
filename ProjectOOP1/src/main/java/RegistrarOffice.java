package oop_project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistrarOffice extends Manager {
    private List<Request> requests;
    private Schedule schedule;
    private List<Course> courses;

    public RegistrarOffice(ManagerType managerType, List<Request> requests, Schedule schedule) {
        super(managerType);
        this.requests = requests;
        this.schedule = schedule;
        this.courses = new ArrayList<>();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequest(List<Request> requests) {
        this.requests = requests;
    }

    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            System.out.println("Course added: " + course.getCourseName());
        } else {
            System.out.println("Course already exists.");
        }
    }

    public void manageCourse(Course course) {
        if (courses.contains(course)) {
            System.out.println("Managing course: " + course.getCourseName());
        } else {
            System.out.println("Course not found.");
        }
    }

    public void removeCourse(Course course) {
        if (courses.remove(course)) {
            System.out.println("Course removed: " + course.getCourseName());
        } else {
            System.out.println("Course not found.");
        }
    }

    public void sendMessage(Manager manager) {
        System.out.println("Message sent to manager: " + manager.getManagerType());
    }

    public void editSchedule() {
        schedule.setTime(new Date());
        System.out.println("Schedule updated: " + schedule);
    }

    public void confirmSchedule() {
        System.out.println("Schedule confirmed: " + schedule);
    }

    @Override
    public String toString() {
        return "RegistrarOffice [requests=" + requests + ", schedule=" + schedule + "]";
    }
}
