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
// Declara��o da classe, extendendo de uma Thread 
public class Server extends Thread{

	// Declara��o das vari�veis da classe
	// o Array clients guarda o BufferedWriter de cada cliente
	//conectado... O BufferedWriter � uma classe para escrita em arquivos de texto
    private static ArrayList<BufferedWriter>clients;           
    private static ServerSocket server; 
    private String nome;
    private Socket conexao;
    // Respons�vel pela leitura de bytes
    private InputStream input;  
    private InputStreamReader inputReader;  
    // BufferedReader faz a leitura do texto
    private BufferedReader bufferReader;
    
    
    //M�todo construtor da classe
    // O par�metro � um socket
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
    

    // M�todo run, acionado quando um cliente conecta-se ao servidor
    // O cliente � alocado em uma thread
    // O m�todo faz a verifica��o de mensagens novas, para enviar aos clientes
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
  
  //servidor recebe e manda msg ara todos os clientes conectados, por meio de uma c�pia da mensagem
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

// Configura��o e inicializa��o do servidor...
// Porta utilizada � a 10001
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
       System.out.println("Aguardando conex�o...");
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