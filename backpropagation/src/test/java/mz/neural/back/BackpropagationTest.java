package mz.neural.back;

import org.junit.Test;

import static org.junit.Assert.*;

public class BackpropagationTest {
    @Test
    public void train() throws Exception {
        Backpropagation backpropagation = new Backpropagation(0.1);
        InputData inputData = new InputData(
                new InputRow(1, 2, 2, 0),
                new InputRow(1, 2, 1, 0),
                new InputRow(1, 2, 1, 0),
                new InputRow(1, 1, 1, 1),
                new InputRow(2, 2, 2, 1),
                new InputRow(2, 1, 2, 1),
                new InputRow(2, 2, 1, 1),
                new InputRow(2, 1, 1, 1),
                new InputRow(3, 1, 2, 0),
                new InputRow(3, 2, 2, 1),
                new InputRow(3, 2, 1, 1),
                new InputRow(3, 1, 1, 1),
                new InputRow(3, 1, 1, 1),
                new InputRow(1, 2, 1, 0),
                new InputRow(1, 2, 2, 0)
        );
        backpropagation.train(inputData);

        System.out.println();

        inputData.forEach(row -> System.out.println(row + " -> " + backpropagation.check(row)));
    }

    @Test
    public void check() throws Exception {
    }

}