
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.Font;



public class ClientFrame extends JFrame{

	JTextArea textArea; //���۹��� �ؽ�Ʈ ���â ��� ��������
	JTextField tfMsg;  //������ �ؽ�Ʈ �Է�â
	JButton btnSend;  //���� ��ư 
	JButton btnHelp;  //������? ��ư
	String ip;			//������ ip
	int port;			//������ port 
	String name;

	public Socket clientSocket; //�������� ����� ����

	
	public ClientFrame(String ip, int port, String name) {

		this.ip = ip;
		this.port = port;
		this.name = name;
		
		setTitle("Client");

		setBounds(450, 400, 500, 350);


		textArea = new JTextArea();		

		textArea.setEditable(false); //���� ����

		JScrollPane scrollPane = new JScrollPane(textArea);

		getContentPane().add(scrollPane,BorderLayout.CENTER);    //ä��ȭ�� �߰�

				
		JPanel btnpanel = new JPanel(); 		//send, ������? ��ư �г� ����
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


		//send ��ư Ŭ���� �����ϴ� ������ �߰�

		btnSend.addActionListener(new ActionListener() {			

			@Override

			public void actionPerformed(ActionEvent e) {
				send(tfMsg.getText());
				tfMsg.setText("");
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
					tfMsg.setText("");
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
		
		
		startClient();

		setVisible(true);

		tfMsg.requestFocus();			//�ؽ�Ʈ �ʵ忡 Ŀ�� �Է�


		addWindowListener(new WindowAdapter() {			

			@Override //Ŭ���̾�Ʈ �����ӿ� window(â) ���� ������ �߰�

			public void windowClosing(WindowEvent e) {				

				super.windowClosing(e);

				try {
					if(clientSocket != null) clientSocket.close();

				} catch (IOException e1) {					

					e1.printStackTrace();

				}

			}			

		});

	}//������
	


	public void startClient()    //client �������ִ� �Լ�
	{
		Thread thread =new Thread(){
			public void run()
			{
				try {
					clientSocket=new Socket(ip,port);

					setTitle(name);				
					
					send(name + " ���� �����Ͽ����ϴ�.\n");
					//send(name + " ���� �����Ͽ����ϴ�.\n",name,true);
					//send(String str,bool init);
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
//						byte[] a = new byte[4];
//						int b = in.read(a);
						
						byte[] buffer= new byte[512]; 
					
						int length=in.read(buffer);
						if (length==-1) throw new IOException();
						
						 /*String recvData=dataInputStream.readUTF();   //quit���� �����Ű�� ��
						 if (recvData.equals("/quit"))
							 isThread=true;
						 else
						*/	 
						String message=new String(buffer,0,length,"UTF-8");
						
					    textArea.append( message+"\n");
					    //textArea.setCaretPosition(textArea.getText().length());
						System.out.println( message); //���� ���� ������ �Լ� ������֤���  
							 
						
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
			//Scanner in=new Scanner(System.in);  //����ڷκ��� �Է� �ޱ� ����

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
