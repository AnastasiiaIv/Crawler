package org.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;



public class Main {
    private static String url = "http://4pda.to/";
    private static TaskController taskController;
    private static App app;


    public static void main(String[] args) throws Exception {

        taskController = new TaskController();
        Document doc = taskController.GetUrl(url);


        ProducerConsumer PC = new ProducerConsumer();
        Thread tMain = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PC.produce(doc);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread tConsume1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PC.consume();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread tConsume2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PC.consume();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        tMain.start();
        tConsume1.start();
        tConsume2.start();

        tMain.join();
        tConsume1.join();
        tConsume2.join();

    }

    public static class ProducerConsumer {
        int capacity = 5;
        public static LinkedList<String> linkedlist = new LinkedList<>();
        public static LinkedList<String> stringlink = new LinkedList<>();
        // Function called by producer thread
        public void produce(Document doc) throws InterruptedException {
            Elements links = doc.getElementsByAttributeValue("itemprop", "url");
            for(Element i : links){
                stringlink.add(i.attr("href"));
            }
            int index = 0;
            System.out.println("Работа " + Thread.currentThread().getName());
            while (true) {
                synchronized (this) {
                    while (linkedlist.size() == capacity)
                        wait();
                    linkedlist.add(stringlink.get(index));
                    index++;
                    notify();
                    Thread.sleep(1000);
                }
            }
        }

            public void consume() throws InterruptedException, IOException {
            {
                while (true) {
                    synchronized (this) {
                        // consumer thread waits while list
                        // is empty
                        while (linkedlist.size() == 0)
                            wait();
                        String childLink = linkedlist.removeFirst();
                        //String convertLink = childLink.toString();
                        Document newDoc = taskController.GetUrl(childLink);
                        System.out.println("Работа " + Thread.currentThread().getName());
                        app.ParseNews(newDoc, childLink);
                        notify();

                        // and sleep
                        Thread.sleep(1000);
                    }
                }
            }
        }
    }
}

