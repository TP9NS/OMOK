import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
 
//회원정보 변경 기능을 수행하는 인터페이스
public class CInfoFrame extends JFrame{
	/* Panel */
	JPanel panel = new JPanel() {
    	protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            // 그라데이션 색상과 중심, 반지름 설정
            float centerX = getWidth() / 2.0f;
            float centerY = getHeight() / 2.0f;
            float radius = Math.min(getWidth(), getHeight()) / 0.2f;
            float[] dist = { 0.0f, 1.0f };
            Color[] colors = {new Color(242, 224, 227), new Color(169, 193, 249)};

            // 원형 그라데이션 생성
            RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, radius, dist, colors);
            
            g2d.setPaint(gradientPaint);
            g2d.fillRect(0, 0, width, height);

            g2d.dispose();
        }
    };
	JLabel imageL= new JLabel();
	/* Button */
    JButton proBtn = new JButton("프로필 변경하기");
	JButton nameBtn = new JButton("이름 변경하기");
	JButton pwBtn = new JButton("비밀번호 변경하기");
	JButton emailBtn = new JButton("이메일 변경하기");
	JButton exitBtn = new JButton("나가기");
	
	String name;	//변경할 이름
	String pw;		//변경할 비밀번호
	String email;	//변경할 이메일
	
	Client c = null;

	final String changeTag = "CHANGE";	//회원정보 변경 기능 태그
	
	CInfoFrame(Client _c) {
		c = _c;
		
		setTitle("회원정보 수정");
		imageL.setPreferredSize(new Dimension(200, 200));
		/* Button 크기 작업 */
		proBtn.setPreferredSize(new Dimension(250, 30));
		nameBtn.setPreferredSize(new Dimension(250, 30));
		pwBtn.setPreferredSize(new Dimension(250, 30));
		emailBtn.setPreferredSize(new Dimension(250, 30));
		exitBtn.setPreferredSize(new Dimension(250, 30));
		
		/* panel 추가 작업 */
		setContentPane(panel);	//panel을 기본 컨테이너로 설정
		panel.add(imageL);
		panel.add(proBtn);
		panel.add(nameBtn);
		panel.add(pwBtn);
		panel.add(emailBtn);
		panel.add(exitBtn);
		
		/* 버튼 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();
		
		proBtn.addActionListener(bl);
		nameBtn.addActionListener(bl);
		pwBtn.addActionListener(bl);
		emailBtn.addActionListener(bl);
		exitBtn.addActionListener(bl);
		
		setSize(280, 420);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	/* Button 이벤트 리스너 */
	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			/* 이름 변경하기 버튼 이벤트 */
			if(b.getText().equals("이름 변경하기")) {
				System.out.println("[Client] 이름 변경 시도");
				//변경할 이름을 받음
				name = JOptionPane.showInputDialog(null, "변경할 이름을 입력하시오", "이름변경", JOptionPane.QUESTION_MESSAGE);
				
				if(name != null) {	//name이 null이 아니면 서버에 "태그//이름//변경할이름" 형태의 메시지를 전송
					c.sendMsg(changeTag + "//name//" + name);
				}
			}
			
			/* 비밀번호 변경하기 버튼 이벤트 */
			else if(b.getText().equals("비밀번호 변경하기")) {
				System.out.println("[Client] 비밀번호 변경 시도");
				//변경할 비밀번호를 받음
				pw = JOptionPane.showInputDialog(null, "변경할 비밀번호를 입력하시오", "비밀번호변경", JOptionPane.QUESTION_MESSAGE);
				
				if(pw != null) {	//pw가 null이 아니면 서버에 "태그//비밀번호//변경할비밀번호" 형태의 메시지를 전송
					c.sendMsg(changeTag + "//password//" + pw);
				}
			}
			
			/* 이메일 변경하기 버튼 이벤트 */
			else if(b.getText().equals("이메일 변경하기")) {
				System.out.println("[Client] 이메일 변경 시도");
				//변경할 이메일을 받음
				email = JOptionPane.showInputDialog(null, "변경할 이메일을 입력하시오", "이메일변경", JOptionPane.QUESTION_MESSAGE);
				
				if(email != null) {	//email이 null이 아니면 서버에 "태그//이메일//변경할이메일" 형태의 메시지를 전송
					c.sendMsg(changeTag + "//email//" + email);
				}
			}else if(b.getText().equals("프로필 변경하기")){
			    String uimage;
				JFileChooser fileChooser = new JFileChooser();
		        int result = fileChooser.showOpenDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
		           
		            try {
		                // 이미지 파일을 BufferedImage로 읽어옴
		                BufferedImage image = ImageIO.read(selectedFile);
		                // 이미지 크기를 200x200으로 조정
		                BufferedImage resizedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		                Graphics2D g = resizedImage.createGraphics();
		                g.drawImage(image, 0, 0, 200, 200, null);
		                g.dispose();
		                // BufferedImage를 바이트 배열로 변환 후 Base64로 인코딩하여 문자열로 변환
		                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		                ImageIO.write(resizedImage, "jpeg", byteArrayOutputStream);
		                byte[] imageBytes = byteArrayOutputStream.toByteArray();
		                uimage = Base64.getEncoder().encodeToString(imageBytes);
		                byteArrayOutputStream.close();
		                imageL.setIcon(new ImageIcon(resizedImage.getScaledInstance(imageL.getWidth(), imageL.getHeight(), Image.SCALE_SMOOTH)));
		                c.sendMsg("changepro//"+uimage);
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		            }
			}else if(b.getText().equals("나가기")) {
				System.out.println("[Client] 회원 정보 변경 인터페이스 종료");
				dispose();	//인터페이스 닫음
			
		}
	}
}
	}