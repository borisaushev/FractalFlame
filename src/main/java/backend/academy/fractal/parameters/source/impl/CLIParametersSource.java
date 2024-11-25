package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.NonLinearTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class CLIParametersSource implements ParameterSource {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public Optional<FractalParameters> getParameters() {
        //TODO exception handling

        Optional<Frame> optionalFrame = getFrame();
        if (optionalFrame.isEmpty()) {
            return Optional.empty();
        }

        Optional<List<FractalTransformation>> optionalTransformations
            = getTransformationsList();
        if (optionalTransformations.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> optionalIterations = getIterations();
        if (optionalIterations.isEmpty()) {
            return Optional.empty();
        }

        Frame frame = optionalFrame.get();
        List<FractalTransformation> transformations = optionalTransformations.get();
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

    private Optional<List<FractalTransformation>> getTransformationsList() {
        try {
            System.out.println("Введите введите количество преобразований (не больше 6)");
            System.out.println("(Пустая строка для генерации 6 преобразований)");
            String input = scanner.nextLine();
            if(input.isEmpty()) {
                //TODO: implement
                return Optional.of(generateTransformations());
            }

            int count = Integer.parseInt(input);

            System.out.println("Возможные нелинейные преобразования:");
            System.out.println(transformationsList());

            System.out.println("Возможные цвета преобразований:");
            System.out.println(colorList());

            List<FractalTransformation> list = new LinkedList<>();
            for(int i = 0; i < count; i++) {
                AffineTransformation affineTransformation = getAffineTransformation(input);
                List<NonLinearTransformation> nonLinearTransformationList = getTransformationList();
                TransformationColor color = getTransformationColor();
                list.add(new FractalTransformation(affineTransformation, nonLinearTransformationList, color));
            }
            return Optional.of(list);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    private List<FractalTransformation> generateTransformations() {
        TransformationColor[] predefinedColors = TransformationColor.values();

        int functionsCount = 6;
        List<FractalTransformation> result = new LinkedList<>();
        for (int i = 0; i < functionsCount; i++) {
            double[] coefficients = new double[6];
            for (int j = 0; j < 6; j++) {
                coefficients[j] = random.nextDouble() * 2 - 1; // Коэффициенты в диапазоне [-1, 1]
            }

            AffineTransformation affineTransformation = new AffineTransformation(
              coefficients[0],
              coefficients[1],
              coefficients[2],
              coefficients[3],
              coefficients[4],
              coefficients[5]
            );
            List<NonLinearTransformation> nonLinearList = new LinkedList<>();
            int nonLinearFunctionsCount = random.nextInt(NonLinearTransformation.values().length) + 1;
            for(int j = 0; j < nonLinearFunctionsCount; j++) {
                int index = random.nextInt(NonLinearTransformation.values().length) + 1;
                NonLinearTransformation transformation = NonLinearTransformation.getByIndex(index).get();
                nonLinearList.add(transformation);
            }

            int colorIndex = i % predefinedColors.length;
            TransformationColor color = predefinedColors[colorIndex];

            result.add(new FractalTransformation(affineTransformation, nonLinearList, color));
        }

        return result;
    }

    private String colorList() {
        StringBuilder colorList = new StringBuilder();
        //without var it takes the whole line
        for (var color : TransformationColor.values()) {
            colorList.append(color.index());
            colorList.append(". ");
            colorList.append(color);
            colorList.append('\n');
        }
        return colorList.toString();
    }

    private TransformationColor getTransformationColor() {
        System.out.println("Выберите цвет преобразования");
        String input = scanner.nextLine();
        int index = Integer.parseInt(input);
        Optional<TransformationColor> optionalColor = TransformationColor.getByIndex(index);

        return null;
    }

    private AffineTransformation getAffineTransformation(String input) {
        System.out.println("Введите параметры линейного преобразования (6 чисел через пробел)");
        //TODO: implement
        System.out.println("(Пустая строка для генерации параметров)");
        input = scanner.nextLine();
        //TODO: parse

        return null;
    }

    private List<NonLinearTransformation> getTransformationList() {
        System.out.println("Введите соответствующие ему нелинейные преобразования через пробел");

        //TODO: implement
        System.out.println("(Пустая строка для генерации параметров)");

        String input = scanner.nextLine();

        List<NonLinearTransformation> list = new LinkedList<>();
        String[] parameters = input.split(" ");
        for (String parameter : parameters) {
            int index = Integer.parseInt(parameter);
            var optionalTransformation = NonLinearTransformation.getByIndex(index);
            optionalTransformation.ifPresent(list::add);
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
