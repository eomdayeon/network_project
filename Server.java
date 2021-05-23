import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private ServerSocket serverSocket;  //accept시켜주는 소
	
	
	public static ExecutorService threadPool;
	public static Vector<forClient> clients=new Vector<forClient>();
	
	private Socket clientSocket;
	
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
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
	
	public Server()
	{
		serverSetting();
		
		//closeAll();
	}
	public static void main(String []args) {
		new Server();
		
		
	}

}
