import java.io.*;
import java.net.*;
public class server {
   public static void main(String args[]){
	   try{
		   ServerSocket serv = null;
		   try{
			   serv = new ServerSocket(4700);
		   }catch(Exception e){
			   System.out.println("can not listen to:"+e);
		   }
		   Socket socket = null;
		   try{
			   socket = serv.accept();
		   }catch(Exception e){
			   System.out.println("Error:"+e);
		   }
		   String line;
		   BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		   PrintWriter os = new PrintWriter(socket.getOutputStream());
		   BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
		   System.out.println("Client:"+is.readLine());
		   line = sin.readLine();
		   while(!line.equals("bye")){
			   os.println(line);
			   os.flush();
			   System.out.println("Server:"+line);
			   System.out.println("Client:"+is.readLine());
			   line = sin.readLine();
		   }
		   os.close();
		   is.close();
		   socket.close();
		   serv.close();
	   }catch(Exception e){
		   System.out.println("Error:"+e);
	   }
   }
}
