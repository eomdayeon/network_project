import java.awt.*;
import javax.swing.*;

public class Frame {
	public static void main (String[] args) {
		JFrame frame = new JFrame() ;  //시작하는 하나의 tool
		JPanel panel = new JPanel();   //frame에 부착하는 panel 생성
		JLabel label = new JLabel("some text");   
		JButton btn1 = new JButton("click me!"); //button 생성
		JTextArea txtarea = new JTextArea(); //여러 글씨 집어넣을 때 사용
		//JTestField  한 줄의 글씨 집어넣을 때 사용
		JTextField txtField = new JTextField(200);
		
		
		panel.setLayout(new BorderLayout());

		panel.add(label, BorderLayout.NORTH);   //위쪽에 글자 추가
		panel.add(btn1, BorderLayout.WEST);  //왼쪽에 버튼 추가
		panel.add(txtarea, BorderLayout.CENTER);  //중앙에 txtarea 추가
		//panel.add(txtField, BorderLayout.CENTER);  중앙에 textField 추가
		
		
		frame.add(panel);   //panel을 frame에 집어넣어줌
		
	
		
		frame.setResizable(false); //프로그램 사이즈 조정 못하도록 고정
		frame.setVisible(true); //프로그램 frame 띄우기
		frame.setPreferredSize(new Dimension(840,840/12*9)); //프레임 사이즈 지정
		frame.setSize(840,840/12*9);  
		frame.setLocationRelativeTo(null); //어떤 위치에서 GUI프로그램 켜지게 할건지 설정
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//프로그램 껐을 때 모든게 종료되도록 설정
	}
}
