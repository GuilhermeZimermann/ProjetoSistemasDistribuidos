import java.net.*;
import java.io.*;
public class Servidor{
	public static void main (String args[]){
		try {
			int serverPort = 7896;
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("Esperando Mensagem");
			while (true){ 
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		}catch (IOException e) {
			System.out.println("Listen:"+ e.getMessage());
		}
	}
}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
	public Connection(Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream()); 
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:"+ e.getMessage());
		} 
	}
	public void run() {
	// continuacao
		try {
			String data = in.readUTF();
			out.writeUTF(data);
			clientSocket.close();
			System.out.println("Mensagem recebida: "+ data);
		}catch (EOFException e){System.out.println("EOF:"+ e.getMessage());
		}catch (IOException e) {System.out.println("IO:"+ e.getMessage());
		}
	}
}
