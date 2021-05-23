import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
	
	private Socket clientSocket;
	
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	//1.데이터 계속전송 쓰레드
	//2.데이터 계속수신 쓰레드
 
	
	public void connect()
	{
		try {

			clientSocket=new Socket("127.0.0.1",49156);
			//소켓이 접속 완료 된 부분
			System.out.println("접속 완 ");
			
		}catch(Exception e) {
			
		}
	}
	public void streamSetting(){
		try {
			dataInputStream= new DataInputStream(clientSocket.getInputStream()); //dataInputStream을 통해 데이터가 온다.
			dataOutputStream= new DataOutputStream(clientSocket.getOutputStream()); //dataOutputStream을 통해 데이터가 간다.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		//String data=dataInputStream.readUTF(); 이런식으로 해주면 데이터 읽어옴
		
		
	}
	
	public void dataRecv() {
	
			boolean isThread= true;
			
			while(isThread)     //쓰레드종료기능만들어줘야
			{
				try {
					 String recvData=dataInputStream.readUTF();
					 if (recvData.equals("/quit"))
						 isThread=false;
					 else
						 System.out.println("상대방: "+ recvData); //수신 받은 데이터 게속 출력해주
						 
						 
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
	
		
	}
	
	public void dataSend()
	{
		
			Scanner in=new Scanner(System.in);  //사용자로부터 입력 받기 위해
			boolean isThread=true;
			
			while(isThread)  //쓰레드 종료 기능 만들어야
			{
				try {
					
					String sendData=in.nextLine();
					if (sendData.equals("/quit/"))
						isThread=false;
					else
						dataOutputStream.writeUTF(sendData);
						

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		
	}
	
	public Client2()
	{
		connect();
		streamSetting();
		
		dataSend();
	    dataRecv();
		//closeAll();
	}
	
	private void closeAll() {
		
		try {
			clientSocket.close();
			dataInputStream.close();
			dataOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}
	public static void main(String []args) {
		new Client2();
		
		
	}

}
