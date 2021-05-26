import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

class ImagePanel extends JPanel{
	private Image img;
	
	public ImagePanel(Image img) {
		this.img = img;
		setSize(640,480);
		setPreferredSize(new Dimension(640,480));
		setLayout(null);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, 640, 480, null);
	}
}


public class MainProgram {

	private JFrame frame;
	private JTextField ipField;
	private JTextField portField;
	private JTextField nameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainProgram window = new MainProgram();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainProgram() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("야, 밥먹자 ! ");
		frame.setSize(640,480);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//로그인 화면 panel 코드 시작
		ImagePanel loginFrame = new ImagePanel(new ImageIcon("C:/Users/엄다연/eclipse-workspace/java test/image/food.jpg").getImage());
		frame.getContentPane().add(loginFrame);
		loginFrame.setLayout(null);

		
		JButton loginbtn = new JButton("enter here");
		loginbtn.setFont(new Font("Ink Free", Font.PLAIN, 16));
		loginbtn.setBounds(127, 275, 227, 37);
		loginFrame.add(loginbtn);
			
		JLabel lbIp = new JLabel("ip : ");
		lbIp.setForeground(Color.BLACK);
		lbIp.setFont(new Font("Ink Free", Font.PLAIN, 23));
		lbIp.setBounds(57, 69, 90, 106);
		loginFrame.add(lbIp);
			
		JLabel lbPort = new JLabel("port : ");
		lbPort.setForeground(Color.BLACK);
		lbPort.setFont(new Font("Ink Free", Font.PLAIN, 23));
		lbPort.setBounds(57, 161, 90, 37);
		loginFrame.add(lbPort);
			
		ipField = new JTextField();
		ipField.setFont(new Font("Ink Free", Font.PLAIN, 14));
		ipField.setBounds(127, 106, 227, 37);
		loginFrame.add(ipField);
		ipField.setColumns(10);
		
		portField = new JTextField();
		portField.setFont(new Font("Ink Free", Font.PLAIN, 14));
		portField.setBounds(127, 161, 227, 37);
		loginFrame.add(portField);
		portField.setColumns(10);
			
		JLabel lblogin = new JLabel("Log in form");
		lblogin.setForeground(new Color(255, 255, 255));
		lblogin.setFont(new Font("Ink Free", Font.BOLD, 22));
		lblogin.setBounds(171, 37, 150, 50);
		loginFrame.add(lblogin);
		
		JLabel lbName = new JLabel("name :");
		lbName.setForeground(Color.BLACK);
		lbName.setFont(new Font("Ink Free", Font.PLAIN, 23));
		lbName.setBounds(57, 215, 112, 50);
		loginFrame.add(lbName);
		
		nameField = new JTextField();
		nameField.setFont(new Font("Ink Free", Font.PLAIN, 14));
		nameField.setBounds(127, 216, 227, 37);
		loginFrame.add(nameField);
		nameField.setColumns(10);
		
		loginFrame.setVisible(false);
		//로그인 화면 panel 코드 끝
		
		
		//프로그램 시작화면 panel 코드 시작
		ImagePanel startFrame = new ImagePanel(new ImageIcon("C:/Users/엄다연/eclipse-workspace/java test/image/food.jpg").getImage());
		frame.getContentPane().add(startFrame);
		startFrame.setLayout(null);
		
		JLabel label1 = new JLabel("What should we eat today?");
		label1.setForeground(Color.WHITE);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setFont(new Font("Ink Free", Font.BOLD, 33));
		label1.setBounds(53, 55, 532, 82);
		startFrame.add(label1);
		
		JLabel label2 = new JLabel("Let's choose the menu together!");
		label2.setFont(new Font("Ink Free", Font.PLAIN, 20));
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setBounds(145, 112, 319, 58);
		startFrame.add(label2);
		
		JButton startbtn = new JButton("Start Program");
		startbtn.setFont(new Font("LG PC", Font.PLAIN, 16));
		startbtn.setBounds(169, 164, 130, 23);
		startFrame.add(startbtn);
		
		JButton exitbtn = new JButton("Exit");
		exitbtn.setFont(new Font("LG PC", Font.PLAIN, 16));
		exitbtn.setBounds(311, 164, 119, 23);
		startFrame.add(exitbtn);
		//프로그램 시작화면 panel 코드 끝
		
		
		//시작화면에서 start누르면 로그인 페이지로 이동 (버튼 리스너 구현)
		startbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ServerFrame frame;
				startFrame.setVisible(false);
				loginFrame.setVisible(true);
				frame = new ServerFrame();

			
			}
			
		});
		
		//시작화면에서 exit누르면 프로그램 종료(버튼 리스너 구현)
		exitbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		
		//로그인화면에서 enter누르면 클라이언트 채팅접속(버튼 리스너 구현)
		loginbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//"127.0.0.1", 42756
				ClientFrame frame;
				if(ipField.getText().equals("127.0.0.1")&& portField.getText().equals("42756"))
					frame = new ClientFrame(ipField.getText(), Integer.parseInt(portField.getText()),nameField.getText());
				
			}
		});
	}
}
