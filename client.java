import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.lang.Object;
import java.nio.*;

public class client {
	public static void client_receive(Socket sr, OutputStream os, InputStream is) throws Exception{
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the directory of the target file in the server: ");
		String target_file = input.next();
		os.write(target_file.getBytes());
		byte[] status_byte = new byte[2];
		is.read(status_byte);
		String status = new String(status_byte);
		while (status.charAt(0) != 'y'){
			System.out.print("File does not exist in the server, please re-enter the directory of the target file: ");
			target_file = input.next();
			os.write(target_file.getBytes());
			is.read(status_byte);
			status = new String(status_byte);
		}		
		System.out.print("Please enter the directory to save incoming file: ");
		Path path = Paths.get(input.next());
		while(Files.notExists(path.getParent())){
			System.out.print("Path does not exist, please re-enter directory: ");
			path = Paths.get(input.next());
		}
		input.close();
		FileOutputStream fr = new FileOutputStream(path.toString());
		byte[] array = is.readAllBytes();
		fr.write(array);
	}
	
	public static void client_send(Socket sr) throws Exception{
		
		
	}
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the IP address of the server: ");
		String ip_addr = input.next();
		Socket sr = new Socket();
		try {
			sr = new Socket(ip_addr, 8080);
		} catch (Exception e) {
			System.out.println("Sorry, server is currently unavailable");
			System.exit(0);	
		}
		System.out.print("Do you want to send file to or retrieve file from the server (enter 's' to send and 'r' to retrieve): ");
		String user_opt = input.next();
		OutputStream os = sr.getOutputStream();
		InputStream is = sr.getInputStream();
		os.write(user_opt.getBytes());
		if (user_opt == "s"){
			
		}
		else{
			client_receive(sr, os, is);
		}
		
	}
}