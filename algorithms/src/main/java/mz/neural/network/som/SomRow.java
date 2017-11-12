package mz.neural.network.som;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SomRow {
    final double values[];

    public SomRow(double[] values) {
        this.values = values;
    }

    public SomRow(List<Double> distances) {
        values = new double[distances.size()];
        for (int i = 0; i < distances.size(); i++) values[i] = distances.get(i);
    }

    public double calculateDistanceTo(SomRow row) {
        if (this.values.length != row.values.length) throw new IllegalArgumentException("Filas incompatibles");

        double acc = 0;
        for (int i = 0; i < values.length; i++) acc += Math.pow(this.values[i] - row.values[i], 2);
        return Math.pow(acc, 0.5);
    }

    public double get(int idx) { return values[idx];}

    public int size() {return values.length;}


    public int getMinIndex() {
        int minIndex = 0;
        double minValue = Double.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < minValue) {
                minIndex = i;
                minValue = values[i];
            }
        }
        return minIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SomRow somRow = (SomRow) o;

        return Arrays.equals(values, somRow.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "SomRow{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
