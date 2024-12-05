package backend.academy.fractal.parameters;

import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.NonLinearTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ParametersGenerator {

    public static final int DEFAULT_FUNCTIONS_COUNT = 100;

    private ParametersGenerator(){}
    private static final Random random = new Random();

    public static List<FractalTransformation> generateTransformations() {
        TransformationColor[] predefinedColors = TransformationColor.values();

        List<FractalTransformation> result = new LinkedList<>();
        for (int i = 0; i < DEFAULT_FUNCTIONS_COUNT; i++) {
            AffineTransformation affineTransformation = generateAffineTransformation();
            List<NonLinearTransformation> nonLinearList = generateNonLinearTransformationList();

            int colorIndex = i % predefinedColors.length;
            TransformationColor color = predefinedColors[colorIndex];

            result.add(new FractalTransformation(affineTransformation, nonLinearList, color));
        }

        return result;
    }
    public static List<NonLinearTransformation> generateNonLinearTransformationList() {
        List<NonLinearTransformation> nonLinearList = new LinkedList<>();
        int nonLinearFunctionsCount = random.nextInt(NonLinearTransformation.values().length) + 1;
        nonLinearFunctionsCount = Math.min(3, nonLinearFunctionsCount);
        for (int j = 0; j < nonLinearFunctionsCount; j++) {
            int index = random.nextInt(NonLinearTransformation.values().length) + 1;
            NonLinearTransformation transformation = NonLinearTransformation.getByIndex(index).get();
            nonLinearList.add(transformation);
        }
        return nonLinearList;
    }

    public static AffineTransformation generateAffineTransformation() {
        double[] coefficients = new double[6];
        for (int j = 0; j < 6; j++) {
            coefficients[j] = random.nextDouble() * 2 - 1; // Коэффициенты в диапазоне [-1, 1]
        }

        return new AffineTransformation(
            coefficients[0],
            coefficients[1],
            coefficients[2],
            coefficients[3],
            coefficients[4],
            coefficients[5]
        );
    }

}
