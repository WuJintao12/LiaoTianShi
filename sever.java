import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class sever {
    public static void main(String[] args) throws Exception {
//        端口配置
        System.out.println("输入配置端口");
        int port = new Scanner(System.in).nextInt();
//        集合
        Set<Socket> socketList = new HashSet<Socket>();
//        创建服务器
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器创建成功");
        while (true) {
//            等待连接
            Socket socket = serverSocket.accept();//阻塞式
//            通知除了自己以外的所有用户自己已经上线
            new Send(socketList,"[官方]：上线了",socket).run();
            socketList.add(socket);//注册列表
            System.out.println(socket.getInetAddress()+"[官方]：上线了");
//            新建接收线程负责接收消息
            Receive receive = new Receive(socket, socketList);
            receive.start();
        }
    }

    public static class Receive extends Thread {

        private Socket socket;
        private Set<Socket> socketList;
        private String message;
        private BufferedReader bufferedReader;

        private Receive() {

        }

        public Receive(Socket socket, Set<Socket> socketList) throws Exception {
            this.socket = socket;
            this.socketList = socketList;
//            获得当前套字的输入流
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.message = new String();

        }

        @Override
        public void run() {
            while (true) {
                try {
//                    等待读取
                    if ((message = bufferedReader.readLine()) != null) {
//                        判断是否下线
                        if (message.equals("byb")) {
                            socketList.remove(socket);//删除列表
                            System.out.println(socket.getInetAddress() + "[官方]:下线");
                            message = "[官方]:下线";
//                            通知所有用户下线
                            Send send = new Send(socketList, message,socket);
                            send.start();
//                            结束当前线程
                            break;
                        } else {
//                            message = socket.getInetAddress() + "用户:" + message;
                            //                        调用发送线程 广播式发送
                            Send send = new Send(socketList, message,socket);
                            send.start();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    广播式发送线程
//    从列表里面遍历发送到所有客户端
    public static class Send extends Thread {
        private Set<Socket> socketList;//客户端队列
        private String message;//需要发送的消息
        private Socket socket;

        private Send() {

        }

        public Send(Set<Socket> socketList, String message,Socket socket) {
            this.socketList = socketList;
            this.message = message;
            this.socket=socket;
        }

        @Override
        public void run() {
//            遍历打印
            for (Socket socket1 : socketList) {
                try {
                    PrintStream printStream = new PrintStream(socket1.getOutputStream());
                    if(socket==socket1){
                        printStream.println("MY:"+message);
                    }
                    else{
                        printStream.println(socket.getInetAddress() + "用户:" + message);//打印消息
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
