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
//        �˿�����
        System.out.println("�������ö˿�");
        int port = new Scanner(System.in).nextInt();
//        ����
        Set<Socket> socketList = new HashSet<Socket>();
//        ����������
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("�����������ɹ�");
        while (true) {
//            �ȴ�����
            Socket socket = serverSocket.accept();//����ʽ
//            ֪ͨ�����Լ�����������û��Լ��Ѿ�����
            new Send(socketList,"[�ٷ�]��������",socket).run();
            socketList.add(socket);//ע���б�
            System.out.println(socket.getInetAddress()+"[�ٷ�]��������");
//            �½������̸߳��������Ϣ
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
//            ��õ�ǰ���ֵ�������
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.message = new String();

        }

        @Override
        public void run() {
            while (true) {
                try {
//                    �ȴ���ȡ
                    if ((message = bufferedReader.readLine()) != null) {
//                        �ж��Ƿ�����
                        if (message.equals("byb")) {
                            socketList.remove(socket);//ɾ���б�
                            System.out.println(socket.getInetAddress() + "[�ٷ�]:����");
                            message = "[�ٷ�]:����";
//                            ֪ͨ�����û�����
                            Send send = new Send(socketList, message,socket);
                            send.start();
//                            ������ǰ�߳�
                            break;
                        } else {
//                            message = socket.getInetAddress() + "�û�:" + message;
                            //                        ���÷����߳� �㲥ʽ����
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

    //    �㲥ʽ�����߳�
//    ���б�����������͵����пͻ���
    public static class Send extends Thread {
        private Set<Socket> socketList;//�ͻ��˶���
        private String message;//��Ҫ���͵���Ϣ
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
//            ������ӡ
            for (Socket socket1 : socketList) {
                try {
                    PrintStream printStream = new PrintStream(socket1.getOutputStream());
                    if(socket==socket1){
                        printStream.println("MY:"+message);
                    }
                    else{
                        printStream.println(socket.getInetAddress() + "�û�:" + message);//��ӡ��Ϣ
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
