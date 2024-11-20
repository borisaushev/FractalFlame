package backend.academy.fractal.transformation;

import backend.academy.fractal.grid.Point;
import java.util.function.Consumer;

public interface TransformationApplier {
    void transform(Point point);
}
