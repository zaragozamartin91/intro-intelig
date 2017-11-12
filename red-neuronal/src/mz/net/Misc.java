package mz.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Misc {
    public static void main(String[] args) {
        List<List<Double>> distances = new ArrayList<>();
        distances.add(Arrays.asList(new Double[]{1.2, 5.5, 4.6}));
        distances.add(Arrays.asList(new Double[]{9.2, 8.5, 1.6}));
        distances.add(Arrays.asList(new Double[]{10.2, 25.5, 44.6}));

        List<Double> mins = distances.stream().map(lst -> lst.stream().min(Double::compare).get()).collect(Collectors.toList());
        System.out.println(mins);
    }
}
