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
	// o Array clients guarda o BufferedWriter de cada cliente
	//conectado... O BufferedWriter é uma classe para escrita em arquivos de texto
    private static ArrayList<BufferedWriter>clients;           
    private static ServerSocket server; 
    private String nome;
    private Socket conexao;
    // Responsável pela leitura de bytes
    private InputStream input;  
    private InputStreamReader inputReader;  
    // BufferedReader faz a leitura do texto
    private BufferedReader bufferReader;
    
    
    //Método construtor da classe
    // O parâmetro é um socket
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
    

    // Método run, acionado quando um cliente conecta-se ao servidor
    // O cliente é alocado em uma thread
    // O método faz a verificação de mensagens novas, para enviar aos clientes
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
       enviarMSG(bufferWriter, mensagem);
       System.out.println(mensagem);                                              
       }                                   
   }catch (Exception e) {
     e.printStackTrace();
   }                       
}
  
  //servidor recebe e manda msg ara todos os clientes conectados, por meio de uma cópia da mensagem
	// para cada cliente
public void enviarMSG(BufferedWriter bwSaida, String mensagem) throws  IOException 
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

// Configuração e inicialização do servidor...
// Porta utilizada é a 10001
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
       Thread thread = new Server(conexao);
        thread.start();   
    }
                             
  }catch (Exception e) {
   
    e.printStackTrace();
  }                       
 }                     
}