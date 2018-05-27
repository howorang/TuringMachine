package pl.turing;

import java.util.*;

import static pl.turing.Symbol.*;
import static pl.turing.Direction.*;

public class TuringMachine {

    private static Map<String, State> stateMap = new HashMap<>();
    private State currentState;
    private List<Symbol> tape = new ArrayList<>();
    private int currentTapePosition;
    private String history;

    static {
        List<State> states = new ArrayList<>();
        states.add(State.withName("qa1")
                .on(ZERO).move(LEFT).write(ONE).goTo("qa2")
                .on(ONE).move(LEFT).write(ZERO).goTo("qa1r")
                .on(EMPTY).goTo("error")
                .build());

        states.add(State.withName("qa2")
                .on(ZERO).move(LEFT).write(ONE).goTo("qr")
                .on(ONE).move(LEFT).write(ZERO).goTo("qa2r")
                .on(EMPTY).move(LEFT).write(ONE).goTo("qd")
                .build());

        states.add(State.withName("qa1r")
                .on(ZERO).move(LEFT).write(ZERO).goTo("qa2")
                .on(ONE).move(LEFT).write(ONE).goTo("qa2")
                .on(EMPTY).move(LEFT).write(ZERO).goTo("qa2")
                .build());

        states.add(State.withName("qa2r")
                .on(ZERO).move(LEFT).write(ONE).goTo("qr")
                .on(ONE).move(LEFT).write(ZERO).goTo("qa2r")
                .on(EMPTY).move(LEFT).write(ONE).goTo("qd")
                .build());

        states.add(State.withName("qr")
                .on(ZERO).move(LEFT).write(ZERO).goTo("qr")
                .on(ONE).move(LEFT).write(ONE).goTo("qr")
                .on(EMPTY).goTo("qd")
                .build());

        states.add(State.withName("qd")
                .on(ZERO).goTo("qd")
                .on(ONE).goTo("qd")
                .on(EMPTY).goTo("qd")
                .isAcceptingState()
                .build());

        states.add(State.withName("error")
                .on(ZERO).goTo("error")
                .on(ONE).goTo("error")
                .on(EMPTY).goTo("error")
                .isAcceptingState()
                .build());
        states.forEach(state -> stateMap.compute(state.name, (key, oldVal) -> state));
    }

    public TuringMachine(String input) {
        for (Character character : input.toCharArray()) {
            switch (character) {
                case '0':
                    tape.add(Symbol.ZERO);
                    break;
                case '1':
                    tape.add(Symbol.ONE);
                    break;
                case'-':
                    tape.add(Symbol.EMPTY);
                    break;
                    default:
                        throw new InvalidInputException();
            }
            currentState = stateMap.get("qa1");
            currentTapePosition = tape.size() - 1;
            history = currentState.name;
        }
    }

    public void proccess() {
        Transition transitionForSymbol = currentState.getTransitionForSymbol(tape.get(currentTapePosition));
        if (transitionForSymbol.writeSymbol != null) {
            tape.set(currentTapePosition, transitionForSymbol.writeSymbol);
        }
        if (transitionForSymbol.direction == LEFT) {
            currentTapePosition-=1;
            if (currentTapePosition == -1) {
                tape.add(0, EMPTY);
                currentTapePosition = 0;
            }
        } else if (transitionForSymbol.direction == RIGHT) {
            currentTapePosition+=1;
            if (currentTapePosition > tape.size() - 1) {
                tape.add(EMPTY);
            }
        }
        currentState = stateMap.get(transitionForSymbol.nextStateName);

        history += " -> " + currentState.name;
    }

    public State getCurrentState() {
        return currentState;
    }

    public List<Symbol> getTape() {
        return new ArrayList<>(tape);
    }

    public int getCurrentTapePosition() {
        return currentTapePosition;
    }

    public String getHistory() {
        return history;
    }
}
