package mz.net;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SomAlgorithm {
    private int categories;

    private SomAlgorithm() { }

    public static SomAlgorithm buildNew() {
        return new SomAlgorithm();
    }

    public SomAlgorithm withCategories(int categories) {
        if (categories < 2) throw new IllegalArgumentException("Cantidad de categorias debe ser superior a 2");


        this.categories = categories;
        return this;
    }

    private double getDistanceBetween(RawRecord r1, RawRecord r2) {
        return r1.distanceTo(r2);
    }

    private Function<RawRecord, Double> distanceTo(RawRecord record) {
        return r -> r.distanceTo(record);
    }

    public Collection<ClassifiedRecord> applyOn(Collection<RawRecord> records) {
        if (records.size() <= categories) {
            throw new IllegalArgumentException("La cantidad de registros debe ser superior a la cantidad de categorias");
        }

        RawRecord[] recordArr = records.toArray(new RawRecord[]{});
        List<RawRecord> centroids = selectCentroidsRandomly(new ArrayList<>(records));

        List<Function<RawRecord, Double>> distanceFunctions = centroids
                .stream()
                .map(this::distanceTo)
                .collect(Collectors.toList());
        List<List<Double>> distances = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            RawRecord record = recordArr[i];
            List<Double> recordDistances = distanceFunctions.stream().map(f -> f.apply(record)).collect(Collectors.toList());
            distances.add(recordDistances);
        }

        List<Integer> mins = distances.stream().map(lst -> {
            Double minVal = lst.stream().min(Double::compare).get();
            return lst.indexOf(minVal);
        }).collect(Collectors.toList());

        Map<Integer, RawRecord> recordMap = new HashMap<>();
        for (int i = 0; i < records.size(); i++) recordMap.put(mins.get(i), recordArr[i]);



        return null;
    }

    private List<RawRecord> selectCentroidsRandomly(List<RawRecord> records) {
        List<RawRecord> chosenRecords = new ArrayList<>();

        while (chosenRecords.size() < categories) {
            int rand = (int) Math.floor(Math.random() * 100);
            RawRecord chosenRecord = records.get(rand);
            if (!chosenRecords.contains(chosenRecord)) chosenRecords.add(chosenRecord);
        }

        return chosenRecords;
    }
}
