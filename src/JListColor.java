import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class JListColor extends JFrame implements ActionListener{
	private JComboBox<String> comboBox1, comboBox2, comboBox3,comboBox4,bgmBox,GamebgmBox,LoginbgmBox;
    private JLabel label1, label2, label3,label4,label5,label6,label7,label8,label9,label10,label11;
    private JTextField showLobbyVolume,showGameVolume,showSystemVolume,showLoginVolume;
    private JButton changeButton;
    private JSlider LobbyvolumeSlider,IngamevolumeSlider,SystemSlider,LoginVolumeSlider;
    
    Client c =null;

    public JListColor(Client _c) {
    	c= _c;
    	setTitle("환경 설정");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel contentPanel = new JPanel(new GridBagLayout()) {
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        label1 = new JLabel("내 채팅 색상:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(label1, gbc);

        comboBox1 = new JComboBox<>(new String[]{"Black","Red", "Green", "Blue", "Yellow"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        contentPanel.add(comboBox1, gbc);
        
        

        label2 = new JLabel("다른 유저 채팅 색상:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(label2, gbc);

        comboBox2 = new JComboBox<>(new String[]{"Black","Red", "Green", "Blue", "Yellow"});
        gbc.gridx = 1;
        gbc.gridy = 1;
       
        contentPanel.add(comboBox2, gbc);

      
        
 
        
        label4 = new JLabel("채팅 폰트 설정 : ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(label4,gbc);
        
        comboBox4 = new JComboBox<>(new String[] {"Dialog","Malgun Gothic","Nanum Pen Script","Batang","Dotum"});
        gbc.gridx = 1;
        gbc.gridy = 2;
       
        contentPanel.add(comboBox4,gbc);
        
        label5 = new JLabel("로비화면 BGM 설정");
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPanel.add(label5,gbc);
        
        bgmBox = new JComboBox<>(new String[] {"start","warm","cool","end","fun","good"});
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPanel.add(bgmBox,gbc);
        
        label6 = new JLabel("로비화면 볼륨 설정");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(label6,gbc);
        
        LobbyvolumeSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
        LobbyvolumeSlider.setMajorTickSpacing(10);
        LobbyvolumeSlider.setPaintTicks(true);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(LobbyvolumeSlider,gbc);
        
        LobbyvolumeSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int volume = (int) source.getValue(); 
                c.setVolume(volume);
            }
        });
        
        showLobbyVolume = new JTextField(2);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        showLobbyVolume.setText(String.valueOf(LobbyvolumeSlider.getValue()));
        showLobbyVolume.setFont(new Font("맑은 고딕",Font.BOLD,12));
        contentPanel.add(showLobbyVolume,gbc);
        
        LobbyvolumeSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = LobbyvolumeSlider.getValue();
                showLobbyVolume.setText(String.valueOf(value));
            }
        });
        
        showLobbyVolume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(showLobbyVolume.getText());
                    if (value >= 0 && value <= 100) {
                        LobbyvolumeSlider.setValue(value);
                    } else {
                        if(value < 0)
                        	LobbyvolumeSlider.setValue(0);
                        else if(value > 100)
                        	LobbyvolumeSlider.setValue(100);
                    }
                } catch (NumberFormatException ex) {
                	JOptionPane.showMessageDialog(null,"숫자만 입력하여 주십시오.","에러 메세지",JOptionPane.ERROR_MESSAGE);
                	showLobbyVolume.setText(String.valueOf(LobbyvolumeSlider.getValue()));
                }
            }
        });
        
        label7 = new JLabel("인게임 BGM 설정");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(label7,gbc);
        
        GamebgmBox = new JComboBox<>(new String[] {"start","warm","cool","end","fun","good"});
        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPanel.add(GamebgmBox,gbc);
        
        label8 = new JLabel("인게임화면 볼륨 설정");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(label8,gbc);
        
        IngamevolumeSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
        IngamevolumeSlider.setMajorTickSpacing(10);
        IngamevolumeSlider.setPaintTicks(true);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(IngamevolumeSlider,gbc);
        
        showGameVolume = new JTextField(2);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        showGameVolume.setFont(new Font("맑은 고딕",Font.BOLD,12));
        showGameVolume.setText(String.valueOf(IngamevolumeSlider.getValue()));
        contentPanel.add(showGameVolume,gbc);
        
        IngamevolumeSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = IngamevolumeSlider.getValue();
                showGameVolume.setText(String.valueOf(value));
            }
        });
        
        showGameVolume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(showGameVolume.getText());
                    if (value >= 0 && value <= 100) {
                        IngamevolumeSlider.setValue(value);
                    } else {
                        if(value < 0)
                        	 IngamevolumeSlider.setValue(0);
                        else if(value > 100)
                        	IngamevolumeSlider.setValue(100);
                    }
                } catch (NumberFormatException ex) {
                	JOptionPane.showMessageDialog(null,"숫자만 입력하여 주십시오.","에러 메세지",JOptionPane.ERROR_MESSAGE);
                	showGameVolume.setText(String.valueOf(IngamevolumeSlider.getValue()));
                }
            }
        });
        
       
        label9 = new JLabel("시스템 효과음 설정");
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(label9,gbc);
        
        SystemSlider = new JSlider(JSlider.HORIZONTAL,0,100,100);
        SystemSlider.setMajorTickSpacing(10);
        SystemSlider.setPaintTicks(true);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(SystemSlider,gbc);
        
        showSystemVolume = new JTextField(2);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.EAST;
        showSystemVolume.setFont(new Font("맑은 고딕",Font.BOLD,12));
        showSystemVolume.setText(String.valueOf(SystemSlider.getValue()));
        contentPanel.add(showSystemVolume,gbc);
        
        SystemSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = SystemSlider.getValue();
                showSystemVolume.setText(String.valueOf(value));
            }
        });
        
        showSystemVolume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(showSystemVolume.getText());
                    if (value >= 0 && value <= 100) {
                        SystemSlider.setValue(value);
                    } else {
                        if(value < 0)
                        	 SystemSlider.setValue(0);
                        else if(value > 100)
                        	SystemSlider.setValue(100);
                    }
                } catch (NumberFormatException ex) {
                	JOptionPane.showMessageDialog(null,"숫자만 입력하여 주십시오.","에러 메세지",JOptionPane.ERROR_MESSAGE);
                	showSystemVolume.setText(String.valueOf(SystemSlider.getValue()));
                }
            }
        });
        
        changeButton = new JButton("저장하기");
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(changeButton, gbc);
        changeButton.addActionListener(this);

        add(contentPanel);
        
        setSize(350, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public float percentageToVolume(int percentage) {
        // Convert percentage to a volume value within the allowable range
        float minVolume = -60.0f;
        float maxVolume = 6.0206f;
        float range = maxVolume - minVolume;
        return ((percentage / 100.0f) * range) + minVolume;
    }
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == changeButton) {
    		c.myColor = (String)comboBox1.getSelectedItem();
    		c.userColor = (String)comboBox2.getSelectedItem();
    		
    		String lobbybgm = (String)bgmBox.getSelectedItem();
            int lobbySound = LobbyvolumeSlider.getValue();
            c.ingame= (String)GamebgmBox.getSelectedItem();
            c.ingameV = IngamevolumeSlider.getValue(); 
            int  EffectSound = SystemSlider.getValue();
            c.loby=lobbybgm;
            c.lobyV=lobbySound;
            c.sysV=EffectSound;
            String sel_font = (String)comboBox4.getSelectedItem();
            c.font= (new Font(sel_font,Font.PLAIN,13));
            c.stopBGM();
            c.playBGM(lobbybgm, lobbySound);
         
    	}
    }
}