package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.grid.Frame;
import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.ParametersGenerator;
import backend.academy.fractal.parameters.source.ParameterSource;
import backend.academy.fractal.transformation.FractalTransformation;
import backend.academy.fractal.transformation.NonLinearTransformation;
import backend.academy.fractal.transformation.color.TransformationColor;
import backend.academy.fractal.transformation.impl.AffineTransformation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
//TODO: test
public class CLIParametersSource implements ParameterSource {
    private static final Scanner scanner = new Scanner(System.in);

    public Optional<FractalParameters> getParameters() {
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

    public Optional<Integer> getIterations() {
        System.out.println("Введите количество итераций");
        String input = scanner.nextLine();
        try {
            int iterations = Integer.parseInt(input);
            return Optional.of(iterations);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    public Optional<List<FractalTransformation>> getTransformationsList() {
        try {
            System.out.println("Введите введите количество преобразований (не больше 6)");
            System.out.println("(Пустая строка для генерации 6 преобразований)");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return Optional.of(ParametersGenerator.generateTransformations());
            }

            int count = Integer.parseInt(input);

            System.out.println("Возможные нелинейные преобразования:");
            System.out.println(transformationsList());

            System.out.println("Возможные цвета преобразований:");
            System.out.println(colorList());

            List<FractalTransformation> list = new LinkedList<>();
            for (int i = 0; i < count; i++) {
                Optional<AffineTransformation> affineTransformation = getAffineTransformation();
                Optional<List<NonLinearTransformation>> optionalNonLinearList = getTransformationList();
                Optional<TransformationColor> optionalColor = getTransformationColors();
                if (affineTransformation.isEmpty() || optionalColor.isEmpty() || optionalNonLinearList.isEmpty()) {
                    continue;
                }

                list.add(new FractalTransformation(
                    affineTransformation.get(), optionalNonLinearList.get(), optionalColor.get()));
            }
            return Optional.of(list);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

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

    private Optional<TransformationColor> getTransformationColors() {
        System.out.println("Выберите цвет преобразования");
        String input = scanner.nextLine();
        int index = Integer.parseInt(input);
        return TransformationColor.getByIndex(index);
    }

    public Optional<AffineTransformation> getAffineTransformation() {
        System.out.println("Введите параметры линейного преобразования (6 чисел в диапазоне [-1, 1] через пробел)");
        System.out.println("(Пустая строка для генерации параметров)");

        String input = scanner.nextLine();
        if(input.isEmpty()) {
            return Optional.of(ParametersGenerator.generateAffineTransformation());
        }

        try {
            String[] numbers = input.split(" ");
            double[] coefficients = new double[6];
            for (int j = 0; j < 6; j++) {
                double value = Double.parseDouble(numbers[j]);
                if(value > 1 || value < -1) {
                    return Optional.empty();
                }
                coefficients[j] = value;
            }

            AffineTransformation affineTransformation = new AffineTransformation(
                coefficients[0],
                coefficients[1],
                coefficients[2],
                coefficients[3],
                coefficients[4],
                coefficients[5]
            );

            return Optional.of(affineTransformation);
        } catch (NumberFormatException exc) {
            return Optional.empty();
        }
    }

    public Optional<List<NonLinearTransformation>> getTransformationList() {
        System.out.println("Введите соответствующие ему нелинейные преобразования через пробел");
        System.out.println("(Пустая строка для генерации параметров)");

        String input = scanner.nextLine();

        if(input.isEmpty()) {
            return Optional.of(ParametersGenerator.generateNonLinearTransformationList());
        }

        try {
            List<NonLinearTransformation> list = new LinkedList<>();
            String[] parameters = input.split(" ");
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
