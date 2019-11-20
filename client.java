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
		FileOutputStream fr = new FileOutputStream(path.toString());
		byte[] array = new byte[12800];
		is.read(array);
		//is.readAllBytes();
		fr.write(array);
		//input.close();
	}
	
	public static void client_send(Socket sr, OutputStream os, InputStream is) throws Exception{
		Scanner input = new Scanner(System.in);
		// Ask user to input the file to be sent
		System.out.print("Please enter the directory of the target file in your desktop: ");
		Path path = Paths.get(input.next());
		while(Files.notExists(path)){
			System.out.print("Invalid path, please re-enter path: ");
			path = Paths.get(input.next());
		}
		FileInputStream fr = new FileInputStream(path.toString());
		byte file_content[] = new byte[12800];
		fr.read(file_content);
		// Ask user where in the server does he want to save the file
		System.out.print("Please enter the directory you want to save your file at: ");
		String target_path = input.next();
		os.write(target_path.getBytes());
		// check path validity
		byte[] status_byte = new byte[2];
		is.read(status_byte);
		String status = new String(status_byte);
		while (status.charAt(0) != 'y'){
			System.out.print("Invalid path, please re-enter path: ");
			target_path = input.next();
		}
		// send the data
		os.write(file_content);
		input.close();
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
		OutputStream os = sr.getOutputStream();
		InputStream is = sr.getInputStream();
		System.out.print("Do you want to send file to or retrieve file from the server (enter 's' to send and 'r' to retrieve): ");
		String user_opt = input.next();
		while ((user_opt.charAt(0) != 's') && (user_opt.charAt(0) != 'r')){
			System.out.print("Invalid input, please re-enter: ");
			user_opt = input.next();
		}
		os.write(user_opt.getBytes());
		if (user_opt.charAt(0) == 's'){
			client_send(sr, os, is);
		}
		else{
			client_receive(sr, os, is);
		}
		
	}
}