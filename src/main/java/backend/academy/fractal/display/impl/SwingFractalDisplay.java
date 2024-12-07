package backend.academy.fractal.display.impl;

import backend.academy.fractal.display.FractalProcessor;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.Pixel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.springframework.stereotype.Component;

/**
 * Used for displaying a generated fractal using java Swing
 */
@Component("SwingFractalDisplay")
public class SwingFractalDisplay implements FractalProcessor {
    /**
     * @param frame generated fractal ({@link Frame} instance) to be displayed
     */
    @Override
    public void processFractal(Frame frame) {
        JFrame jFrame = new JFrame("Fractal Image");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int width = frame.width();
        int height = frame.height();
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Создаем BufferedImage из матрицы пикселей
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Pixel pixel = frame.getPixel(x, y);
                        image.setRGB(x, y, pixel.getRgb());
                    }
                }
                // Рисуем изображение на панели
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));
        jFrame.add(panel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
