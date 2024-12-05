package backend.academy.fractal.generator.impl;

import backend.academy.fractal.grid.Pixel;
import org.springframework.stereotype.Component;
import java.util.Optional;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

@Component
public class GeneratorWithColorCorrection extends FractalGenerator {
    @Override
    public Optional<Pixel[][]> generate() {
        Optional<Pixel[][]> optionalGrid = super.generate();
        if (optionalGrid.isEmpty()) {
            return Optional.empty();
        }
        applyColorCorrection(optionalGrid.get());
        return optionalGrid;
    }

    private void applyColorCorrection(Pixel[][] grid) {
        int maxDensity = findMaxDensity(grid);
        double logMaxDensity = Math.log(maxDensity); // Логарифм с добавлением 1 для устойчивости

        // Гамма-значение для коррекции
        final double GAMMA = 1.5;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int hitCount = grid[y][x].hitCount();
                // Нормализуем значение плотности с логарифмом
                double normalizedDensity = Math.log(hitCount) / logMaxDensity;

                // Усреднённый цвет, скорректированный по плотности
                double normalizedColorFactor = Math.pow(normalizedDensity, 1.0 / GAMMA);

                // Применяем нормализованную плотность к каждому каналу цвета
                int red = (int) Math.min(255, grid[y][x].red() * normalizedColorFactor);
                int green = (int) Math.min(255, grid[y][x].green() * normalizedColorFactor);
                int blue = (int) Math.min(255, grid[y][x].blue() * normalizedColorFactor);

                // Обновляем пиксель с новыми значениями цветов
                grid[y][x].red(red);
                grid[y][x].green(green);
                grid[y][x].blue(blue);
            }
        }
    }


    private static int findMaxDensity(Pixel[][] optionalGrid) {
        int max = 0;
        for (Pixel[] row : optionalGrid) {
            for (Pixel pixel : row) {
                if (pixel.hitCount() > max) {
                    max = pixel.hitCount();
                }
            }
        }
        return max;
    }
}
