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

	JTextArea textArea; //멤버 참조변수

	JTextField tfMsg;

	JButton btnSend;

	
	
	ServerSocket serverSocket;  //accept시켜주는 소
	Socket clientSocket;       //socket
	
	
	public static ExecutorService threadPool;
	public static Vector<forClient> clients=new Vector<forClient>();
	
	
	
	DataInputStream dataInputStream;   //dis
	DataOutputStream dataOutputStream; //dos
	

	public ServerFrame() {		

		setTitle("Server");

		setBounds(450, 50, 500, 350);

		

		textArea = new JTextArea();		

		textArea.setEditable(false); //쓰기 금지

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

		//send 버튼 클릭에 반응하는 리스너 추가

		btnSend.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {

				sendMessage();

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

					sendMessage();

					break;

				}

			}

		});		  */
		
		
		serverSetting();
		

		setVisible(true);

		tfMsg.requestFocus();  //focus 주는 

		

		//상대방이 접속할 수 있도록 서버소켓을 만들고 통신할 수 있는 준비 작업!

		//네트워크 작업을 Main Thread가 하게하면 다른 작업(키보드 입력, 클릭 등..)들을 

		//전혀 할 수 없음, 프로그램이 멈춤, 그래서 Main은 UI작업에 전념하도록 하고, 

		//다른 작업들(오래 걸리는)은  별도의 Thread에게 위임하는 것이 적절함.	
		
		

		/*ServerThread serverThread = new ServerThread();

		serverThread.setDaemon(true); //메인 끝나면 같이 종료

		serverThread.start();*/
		
		
	   

		addWindowListener(new WindowAdapter() {			

			@Override //클라이언트 프레임에 window(창) 관련 리스너 추가

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

	}//생성자 메소드	
	
	
	
		
		public void serverSetting()
		{
			try {
				serverSocket=new ServerSocket();  //10002번으로포트 번호 줘서 소켓 생성 및 바인드
			    serverSocket.bind(new InetSocketAddress("127.0.0.1",49157));
				System.out.println("서버 생성  ");	
				
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
							clientSocket= serverSocket.accept();  //accept 하기
							clients.add(new forClient(clientSocket));  
							//소켓이 접속 완료 된 부분
							System.out.println("클라이언트 접 "+clientSocket.getRemoteSocketAddress()+':'+Thread.currentThread().getName());
							
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
				//현재 작동 중인 모든 소켓 닫기  
				Iterator<forClient> iterator=clients.iterator();
				while(iterator.hasNext()) {
					forClient client= iterator.next();
					client.socket.close();
					iterator.remove();
					
				}
				//서보 소켓 객체 닫
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










//이너클래스 : 서버소켓을 생성하고 클라이언트의 연결을 대기하고,

//연결되면 메시지를 지속적으로 받는 역할 수행

/*class ServerThread extends Thread {

@Override

public void run() {			

	try {  //서버 소켓 생성 작업

		serverSocket = new ServerSocket(42756);

		textArea.append("서버소켓이 준비됐습니다...\n");

		textArea.append("클라이언트의 접속을 기다립니다.\n");				

		socket = serverSocket.accept();//클라이언트가 접속할때까지 커서(스레드)가 대기

		textArea.append(socket.getInetAddress().getHostAddress() + "님이 접속하셨습니다.\n");

		

		//통신을 위한 스트림 생성

		dis = new DataInputStream(socket.getInputStream());

		dos = new DataOutputStream(socket.getOutputStream());

		

		while(true) {

			//상대방이 보내온 데이터를 읽기

			String msg = dis.readUTF();//상대방이 보낼때까지 대기

			textArea.append(" [Clinent] : " + msg + "\n");

			textArea.setCaretPosition(textArea.getText().length());

		}				

		

	} catch (IOException e) {

		textArea.append("클라이언트가 나갔습니다.\n");

	}

}

}



//메시지 전송하는 기능 메소드

void sendMessage() {	

String msg = tfMsg.getText(); //TextField에 써있는 글씨를 얻어오기

tfMsg.setText(""); //입력 후 빈칸으로

textArea.append(" [SERVER] : " + msg + "\n");//1.TextArea(채팅창)에 표시

textArea.setCaretPosition(textArea.getText().length()); //스크롤 따라가게

//2.상대방(Client)에게 메시지 전송하기

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



/*if (tfMsg.getText().contains("강아지")) {  //비속어 필터

String s = textArea.getText().replace("강아지", "****");

tfMsg.setText(s); */
