
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

import java.io.InputStream;

import java.io.OutputStream;

import java.net.Socket;

import java.net.UnknownHostException;


import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;




import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;

import javax.swing.JTextField;



public class ClientFrame extends JFrame{

	JTextArea textArea; //��� ��������

	JTextField tfMsg;

	JButton btnSend;
	
	String ip;
	int port;


	public Socket clientSocket;
	

	
	

	public ClientFrame(String ip, int port) {

		
		this.ip = ip;
		this.port = port;
		
		setTitle("Client");

		setBounds(450, 400, 500, 350);

		

		textArea = new JTextArea();		

		textArea.setEditable(false); //���� ����

		JScrollPane scrollPane = new JScrollPane(textArea);

		add(scrollPane,BorderLayout.CENTER);    //ä��ȭ�� �߰�

				

		JPanel msgPanel = new JPanel();

		msgPanel.setLayout(new BorderLayout());

		tfMsg = new JTextField();

		btnSend = new JButton("send");

		msgPanel.add(tfMsg, BorderLayout.CENTER);

		msgPanel.add(btnSend, BorderLayout.EAST);    

		

		add(msgPanel,BorderLayout.SOUTH);

		

		//send ��ư Ŭ���� �����ϴ� ������ �߰�

		btnSend.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {

				send(tfMsg.getText());

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

					send(tfMsg.getText());

					break;

				}

			}

		});

		startClient();

		setVisible(true);

		tfMsg.requestFocus();

		/*
		//������ �����ϴ� ��Ʈ��ũ �۾� : ������ ��ü ���� �� ����
		ClientThread clientThread = new ClientThread();
		clientThread.setDaemon(true);
		clientThread.start();
	*/
		

		addWindowListener(new WindowAdapter() {			

			@Override //Ŭ���̾�Ʈ �����ӿ� window(â) ���� ������ �߰�

			public void windowClosing(WindowEvent e) {				

				super.windowClosing(e);

				try {

					//if(dos != null) dos.close();

					//if(dis != null) dis.close();

					if(clientSocket != null) clientSocket.close();

				} catch (IOException e1) {					

					e1.printStackTrace();

				}

			}			

		});

		

	}//������
	


	public void startClient()    //client �������ִ� ��
	{
		Thread thread =new Thread(){
			public void run()
			{
				try {

					clientSocket=new Socket(ip,port);
					recieve();
					//������ ���� �Ϸ� �� �κ�
					System.out.println("���� �Ϸ�  ");
					
				}catch(Exception e) {
					if (clientSocket.isClosed())
					{
						stopClient();
						System.out.println("���� ���� ����  ");
					
					}
					
				}
				
			}
			
			
		};
		thread.start();
		
	
		
	}
	
	public void recieve() {
		new Thread(new Runnable() {
			boolean isThread= true;
			
			@Override
			public void run() {
				while(isThread)     //�����������ɸ�������
				{
					try {
						InputStream in=clientSocket.getInputStream();
						byte[] buffer= new byte[512];  
						int length=in.read(buffer);
						if (length==-1) throw new IOException();
						
						 /*String recvData=dataInputStream.readUTF();   //quit���� �����Ű�� ��
						 if (recvData.equals("/quit"))
							 isThread=true;
						 else
						*/	 
						String message=new String(buffer,0,length,"UTF-8");
					    textArea.append(message+"\n");
					    //textArea.setCaretPosition(textArea.getText().length());
						System.out.println( message); //���� ���� ������ �Լ� ������֤���  
							 
							 
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						stopClient();
						//e.printStackTrace();
						break;
					}
					
				}
				// TODO Auto-generated method stub
				
			}
			
		
		}).start();
	
		
	}
	
	public void send(String message)
	{
		
		
		Thread  thread=new Thread() {
			//Scanner in=new Scanner(System.in);  //����ڷκ��� �Է� �ޱ� ����

			@Override
			public void run() {
				
					try {
						
						OutputStream out=clientSocket.getOutputStream();
						//ObjectOutputStream os= new ObjectOutputStream(os);
						byte[] buffer= message.getBytes("UTF-8");
						out.write(buffer);
						out.flush();
						
							

					} catch (Exception e) {
						// TODO Auto-generated catch block
						stopClient();
					
					}
			}
					
			
				
			
			
		};
		thread.start();
		
		
	}
	
	
	public  void stopClient() {
		
		try {
			clientSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}
 

}//class
