
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

	JTextArea textArea; //멤버 참조변수

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

		textArea.setEditable(false); //쓰기 금지

		JScrollPane scrollPane = new JScrollPane(textArea);

		add(scrollPane,BorderLayout.CENTER);    //채팅화면 중간

				

		JPanel msgPanel = new JPanel();

		msgPanel.setLayout(new BorderLayout());

		tfMsg = new JTextField();

		btnSend = new JButton("send");

		msgPanel.add(tfMsg, BorderLayout.CENTER);

		msgPanel.add(btnSend, BorderLayout.EAST);    

		

		add(msgPanel,BorderLayout.SOUTH);

		

		//send 버튼 클릭에 반응하는 리스너 추가

		btnSend.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {

				send(tfMsg.getText());

			}

		});

		//엔터키 눌렀을 때 반응하기

		tfMsg.addKeyListener(new KeyAdapter() {

			//키보드에서 키 하나를 눌렀을때 자동으로 실행되는 메소드..: 콜백 메소드

			@Override

			public void keyPressed(KeyEvent e) {				

				super.keyPressed(e);

				

			//입력받은 키가 엔터인지 알아내기, KeyEvent 객체가 키에대한 정보 갖고있음

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
		//서버와 연결하는 네트워크 작업 : 스레드 객체 생성 및 실행
		ClientThread clientThread = new ClientThread();
		clientThread.setDaemon(true);
		clientThread.start();
	*/
		

		addWindowListener(new WindowAdapter() {			

			@Override //클라이언트 프레임에 window(창) 관련 리스너 추가

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

		

	}//생성자
	


	public void startClient()    //client 시작해주는 함
	{
		Thread thread =new Thread(){
			public void run()
			{
				try {

					clientSocket=new Socket(ip,port);
					recieve();
					//소켓이 접속 완료 된 부분
					System.out.println("접속 완료  ");
					
				}catch(Exception e) {
					if (clientSocket.isClosed())
					{
						stopClient();
						System.out.println("서버 접속 실패  ");
					
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
				while(isThread)     //쓰레드종료기능만들어줘야
				{
					try {
						InputStream in=clientSocket.getInputStream();
						byte[] buffer= new byte[512];  
						int length=in.read(buffer);
						if (length==-1) throw new IOException();
						
						 /*String recvData=dataInputStream.readUTF();   //quit해주 종료시키기 기
						 if (recvData.equals("/quit"))
							 isThread=true;
						 else
						*/	 
						String message=new String(buffer,0,length,"UTF-8");
					    textArea.append(message+"\n");
					    //textArea.setCaretPosition(textArea.getText().length());
						System.out.println( message); //수신 받은 데이터 게속 출력해주ㄱㅣ  
							 
							 
						
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
			//Scanner in=new Scanner(System.in);  //사용자로부터 입력 받기 위해

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
