package Server;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
//클라이언트의 연결 요청 및 입출력을 상시 관리하는 클래스.
public class Server {
	ServerSocket ss = null;
	
	/* 각 객체들을 Vector로 관리 */
	Vector<CCUser> alluser;		//연결된 모든 클라이언트
	Vector<CCUser> waituser;	//대기실에 있는 클라이언트
	Vector<Room> room;			//생성된 Room
	
	public static void main(String[] args) {
		Server server = new Server();
		
		server.alluser = new Vector<>();
		server.waituser = new Vector<>();
		server.room = new Vector<>();
		
		try {
			//서버 소켓 준비
			server.ss = new ServerSocket(8080,0,InetAddress.getByName(null));
			System.out.println("[Server] 서버 소켓 준비 완료");
			
			//클라이언트의 연결 요청을 상시 대기. 
			while(true) {
				Socket socket = server.ss.accept();
				CCUser c = new CCUser(socket, server);	//소켓과 서버를 넘겨 CCUser(접속한 유저 관리)객체 생성
				
				c.start();	//CCUser 스레드 시작
			}
		} catch(SocketException e) {	//각 오류를 콘솔로 알린다.
			System.out.println("[Server] 서버 소켓 오류 > " + e.toString());
		} catch(IOException e) {
			System.out.println("[Server] 입출력 오류 > " + e.toString());
		}
	}
}

//서버에 접속한 유저와의 메시지 송수신을 관리하는 클래스.
//스레드를 상속받아 연결 요청이 들어왔을 때도 독립적으로 동작할 수 있도록 한다.
class CCUser extends Thread{
	Server server;
	Socket socket;
	
	/* 각 객체를 Vector로 관리 */
	Vector<CCUser> auser;	//연결된 모든 클라이언트
	Vector<CCUser> wuser;	//대기실에 있는 클라이언트
	Vector<Room> room;		//생성된 Room
	
	Database db = new Database();
	
	/* 메시지 송수신을 위한 필드 */
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	
	String msg;			//수신 메시지를 저장할 필드
	String nickname;	//클라이언트의 닉네임을 저장할 필드
	String mode ="game";
	Room myRoom;		//입장한 방 객체를 저장할 필드
	
	final String chatTag = "CHAT";

	// 채팅 메시지를 처리하는 메서드 추가
	void sendChatMessage(String message) {
	    try {
	        dos.writeUTF(chatTag + "//" + message);
	    } catch (IOException e) {
	        // 예외 처리 (예를 들어, 사용자를 방에서 제거)
	    }
	}
	void sendChatMessageToRoom(String message) {
	    for (int i = 0; i < myRoom.ccu.size(); i++) {
	        myRoom.ccu.get(i).sendChatMessage(nickname + ": " + message);
	    }
	}
	void sendAllWait(String message) {
	    String message1 = nickname+":"+message;
		for (CCUser user : wuser) {
	        try {
	            user.dos.writeUTF("allchat//" + message1);
	        } catch (IOException e) {
	            // 메시지 전송에 실패한 경우 해당 사용자를 삭제하거나 예외 처리하세요
	            wuser.remove(user);
	        }
	    }
	}
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
	final String pexitTag = "PEXIT";	//프로그램종료
	final String rexitTag = "REXIT";	//방퇴장
	final String omokTag = "OMOK";		//오목
	final String winTag = "WIN";		//승리
	final String loseTag = "LOSE";		//패배
	final String recordTag = "RECORD";	//전적업데이트
	final String adminTag ="ADMIN";
	CCUser(Socket _s, Server _ss) {
		this.socket = _s;
		this.server = _ss;
		
		auser = server.alluser;
		wuser = server.waituser;
		room = server.room;		
		
	}
	
