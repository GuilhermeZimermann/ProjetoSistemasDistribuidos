import java.net.*;
import java.io.*;
public class Cliente{
	public static void main (String args[]){
	// arguments supply message and hostname of destination
		try {
			int serverPort = 7896;
			Socket s = new Socket(args[1], serverPort);
			DataInputStream in = new DataInputStream(s.getInputStream()); 
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(args[0]);      // UTF is a String encoding
			String data = in.readUTF();
			System.out.println("Voltou: "+ data); 
			s.close();
		}catch (UnknownHostException e)
			{System.out.println("Sock:"+ e.getMessage());
		}catch (EOFException e){System.out.println("EOF:"+ e.getMessage());
		}catch (IOException e) {System.out.println("IO:"+ e.getMessage());
		}
	}
}
