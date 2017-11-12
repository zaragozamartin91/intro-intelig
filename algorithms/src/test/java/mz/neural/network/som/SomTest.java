package mz.neural.network.som;

import mz.neural.network.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class SomTest {
    @Autowired
    Som som;

    @Test
    public void testSort() {
        int n = 16;
        n /= 2;
        System.out.println(n);

        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(9);
        ints.add(5);
        ints.add(4);
        ints.add(3);
        ints.add(17);
        ints.sort(Integer::compare);
        System.out.println(ints);
    }

    @Test
    public void testApply() {
        List<SomRow> rows = new ArrayList<>();
        rows.add(new SomRow(new double[]{1, 1, 4, 3}));
        rows.add(new SomRow(new double[]{2, 2, 1, 3}));
        rows.add(new SomRow(new double[]{3, 1, 3, 4}));
        rows.add(new SomRow(new double[]{4, 3, 2, 4}));
        rows.add(new SomRow(new double[]{5, 2, 3, 1}));
        rows.add(new SomRow(new double[]{6, 4, 3, 2}));
        rows.add(new SomRow(new double[]{7, 1, 5, 5}));
        rows.add(new SomRow(new double[]{8, 5, 3, 1}));
        rows.add(new SomRow(new double[]{9, 1, 5, 2}));
        rows.add(new SomRow(new double[]{10, 3, 2, 4}));

        som.apply(new SomTable(rows), 4);
    }
}