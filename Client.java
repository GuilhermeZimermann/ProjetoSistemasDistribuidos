
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author GuilhermeZ
 */
public class Client extends JFrame implements ActionListener, KeyListener{
    
    // Declaração de variáveis e atributos da interface gráfica
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txMsg;
    private JButton btSend;
    private JButton btSair;    
    private JLabel lbTitulo;
    private JLabel lbMsg;
    private JPanel painel;
    private JLabel porta;
    private JLabel ip;
    private JLabel nick;
    private Socket socket;
    private OutputStream ou ;
    private Writer write; 
    private BufferedWriter bufferWriter;
    private JTextField txIP;
    private JTextField txPorta;
    private JTextField txNome;  
   
    
    
    
    // Método construtor
    // Definido o ip - no caso, localhost - e a porta do server - 10001
    // O usuário informa o seu nome
    public Client() throws IOException{                  
    JLabel lblMessage = new JLabel("Dados de conexao");
    JLabel ip = new JLabel("IP:");
    txIP = new JTextField("127.0.0.1");
    JLabel porta = new JLabel("Porta:");
    txPorta = new JTextField("10001");
    JLabel nickname = new JLabel("Nome: ");
    txNome = new JTextField();                
    Object[] texts = {lblMessage,ip, txIP, porta, txPorta, nickname, txNome };  
        JOptionPane.showMessageDialog(null, texts);             
     painel = new JPanel();
     texto = new JTextArea(10,40);
     texto.setEditable(false);
     txMsg = new JTextField(40);
     lbTitulo = new JLabel("Messenger");
     lbMsg = new JLabel("Mensagem");
     btSend = new JButton("Envia");
     btSend.setToolTipText("Enviar Mensagem");
     btSair = new JButton("Sair");
     btSair.setToolTipText("Sair do Chat");
     btSend.addActionListener(this);
     btSair.addActionListener(this);
     btSend.addKeyListener(this);
     txMsg.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(texto);
     texto.setLineWrap(true);  
     painel.add(lbTitulo);
     painel.add(scroll);
     painel.add(lbMsg);
     painel.add(txMsg);
     painel.add(btSair);
     painel.add(btSend);
                                                       
     setTitle(txNome.getText());
     setContentPane(painel);
     setLocationRelativeTo(null);
     setResizable(false);
     setSize(500,300);
     setVisible(true);
     setDefaultCloseOperation(EXIT_ON_CLOSE);
}
    
    // Método que conecta o usuário ao servidor

  public void conectar() throws IOException{
                          
  socket = new Socket(txIP.getText(),Integer.parseInt(txPorta.getText()));
  ou = socket.getOutputStream();
  write = new OutputStreamWriter(ou);
  bufferWriter = new BufferedWriter(write);
  bufferWriter.write(txNome.getText()+"\r\n");
  bufferWriter.flush();
}
  
  // Método que envia mensagens
   public void sendMSG (String msg) throws IOException{
    // Aqui faz a verificação do tamanho da mensagem.
    // Caso seja maior que 140 caracteres    
    // o sistema emite um alerta, através de uma janela de diálogo 
    // e a mensagem é descartada.                 
   if (msg.length()> 140){
        JOptionPane.showMessageDialog(null,"A mensagem possui mais de 140 caracteres");
        txMsg.setText(null);
    } else if(msg.equals("Sair")){
      bufferWriter.write("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    }else{
      bufferWriter.write(msg+"\r\n");
      texto.append( txNome.getText() + " diz -> " + txMsg.getText()+"\r\n");
    }
    
     bufferWriter.flush();
     txMsg.setText("");        
}
   
   // Aqui recebe as mensagens
   public void recebeMSG() throws IOException{
                          
       InputStream input = socket.getInputStream();
        InputStreamReader inputReader = new InputStreamReader(input);
        BufferedReader bufferReader = new BufferedReader(inputReader);
   String msg = "";
                          
    while(!"Sair".equalsIgnoreCase(msg))
                                     
       if(bufferReader.ready()){
         msg = bufferReader.readLine();
       if(msg.equals("Sair"))
         texto.append("Servidor caiu! \r\n");
        else
         texto.append(msg+"\r\n");         
        }
}
   
   // Método sair fecha os streams
   public void sair() throws IOException{
                          
    sendMSG("Sair");
    bufferWriter.close();
    write.close();
    ou.close();
    socket.close();
 }
   
   public void actionPerformed(ActionEvent e) {
         
  try {
     if(e.getActionCommand().equals(btSend.getActionCommand()))
        sendMSG(txMsg.getText());
     else
        if(e.getActionCommand().equals(btSair.getActionCommand()))
        sair();
     } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
     }                       
}
   
  // Método que recebe as ações dos botões da tela do chat
   @Override
public void keyPressed(KeyEvent e) {
               
    if(e.getKeyCode() == KeyEvent.VK_ENTER){
       try {
          sendMSG(txMsg.getText());
       } catch (IOException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       }                                                          
   }                       
}
   
   // Método que habilita o botão "Enter" para o envio de mensagens
@Override
public void keyReleased(KeyEvent arg0) {
  // TODO Auto-generated method stub               
}
   
@Override
public void keyTyped(KeyEvent arg0) {
  // TODO Auto-generated method stub               
} 

  // Método principal - cria um novo cliente e define os métodos 
// de conexão e recebimento de mensagens
  public static void main(String []args) throws IOException{
              
   Client client = new Client();
   client.conectar();
   client.recebeMSG();
}     
    
}
