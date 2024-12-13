package oop_project;

import java.util.Date;

public class News {
    private String title;
    private String content;
    private Date postDate;
    private Languages language;

    public News(String title, String content, Date postDate, Languages language) {
        this.title = title;
        this.content = content;
        this.postDate = postDate;
        this.language = language;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Languages getLanguage() {
        return language;
    }

    public void setLanguage(Languages language) {
        this.language = language;
    }

    public void switchLanguage() {
        switch (language) {
            case KZ:
                language = Languages.RU;
                break;
            case RU:
                language = Languages.EN;
                break;
            case EN:
                language = Languages.KZ;
                break;
        }
        System.out.println("Language switched to: " + language);
    }

    @Override
    public String toString() {
        return "News [title=" + title + ", content=" + content + ", postDate=" + postDate + ", language=" + language + "]";
    }
}
