

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class replayFrame extends JFrame implements ActionListener{
    private static final int num = 20; // 보드의 크기를 나타내는 변수
     private static final int x = 30; // 보드의 시작 x 좌표
     private static final int y = 30; // 보드의 시작 y 좌표
     private static final int size = 24; // 각 셀의 크기
     public static final int BLACK =1,WHITE = -1;
     public static final int width = 24;
     public static final int height = 24;
   public JPanel board;
   public JLabel PlayingData;
   public String nowData;
   public JButton prev,next,close;
   public JLabel infoText;
   JLabel black;
   JLabel white;
   public int stone[][] = new int [num][num];
   public ArrayList<int[]> test = new ArrayList<>();
   public int counter = 0;
   String player1="";
   String player2="";
   String win="";
   public replayFrame() {
      try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(800, 600);
      setLayout(new BorderLayout());
       JPanel leftPanel = new JPanel(); 
        leftPanel.setLayout(new BorderLayout());
       board = new Board();
       JPanel rightPanel = new JPanel() {
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
       rightPanel.setLayout(null);
       rightPanel.setPreferredSize(new Dimension(220, getHeight()));
       leftPanel.setPreferredSize(new Dimension(550, getHeight())); 
       
      //테스트 데이터(실제로는 플레이데이터)
           
            black = new JLabel("흑 :"+player1);
            white = new JLabel("백 :"+player2);
           black.setBounds(10, 40, 100, 30);
           white.setBounds(110, 40, 100, 30);
           black.setFont(new Font("맑은 고딕",Font.BOLD,14));
           white.setFont(new Font("맑은 고딕",Font.BOLD,14));
       prev = new JButton("이전으로");
       prev.setBounds(10,100,100,30);
       prev.setVisible(true);
       prev.addActionListener(this);
       next = new JButton("다음으로");
       next.setBounds(115,100,100,30);
       next.addActionListener(this);
       infoText = new JLabel("REPLAY");
       infoText.setBounds(75,10,150,50);
       close = new JButton("나가기");
       close.setBounds(120,500,100,30);
       nowData = "게임 시작 전";
       PlayingData = new JLabel(nowData);
       PlayingData.setFont(new Font("맑은 고딕",Font.BOLD,14));
       PlayingData.setBounds(50,200,150,50);
       
       infoText.setFont(new Font("맑은 고딕",Font.BOLD,20));
       close.addActionListener(this);
       
       
       leftPanel.add(board,BorderLayout.CENTER);
       rightPanel.add(prev);
       rightPanel.add(next);
       rightPanel.add(infoText);
       rightPanel.add(PlayingData);
       rightPanel.add(close);
       rightPanel.add(white);
       rightPanel.add(black);
       
       add(leftPanel,BorderLayout.LINE_START);
       add(rightPanel,BorderLayout.LINE_END);
       setVisible(true);
       
       
       
   }
   
   public void actionPerformed(ActionEvent e) {
      if(e.getSource() == close) {
         this.dispose();
      }
      else if(e.getSource() == next) {
         if(counter < test.size()) {
            int[] data =test.get(counter);
            stone[data[0]][data[1]] = data[2];
            board.repaint();
            System.out.println("Counter:"+counter);
            nowData = "총 "+test.size()+"개의 돌 중 "+(counter+1)+"수";
            PlayingData.setText(nowData);
            counter++;
         }
         else {
            JOptionPane.showMessageDialog(null,win+"가 승리 하였습니다.","안내 메세지",JOptionPane.INFORMATION_MESSAGE);   
         }
      }
      else if(e.getSource()==prev) {
         if(counter > 0) {
            int[] data = test.get(counter - 1);
            stone[data[0]][data[1]] = 0;
            board.repaint();
            counter--;
            nowData = "총 "+test.size()+"개의 돌 중 "+counter+"수";
            PlayingData.setText(nowData);
         }
         else {
            JOptionPane.showMessageDialog(null,"맨 처음 지점입니다.","에러 메세지",JOptionPane.ERROR_MESSAGE);   
         }
      }
      
   }
   
   
   
    class Board extends JPanel {
       
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            setBackground(new Color(253, 211, 124));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < num; i++) {
                g2.setColor(Color.BLACK);
                g2.drawLine(x, y + (size * i), x + (size * (num - 1)), y + (size * i));
                g2.drawLine(x + (size * i), y, x + (size * i), y + (size * (num - 1)));
            }
            for(int i = 0;i<num;i++) {
               for(int j = 0;j<num;j++) {
                  if (stone[i][j] == BLACK) {
                  g.setColor(Color.BLACK);
                  g.fillOval((x - size / 2) + i * size, (y - size / 2)
                        + j * size, width, height);
                  }
                  else if (stone[i][j] == WHITE) {
                  g.setColor(Color.WHITE);
                  g.fillOval((x - size / 2) + i * size, (y - size / 2)
                        + j * size, width, height);
               }
            }  
        }       
        }
    }
   
    }