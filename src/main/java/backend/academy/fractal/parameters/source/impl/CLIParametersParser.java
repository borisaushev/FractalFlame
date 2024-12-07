package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.FrameParameters;
import backend.academy.fractal.parameters.generator.ParametersGenerator;
import backend.academy.fractal.parameters.source.CLInputSource;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import backend.academy.fractal.transformation.impl.NonLinearTransformation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static backend.academy.fractal.parameters.generator.ParametersGenerator.DEFAULT_FUNCTIONS_COUNT;

/**
 * A command-line interface (CLI) parser for getting {@link FractalParameters}.
 * <p>
 * This class reads user input from the command line to configure fractal generation parameters,
 * including frame size, number of iterations, and transformations.
 * </p>
 */
@Component
public class CLIParametersParser implements ParameterSource {

    @Autowired
    private CLInputSource clReader;

    @Autowired
    private ParametersGenerator parametersGenerator;

    /**
     * Reads and parses all required fractal parameters from the user.
     *
     * @return an {@link Optional} containing {@link FractalParameters} if all inputs are valid;
     *     otherwise, an empty {@link Optional}.
     */
    public Optional<FractalParameters> getParameters() {
        Optional<FrameParameters> optionalParameters = getFrameParameters();
        if (optionalParameters.isEmpty()) {
            System.out.println("Invalid image parameters");
            return Optional.empty();
        }

        Optional<List<FractalTransformation>> optionalTransformations = getTransformationsList();
        if (optionalTransformations.isEmpty()) {
            System.out.println("Invalid transformation parameters");
            return Optional.empty();
        }

        Optional<Integer> optionalIterations = getIterations();
        if (optionalIterations.isEmpty()) {
            System.out.println("Invalid number of iterations");
            return Optional.empty();
        }

        FrameParameters frameParameters = optionalParameters.get();
        List<FractalTransformation> transformations = optionalTransformations.get();
        int iterations = optionalIterations.get();

        return Optional.of(new FractalParameters(frameParameters, transformations, iterations));
    }

