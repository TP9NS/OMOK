import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.border.*;
import java.util.HashSet;
public class GameFrame extends JFrame{
	ImageIcon tmp_img = new ImageIcon("imo/dda.jpg");
	Image temp_img = tmp_img.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	public ImageIcon thumbs_up = new ImageIcon(temp_img);
	
	ImageIcon tmp_img2 = new ImageIcon("imo/down.jpg");
	Image temp2_img = tmp_img2.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	public ImageIcon thumbs_down = new ImageIcon(temp2_img);
	
	ImageIcon tmp_img3 = new ImageIcon("imo/smile.png");
	Image temp3_img = tmp_img3.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	public ImageIcon smile = new ImageIcon(temp3_img);
	
	ImageIcon tmp_img4 = new ImageIcon("imo/fuck.jpeg");
	Image temp4_img = tmp_img4.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	public ImageIcon fuck = new ImageIcon(temp4_img);
	public class ChatCellRenderer extends DefaultListCellRenderer {
	    private String myNick;
	    Font font;
	    String myColor="Black";
	    String userColor="Black";
	    String sysColor="Black";
	   
	    public ChatCellRenderer(String myNick) {
	        this.myNick = myNick;
	    }
	    public void setmyNick(String mynick) {
	    	this.myNick=mynick;
	    }
	    public void setmyFont(Font font) {
	    	this.font = font;
	    }
	    public void setmyColor(String my,String user,String sys) {
	    	this.myColor=my;
	    	this.userColor=user;
	    	this.sysColor=sys;
	    }
	    @Override
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    	
	          JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	           String message = value.toString();
	           String[] parts = message.split(":", 3); // 최대 2개로 나누어 닉네임 부분만 제거합니다.마지막 3번째 부분은 날짜정보

