package backend.academy.fractal.transformation.impl;

import backend.academy.fractal.grid.BiUnitPoint;
import backend.academy.fractal.transformation.TransformationFunction;
import lombok.Getter;

@Getter
public class AffineTransformation implements TransformationFunction {
    private final int a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;
    private final int f;

    public AffineTransformation(int a, int b, int c, int d, int e, int f) {
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
