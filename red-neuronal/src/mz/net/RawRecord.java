package mz.net;

public class RawRecord {
    private double[] values;

    public RawRecord(double[] values) {
        this.values = values;
    }

    public double distanceTo(RawRecord otherRecord) {
        double[] otherValues = otherRecord.values;
        double acc = 0;
        for (int i = 0; i < values.length; i++) {
            acc += Math.pow(values[i] - otherValues[i], 2);
        }
        return Math.pow(acc, 0.5);
    }

    public double value(int idx) { return values[idx]; }
}