	           if (parts.length == 3) {
	               String sender = parts[0].trim();
	               String content = parts[1].trim(); // 닉네임을 제외한 채팅 내용
	           
	               String dateInfo = parts[2].trim();//시간
	               if (sender.equals(myNick)) {
	            	   if(myColor.equals("Red")) {
	            		   label.setForeground(Color.RED);
	            	   }else if(myColor.equals("Green")) {
	            		   label.setForeground(Color.GREEN);
	            	   }else if(myColor.equals("Yellow")) {
	            		   label.setForeground(Color.RED);
	            	   }else if(myColor.equals("Black")) {
	            		   label.setForeground(Color.BLACK);
	            	   }else if(myColor.equals("Blue")) {
	            		   label.setForeground(Color.BLUE);
	            	   }
	            	   
	            	   label.setFont(font);
	                   if (content.isEmpty()) {
	                       label.setText(""); // 내 채팅이고 내용이 없을 경우 빈 문자열로 설정하여 표시하지 않습니다.
	                   } else if(content.equals("/bb")){
	                	   label.setText("");   	   
	                       label.setIcon(thumbs_up);      
	                       label.setIconTextGap(5);
	                   }else if(content.equals("/pp")){
		                	   label.setText("");   	   
		                       label.setIcon(thumbs_down);      
		                       label.setIconTextGap(5);
	                   }else if(content.equals("/ㅗ")){
	                	   label.setText("");   	   
	                       label.setIcon(fuck);      
	                       label.setIconTextGap(5);
                   }else if(content.equals("/^^")){
                	   label.setText("");   	   
                       label.setIcon(smile);      
                       label.setIconTextGap(5);
               }
	                   else{
	                       label.setText(content); // 내 채팅이고 채팅 내용만 표시합니다.
	                   }
	                   label.setHorizontalAlignment(SwingConstants.RIGHT); // 내 채팅일 경우 오른쪽 정렬
	                   label.setToolTipText(dateInfo);
	                   
	               } else {
	            	 
	            	   if(userColor.equals("Red")) {
	            		   label.setForeground(Color.RED);
	            	   }else if(userColor.equals("Green")) {
	            		   label.setForeground(Color.GREEN);
	            	   }else if(userColor.equals("Yellow")) {
	            		   label.setForeground(Color.RED);
	            	   }else if(userColor.equals("Black")) {
	            		   label.setForeground(Color.BLACK);
	            	   }else if(userColor.equals("Blue")) {
	            		   label.setForeground(Color.BLUE);
	            	   }
	            	   String a[] = content.split(">");
	            	   System.out.println(a.length);
	            	   if(a[0].equals(myNick)&a.length==2) {
	            		   label.setText(sender+"님의 귓속말:"+a[1]);
	            		   label.setToolTipText(dateInfo);
	            		   label.setForeground(Color.PINK);
	            		   label.setHorizontalAlignment(SwingConstants.LEFT); 
	            	   }else {
	            		   if(a.length==2) {
	            			   label.setText("");
	            			   return label;
	            		   }else {
	            	   label.setFont(font);
	                   label.setText(sender+":"+content); // 내 채팅이 아니면 전체 메시지를 표시합니다.
	                   if(content.equals("/bb")){
	                	   label.setText(sender+":");
	                       label.setIcon(thumbs_up);
	                       label.setHorizontalTextPosition(SwingConstants.LEFT); 
	                       label.setIconTextGap(5);
	            	   }else if(content.equals("/pp")){
	                	   label.setText(sender+":");
	                       label.setIcon(thumbs_down);
	                       label.setHorizontalTextPosition(SwingConstants.LEFT); 
	                       label.setIconTextGap(5);
	            	   }else if(content.equals("/ㅗ")){
	                	   label.setText(sender+":");
	                       label.setIcon(fuck);
	                       label.setHorizontalTextPosition(SwingConstants.LEFT); 
	                       label.setIconTextGap(5);
	            	   }else if(content.equals("/^^")){
	                	   label.setText(sender+":");
	                       label.setIcon(smile);
	                       label.setHorizontalTextPosition(SwingConstants.LEFT); 
	                       label.setIconTextGap(5);
	            	   }
	                   label.setHorizontalAlignment(SwingConstants.LEFT); // 상대방 채팅일 경우 왼쪽 정렬
	                   label.setToolTipText(dateInfo);
	               }
	            }
	            }
	           }
	           
