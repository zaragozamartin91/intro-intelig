package mz.iint.bank;

public class CsvStats {
    private int yescount = 0;
    private int nocount = 0;

    private int prevYesCount = 0;
    private int prevNoCount = 0;

    private int campOneCount = 0;
    private int campGreaterCount = 0;

    public void augmentYes() { ++yescount; }

    public void augmentNo() { ++nocount; }

    public void augmentPrevYes() { ++prevYesCount; }

    public void augmentPrevNo() { ++prevNoCount; }

    public void augmentCampOne() { ++campOneCount; }

    public void augmentCampGreater() { ++campGreaterCount; }

    public int getTotalCount() { return yescount + nocount; }

    public int getYescount() {
        return yescount;
    }

    public int getNocount() {
        return nocount;
    }

    public int getPrevYesCount() {
        return prevYesCount;
    }

    public int getPrevNoCount() {
        return prevNoCount;
    }

    public int getCampOneCount() {
        return campOneCount;
    }

    public int getCampGreaterCount() {
        return campGreaterCount;
    }
}
