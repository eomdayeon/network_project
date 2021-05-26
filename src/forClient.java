import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;



public class forClient {
	Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public forClient(Socket socket){
		this.socket=socket;
		streamSetting();
		receive();
		
	}
	public void streamSetting(){
		try {
			dataInputStream= new DataInputStream(socket.getInputStream()); //dataInputStream�� ���� �����Ͱ� �´�.
			dataOutputStream= new DataOutputStream(socket.getOutputStream()); //dataOutputStream�� ���� �����Ͱ� ����.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		//String data=dataInputStream.readUTF(); �̷������� ���ָ� ������ �о��
		
		
	}
	
	
	
	

	

	public void receive() {
		Runnable thread=new Runnable() {
			boolean isThread= true;
			String recvData;
			
			@Override
			public void run() {
				
					try {
						while(isThread)     //�����������ɸ�������
						{
							
							InputStream in=socket.getInputStream();
							byte[] buffer= new byte[512];
							int length=in.read(buffer);
							System.out.println(buffer);
							if (length==-1) throw new IOException();
						
									
							String message=new String(buffer,0,length,"UTF-8"); 
				
							
							System.out.println("[�޼��� ���� ����:]"+message+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
							
							 if (message.equals("help"))
							 {
								 for (forClient client :ServerFrame.clients) {
										client.send("���� ");   //��� Ŭ���̾�Ʈ���� �ż��� ����
									}
								 
							 }
							 else
							 {
								 for (forClient client :ServerFrame.clients) {
										client.send(message);   //��� Ŭ���̾�Ʈ���� �ż��� ����
									}
							 }
						
						}
						
						
					}catch(Exception e)
					{
						try {
							System.out.println("[�޼��� ���� ���� ]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
					        ServerFrame.clients.remove(forClient.this);
					        socket.close();
					        
							
						}
						catch (Exception e2)
						{
							e2.printStackTrace();
						}
						
		
				}
			
			}
			
		
		};
		ServerFrame.threadPool.submit(thread); //������ Ǯ�� �߰�;
		
	   
	
		
	}
	
	

	public void send(String message)
	{
		
		Runnable thread= new Runnable() {
		
			@Override
			public void run() {
				
				
					try {
						OutputStream out= socket.getOutputStream();
						byte[] buffer=message.getBytes("UTF-8");
						out.write(buffer);
						out.flush();
						
					
							

					} catch (Exception e) {
						try {
							
							System.out.println("[�޼��� �۽� ���� ]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
					        ServerFrame.clients.remove(forClient.this);
					        socket.close();
					    	//dataInputStream.close();
							//dataOutputStream.close();
							
						}
						catch (Exception e2)
						{
							e2.printStackTrace();
						}
					
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				// TODO Auto-generated method stub
				
			
			
		};
		ServerFrame.threadPool.submit(thread);
		
		
	}
}


//quit���ָ�Ŭ���̾�Ʈ �������ֱ�
//���� �ݾ���