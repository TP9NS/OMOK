
import java.net.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
//서버와의 연결과 각 인터페이스를 관리하는 클래스.

public class Client {
	
	Socket mySocket = null;
	
	/* 메시지 송신을 위한 필드 */
	OutputStream os = null;
	DataOutputStream dos = null;

	
	/* 각 프레임을 관리할 필드 */
	MainFrame mf = null;
	LoginFrame lf = null;
	JoinFrame jf = null;
	RankingFrame rf = null;
	InfoFrame inf = null;
	CInfoFrame cinf = null;
	GameFrame gf = null;
	SRankFrame srf = null;
	AdminFrame adm =null;
	String loby="start";
	String login="fun";
	String ingame="warm";
	int lobyV=60;
	int loginV=60;
	int ingameV=60;
	int sysV=60;
	Font font=new Font("맑은 고딕",Font.PLAIN,12);
	String userColor="Black";
	String myColor ="Black";
	String SystemColor="Black";
	public static void main(String[] args) {
		try {       UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
		Client client = new Client();
		String mynick="";
		
		try {
			//서버에 연결
			client.mySocket = new Socket("localhost",8080 );
			System.out.println("[Client] 서버 연결 성공");
			
			client.os = client.mySocket.getOutputStream();
			client.dos = new DataOutputStream(client.os);
			
			/* 프레임 생성 */
			client.mf = new MainFrame(client);
			client.lf = new LoginFrame(client);
			client.jf = new JoinFrame(client);
			client.rf = new RankingFrame(client);
			client.inf = new InfoFrame(client);
			client.cinf = new CInfoFrame(client);
			client.gf = new GameFrame(client);
			client.srf = new SRankFrame(client);
			client.adm=new AdminFrame(client);
			
			MessageListener msgListener = new MessageListener(client, client.mySocket);
			msgListener.start();	//스레드 시작
			
		} catch(SocketException e) {
			System.out.println("[Client] 서버 연결 오류 > " + e.toString());
		} catch(IOException e) {
			System.out.println("[Client] 입출력 오류 > " + e.toString());
		}
		
	}
	 
	/* 서버에 메시지 전송 */
	void sendMsg(String _m) {
		try {
			dos.writeUTF(_m);
		} catch(Exception e) {
			System.out.println("[Client] 메시지 전송 오류 > " + e.toString());
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
                        clip.start();
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
	public void setVolume(int percentage) {
	       if (clip != null) {
	           FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	           float volume = percentageToVolume(percentage);
	           gainControl.setValue(volume);
	       }
	   }
}

//서버와의 메시지 송수신을 관리하는 클래스.
//스레드를 상속받아 각 기능과 독립적으로 동작할 수 있도록 한다.
class MessageListener extends Thread{
	Socket socket;
	Client client;
	
	/* 메시지 수신을 위한 필드 */
	InputStream is;
	DataInputStream dis;
	
	String msg;	//수신 메시지 저장
	
	/* 각 메시지를 구분하기 위한 태그 */
	final String loginTag = "LOGIN";	//로그인
	final String joinTag = "JOIN";		//회원가입
	final String overTag = "OVER";		//중복확인
	final String viewTag = "VIEW";		//회원정보조회
	final String changeTag = "CHANGE";	//회원정보변경
	final String rankTag = "RANK";		//전적조회(전체회원)
	final String croomTag = "CROOM";	//방생성
	final String vroomTag = "VROOM";	//방목록
	final String uroomTag = "UROOM";	//방유저
	final String eroomTag = "EROOM";	//방입장
	final String cuserTag = "CUSER";	//접속유저
	final String searchTag = "SEARCH";	//전적조회(한명)
	final String omokTag = "OMOK";		//오목
	final String blackTag = "BLACK";	//검은색 돌
	final String whiteTag = "WHITE";	//흰색 돌
	final String winTag = "WIN";		//승리
	final String loseTag = "LOSE";		//패배
	final String rexitTag = "REXIT";	//방퇴장
	final String recordTag = "RECORD";	//전적업데이트
	final String chatTag = "CHAT";
	final String adminTag ="ADMIN";
	String mynick="";
	
	String othernick="";
	
	void handleChatMessage(String message) {
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String formatDate = formatter.format(date);
          		client.gf.cellRenderer.setmyNick(mynick);
          		client.gf.cellRenderer.setmyFont(client.font);
          		client.gf.cellRenderer.setmyColor(client.myColor, client.userColor, client.SystemColor);
          		client.gf.chatList.setCellRenderer(client.gf.cellRenderer);
          		
	            client.gf.chatListModel.addElement(message+":"+formatDate);
	        
	    }

	void handleallchat(String message) {
		Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String formatDate = formatter.format(date);
          		client.mf.cellRenderer.setmyNick(mynick);
          		client.mf.cellRenderer.setmyFont(client.font);
          		client.mf.cellRenderer.setmyColor(client.myColor, client.userColor, client.SystemColor);
          		client.mf.chatList.setCellRenderer(client.mf.cellRenderer);
          		
	            client.mf.chatListModel.addElement(message+":"+formatDate);
	        
	    }
	MessageListener(Client _c, Socket _s) {
		this.client = _c;
		this.socket = _s;
	}
	
	public void run() {
		try {
			
			is = this.socket.getInputStream();
			dis = new DataInputStream(is);
			client.playBGM("start",client.sysV);
			while(true) {
				
				msg = dis.readUTF();	//메시지 수신을 상시 대기한다.
				
				String[] m = msg.split("//");	//msg를 "//"로 나누어 m[]배열에 차례로 집어넣는다.
				
				// 수신받은 문자열들의 첫 번째 배열(m[0])은 모두 태그 문자. 각 기능을 분리한다.
				if (m[0].equals(chatTag)) {
                    // 채팅 메시지를 처리합니다.
					
                    handleChatMessage(m[1]);
                } 
            
				/* 로그인 */
				if(m[0].equals(loginTag)) {
					loginCheck(m[1]);
				}
				
				/* 회원가입 */
				else if(m[0].equals(joinTag)) {
					joinCheck(m[1]);
				}
				
				/* 중복확인 */
				else if(m[0].equals(overTag)) {
					if(m[2].equals("nickname")) {
						overlapCheck_nickname(m[1]);
					}else if(m[2].equals("id")) {
						overlapCheck_id(m[1]);
					}
				}
				else if(m[0].equals("getID")) {
					if(m[1].equals("not_found")) {
						JOptionPane.showMessageDialog(null, "입력하신 정보를 가진 회원이 존재하지 않습니다.");
					}else {
						JOptionPane.showMessageDialog(null, "당신의 ID는 "+m[1]);
					}
				}
				else if(m[0].equals("getpass")) {
					if(m[1].equals("not_found")) {
						JOptionPane.showMessageDialog(null, "입력하신 정보를 가진 회원이 존재하지 않습니다.");
					}else {
						JOptionPane.showMessageDialog(null, "당신의 비밀번호는 "+m[1]);
					}
				}
				/* 회원정보 조회 */
				else if(m[0].equals(viewTag)) {
					viewMyInfo(m[1], m[2], m[3]);
				}
				
				/* 전체 전적 조회 */
				else if(m[0].equals(rankTag)) {
					viewRank(m[1]);
				}
				
				/* 회원정보 변경 */
				else if(m[0].equals(changeTag)) {
					changeInfo(m[1]);
				}
				
				/* 방 생성 */
				else if(m[0].equals(croomTag)) {					
					createRoom(m[1]);
				}
				
				/* 접속 유저 */
				else if(m[0].equals(cuserTag)) {
					viewCUser(m[1]);
				}
				
				/* 방 목록 */
				else if(m[0].equals(vroomTag)) {
					if(m.length > 1) {	//배열크기가 1보다 클 때
						roomList(m[1]);
					} else {	//배열크기가 1보다 작다 == 방이 없다
						String[] room = {""};	//방 목록이 비도록 함
						client.mf.rList.setListData(room);
					}
				}
				
				/* 방 입장 */
				else if(m[0].equals(eroomTag)) {
					
					enterRoom(m[1]);
				}
				
				else if(m[0].equals("imageset")) {
					System.out.println(m[1]);
					if(!m[1].equals("no")) {
					byte[] decodedBytes = Base64.getDecoder().decode(m[1]);
	                // 이미지 바이트 배열을 ByteArrayInputStream에 넣어 BufferedImage로 읽어오기
	                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
	                BufferedImage decodedImage = ImageIO.read(bis);
	                // 이미지를 JLabel에 표시하기 위해 ImageIcon 생성
	                ImageIcon imageIcon = new ImageIcon(decodedImage);
	                Image image = imageIcon.getImage();
	             // Resize the image if needed
	                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	                // Create a new ImageIcon with the scaled image
	                ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
	                client.gf.imageLabel1.setIcon(scaledImageIcon);
					}
					 
	                client.gf.player1L.setText(mynick);
				}
				else if(m[0].equals("imageset2")) {
					if(!m[1].equals("no")) {
					byte[] decodedBytes = Base64.getDecoder().decode(m[1]);
	                // 이미지 바이트 배열을 ByteArrayInputStream에 넣어 BufferedImage로 읽어오기
	                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
	                BufferedImage decodedImage = ImageIO.read(bis);
	                // 이미지를 JLabel에 표시하기 위해 ImageIcon 생성
	                ImageIcon imageIcon = new ImageIcon(decodedImage);
	                Image image = imageIcon.getImage();
	             // Resize the image if needed
	                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
	                // Create a new ImageIcon with the scaled image
	                ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
	                client.inf.imageLabel.setIcon(scaledImageIcon);
	                client.cinf.imageL.setIcon(scaledImageIcon);
					}
					 
				}else if(m[0].equals("imageset3")) {
					if(!m[1].equals("no")) {
					byte[] decodedBytes = Base64.getDecoder().decode(m[1]);
	                // 이미지 바이트 배열을 ByteArrayInputStream에 넣어 BufferedImage로 읽어오기
	                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
	                BufferedImage decodedImage = ImageIO.read(bis);
	                // 이미지를 JLabel에 표시하기 위해 ImageIcon 생성
	                ImageIcon imageIcon = new ImageIcon(decodedImage);
	                Image image = imageIcon.getImage();
	             // Resize the image if needed
	                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
	                // Create a new ImageIcon with the scaled image
	                ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
	                client.cinf.imageL.setIcon(scaledImageIcon);
					}
					 
				}else if(m[0].equals("changepro")) {
					if(m[1].equals("ok")) {
						JOptionPane.showMessageDialog(null, "변경 성공!");
					}else {
						JOptionPane.showMessageDialog(null, "변경 실패");
					}
				}
				
				else if(m[0].equals("imagesetEnter")) {
					if(!m[1].equals("no")) {
					byte[] decodedBytes = Base64.getDecoder().decode(m[1]);
	                // 이미지 바이트 배열을 ByteArrayInputStream에 넣어 BufferedImage로 읽어오기
	                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
	                BufferedImage decodedImage = ImageIO.read(bis);
	                // 이미지를 JLabel에 표시하기 위해 ImageIcon 생성
	                ImageIcon imageIcon = new ImageIcon(decodedImage);
	                Image image = imageIcon.getImage();
	             // Resize the image if needed
	                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	                // Create a new ImageIcon with the scaled image
	                ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
	                client.gf.imageLabel1.setIcon(scaledImageIcon);
					}else {
						 client.gf.imageLabel1.setIcon(null);
					}
	                client.gf.player1L.setText(m[3]);
	                if(!m[2].equals("no")){
	                byte[] decodedBytes2 = Base64.getDecoder().decode(m[2]);
	                // 이미지 바이트 배열을 ByteArrayInputStream에 넣어 BufferedImage로 읽어오기
	                ByteArrayInputStream bis2 = new ByteArrayInputStream(decodedBytes2);
	                BufferedImage decodedImage2 = ImageIO.read(bis2);
	                // 이미지를 JLabel에 표시하기 위해 ImageIcon 생성
	                ImageIcon imageIcon2 = new ImageIcon(decodedImage2);
	                Image image2 = imageIcon2.getImage();
	             // Resize the image if needed
	                Image scaledImage2 = image2.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	                // Create a new ImageIcon with the scaled image
	                ImageIcon scaledImageIcon2 = new ImageIcon(scaledImage2);
	                client.gf.imageLabel2.setIcon(scaledImageIcon2);
	                }else {
						 client.gf.imageLabel2.setIcon(null);
					}
	                client.gf.player2L.setText(m[4]);
				}
				else if(m[0].equals("see")) {
					
					enterseeRoom(m[1]);
				}
				else if(m[0].equals("ready")) {
					if(m[1].equals("player1")) {
						client.gf.player1L.setText(m[2]+("/(준비 완료)"));
					}else if(m[1].equals("player2")) {
						client.gf.player2L.setText(m[2]+("/(준비 완료)"));
						
					}
					
				}
				else if(m[0].equals("readycancel")) {
					if(m[1].equals("player1")) {
						client.gf.player1L.setText(m[2]);
					}else if(m[1].equals("player2")) {
						client.gf.player2L.setText(m[2]);
					}
					
				}
				else if(m[0].equals("allready")) {
					String player1[] =client.gf.player1L.getText().split("/");
					String player2[] =client.gf.player2L.getText().split("/");
					client.gf.readyBtn.setVisible(false);
					 client.gf.enableL.setText("게임 시작 5초전");
					 Thread.sleep(1000);
					 client.gf.enableL.setText("게임 시작 4초전");
					 Thread.sleep(1000);
					 client.gf.enableL.setText("게임 시작 3초전");
					 Thread.sleep(1000);
					 client.gf.enableL.setText("게임 시작 2초전");
					 Thread.sleep(1000);
					 client.gf.enableL.setText("게임 시작 1초전");
					 client.gf.playBGM("changrang",client.sysV);
					 client.gf.gamestate="game";
					 client.gf.player1L.setText(player1[0]);
					client.gf.player2L.setText(player2[0]);
					if (player1[0].equals(m[1])) {
						client.gf.enableL.setText("당신의 차례입니다.");
						client.gf.enable=true;
						client.gf.readyBtn.setVisible(false);
						client.gf.loseBtn.setText("기권하기");
					}
					else if (player2[0].equals(m[1])) {
						client.gf.enableL.setText("상대방의 차례입니다.");
						client.gf.readyBtn.setVisible(false);
						client.gf.loseBtn.setText("기권하기");
					}else {
						client.gf.enableL.setText("관전중입니다.");
					}
				}
				
				/* 방 인원 */
				else if(m[0].equals(uroomTag)) {
					roomUser(m[1],m[2]);
				}
				
				/* 전적 조회 */
				else if(m[0].equals(searchTag)) {
					searchRank(m[1]);
				}
				
				/* 오목 */
				else if(m[0].equals(omokTag)) {
					inputOmok(m[1], m[2], m[3]);
				}
				else if(m[0].equals("getomok")) {
				String a= arrayToString(client.gf.omok);
				client.sendMsg("getomok"+"//"+a);
				System.out.println("client : getomok 호출");
				}else if(m[0].equals("setomok")) {
					if(client.gf.checkseeorgame.equals("see")){
						client.gf.omok = stringToArray(m[1]);
						System.out.println("client : setomok 호출"+m[1]);
					}
				}
				
				/* 패배 */
				else if(m[0].equals(loseTag)) {
					if(client.gf.checkseeorgame.equals("game")) {
					loseGame();
					}
				}
				
				/* 승리 */
				else if(m[0].equals(winTag)) {
					if(client.gf.checkseeorgame.equals("game")) {
					winGame();
					}
				}
				else if(m[0].equals("endgame")) {
					endgame();
				}
				
				else if(m[0].equals("allchat")) {
					handleallchat(m[1]);
				}
				else if(m[0].equals("getimage")) {
					if(m[1].equals("no")){
						JOptionPane.showMessageDialog(null, "닉네임이 잘못되었거나 프로필 사진이 없는 유저 입니다.");
						client.adm.image="no";
					}
					client.adm.image=m[1];
				}
				//나 말고 다른 사람 닉네임 받아오기 채팅기록을 저장하기 위해 사용
				else if(m[0].equals("getnick")){
					
					 client.gf.mynick = (m[1]);
					if(m[1].equals("alone")){ 
						client.gf.othernick="";
					}else {
					client.gf.othernick=(m[2]);
					}
				}
				else if(m[0].equals("getmynick")) {
					client.mf.mynick=m[1];
					client.gf.mynick=m[1];
					mynick=m[1];
					System.out.println(mynick+client.gf.mynick);
				}
				//채팅한 닉네임들 리스트
				else if(m[0].equals("chatnick")) {
					Gson gson = new Gson();
					Type type = new TypeToken<List<String>>() {}.getType();
					List<String> receivedList = gson.fromJson(m[1], type);
					client.adm.nicknameList=receivedList;
					client.adm.flag=true;
					System.out.print(receivedList.size()+"전달이 제대로 안됐나?");
				}
				else if(m[0].equals("replaynick")) {
					Gson gson = new Gson();
					Type type = new TypeToken<List<String>>() {}.getType();
					List<String> receivedList = gson.fromJson(m[1], type);
					client.mf.nicknameList=receivedList;
					
				}
				//채팅내역 받아오기
				else if(m[0].equals("getchat")) {
					System.out.println(m[1]);
					client.adm.chatRecord=m[1];
				}
				else if(m[0].equals("getreplay")) {
					client.mf.player1=m[1];
					client.mf.player2=m[2];
					String[] game= m[3].split("@");
					for(int i=0;i<game.length;i++) {
						 String[] xyz = game[i].split(",");
						 int x = Integer.parseInt(xyz[0]);
						 int y = Integer.parseInt(xyz[1]);
						 int z = Integer.parseInt(xyz[2]);
						 System.out.println(x);
						 System.out.println(game.length);
						 client.mf.test.add(new int[] {x,y,z});
					}
					client.mf.win=m[4];
				}
				/* 전적 업데이트 */
 				else if(m[0].equals(recordTag)) {
					dataRecord(m[1]);
				}
				/*서버에서 정보 받아와서 어드민 프레임*/
 				else if(m[0].equals(adminTag)) {
 				if(m[1].equals("ok")) {
 					client.adm.showDel();
 					client.adm.allUserInfo();
 				}else if(m[1].equals("chgok")) {
			    	 JOptionPane.showMessageDialog(null, "회원 변경에 성공하였습니다.");
			    }else if(m[1].equals("chgno")) {
			    	JOptionPane.showMessageDialog(null, "존재하지 않는 ID이거나 중복된 값이 있습니다.");
			    }else if (m[2].equals("now")) {
 				        // client.adm.userInfoArea.setText(m[1]); // 텍스트를 설정하는 부분을
 						client.adm.clearTable();
 				        client.adm.allUserInfo(m[1]); // JTable 데이터를 추가하는 메서드 호출로 변경합니다.
 				   } else if (m[2].equals("all")) {
 				        // client.adm.userInfoArea.setText(m[1]); // 텍스트를 설정하는 부분을
 				    	client.adm.clearTable();
 				    	client.adm.allUserInfo(m[1]); // JTable 데이터를 추가하는 메서드 호출로 변경합니다.
 				    }
 					
 				}else if(m[0].equals("searchUser")){
 					client.adm.clearTable();
 					client.adm.allUserInfo(m[1]);
 				}
			
			}
			} catch(Exception e) {
			System.out.println("[Client] Error: 메시지 받기 오류 > " + e.toString());
			}
	
	}
	
	/* 로그인 성공 여부를 확인하는 메소드 */
	void loginCheck(String _m) {	
		if(_m.equals("OKAY")) {	//로그인 성공
			System.out.println("[Client] 로그인 성공 : 메인 인터페이스 열림 : 로그인 인터페이스 종료");
			client.sendMsg("getmynick//");
			client.mf.setVisible(true);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			client.mf.cellRenderer.setmyNick(mynick);
			client.lf.dispose();
		}
		
		else {				//로그인 실패
			System.out.println("[Client] 로그인 실패 : 회원정보 불일치");
			JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			client.lf.id.setText("");
			client.lf.pw.setText("");
		}
	}
	
	/* 회원가입 성공 여부를 확인하는 메소드 */
	void joinCheck(String _m) {
		if(_m.equals("OKAY")) {	//회원가입 성공
			JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다", "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);
			client.jf.dispose();
			System.out.println("[Client] 회원가입 성공 : 회원가입 인터페이스 종료");
		}
		
		else {				//회원가입 실패
			JOptionPane.showMessageDialog(null, "닉네임이나 이름이 중복되었는지 확인하세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
			System.out.println("[Client] 회원가입 실패");
			client.jf.name.setText("");
			client.jf.nickname.setText("");
			client.jf.id.setText("");
			client.jf.pw.setText("");
			client.jf.email.setText("");
		}
	}
	
	/* 중복 여부를 확인하는 메소드 */
	void overlapCheck_id(String _m) {
		if(_m.equals("OKAY")) {	//사용 가능
			System.out.println("[Client] 사용 가능");
			JOptionPane.showMessageDialog(null, "사용 가능한 닉네임/아이디 입니다", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else {				//사용 불가능
			System.out.println("[Client] 사용 불가능");
			JOptionPane.showMessageDialog(null, "이미 존재하는 닉네임/아이디 입니다", "중복 확인", JOptionPane.ERROR_MESSAGE);
			client.jf.id.setText("");
		}
	}
	void overlapCheck_nickname(String _m) {
		if(_m.equals("OKAY")) {	//사용 가능
			System.out.println("[Client] 사용 가능");
			JOptionPane.showMessageDialog(null, "사용 가능한 닉네임/아이디 입니다", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else {				//사용 불가능
			System.out.println("[Client] 사용 불가능");
			JOptionPane.showMessageDialog(null, "이미 존재하는 닉네임/아이디 입니다", "중복 확인", JOptionPane.ERROR_MESSAGE);
			client.jf.nickname.setText("");
		}
	}
	
	/* 내 정보를 확인하는 메소드 */
	void viewMyInfo(String m1, String m2, String m3) {
		if(!m1.equals("FAIL")) {	//회원정보 조회 성공
			System.out.println("[Client] 회원 정보 조회 성공");
			client.inf.name.setText(m1);
			client.inf.nickname.setText(m2);
			client.inf.email.setText(m3);
		}
		
		else {					//회원정보 조회 실패
			System.out.println("[Client] 회원 정보 조회 실패");
		}
	}
	
	/* 전적을 출력하는 메소드 */
	void viewRank(String _m) {
		if(!_m.equals("FAIL")) {	//전적 조회 성공
			System.out.println("[Client] 전적 조회 성공");
			String[] user = _m.split("@");
			
			client.rf.rank.setListData(user);
		}
	}
	
	/* 회원정보 변경 여부를 확인하는 메소드 */
	void changeInfo(String _m) {
		if(_m.equals("OKAY")) {	//회원정보 변경 성공
			System.out.println("[Client] 변경 성공");
			JOptionPane.showMessageDialog(null, "정상적으로 변경되었습니다", "회원정보변경", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else {				//회원정보 변경 실패
			System.out.println("[Client] 이름 변경 실패");
			JOptionPane.showMessageDialog(null, "변경에 실패하였습니다", "회원정보변경", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/* 방 생성 여부를 확인하는 메소드 */
	void createRoom(String _m) {
		if(_m.equals("OKAY")) {	//방 생성 성공
			System.out.println("[Client] 방 생성 성공");
			client.gf =new GameFrame(client);
			client.gf.setVisible(true);
			client.mf.setVisible(false);
			client.gf.setTitle(client.mf.roomName);
			client.gf.dc = blackTag;	//방을 생성한 사람은 검은 돌
			client.gf.enable = false;	//돌 놓기 가능하게 바꿈
			client.sendMsg("imageset");
			System.out.println(mynick);
			client.stopBGM();
			client.playBGM(client.ingame,client.ingameV);
		}
	}
	
	/* 접속 인원을 출력하는 메소드 */
	void viewCUser(String _m) {
		if(!_m.equals("")) {
			String[] user = _m.split("@");
			
			client.mf.cuList.setListData(user);
		}
	}
	
	/* 방 목록을 출력하는 메소드 */
	void roomList(String _m) {
		if(!_m.equals("")) {
			String[] room = _m.split("@");
			
			client.mf.rList.setListData(room);
		}
	}
	
	/* 방 입장 여부를 확인하는 메소드 */
	void enterRoom(String _m) {
		if(_m.equals("OKAY")) {	//방 입장 성공
			System.out.println("[Client] 방 입장 성공");
			client.gf.setVisible(true);
			client.mf.setVisible(false);
			client.gf.setTitle(client.mf.selRoom);
			client.gf.dc = whiteTag;	//방에 입장한 사람은 흰 돌
			client.gf.enable = false;	//돌 놓기 불가능하게 바꿈
			client.stopBGM();
			client.playBGM(client.ingame,client.ingameV);
		}	else {				//방 입장 실패
			System.out.println("[Client] 방 입장 실패");
			JOptionPane.showMessageDialog(null, "플레이어가 가득 찼거나 게임에 시작한 방입니다. 관전하기로 입장해주세요", "방입장", JOptionPane.ERROR_MESSAGE);
		}
	}
	void enterseeRoom(String _m) {
		if(_m.equals("OKAY")) {	//방 입장 성공
			System.out.println("[Client] 방 입장 성공");
			client.gf.setVisible(true);
			client.mf.setVisible(false);
			client.gf.setTitle(client.mf.selRoom);
			client.gf.checkseeorgame = "see";
			client.gf.enableL.setText("관전 중 입니다");
			client.gf.readyBtn.setVisible(false);
			client.gf.changeBtn.setText("게임하기");
			client.gf.enable = false;	//돌 놓기 불가능하게 바꿈
			client.gf.loseBtn.setText("방나가기");
			client.stopBGM();
			client.playBGM(client.ingame,client.ingameV);
		}else {				//방 입장 실패
			System.out.println("[Client] 방 입장 실패");
			JOptionPane.showMessageDialog(null, "플레이어가 가득 차지 않아 관전할 수 없습니다.", "방입장", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/* 방 인원 목록을 출력하는 메소드 */
	void roomUser(String player,String see) {
		client.gf.userListModel.removeAllElements();
		client.gf.seeuserListModel.removeAllElements();
		String[] playerlist= player.split("@");
		for(int i =0;i<playerlist.length;i++) {
			if((!playerlist[i].equals("player1"))&(!playerlist[i].equals("player2")))
			client.gf.userListModel.addElement(playerlist[i]);
		}
		String[] seelist= see.split("@");
		for(int i =0;i<seelist.length;i++) {
			if(!seelist[i].equals("nosee")) {
			client.gf.seeuserListModel.addElement(seelist[i]);
			}
		}
	}
	
	/* 전적 조회 메소드 */
	void searchRank(String _m) {
		if(!_m.equals("FAIL")) {	//전적 조회 성공
			client.srf.setVisible(true);
			client.srf.l.setText(_m);
		}
	}
	
	/* 상대 오목을 두는 메소드 */
	void inputOmok(String m1, String m2, String m3) {
		if(!m1.equals("") || !m2.equals("") || !m3.equals("")) {
			client.gf.playBGM("ddak",60);
			int n1 = Integer.parseInt(m1);
			int n2 = Integer.parseInt(m2);
			
			if(m3.equals(blackTag)) {	//검은 돌 태그면 1
				client.gf.omok[n2][n1] = 1;
			} else {					//흰 돌 태그면 2
				client.gf.omok[n2][n1] = 2;
			}
			
			client.gf.repaintBoard();
			if(client.gf.checkseeorgame.equals("game")){
			client.gf.enable = true;	//돌을 놓을 수 있도록 함
			client.gf.enableL.setText("본인 차례입니다.");
			}
		}
	}
	
	/* 패배를 알리는 메소드 */
	void loseGame() {
		System.out.println("[Client] 게임 패배");
		JOptionPane.showMessageDialog(null, "게임에 패배하였습니다", "패배", JOptionPane.INFORMATION_MESSAGE);
		client.gf.remove();	//화면을 지움
		client.gf.dispose();
		client.gf.chatListModel.removeAllElements();
		client.mf.setVisible(true);
		client.stopBGM();
		client.playBGM(client.loby,client.lobyV);		
	}
	
	/* 승리를 알리는 메소드 */
	void winGame() {
		System.out.println("[Client] 게임 승리");
		JOptionPane.showMessageDialog(null, "게임에 승리하였습니다", "승리", JOptionPane.INFORMATION_MESSAGE);
		client.gf.remove();	//화면을 지움
		client.gf.dispose();
		client.gf.chatListModel.removeAllElements();
		client.mf.setVisible(true);
		client.stopBGM();
		client.playBGM(client.loby,client.lobyV);
		
	}
	void endgame() {
		System.out.println("[Client] 게임 관전 종료");
		JOptionPane.showMessageDialog(null, "게임이 끝났습니다.", "관전 종료", JOptionPane.INFORMATION_MESSAGE);
		client.sendMsg(rexitTag + "//");
		client.gf.remove();	//화면을 지움
		client.gf.dispose();
		client.gf.chatListModel.removeAllElements();
		client.mf.setVisible(true);
		client.stopBGM();
		client.playBGM(client.loby,client.lobyV);
	}
	/* 전적 업데이트 여부를 알리는 메소드 */
	void dataRecord(String _m) {
		if(_m.equals("NO")) {			//전적 업데이트 안함
			System.out.println("[Client] 데이터 미반영 : 상대가 없음");
			JOptionPane.showMessageDialog(null, "게임 상대가 없어 전적을 반영하지 않았습니다", "전적반영", JOptionPane.INFORMATION_MESSAGE);
			client.sendMsg(rexitTag + "//");
		} else if(_m.equals("OKAY")) {	//전적 업데이트 성공
			System.out.println("[Client] 데이터 반영 성공");
			JOptionPane.showMessageDialog(null, "전적 반영이 정상적으로 완료되었습니다", "전적반영", JOptionPane.INFORMATION_MESSAGE);
			client.sendMsg(rexitTag + "//");
		} else if(_m.equals("FAIL")) {	//전적 업데이트 실패
			System.out.println("[Client] 데이터 반영 실패");
			JOptionPane.showMessageDialog(null, "시스템 장애로 인하여 전적 반영에 실패하였습니다", "전적반영", JOptionPane.INFORMATION_MESSAGE);
			client.sendMsg(rexitTag + "//");
		}
	}
	 String arrayToString(int[][] array) {
	        StringBuilder sb = new StringBuilder();

	        for (int i = 0; i < array.length; i++) {
	            for (int j = 0; j < array[i].length; j++) {
	                sb.append(array[i][j]).append(" ");
	            }
	            sb.append("\n");
	        }

	        return sb.toString();
	    }
	 int[][] stringToArray(String str) {
	        String[] rows = str.split("\n"); // 행으로 분할

	        int[][] newArray = new int[rows.length][];

	        for (int i = 0; i < rows.length; i++) {
	            String[] columns = rows[i].trim().split("\\s+"); // 각 행의 요소로 분할
	            newArray[i] = new int[columns.length];

	            for (int j = 0; j < columns.length; j++) {
	                newArray[i][j] = Integer.parseInt(columns[j]); // 정수로 변환하여 배열에 저장
	            }
	        }

	        return newArray;
	    }
}