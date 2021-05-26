import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;




public class ServerFrame extends JFrame {

	JTextArea textArea; //��� ��������

	JTextField tfMsg;

	JButton btnSend;

	

	ServerSocket serverSocket;

	Socket socket;

	DataInputStream dis;

	DataOutputStream dos;

	

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

		});		

		

		setVisible(true);

		tfMsg.requestFocus();  //focus �ִ� 

		

		//������ ������ �� �ֵ��� ���������� ����� ����� �� �ִ� �غ� �۾�!

		//��Ʈ��ũ �۾��� Main Thread�� �ϰ��ϸ� �ٸ� �۾�(Ű���� �Է�, Ŭ�� ��..)���� 

		//���� �� �� ����, ���α׷��� ����, �׷��� Main�� UI�۾��� �����ϵ��� �ϰ�, 

		//�ٸ� �۾���(���� �ɸ���)��  ������ Thread���� �����ϴ� ���� ������.	

		ServerThread serverThread = new ServerThread();

		serverThread.setDaemon(true); //���� ������ ���� ����

		serverThread.start();

		

		addWindowListener(new WindowAdapter() {			

			@Override //Ŭ���̾�Ʈ �����ӿ� window(â) ���� ������ �߰�

			public void windowClosing(WindowEvent e) {				

				super.windowClosing(e);

				try {

					if(dos != null) dos.close();

					if(dis != null) dis.close();

					if(socket != null) socket.close();

					if(serverSocket != null) serverSocket.close();

				} catch (IOException e1) {					

					e1.printStackTrace();

				}

			}			

		});

	}//������ �޼ҵ�	

	

	//�̳�Ŭ���� : ���������� �����ϰ� Ŭ���̾�Ʈ�� ������ ����ϰ�,

	//����Ǹ� �޽����� ���������� �޴� ���� ����

	class ServerThread extends Thread {

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
	tfMsg.setText(s);
}		*/