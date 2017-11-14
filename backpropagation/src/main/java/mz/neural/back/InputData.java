package mz.neural.back;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class InputData implements Iterable<InputRow> {
    private List<InputRow> rows;

    public InputData(List<InputRow> rows) {
        this.rows = new ArrayList<>(rows);
    }

    public InputData(InputRow... rows) { this(Arrays.asList(rows)); }

    public int size() {return rows.size();}

    public Iterator<InputRow> iterator() {return rows.iterator();}

    public boolean add(InputRow doubles) {return rows.add(doubles);}

    public InputRow get(int index) {return rows.get(index);}

    public void forEach(Consumer<? super InputRow> action) {rows.forEach(action);}

    public int getColCount() { return this.get(0).size(); }

}
