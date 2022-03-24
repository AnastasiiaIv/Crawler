package org.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class App {

    public static void ParseNews(Document doc,String link) {

        PrintNews printNews = new PrintNews();

        printNews.setDetailsLink(link);
        printNews.setTitle(doc.title());

            try {
               Elements text = doc.select("div.content-box > p");
               Elements text_2 = doc.select("div.article > p");
               printNews.setText(text.text()+ text_2.text());
                Element authorElement = doc.getElementsByClass("name").first().child(0);
                printNews.setAuthor(authorElement.text());

            } catch (NullPointerException e) {
                Element authorElement2 = doc.getElementsByClass("article-footer-item").last();
                if (authorElement2 != null) {
                    printNews.setAuthor(authorElement2.text());
                } else {
                    printNews.setAuthor("Автор не определился");
                }
            }

            try {
                Element dateElement = doc.getElementsByClass("date").first();
                printNews.setDateOfCreated(dateElement.text());

            } catch (NullPointerException e) {
                Element dateElement2 = doc.getElementsByClass("article-meta-time").first();
                if (dateElement2 != null) {
                    printNews.setAuthor(dateElement2.text());
                } else {
                    printNews.setDateOfCreated("Дата создания не определена");
                }
                printNews.setText("Текст публикации не определился");
            }
            printNews.toPrint();

        }
    }
