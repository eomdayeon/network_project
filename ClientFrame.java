import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;




public class ClientFrame extends JFrame{

	JTextArea textArea; //��� ��������

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

		textArea.setEditable(false); //���� ����

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
		
		
		//help ��ư Ŭ���� �����ϴ� ������ �߰�
		btnHelp.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {

				//help() �Լ� ����;

			}

		});

		setVisible(true);

		tfMsg.requestFocus();

		

		//������ �����ϴ� ��Ʈ��ũ �۾� : ������ ��ü ���� �� ����

		ClientThread clientThread = new ClientThread();

		clientThread.setDaemon(true);

		clientThread.start();

		

		addWindowListener(new WindowAdapter() {			

			@Override //Ŭ���̾�Ʈ �����ӿ� window(â) ���� ������ �߰�

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

		

	}//������

	

	//�̳�Ŭ���� : ������ �����ϴ� ��Ʈ��ũ �۾� ������

	class ClientThread extends Thread {

		@Override

		public void run() {

			try {

				socket = new Socket("127.0.0.1", 42756);

				textArea.append("������ ���ӵƽ��ϴ�.\n");

				//������ ������ ���� ��Ʈ�� ����(���߷� ���)

				InputStream is = socket.getInputStream();

				OutputStream os = socket.getOutputStream();

				

				//������Ʈ������ ���� ���������� �۾��� ���ϰ� �شٸ� ������Ʈ�� ���

				dis = new DataInputStream(is);

				dos = new DataOutputStream(os);	

				

				while(true) {//���� �޽��� �ޱ�

					String msg = dis.readUTF();

					textArea.append(" [SERVER] : " + msg + "\n");

					textArea.setCaretPosition(textArea.getText().length());

				}

			} catch (UnknownHostException e) {

				textArea.append("���� �ּҰ� �̻��մϴ�.\n");

			} catch (IOException e) {

				textArea.append("������ ������ ������ϴ�.\n");

			}

		}

	}

	

	//�޽��� �����ϴ� ��� �޼ҵ�

		void sendMessage() {	

			String msg = tfMsg.getText(); //TextField�� ���ִ� �۾��� ������

			tfMsg.setText(""); //�Է� �� ��ĭ����

			textArea.append(" [Clinet] : " + msg + "\n");//1.TextArea(ä��â)�� ǥ��

			textArea.setCaretPosition(textArea.getText().length());

			//2.����(Server)���� �޽��� �����ϱ�

			//�ƿ�ǲ ��Ʈ���� ���� ���濡 ������ ����

			//��Ʈ��ũ �۾��� ������ Thread�� �ϴ� ���� ����

			Thread t = new Thread() {

				@Override

				public void run() {

					try { //UTF = �����ڵ��� �Ծ�(����), �ѱ� ������ �ʰ� ����

						dos.writeUTF(msg);

						dos.flush(); //��� ä�� ���� close()�ϸ� �ȵ�				

					} catch (IOException e) {

						e.printStackTrace();

					}

				}

			};

			t.start();			

		}

}//class