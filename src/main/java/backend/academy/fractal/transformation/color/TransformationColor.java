package backend.academy.fractal.transformation.color;

import java.awt.Color;
import java.util.Optional;
import lombok.Getter;

/**
 * Enum representing colors for fractal transformations.
 */
@Getter
public enum TransformationColor {
    GOLD(1, new Color(255, 223, 0)),
    GREEN(2, new Color(30, 240, 80)),
    DARK_BLUE(3, new Color(30, 144, 255)),
    RED(4, new Color(255, 50, 50)),
    PURPLE(5, new Color(186, 85, 111)),
    BLUE(6, new Color(135, 206, 200));

    private final Color color;
    private final int index;

    /**
     * Constructor to associate color with an index.
     *
     * @param index The index of the color.
     * @param color The color value.
     */
    TransformationColor(int index, Color color) {
        this.color = color;
        this.index = index;
    }

    /**
     * Gets a TransformationColor by its index.
     *
     * @param index The color index.
     * @return An Optional containing the color or empty if not found.
     */
    public static Optional<TransformationColor> getByIndex(int index) {
        for (var color : TransformationColor.values()) {
            if (color.index == index) {
                return Optional.of(color);
            }
        }
        return Optional.empty();
    }
}
