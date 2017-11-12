package mz.neural.network.som;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Som {
    public SomResult apply(SomTable input, int clusterCount) {
        SomTable clusterTable = input.getRandomClusters(clusterCount);
        SomTable clusterDistances = clusterTable.calculateDistanceTo(input);
        List<Integer> rowClusters = clusterDistances.classify();


        return new SomResult();
    }
}
