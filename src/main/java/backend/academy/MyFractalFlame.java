package backend.academy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class MyFractalFlame {
    private static final int WIDTH = 2000; // Ширина изображения
    private static final int HEIGHT = 2000; // Высота изображения
    private static final int MAX_ITERATIONS = 10000000; // Количество итераций
    private static final int NUM_FUNCTIONS = 6; // Число аффинных преобразований
    private static final int THREAD_COUNT = 5; // Число потоков

    private static Random random = new Random();

    private static double[][] coefficients = new double[NUM_FUNCTIONS][9]; // Матрицы для аффинных преобразований
    private static double[] probabilities = new double[NUM_FUNCTIONS]; // Вероятности выбора функции

    private static double[][][] colorHistogram = new double[WIDTH][HEIGHT][3]; // Гистограмма цвета (RGB)
    private static final int[][] densityHistogram = new int[WIDTH][HEIGHT]; // Плотность точек
    //private static volatile AtomicInteger progress = new AtomicInteger(0); // Прогресс для всех потоков

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();
        generateAffineTransforms();
        BufferedImage image = generateFractal();
        saveImage(image, "src/main/resources/fractal_flame_colored.png");
        System.out.println("Фрактал сохранён в файл fractal_flame_colored.png");
        System.out.println(System.currentTimeMillis() - time);
    }

    // Генерация случайных аффинных преобразований
    private static void generateAffineTransforms() {
        double[][] predefinedColors = {
            {255, 223, 0},    // Золотой
            {255, 50, 50},  // Фиолетово-розовый
            {72, 61, 139},    // Темно-фиолетовый
            {30, 144, 255},   // Синий
            {186, 85, 111},   // Фиолетовый
            {135, 206, 200}   // Голубой
        };

        for (int i = 0; i < NUM_FUNCTIONS; i++) {
            for (int j = 0; j < 6; j++) {
                coefficients[i][j] = random.nextDouble() * 2 - 1; // Коэффициенты в диапазоне [-1, 1]
            }

            int colorIndex = i % predefinedColors.length;
            coefficients[i][6] = predefinedColors[colorIndex][0]; // R
            coefficients[i][7] = predefinedColors[colorIndex][1]; // G
            coefficients[i][8] = predefinedColors[colorIndex][2]; // B

            probabilities[i] = random.nextDouble(); // Вероятности выбора
        }
        normalizeProbabilities();
    }

    private static void normalizeProbabilities() {
        double sum = 0;
        for (double prob : probabilities) sum += prob;
        for (int i = 0; i < probabilities.length; i++) probabilities[i] /= sum;
    }

    private static BufferedImage generateFractal() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        System.out.println("Начало генерации фрактала...");
        int iterationsPerThread = MAX_ITERATIONS / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int start = t * iterationsPerThread;
            final int end = (t == THREAD_COUNT - 1) ? MAX_ITERATIONS : start + iterationsPerThread;
            executor.execute(() -> processIterations(start, end));
        }
        executor.shutdown();
        executor.awaitTermination(1000, TimeUnit.SECONDS);

        /*executor.shutdown();
        while (!executor.isTerminated()) {
            // Периодически обновляем прогресс
            int progressPercent = (int) (((double) progress.get() /  MAX_ITERATIONS) * 100);
            System.out.println("Прогресс: " + progressPercent + "%");
            try {
                Thread.sleep(500); // Пауза между обновлениями прогресса
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/

        System.out.println("Генерация завершена!");
        return applyColorCorrection();
    }

    private static void processIterations(int start, int end) {
        double x = 0, y = 0;
        double r, g, b;

        for (int i = start; i < end; i++) {
            int funcIndex = selectFunction();
            double[] transform = coefficients[funcIndex];
            double newX = transform[0] * x + transform[1] * y + transform[2];
            double newY = transform[3] * x + transform[4] * y + transform[5];

            double[] result = applyVariation(newX, newY, funcIndex);
            x = result[0];
            y = result[1];

            r = coefficients[funcIndex][6];
            g = coefficients[funcIndex][7];
            b = coefficients[funcIndex][8];

            int px = (int) ((x + 1) / 2.0 * WIDTH);
            int py = (int) ((y + 1) / 2.0 * HEIGHT);

            if (i > 0 && px >= 0 && px < WIDTH && py >= 0 && py < HEIGHT) {
                synchronized (densityHistogram) {
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
            }

            /*if (i % 1000 == 0) {
                progress.incrementAndGet();
            }*/
        }
    }

    private static BufferedImage applyColorCorrection() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        int maxDensity = findMaxDensity();
        double maxNormal = log10(maxDensity);
        double gamma = 1.5;

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (densityHistogram[i][j] > 0) {
                    double normal = log10(densityHistogram[i][j]);
                    normal /= maxNormal;

                    int red = (int) (colorHistogram[i][j][0] * pow(normal, (1.0 / gamma)));
                    int green = (int) (colorHistogram[i][j][1] * pow(normal, (1.0 / gamma)));
                    int blue = (int) (colorHistogram[i][j][2] * pow(normal, (1.0 / gamma)));

                    red = Math.min(255, red);
                    green = Math.min(255, green);
                    blue = Math.min(255, blue);

                    image.setRGB(i, j, new Color(red, green, blue).getRGB());
                } else {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }

    private static int selectFunction() {
        double r = random.nextDouble();
        double cumulative = 0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulative += probabilities[i];
            if (r < cumulative) return i;
        }
        return probabilities.length - 1;
    }

    private static double[] applyVariation(double x, double y, int funcIndex) {
        double[] result = new double[2];
        double r2 = x * x + y * y;

        switch (funcIndex % 5) {
            case 0:
                result[0] = x;
                result[1] = y;
                break;
            case 1:
                result[0] = sin(x) / r2;
                result[1] = sin(y) / r2;
                break;
            case 2:
                double swirl = sin(r2) - cos(r2);
                result[0] = x * swirl / r2;
                result[1] = y * swirl / r2;
                break;
            case 3:
                double horseshoeR = Math.sqrt(r2);
                result[0] = (x - y) * (x + y) / horseshoeR;
                result[1] = 2 * x * y / horseshoeR;
                break;
            case 4:
                double theta = Math.atan2(y, x);
                double bubble = 4 / (r2 + 4);
                result[0] = theta / Math.PI * bubble;
                result[1] = Math.sqrt(r2) * bubble;
                break;
            default:
                result[0] = x / (1 + r2);
                result[1] = y / (1 + r2);
                break;
        }

        return result;
    }

    private static int findMaxDensity() {
        int max = 0;
        for (int[] row : densityHistogram) {
            for (int value : row) {
                if (value > max) max = value;
            }
        }
        return max;
    }

    private static void saveImage(BufferedImage image, String filename) {
        try {
            File file = new File(filename);
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }
}
