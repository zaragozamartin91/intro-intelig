package mz.neural.back;

public class Backpropagation {
    double alpha;
    WeightRow weightRow;

    public Backpropagation(double alpha) {
        this.alpha = alpha;
    }

    public synchronized WeightRow train(InputData inputData) {
        int colCount = inputData.getColCount();
        weightRow = WeightRow.generate(colCount - 1); // la ultima columna es de resultados

        System.out.println("Pesos iniciales: " + weightRow);

        inputData.forEach(row -> {
            System.out.println("Calculando fila " + row);

            double zval = weightRow.matchWith(row);
            System.out.println("Valor calculado: " + zval);

            weightRow = weightRow.refactor(alpha, zval, row);
            System.out.println("Pesos nuevos: " + weightRow);
        });

        return new WeightRow(weightRow);
    }

    public double check(InputRow input) {
        return weightRow.matchWith(input);
    }

    public boolean hasMatch(double zval) {
        return zval >= 0.5;
    }
}
