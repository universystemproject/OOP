package oop_project;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Employee {
    private ManagerType managerType;
    private List<Request> requests;
    private List<News> newsList;
    private List<Teacher> teachers;
    private List<Student> students;

    public Manager(ManagerType managerType) {
        this.managerType = managerType;
        this.requests = new ArrayList<>();
        this.newsList = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public ManagerType getManagerType() {
        return managerType;
    }

    public void setManagerType(ManagerType managerType) {
        this.managerType = managerType;
    }

    public boolean approveRegistration() {
        if (!requests.isEmpty()) {
            requests.clear();
            return true;
        }
        return false;
    }

    public void assignCourse(Teacher teacher) {
        if (teachers.contains(teacher)) {
            System.out.println("Assigned a new course to " + teacher.getName());
        } else {
            System.out.println("Teacher not found.");
        }
    }

    public void createReport(Report report) {
        System.out.println("Report titled \"" + report.getTitle() + "\" created successfully.");
    }

    public void manageNews(News news) {
        newsList.add(news);
        System.out.println("News titled \"" + news.getTitle() + "\" managed successfully.");
    }

    public void viewStudents() {
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void viewTeachers() {
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }

    public void viewRequests() {
        for (Request request : requests) {
            System.out.println(request);
        }
    }

    @Override
    public String toString() {
        return "Manager [managerType=" + managerType + "]";
    }
}