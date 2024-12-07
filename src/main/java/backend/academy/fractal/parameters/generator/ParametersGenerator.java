package backend.academy.fractal.parameters.generator;

import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import backend.academy.fractal.transformation.impl.NonLinearTransformation;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * Generates random parameters for fractal transformations, including affine and non-linear transformations.
 * <p>
 * This class creates a list of {@link FractalTransformation} objects with randomized attributes
 * to be used in fractal generation.
 * </p>
 */
@SuppressWarnings("MagicNumber")
@SuppressFBWarnings({"PREDICTABLE_RANDOM", "CLI_CONSTANT_LIST_INDEX"})
@Component
public class ParametersGenerator {
    /**
     * Default number of transformation functions to generate.
     */
    public static final int DEFAULT_FUNCTIONS_COUNT = 10;

    /**
     * Maximum number of non-linear transformations per transformation.
     */
    public static final int MAX_NL_TRANSFORMATIONS = 3;

    private final Random random = new Random();

    /**
     * Generates a list of {@link FractalTransformation} objects.
     * <p>
     * Each transformation is composed of:
     * <ul>
     *   <li>An affine transformation with randomized coefficients.</li>
     *   <li>A list of up to {@link #MAX_NL_TRANSFORMATIONS} non-linear transformations.</li>
     *   <li>A predefined transformation color.</li>
     * </ul>
     * </p>
     *
     * @return a list of {@link FractalTransformation} objects
     */
    public List<FractalTransformation> generateTransformations() {
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

    /**
     * Generates a random list of {@link NonLinearTransformation} objects.
     * <p>
     * The number of transformations is chosen randomly, up to {@link #MAX_NL_TRANSFORMATIONS}.
     * </p>
     *
     * @return a list of {@link NonLinearTransformation} objects
     */
    public List<NonLinearTransformation> generateNonLinearTransformationList() {
        List<NonLinearTransformation> nonLinearList = new LinkedList<>();
        int maxTransformations = Math.min(MAX_NL_TRANSFORMATIONS, NonLinearTransformation.values().length);
        int nonLinearFunctionsCount = random.nextInt(maxTransformations) + 1;

        for (int j = 0; j < nonLinearFunctionsCount; j++) {
            int index = random.nextInt(NonLinearTransformation.values().length) + 1;
            NonLinearTransformation transformation = NonLinearTransformation.getByIndex(index).orElseThrow();
            nonLinearList.add(transformation);
        }
        return nonLinearList;
    }

    /**
     * Generates a random {@link AffineTransformation} with coefficients in the range [-1, 1].
     *
     * @return a randomly generated {@link AffineTransformation}
     */
    public AffineTransformation generateAffineTransformation() {
        double[] coefficients = new double[6];
        for (int j = 0; j < 6; j++) {
            coefficients[j] = random.nextDouble() * 2 - 1; // Coefficients in the range [-1, 1]
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

    /**
     * Returns the number of available threads for the system
     *
     * @return recommended threads count
     */
    public int generateThreadCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
