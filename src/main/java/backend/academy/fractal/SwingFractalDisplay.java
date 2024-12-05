package backend.academy.fractal;

import backend.academy.fractal.generator.impl.GeneratorWithColorCorrection;
import backend.academy.fractal.grid.Pixel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

@Component
public class SwingFractalDisplay {
    @Autowired
    private GeneratorWithColorCorrection generator;

    public void generateAndDisplay() {
        Pixel[][] grid = generator.generate().get();

        // Создаем окно
        JFrame frame = new JFrame("Fractal Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создаем панель для отображения изображения
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Создаем BufferedImage из матрицы пикселей
                int width = grid[0].length;
                int height = grid.length;
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Pixel pixel = grid[y][x];
                        image.setRGB(x, y, pixel.getRgb());
                    }
                }

                // Рисуем изображение на панели
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };

        // Настраиваем размеры панели и добавляем её в окно
        int width = grid[0].length;
        int height = grid.length;
        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel);

        // Устанавливаем размер окна и делаем его видимым
        frame.pack();
        frame.setLocationRelativeTo(null); // Центрируем окно
        frame.setVisible(true);
    }
}
