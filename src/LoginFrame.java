import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;
//로그인 기능을 수행하는 인터페이스
public class LoginFrame extends JFrame{
	
	/* Panel */
	JPanel backgroud = new ImagePanel();
	JPanel basePanel = new JPanel(new BorderLayout());
	JPanel centerPanel = new JPanel(new BorderLayout());
	JPanel westPanel = new JPanel();
	JPanel eastPanel = new JPanel();
	JPanel southPanel = new JPanel();
	
	/* Label */
	JLabel idL = new JLabel("아이디");
	JLabel pwL = new JLabel("비밀번호");
	JLabel idsL= new JLabel("아이디 찾기");
	JLabel LL= new JLabel("/");
	JLabel pwsL = new JLabel("비밀번호 찾기");
	/* TextField */
	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();
	
	/* Button */
	JButton loginBtn = new JButton("로그인");
	JButton joinBtn = new JButton("회원가입");
	JButton exitBtn = new JButton("게임종료");
	JButton adminBtn = new JButton("관리자 창으로 이동");
	
	Client c = null;
	
	final String loginTag = "LOGIN";	//로그인 기능 태그
	final String adminPassword = "0000"; 
	LoginFrame(Client _c) {
		c = _c;
		
		setTitle("로그인");
		
		/* Panel 크기 작업 */
		centerPanel.setPreferredSize(new Dimension(260, 80));
		westPanel.setPreferredSize(new Dimension(210, 75));
		eastPanel.setPreferredSize(new Dimension(90, 85));
		southPanel.setPreferredSize(new Dimension(290, 60));
		
		/* Label 크기 작업 */
		idL.setPreferredSize(new Dimension(50, 30));
		pwL.setPreferredSize(new Dimension(50, 30));
		idsL.setPreferredSize(new Dimension(80, 20));
		pwsL.setPreferredSize(new Dimension(80, 20));
		LL.setPreferredSize(new Dimension(20, 10));
		/* TextField 크기 작업 */
		id.setPreferredSize(new Dimension(140, 30));
		pw.setPreferredSize(new Dimension(140, 30));
		
		/* Button 크기 작업 */
		loginBtn.setPreferredSize(new Dimension(75, 63));
		joinBtn.setPreferredSize(new Dimension(135, 25));
		exitBtn.setPreferredSize(new Dimension(135, 25));
		adminBtn.setPreferredSize(new Dimension(30, 30));
		/* Panel 추가 작업 */
		setContentPane(backgroud);	//panel을 기본 컨테이너로 설정
		
		basePanel.add(centerPanel, BorderLayout.CENTER);
		basePanel.add(southPanel, BorderLayout.SOUTH);
		centerPanel.add(westPanel, BorderLayout.WEST);
		centerPanel.add(eastPanel, BorderLayout.EAST);
		
		westPanel.setLayout(new FlowLayout());
		eastPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new FlowLayout());
		
		/* westPanel 컴포넌트 */
		westPanel.add(idL);
		westPanel.add(id);
		westPanel.add(pwL);
		westPanel.add(pw);
		
		/* eastPanel 컴포넌트 */
		eastPanel.add(loginBtn);
		eastPanel.add(adminBtn);
		/* southPanel 컴포넌트 */
		southPanel.add(idsL);
		southPanel.add(LL);
		southPanel.add(pwsL);
		southPanel.add(joinBtn);
		southPanel.add(exitBtn);
		backgroud.add(basePanel);
		/* Button 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();
		
		loginBtn.addActionListener(bl);
		exitBtn.addActionListener(bl);
		joinBtn.addActionListener(bl);
		adminBtn.addActionListener(bl); 
		/* Key 이벤트 리스너 추가 */
		KeyBoardListener kl = new KeyBoardListener();
		
		id.addKeyListener(kl);
		pw.addKeyListener(kl);
		basePanel.setOpaque(false);
		centerPanel.setOpaque(false);
		westPanel.setOpaque(false);
		eastPanel.setOpaque(false);
		southPanel.setOpaque(false);
		loginBtn.setBackground(new Color(181,157,84));
		adminBtn.setBackground(new Color(181,157,84));
		joinBtn.setBackground(new Color(181,157,84));
		exitBtn.setBackground(new Color(181,157,84));
		Font buttonFont = new Font("맑은 고딕", Font.BOLD, 12);

        // 버튼 배경색 설정
        

