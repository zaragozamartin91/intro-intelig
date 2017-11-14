package mz.neural.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightRow extends InputRow {
    public WeightRow(List<Double> data) {
        super(data);
    }

    public WeightRow(Double... values) {
        super(values);
    }

    public WeightRow(WeightRow weightRow) { this(weightRow.data);}

    public static WeightRow generate(int size) {
        List<Double> values = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            double value = random.nextDouble(); // genera valor entre 0 y 1
            boolean positive = random.nextBoolean(); // determino si sera positivo o no
            values.add(positive ? value : value * -1); // guardo valores entre -1 y 1
        }

        return new WeightRow(values);
    }

    public double matchWith(InputRow row) {
        double res = 0.0;
        Double[] weightValues = this.toArray();
        Double[] inputValues = row.toArray();

        // hago el calculo partiendo de las celdas de pesos
        for (int i = 0; i < weightValues.length; i++) res += weightValues[i] * inputValues[i];

        return res;
    }

    public WeightRow refactor(double alpha, double zval, InputRow input) {
        List<Double> newWeights = new ArrayList<>();

        double checkVal = input.getCheckValue();
        Double[] currWeights = this.toArray();
        Double[] inputValues = input.toArray();

        for (int i = 0; i < currWeights.length; i++) {
            double newWeight = currWeights[i] + alpha * (checkVal - zval) * inputValues[i];
            newWeights.add(newWeight);
        }

        return new WeightRow(newWeights);
    }

}
