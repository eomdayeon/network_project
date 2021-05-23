import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class forClient {
	Socket socket;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	public forClient(Socket socket){
		this.socket=socket;
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
							
							try {
								recvData = dataInputStream.readUTF();
								if (recvData.equals("/quit"))
									 isThread=true;
								else
									System.out.println("상대방: "+ recvData);   //수신 받은 데이터 게속 출력해주
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//System.out.println("[메세지 수신 성공]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
							for (forClient client :Server.clients) {
								client.send(recvData);   //모든 클라이언트에게 매세지 보내
							}
						
						}
						
						
					}catch(Exception e)
					{
						try {
							System.out.println("[메세지 수신 오류 ]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
					        Server.clients.remove(forClient.this);
					        socket.close();
					    	//dataInputStream.close();
							//dataOutputStream.close();
					        
							
						}
						catch (Exception e2)
						{
							e2.printStackTrace();
						}
						
				        
					
					
					
					
				  
					 
				}
				// TODO Auto-generated method stub
				
			}
			
		
		};
		Server.threadPool.submit(thread); //쓰레드 풀에 추가;
		
	   
	
		
	}
	
	

	public void send(String message)
	{
		
		Runnable thread= new Runnable() {
		
			@Override
			public void run() {
				
				
				while(true)  //쓰레드 종료 기능 만들어야
				{
					try {
						String sendData=message;
						//if (sendData.equals("/quit/"))
						//	isThread=false;
						//else
						dataOutputStream.writeUTF(sendData);
							

					} catch (IOException e) {
						try {
							System.out.println("[메세지 송신 오류 ]"+socket.getRemoteSocketAddress()+":"+Thread.currentThread().getName());
					        Server.clients.remove(forClient.this);
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
				
			}
			
		};
		Server.threadPool.submit(thread);
		
		
	}
}


//quit해주면클라이언트 종료해주기
//소켓 닫아주
