package backend.academy.fractal.generator.impl;

import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.grid.FramePoint;
import backend.academy.fractal.grid.Pixel;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import backend.academy.fractal.transformation.color.TransformationColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FractalGenerator implements backend.academy.fractal.generator.FractalGenerator {
    public static final int MIN_ITERATIONS = 0;
    @Autowired
    @Qualifier("CLIParametersParser")
    private ParameterSource parameterSource;

    private static final Random random = new Random();
    public static final double GAMMA = 2.2;

    public Optional<Pixel[][]> generate() {
        Optional<FractalParameters> optionalParameters = parameterSource.getParameters();
        if (optionalParameters.isEmpty()) {
            return Optional.empty();
        }

        FractalParameters parameters = optionalParameters.get();
        generate(parameters);

        return Optional.of(parameters.frame().pixelGrid());
    }

    private void initializeGrid(Pixel[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Pixel();
            }
        }
    }

    private void generate(FractalParameters parameters) {
        Pixel[][] grid = parameters.frame().pixelGrid();
        initializeGrid(grid);

        BiUnitPoint currentPoint = new BiUnitPoint(0, 0);
        Frame frame = parameters.frame();

        for (int i = 0; i < parameters.iterations(); i++) {
            FractalTransformation transform = selectTransform(parameters.transformations());

            transform.applyTo(currentPoint);
            FramePoint framePoint = frame.convertToFramePoint(currentPoint);

            if (i > MIN_ITERATIONS && frame.pointInBounds(framePoint)) {
                Pixel curPixel = grid[framePoint.y()][framePoint.x()];
                TransformationColor transformationColor = transform.color();
                updatePixel(curPixel, transformationColor);
                curPixel.hit();
            }
        }
    }

    private static void updatePixel(Pixel curPixel, TransformationColor transformationColor) {
        if(curPixel.hitCount() == 0) {
            curPixel.red(transformationColor.color().getRed());
            curPixel.green(transformationColor.color().getGreen());
            curPixel.blue(transformationColor.color().getBlue());
        }
        else {
            curPixel.red((curPixel.red() + transformationColor.color().getRed())/2);
            curPixel.green((curPixel.green() + transformationColor.color().getGreen())/2);
            curPixel.blue((curPixel.blue() + transformationColor.color().getBlue())/2);
        }
    }

    private static FractalTransformation selectTransform(List<FractalTransformation> transformations) {
        int index = random.nextInt(transformations.size());
        return transformations.get(index);
    }
}
