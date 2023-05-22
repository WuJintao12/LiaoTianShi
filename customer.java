import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class customer {
    public static void main(String[] args) throws Exception {

//        获取IP地址和端口号码
        System.out.println("请配置服务器端IP地址：");
        String Ip = new String(new Scanner(System.in).next());
        System.out.println("请输入服务器端的端口号码：");
        int port = new Scanner(System.in).nextInt();

//        连接服务器
        InetAddress inet = InetAddress.getByName(Ip);
        Socket socket = null;
        try {
            socket = new Socket(inet, port);
        } catch (IOException e) {
            System.out.println("无法通过配置成功连接，请检查IP地址和端口号是否正确，程序执行结束");
            return;
        }


//        连接成功后执行
        System.out.println("连接成功");
//        启动接收线程
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Receive receive = new Receive(bufferedReader);
        receive.start();
//        启用发送线程
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        Send send = new Send(printStream);
        send.start();


    }

    //接收线程
    public static class Receive extends Thread {
        private BufferedReader bufferedReader;
        String message;

        private Receive() {

        }

        public Receive(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
        }


        @Override
        public void run() {
//            循环重输入流中读入数据并打印
//            System.out.println("接收线程run方法被调用");
            while (true) {
                try {
                    if ((message = bufferedReader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    消息发送线程
    public static class Send extends Thread {
        private String message;
        private PrintStream printStream;

        private Send(){

        }
        public Send(PrintStream printStream) {
            this.printStream = printStream;
        }

        @Override
        public void run() {
//            System.out.println("发送线程run被调用");
            while (true) {
//                System.out.println("自己的话");
                message = new Scanner(System.in).next();
                printStream.println(message);
                if(message.equals("byb")){
                    System.out.println("欢迎使用");
                    System.exit(0);
                }
            }
        }


    }

}
