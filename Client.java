
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
    
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txMsg;
    private JButton btSend;
    private JButton btSair;    
    private JLabel lbTitulo;
    private JLabel lbMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou ;
    private Writer write; 
    private BufferedWriter bufferWriter;
    private JTextField txIP;
    private JTextField txPorta;
    private JTextField txNome;  
    private String mensagem;
    
    
    
    public Client() throws IOException{                  
    JLabel lblMessage = new JLabel("Dados de conexao");
    txIP = new JTextField("127.0.0.1");
    txPorta = new JTextField("10001");
    txNome = new JTextField("Informe seu nome");                
    Object[] texts = {lblMessage, txIP, txPorta, txNome };  
        JOptionPane.showMessageDialog(null, texts);              
     pnlContent = new JPanel();
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
     pnlContent.add(lbTitulo);
     pnlContent.add(scroll);
     pnlContent.add(lbMsg);
     pnlContent.add(txMsg);
     pnlContent.add(btSair);
     pnlContent.add(btSend);
                                                       
     setTitle(txNome.getText());
     setContentPane(pnlContent);
     setLocationRelativeTo(null);
     setResizable(false);
     setSize(500,300);
     setVisible(true);
     setDefaultCloseOperation(EXIT_ON_CLOSE);
}
    
  public void conectar() throws IOException{
                          
  socket = new Socket(txIP.getText(),Integer.parseInt(txPorta.getText()));
  ou = socket.getOutputStream();
  write = new OutputStreamWriter(ou);
  bufferWriter = new BufferedWriter(write);
  bufferWriter.write(txNome.getText()+"\r\n");
  bufferWriter.flush();
}
  
   public void sendMSG (String msg) throws IOException{
                          
    if(msg.equals("Sair")){
      bufferWriter.write("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    }else{
      bufferWriter.write(msg+"\r\n");
      mensagem = txMsg.getText();
      if(mensagem.length()>140)
      {
        JOptionPane.showMessageDialog(null,"A mensagem possui mais de 140 caracteres");
      } else {
          texto.append( txNome.getText() + " diz -> " + txMsg.getText()+"\r\n");
        }
    }
     bufferWriter.flush();
     txMsg.setText("");        
}
   
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
   
@Override
public void keyReleased(KeyEvent arg0) {
  // TODO Auto-generated method stub               
}
   
@Override
public void keyTyped(KeyEvent arg0) {
  // TODO Auto-generated method stub               
} 

  public static void main(String []args) throws IOException{
              
   Client client = new Client();
   client.conectar();
   client.recebeMSG();
}     


    
    
}
