package backend.academy.fractal.transformation.impl;

import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.transformation.TransformationFunction;
import lombok.Getter;

@Getter
public class AffineTransformation implements TransformationFunction {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    public AffineTransformation(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    @Override
    public void transform(BiUnitPoint p) {
        p.x(a*p.x() + b*p.y() + c);
        p.y(d*p.x() + e*p.y() + f);
    }
}
