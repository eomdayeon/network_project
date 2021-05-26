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
			dataInputStream= new DataInputStream(socket.getInputStream()); //dataInputStream을 통해 데이터가 온다.
			dataOutputStream= new DataOutputStream(socket.getOutputStream()); //dataOutputStream을 통해 데이터가 간다.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		//String data=dataInputStream.readUTF(); 이런식으로 해주면 데이터 읽어옴
		
		
	}
	
	
	
	

	

	public void receive() {
		Runnable thread=new Runnable() {
			boolean isThread= true;
			String recvData;
			
			@Override
			public void run() {
				
					try {
						while(isThread)     //쓰레드종료기능만들어줘야
						{
							
							InputStream in=socket.getInputStream();
							byte[] buffer= new byte[512];
							int length=in.read(buffer);
							System.out.println(buffer);
							if (length==-1) throw new IOException();
						
									
							String message=new String(buffer,0,length,"UTF-8"); 
				
							
							System.out.println("[메세지 수신 성공:]"+message+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
							
							 if (message.equals("help"))
							 {
								 for (forClient client :ServerFrame.clients) {
										client.send("음식 ");   //모든 클라이언트에게 매세지 보내
									}
								 
							 }
							 else
							 {
								 for (forClient client :ServerFrame.clients) {
										client.send(message);   //모든 클라이언트에게 매세지 보내
									}
							 }
						
						}
						
						
					}catch(Exception e)
					{
						try {
							System.out.println("[메세지 수신 오류 ]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
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
		ServerFrame.threadPool.submit(thread); //쓰레드 풀에 추가;
		
	   
	
		
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
							
							System.out.println("[메세지 송신 오류 ]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
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


//quit해주면클라이언트 종료해주기
//소켓 닫아주
