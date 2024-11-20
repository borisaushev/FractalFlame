package backend.academy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class MyFractalFlame {
    private static final int WIDTH = 800; // Ширина изображения
    private static final int HEIGHT = 800; // Высота изображения
    private static final int MAX_ITERATIONS = 100000000; // Количество итераций
    private static final int NUM_FUNCTIONS = 6; // Число аффинных преобразований

    private static Random random = new Random();

    private static double[][] coefficients = new double[NUM_FUNCTIONS][9]; // Матрицы для аффинных преобразований
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
        // Предустановленные красивые цвета
        double[][] predefinedColors = {
            {255, 223, 0},    // Золотой
            {219, 112, 147},  // Фиолетово-розовый
            {72, 61, 139},    // Темно-фиолетовый
            {30, 144, 255},   // Синий
            {186, 85, 211},   // Фиолетовый
            {135, 206, 200}   // Голубой
        };

        for (int i = 0; i < NUM_FUNCTIONS; i++) {
            for (int j = 0; j < 6; j++) {
                coefficients[i][j] = random.nextDouble() * 2 - 1; // Коэффициенты в диапазоне [-1, 1]
            }

            // Назначаем предустановленные цвета циклично
            int colorIndex = i % predefinedColors.length;
            coefficients[i][6] = predefinedColors[colorIndex][0]; // R
            coefficients[i][7] = predefinedColors[colorIndex][1]; // G
            coefficients[i][8] = predefinedColors[colorIndex][2]; // B

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
        int progressStep = MAX_ITERATIONS / 10; // Шаг для обновления прогресса
        int nextProgressUpdate = progressStep;

        System.out.println("Начало генерации фрактала...");

        for (int i = -20; i < MAX_ITERATIONS; i++) {
            int funcIndex = selectFunction();
            double[] transform = coefficients[funcIndex];
            double newX = transform[0] * x + transform[1] * y + transform[2];
            double newY = transform[3] * x + transform[4] * y + transform[5];

            // Применение вариаций
            double[] result = applyVariation(newX, newY, funcIndex);
            x = result[0];
            y = result[1];

            // Генерация цвета
            r = coefficients[funcIndex][6];
            g = coefficients[funcIndex][7];
            b = coefficients[funcIndex][8];

            // Преобразование в пиксели
            int px = (int) ((x + 1) / 2.0 * WIDTH);
            int py = (int) ((y + 1) / 2.0 * HEIGHT);

            if (i > 0 && px >= 0 && px < WIDTH && py >= 0 && py < HEIGHT) {
                densityHistogram[px][py]++;
                if (densityHistogram[px][py] == 1) {
                    colorHistogram[px][py][0] = r;
                    colorHistogram[px][py][1] = g;
                    colorHistogram[px][py][2] = b;
                } else {
                    colorHistogram[px][py][0] = (colorHistogram[px][py][0] + r) / 2;
                    colorHistogram[px][py][1] = (colorHistogram[px][py][1] + g) / 2;
                    colorHistogram[px][py][2] = (colorHistogram[px][py][2] + b) / 2;
                }
            }

            // Обновление прогресса
            if (i >= nextProgressUpdate) {
                int progressPercent = (int) ((i / (double) MAX_ITERATIONS) * 100);
                System.out.println("Прогресс: " + progressPercent + "%");
                nextProgressUpdate += progressStep;
            }
        }

        System.out.println("Генерация завершена!");
        return applyColorCorrection();
    }

    // Применение цветовой коррекции для финального изображения
    private static BufferedImage applyColorCorrection() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Нахождение максимального значения плотности
        int maxDensity = findMaxDensity();
        double maxNormal = log10(maxDensity);
        double gamma = 1.5;

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (densityHistogram[i][j] > 0) {
                    double normal = log10(densityHistogram[i][j]);

                    normal /= maxNormal;

                    // Плотностное взвешивание цвета*/
                    int red = (int) (colorHistogram[i][j][0]*pow(normal, (1.0 / gamma)));
                    int green = (int) (colorHistogram[i][j][1]*pow(normal, (1.0 / gamma)));
                    int blue =(int) (colorHistogram[i][j][2]*pow(normal, (1.0 / gamma)));

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
        double r2 = x * x + y * y; // Используется для некоторых вариаций

        switch (funcIndex % 5) { // Увеличено количество вариантов
            case 0: // Linear
                result[0] = x;
                result[1] = y;
                break;

            case 1: // Sinusoidal + Spherical
                result[0] = sin(x) / r2;
                result[1] = sin(y) / r2;
                break;

            case 2: // Spherical + Swirl
                double swirl = sin(r2) - cos(r2);
                result[0] = x * swirl / r2;
                result[1] = y * swirl / r2;
                break;

            case 3: // Horseshoe
                double horseshoeR = Math.sqrt(r2);
                result[0] = (x - y) * (x + y) / horseshoeR;
                result[1] = 2 * x * y / horseshoeR;
                break;

            case 4: // Polar + Bubble
                double theta = Math.atan2(y, x);
                double bubble = 4 / (r2 + 4);
                result[0] = theta / Math.PI * bubble;
                result[1] = Math.sqrt(r2) * bubble;
                break;

            default: // Фallback: Linear + Spherical
                result[0] = x / (1 + r2);
                result[1] = y / (1 + r2);
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
