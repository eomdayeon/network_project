import java.awt.BorderLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.io.DataInputStream;

import java.io.DataOutputStream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;

import javax.swing.JTextField;




import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class ServerFrame extends JFrame {

	JTextArea textArea; //��� ��������

	JTextField tfMsg;

	JButton btnSend;

	
	
	ServerSocket serverSocket;  //accept�����ִ� ��
	Socket clientSocket;       //socket
	
	
	public static ExecutorService threadPool;
	public static Vector<forClient> clients=new Vector<forClient>();
	
	
	
	DataInputStream dataInputStream;   //dis
	DataOutputStream dataOutputStream; //dos
	

	public ServerFrame() {		

		setTitle("Server");

		setBounds(450, 50, 500, 350);

		

		textArea = new JTextArea();		

		textArea.setEditable(false); //���� ����

		JScrollPane scrollPane = new JScrollPane(textArea);

		add(scrollPane,BorderLayout.CENTER);

				

		JPanel msgPanel = new JPanel();

		msgPanel.setLayout(new BorderLayout());

		tfMsg = new JTextField();

		btnSend = new JButton("send");

		msgPanel.add(tfMsg, BorderLayout.CENTER);

		msgPanel.add(btnSend, BorderLayout.EAST);

		

		add(msgPanel,BorderLayout.SOUTH);

		
		
		/*
		//send ��ư Ŭ���� �����ϴ� ������ �߰�
		btnSend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		//����Ű ������ �� �����ϱ�
		tfMsg.addKeyListener(new KeyAdapter() {
			//Ű���忡�� Ű �ϳ��� �������� �ڵ����� ����Ǵ� �޼ҵ�..: �ݹ� �޼ҵ�
			@Override
			public void keyPressed(KeyEvent e) {				
				super.keyPressed(e);
				
			//�Է¹��� Ű�� �������� �˾Ƴ���, KeyEvent ��ü�� Ű������ ���� ��������
				int keyCode = e.getKeyCode();
				switch(keyCode) {
				case KeyEvent.VK_ENTER:
					sendMessage();
					break;
				}
			}
		});		  */
		
		
		serverSetting();
		

		setVisible(true);

		tfMsg.requestFocus();  //focus �ִ� 

		

		//������ ������ �� �ֵ��� ���������� ����� ����� �� �ִ� �غ� �۾�!

		//��Ʈ��ũ �۾��� Main Thread�� �ϰ��ϸ� �ٸ� �۾�(Ű���� �Է�, Ŭ�� ��..)���� 

		//���� �� �� ����, ���α׷��� ����, �׷��� Main�� UI�۾��� �����ϵ��� �ϰ�, 

		//�ٸ� �۾���(���� �ɸ���)��  ������ Thread���� �����ϴ� ���� ������.	
		
		

		/*ServerThread serverThread = new ServerThread();
		serverThread.setDaemon(true); //���� ������ ���� ����
		serverThread.start();*/
		
		
	   

		addWindowListener(new WindowAdapter() {			

			@Override //Ŭ���̾�Ʈ �����ӿ� window(â) ���� ������ �߰�

			public void windowClosing(WindowEvent e) {				

				super.windowClosing(e);

				try {

					if(dataOutputStream != null) dataOutputStream.close();

					if(dataInputStream != null) dataInputStream.close();

					if(clientSocket != null) clientSocket.close();

					if(serverSocket != null) serverSocket.close();

				} catch (IOException e1) {					

					e1.printStackTrace();

				}

			}			

		});

	}//������ �޼ҵ�	
	
	
	
		
		public void serverSetting()
		{
			try {
				serverSocket=new ServerSocket();  //10002��������Ʈ ��ȣ �༭ ���� ���� �� ���ε�
			    serverSocket.bind(new InetSocketAddress("127.0.0.1",42756));
				System.out.println("���� ����  ");	
				
			}catch(Exception e) {
				e.printStackTrace();
				if (!serverSocket.isClosed())
					stopServer();
				
			}
			
			Runnable thread= new Runnable() {

				@Override
				public void run() {
					while(true)
					{
						try {
							clientSocket= serverSocket.accept();  //accept �ϱ�
							clients.add(new forClient(clientSocket));  
							//������ ���� �Ϸ� �� �κ�
							System.out.println("Ŭ���̾�Ʈ �� "+clientSocket.getRemoteSocketAddress()+':'+Thread.currentThread().getName());
							
						}catch(Exception e)
						{
							if (!serverSocket.isClosed())
								stopServer();
							break;
						}
					}
					
					// TODO Auto-generated method stub
					
				}
				
			};
			
			threadPool=Executors.newCachedThreadPool();
			threadPool.submit(thread);
			
					
					
					
		}
		
		
		
		public void stopServer()
		{
			try {
				//���� �۵� ���� ��� ���� �ݱ�  
				Iterator<forClient> iterator=clients.iterator();
				while(iterator.hasNext()) {
					forClient client= iterator.next();
					client.socket.close();
					iterator.remove();
					
				}
				//���� ���� ��ü ��
				if (serverSocket !=null && !serverSocket.isClosed())
					serverSocket.close();
				if (threadPool !=null && !threadPool.isShutdown())
					threadPool.shutdown();
				
				
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			 
			
		}


}		






/*public Server()
{
	serverSetting();
	
	//closeAll();
}
public static void main(String []args) {
	new Server();
	
	
}
*/










//�̳�Ŭ���� : ���������� �����ϰ� Ŭ���̾�Ʈ�� ������ ����ϰ�,

//����Ǹ� �޽����� ���������� �޴� ���� ����

/*class ServerThread extends Thread {
@Override
public void run() {			
	try {  //���� ���� ���� �۾�
		serverSocket = new ServerSocket(42756);
		textArea.append("���������� �غ�ƽ��ϴ�...\n");
		textArea.append("Ŭ���̾�Ʈ�� ������ ��ٸ��ϴ�.\n");				
		socket = serverSocket.accept();//Ŭ���̾�Ʈ�� �����Ҷ����� Ŀ��(������)�� ���
		textArea.append(socket.getInetAddress().getHostAddress() + "���� �����ϼ̽��ϴ�.\n");
		
		//����� ���� ��Ʈ�� ����
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		
		while(true) {
			//������ ������ �����͸� �б�
			String msg = dis.readUTF();//������ ���������� ���
			textArea.append(" [Clinent] : " + msg + "\n");
			textArea.setCaretPosition(textArea.getText().length());
		}				
		
	} catch (IOException e) {
		textArea.append("Ŭ���̾�Ʈ�� �������ϴ�.\n");
	}
}
}
//�޽��� �����ϴ� ��� �޼ҵ�
void sendMessage() {	
String msg = tfMsg.getText(); //TextField�� ���ִ� �۾��� ������
tfMsg.setText(""); //�Է� �� ��ĭ����
textArea.append(" [SERVER] : " + msg + "\n");//1.TextArea(ä��â)�� ǥ��
textArea.setCaretPosition(textArea.getText().length()); //��ũ�� ���󰡰�
//2.����(Client)���� �޽��� �����ϱ�
Thread t = new Thread() {
	@Override
	public void run() {
		try {
			dos.writeUTF(msg);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
};		
t.start();
}	
}//class
/*if (tfMsg.getText().contains("������")) {  //��Ӿ� ����
String s = textArea.getText().replace("������", "****");
tfMsg.setText(s); */