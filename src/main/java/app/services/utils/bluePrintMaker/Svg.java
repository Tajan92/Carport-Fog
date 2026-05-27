package app.services.utils.bluePrintMaker;

public class Svg {
    private final static String SVG_START_TEMPLATE = "<svg x=\"%d\" y=\"%d\" width=\"%s\" height=\"%s\" viewBox=\"%s\" style=\"border: %dpx solid #ccc; \" preserveAspectRatio=\"xMinYMin\">";
    private final static String SVG_ARROW_DEFS_TEMPLATE = "<defs>\n" +
            "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n";
    private final static String SVG_ROOF_DEFS_TEMPLATE = "<pattern id=\"high-roof-tiles\" width=\"22\" height=\"16\" patternUnits=\"userSpaceOnUse\">\n" +
            "            <rect x=\"0\" y=\"0\" width=\"22\" height=\"16\" fill=\"none\" stroke=\"#555555\" stroke-width=\"0.5\"/>\n" +
            "            <!-- Traditional roof tile arc -->\n" +
            "            <path d=\"M 0 16 Q 11 11 22 16\" fill=\"none\" stroke=\"#000000\" stroke-width=\"0.8\"/>\n" +
            "        </pattern>"+"</defs>";

    private final static String SVG_GROUP_START_TEMPLATE = "<g transform=\"translate(%f,%f)\">";
    private final static String SVG_GROUP_END = "</g>";

    private final static String SVG_RECT_TEMPLATE = "<rect x=\"%f\" y=\"%f\" height=\"%f\" width=\"%f\"\n" +
            "              style=\"stroke:#000000; fill: %s\"/>";

    private final static String SVG_LINE_TEMPLATE = "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke: #000000;\" />";

    private final static String SVG_DASHED_LINE_TEMPLATE = "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"stroke:#000000; stroke-dasharray: 5 5;\" />";

    private final static String SVG_ARROW_TEMPLATE = "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\"\n" +
            "          style=\"stroke: #000000;  marker-start: url(#beginArrow); marker-end: url(#endArrow);\" />";

    private final static String SVG_TEXT_TEMPLATE = "<text style=\"text-anchor: middle\" transform=\"translate(%f,%f) rotate(%f)\">%s</text>\n";

    private final static String SVG_SHED_DASHED_LINE_TEMPLATE = "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" stroke=\"black\" stroke-width=\"2\" stroke-dasharray=\"12, 4\" />";
    private final static String SVG_SHED_RECT_TEMPLATE = "<rect x=\"%f\" y=\"%f\" stroke-width=\"2\" height=\"%f\" width=\"%f\"\n" +
            "              style=\"stroke:#000000; fill: #bfbfbf\"/>";

    private final static String SVG_ROOF_RECT_TEMPLATE = "<rect x=\"%f\" y=\"%f\" height=\"%f\" width=\"%f\"\n" +
            "              style=\"fill:url(#high-roof-tiles); stroke:#000000; stroke-width:1.2;\"/>";

    private final static String SVG_END = "</svg>";

    private StringBuilder svg = new StringBuilder();

    public Svg(int x, int y, String width, String height, String viewBox, int borderPx) {
        svg.append(String.format(SVG_START_TEMPLATE, x, y, width, height, viewBox, borderPx));
        svg.append(String.format(SVG_ARROW_DEFS_TEMPLATE));
        svg.append(String.format(SVG_ROOF_DEFS_TEMPLATE));
    }

    public void addRectangle(double x, double y, double height, double width, String fillColor) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, fillColor));
    }

    public void addLine(double x1, double y1, double x2, double y2) {
        svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2));
    }

    public void addDashedLine(double x1, double y1, double x2, double y2) {
        svg.append(String.format(SVG_DASHED_LINE_TEMPLATE, x1, y1, x2, y2));
    }

    public void addShedRectangle(double x, double y, double height, double width) {
        svg.append(String.format(SVG_SHED_RECT_TEMPLATE, x, y, width, height));
    }

    public void addShedDashedLine(double x1, double y1, double x2, double y2) {
        svg.append(String.format(SVG_SHED_DASHED_LINE_TEMPLATE, x1, y1, x2, y2));
    }

    public void addRoofRectangle(double x, double y, double height, double width) {
        svg.append(String.format(SVG_ROOF_RECT_TEMPLATE, x, y, height, width));
    }

    public void addArrow(double x1, double y1, double x2, double y2) {
        svg.append(String.format(SVG_ARROW_TEMPLATE, x1, y1, x2, y2));
    }

    public void addText(double x, double y, double rotation, String text) {
        svg.append(String.format(SVG_TEXT_TEMPLATE, x, y, rotation, text));
    }

    public void startGroup(double x, double y) {
        svg.append(String.format(SVG_GROUP_START_TEMPLATE, x, y));
    }

    public void endGroup() {
        svg.append(SVG_GROUP_END);
    }

    @Override
    public String toString() {
        return svg.toString() + SVG_END;
    }
}
