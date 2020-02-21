package demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 模拟录入机票程序，分别使用多线程和单线程，
 * 但是，多线程执行时间似乎并没有比单线程执行时间快？
 * 三类机票完全设置完成的总时间是固定的，
 * 三个线程和单个线程相比，执行的总时间不单是没有减少，反而增加了线程上下文切换的时间
 *
 * 如果像生产者消费者这样的问题，用多线程实现的执行时间是比单线程实现的执行时间少得多的
 *
 * @projectName: concurrency
 * @package: demo
 * @className: AddTicket
 * @author: Qinyunchan
 * @date: 2020/2/21  6:34 下午
 * @version: 1.0
 */
public class AddTicket {

    class Ticket {

        private String company;

        private String startStation;

        private String endStation;

        private Integer fenPrice;

        private Date date;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getStartStation() {
            return startStation;
        }

        public void setStartStation(String startStation) {
            this.startStation = startStation;
        }

        public String getEndStation() {
            return endStation;
        }

        public void setEndStation(String endStation) {
            this.endStation = endStation;
        }

        public Integer getFenPrice() {
            return fenPrice;
        }

        public void setFenPrice(Integer fenPrice) {
            this.fenPrice = fenPrice;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Ticket{" +
                    "company='" + company + '\'' +
                    ", startStation='" + startStation + '\'' +
                    ", endStation='" + endStation + '\'' +
                    ", fenPrice=" + fenPrice +
                    ", date=" + date +
                    '}';
        }
    }

    private static CountDownLatch latch = new CountDownLatch(3);
    private static int ticketListLength = 100000;
    private static String[] companies = new String[]{"国航","海航","南航"};
    private static String[] stations = new String[]{"北京","上海","广州","深圳"};
    private static String[] dateStr = new String[]{"2020-01-01","2020-01-02","2020-01-03"};

    private static final List<Ticket> ghTicketList = new ArrayList<>();
    private static final List<Ticket> hhTicketList = new ArrayList<>();
    private static final List<Ticket> nhTicketList = new ArrayList<>();

    public List<Ticket> getGhTicketList() {
        return ghTicketList;
    }

    public List<Ticket> getHhTicketList() {
        return hhTicketList;
    }

    public List<Ticket> getNhTicketList() {
        return nhTicketList;
    }

    private static Ticket createTicket(){
        Ticket ticket = new AddTicket().new Ticket();

        Random companyRandom = new Random();
        int companyIndex = companyRandom.nextInt(companies.length);
        ticket.setCompany(companies[companyIndex]);

        Random stationRandom = new Random();
        int stationIndex = stationRandom.nextInt(stations.length);
        String startStation = stations[stationIndex];
        ticket.setStartStation(startStation);

        stationIndex = stationRandom.nextInt(stations.length);
        while (startStation.equals(stations[stationIndex])){
            stationIndex = stationRandom.nextInt(stations.length);
        }
        String endStation = stations[stationIndex];
        ticket.setEndStation(endStation);

        Random dateStrRandom = new Random();
        int dateStrIndex = dateStrRandom.nextInt(dateStr.length);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateStr[dateStrIndex]);
            ticket.setDate(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        Random priceRandom = new Random();
        int min = 300;
        int max = 1000;
        ticket.setFenPrice(priceRandom.nextInt(max) % (max-min+1) + min);
        return ticket;
    }

    /**
     * 三个线程分别设置机票数据
     */
    private static void setTicketListByThread(){
        new Thread("国航创建机票线程"){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"启动");
                while (true){
                    synchronized (ghTicketList){
//                        System.out.println(Thread.currentThread().getName()+"创建机票");
                        Ticket ticket = AddTicket.createTicket();
                        if(ghTicketList.size() == ticketListLength){
                            latch.countDown();
                            break;
                        }
                        if(companies[0].equals(ticket.getCompany()) && ghTicketList.size() < ticketListLength){
                            ghTicketList.add(ticket);
                        }
                    }
                }
            }
        }.start();

        new Thread("海航创建机票线程"){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"启动");
                while (true){
                    synchronized (hhTicketList){
//                        System.out.println(Thread.currentThread().getName()+"创建机票");
                        Ticket ticket = AddTicket.createTicket();
                        if(hhTicketList.size() == ticketListLength){
                            latch.countDown();
                            break;
                        }
                        if(companies[1].equals(ticket.getCompany()) && hhTicketList.size() < ticketListLength){
                            hhTicketList.add(ticket);
                        }
                    }
                }
            }
        }.start();

        new Thread("南航创建机票线程"){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"启动");
                while (true){
                    synchronized (nhTicketList){
//                        System.out.println(Thread.currentThread().getName()+"创建机票");
                        Ticket ticket = AddTicket.createTicket();
                        if(nhTicketList.size() == ticketListLength){
                            latch.countDown();
                            break;
                        }
                        if(companies[2].equals(ticket.getCompany()) && nhTicketList.size() < ticketListLength){
                            nhTicketList.add(ticket);
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 单线程的设置机票
     */
    private static void setTicketNoThread(){
        while (true){
            Ticket ticket = AddTicket.createTicket();
            if(companies[0].equals(ticket.getCompany()) && ghTicketList.size() < ticketListLength){
                ghTicketList.add(ticket);
            }
            if(companies[1].equals(ticket.getCompany()) && hhTicketList.size() < ticketListLength){
                hhTicketList.add(ticket);
            }
            if(companies[2].equals(ticket.getCompany()) && nhTicketList.size() < ticketListLength){
                nhTicketList.add(ticket);
            }
            if(ghTicketList.size() == ticketListLength
                    && hhTicketList.size() == ticketListLength
                    && nhTicketList.size() == ticketListLength){
                break;
            }
        }
    }

    public static void printTicketList(){
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < ticketListLength; i++){
            System.out.println("国航：" + ghTicketList.get(i));
            System.out.println("海航：" + hhTicketList.get(i));
            System.out.println("南航：" + nhTicketList.get(i));
        }
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        long startTime = System.currentTimeMillis();
        AddTicket.setTicketListByThread();
//        SearchTicket.setTicketNoThread();
        AddTicket.printTicketList();
        long endTime = System.currentTimeMillis();
        System.out.println(TimeUnit.MILLISECONDS.toMillis(endTime - startTime));
    }

}
