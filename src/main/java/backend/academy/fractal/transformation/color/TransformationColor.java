package backend.academy.fractal.transformation.color;

import java.awt.Color;
import java.util.Optional;
import lombok.Getter;

public enum TransformationColor {
    GOLD(1, new Color(255, 223, 0)),
    GREEN(2, new Color(30, 240, 80)),
    DARK_BLUE(3, new Color(30, 144, 255)),
    RED(4, new Color(255, 50, 50)),
    PURPLE(5, new Color(186, 85, 111)),
    BLUE(6, new Color(135, 206, 200));

    @Getter
    private final Color color;
    @Getter
    private final int index;

    TransformationColor(int index, Color color) {
        this.color = color;
        this.index = index;
    }

    public static Optional<TransformationColor> getByIndex(int index) {
        for (var color : TransformationColor.values()) {
            if (color.index == index) {
                return Optional.of(color);
            }
        }

        return Optional.empty();
    }
}
