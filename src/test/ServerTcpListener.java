import java.net.*;
import java.io.*;
public class ServerTcpListener implements Runnable {
 
    public static void main(String[] args) {
 
        try {
            @SuppressWarnings("resource")
			final ServerSocket server = new ServerSocket(11212);
            Thread th = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            System.out.println("开始监听...");
                            Socket socket = server.accept();
                            System.out.println("有链接");
                            receiveFile(socket);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
 
            });
 
            th.run(); //启动线程运行
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public void run() {
    }
 
    public static void receiveFile(Socket socket) {
 
        byte[] inputByte = null;
        int length = 0;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        try {
            try {
 
                dis = new DataInputStream(socket.getInputStream());
                fos = new FileOutputStream(new File("E:\\aa.xml"));
                inputByte = new byte[1024*4];
                System.out.println("开始接收数据...");
                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                    fos.write(inputByte, 0, length);
                    fos.flush();
                }
                System.out.println("完成接收");
            } finally {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                if (socket != null)
                    socket.close();
            }
        } catch (Exception e) {
 
        }
    }
}
