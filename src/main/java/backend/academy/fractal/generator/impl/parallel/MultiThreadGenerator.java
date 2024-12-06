package backend.academy.fractal.generator.impl.parallel;

import backend.academy.fractal.generator.FractalGenerator;
import backend.academy.fractal.generator.impl.GeneratorWithColorCorrection;
import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.FramePoint;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component("MultiThreadGenerator")
public class MultiThreadGenerator extends GeneratorWithColorCorrection implements FractalGenerator {
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    @Override
    public Optional<Frame> generate() {
        Optional<FractalParameters> optionalParameters = parameterSource.getParameters();
        if (optionalParameters.isEmpty()) {
            return Optional.empty();
        }
        FractalParameters parameters = optionalParameters.get();
        Frame frame = new Frame(parameters.frameParameters());

        try (ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT)) {
            int iterations = parameters.iterations();
            for (int i = 0; i < iterations; i += (iterations / THREAD_COUNT)) {
                int start = i;
                int end = i + iterations / THREAD_COUNT;
                service.submit(() -> generatePart(frame, parameters, start, end));
            }
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
            return Optional.empty();
        }
        applyColorCorrection(frame);

        return Optional.of(frame);
    }

    @Override
    protected void applyColorCorrection(Frame frame) {
        int maxDensity = findMaxDensity(frame);
        double logMaxDensity = Math.log(maxDensity);

        try (ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT)) {
            for(int y = 0; y < frame.height(); y += frame.height()/THREAD_COUNT) {
                int start = y;
                int end = y + frame.height() / THREAD_COUNT;
                service.execute(() -> correctPart(frame, logMaxDensity, start, end));
            }
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
        }
    }

    protected static void correctPart(Frame frame, double logMaxDensity, int yStart, int yEnd) {
        for (int y = yStart; y < Math.min(yEnd, frame.height()); y++) {
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

    protected Frame generatePart(Frame frame, FractalParameters parameters, int start, int end) {
        BiUnitPoint currentPoint = new BiUnitPoint(0, 0);
        for (int i = start; i < Math.min(parameters.iterations(), end); i++) {
            FractalTransformation transform = selectTransform(parameters.transformations());

            transform.applyTo(currentPoint);
            FramePoint framePoint = frame.convertToFramePoint(currentPoint);

            if (i > MIN_ITERATIONS && frame.pointInBounds(framePoint)) {
                Pixel curPixel = frame.getPixel(framePoint);
                TransformationColor transformationColor = transform.color();
                updatePixel(curPixel, transformationColor);
                curPixel.hit();
            }
        }

        return frame;
    }
}
