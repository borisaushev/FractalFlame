package backend.academy.fractal.grid;

import lombok.Getter;

@Getter
public final class Frame {
    private final int height;
    private final int width;
    private final Pixel[][] pixelGrid;

    public Frame(int height, int width) {
        this.height = height;
        this.width = width;
        this.pixelGrid = new Pixel[height][width];
    }

    public FramePoint convertToFramePoint(BiUnitPoint point) {
        int px = (int) ((point.x() + 1) / 2.0 * width());
        int py = (int) ((point.y() + 1) / 2.0 * height());
        return new FramePoint(px, py);
    }
}
