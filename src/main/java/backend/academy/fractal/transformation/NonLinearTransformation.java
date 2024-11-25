package backend.academy.fractal.transformation;

import backend.academy.fractal.grid.BiUnitPoint;
import java.util.Optional;
import lombok.Getter;
import static backend.academy.fractal.transformation.TransformationColor.BLUE;
import static backend.academy.fractal.transformation.TransformationColor.DARK_BLUE;
import static backend.academy.fractal.transformation.TransformationColor.GOLD;
import static backend.academy.fractal.transformation.TransformationColor.GREEN;
import static backend.academy.fractal.transformation.TransformationColor.PURPLE;
import static backend.academy.fractal.transformation.TransformationColor.RED;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public enum NonLinearTransformation {
    SINUSOIDAL(1, p -> {
        p.x(sin(p.x()));
        p.y(sin(p.y()));
    }, GOLD),
    SPHERICAL(2, p -> {
        double rSquare = p.x() * p.x() + p.y() * p.y();
        p.x(p.x() / rSquare);
        p.y(p.y() / rSquare);
    }, GREEN),
    POLAR(3, p -> {
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        double theta = atan2(p.y(), p.x());
        p.x(theta / PI);
        p.y(r - 1);
    }, DARK_BLUE),
    HEART(4, p -> {
        double theta = atan2(p.y(), p.x());
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        p.x(r * sin(theta * r));
        p.y(-r * cos(theta * r));
    }, RED),
    LINEAR(5, p -> {
        //Nothing changes
    }, PURPLE),
    DISK(6, p -> {
        double theta = atan2(p.y(), p.x());
        double r = Math.sqrt(p.x() * p.x() + p.y() * p.y());
        p.x(theta / PI * sin(PI * r));
        p.y(theta / PI * cos(PI * r));
    }, BLUE);

    private final TransformationFunction function;
    @Getter private final int index;
    @Getter private final TransformationColor color;

    NonLinearTransformation(int index, TransformationFunction function, TransformationColor color) {
        this.index = index;
        this.function = function;
        this.color = color;
    }

    public static Optional<NonLinearTransformation> getByIndex(int index) {
        for (var transformation : NonLinearTransformation.values()) {
            if (transformation.index == index) {
                return Optional.of(transformation);
            }
        }

        return Optional.empty();
    }

    public void transform(BiUnitPoint point) {
        this.function.transform(point);
    }
}
