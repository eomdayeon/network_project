import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;




public class ClientFrame extends JFrame{

	JTextArea textArea; //멤버 참조변수

	JTextField tfMsg;

	JButton btnSend;

	JButton btnHelp;
	
	Socket socket;

	DataInputStream dis;

	DataOutputStream dos;	


	

	public ClientFrame() {

		setTitle("Client");

		setBounds(450, 400, 500, 350);

		
		textArea = new JTextArea();		

		textArea.setEditable(false); //쓰기 금지

		JScrollPane scrollPane = new JScrollPane(textArea);

		getContentPane().add(scrollPane,BorderLayout.CENTER);

		
		
		JPanel btnpanel = new JPanel();
		
		btnSend = new JButton("send");
		btnHelp = new JButton("help");
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

		});
		
		
		//help 버튼 클릭에 반응하는 리스너 추가
		btnHelp.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {

				//help() 함수 실행;

			}

		});

		setVisible(true);

		tfMsg.requestFocus();

		

		//서버와 연결하는 네트워크 작업 : 스레드 객체 생성 및 실행

		ClientThread clientThread = new ClientThread();

		clientThread.setDaemon(true);

		clientThread.start();

		

		addWindowListener(new WindowAdapter() {			

			@Override //클라이언트 프레임에 window(창) 관련 리스너 추가

			public void windowClosing(WindowEvent e) {				

				super.windowClosing(e);

				try {

					if(dos != null) dos.close();

					if(dis != null) dis.close();

					if(socket != null) socket.close();

				} catch (IOException e1) {					

					e1.printStackTrace();

				}

			}			

		});

		

	}//생성자

	

	//이너클래스 : 서버와 연결하는 네트워크 작업 스레드

	class ClientThread extends Thread {

		@Override

		public void run() {

			try {

				socket = new Socket("127.0.0.1", 42756);

				textArea.append("서버에 접속됐습니다.\n");

				//데이터 전송을 위한 스트림 생성(입추력 모두)

				InputStream is = socket.getInputStream();

				OutputStream os = socket.getOutputStream();

				

				//보조스트림으로 만들어서 데이터전송 작업을 편하게 ※다른 보조스트림 사용

				dis = new DataInputStream(is);

				dos = new DataOutputStream(os);	

				

				while(true) {//상대방 메시지 받기

					String msg = dis.readUTF();

					textArea.append(" [SERVER] : " + msg + "\n");

					textArea.setCaretPosition(textArea.getText().length());

				}

			} catch (UnknownHostException e) {

				textArea.append("서버 주소가 이상합니다.\n");

			} catch (IOException e) {

				textArea.append("서버와 연결이 끊겼습니다.\n");

			}

		}

	}

	

	//메시지 전송하는 기능 메소드

		void sendMessage() {	

			String msg = tfMsg.getText(); //TextField에 써있는 글씨를 얻어오기

			tfMsg.setText(""); //입력 후 빈칸으로

			textArea.append(" [Clinet] : " + msg + "\n");//1.TextArea(채팅창)에 표시

			textArea.setCaretPosition(textArea.getText().length());

			//2.상대방(Server)에게 메시지 전송하기

			//아웃풋 스트림을 통해 상대방에 데이터 전송

			//네트워크 작업은 별도의 Thread가 하는 것이 좋음

			Thread t = new Thread() {

				@Override

				public void run() {

					try { //UTF = 유니코드의 규약(포맷), 한글 깨지지 않게 해줌

						dos.writeUTF(msg);

						dos.flush(); //계속 채팅 위해 close()하면 안됨				

					} catch (IOException e) {

						e.printStackTrace();

					}

				}

			};

			t.start();			

		}

}//class