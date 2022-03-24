package org.parser;

public class PrintNews {
    private String title;
    private String detailsLink;
    private String author;
    private String dateOfCreated;
    private String text;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetailsLink() {
        return detailsLink;
    }
    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }


    public String getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }


    public void toPrint() {
        System.out.println("title: " + title + "," + System.lineSeparator() +
                "detailslinks: " + detailsLink + ","+ System.lineSeparator()  +
                "author: " + author + ","+ System.lineSeparator()  +
                "dateOfCreated: " + dateOfCreated + ","+ System.lineSeparator()  +
                "text: " + text + "," + System.lineSeparator());
    }

}
