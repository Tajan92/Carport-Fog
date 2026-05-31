package app.services.utils.bluePrintMaker;

public class RoofHeightCalculator {
    public static double calculateRoofHeight(double width, double roofSlope) {
        double run = width / 2.0;
        double angleInRadians = Math.toRadians(roofSlope);
        return run * Math.tan(angleInRadians);
    }
}
