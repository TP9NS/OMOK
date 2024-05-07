package Server;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//클라이언트가 요청한 데이터베이스 업데이트 및 쿼리 작업을 수행하는 클래스.
//서버에서 객체 생성 시에 데이터베이스 연동 작업을 수행하고 다른 부가적인 작업들은 메소드를 통해 서버에서 불려지면 수행하도록 한다.
public class Database {
	/* 데이터베이스와의 연결에 사용할 변수들 */
	Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost:3306/omok?serverTimezone=Asia/Seoul";
	String user = "root";
	String passwd = "psh0811";
	
	Database() {	//Database 객체 생성 시 데이터베이스 서버와 연결한다.
		try {	//데이터베이스 연결은 try-catch문으로 예외를 잡아준다.
			//데이터베이스와 연결한다.
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, passwd);
			stmt = con.createStatement();
			System.out.println("[Server] MySQL 서버 연동 성공");	//데이터베이스 연결에 성공하면 성공을 콘솔로 알린다.
		} catch(Exception e) {	//데이터베이스 연결에 예외가 발생했을 때 실패를 콘솔로 알린다.
			System.out.println("[Server] MySQL 서버 연동 실패> " + e.toString());
		}
	}
	
	//로그인 여부를 확인하는 메소드. 서버에 닉네임을 String 형식으로 반환한다.
	String loginCheck(String _i, String _p) {
		String nickname = "null";	//반환할 닉네임 변수를 "null"로 초기화.
		
		//매개변수로 받은 id와 password값을 id와 pw값에 초기화한다.
		String id = _i;
		String pw = _p;
		
		try {
			//id와 일치하는 비밀번호와 닉네임이 있는지 조회한다.
			String checkingStr = "SELECT password, nickname FROM member WHERE id='" + id + "'";
			ResultSet result = stmt.executeQuery(checkingStr);
			
			int count = 0;
			while(result.next()) {
				//조회한 비밀번호와 pw 값을 비교.
				if(pw.equals(result.getString("password"))) {	//true일 경우 nickname에 조회한 닉네임에 반환하고 로그인 성공을 콘솔로 알린다.
					nickname = result.getString("nickname");
					System.out.println("[Server] 로그인 성공");
				}
				
				else {	//false일 경우 nickname을 "null"로 초기화하고 로그인 실패를 콘솔로 알린다.
					nickname = "null";
					System.out.println("[Server] 로그인 실패");
				}
				count++;
			}
		} catch(Exception e) {	//조회에 실패했을 때 nickname을 "null"로 초기화. 실패를 콘솔로 알린다.
			nickname = "null";
			System.out.println("[Server] 로그인 실패 > " + e.toString());
		}
		
		return nickname;	//nickname 반환
	}
	
	//회원가입을 수행하는 메소드. 회원가입에 성공하면 true, 실패하면 false를 반환한다.
	boolean joinCheck(String _n,String _pp, String _b ,String _nn, String _i, String _p, String _e,String ex,String _a,String image) {
		boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false
		
		//매개변수로 받은 각 문자열들을 각 변수에 초기화한다.
		String na = _n;
		String p = _pp;
		String bi=_b;
		String nn = _nn;
		String id = _i;
		String pw = _p;
		String em = _e;
		String exp =ex;
		String ad = _a;
		String uimage = image;
		
		try {
			//member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 승, 패는 초기값을 숫자 0으로 한다.
			String insertStr = "INSERT INTO member VALUES('" + na + "', '"+p+"', '" +bi+ "', '" + nn + "', '" + id + "', '" + pw + "', '" + em + "', '" + exp + "','"+ ad +"'"+", 0, 0,'"+uimage+"')";
			stmt.executeUpdate(insertStr);
			
			flag = true;	//업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
			System.out.println("[Server] 회원가입 성공");
		} catch(Exception e) {	//회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 회원가입 실패 > " + e.toString());
		}
		
		return flag;	//flag 반환
	}
	boolean chatRecord(String nick,String nick2,String chat,String time) {
		boolean flag=false;
		try {
			//member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 승, 패는 초기값을 숫자 0으로 한다.
			String insertStr = "INSERT INTO chat VALUES('" + nick + "','"+nick2+"','" +chat+ "','"+time+"')";
			stmt.executeUpdate(insertStr);
			
			flag = true;	//업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
			System.out.println("[Server] 채팅기록 저장 성공 ");
		} catch(Exception e) {	//회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 채팅기록 저장 실패 > " + e.toString());
		}
		return flag;
	}
	boolean replay(String nick,String nick2,String replay,String time, String win) {
		boolean flag=false;
		try {
			//member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 승, 패는 초기값을 숫자 0으로 한다.
			String insertStr = "INSERT INTO replay VALUES('" + nick + "','"+nick2+"','" +replay+ "','"+time+"','"+win+"')";
			stmt.executeUpdate(insertStr);
			
			flag = true;	//업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
			System.out.println("[Server] 리플레이 저장 성공 ");
		} catch(Exception e) {	//회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 리플레이 저장 실패 > " + e.toString());
		}
		return flag;
	}
	//아이디나 닉네임이 중복되었는지 확인해주는 메소드. 중복 값이 존재하면 false, 존재하지 않으면 true를 반환한다.
	boolean overCheck(String _a, String _v) {
		boolean flag = true;	//참거짓을 반환할 flag 변수. 초기값은 false
		
		//att는 속성(아이디, 닉네임)을 구분하고, val은 확인할 값이 초기화.
		String att = _a;
		String val = _v;
		
		try {
			String selectStr = "SELECT " + att + " FROM member WHERE " + att + " = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(selectStr);
	        preparedStatement.setString(1, val);

	        ResultSet result = preparedStatement.executeQuery();
			 if (result.next()) {
		            // 결과가 있으면 중복이 존재하므로 flag를 false로 설정
		            flag = false;
		            System.out.println("[Server] 중복 확인 성공");
		        } else {
		            // 결과가 없으면 중복이 존재하지 않으므로 flag는 그대로 true
		            System.out.println("[Server] 중복 확인 성공");
		        }
			 result.close();
		        preparedStatement.close();
		}catch (Exception e) {
	        System.out.println("[Server] 중복 확인 실패 > " + e.toString());
	    }
		        
		
		return flag;	//flag 반환
	}
	
	//데이터베이스에 저장된 자신의 정보를 조회하는 메소드. 조회한 정보들을 String 형태로 반환한다.
	String viewInfo(String _nn) {
		String msg = "null";	//반환할 문자열 변수를 "null"로 초기화.
		
		//매개변수로 받은 닉네임을 nick에 초기화한다.
		String nick = _nn;
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 이름과 이메일 정보를 조회한다.
			String viewStr = "SELECT name, email FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(viewStr);
			
			int count = 0;
			while(result.next()) {
				//msg에 "이름//닉네임//이메일" 형태로 초기화한다.
				msg = result.getString("name") + "//" + nick + "//" + result.getString("email");
				count++;
			}
			System.out.println("[Server] 회원정보 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 회원정보 조회 실패 > " + e.toString());
		}
		
		return msg;	//msg 반환
	}
	
	//회원정보를 변경을 수행하는 메소드. 변경에 성공하면 true, 실패하면 false를 반환한다.
	boolean changeInfo(String _id, String _a, String _v) {
		boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.
		
		//매개변수로 받은 정보들을 초기화한다. att는 속성(이름, 이메일, 비밀번호) 구분용이고 val은 바꿀 값.
		String id = _id;
		String att = _a;
		String val = _v;
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 att(이름, 이메일, 비밀번호)를 val로 변경한다.
			String changeStr = "UPDATE member SET " + att + "='" + val + "' WHERE id='" + id +"'";
			stmt.executeUpdate(changeStr);
			
			flag = true;	//정상적으로 수행되면 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 회원정보 변경 성공");
		} catch(Exception e) {	//정상적으로 수행하지 못하면 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 회원정보 변경 실패 > " + e.toString());
		}
		
		return flag;	//flag 반환
	}
	boolean changepro(String image, String nick) {
		boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.
		
		//매개변수로 받은 정보들을 초기화한다. att는 속성(이름, 이메일, 비밀번호) 구분용이고 val은 바꿀 값.
		String nick1 = nick;
		String imagedata = image;
		
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 att(이름, 이메일, 비밀번호)를 val로 변경한다.
			String changeStr = "UPDATE member SET " + "image" + "='" + image + "' WHERE nickname='" + nick +"'";
			stmt.executeUpdate(changeStr);
			
			flag = true;	//정상적으로 수행되면 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 프로필 변경 성공");
		} catch(Exception e) {	//정상적으로 수행하지 못하면 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 회원정보 변경 실패 > " + e.toString());
		}
		
		return flag;	//flag 반환
	}
	
	
	//전체 회원의 전적을 조회하는 메소드. 모든 회원의 전적을 String 형태로 반환한다.
	String viewRank() {
		String msg = "";	//전적을 받을 문자열. 초기값은 ""로 한다.
		
		try {
			//member 테이블의 닉네임, 승, 패를 모두 조회한다.
			String viewStr = "SELECT nickname, win, lose FROM member";
			ResultSet result = stmt.executeQuery(viewStr);
			
			int count = 0;
			while(result.next()) {
				//기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
				msg = msg + result.getString("nickname") + " : " + result.getInt("win") + "승 " + result.getInt("lose") + "패@";
				count++;
			}
			System.out.println("[Server] 전적 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}
		
		return msg;	//msg 반환
	}
	
	//한 명의 회원의 전적을 조회하는 메소드. 해당 회원의 전적을 String 형태로 반환한다.
	String searchRank(String _nn) {
		String msg = "null";	//전적을 받을 문자열. 초기값은 "null"로 한다.
		
		//매개변수로 받은 닉네임을 초기화한다.
		String nick = _nn;
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 승, 패를 조회한다.
			String searchStr = "SELECT win, lose FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);
			
			int count = 0;
			while(result.next()) {
				//msg에 "닉네임 : n승 n패" 형태의 문자열을 초기화한다.
				msg = nick + " : " + result.getInt("win") + "승 " + result.getInt("lose") + "패";
				count++;
			}
			System.out.println("[Server] 전적 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
		} catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
			System.out.println("[Server] 전적 조회 실패 > " + e.toString());
		}
		
		return msg;	//msg 반환
	}
	
	//게임 승리 시 전적을 업데이트하는 메소드. 조회 및 업데이트에 성공하면 true, 실패하면 false를 반환한다.
	boolean winRecord(String _nn) {
		boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.
		
		//매개변수로 받은 닉네임과 조회한 승리 횟수를 저장할 변수. num의 초기값은 0.
		String nick = _nn;
		int num = 0;
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 조회한다.
			String searchStr = "SELECT win FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);
			
			int count = 0;
			while(result.next()) {
				//num에 조회한 승리 횟수를 초기화.
				num = result.getInt("win");
				count++;
			}
			num++;	//승리 횟수를 올림
			
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 num으로 업데이트한다.
			String changeStr = "UPDATE member SET win=" + num + " WHERE nickname='" + nick +"'";
			stmt.executeUpdate(changeStr);
			flag = true;	//조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 전적 업데이트 성공");
		} catch(Exception e) {	//조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] 전적 업데이트 실패 > " + e.toString());
		}
		
		return flag;	//flag 반환
	}
	
	//게임 패배 시 전적을 업데이트하는 메소드. 조회 및 업데이트에 성공하면 true, 실패하면 false를 반환한다.
	boolean loseRecord(String _nn) {
		boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.
		
		//매개변수로 받은 닉네임과 조회한 패배 횟수를 저장할 변수. num의 초기값은 0.
		String nick =  _nn;
		int num = 0;
		
		try {
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 패배 횟수를 조회한다.
			String searchStr = "SELECT lose FROM member WHERE nickname='" + nick + "'";
			ResultSet result = stmt.executeQuery(searchStr);
			
			int count = 0;
			while(result.next()) {
				//num에 조회한 패배 횟수를 초기화.
				num = result.getInt("lose");
				count++;
			}
			num++;	//패배 횟수를 올림
			
			//member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 num으로 업데이트한다.
			String changeStr = "UPDATE member SET lose=" + num + " WHERE nickname='" + nick +"'";
			stmt.executeUpdate(changeStr);
			flag = true;	//조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
			System.out.println("[Server] 전적 업데이트 성공");
		} catch(Exception e) {	//조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
			flag = false;
			System.out.println("[Server] Error: > " + e.toString());
		}
		
		return flag;	//flag 반환
	}
	void deleteUserInfo(String _id) {
	    // 매개변수로 받은 아이디를 초기화
	    String idToDelete = _id;

	    try {
	        // 해당 아이디의 회원 정보를 조회
	        String selectUserInfo = "SELECT * FROM member WHERE id='" + idToDelete + "'";
	        ResultSet result = stmt.executeQuery(selectUserInfo);

	        // delmember 테이블에 삭제된 회원 정보를 삽입하는 쿼리
	       String insertIntoDelMember = "INSERT INTO delmember SELECT * FROM member WHERE id='" + idToDelete + "'";

	        // 삭제할 아이디의 정보를 delmember 테이블로 복사
	        stmt.executeUpdate(insertIntoDelMember);

	        // 해당 아이디의 회원 정보를 삭제
	        String deleteUserInfo = "DELETE FROM member WHERE id='" + idToDelete + "'";
	        int deletedRows = stmt.executeUpdate(deleteUserInfo);

	        if (deletedRows > 0) {
	            System.out.println("[Server] 회원 정보 삭제 및 delmember 테이블에 저장 성공");
	        } else {
	            System.out.println("[Server] 해당 아이디의 회원 정보가 존재하지 않습니다.");
	        }
	    } catch (Exception e) {
	        System.out.println("[Server] 회원 정보 삭제 실패 > " + e.toString());
	    }
	}
	// 메소드 추가: 모든 사용자 정보 조회
	String getAllUsersInfo() {
		 StringBuilder allUsersInfo = new StringBuilder();

		    try {
		        // member 테이블에서 모든 사용자의 정보를 조회한다.
		        String queryStr = "SELECT * FROM member";
		        ResultSet result = stmt.executeQuery(queryStr);

		        // 테이블 헤더 추가
		        allUsersInfo.append("이름\t전화번호\t생년월일\t닉네임\t아이디\t비밀번호\t이메일\t우편번호\t상세주소\t승리\t패배\n");

		        while (result.next()) {
		            // 각 사용자의 정보를 테이블 형태로 추가한다.
		            String userInfo = result.getString("name") + "\t" +
		            		 			result.getString("number") + "\t"+
		                              result.getString("birth") + "\t" +
		                              result.getString("nickname") + "\t" +
		                              result.getString("id") + "\t" +
		                              result.getString("password") + "\t" +
		                              result.getString("email") + "\t" +
		                              result.getString("express") + "\t" +
		                              result.getString("addr") + "\t" +
		                              result.getInt("win") + "\t" +
		                              result.getInt("lose") + "\n";
		            allUsersInfo.append(userInfo);
		        }

		        System.out.println("[Server] 모든 사용자 정보 조회 성공");
		    } catch (Exception e) {
		        System.out.println("[Server] 모든 사용자 정보 조회 실패 > " + e.toString());
		    }

		    return allUsersInfo.toString();
	}
	String getNowUsersInfo(String nick) {
		 StringBuilder nowUsersInfo = new StringBuilder();

		    try {
		        // member 테이블에서 모든 사용자의 정보를 조회한다.
		        String queryStr = "SELECT * FROM member where nickname in "+nick;
		        ResultSet result = stmt.executeQuery(queryStr);

		        // 테이블 헤더 추가
		        nowUsersInfo.append("이름\t전화번호\t생년월일\t닉네임\t아이디\t비밀번호\t이메일\t우편번호\t상세주소\t승리\t패배\n");

		        while (result.next()) {
		            // 각 사용자의 정보를 테이블 형태로 추가한다.
		            String userInfo = result.getString("name") + "\t" +
		            		result.getString("number") + "\t"+
		                              result.getString("birth") + "\t" +
		                              result.getString("nickname") + "\t" +
		                              result.getString("id") + "\t" +
		                              result.getString("password") + "\t" +
		                              result.getString("email") + "\t" +
		                              result.getString("express") + "\t" +
		                              result.getString("addr") + "\t" +
		                              result.getInt("win") + "\t" +
		                              result.getInt("lose") + "\n";
		            nowUsersInfo.append(userInfo);
		        }

		        System.out.println("[Server] 모든 사용자 정보 조회 성공");
		    } catch (Exception e) {
		        System.out.println("[Server] 모든 사용자 정보 조회 실패 > " + e.toString());
		    }

		    return nowUsersInfo.toString();
	}
	String getoneUsersInfo(String ati,String nick) {
		 StringBuilder allUsersInfo = new StringBuilder();

		    try {
		        // member 테이블에서 모든 사용자의 정보를 조회한다.
		        String queryStr = "SELECT * FROM member where "+ati+" = '"+nick+"'";
		        ResultSet result = stmt.executeQuery(queryStr);

		        // 테이블 헤더 추가
		        allUsersInfo.append("이름\t전화번호\t생년월일\t닉네임\t아이디\t비밀번호\t이메일\t우편번호\t상세주소\t승리\t패배\n");

		        while (result.next()) {
		            // 각 사용자의 정보를 테이블 형태로 추가한다.
		            String userInfo = result.getString("name") + "\t" +
		            				  result.getString("number") + "\t"+
		                              result.getString("birth") + "\t" +
		                              result.getString("nickname") + "\t" +
		                              result.getString("id") + "\t" +
		                              result.getString("password") + "\t" +
		                              result.getString("email") + "\t" +
		                              result.getString("express") + "\t" +
		                              result.getString("addr") + "\t" +
		                              result.getInt("win") + "\t" +
		                              result.getInt("lose") + "\n";
		            allUsersInfo.append(userInfo);
		        }

		        System.out.println("[Server] 모든 사용자 정보 조회 성공");
		    } catch (Exception e) {
		        System.out.println("[Server] 모든 사용자 정보 조회 실패 > " + e.toString());
		    }

		    return allUsersInfo.toString();
	}
	public List<String> getNicknameListFromDatabase(String nick) {
	    List<String> nicknameList = new ArrayList<>();

	    try {
	        // member 테이블에서 모든 닉네임을 조회합니다.
	        String queryStr = "SELECT id1,id2,time FROM chat WHERE id1 ='"+nick+"'OR id2='"+nick+"'";
	        ResultSet result = stmt.executeQuery(queryStr);
	        
	        while (result.next()) {
	        	
	            // 조회된 각 닉네임을 리스트에 추가합니다.
	        	if(result.getString("id1").equals(nick)) {
	        		String nickname = result.getString("id2")+"-채팅 일 시 :"+result.getString("time");
		            nicknameList.add(nickname);	
	        	}else if(result.getString("id2").equals(nick)) {
	        		String nickname = result.getString("id1")+"-채팅 일 시 :"+result.getString("time");
		            nicknameList.add(nickname);	
	        	}
	            
	        }

	        System.out.println("[Server] 닉네임 목록 조회 성공");
	    } catch (Exception e) {
	        System.out.println("[Server] 닉네임 목록 조회 실패 > " + e.toString());
	    }

	    return nicknameList;
	}
	public String getChatRecordFromDatabase(String nick1, String nick2,String time) {
	    StringBuilder chatRecord = new StringBuilder();

	    try {
	        // table에 저장된 두 닉네임 사이의 채팅 내역을 조회합니다.
	        String queryStr = "SELECT chat FROM chat WHERE (id1='" + nick1 + "' AND id2='" + nick2 +"' AND time ='"+time+ "') OR (id1='" + nick2 + "' AND id2='" + nick1 + "' AND time='"+time+"')";
	        ResultSet result = stmt.executeQuery(queryStr);
	 
	        while (result.next()) {
	            // 조회된 채팅 내역을 chatRecord에 추가합니다.
	           
	            String chat = result.getString("chat");
	            chatRecord.append(chat).append("\n");
	          
	            }
	        
	        System.out.println("[Server] 채팅 기록 조회 성공"+chatRecord.toString());
	    } catch (Exception e) {
	        System.out.println("[Server] 채팅 기록 조회 실패 > " + e.toString());
	    }

	    return chatRecord.toString();
	}
	public String getreplay(String nick1, String nick2,String time) {
		String player1="";
		String player2="";
		String replay="";
		String win="";
	    try {
	        // table에 저장된 두 닉네임 사이의 채팅 내역을 조회합니다.
	        String queryStr = "SELECT player1,player2,game,win FROM replay WHERE (player1='" + nick1 + "' AND player2='" + nick2 +"' AND time ='"+time+ "') OR (player1='" + nick2 + "' AND player2='" + nick1 + "' AND time='"+time+"')";
	        ResultSet result = stmt.executeQuery(queryStr);
	 
	        while (result.next()) {
	            // 조회된 채팅 내역을 chatRecord에 추가합니다.
	            player1 = result.getString("player1");
	            player2 = result.getString("player2");
	            replay = result.getString("game");
	            win = result.getString("win");
	            
	            }
	        
	        System.out.println("[Server] 리플레이 기록 조회 성공"+replay);
	    } catch (Exception e) {
	        System.out.println("[Server] 리플레이 기록 조회 실패 > " + e.toString());
	    }

	    return player1+"//"+player2+"//"+replay+"//"+win;
	}
	public String getImage(String nick) {
		String imageData = "";
		 try {
		        // 해당 닉네임과 일치하는 회원의 이미지 데이터 조회 쿼리
		        String queryStr = "SELECT image FROM member WHERE nickname='" + nick + "'";
		        ResultSet result = stmt.executeQuery(queryStr);

		        if (result.next()) {
		            // 이미지 데이터를 String으로 읽어오기
		            imageData = result.getString("image");
		            
		            if (imageData == null || imageData.equals("null") || imageData.isEmpty()) {
		                imageData = "no"; // 이미지 데이터가 비어있는 경우 "no"로 설정
		            }
		        } else {
		            imageData = "no"; // 해당 닉네임에 대한 데이터가 없는 경우 "no"로 설정
		        }

		        System.out.println("[Server] 이미지 데이터 조회 성공");
		    } catch (Exception e) {
		        System.out.println("[Server] 이미지 데이터 조회 실패 > " + e.toString());
		    }
		    return imageData;
	}
	public String findIDFromMember(String name, String birth, String email) {
	    String foundID = ""; // 반환할 ID를 저장할 변수

	    try {
	        // member 테이블에서 입력된 이름, 생년월일, 이메일을 가진 사용자의 ID 조회
	        String queryStr = "SELECT id FROM member WHERE name = '" + name + "' AND birth = '" + birth + "' AND email = '" + email + "'";
	        ResultSet result = stmt.executeQuery(queryStr);

	        if (result.next()) {
	            // 결과가 존재할 경우 해당 ID를 가져옴
	            foundID = result.getString("id");
	        } else {
	            foundID = "not_found"; // 해당하는 정보를 찾지 못한 경우
	        }

	        System.out.println("[Server] ID 조회 성공");
	    } catch (Exception e) {
	        System.out.println("[Server] ID 조회 실패 > " + e.toString());
	    }

	    return foundID;
	}
	public String findpasswordFromMember(String id,String name, String birth, String email) {
	    String foundpassword = ""; // 반환할 ID를 저장할 변수

	    try {
	        // member 테이블에서 입력된 이름, 생년월일, 이메일을 가진 사용자의 ID 조회
	        String queryStr = "SELECT password FROM member WHERE id = '"+id+"' and name = '" + name + "' AND birth = '" + birth + "' AND email = '" + email + "'";
	        ResultSet result = stmt.executeQuery(queryStr);

	        if (result.next()) {
	            // 결과가 존재할 경우 해당 ID를 가져옴
	            foundpassword = result.getString("password");
	        } else {
	            foundpassword = "not_found"; // 해당하는 정보를 찾지 못한 경우
	        }

	        System.out.println("[Server] ID 조회 성공");
	    } catch (Exception e) {
	        System.out.println("[Server] ID 조회 실패 > " + e.toString());
	    }

	    return foundpassword;
	}
	public List<String> getnickreplay(String nick) {
	    List<String> nicknameList = new ArrayList<>();

	    try {
	        // member 테이블에서 모든 닉네임을 조회합니다.
	        String queryStr = "SELECT player1,player2,time FROM replay WHERE player1 ='"+nick+"'OR player2='"+nick+"'";
	        ResultSet result = stmt.executeQuery(queryStr);
	        
	        while (result.next()) {
	        	
	            // 조회된 각 닉네임을 리스트에 추가합니다.
	        	if(result.getString("player1").equals(nick)) {
	        		String nickname = result.getString("player2")+"-게임 일 시 :"+result.getString("time");
		            nicknameList.add(nickname);	
	        	}else if(result.getString("player2").equals(nick)) {
	        		String nickname = result.getString("player1")+"-게임 일 시 :"+result.getString("time");
		            nicknameList.add(nickname);	
	        	}
	            
	        }

	        System.out.println("[Server] 닉네임 목록 조회 성공");
	    } catch (Exception e) {
	        System.out.println("[Server] 닉네임 목록 조회 실패 > " + e.toString());
	    }

	    return nicknameList;
	}
}
