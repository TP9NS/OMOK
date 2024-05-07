import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel() {
        try {
            // 이미지를 불러옵니다. 이미지 경로를 올바르게 설정해주세요.
            image = ImageIO.read(new File("C:\\Users\\pshcc\\OneDrive\\바탕 화면\\다운로드.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 이미지를 패널에 그립니다.
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}