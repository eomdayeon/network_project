
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.Font;



public class ClientFrame extends JFrame{

	JTextArea textArea; //전송받은 텍스트 출력창 멤버 참조변수
	JTextField tfMsg;  //전송할 텍스트 입력창
	JButton btnSend;  //전송 버튼 
	JButton btnHelp;  //뭐먹지? 버튼
	String ip;			//접속할 ip
	int port;			//접속할 port 
	String name;

	public Socket clientSocket; //서버와의 통신을 위함

	
	public ClientFrame(String ip, int port, String name) {

		this.ip = ip;
		this.port = port;
		this.name = name;
		
		setTitle("Client");

		setBounds(450, 400, 500, 350);


		textArea = new JTextArea();		

		textArea.setEditable(false); //쓰기 금지

		JScrollPane scrollPane = new JScrollPane(textArea);

		getContentPane().add(scrollPane,BorderLayout.CENTER);    //채팅화면 중간

				
		JPanel btnpanel = new JPanel(); 		//send, 뭐먹지? 버튼 패널 생성
		btnSend = new JButton("send");
		btnHelp = new JButton("\uBB50 \uBA39\uC9C0?");
		btnHelp.setFont(new Font("LG PC", Font.PLAIN, 12));
		btnpanel.add(btnSend, BorderLayout.EAST);
		btnpanel.add(btnHelp, BorderLayout.EAST);
		
		
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BorderLayout());

		tfMsg = new JTextField();

		msgPanel.add(tfMsg, BorderLayout.CENTER);
		msgPanel.add(btnpanel, BorderLayout.EAST);    
		

		getContentPane().add(msgPanel,BorderLayout.SOUTH);


		//send 버튼 클릭에 반응하는 리스너 추가

		btnSend.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {
				send(tfMsg.getText());
				tfMsg.setText("");
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
					tfMsg.setText("");
					break;

				}

			}

		});
	
		
		//help 버튼 클릭에 반응하는 리스너 추가
		btnHelp.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {

				//help() 함수 실행;

			}

		});
		
		
		startClient();

		setVisible(true);

		tfMsg.requestFocus();			//텍스트 필드에 커서 입력


		addWindowListener(new WindowAdapter() {			

			@Override //클라이언트 프레임에 window(창) 관련 리스너 추가

			public void windowClosing(WindowEvent e) {				

				super.windowClosing(e);

				try {
					if(clientSocket != null) clientSocket.close();

				} catch (IOException e1) {					

					e1.printStackTrace();

				}

			}			

		});

	}//생성자
	


	public void startClient()    //client 시작해주는 함수
	{
		Thread thread =new Thread(){
			public void run()
			{
				try {
					clientSocket=new Socket(ip,port);

					setTitle(name);				
					
					send(name + " 님이 접속하였습니다.\n");
					//send(name + " 님이 접속하였습니다.\n",name,true);
					//send(String str,bool init);
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
//						byte[] a = new byte[4];
//						int b = in.read(a);
						
						byte[] buffer= new byte[512]; 
					
						int length=in.read(buffer);
						if (length==-1) throw new IOException();
						
						 /*String recvData=dataInputStream.readUTF();   //quit해주 종료시키기 기
						 if (recvData.equals("/quit"))
							 isThread=true;
						 else
						*/	 
						String message=new String(buffer,0,length,"UTF-8");
						
					    textArea.append( message+"\n");
					    //textArea.setCaretPosition(textArea.getText().length());
						System.out.println( message); //수신 받은 데이터 게속 출력해주ㄱㅣ  
							 
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						stopClient();
						//e.printStackTrace();
						break;
					}
					
				}
				
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
						
//						String aaa = "aaaa";
//						byte[] bb = aaa.getBytes();
//						out.write(bb);
//						out.flush();
						
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
