package org.parser;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect

public class PrintNews {
    private String title;
    private String detailsLink;
    private String author;
    private String dateOfCreated;
    private String text;

    public void setText(String text) {
        this.text = text;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }
    public void setAuthor(String author) {
        this.author = author;
    }



    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }



PrintNews(){}
}
