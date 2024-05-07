
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class AdminFrame extends JFrame {
    JPanel basePanel = new JPanel(new BorderLayout());
    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel westPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();

    Client c = null;

    final String adminTag = "ADMIN";
    DefaultTableModel tableModel;
    JTable userTable;
    List<String> nicknameList;
    boolean flag = false;
    String chatRecord;
    String image="";
    AdminFrame(Client _c) {
        c = _c;

        setTitle("관리자 창");

        centerPanel.setPreferredSize(new Dimension(0, 0));
        westPanel.setPreferredSize(new Dimension(1000, 1000));
        eastPanel.setPreferredSize(new Dimension(200, 400));
        southPanel.setPreferredSize(new Dimension(0, 0));

        setContentPane(basePanel);

        basePanel.add(centerPanel, BorderLayout.CENTER);
        basePanel.add(southPanel, BorderLayout.SOUTH);
        centerPanel.add(westPanel, BorderLayout.WEST);
        centerPanel.add(eastPanel, BorderLayout.EAST);

        westPanel.setLayout(new BorderLayout());
        eastPanel.setLayout(new FlowLayout());
        southPanel.setLayout(new FlowLayout());

        String[] columnNames = {"이름", "전화번호","생년월일", "닉네임", "아이디", "비밀번호", "이메일", "우편번호", "주소", "승리", "패배"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(userTable);
        westPanel.add(scrollPane);

        JButton refreshBtn = new JButton("접속 이용자");
        JButton allBtn = new JButton("모든 이용자");
        JButton delBtn = new JButton("계정 삭제");
        JButton chgBtn = new JButton("정보 변경");
        JButton chatBtn = new JButton("채팅 내역");
        JButton imageBtn=new JButton("프사 보기");
        JButton searchBtn = new JButton("사용자 검색");
        
        refreshBtn.setPreferredSize(new Dimension(180,100));
        allBtn.setPreferredSize(new Dimension(180,100));
        delBtn.setPreferredSize(new Dimension(180,100));
        chgBtn.setPreferredSize(new Dimension(180,100));
        chatBtn.setPreferredSize(new Dimension(180,100));
        imageBtn.setPreferredSize(new Dimension(180,100));
        searchBtn.setPreferredSize(new Dimension(180,100));
        eastPanel.add(refreshBtn);
        eastPanel.add(allBtn);
        eastPanel.add(delBtn);
        eastPanel.add(chgBtn);
        eastPanel.add(chatBtn);
        eastPanel.add(imageBtn);
        eastPanel.add(searchBtn);
        
        Font buttonFont = new Font("맑은 고딕", Font.BOLD, 14);

        // 버튼 배경색 설정
        Color buttonColor = new Color(255, 255, 255); 

        // 버튼 스타일 적용
        JButton[] buttons = {refreshBtn, allBtn, delBtn, chgBtn, chatBtn, imageBtn, searchBtn};
        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
        }

        // JFrame 배경색 설정
        basePanel.setBackground(new Color(245, 245, 220));
		
		
        setSize(1200, 1200);
        setLocationRelativeTo(null);
        setResizable(false);

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUserInfo();
            }
        });
        allBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allUserInfo();
            }
        });
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String userInput = JOptionPane.showInputDialog(null, "삭제할 계정의 아이디를 입력하세요:");

                // userInput이 null이 아니고, 빈 문자열이 아닌 경우에만 삭제 메시지 보내기
            	if (userInput != null && !userInput.isEmpty()) {
                    c.sendMsg(adminTag + "//deluser//" + userInput);
                    
                } else {
                    JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
        chgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String id = JOptionPane.showInputDialog(null, "정보 변경을 하실 아이디를 입력하세요:");

                if (id != null && !id.isEmpty()) {
                    // 다이얼로그로 변경할 정보 선택
                    String[] attributes = {"name", "birth", "nickname", "id", "password", "email", "express", "addr", "win", "lose"};
                    JComboBox<String> attributeComboBox = new JComboBox<>(attributes);
                    JTextField valueField = new JTextField();

                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("바꿀 정보를 선택하세요:"));
                    panel.add(attributeComboBox);
                    panel.add(new JLabel("새로운 값 입력:"));
                    panel.add(valueField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "정보 변경",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String att = (String) attributeComboBox.getSelectedItem();
                        String val = valueField.getText();

                        // 여기서 att와 val을 이용하여 원하는 동작을 수행하도록 코드를 추가하세요
                        // att는 선택된 변경할 정보이며, val은 입력된 새 값입니다.
                        c.sendMsg(adminTag+"//"+"chg//"+id+"//"+att+"//"+val);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.");
                }
            }
        });
        chatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nick = JOptionPane.showInputDialog(null, "닉네임을 입력하세요:");
                if (nick != null && !nick.isEmpty()) {        
                            // 서버 응답을 받는 부분
                        	c.sendMsg("chatnick" + "//" + nick);
                        	try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                            // 예시로 작성한 것입니다. 실제로는 서버 응답을 받을 때까지 대기해야 합니다.
                            // 응답이 오길 기다림

                            // 응답을 받으면 닉네임 리스트를 업데이트하고 채팅 창을 열어줌
                            JComboBox<String> nicknameComboBox = new JComboBox<>(nicknameList.toArray(new String[0]));
                            JButton selectButton = new JButton("선택");

                            JFrame chatFrame = new JFrame(nick + "님의 채팅 내역");
                            chatFrame.setSize(600, 400);
                            chatFrame.setLocationRelativeTo(null);
                            nicknameComboBox.setPreferredSize(new Dimension(100,50));
                            chatFrame.add(nicknameComboBox, BorderLayout.NORTH);
                            chatFrame.add(selectButton, BorderLayout.EAST);

                            JTextArea chatTextArea = new JTextArea();
                            chatTextArea.setEditable(false);
                            chatFrame.add(new JScrollPane(chatTextArea), BorderLayout.CENTER);

                            selectButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String selectedNickname = (String) nicknameComboBox.getSelectedItem();
                                    chatTextArea.setText("");
                                    String[] m = selectedNickname.split("-채팅 일 시 :");
                                    if (selectedNickname != null) {
                                        c.sendMsg("getchat"+"//"+nick+"//"+m[0]+"//"+m[1]);
                                        try {
											Thread.sleep(2);
										} catch (InterruptedException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
                                        chatTextArea.setText(chatRecord);
                                    }
                                }
                            });
                            chatFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "닉네임을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        imageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String userInfo = JOptionPane.showInputDialog(null, "프로필 사진을 볼 닉네임을 입력하세요:");

                // userInput이 null이 아니고, 빈 문자열이 아닌 경우에만 삭제 메시지 보내기
            	if (userInfo != null && !userInfo.isEmpty()) {
                    c.sendMsg("getimage"+"//"+userInfo);
                    
                } else {
                    JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            if(!image.equals("")) {
            try {
            	if(image.equals("no")) {
            		JOptionPane.showMessageDialog(null, "이미지가 없습니다.", "이미지 없음", JOptionPane.INFORMATION_MESSAGE);
            	}
            	else {
                // uimage에서 Base64 디코딩하여 이미지 바이트 배열로 가져오기
                byte[] decodedBytes = Base64.getDecoder().decode(image);
                // 이미지 바이트 배열을 ByteArrayInputStream에 넣어 BufferedImage로 읽어오기
                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                BufferedImage decodedImage = ImageIO.read(bis);
                // 이미지를 JLabel에 표시하기 위해 ImageIcon 생성
                ImageIcon imageIcon = new ImageIcon(decodedImage);
                JLabel imageLabel = new JLabel(imageIcon);
                // 팝업창에 이미지 띄우기
                JOptionPane.showMessageDialog(null, imageLabel, userInfo + "님의 프로필 사진", JOptionPane.PLAIN_MESSAGE);
                // 프로필 사진 아래에 사용자 이름과 함께 표시
                System.out.println(userInfo + "님의 프로필 사진");
            	}
            	} catch (IOException ex) {
                ex.printStackTrace();
            	}
            }
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// 사용자가 검색할 속성 입력을 위한 다이얼로그 생성
                String attribute = (String) JOptionPane.showInputDialog(null,
                        "검색할 속성을 선택하세요:", "사용자 검색", JOptionPane.QUESTION_MESSAGE,
                        null, new String[]{"name", "birth", "nickname", "id", "password", "email", "express", "addr", "win", "lose"}, "name");

                if (attribute != null && !attribute.isEmpty()) {
                    String value = JOptionPane.showInputDialog(null,
                            "검색할 " + attribute + "를 입력하세요:");

                    if (value != null && !value.isEmpty()) {
                        c.sendMsg("searchUser//" + attribute + "//" + value);
                    } else {
                        JOptionPane.showMessageDialog(null, "값을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "속성을 선택해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void refreshUserInfo() {
        c.sendMsg(adminTag + "//now");
    }
   
    public void allUserInfo() {
        c.sendMsg(adminTag + "//all");
    }
    public void showDel() {
    	JOptionPane.showMessageDialog(null, "삭제에 성공하였습니다", "계정삭제 성공", JOptionPane.INFORMATION_MESSAGE);
    }
    public void allUserInfo(String userData) {
    	String[] lines = userData.split("\n");
        for (String line : lines) {
            String[] data = line.split("\t");
            tableModel.addRow(data);
        }
    }
    public void clearTable() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }
}