	           return label;
	       }
	   
	}
	Clip clip ;
	public void playBGM(String selectbgm,int setvolume) {
        try {
        	
            String bgm = selectbgm;
             File audioFile = new File("bgm/"+bgm+".wav");
             AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

             clip = AudioSystem.getClip();
             clip.open(audioStream);

             FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

             int startVolume = setvolume;
             float volume = percentageToVolume(startVolume);

             //볼륨조절 기능
             volumeControl.setValue(volume);

             clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP ) {
                        clip.setMicrosecondPosition(0);
                        clip.stop();
                    }
                });

             clip.start();
        }catch(UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
            System.out.println("BGM불러오기 실패");
        }
    }
	public void stopBGM() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
	public float percentageToVolume(int percentage) {
        // Convert percentage to a volume value within the allowable range
        float minVolume = -60.0f;
        float maxVolume = 6.0206f;
        float range = maxVolume - minVolume;
        return ((percentage / 100.0f) * range) + minVolume;
    }
	

      
	/* Panel */
	JPanel basePanel = new JPanel(new BorderLayout()) {
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
	JPanel centerPanel = new JPanel();
	JPanel eastPanel = new JPanel() {
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
	
	/* List */
	DefaultListModel<String> userListModel = new DefaultListModel<>();
	DefaultListModel<String> seeuserListModel = new DefaultListModel<>();
	JList<String> userList =new JList<>(userListModel);
	JList<String> seeuserList =new JList<>(seeuserListModel);
	
	DefaultListModel<String> chatListModel = new DefaultListModel<>();
	//채팅 
	JList<String> chatList = new JList<>(chatListModel);
	
	JScrollPane scrollPane = new JScrollPane(chatList);
	JTextField textField = new JTextField(25);
	JButton sendButton = new JButton("전송");
	
	/* Label */
	JLabel la1 = new JLabel();
	JLabel la2 = new JLabel();
	JLabel la3 = new JLabel();
	JLabel la4 = new JLabel();
	JLabel imageLabel1= new JLabel();
	JLabel imageLabel2= new JLabel();
	JLabel player1L = new JLabel("player1");
	JLabel player2L = new JLabel("player2");
	JLabel userListL = new JLabel("참가자 목록");
	JLabel seeuserListL = new JLabel("관전자 목록");
	JLabel enableL = new JLabel();
	
	/* Button */
	JButton searchBtn = new JButton("전적검색");
	JButton imoBtn= new JButton("^-^");
	JButton loseBtn = new JButton("방나가기");
	JButton readyBtn = new JButton("준비");
	JButton changeBtn = new JButton("관전하기");
	
	String selUser;	//선택된 사용자
	String checkseeorgame = "game";
	String gamestate = "wait";
	String dc = "";	//돌 색깔
	int col;		//돌 색깔
	
	int omok[][] = new int[20][20];	//오목 위치 배열
	boolean enable = false;	//돌을 둘 수 있는지 여부
	
	Client c = null;
	
	final String searchTag = "SEARCH";	//전적 조회 기능 태그
	final String rexitTag = "REXIT";	//방 퇴장 기능 태그
	final String omokTag = "OMOK";		//오목 기능 태그
	final String blackTag = "BLACK";	//검정색 돌 태그
	final String whiteTag = "WHITE";	//흰색 돌 태그
	final String winTag = "WIN";		//승리 태그
	final String loseTag = "LOSE";		//패배 태그
	final String chatTag = "CHAT";
	 String othernick="";
	 String mynick="";
	 boolean wait = false;
	 
	 ChatCellRenderer cellRenderer = new ChatCellRenderer(mynick);
	GameFrame(Client _c) {
		c = _c;
		
		//text필드 크기 작업
		textField.setPreferredSize(new Dimension(120,30));
		// 스크롤 pane 크키 작업
		scrollPane.setPreferredSize(new Dimension(230,150));
		/* List 크기 작업 */
		userList.setPreferredSize(new Dimension(140, 38));
		seeuserList.setPreferredSize(new Dimension(140, 38));
		chatList.setPreferredSize(new Dimension(180,350));
		/* Label 크기 작업 */
		la1.setPreferredSize(new Dimension(250, 30));
		userListL.setPreferredSize(new Dimension(80, 20));
		userListL.setHorizontalAlignment(JLabel.LEFT);
		seeuserListL.setPreferredSize(new Dimension(80, 20));
		seeuserListL.setHorizontalAlignment(JLabel.LEFT);
		la2.setPreferredSize(new Dimension(155, 20));
		la4.setPreferredSize(new Dimension(155, 20));
		enableL.setPreferredSize(new Dimension(235, 38));
		
		enableL.setForeground(Color.RED);
		la3.setPreferredSize(new Dimension(250, 70));
		imageLabel1.setPreferredSize(new Dimension(100, 100));
		imageLabel2.setPreferredSize(new Dimension(100, 100));
		player1L.setPreferredSize(new Dimension(100,15));
		player2L.setPreferredSize(new Dimension(100,15));
		/* Button 크기 작업 */
		searchBtn.setPreferredSize(new Dimension(90, 47));
		changeBtn.setPreferredSize(new Dimension(90, 47));
		loseBtn.setPreferredSize(new Dimension(235, 27));
		readyBtn.setPreferredSize(new Dimension(235, 27));
		readyBtn.setBackground(Color.green);
		imoBtn.setPreferredSize(new Dimension(30,30));
		/* Panel 추가 작업 */
		setContentPane(basePanel);	//panel을 기본 컨테이너로 설정
		
		centerPanel.setPreferredSize(new Dimension(625, 652));
		centerPanel.setLayout(new FlowLayout());
		
		eastPanel.setPreferredSize(new Dimension(250, 652));
		eastPanel.setLayout(new FlowLayout());
		
		centerPanel.setBackground(new Color(206,167,61));
		centerPanel.setLayout(null);
		
		basePanel.add(centerPanel, BorderLayout.CENTER);
		basePanel.add(eastPanel, BorderLayout.EAST);
		
		
		eastPanel.add(userListL);
		eastPanel.add(la2);
		eastPanel.add(userList);
		eastPanel.add(searchBtn);
		eastPanel.add(seeuserListL);
		eastPanel.add(la4);
		eastPanel.add(seeuserList);
		eastPanel.add(changeBtn);
		eastPanel.add(imageLabel1);
		eastPanel.add(imageLabel2);
		eastPanel.add(player1L);
		eastPanel.add(player2L);
		eastPanel.add(enableL);
		eastPanel.add(readyBtn);
		eastPanel.add(loseBtn);
		eastPanel.add(scrollPane);
		eastPanel.add(textField);
		eastPanel.add(sendButton);
		/* Button 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();
		loseBtn.addActionListener(bl);
		searchBtn.addActionListener(bl);
		sendButton.addActionListener(bl);
		readyBtn.addActionListener(bl);
		changeBtn.addActionListener(bl);
		/* Mouse 이벤트 리스너 추가 */
		DolAction da = new DolAction();
		centerPanel.addMouseListener(da);
		
		
		
		/* Mouse 이벤트 추가 */
		userList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!userList.isSelectionEmpty()) {
					String[] m = userList.getSelectedValue().split(" : ");
					selUser = m[0];
				}
			}
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
			
		});
		
		setSize(885, 652);
		setResizable(true);
		setLocationRelativeTo(null);
	}
	
	  
	@Override
	public void paint(Graphics g) {	//panel에 그리기 작업
		super.paintComponents(g);
		g.setColor(Color.BLACK);
		
		for(int i=1; i<=20; i++) {
			g.drawLine(30, i*30+20, 30*20, i*30+20);	//가로 줄 그리기
			g.drawLine(i*30, 50, i*30, 30*20+20);	//세로 줄 그리기
		}
		
		drawdol(g);	//돌 그리기
	}
	void repaintBoard() {
	    Graphics g = getGraphics(); // 그래픽 컨텍스트를 가져옵니다.
	    if (g != null) {
	        drawdol(g); // 돌을 그리는 메소드 호출
	    }
	    g.dispose(); // 그래픽 컨텍스트를 해제합니다.
	} 
	void drawdol(Graphics g) {	//돌 그리기 작업
		for(int i=0; i<20; i++){
			for(int j=0;j<20;j++){
				if(omok[j][i]==1) {			//1일 때 검정 돌
					g.setColor(Color.BLACK);
					g.fillOval((i+1)*30-12, (j)*30+37, 25, 25);
				}
				else if(omok[j][i]==2) {	//2일 때 흰 돌
					g.setColor(Color.WHITE);
					g.fillOval((i+1)*30-12, (j)*30+37, 25, 25);
				}
			}
		}
	}
	
	void remove() {	//돌 초기화 작업
		for(int i=0; i<20; i++) {
			for(int j=0;j<20;j++) {
				omok[i][j] = 0;
			}
		}
		repaint();
	}
	
	/* Button 이벤트 리스너 */
	
	class ButtonListener implements ActionListener{
		@Override
		
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			/* 전적검색 버튼 이벤트 */
			if(b.getText().equals("전적검색")) {
				if(selUser != null) {	//selUser가 null이 아니면 서버에 "태그//닉네임" 형태의 메시지를 전송
					c.sendMsg(searchTag + "//" + selUser);
				} else {				//selUser가 null이면 전적검색 시도 실패
					JOptionPane.showMessageDialog(null, "검색할 닉네임을 선택해주세요", "검색 실패", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(b.getText().equals("준비")) {
				 System.out.println(mynick);
				readyBtn.setText("준비 취소");
				c.sendMsg("ready//"+mynick);
				readyBtn.setBackground(Color.RED);
			}
			else if(b.getText().equals("준비 취소")) {
				 System.out.println("준비취소 버튼 클릭됨");
				readyBtn.setText("준비");
				readyBtn.setBackground(Color.GREEN);
				c.sendMsg("readycancel//"+mynick);
			}
			
			/* 기권하기 버튼 이벤트 */
			else if(b.getText().equals("기권하기")) {
				String chatR = chatRecord();
				c.sendMsg("getnick//");
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(mynick+othernick);
				
				if(chatR !="" & othernick !="") {
					LocalDateTime endTime = LocalDateTime.now();
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			        String time = endTime.format(formatter);
					c.sendMsg(loseTag + "//"+mynick+"//"+othernick+"//"+chatR+"//"+time);
					}else{
						c.sendMsg(loseTag+"//"+"nochat");
						}
				c.stopBGM();
				c.playBGM(c.loby,c.lobyV);
				//서버에 패배 태그 전송
				dispose();					//인터페이스 닫음
				chatListModel.removeAllElements();
				c.mf.setVisible(true);
			}
			else if(b.getText().equals("방나가기")) {
				c.sendMsg(rexitTag + "//");
				c.sendMsg("refreshRoom//");
				dispose();					
				c.stopBGM();
				c.playBGM(c.loby,c.lobyV);
				chatListModel.removeAllElements();
				c.mf.setVisible(true);
			}
			//서버에 메세지 전송
			else if(b.getText().equals("전송")){
				
				// JList에 셀 렌더러를 설정합니다.
				cellRenderer.setmyNick(mynick);
				chatList.setCellRenderer(cellRenderer);
				
				String message = textField.getText();
                if (!message.isEmpty()) {
                	c.sendMsg(chatTag+"//"+message);
                    textField.setText(""); // 입력 필드 지우기
                   
                }
            
			}else if (b.getText().equals("관전하기")) {
				if(gamestate.equals("wait")) { 
				if (userListModel.contains(mynick)) {
				        c.sendMsg("chggametosee"+"//"+mynick);
				 }
				 enableL.setText("관전 중 입니다");
				 readyBtn.setVisible(false);
				 enable = false;
				 checkseeorgame="see";
				 changeBtn.setText("게임하기");
				}else {
					JOptionPane.showMessageDialog(null, "게임중에는 변경 하실 수 없습니다..", "방입장", JOptionPane.ERROR_MESSAGE);
				}
			}else if (b.getText().equals("게임하기")) {
				 if (seeuserListModel.contains(mynick)&userListModel.getSize()<2) {
				        c.sendMsg("chgseetogame"+"//"+mynick);
				 
				 enableL.setText("");
				 readyBtn.setVisible(true);
				 enable = false;
				 checkseeorgame="game";
				 if(player1L.getText().equals("player1")) {
					 dc = blackTag;
				 }else {
					 dc=whiteTag;
				 }
				 changeBtn.setText("관전하기");
				 }else {
					 JOptionPane.showMessageDialog(null, "플레이어가 가득 차 player가 될 수 없습니다.", "방입장", JOptionPane.ERROR_MESSAGE);
				 }
			}
		}
	}
	
	/* Mouse 이벤트 리스너 : 돌 올릴 위치 선정 */
	class DolAction implements MouseListener{
		@Override
		public void mousePressed(MouseEvent e) {
			if(!enable) return;		//누를 수 없으면 return
			playBGM("ddak",c.sysV);
			//각 좌표 계산
			int x = (int)(Math.round(e.getX() / (double)30) - 1);
			int y = (int)(Math.round(e.getY() / (double)30) - 1);
			
			if(x<0 || x>19 || y<0 || y>19) return;			//둘 수 없는 위치면 return
			if(omok[y][x] == 1 || omok[y][x] == 2) return;	//다른 돌이 있으면 return
			
			System.out.println("[Client] 돌을 (" + x + ", " + y + ")에 두었습니다");	//돌을 둔 위치를 알림

			if(dc.equals(blackTag)) {	//검정색 태그면 1
				omok[y][x] = 1;
				col = 1;
			} else {					//흰색 태그면 2
				omok[y][x] = 2;
				col = 2;
				
			}
			c.sendMsg(omokTag + "//" + x + "//" + y + "//" + dc);	//서버에 오목 태그, 좌표, 돌 색깔을 전송
			
			repaint();
			
			if(check(new Point(x, y), col)) {	//이겼는지 확인. true면 서버에 승리 태그 전송
				String chatR = chatRecord();
				 // 끝난 시간 기록
				LocalDateTime endTime = LocalDateTime.now();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        String time = endTime.format(formatter);
				c.sendMsg("getnick//");
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(mynick+othernick);
				
				if(chatR !="") {
					c.sendMsg(winTag + "//"+mynick+"//"+othernick+"//"+chatR+"//"+time);
					}else{
						c.sendMsg(winTag+"//"+"nochat");
						}
				
				JOptionPane.showMessageDialog(null, "게임에 승리하였습니다", "승리", JOptionPane.INFORMATION_MESSAGE);
				remove();
				dispose();	//인터페이스 닫음
				c.stopBGM();
				c.playBGM(c.loby,c.lobyV);
				chatListModel.removeAllElements();
				c.mf.setVisible(true);
				
				}
			
			enable = false;	//돌을 두면 false로 바꿈
			if(!checkseeorgame.equals("see")) {
			enableL.setText("상대가 두기를 기다리는 중...");	//본인 차례인지 아닌지 알려줌
			}
		}
		public void mouseClicked(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
	}
	
	String chatRecord() {
		StringBuilder chatHistory = new StringBuilder();
		for (int i = 0; i < chatListModel.size(); i++) {
		    String chatMessage = chatListModel.getElementAt(i);
		    chatHistory.append(chatMessage).append("\n");
		}
		String allChatHistory="";
		return allChatHistory = chatHistory.toString();
		
	}
	//승리 여부를 확인하는 메소드. 승리 시 true, 승리가 아니면 false를 반환
	boolean check(Point p, int c) {
		/* 돌을 올린 위치의 가로, 세로, 대각선에 같은 색의 돌이 연달아 4개가 있으면 true를 반환 */
		if(count(p, 1, 0, c) + count(p, -1, 0, c) == 4) {	//가로
			return true;
		}
		
		if(count(p, 0, 1, c) + count(p, 0, -1, c) == 4) {	//세로
			return true;
		}
		
		if(count(p, -1, -1, c) + count(p, 1, 1, c) == 4) {	//오른쪽 대각선
			return true;
		}
		
		if(count(p, 1, -1, c) + count(p, -1, 1, c) == 4) {	//왼쪽대각선
			return true;
		}
		
		return false;
	}
	
	//특정 위치에 같은 색의 돌이 있는지 확인하는 메소드.
	int count(Point p, int _x, int _y, int c) {
		int i=0;
		//omok[p.y+(i+1)*_y][p.x+(i+1)*_x]==c가 true면 i가 무한대로 증가한다.
		for(i=0; omok[p.y+(i+1)*_y][p.x+(i+1)*_x]==c; i++);
		return i;
	}
	 void removeDuplicates(DefaultListModel<String> model) {
	        HashSet<String> uniqueUsers = new HashSet<>();
	        
	        // DefaultListModel에 있는 요소들을 HashSet에 추가
	        for (int i = 0; i < model.size(); i++) {
	            uniqueUsers.add(model.getElementAt(i));
	        }
	        
	        model.clear();
	        
	        // 중복이 없는 요소들을 다시 DefaultListModel에 추가
	        for (String user : uniqueUsers) {
	            model.addElement(user);
	        }
	    }
}