    /**
     * Reads the number of iterations for fractal generation.
     *
     * @return an {@link Optional} containing the number of iterations if valid; otherwise, an empty {@link Optional}.
     */
    public Optional<Integer> getIterations() {
        System.out.println("Enter the number of iterations:");
        String input = clReader.nextLine();
        try {
            int iterations = Integer.parseInt(input);
            if (iterations < FractalParameters.MIN_ITERATIONS) {
                return Optional.empty();
            }
            return Optional.of(iterations);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    /**
     * Reads or generates a list of {@link FractalTransformation} objects.
     *
     * @return an {@link Optional} containing a list of transformations if valid; otherwise, an empty {@link Optional}.
     */
    public Optional<List<FractalTransformation>> getTransformationsList() {
        try {
            System.out.println("Enter the number of transformations:");
            System.out.printf("(Leave empty to generate %d transformations)\n", DEFAULT_FUNCTIONS_COUNT);
            String input = clReader.nextLine();
            if (input.isEmpty()) {
                return Optional.of(parametersGenerator.generateTransformations());
            }

            int count = Integer.parseInt(input);
            if (count <= 0) {
                return Optional.empty();
            }

            System.out.println("Available non-linear transformations:");
            System.out.println(transformationsList());

            System.out.println("Available transformation colors:");
            System.out.println(colorList());

            List<FractalTransformation> list = new LinkedList<>();
            for (int i = 0; i < count; i++) {
                Optional<AffineTransformation> affineTransformation = getAffineTransformation();
                if (affineTransformation.isEmpty()) {
                    return Optional.empty();
                }

                Optional<List<NonLinearTransformation>> optionalNonLinearList = getNLTransformationsList();
                if (optionalNonLinearList.isEmpty()) {
                    return Optional.empty();
                }

                Optional<TransformationColor> optionalColor = getTransformationColors();
                if (optionalColor.isEmpty()) {
                    return Optional.empty();
                }

                list.add(new FractalTransformation(
                    affineTransformation.get(), optionalNonLinearList.get(), optionalColor.get()));
            }
            return Optional.of(list);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    /**
     * Returns the string value representing
     * the list of available transformation colors.
     *
     * @return a formatted string of colors and their indices.
     */
    private String colorList() {
        StringBuilder colorList = new StringBuilder();
        for (var color : TransformationColor.values()) {
            colorList.append(color.index());
            colorList.append(". ");
            colorList.append(color);
            colorList.append('\n');
        }
        return colorList.toString();
    }

    /**
     * Reads the affine transformation parameters from the user or generates them randomly
     * if the user wants so.
     *
     * @return an {@link Optional} containing an {@link AffineTransformation} if valid;
     *     otherwise, an empty {@link Optional}.
     */
    public Optional<AffineTransformation> getAffineTransformation() {
        System.out.println("Enter linear transformation parameters (6 numbers in range [-1, 1] separated by spaces):");
        System.out.println("(Leave empty to generate random parameters)");

        String input = clReader.nextLine().trim();
        if (input.isEmpty()) {
            return Optional.of(parametersGenerator.generateAffineTransformation());
        }

        try {
            String[] numbers = input.split(" ");
            double[] coefficients = new double[6];
            for (int j = 0; j < 6; j++) {
                double value = Double.parseDouble(numbers[j]);
                if (value > 1 || value < -1) {
                    return Optional.empty();
                }
                coefficients[j] = value;
            }

            return Optional.of(new AffineTransformation(
                coefficients[0],
                coefficients[1],
                coefficients[2],
                coefficients[3],
                coefficients[4],
                coefficients[5]
            ));
        } catch (Exception exc) {
            return Optional.empty();
        }
    }

    /**
     * Reads the dimensions of the fractal frame (width and height).
     *
     * @return an {@link Optional} containing {@link FrameParameters} if valid;
     *     otherwise, an empty {@link Optional}.
     */
    public Optional<FrameParameters> getFrameParameters() {
        System.out.println("Enter the fractal dimensions (width and height in pixels separated by space):");
        String input = clReader.nextLine();
        try {
            String[] parameters = input.split(" ");
            int width = Integer.parseInt(parameters[0]);
            int height = Integer.parseInt(parameters[1]);

            return Optional.of(new FrameParameters(height, width));
        } catch (Exception exc) {
            return Optional.empty();
        }
    }

    /**
     * Reads a list of non-linear transformations from the user or generates them randomly
     * if the user wants so.
     *
     * @return an {@link Optional} containing a list of {@link NonLinearTransformation} if valid;
     *     otherwise, an empty {@link Optional}.
     */
    public Optional<List<NonLinearTransformation>> getNLTransformationsList() {
        System.out.println("Enter the indices of corresponding non-linear transformations separated by spaces:");
        System.out.println("(Leave empty to generate random parameters)");

        String input = clReader.nextLine();

        if (input.isEmpty()) {
            return Optional.of(parametersGenerator.generateNonLinearTransformationList());
        }

        try {
            List<NonLinearTransformation> list = new LinkedList<>();
            String[] parameters = input.split(" ");
            for (String parameter : parameters) {
                int index = Integer.parseInt(parameter);
                var optionalTransformation = NonLinearTransformation.getByIndex(index);
                if (optionalTransformation.isEmpty()) {
                    return Optional.empty();
                }
                list.add(optionalTransformation.get());
            }
            return Optional.of(list);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    /**
     * Reads the color for a transformation from the user.
     *
     * @return an {@link Optional} containing a {@link TransformationColor} if valid;
     *     otherwise, an empty {@link Optional}.
     */
    public Optional<TransformationColor> getTransformationColors() {
        try {
            System.out.println("Choose a transformation color by index:");
            String input = clReader.nextLine();
            int index = Integer.parseInt(input);
            return TransformationColor.getByIndex(index);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    /**
     * Generates a formatted string listing available non-linear transformations.
     *
     * @return a formatted string of transformations and their indices.
     */
    private String transformationsList() {
        StringBuilder transformationsList = new StringBuilder();
        for (var transformation : NonLinearTransformation.values()) {
            transformationsList.append(transformation.index());
            transformationsList.append(". ");
            transformationsList.append(transformation);
            transformationsList.append('\n');
        }
        return transformationsList.toString();
    }
}
