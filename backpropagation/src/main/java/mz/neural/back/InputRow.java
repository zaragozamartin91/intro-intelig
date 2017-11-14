package mz.neural.back;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class InputRow implements Iterable<Double> {
    List<Double> data = new ArrayList<>();

    public InputRow(List<Double> data) {
        this.data = new ArrayList<>(data);
    }

    public InputRow(Double... values) {
        this(Arrays.asList(values));
    }

    public InputRow(int... values) {
        for (int value : values) data.add((double) value);
    }


    public int size() {return data.size();}


    public Iterator<Double> iterator() {return data.iterator();}


    public <T> T[] toArray(T[] a) {return data.toArray(a);}


    public Double[] toArray() {return this.toArray(new Double[]{});}


    public boolean add(Double aDouble) {return data.add(aDouble);}


    public Double get(int index) {return data.get(index);}


    public void forEach(Consumer<? super Double> action) {data.forEach(action);}

    public double getCheckValue() {
        return this.data.get(this.size() - 1);
    }

    @Override
    public String toString() {
        return "InputRow{" + data + '}';
    }
}
