public class Score {
    private int score;

    public Score() { this.score = 0; }
    public void increment() { score++; }
    public void reset() { score = 0; }
    public int now() { return score; }
}
