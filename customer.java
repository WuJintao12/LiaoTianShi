import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class customer {
    public static void main(String[] args) throws Exception {

//        ��ȡIP��ַ�Ͷ˿ں���
        System.out.println("�����÷�������IP��ַ��");
        String Ip = new String(new Scanner(System.in).next());
        System.out.println("������������˵Ķ˿ں��룺");
        int port = new Scanner(System.in).nextInt();

//        ���ӷ�����
        InetAddress inet = InetAddress.getByName(Ip);
        Socket socket = null;
        try {
            socket = new Socket(inet, port);
        } catch (IOException e) {
            System.out.println("�޷�ͨ�����óɹ����ӣ�����IP��ַ�Ͷ˿ں��Ƿ���ȷ������ִ�н���");
            return;
        }


//        ���ӳɹ���ִ��
        System.out.println("���ӳɹ�");
//        ���������߳�
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Receive receive = new Receive(bufferedReader);
        receive.start();
//        ���÷����߳�
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        Send send = new Send(printStream);
        send.start();


    }

    //�����߳�
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
//            ѭ�����������ж������ݲ���ӡ
//            System.out.println("�����߳�run����������");
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

    //    ��Ϣ�����߳�
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
//            System.out.println("�����߳�run������");
            while (true) {
//                System.out.println("�Լ��Ļ�");
                message = new Scanner(System.in).next();
                printStream.println(message);
                if(message.equals("byb")){
                    System.out.println("��ӭʹ��");
                    System.exit(0);
                }
            }
        }


    }

}
