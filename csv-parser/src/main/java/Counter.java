public class Counter {
    int count = 0;

    Counter augment() {
        ++count;
        return this;
    }
}