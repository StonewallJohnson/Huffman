public class LetterData implements Comparable<LetterData> {
    protected String c;
    protected int frequency;
    protected LetterData left;
    protected LetterData right;

    public LetterData(String letter, int freq) {
        this(null, letter, freq, null);
    }

    public LetterData(LetterData l, String letter, int freq, LetterData r) {
        left = l;
        c = letter;
        frequency = freq;
        right = r;
    }

    public LetterData() {
    }

    @Override
    public int compareTo(LetterData o) {
        return this.frequency - o.frequency;
    }
}

