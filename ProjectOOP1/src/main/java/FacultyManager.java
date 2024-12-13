package oop_project;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class FacultyManager extends Manager {
    private String facultyName;
    private List<String> majors;
    private List<Student> students;
    private List<News> newsList;

    public FacultyManager(String facultyName, List<String> majors, List<Student> students) {
        super(ManagerType.DEPARTMENT);
        this.facultyName = facultyName;
        this.majors = majors;
        this.students = students;
        this.newsList = new ArrayList<>();
    }

    public void createNews(String title, String content, Languages language) {
        News news = new News(title, content, new Date(), language);
        newsList.add(news);
        System.out.println("News created: " + news.getTitle() + " in " + language + " language");
    }

    public void editNews() {
        if (!newsList.isEmpty()) {
            News news = newsList.get(0);
            news.setTitle("Updated Faculty Event");
            System.out.println("News updated: " + news.getTitle());
        } else {
            System.out.println("No news to edit.");
        }
    }

    public void deleteNews() {
        if (!newsList.isEmpty()) {
            News news = newsList.remove(0);
            System.out.println("News deleted: " + news.getTitle());
        } else {
            System.out.println("No news to delete.");
        }
    }

    public String getFaculty() {
        return facultyName;
    }

    public void setFaculty(String facultyName) {
        this.facultyName = facultyName;
    }

    public List<Student> getFacultyStudents() {
        return students;
    }

    public List<String> getMajors() {
        return majors;
    }

    public void setMajors(List<String> majors) {
        this.majors = majors;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public void showNews() {
        for (News news : newsList) {
            System.out.println(news);
        }
    }

    @Override
    public String toString() {
        return "FacultyManager [facultyName=" + facultyName + ", majors=" + majors + "]";
    }
}