        // 버튼 스타일 적용
        JButton[] buttons = {loginBtn, adminBtn, joinBtn, exitBtn};
        for (JButton button : buttons) {
            button.setFont(buttonFont);
            
        }
		setSize(340, 190);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		idsL.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	JTextField nameField = new JTextField(10);
		    	
		        String[] years = new String[100];
		        for (int i = 0; i < 100; i++) {
		            years[i] = String.valueOf(2023 - i);
		        }
		        JComboBox<String> yearBox = new JComboBox<>(years);

		        JComboBox<String> monthBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
		        JComboBox<String> dayBox = new JComboBox<>();

		        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "직접입력"};
		        JComboBox<String> domainBox = new JComboBox<>(domains);
		        JTextField customDomainField = new JTextField(10);
		        customDomainField.setEnabled(false); // 텍스트 필드 숨기기

		        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        panel.setPreferredSize(new Dimension(300,150));
		        JPanel namePanel =new JPanel(new FlowLayout(FlowLayout.LEFT));
		        namePanel.setPreferredSize(new Dimension(300,30));
		        namePanel.add(new JLabel("이름:        "));
		        namePanel.add(nameField);
		        panel.add(namePanel);
		        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        datePanel.setPreferredSize(new Dimension(300,30));
		        datePanel.add(new JLabel("생년월일:"));
		        datePanel.add(yearBox);
		        datePanel.add(new JLabel("년"));
		        datePanel.add(monthBox);
		        datePanel.add(new JLabel("월"));
		        datePanel.add(dayBox);
		        datePanel.add(new JLabel("일"));
		        panel.add(datePanel);
		        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        emailPanel.setPreferredSize(new Dimension(300,60));
		        emailPanel.add(new JLabel("이메일:    "));
		        emailPanel.add(new JTextField(10));
		        emailPanel.add(new JLabel("@"));
		        emailPanel.add(domainBox);
		        JLabel empty = new JLabel("");
		        empty.setPreferredSize(new Dimension(180,20));
		        emailPanel.add(empty);
		        emailPanel.add(customDomainField); // 커스텀 도메인 입력 텍스트 필드 추가
		        
		        panel.add(emailPanel);
		        panel.setBackground(new Color(250,238,238));
		        emailPanel.setBackground(new Color(250,238,238));
		        datePanel.setBackground(new Color(250,238,238));
		        namePanel.setBackground(new Color(250,238,238));
		        
		        
		        domainBox.addActionListener(e1 -> {
		            String selectedDomain = (String) domainBox.getSelectedItem();
		            if (selectedDomain != null && selectedDomain.equals("직접입력")) {
		            	System.out.println("ㅎㅇ");
		                customDomainField.setEnabled(true); // 텍스트 필드 나타나기
		            } else {
		                customDomainField.setEnabled(false); // 텍스트 필드 숨기기
		            }
		        });


		        monthBox.addActionListener(e1 -> {
		            int selectedMonth = Integer.parseInt((String) Objects.requireNonNull(monthBox.getSelectedItem()));
		            int selectedYear = Integer.parseInt((String) Objects.requireNonNull(yearBox.getSelectedItem()));
		            int daysInMonth;
		            switch (selectedMonth) {
		                case 2: // February
		                    if ((selectedYear % 4 == 0 && selectedYear % 100 != 0) || selectedYear % 400 == 0)
		                        daysInMonth = 29;
		                    else
		                        daysInMonth = 28;
		                    break;
		                case 4: // April
		                case 6: // June
		                case 9: // September
		                case 11: // November
		                    daysInMonth = 30;
		                    break;
		                default:
		                    daysInMonth = 31;
		            }

		            dayBox.removeAllItems();
		            for (int i = 1; i <= daysInMonth; i++) {
		                dayBox.addItem(String.format("%d", i));
		            }
		            
		        });

		        int result = JOptionPane.showConfirmDialog(null, panel, "정보 입력",
		                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		        if (result == JOptionPane.OK_OPTION) {
		            String name = nameField.getText();
		            String year = (String) yearBox.getSelectedItem();
		            String month = (String) monthBox.getSelectedItem();
		            String day = (String) dayBox.getSelectedItem();
		            String emailID = ((JTextField) emailPanel.getComponent(1)).getText(); // 이메일 아이디 입력 필드 수정

		            String domain;
		            if (domainBox.getSelectedItem().equals("직접입력")) {
		                domain = customDomainField.getText();
		            } else {
		                domain = (String) domainBox.getSelectedItem();
		            }

		            String birthday = year + "/" + month + "/" + day;
		            String email = emailID + "@" + domain;

		            System.out.println("이름: " + name);
		            System.out.println("생년월일: " + birthday);
		            System.out.println("이메일: " + email);
		            c.sendMsg("getID"+"//"+name+"//"+birthday+"//"+email);
		            // 여기서 정보를 어떻게 활용할지 추가하는 로직을 작성할 수 있어요.
		        }
		    }
		});
		pwsL.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	JTextField nameField = new JTextField(10);
		    	JTextField idField = new JTextField(10);
		        String[] years = new String[100];
		        for (int i = 0; i < 100; i++) {
		            years[i] = String.valueOf(2023 - i);
		        }
		        JComboBox<String> yearBox = new JComboBox<>(years);

		        JComboBox<String> monthBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
		        JComboBox<String> dayBox = new JComboBox<>();

		        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "직접입력"};
		        JComboBox<String> domainBox = new JComboBox<>(domains);
		        JTextField customDomainField = new JTextField(10);
		        customDomainField.setEnabled(false); // 텍스트 필드 숨기기

		        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        panel.setPreferredSize(new Dimension(300,180));
		        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        idPanel.setPreferredSize(new Dimension(300,30));
		        idPanel.add(new JLabel("아이디:      "));
		        idPanel.add(nameField);
		        panel.add(idPanel);
		        JPanel namePanel =new JPanel(new FlowLayout(FlowLayout.LEFT));
		        namePanel.setPreferredSize(new Dimension(300,30));
		        namePanel.add(new JLabel("이름:        "));
		        namePanel.add(idField);
		        panel.add(namePanel);
		        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        datePanel.setPreferredSize(new Dimension(300,30));
		        datePanel.add(new JLabel("생년월일:"));
		        datePanel.add(yearBox);
		        datePanel.add(new JLabel("년"));
		        datePanel.add(monthBox);
		        datePanel.add(new JLabel("월"));
		        datePanel.add(dayBox);
		        datePanel.add(new JLabel("일"));
		        panel.add(datePanel);
		        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		        emailPanel.setPreferredSize(new Dimension(300,60));
		        emailPanel.add(new JLabel("이메일:    "));
		        emailPanel.add(new JTextField(10));
		        emailPanel.add(new JLabel("@"));
		        emailPanel.add(domainBox);
		        JLabel empty = new JLabel("");
		        empty.setPreferredSize(new Dimension(180,20));
		        emailPanel.add(empty);
		        emailPanel.add(customDomainField); // 커스텀 도메인 입력 텍스트 필드 추가
		        
		        panel.add(emailPanel);
		        panel.setBackground(new Color(250,238,238));
		        emailPanel.setBackground(new Color(250,238,238));
		        datePanel.setBackground(new Color(250,238,238));
		        namePanel.setBackground(new Color(250,238,238));
		        idPanel.setBackground(new Color(250,238,238));
		        domainBox.addActionListener(e1 -> {
		            String selectedDomain = (String) domainBox.getSelectedItem();
		            if (selectedDomain != null && selectedDomain.equals("직접입력")) {
		            	
		                customDomainField.setEnabled(true); // 텍스트 필드 나타나기
		            } else {
		                customDomainField.setEnabled(false); // 텍스트 필드 숨기기
		            }
		        });


		        monthBox.addActionListener(e1 -> {
		            int selectedMonth = Integer.parseInt((String) Objects.requireNonNull(monthBox.getSelectedItem()));
		            int selectedYear = Integer.parseInt((String) Objects.requireNonNull(yearBox.getSelectedItem()));
		            int daysInMonth;
		            switch (selectedMonth) {
		                case 2: // February
		                    if ((selectedYear % 4 == 0 && selectedYear % 100 != 0) || selectedYear % 400 == 0)
		                        daysInMonth = 29;
		                    else
		                        daysInMonth = 28;
		                    break;
		                case 4: // April
		                case 6: // June
		                case 9: // September
		                case 11: // November
		                    daysInMonth = 30;
		                    break;
		                default:
		                    daysInMonth = 31;
		            }

		            dayBox.removeAllItems();
		            for (int i = 1; i <= daysInMonth; i++) {
		                dayBox.addItem(String.format("%d", i));
		            }
		            
		        });

		        int result = JOptionPane.showConfirmDialog(null, panel, "정보 입력",
		                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		        if (result == JOptionPane.OK_OPTION) {
		        	String id = idField.getText();
		            String name = nameField.getText();
		            String year = (String) yearBox.getSelectedItem();
		            String month = (String) monthBox.getSelectedItem();
		            String day = (String) dayBox.getSelectedItem();
		            String emailID = ((JTextField) emailPanel.getComponent(1)).getText(); // 이메일 아이디 입력 필드 수정

		            String domain;
		            if (domainBox.getSelectedItem().equals("직접입력")) {
		                domain = customDomainField.getText();
		            } else {
		                domain = (String) domainBox.getSelectedItem();
		            }

		            String birthday = year + "/" + month + "/" + day;
		            String email = emailID + "@" + domain;
		            System.out.println("아이디 :"+ id);
		            System.out.println("이름: " + name);
		            System.out.println("생년월일: " + birthday);
		            System.out.println("이메일: " + email);
		            c.sendMsg("getpass"+"//"+id+"//"+name+"//"+birthday+"//"+email);
		            // 여기서 정보를 어떻게 활용할지 추가하는 로직을 작성할 수 있어요.
		        }
		    }
		});

		pwsL.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        // 클릭되었을 때 실행되는 코드 작성
		        System.out.println("Label clicked!");
		        // 원하는 동작 추가
		    }
		});
	}
	
	/* Button 이벤트 리스너 */
	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			/* TextField에 입력된 아이디와 비밀번호를 변수에 초기화 */
			String uid = id.getText();
			String upass = "";
			for(int i=0; i<pw.getPassword().length; i++) {
				upass = upass + pw.getPassword()[i];
			}
			
			/* 게임종료 버튼 이벤트 */
			if(b.getText().equals("게임종료")) {
				System.out.println("[Client] 게임 종료");
				System.exit(0);
			}
			
			/* 회원가입 버튼 이벤트 */
			else if(b.getText().equals("회원가입")) {
				System.out.println("[Client] 회원가입 인터페이스 열림");
				c.jf.setVisible(true);
			}
			
			/* 로그인 버튼 이벤트 */
			else if(b.getText().equals("로그인")) {
				if(uid.equals("") && !upass.equals("")) {	//아이디 미입력 시 로그인 시도 실패
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 로그인 실패 : 아이디 미입력");
				}
				
				else if(!uid.equals("") && upass.equals("")) {	//비밀번호 미입력 시 로그인 시도 실패
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 로그인 실패 : 비밀번호 미입력");
				}
				
				else if(!uid.equals("") && !upass.equals("")) {	//로그인 시도 성공
					
					c.sendMsg(loginTag + "//" + uid + "//" + upass);	//서버에 로그인 정보 전송
					
					c.sendMsg("getmynick//");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			 else if (b.getText().equals("관리자 창으로 이동")) {
		            String inputPassword = JOptionPane.showInputDialog(null, "관리자 비밀번호를 입력하세요:");
		            if (inputPassword != null && inputPassword.equals(adminPassword)) {
		                System.out.println("[Client] 관리자 창으로 이동");
		                c.adm.setVisible(true);  // 관리자 창 생성
		            } else {
		                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "인증 실패", JOptionPane.ERROR_MESSAGE);
		                System.out.println("[Client] 관리자 창으로 이동 실패 : 비밀번호 불일치");
		            }
		        }
		}
	}
	
	/* Key 이벤트 리스너 */
	class KeyBoardListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			/* Enter 키 이벤트 */
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				
				/* TextField에 입력된 아이디와 비밀번호를 변수에 초기화 */
				String uid = id.getText();
				String upass = "";
				for(int i=0; i<pw.getPassword().length; i++) {
					upass = upass + pw.getPassword()[i];
				}
				
				if(uid.equals("") && !upass.equals("")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 로그인 실패 : 아이디 미입력");
				}
				
				else if(!uid.equals("") && upass.equals("")) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 로그인 실패 : 비밀번호 미입력");
				} 
				
				else if(!uid.equals("") && !upass.equals("")) {
					c.sendMsg(loginTag + "//" + uid + "//" + upass);
				}
			}
		}
		public void keyTyped(KeyEvent e) {}		
		public void keyReleased(KeyEvent e) {}
		
	}
}