import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;


class myThread extends Thread{
	private ServerSocket s;
	
	public myThread(ServerSocket s){
		this.s = s;
	}
	public void run(){
		while (true){
			try {
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
					byte []file_byte = new byte[1024];
					is.read(file_byte);
					String file_name = new String(file_byte);
					file_name = file_name.replace("\0", "");
					Path path = Paths.get(file_name);
					String status = new String();
					while(Files.notExists(path.getParent())){
						status = "n";
						os.write(status.getBytes());
						file_byte = new byte[1024];
						is.read(file_byte);
						file_name = new String(file_byte);
						file_name = file_name.replace("\0", "");
						path = Paths.get(file_name);
					}
					status = "y";
					os.write(status.getBytes());
					FileOutputStream fr = new FileOutputStream(path.toString());
					byte[] array = is.readAllBytes();
					fr.write(array);
				}
			}
			catch (Exception e) {
				continue;
			}
		}	
	}
}

public class server {
	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(8080);
		for (int i = 0; i < 10; i++){
			new myThread(s).start();	
		}
	}
}