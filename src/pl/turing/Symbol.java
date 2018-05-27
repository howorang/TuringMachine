package pl.turing;

public enum  Symbol {
    ZERO('0'),
    ONE('1'),
    EMPTY('-');

    public final char symbolIcon;

    Symbol(char symbolIcon) {
        this.symbolIcon = symbolIcon;
    }
}
