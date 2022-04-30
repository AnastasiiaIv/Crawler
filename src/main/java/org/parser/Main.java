package org.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;




public class Main {
    private static String url = "http://4pda.to/";
    private static TaskController taskController;
    private static App app;


    static void SendMsg(String link) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        factory.setPort(5672);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare("QUEUE_LINKS", false, false, true, null);
            channel.basicPublish("", "QUEUE_LINKS", null, link.getBytes(StandardCharsets.UTF_8));
            channel.close();
            connection.close();
        } catch (Exception e) {
            return;
        }
    }

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

        public void produce(Document doc) throws InterruptedException {
            Elements links = doc.getElementsByAttributeValue("itemprop", "url");
            for(Element i : links){
             //   stringlink.add(i.attr("href"));
                SendMsg(i.attr("href"));
            }

        }


            public void consume() throws InterruptedException, IOException {
            {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    factory.setUsername("rabbitmq");
                    factory.setPassword("rabbitmq");
                    factory.setPort(5672);
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();

                    channel.queueDeclare("QUEUE_LINKS", false, false, true, null);
                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), "UTF-8");
                        try {
                            String childlink = message;
                            Document newDoc = taskController.GetUrl(childlink);
                            app.ParseNews(newDoc, childlink);
                        } catch (Exception e) {
                            System.out.println(" error downloading page " + e.toString());
                        } finally {
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        }

                    };
                    channel.basicConsume("QUEUE_LINKS", false, deliverCallback, consumerTag -> { });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }


            }
        }
    }


