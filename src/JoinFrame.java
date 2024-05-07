import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//회원가입 기능을 수행하는 인터페이스
public class JoinFrame extends JFrame{
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
	
	/* Label */
	JLabel imageL = new JLabel();
	JLabel empty2L = new JLabel();
	JLabel nameL = new JLabel("이름");
	JLabel numL= new JLabel("전화번호");
	JLabel nicknameL = new JLabel("닉네임");
	JLabel idL = new JLabel("아이디");
	JLabel pwL = new JLabel("비밀번호");
	JLabel empty3L = new JLabel();
	JLabel pwaL = new JLabel("대문자,소문자,숫자를 포함한 8~16");
	JLabel emailL = new JLabel("이메일");
	JLabel expressL =  new JLabel("우편번호");
	JLabel addr2L=new JLabel("주소");
	JLabel addrL=new JLabel("상세주소");
	JLabel emailGL = new JLabel("@");
	JLabel empty = new JLabel("");
	JLabel ymd = new JLabel("생년월일");
	JLabel year = new JLabel("년");
	JLabel month = new JLabel("월");
	JLabel day = new JLabel("일");
	JLabel empty4L = new JLabel();
	JLabel empty5L = new JLabel();
	/* TextField */
	JTextField name = new JTextField();
	JTextField num = new JTextField();
	JTextField nickname = new JTextField();
	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();
	JTextField email = new JTextField();
	JTextField express= new JTextField();
	JTextField addr2= new JTextField();
	JTextField addr= new JTextField();
	JTextField emailDomain = new JTextField();
	 
	/* Button */
	JButton nnolBtn = new JButton("확인");
	JButton idolBtn = new JButton("확인");
	JButton expBtn= new JButton("우편 번호 찾기");
	JButton joinBtn = new JButton("가입하기");
	JButton cancelBtn = new JButton("가입취소");
	JButton selectImageBtn = new JButton("이미지 선택");
	JButton selectCa1=new JButton("캐릭터 1");
	JButton selectCa2=new JButton("캐릭터 2");
	Font currentFont = expBtn.getFont();
	
	Client c = null;
	
