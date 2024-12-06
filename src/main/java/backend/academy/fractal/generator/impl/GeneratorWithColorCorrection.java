package backend.academy.fractal.generator.impl;

import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.Pixel;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component("GeneratorWithColorCorrection")
public class GeneratorWithColorCorrection extends SingleThreadFractalGenerator implements FractalGenerator {
    public static final double GAMMA = 1.5;

    @Override
    public Optional<Frame> generate() {
        Optional<Frame> optionalGrid = super.generate();
        if (optionalGrid.isEmpty()) {
            return Optional.empty();
        }

        applyColorCorrection(optionalGrid.get());
        return optionalGrid;
    }

    protected void applyColorCorrection(Frame frame) {
        int maxDensity = findMaxDensity(frame);
        double logMaxDensity = Math.log(maxDensity);

        for (int y = 0; y < frame.height(); y++) {
            for (int x = 0; x < frame.width(); x++) {
                Pixel currentPixel = frame.getPixel(x, y);
                int hitCount = currentPixel.hitCount();
                // Нормализуем значение плотности с логарифмом
                double normalizedDensity = Math.log(hitCount) / logMaxDensity;

                // Усреднённый цвет, скорректированный по плотности
                double normalizedColorFactor = Math.pow(normalizedDensity, 1.0 / GAMMA);

                // Применяем нормализованную плотность к каждому каналу цвета
                int red = (int) Math.min(255, currentPixel.red() * normalizedColorFactor);
                int green = (int) Math.min(255, currentPixel.green() * normalizedColorFactor);
                int blue = (int) Math.min(255, currentPixel.blue() * normalizedColorFactor);

                // Обновляем пиксель с новыми значениями цветов
                currentPixel.red(red);
                currentPixel.green(green);
                currentPixel.blue(blue);
            }
        }
    }


    protected static int findMaxDensity(Frame frame) {
        int max = 0;
        for (int x = 0; x < frame.width(); x++) {
            for (int y = 0; y < frame.width(); y++) {
                Pixel curPixel = frame.getPixel(x, y);
                if (curPixel != null && curPixel.hitCount() > max) {
                    max = curPixel.hitCount();
                }
            }
        }
        return max;
    }
}
