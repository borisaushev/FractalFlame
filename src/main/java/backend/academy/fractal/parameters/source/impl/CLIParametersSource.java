package backend.academy.fractal.parameters.source.impl;

import backend.academy.fractal.parameters.FractalParameters;
import backend.academy.fractal.parameters.source.ParameterSource;
import javax.swing.text.AttributeSet;
import java.util.Scanner;

public class CLIParametersSource implements ParameterSource {

    private static final Scanner scanner = new Scanner(System.in);
    public FractalParameters getParameters() {
        //TODO exception handling
        System.out.println("Введите размеры фрактала (ширина и высота в пикселях через пробел)");
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        System.out.println("\nВведите список трансформационных функций (Пустая строка для всех сразу)");
        System.out.println("\nВыберите одну из: ");

        return null;
    }
}
