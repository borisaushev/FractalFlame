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
        int px = (int) ((point.x()) / 4.0 * width() + (double) width() / 2);
        int py = (int) ((point.y()) / 4.0 * height() + (double) height() / 2);
        return new FramePoint(px, py);
    }

    public boolean pointInBounds(FramePoint point) {
        return point.x() >= 0 && point.x() < width()
            && point.y() >= 0 && point.y() < height();
    }
}
