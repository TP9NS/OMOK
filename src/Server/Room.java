package Server;
import java.util.*;

//멀티룸을 지원하기 위한 기능을 구현할 클래스.
//각 Room들을 객체로 관리. Room 객체는 제목, 인원수, 클라이언트 객체 배열을 필드로 가지며 이들을 관리한다.
public class Room {
	Vector<CCUser> ccu;
	Vector<CCUser> player;
	Vector<CCUser> see;
	String title;
	int count = 0;
	int pcount = 0;
	String player1="player1";
	String player2="player2";
	String player1image= "no";
	String player2image = "no";
	StringBuilder replay = new StringBuilder();
	boolean player1ready =false;
	boolean player2ready=false;
	String seeUser1 ="";
	String seeUser2="";
	Room() {	//Room 객체 생성 시 접속(입장)한 클라이언트 객체에 대한 정보를 Room에 저장한다.
		ccu = new Vector<>();
		player = new Vector<>();
		see = new Vector<>();
	}
}
