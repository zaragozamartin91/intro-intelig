package mz.iint.bank;

public class CsvStats {
    private int yescount = 0;
    private int nocount = 0;

    public void augmentYes() { ++yescount; }

    public void augmentNo() { ++nocount; }

    public int getTotalCount() { return yescount + nocount; }

    public int getYescount() {
        return yescount;
    }

    public int getNocount() {
        return nocount;
    }
}
