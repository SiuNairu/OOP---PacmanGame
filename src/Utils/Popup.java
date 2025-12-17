package Utils;

public class Popup {
    private int x;
    private int y;
    private String text;
    private long createdMs;
    private long durationMs;

    public Popup(int x, int y, String text, long createdMs, long durationMs) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.createdMs = createdMs;
        this.durationMs = durationMs;
    }

    public boolean isExpired(long now) {
        return now >= createdMs + durationMs;
    }

    public int getX()           { return x; }
    public int getY()           { return y; }
    public String getText()     { return text; }
    public long getCreatedMs()  { return createdMs; }
    public long getDurationMs() { return durationMs; }
}
