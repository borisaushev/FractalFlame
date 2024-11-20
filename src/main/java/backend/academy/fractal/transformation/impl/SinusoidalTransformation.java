package backend.academy.fractal.transformation.impl;

import backend.academy.fractal.grid.Point;
import backend.academy.fractal.transformation.TransformationApplier;

public class SinusoidalTransformation implements TransformationApplier {


    /*
    F(x,y) -> a, b ,c ,d ,e ,f
    = P(Sum(Vi(x*a + y*b + c, x*d + y*e + f)/n(|V|))

    Pi(x, y) = (αix + βiy + γi, δix + ǫiy + ζi)


     */
    @Override
    public void transform(Point point) {
    }
}
