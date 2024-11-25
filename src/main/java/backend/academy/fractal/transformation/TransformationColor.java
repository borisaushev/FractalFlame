package backend.academy.fractal.transformation;

import java.awt.Color;
import lombok.Getter;

public enum TransformationColor {
    GOLD(new Color(255, 223, 0)),
    GREEN(new Color(30, 240, 80)),
    DARK_BLUE(new Color(30, 144, 255)),
    RED(new Color(255, 50, 50)),
    PURPLE(new Color(186, 85, 111)),
    BLUE(new Color(135, 206, 200));

    @Getter
    private final Color color;

    TransformationColor(Color color) {
        this.color = color;
    }
}
