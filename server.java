import java.net.*;
import java.io.*;
import java.nio.charset.*;

public class server {
	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(8080);
		Socket sr = s.accept();
		InputStream is = sr.getInputStream();
		OutputStream os = sr.getOutputStream();
		// check if user want to send or retrieve
		byte []user_opt_byte = new byte[2];
		is.read(user_opt_byte);
		String user_opt = new String(user_opt_byte, StandardCharsets.UTF_8);	
		// Send data to client
		if (user_opt.charAt(0) == 'r'){
			byte []file_byte = new byte[12800];
			is.read(file_byte);
			String file_name = new String(file_byte);
			file_name = file_name.replace("\0", "");
			File file = new File(file_name);
			String status = new String();
			while(file.exists() == false){
				status = "n";
				os.write(status.getBytes());
				file_byte = new byte[12800];
				is.read(file_byte);
				file_name = new String(file_byte);
				file_name = file_name.replace("\0", "");
				file = new File(file_name);
			}
			status = "y";
			os.write(status.getBytes());
			FileInputStream fr = new FileInputStream(file);
			byte b[] = new byte[2002];
			fr.read(b, 0, b.length);
			os.write(b, 0, b.length);	
		}
		else{
			
		}		
	}
}