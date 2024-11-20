package backend.academy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

public class FractalFlame {
    private static final int WIDTH = 1400; // Ширина изображения
    private static final int HEIGHT = 1000; // Высота изображения
    private static final int MAX_ITERATIONS = 10000000; // Количество итераций
    private static final int NUM_FUNCTIONS = 6; // Число аффинных преобразований

    private static Random random = new Random();

    private static double[][] coefficients = new double[NUM_FUNCTIONS][6]; // Матрицы для аффинных преобразований
    private static double[] probabilities = new double[NUM_FUNCTIONS]; // Вероятности выбора функции

    private static double[][][] colorHistogram = new double[WIDTH][HEIGHT][3]; // Гистограмма цвета (RGB)
    private static int[][] densityHistogram = new int[WIDTH][HEIGHT]; // Плотность точек

    public static void main(String[] args) {
        generateAffineTransforms();
        BufferedImage image = generateFractal();
        saveImage(image, "src/main/resources/fractal_flame_colored.png");
        System.out.println("Фрактал сохранён в файл fractal_flame_colored.png");
    }

    // Генерация случайных аффинных преобразований
    private static void generateAffineTransforms() {
        for (int i = 0; i < NUM_FUNCTIONS; i++) {
            for (int j = 0; j < 6; j++) {
                coefficients[i][j] = random.nextDouble() * 2 - 1; // Коэффициенты в диапазоне [-1, 1]
            }
            probabilities[i] = random.nextDouble(); // Вероятности выбора
        }
        normalizeProbabilities();
    }

    // Нормализация вероятностей
    private static void normalizeProbabilities() {
        double sum = 0;
        for (double prob : probabilities) sum += prob;
        for (int i = 0; i < probabilities.length; i++) probabilities[i] /= sum;
    }

    // Генерация фрактала с использованием цветовой коррекции
    private static BufferedImage generateFractal() {
        double x = 0, y = 0;
        double r, g, b; // RGB-значения для цветовой коррекции

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            int funcIndex = selectFunction();
            double[] transform = coefficients[funcIndex];
            double newX = transform[0] * x + transform[1] * y + transform[2];
            double newY = transform[3] * x + transform[4] * y + transform[5];

            // Применение вариаций
            double[] result = applyVariation(newX, newY, funcIndex);
            x = result[0];
            y = result[1];

            // Генерация цвета
            r = Math.abs(Math.sin(funcIndex * Math.PI / 3));
            g = Math.abs(Math.cos(funcIndex * Math.PI / 5));
            b = Math.abs(Math.sin(funcIndex * Math.PI / 7));

            // Преобразование в пиксели
            int px = (int) ((x + 1.5) / 3.0 * WIDTH);
            int py = (int) ((y + 1.5) / 3.0 * HEIGHT);

            if (px >= 0 && px < WIDTH && py >= 0 && py < HEIGHT) {
                densityHistogram[px][py]++;
                colorHistogram[px][py][0] += r;
                colorHistogram[px][py][1] += g;
                colorHistogram[px][py][2] += b;
            }
        }

        return applyColorCorrection();
    }

    // Применение цветовой коррекции для финального изображения
    private static BufferedImage applyColorCorrection() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Нахождение максимального значения плотности
        int maxDensity = findMaxDensity();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (densityHistogram[i][j] > 0) {
                    // Нормализация цвета
                    double r = colorHistogram[i][j][0] / densityHistogram[i][j];
                    double g = colorHistogram[i][j][1] / densityHistogram[i][j];
                    double b = colorHistogram[i][j][2] / densityHistogram[i][j];

                    // Плотностное взвешивание цвета
                    double intensity = Math.log(1 + densityHistogram[i][j]) / Math.log(1 + maxDensity);
                    int red = Math.min(255, (int) (r * intensity * 255));
                    int green = Math.min(255, (int) (g * intensity * 255));
                    int blue = Math.min(255, (int) (b * intensity * 255));

                    image.setRGB(i, j, new Color(red, green, blue).getRGB());
                } else {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }

    // Выбор случайной функции на основе вероятностей
    private static int selectFunction() {
        double r = random.nextDouble();
        double cumulative = 0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulative += probabilities[i];
            if (r < cumulative) return i;
        }
        return probabilities.length - 1;
    }

    // Применение вариаций
    private static double[] applyVariation(double x, double y, int funcIndex) {
        double[] result = new double[2];
        switch (funcIndex % 3) {
            case 0: // Linear
                result[0] = x;
                result[1] = y;
                break;
            case 1: // Sinusoidal
                result[0] = x - 1/x;
                result[1] = y - 1/y*y;
                break;
            case 2: // Spherical
                double r2 = x * x + y * y;
                result[0] = x / r2;
                result[1] = y / r2;
                break;
        }
        return result;
    }

    // Нахождение максимальной плотности
    private static int findMaxDensity() {
        int max = 0;
        for (int[] row : densityHistogram) {
            for (int value : row) {
                if (value > max) max = value;
            }
        }
        return max;
    }

    // Сохранение изображения
    private static void saveImage(BufferedImage image, String filename) {
        try {
            File file = new File(filename);
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }
}