	public void run() {
		try {
			System.out.println("[Server] 클라이언트 접속 > " + this.socket.toString());
			
			os = this.socket.getOutputStream();
			dos = new DataOutputStream(os);
			is = this.socket.getInputStream();
			dis = new DataInputStream(is);
			
			while(true) {
				msg = dis.readUTF();	//메시지 수신을 상시 대기한다.
				
				String[] m = msg.split("//");	//msg를 "//"로 나누어 m[]배열에 차례로 집어넣는다.
				 
				 if (m[0].equals(chatTag)) {
			            // 이것은 채팅 메시지이므로 해당 방의 모든 사용자에게 전송합니다.
			            String chatMessage = m[1];
			            sendChatMessageToRoom(chatMessage);
			        } 
			    
				// 수신받은 문자열들의 첫 번째 배열(m[0])은 모두 태그 문자. 각 기능을 분리한다.
				
				/* 로그인 */
				if(m[0].equals(loginTag)) {
					String mm = db.loginCheck(m[1], m[2]);
					
					if(!mm.equals("null")) {	//로그인 성공
						nickname = mm;		//로그인한 사용자의 닉네임을 필드에 저장
						
						auser.add(this);	//모든 접속 인원에 추가
						wuser.add(this);	//대기실 접속 인원에 추가
						
						dos.writeUTF(loginTag + "//OKAY");
						
						sendWait(connectedUser());	//대기실 접속 유저에 모든 접속 인원을 전송
						
						if(room.size() > 0) {	//생성된 방의 개수가 0 이상일 때
							sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
						}
						
					}
					
					else {	//로그인 실패
						dos.writeUTF(loginTag + "//FAIL");
					}
				}
				
				/* 회원가입 */
				else if(m[0].equals(joinTag)) {
					if(db.joinCheck(m[1], m[2], m[3], m[4], m[5],m[6],m[7],m[8],m[9],m[10])) {	//회원가입 성공
						dos.writeUTF(joinTag + "//OKAY");
					}
					
					else {	//회원가입 실패
						dos.writeUTF(joinTag + "//FAIL");
					}
				}
				
				/* 중복확인 */
				else if(m[0].equals(overTag)) {
					if(db.overCheck(m[1], m[2])) {	//사용 가능
						if(m[1].equals("nickname")) {
									dos.writeUTF(overTag + "//OKAY//nickname");
							}
						else if(m[1].equals("id")) {
									dos.writeUTF(overTag + "//OKAY//id");
							}
						}
					
					else {	//사용 불가능
						if(m[1].equals("nickname")) {
							dos.writeUTF(overTag + "//FAIL//nickname");
						}
						else if(m[1].equals("id")) {
							dos.writeUTF(overTag + "//FAIL//id");
					}
				}
					}
				else if(m[0].equals("getID")){
					String id =db.findIDFromMember(m[1],m[2],m[3]);
					dos.writeUTF("getID"+"//"+id);
				}
				else if(m[0].equals("getpass")) {
					String pass = db.findpasswordFromMember(m[1],m[2],m[3],m[4]);
					dos.writeUTF("getpass"+"//"+pass);
				}
				
				
				else if(m[0].equals(adminTag)) {
						//대기실 접속 인원에서 클라이언트 삭제
					if(m[1].equals("now")) {
						String nicksql=nickSql();
						String nui=db.getNowUsersInfo(nicksql);
						dos.writeUTF(adminTag+"//"+nui+"//now");
					}else if(m[1].equals("all")) {
						String aui = db.getAllUsersInfo();
						dos.writeUTF(adminTag+"//"+aui+"//all");
					} 
					else if (m[1].equals("deluser")) {
						db.deleteUserInfo(m[2]);
						dos.writeUTF(adminTag+"//ok");
						
					}else if(m[1].equals("chg")) {
						if(db.changeInfo(m[2], m[3],m[4])) {
							dos.writeUTF(adminTag+"//chgok");
						}else {
							dos.writeUTF(adminTag+"//chgno");
						}
					}
					
					//대기실 접속 인원에 전체 접속 인원을 전송
				}
				else if(m[0].equals("searchUser")) {
					String oui=db.getoneUsersInfo(m[1],m[2]);
					dos.writeUTF("searchUser"+"//"+oui);
				}
				/* 회원정보 조회 */
				else if(m[0].equals(viewTag)) {
					if(!db.viewInfo(nickname).equals("null")) {	//조회 성공
						dos.writeUTF(viewTag + "//" + db.viewInfo(nickname));	//태그와 조회한 내용을 같이 전송
					}
					
					else {	//조회 실패
						dos.writeUTF(viewTag + "//FAIL");
					}
				}
				
				/* 회원정보 변경 */
				else if(m[0].equals(changeTag)) {
					if(db.changeInfo(nickname, m[1], m[2])) {	//변경 성공
						dos.writeUTF(changeTag + "//OKAY");
					}
					
					else {	//변경 실패
						dos.writeUTF(changeTag + "//FAIL");
					}
				}
				
				/* 전체 전적 조회 */
				else if(m[0].equals(rankTag)) {
					if(!db.viewRank().equals("")) {	//조회 성공
						dos.writeUTF(rankTag + "//" + db.viewRank());	//태그와 조회한 내용을 같이 전송
					}
					
					else {	//조회 실패
						dos.writeUTF(rankTag + "//FAIL");
					}
				}
				else if(m[0].equals("ready")) {
					//myRoom의 인원수만큼 반복
						if(myRoom.player1.equals(m[1])) {
						myRoom.player1ready=true;
						for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
								myRoom.ccu.get(j).dos.writeUTF("ready//"+"player1//"+m[1]);	
							}
						}else if(myRoom.player2.equals(m[1])){
							myRoom.player2ready=true;
							for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
								myRoom.ccu.get(j).dos.writeUTF("ready//"+"player2//"+m[1]);	
							}
						}
						if(myRoom.player1ready&myRoom.player2ready) {
							for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
									myRoom.ccu.get(j).dos.writeUTF("allready//"+myRoom.ccu.get(j).nickname);
									System.out.println(myRoom.ccu.get(j).nickname);
							}
						}
				}
				else if(m[0].equals("readycancel")) {
					//myRoom의 인원수만큼 반복
						if(myRoom.player1.equals(m[1])) {
						myRoom.player1ready=false;
						for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
								myRoom.ccu.get(j).dos.writeUTF("readycancel//"+"player1//"+m[1]);	
							}
						}else if(myRoom.player2.equals(m[1])){
							myRoom.player2ready=false;
							for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
								myRoom.ccu.get(j).dos.writeUTF("readycancel//"+"player2//"+m[1]);	
							}
						}
						if(myRoom.player1ready&myRoom.player2ready) {
							for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
									myRoom.ccu.get(j).dos.writeUTF("allready//"+myRoom.ccu.get(j).nickname);
									System.out.println(myRoom.ccu.get(j).nickname);
							}
						}
				}
				/* 방 생성 */
				else if(m[0].equals(croomTag)) {
					myRoom = new Room();	//새로운 Room 객체 생성 후 myRoom에 초기화
					myRoom.title = m[1];	//방 제목을 m[1]로 설정
					myRoom.count++;			//방의 인원수 하나 추가
					myRoom.pcount++;
					myRoom.player1=nickname;
					myRoom.player1image=db.getImage(nickname);
					room.add(myRoom);		//room 배열에 myRoom을 추가
					
					myRoom.ccu.add(this);
					myRoom.player.add(this);
					//myRoom의 접속인원에 클라이언트 추가
					wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
					
					dos.writeUTF(croomTag + "//OKAY");
					System.out.println("[Server] "+ nickname + " : 방 '" + m[1] + "' 생성");
					
					sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
					sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
				}else if(m[0].equals("imageset")) {
					dos.writeUTF("imageset"+"//"+myRoom.player1image);
				}else if(m[0].equals("imageset2")) {
					dos.writeUTF("imageset2"+"//"+db.getImage(nickname));
				}else if(m[0].equals("imageset3")) {
					dos.writeUTF("imageset3"+"//"+db.getImage(nickname));
				}else if(m[0].equals("changepro")){
					if(db.changepro(m[1], nickname)) {
						dos.writeUTF("changepro//ok");
					}else {
						dos.writeUTF("changepro//no");
					}
				}
				else if(m[0].equals("seeroomTag")) {
					for(int i=0; i<room.size(); i++) {	//생성된 방의 개수만큼 반복
						Room r = room.get(i);
						if(r.title.equals(m[1])) {	//방 제목이 같고
							if(r.count >= 2) { 
							myRoom = room.get(i);	//myRoom에 두 조건이 맞는 i번째 room을 초기화
							myRoom.count++;
							myRoom.see.add(this);//방의 인원수 하나 추가
							wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
							myRoom.ccu.add(this);	//myRoom의 접속 인원에 클라이언트 추가
							for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
								if(!myRoom.ccu.get(j).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
									myRoom.ccu.get(j).dos.writeUTF("getomok" + "//");
									break;
								}
							}
							sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
							sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
							dos.writeUTF("imagesetEnter"+"//"+myRoom.player1image+"//"+myRoom.player2image+"//"+myRoom.player1+"//"+myRoom.player2);
							dos.writeUTF("see//"+"OKAY");
							System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 관전");
							}
							else {
									//방 인원수가 2명 이상이므로 입장 실패
									dos.writeUTF("see" + "//FAIL");
									System.out.println("[Server] 인원 초과. 입장 불가능");															
						}
				}
					}
					}
				/* 방 입장 */
				else if(m[0].equals(eroomTag)) {
					for(int i=0; i<room.size(); i++) {	//생성된 방의 개수만큼 반복
						Room r = room.get(i);
						if(r.title.equals(m[1])) {	//방 제목이 같고
							
							if(r.pcount < 2) {			//방 인원수가 2명보다 적을 때 입장 성공
								myRoom = room.get(i);	//myRoom에 두 조건이 맞는 i번째 room을 초기화
								myRoom.count++;
								myRoom.pcount++;//방의 인원수 하나 추가
								myRoom.player.add(this);
								if(!myRoom.player1.equals("player1")) {
								myRoom.player2 = nickname;
								myRoom.player2image=db.getImage(nickname);
								}else {
									myRoom.player1 = nickname;	
									myRoom.player1image=db.getImage(nickname);
								}
								
								wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
								myRoom.ccu.add(this);	//myRoom의 접속 인원에 클라이언트 추가
								
								sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
								sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
								for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
									
										myRoom.ccu.get(j).dos.writeUTF("imagesetEnter"+"//"+myRoom.player1image+"//"+myRoom.player2image+"//"+myRoom.player1+"//"+myRoom.player2);
									
								}
								dos.writeUTF(eroomTag + "//OKAY");
								System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 입장");
							}
							
							else {	//방 인원수가 2명 이상이므로 입장 실패
								dos.writeUTF(eroomTag + "//FAIL");
								System.out.println("[Server] 인원 초과. 입장 불가능");
							}
						}
						
						else {	//같은 방 제목이 없으니 입장 실패
							dos.writeUTF(eroomTag + "//FAIL");
							System.out.println("[Server] " + nickname + " : 방 '" + m[1] + "' 입장 오류");
						}
						}
					}
				else if(m[0].equals("chggametosee")) {
					
						if(myRoom.player1.equals(m[1])) {
							myRoom.player1 ="player1";
							myRoom.player1image ="no";
							myRoom.player.remove(this);
							myRoom.pcount--;
							myRoom.see.add(this);
							
						}else if(myRoom.player2.equals(m[1])) {
							myRoom.player2 ="player2";
							myRoom.player2image ="no";
							myRoom.player.remove(this);
							myRoom.pcount--;
							myRoom.see.add(this);
						}
						sendRoom(roomUser());
						for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
								myRoom.ccu.get(j).dos.writeUTF("imagesetEnter"+"//"+myRoom.player1image+"//"+myRoom.player2image+"//"+myRoom.player1+"//"+myRoom.player2);
						}
						
				}else if(m[0].equals("chgseetogame")) {
					if(myRoom.player1.equals("player1")) {
						myRoom.player1 =m[1];
						myRoom.player1image =db.getImage(m[1]);
						myRoom.player.add(this);
						myRoom.pcount++;
						myRoom.see.remove(this);					
					}else if(myRoom.player2.equals("player2")) {
						myRoom.player2 =m[1];
						myRoom.player2image =db.getImage(m[1]);
						myRoom.player.add(this);
						myRoom.pcount++;
						myRoom.see.remove(this);
					}
					sendRoom(roomUser());
					for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
							myRoom.ccu.get(j).dos.writeUTF("imagesetEnter"+"//"+myRoom.player1image+"//"+myRoom.player2image+"//"+myRoom.player1+"//"+myRoom.player2);
					}
				}
				else if(m[0].equals("getomok")) {
					for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
						if(!myRoom.ccu.get(j).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
							myRoom.ccu.get(j).dos.writeUTF("setomok"+"//"+m[1]);
							System.out.println("set오목 보냄"+m[1]);
						}
					}
					
				}
				/* 전적 조회 */
				else if(m[0].equals(searchTag)) {
					String mm = db.searchRank(m[1]);
					
					if(!mm.equals("null")) {	//조회 성공
						dos.writeUTF(searchTag + "//" + mm);	//태그와 조회한 내용을 같이 전송
					}
					
					else {	//조회 실패
						dos.writeUTF(searchTag + "//FAIL");
					}
				}
				
				/* 프로그램 종료 */
				else if(m[0].equals(pexitTag)) {
					auser.remove(this);		//전체 접속 인원에서 클라이언트 삭제
					wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제
					
					sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
				}
				
				
				/* 방 퇴장 */
				else if(m[0].equals(rexitTag)) {
					myRoom.count--;				//myRoom의 인원수 하나 삭제
					if(myRoom.player1.equals(nickname)) {
						myRoom.player.remove(this);
						myRoom.player1="player1";
						myRoom.player1image="no";
						myRoom.pcount--;
					}else if(myRoom.player2.equals(nickname)) {
						myRoom.player.remove(this);
						myRoom.player2="player2";
						myRoom.player2image="no";
						myRoom.pcount--;
					}else {
					myRoom.see.remove(this);
					}
					for(int j=0; j<myRoom.ccu.size(); j++) {	//myRoom의 인원수만큼 반복
						if(!myRoom.ccu.get(j).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
							myRoom.ccu.get(j).dos.writeUTF("imagesetEnter"+"//"+myRoom.player1image+"//"+myRoom.player2image+"//"+myRoom.player1+"//"+myRoom.player2);
							
						}
					}
					myRoom.ccu.remove(this);	//myRoom의 접속 인원에서 클라이언트 삭제
					wuser.add(this);			//대기실 접속 인원에 클라이언트 추가
					
					System.out.println("[Server] " + nickname + " : 방 '" + myRoom.title + "' 퇴장");
					
					if(myRoom.count==0) {	//myRoom의 인원수가 0이면 myRoom을 room 배열에서 삭제
						room.remove(myRoom);
					}
					
					if(room.size() != 0) {	//생성된 room의 개수가 0이 아니면 방에 입장한 인원에 방 인원 목록을 전송
						sendRoom(roomUser());
						
					}
					
					sendWait(roomInfo());		//대기실 접속 인원에 방 목록을 전송
					sendWait(connectedUser());	//대기실 접속 인원에 전체 접속 인원을 전송
				}
				
				/* 오목 */
				else if(m[0].equals(omokTag)) {
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
							myRoom.ccu.get(i).dos.writeUTF(omokTag + "//" + m[1] + "//" + m[2] + "//" + m[3]);
						}
					}
					if(m[3].equals("BLACK")) {
						m[3]="1";
					}else {
						m[3]="-1";
					}
					myRoom.replay.append(m[1]+","+m[2]+","+m[3]+"@");
					System.out.println(myRoom.replay.toString());
				}
				else if(m[0].equals("getnick")) {
						String othernick="";
						for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//방 접속 인원 중 관전자가 아니고 클라이언트와 다른 닉네임의 클라이언트에게만 전송
								 if(myRoom.ccu.get(i).nickname.equals(myRoom.player1)||myRoom.ccu.get(i).nickname.equals(myRoom.player2)) {
							othernick = myRoom.ccu.get(i).nickname;
							}
						}
						}
						if(othernick=="") {
							dos.writeUTF("getnick" + "//" +nickname+"//"+"alone");
						}else {
						dos.writeUTF("getnick" + "//" +nickname+"//"+othernick);
						
						
							}
						}
				else if(m[0].equals("getmynick")) {
					System.out.println(nickname +"getmynick 호출~");
					dos.writeUTF("getmynick//"+nickname);
				}
				//이미지 얻어오기
				else if(m[0].equals("getimage")) {
					String imagedata="";
					imagedata = db.getImage(m[1]);
					dos.writeUTF("getimage"+"//"+imagedata);
				}
				//닉네임으로 누구랑 채팅했었는지 리스트 얻어오기
				else if(m[0].equals("chatnick")) {
					List<String> nicknameList =db.getNicknameListFromDatabase(m[1]);
					Gson gson = new Gson();
					String jsonData = gson.toJson(nicknameList);
					dos.writeUTF("chatnick"+"//"+jsonData);
					System.out.println("json까지 보냄");
					System.out.println(nicknameList.size()+"이게 닉네임 리스트"+jsonData);
				}
				else if(m[0].equals("replaynick")) {
					List<String> nicknameList =db.getnickreplay(m[1]);
					Gson gson = new Gson();
					String jsonData = gson.toJson(nicknameList);
					dos.writeUTF("replaynick"+"//"+jsonData);
					System.out.println("json까지 보냄");
					System.out.println(nicknameList.size()+"이게 닉네임 리스트"+jsonData);
				}
				//닉네임1,닉네임2,날짜로 채팅기록 얻어오기
				else if(m[0].equals("getchat")) {
					String chatRecord = db.getChatRecordFromDatabase(m[1],m[2],m[3]);
					dos.writeUTF("getchat"+"//"+chatRecord);
				}
				else if(m[0].equals("getreplay")) {
					String replay =db.getreplay(m[1],m[2],m[3]);
					dos.writeUTF("getreplay"+"//"+replay);
				}
				else if(m[0].equals("allchat")) {
					String message = m[1];
					sendAllWait(message);
				}
				/* 승리 및 전적 업데이트 */
				else if(m[0].equals(winTag)) {
					System.out.println("[Server] " + nickname + " 승리");
					
					if(db.winRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
						dos.writeUTF(recordTag + "//OKAY");
					} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
						dos.writeUTF(recordTag + "//FAIL");
					}
					
					if(!m[1].equals("nochat")) {
						String win;
						if(nickname.equals(myRoom.player1)) {
							win  = myRoom.player1;
						}else {
							win = myRoom.player2;
						}
						db.replay(myRoom.player1,myRoom.player2,myRoom.replay.toString(),m[4],win);
						if(db.chatRecord(m[1], m[2], m[3],m[4])) {
						System.out.println("채팅기록 성공");
						}
					}else {
						String win;
						LocalDateTime endTime = LocalDateTime.now();
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				        String time = endTime.format(formatter);
				        if(nickname.equals(myRoom.player1)) {
							win  = myRoom.player1;
						}else {
							win = myRoom.player2;
						}
						db.replay(myRoom.player1,myRoom.player2,myRoom.replay.toString(),time,win);
						System.out.println("채팅내역 없음");
					}
					
					for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
						
						/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
						if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
							 if(myRoom.ccu.get(i).nickname.equals(myRoom.player1)||myRoom.ccu.get(i).nickname.equals(myRoom.player2)) {
							myRoom.ccu.get(i).dos.writeUTF(loseTag + "//");
							
							if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
								myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
							} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
								myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
							}
						}
							 
							}
						 if(!(myRoom.ccu.get(i).nickname.equals(myRoom.player1)||myRoom.ccu.get(i).nickname.equals(myRoom.player2))) {
							 myRoom.ccu.get(i).dos.writeUTF("endgame//");
							 System.out.println(myRoom.player1+myRoom.player2);
						 }
					}
				}
				
				/* 패배, 기권 및 전적 업데이트 */
				else if(m[0].equals(loseTag)) {
					if(myRoom.count==1) {	//기권을 했는데 방 접속 인원이 1명일 때 전적 미반영을 전송
						dos.writeUTF(recordTag + "//NO");
					}
					
					else if(myRoom.count>=2) {	//기권 및 패배를 했을 때 방 접속 인원이 2명일 때
						dos.writeUTF(loseTag + "//");
						
						if(db.loseRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
							dos.writeUTF(recordTag + "//OKAY");
						} else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
							dos.writeUTF(recordTag + "//FAIL");
						}
						if(!m[1].equals("nochat")) {
							String win;
							if(nickname.equals(myRoom.player1)) {
								win  = myRoom.player2;
							}else {
								win = myRoom.player1;
							}
							db.replay(myRoom.player1,myRoom.player2,myRoom.replay.toString(),m[4],win);
							if(db.chatRecord(m[1], m[2], m[3],m[4])) {
							System.out.println("채팅기록 성공");
							}
						}else {
							String win;
							LocalDateTime endTime = LocalDateTime.now();
					        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					        String time = endTime.format(formatter);
					        if(nickname.equals(myRoom.player1)) {
								win  = myRoom.player1;
							}else {
								win = myRoom.player2;
							}
							db.replay(myRoom.player1,myRoom.player2,myRoom.replay.toString(),time,win);
							System.out.println("채팅내역 없음");
						}
						
						for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복
							/* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
							
							if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
								 if(myRoom.ccu.get(i).nickname.equals(myRoom.player1)||myRoom.ccu.get(i).nickname.equals(myRoom.player2)) {
								myRoom.ccu.get(i).dos.writeUTF(winTag + "//");
								
							
								if(db.winRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
									myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
								} else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
									myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
								}
							}
								 
						}
							 if(!(myRoom.ccu.get(i).nickname.equals(myRoom.player1)||myRoom.ccu.get(i).nickname.equals(myRoom.player2))) {
								 myRoom.ccu.get(i).dos.writeUTF("endgame//");
								 System.out.println(myRoom.player1+myRoom.player2);
							 }
					}
					}
				}
			}
			
		} catch(IOException e) {
			System.out.println("[Server] 입출력 오류 > " + e.toString());
		}
	}
	 void sendUserInfo() {
	    StringBuilder userInfo = new StringBuilder();
	    StringJoiner joiner = new StringJoiner(",", "(", ")");
	    
	    for (CCUser user : auser) {
	        userInfo.append("Nickname: ").append(user.nickname).append("\n");
	        joiner.add("'"+user.nickname+"'");
	        // 다른 사용자 정보도 필요하다면 추가
	    }
	    try {
	        dos.writeUTF(adminTag + "//" + userInfo.toString()+"//now");
	    } catch (IOException e) {
	        System.out.println("실패");
	    }
	    System.out.println(joiner.toString());
	}
	 String nickSql() {
		 StringJoiner joiner = new StringJoiner(",", "(", ")");
		    
		    for (CCUser user : auser) {
		        joiner.add("'"+user.nickname+"'");
		        // 다른 사용자 정보도 필요하다면 추가
		    }
		    
		    System.out.println(joiner.toString());
		    return joiner.toString();
		}
	

	/* 현재 존재하는 방의 목록을 조회 */
	String roomInfo() {
		String msg = vroomTag + "//";
		
		for(int i=0; i<room.size(); i++) {
			msg = msg + room.get(i).title + " : " + room.get(i).count + "@";
		}
		return msg;
	}
	
	/* 클라이언트가 입장한 방의 인원을 조회 */
	String roomUser() {
		String player=myRoom.player1+"@"+myRoom.player2;
		String see="";
		for(int j=0; j<myRoom.see.size(); j++) {	//myRoom의 인원수만큼 반복
			see=see+myRoom.see.get(j).nickname+"@";
			}
		if(see.equals("")) {
			see="nosee";
		}
		
		return uroomTag+"//"+player+"//"+see;
	}
	
	/* 접속한 모든 회원 목록을 조회 */
	String connectedUser() {
		String msg = cuserTag + "//";
		 Set<String> uniqueNicknames = new HashSet<>();
		for(int i=0; i<auser.size(); i++) {
			//msg = msg + auser.get(i).nickname + "@";
			uniqueNicknames.add(auser.get(i).nickname);
		}
		StringBuilder userString = new StringBuilder();
	    for (String nickname : uniqueNicknames) {
	        userString.append(nickname).append("@");
	    }
	    msg += userString.toString();
		return msg;
	}
	
	/* 대기실에 있는 모든 회원에게 메시지 전송 */
	void sendWait(String m) {
		for(int i=0; i<wuser.size(); i++) {
			try {
				wuser.get(i).dos.writeUTF(m);
			} catch(IOException e) {
				wuser.remove(i--);
			}
		}
	}
	
	/* 방에 입장한 모든 회원에게 메시지 전송 */
	void sendRoom(String m) {
		for(int i=0; i<myRoom.ccu.size(); i++) {
			try {
				myRoom.ccu.get(i).dos.writeUTF(m);
			} catch(IOException e) {
				myRoom.ccu.remove(i--);
			}
		}
	}
}