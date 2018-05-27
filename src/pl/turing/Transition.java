package pl.turing;

public class Transition {
    public final Symbol triggerSymbol;
    public final Symbol writeSymbol;
    public final Direction direction;
    public final String nextStateName;

    public Transition(Symbol triggerSymbol, Symbol writeSymbol, Direction direction, String nextStateName) {
        this.triggerSymbol = triggerSymbol;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
        this.nextStateName = nextStateName;
    }
}
