package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.NonLinearTransformation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIParametersSource implements ParameterSource {
    private static final Scanner scanner = new Scanner(System.in);

    public Optional<FractalParameters> getParameters() {
        //TODO exception handling

        Optional<Frame> optionalFrame = getFrame();
        if (optionalFrame.isEmpty()) {
            return Optional.empty();
        }

        Optional<HashMap<NonLinearTransformation, List<NonLinearTransformation>>> optionalTransformations
            = getTransformationsList();
        if (optionalTransformations.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> optionalIterations = getIterations();
        if (optionalIterations.isEmpty()) {
            return Optional.empty();
        }

        Frame frame = optionalFrame.get();
        List<NonLinearTransformation> transformations = optionalTransformations.get();
        int iterations = optionalIterations.get();

        return Optional.of(new FractalParameters(frame, transformations, iterations));
    }

    private Optional<Integer> getIterations() {
        System.out.println("Введите количество итераций");
        String input = scanner.nextLine();
        try {
            int iterations = Integer.parseInt(input);
            return Optional.of(iterations);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    private Optional<List<NonLinearTransformation>> getTransformationsList() {
        System.out.println(transformationsList());
        System.out.println("Введите список трансформаций через пробел");
        System.out.println("(Пустая строка для всех сразу)");

        String input = scanner.nextLine();
        if (input.isEmpty()) {
            List<NonLinearTransformation> result
                = Arrays.stream(NonLinearTransformation.values()).toList();
            return Optional.of(result);
        }

        List<NonLinearTransformation> list = new LinkedList<>();
        String[] parameters = input.split(" ");
        try {
            for (String parameter : parameters) {
                int index = Integer.parseInt(parameter);
                var optionalTransformation = NonLinearTransformation.getByIndex(index);
                optionalTransformation.ifPresent(list::add);
            }
            return Optional.of(list);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    private String transformationsList() {
        StringBuilder transformationsList = new StringBuilder();
        //without var it takes the whole line
        for (var transformation : NonLinearTransformation.values()) {
            transformationsList.append(transformation.index());
            transformationsList.append(". ");
            transformationsList.append(transformation);
            transformationsList.append('\n');
        }
        return transformationsList.toString();
    }

    public Optional<Frame> getFrame() {
        System.out.println("Введите размеры фрактала (ширина и высота в пикселях через пробел)");
        String input = scanner.nextLine();
        try {
            String[] parameters = input.split(" ");
            int width = Integer.parseInt(parameters[0]);
            int height = Integer.parseInt(parameters[1]);

            return Optional.of(new Frame(height, width));
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }
}
