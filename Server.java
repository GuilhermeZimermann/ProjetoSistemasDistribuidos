import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author GuilhermeZ
 */
// Declaração da classe, extendendo de uma Thread 
public class Server extends Thread{

	// Declaração das variáveis da classe
	// o Array clients 
    private static ArrayList<BufferedWriter>clients;           
    private static ServerSocket server; 
    private String nome;
    private Socket conexao;
    private InputStream input;  
    private InputStreamReader inputReader;  
    private BufferedReader bufferReader;
    
    
    public Server(Socket conexao){
        this.conexao = conexao;
         try {
            input  = conexao.getInputStream();
            inputReader = new InputStreamReader(input);
            bufferReader = new BufferedReader(inputReader);
        } catch (IOException e) {
          e.printStackTrace();
        }                          
    }
    
  public void run(){
                      
  try{
                                     
    String mensagem;
    OutputStream ou =  this.conexao.getOutputStream();
    Writer write = new OutputStreamWriter(ou);
    BufferedWriter bufferWriter = new BufferedWriter(write); 
    clients.add(bufferWriter);
    nome = mensagem = bufferReader.readLine();
              
    while(!"Sair".equalsIgnoreCase(mensagem) && mensagem != null)
      {           
       mensagem = bufferReader.readLine();
       sendToAll(bufferWriter, mensagem);
       System.out.println(mensagem);                                              
       }                                   
   }catch (Exception e) {
     e.printStackTrace();
   }                       
}
  
  //servidor recebe e manda msg ara todos os clientes conectados
public void sendToAll(BufferedWriter bwSaida, String mensagem) throws  IOException 
{
  BufferedWriter bwS;
   
  for(BufferedWriter bw : clients){
   bwS = (BufferedWriter)bw;
   if(!(bwSaida == bwS)){
     bw.write(nome + " -> " + mensagem+"\r\n");
     bw.flush(); 
   }
  }          
}

public static void main(String []args) {
   
  try{
    
      JLabel lblMessage = new JLabel("Servidor/Porta:");
      JTextField txtPorta = new JTextField("10001");
    Object[] texts = {lblMessage, txtPorta };  
      JOptionPane.showMessageDialog(null, texts);
    server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
    clients = new ArrayList<BufferedWriter>();
    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+         
    txtPorta.getText());
   
     while(true){
       System.out.println("Aguardando conexão...");
       Socket conexao = server.accept();
       System.out.println("Cliente conectado...");
       Thread t = new Server(conexao);
        t.start();   
    }
                             
  }catch (Exception e) {
   
    e.printStackTrace();
  }                       
 }                     
}