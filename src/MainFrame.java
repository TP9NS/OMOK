import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//대기실 기능을 수행하는 인터페이스
public class MainFrame extends JFrame{
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
	 boolean imostate=true;
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
	/* Panel */
	JPanel basePanel =  new JPanel(new BorderLayout()) {
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
	JPanel centerPanel =  new JPanel() {
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
    JPanel imoPanel =  new JPanel(new GridLayout(1,6)) {
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
    JLabel dda = new JLabel(thumbs_up) ; 
    JLabel down = new JLabel(thumbs_down);
   JLabel ff= new JLabel(fuck);
   JLabel sm=new JLabel(smile);
   
    
	JPanel eastPanel =  new JPanel() {
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
	JLabel roomListL = new JLabel("============ 생성된 방 목록 =============");
	JLabel cuListL = new JLabel("==== 접속중인 인원 ====");
	JLabel chatL = new JLabel("============== 전체 채팅  ===============");
	/* ScrollPane */
	JScrollPane rL_sp;
	JScrollPane cL_sp;
	JScrollPane chatSP;
	/* List */
	JList<String> rList = new JList<String>();
	JList<String> cuList = new JList<String>();
	List<String> nicknameList;
	String mynick;
	DefaultListModel<String> chatListModel = new DefaultListModel<>();
	JList<String> chatList = new JList<String>(chatListModel);
	/*text*/
	JTextField chatField = new JTextField(20);
	/* Menu */
	JMenuBar mb = new JMenuBar();
	JMenu infoMenu = new JMenu("설정");
	JMenuItem viewInfo = new JMenuItem("내 정보 보기");
	JMenuItem changeInfo = new JMenuItem("내 정보 바꾸기");
	JMenuItem setting = new JMenuItem("환경설정");
	/* Button */
	JButton viewRanking = new JButton("전적 보기");
	JButton createRoom = new JButton("방 생성하기");
	JButton enterRoom = new JButton("방 입장하기");
	JButton seeRoom =new JButton("관전하기");
	JButton exitGame = new JButton("게임 종료하기");
	JButton replay = new JButton("리플레이");
	JButton imoBtn = new JButton("^-^");
	String selRoom;		//선택된 방 제목
	String roomName;	//생성할 방 제목
	public Clip clip;
	Client c = null;
	
	 public ArrayList<int[]> test = new ArrayList<>();
	 String player1="";
	 String player2="";
	 String win = "";
	final String croomTag = "CROOM";	//방 생성 기능 태그
	final String eroomTag = "EROOM";	//방 입장 기능 태그
	final String rankTag = "RANK";		//전적 조회 기능 태그
	final String pexitTag = "PEXIT";	//프로그램 종료 기능 태그
	Font currentFont = enterRoom.getFont();
	 ChatCellRenderer cellRenderer = new ChatCellRenderer(mynick);
	MainFrame(Client _c) {
	
		c = _c;

		setTitle("대기실");
	
		/* Menu */
		infoMenu.add(viewInfo);
		infoMenu.addSeparator();
		infoMenu.add(changeInfo);
		infoMenu.addSeparator();
		infoMenu.add(setting);
		mb.add(infoMenu);
		setJMenuBar(mb);
		
		/* Panel 크기 작업 */
		centerPanel.setPreferredSize(new Dimension(310, basePanel.getHeight()));
		eastPanel.setPreferredSize(new Dimension(180, basePanel.getHeight()));
		
		/* Label 크기 작업 */
		roomListL.setPreferredSize(new Dimension(290, 20));
		cuListL.setPreferredSize(new Dimension(160, 20));
		
		/* ScrollPane 크기 작업 */
		rL_sp = new JScrollPane(rList);
		cL_sp = new JScrollPane(cuList);
		chatSP=new JScrollPane(chatList);
		rL_sp.setPreferredSize(new Dimension(300, 150));
		cL_sp.setPreferredSize(new Dimension(160, 150));
		chatSP.setPreferredSize(new Dimension(300,150));
		/* Button 크기 작업 */
		viewRanking.setPreferredSize(new Dimension(160, 30));
		createRoom.setPreferredSize(new Dimension(160, 30));
		enterRoom.setPreferredSize(new Dimension(160, 30));
		seeRoom.setPreferredSize(new Dimension(160, 30));
		replay.setPreferredSize(new Dimension(160,30));
		exitGame.setPreferredSize(new Dimension(160, 30));
		imoBtn.setPreferredSize(new Dimension(30,30));
		/* Panel 추가 작업 */
		setContentPane(basePanel);	//panel을 기본 컨테이너로 설정
		
		basePanel.add(centerPanel, BorderLayout.CENTER);
		basePanel.add(eastPanel, BorderLayout.EAST);
		
		centerPanel.setLayout(new FlowLayout());
		eastPanel.setLayout(new FlowLayout());
		
		centerPanel.add(roomListL);
		centerPanel.add(rL_sp);
		centerPanel.add(chatL);		
		centerPanel.add(chatSP);
		
		imoPanel.add(dda);
		 imoPanel.add(down); 
		 imoPanel.add(ff);
		 imoPanel.add(sm);
		 imoPanel.setPreferredSize(new Dimension(200,40));
		chatField.setPreferredSize(new Dimension(250,30));
		JButton sendButton = new JButton("전송");
		sendButton.setPreferredSize(new Dimension(50,30));
		sendButton.setFont(new Font(currentFont.getName(), currentFont.getStyle(),8 ));
		centerPanel.add(imoBtn);
		centerPanel.add(chatField);
		centerPanel.add(sendButton);
		centerPanel.add(imoPanel);
		/* eastPanel 컴포넌트 */
		eastPanel.add(cuListL);
		eastPanel.add(cL_sp);
		eastPanel.add(viewRanking);
		eastPanel.add(createRoom);
		eastPanel.add(enterRoom);
		eastPanel.add(seeRoom);
		eastPanel.add(replay);
		eastPanel.add(exitGame);
		
		/* MenuItem 이벤트 리스너 추가 */
		MenuItemListener mil = new MenuItemListener();
		
		viewInfo.addActionListener(mil);
		changeInfo.addActionListener(mil);
		setting.addActionListener(mil);
		/* Button 이벤트 리스너 추가 */
		ButtonListener bl = new ButtonListener();
		imoBtn.addActionListener(bl);
		viewRanking.addActionListener(bl);
		createRoom.addActionListener(bl);
		enterRoom.addActionListener(bl);
		seeRoom.addActionListener(bl);
		replay.addActionListener(bl);
		exitGame.addActionListener(bl);
		viewRanking.setBackground(new Color(250,238,238));
		createRoom.setBackground(new Color(250,238,238));
		enterRoom.setBackground(new Color(250,238,238));
		seeRoom.setBackground(new Color(250,238,238));
		replay.setBackground(new Color(250,238,238));
		exitGame.setBackground(new Color(250,238,238));
		imoBtn.setBackground(new Color(250,238,238));
		sendButton.setBackground(new Color(250,238,238));
		/* Mouse 이벤트 추가 */
		rList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!rList.isSelectionEmpty()) {
					String[] m = rList.getSelectedValue().split(" : ");
					selRoom = m[0];
				}
			}
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
		});
		 sendButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(!chatField.getText().equals("")) {
	            	c.sendMsg("allchat"+"//"+chatField.getText());
					chatField.setText("");
	            }
	            }
	        });
		 dda.addMouseListener(new MouseAdapter() {
	            
	            public void mouseClicked(MouseEvent e) {
	               Date date = new Date();
	             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String formatDate = formatter.format(date);
	               c.sendMsg("allchat"+"//"+"/bb");
	            }
	        });
		 down.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	               Date date = new Date();
	             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String formatDate = formatter.format(date);
	               c.sendMsg("allchat"+"//"+"/pp");
	            }
	        });
		 sm.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	               Date date = new Date();
	             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String formatDate = formatter.format(date);
	               c.sendMsg("allchat"+"//"+"/^^");
	            }
	        });
		 ff.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	               Date date = new Date();
	             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String formatDate = formatter.format(date);
	               c.sendMsg("allchat"+"//"+"/ㅗ");
	            }
	        });
		setSize(510, 495);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/* Button 이벤트 리스너 */
	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			/* 전적 보기 버튼 이벤트 */
			if(b.getText().equals("전적 보기")) {
				c.rf.setVisible(true);
				System.out.println("[Client] 전적 조회 인터페이스 열림");
				c.sendMsg(rankTag + "//");	//서버에 전적 조회 태그 전송
			}
			else /* 전적 보기 버튼 이벤트 */
				if(b.getText().equals("^-^")) {
					if(imostate) {
						imoPanel.setVisible(false);
						imostate=false;
					}else {
						imoPanel.setVisible(true);
						imostate=true;
					}
					
				}
			
			/* 방 생성하기 버튼 이벤트 */
			else if(b.getText().equals("방 생성하기")) {
				//생성할 방 제목을 입력받음
				roomName = JOptionPane.showInputDialog(null, "생성할 방 제목을 입력하시오", "방 생성", JOptionPane.QUESTION_MESSAGE);
				
				if(!roomName.equals("")) {	//roomName이 null이 아니면 서버에 "태그//방이름" 형태의 메시지를 전송
					c.sendMsg(croomTag + "//" + roomName);
					c.sendMsg("getmynick//");
				} else {				//roomName이 null이면 방 생성 시도 실패
					JOptionPane.showMessageDialog(null, "방 제목이 입력되지 않았습니다", "생성 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 방 생성 오류 : 입력 값이 존재하지 않음");
				}
			}
			
			
			/* 방 입장하기 버튼 이벤트 */
			else if(b.getText().equals("방 입장하기")) {
				if(selRoom != null) {	//selRoom이 null이 아니면 서버에 "태그//방이름" 형태의 메시지를 전송
					c.sendMsg(eroomTag + "//" + selRoom);
					c.sendMsg("getmynick//");
				} else {				//selRoom이 null이면 입장 시도 실패
					JOptionPane.showMessageDialog(null, "입장할 방을 선택해주세요", "입장 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 방 입장 오류 : 선택 값이 존재하지 않음");
				}
			}else if(b.getText().equals("관전하기")) {
				if(selRoom != null) {	//selRoom이 null이 아니면 서버에 "태그//방이름" 형태의 메시지를 전송
					c.sendMsg("seeroomTag" + "//" + selRoom);
					c.sendMsg("getmynick//");
				} else {				//selRoom이 null이면 입장 시도 실패
					JOptionPane.showMessageDialog(null, "관전할 방을 선택해주세요", "입장 실패", JOptionPane.ERROR_MESSAGE);
					System.out.println("[Client] 방 입장 오류 : 선택 값이 존재하지 않음");
				}
			}
			else if(b.getText().equals("리플레이")) {
				c.sendMsg("replaynick//"+mynick);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 JComboBox<String> nicknameComboBox = new JComboBox<>(nicknameList.toArray(new String[0]));
                 JButton selectButton = new JButton("선택");
                 selectButton.setPreferredSize(new Dimension(55,100));
                 selectButton.setFont(new Font(currentFont.getName(), currentFont.getStyle(),10));
                 JFrame showreplayFrame = new JFrame( mynick+"님의 리플레이");
                 showreplayFrame.setSize(300, 100);
                 showreplayFrame.setLocationRelativeTo(null);
                 nicknameComboBox.setPreferredSize(new Dimension(240,100));
                 showreplayFrame.add(nicknameComboBox, BorderLayout.WEST);
                 showreplayFrame.add(selectButton, BorderLayout.EAST);

                
                 selectButton.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                    	 replayFrame reF = new replayFrame();
                    	 reF.setVisible(false);
                    	 String selectedNickname = (String) nicknameComboBox.getSelectedItem();
                         
                         String[] m = selectedNickname.split("-게임 일 시 :");
                         if (selectedNickname != null) {
                        	 test = new ArrayList<>();
                             c.sendMsg("getreplay"+"//"+mynick+"//"+m[0]+"//"+m[1]);
                             try {
									Thread.sleep(2);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
                         }
                         reF.black.setText("흑 : "+player1);
                         reF.white.setText("백 : "+player2);
                         reF.test=test;
                     
                         reF.win=win;
                         reF.setVisible(true);
                         
                     }
                 });
                 showreplayFrame.setVisible(true);
			}					
			/* 게임 종료하기 버튼 이벤트 */
			else if(b.getText().equals("게임 종료하기")) {
				System.out.println("[Client] 게임 종료");
				c.sendMsg(pexitTag + "//");		//서버에 프로그램 종료 태그 전송
				System.exit(0);					//프로그램 강제 종료
			}
		}
	}
	
	/* MenuItem 이벤트 리스너 */
	class MenuItemListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem)e.getSource();
			
			/* 내 정보 보기 메뉴 이벤트 */
			if(mi.getText().equals("내 정보 보기")) {
				c.inf.setVisible(true);
				System.out.println("[Client] 회원 정보 조회 인터페이스 열림");
			}
			
			/* 내 정보 바꾸기 메뉴 이벤트 */
			else if(mi.getText().equals("내 정보 바꾸기")) {
				c.sendMsg("imageset3//");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				c.cinf.setVisible(true);
				System.out.println("[Client] 회원 정보 변경 인터페이스 열림");
			}
			else if(mi.getText().equals("환경설정")) {
				JListColor a = new JListColor(c); 
				System.out.println("[Client] 회원 정보 변경 인터페이스 열림");
			}
		}
	}
}