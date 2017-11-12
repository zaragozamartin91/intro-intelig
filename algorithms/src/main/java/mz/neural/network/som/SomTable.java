package mz.neural.network.som;

import java.util.*;
import java.util.stream.Collectors;

public class SomTable {
    List<SomRow> rows = new ArrayList<>();

    public SomTable(Collection<SomRow> rows) {
        this.rows = new ArrayList<>(rows);
    }

    public SomTable getRandomClusters(int clusterCount) {
        if (clusterCount > rows.size()) throw new IllegalArgumentException("Cantidad invalida");
        Random random = new Random();

        List<Integer> randClusters = new ArrayList<>();
        while (clusterCount > randClusters.size()) {
            Integer idx = random.nextInt(rows.size());
            if (!randClusters.contains(idx)) randClusters.add(idx);
        }
        randClusters.sort(Integer::compare);
        List<SomRow> clusters = randClusters.stream().map(idx -> rows.get(idx)).collect(Collectors.toList());

        return new SomTable(clusters);
    }

    /**
     * Obtiene las distancias de los clusters a una fila (siendo this la tabla de clusters).
     *
     * @param otherRow Fila hacia la cual calcular la distancia.
     * @return Distancia al cluster this.
     */
    public SomRow calculateDistanceTo(SomRow otherRow) {
        List<SomRow> clusters = this.rows;
        List<Double> distances = clusters.stream().map(otherRow::calculateDistanceTo).collect(Collectors.toList());
        return new SomRow(distances);
    }

    /**
     * Obtiene la tabla de distancias de los registros con los clusters (siendo this la tabla de clusters).
     *
     * @param somTable Tabla de casos o registros.
     * @return Tabla de distancias a los cluster: fila = caso o registro ; columna = cluster.
     */
    public SomTable calculateDistanceTo(SomTable somTable) {
        SomTable clusterTable = this;
        List<SomRow> clusterDistances = somTable.rows.stream().map(clusterTable::calculateDistanceTo).collect(Collectors.toList());
        return new SomTable(clusterDistances);
    }

    /**
     * Obtiene el cluster al cual corresponde cada fila.
     *
     * @return Ids de cluster de cada fila.
     */
    public List<Integer> classify() {
        List<Integer> rowClusters = rows.stream().map(r -> r.getMinIndex()).collect(Collectors.toList());
        return rowClusters;
    }

    public List<SomTable> split(List<Integer> rowClusters) {
        // TODO
        return null;
    }

    public SomRow getAverages() {
        double[] values = new double[rows.get(0).size()];
        for (int i = 0; i < values.length; i++) values[i] = 0;

        rows.forEach(row -> {
            for (int i = 0; i < row.size(); i++) values[i] += row.get(i);
        });

        for (int i = 0; i < values.length; i++) values[i] /= rows.size();

        return new SomRow(values);
    }

    @Override
    public String toString() {
        return "SomTable{" +
                "rows=" + rows +
                '}';
    }
}