	final String joinTag = "JOIN";	//회원가입 기능 태그
	final String overTag = "OVER";	//중복확인 기능 태그
	String selectedYear="";
	String selectedMonth="";
	String selectedDay="";
	String selectedDomain ="";
	String uimage="";
	JoinFrame(Client _c) {
		c = _c;
		
		setTitle("회원가입");
		
		/* Label 크기 작업 */
		
		imageL.setPreferredSize(new Dimension(200, 200));
		imageL.setOpaque(true);
		imageL.setBackground(new Color(192, 192, 192));
		empty2L.setPreferredSize(new Dimension(60,30));
		nameL.setPreferredSize(new Dimension(60, 30));
		numL.setPreferredSize(new Dimension(60,30));
		nicknameL.setPreferredSize(new Dimension(60, 30));
		idL.setPreferredSize(new Dimension(60, 30));
		pwL.setPreferredSize(new Dimension(60, 30));
		empty3L.setPreferredSize(new Dimension(60,15));
		pwaL.setPreferredSize(new Dimension(210,15));
		pwaL.setFont(new Font(currentFont.getName(), currentFont.getStyle(),10 ));
		emailL.setPreferredSize(new Dimension(60, 30));
		addrL.setPreferredSize(new Dimension(60, 30));
		expressL.setPreferredSize(new Dimension(60, 30));
		addr2L.setPreferredSize(new Dimension(60, 30));
		emailGL.setPreferredSize(new Dimension(15,30));
		empty.setPreferredSize(new Dimension(190,30));
		empty4L.setPreferredSize(new Dimension(190,30));
		empty5L.setPreferredSize(new Dimension(100,25));
		ymd.setPreferredSize(new Dimension(60,30));
		year.setPreferredSize(new Dimension(15,30));
		month.setPreferredSize(new Dimension(15,30));
		day.setPreferredSize(new Dimension(15,30));
		/* TextField 크기 작업 */
		name.setPreferredSize(new Dimension(210, 30));
		num.setPreferredSize(new Dimension(210,30));
		nickname.setPreferredSize(new Dimension(145, 30));
		id.setPreferredSize(new Dimension(145, 30));
		pw.setPreferredSize(new Dimension(210, 30));
		email.setPreferredSize(new Dimension(100, 30));
		addr.setPreferredSize(new Dimension(210,30));
		addr2.setPreferredSize(new Dimension(210,30));
		express.setPreferredSize(new Dimension(115,30));
		/* Button 크기 작업 */
		nnolBtn.setPreferredSize(new Dimension(60, 30));
		idolBtn.setPreferredSize(new Dimension(60, 30));
		expBtn.setPreferredSize(new Dimension(90,30));
		expBtn.setFont(new Font(currentFont.getName(), currentFont.getStyle(),8 ));
		selectImageBtn.setPreferredSize(new Dimension(80,30));
		selectImageBtn.setFont(new Font(currentFont.getName(), currentFont.getStyle(),8 ));
		selectCa1.setPreferredSize(new Dimension(80,25));
		selectCa1.setFont(new Font(currentFont.getName(), currentFont.getStyle(),8 ));
		selectCa2.setPreferredSize(new Dimension(80,25));
		selectCa2.setFont(new Font(currentFont.getName(), currentFont.getStyle(),8 ));
		
		joinBtn.setPreferredSize(new Dimension(135, 30));
		cancelBtn.setPreferredSize(new Dimension(135, 30));
		
			String[] years = generateYears();
	        String[] months = generateMonths();
	        String[] days = generateDays(31);
	        JComboBox<String> yearComboBox = new JComboBox<>(years);
	        JComboBox<String> monthComboBox = new JComboBox<>(months);
	        JComboBox<String> dayComboBox = new JComboBox<>(days);
	        yearComboBox.setPreferredSize(new Dimension(65,30));
	        monthComboBox.setPreferredSize(new Dimension(40,30));
	        dayComboBox.setPreferredSize(new Dimension(40,30));
	        yearComboBox.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                 selectedYear = (String) yearComboBox.getSelectedItem();
	            }
	            
	        });
	        monthComboBox.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                int selectedMonthIndex = monthComboBox.getSelectedIndex();
	                int daysInMonth = getDaysInMonth(selectedMonthIndex + 1); // 월은 0부터 시작하므로 +1
	                String[] newDays = generateDays(daysInMonth);
	                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(newDays);
	                dayComboBox.setModel(model);
	                 selectedMonth = (String) monthComboBox.getSelectedItem();
	            }
	            
	        });
	        dayComboBox.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                 selectedDay = (String) dayComboBox.getSelectedItem();
	            }
	            
	        });
		//이메일 자동선택 기능
		 String[] emailDomains = {"직접 입력", "gmail.com", "naver.com", "yahoo.com", "example.com"};
		 JComboBox<String> domainComboBox = new JComboBox<>(emailDomains);
		
		 emailDomain.setPreferredSize(new Dimension(90, 30));
		 domainComboBox.setPreferredSize(new Dimension(90, 30));
	     domainComboBox.setEditable(true);
	     domainComboBox.setSelectedIndex(0);
	     domainComboBox.addActionListener(new ActionListener() {
	    	 private void updateEmailField() {
	    	         selectedDomain = (String) domainComboBox.getSelectedItem();
	    	        if (selectedDomain != null && selectedDomain.equals("직접 입력")) {
	    	            emailDomain.setEditable(true);
	    	        } else {
	    	            emailDomain.setEditable(false);
	    	            emailDomain.setText("");
	    	        }
	    	    }
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if (domainComboBox.getSelectedIndex() == 0) {
	                    // "직접 입력" 선택 시
	                    emailDomain.setEditable(true);
	                } else {
	                    emailDomain.setEditable(false);
	                    updateEmailField();
	                }
	            }
	        });
	     
		/* Panel 추가 작업 */
		setContentPane(panel);	//panel을 기본 컨테이너로 설정
		panel.add(imageL);
		panel.add(selectImageBtn);
		panel.add(selectCa1);
		panel.add(selectCa2);
		panel.add(empty5L);		
		panel.add(nameL);
		panel.add(name);
		
		panel.add(numL);
		panel.add(num);
		panel.add(ymd);
		panel.add(yearComboBox);
		panel.add(year);
		panel.add(monthComboBox);
		panel.add(month);
		panel.add(dayComboBox);
		panel.add(day);
		
		panel.add(nicknameL);
		panel.add(nickname);
		panel.add(nnolBtn);
		
		panel.add(idL);
		panel.add(id);
		panel.add(idolBtn);
		
		panel.add(pwL);
		panel.add(pw);
		panel.add(empty3L);
		panel.add(pwaL);
		
		panel.add(emailL);
		panel.add(email);
		panel.add(emailGL);
		panel.add(domainComboBox);
		panel.add(empty);
        panel.add(emailDomain);
		
		panel.add(expressL);
		panel.add(express);
		panel.add(expBtn);
		
		panel.add(addr2L);
		panel.add(addr2);
		
		panel.add(addrL);
		panel.add(addr);
		
		panel.add(cancelBtn);
		panel.add(joinBtn);
		
		/* Button 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();
		
		cancelBtn.addActionListener(bl);
		joinBtn.addActionListener(bl);
		
		/* Button 이벤트 추가 */
		nnolBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!nickname.getText().equals("")) {
					System.out.println("[Client] 닉네임 중복 확인");
					c.sendMsg(overTag + "//nickname//" + nickname.getText());	//서버에 태그와 함께 닉네임 전송
				}
			}
		});
		
		idolBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!id.getText().equals("")) {
					System.out.println("[Client] 아이디 중복 확인");
					c.sendMsg(overTag + "//id//" + id.getText());	//서버에 태그와 함께 아이디 전송
				}
			}
		});
		expBtn.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            // 주소 선택을 위한 팝업을 열기 위한 메소드 호출
	        	 JoinFrame.this.showAddressPopup();
	        }
	    });
		selectImageBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
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
		                System.out.println(uimage);
		                System.out.println(uimage.length());
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		            // 선택한 이미지를 JLabel에 설정
		           
		        }
		    }
		});
		selectCa1.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	File selectedFile = new File("profile/jinro2.jpeg");
	        	 ImageIcon imageIcon = new ImageIcon("profile/jinro2.jpeg");
		           
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
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
	        }
	    });
		selectCa2.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	 ImageIcon imageIcon = new ImageIcon("profile//jinro.jpeg");
	        	 File selectedFile = new File("profile/jinro.jpeg");
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
		                System.out.println(uimage.length());
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
	        }
	    });
		
		pw.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        isStrongPassword(new String(pw.getPassword()));
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        isStrongPassword(new String(pw.getPassword()));
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        isStrongPassword(new String(pw.getPassword()));
		    }
		    boolean isStrongPassword(String password) {
		    	boolean strongPassword = password.length() >= 8 && containsUppercase(password) && containsLowercase(password) && containsDigit(password);
		        if (strongPassword) {
		            pwaL.setForeground(Color.GREEN); // 안전한 비밀번호인 경우 라벨 글자색을 초록색으로 설정
		            pwaL.setText("비밀번호가 안전합니다.");
		        } else {
		            pwaL.setForeground(Color.RED); // 안전하지 않은 비밀번호인 경우 라벨 글자색을 빨간색으로 설정
		            pwaL.setText("비밀번호가 안전하지 않습니다.");
		        }
		        return strongPassword;
		        
		    }

		   boolean containsUppercase(String s) {
		        return !s.equals(s.toLowerCase());
		    }

		   boolean containsLowercase(String s) {
		        return !s.equals(s.toUpperCase());
		    }

		   boolean containsDigit(String s) {
		        for (char c : s.toCharArray()) {
		            if (Character.isDigit(c)) {
		                return true;
		            }
		        }
		        return false;
		    }
		});
		
		nnolBtn.setBackground(new Color(250,238,238));
		idolBtn.setBackground(new Color(250,238,238));
		expBtn.setBackground(new Color(250,238,238));
		joinBtn.setBackground(new Color(250,238,238));
		cancelBtn.setBackground(new Color(250,238,238));
		selectImageBtn.setBackground(new Color(250,238,238));
		selectCa1.setBackground(new Color(250,238,238));
		selectCa2.setBackground(new Color(250,238,238));
		setSize(310, 720);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	 private String[] generateYears() {
	        String[] years = new String[124]; // 1900부터 2023년까지
	        for (int i = 1900; i <= 2023; i++) {
	            years[i - 1900] = Integer.toString(i);
	        }
	        return years;
	    }

	    private String[] generateMonths() {
	        String[] months = new String[12]; // 1월부터 12월까지
	        for (int i = 0; i < 12; i++) {
	            months[i] = Integer.toString(i + 1);
	        }
	        return months;
	    }

	    private String[] generateDays(int maxDays) {
	        String[] days = new String[maxDays];
	        for (int i = 0; i < maxDays; i++) {
	            days[i] = Integer.toString(i + 1);
	        }
	        return days;
	    }

	    private int getDaysInMonth(int month) {
	        int days;
	        switch (month) {
	            case 2: // February
	                days = 28; // or 29 in a leap year
	                break;
	            case 4: // April
	            case 6: // June
	            case 9: // September
	            case 11: // November
	                days = 30;
	                break;
	            default:
	                days = 31;
	                break;
	        }
	        return days;
	    }
	
	/* Button 이벤트 리스너 */
	public void showAddressPopup() {
        // 팝업을 위한 JFrame 생성
        JFrame addressPopup = new JFrame("주소 검색");
        addressPopup.setSize(400, 300);
        addressPopup.setLocationRelativeTo(null);

        // 팝업을 위한 JPanel 생성
        JPanel popupPanel = new JPanel();

        // 사용자 입력을 위한 텍스트 필드 생성
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");

        // 주소를 저장하기 위한 리스트 생성
        DefaultListModel<String> addressListModel = new DefaultListModel<>();
        JList<String> addressList = new JList<>(addressListModel);

        // 주소 선택을 처리하기 위한 마우스 리스너 추가
        addressList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // 더블 클릭 이벤트
                    String selectedAddress = addressList.getSelectedValue();
                    if (selectedAddress != null) {
                        // 선택된 주소에서 express와 addr2 추출
                        String[] parts = selectedAddress.split("\\s+", 2);
                        express.setText(parts[0].split("/")[0]);
                        addr2.setText(parts[1].split("/")[0]);
                    }

                    // 팝업 닫기
                    addressPopup.dispose();
                }
            }
        });
        
        // 팝업 패널에 컴포넌트 추가
        popupPanel.add(searchField);
        popupPanel.add(searchButton);
        popupPanel.add(new JScrollPane(addressList));

        // 팝업 프레임에 패널 추가
        addressPopup.add(popupPanel);

        // 팝업 보이기
        addressPopup.setVisible(true);

        // 검색 버튼에 대한 액션 리스너 추가하여 주소 가져오기
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 사용자 입력을 기반으로 주소 가져오는 메소드 호출
                List<String> addresses = fetchAddresses(searchField.getText());
                // 가져온 주소로 리스트 모델 업데이트
                addressListModel.clear();
                for (String address : addresses) {
                    addressListModel.addElement(address);
                }
            }
        });
    }
    private List<String> fetchAddresses(String searchText) {
    	List<String> addresses = new ArrayList<>();

        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.epost.go.kr/postal/retrieveNewAdressAreaCdSearchAllService/retrieveNewAdressAreaCdSearchAllService/getNewAddressListAreaCdSearchAll"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=6t%2BrQN73JNGSc2ftjGGczgE40DdaWTT1kjY0B%2F7O0iYryXTPjwELZCn%2Bdo3qjS8FwrHLZFT%2BUsRVhz5h7W%2BoOw%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("srchwrd", "UTF-8") + "=" + URLEncoder.encode(searchText, "UTF-8")); /*검색어*/
            urlBuilder.append("&" + URLEncoder.encode("countPerPage", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*페이지당 출력될 개수를 지정(최대50)*/
            urlBuilder.append("&" + URLEncoder.encode("currentPage", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*출력될 페이지 번호*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            rd.close();
            conn.disconnect();

            String result = sb.toString();
            String[] parts = result.split("<zipNo>");

            for (int i = 1; i < parts.length; i++) {
                parts[i] = parts[i].replaceAll("[a-zA-Z]", "");
                parts[i] = parts[i].replaceAll("</>", "");
                String[] temp = parts[i].split("<>");
                String address = temp[0] + "/" + temp[1] + "/" + temp[2] + "/";
                addresses.add(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses;
    }
	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			/* TextField에 입력된 회원 정보들을 변수에 초기화 */
			String uname = name.getText();
			String unum =num.getText();
			String ubirth = selectedYear+"/"+selectedMonth+"/"+selectedDay;
			String unick = nickname.getText();
			String uid = id.getText();
			String upass = "";	
			String uemail ="";
			for(int i=0; i<pw.getPassword().length; i++) {
				upass = upass + pw.getPassword()[i];
			}
			if (emailDomain.getText().equals("")) {
				 uemail = email.getText()+"@"+selectedDomain;	
			}else {
				 uemail = email.getText()+"@"+emailDomain.getText();
			}
			
			String uexpress=express.getText();
			String uaddr=addr2.getText()+"/"+addr.getText();
			/* 가입취소 버튼 이벤트 */
			if(b.getText().equals("가입취소")) {
				System.out.println("[Client] 회원가입 인터페이스 종료");
				dispose();	//인터페이스 닫음
			}
			
			/* 가입하기 버튼 이벤트 */
			else if(b.getText().equals("가입하기")) {
				if(uname.equals("") ||unum.equals("") ||ubirth.equals("") || unick.equals("") || uid.equals("") || upass.equals("") || uemail.equals("")||uaddr.equals("")||uexpress.equals("")||uimage.equals("")) {
					//모든 정보가 입력되지 않으면 회원가입 시도 실패
					JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 회원가입 실패 : 회원정보 미입력");
				}if (isStrongPassword(upass)) {
                    // 비밀번호가 안전하면 회원가입 시도 성공
                    c.sendMsg(joinTag + "//" + uname + "//"+unum+"//"+ubirth+"//" + unick + "//" + uid + "//" + upass + "//" + uemail +"//"+uexpress+ "//" + uaddr+"//"+uimage);
                } else {
                    // 비밀번호가 안전하지 않음, 사용자에게 알림 표시
                    JOptionPane.showMessageDialog(null, "비밀번호가 안전하지 않습니다. 비밀번호는 최소한 8자 이상이어야 하며, 대문자, 소문자, 숫자를 포함해야 합니다.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[Client] 회원가입 실패: 비밀번호 미충족");
                }
			}
			
		
		}
		boolean isStrongPassword(String password) {
	        // 비밀번호는 최소한 8자 이상이어야 하며, 대문자, 소문자, 숫자를 포함해야 함
	        return password.length() >= 8 && containsUppercase(password) && containsLowercase(password) && containsDigit(password);
	    }

	   boolean containsUppercase(String s) {
	        return !s.equals(s.toLowerCase());
	    }

	   boolean containsLowercase(String s) {
	        return !s.equals(s.toUpperCase());
	    }

	   boolean containsDigit(String s) {
	        for (char c : s.toCharArray()) {
	            if (Character.isDigit(c)) {
	                return true;
	            }
	        }
	        return false;
	    }

		  
		    
	}
}