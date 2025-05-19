package dev.zsebel.bitcoin.integration.support;

public enum Times {
    ONCE(1),
    FOUR(4),
    FIVE(5),
    SIX(6);

    private final int count;

    Times(int count) {
        this.count = count;
    }

    public int value() {
        return count;
    }
}
