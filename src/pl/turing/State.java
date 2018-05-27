package pl.turing;

import java.util.*;
import java.util.stream.Collectors;

public class State {

    public final String name;
    private Map<Symbol, Transition> transitions = new HashMap<>();
    public final boolean isAcceptingState;


    private State(String name, boolean isAcceptingState) {
        this.name = name;
        this.isAcceptingState = isAcceptingState;
    }

    public static StateBuilder withName(String name) {
        return new StateBuilder(name);
    }

    public Transition getTransitionForSymbol(Symbol symbol) {
        return transitions.get(symbol);
    }

    private static void validate(State state) {
        boolean hasAllTheTransitions = state.transitions
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().triggerSymbol)
                .collect(Collectors.toSet())
                .containsAll(Arrays.asList(Symbol.values()));
        if (!hasAllTheTransitions) {
            throw new RuntimeException("Invalid state");
        }
    }

    public static class StateBuilder {
        private String name;
        private Set<TransitionBuilder> transitionsBuilders = new HashSet<>();
        private boolean isAcceptingState = false;

        public StateBuilder(String name) {
            this.name = name;
        }

        public TransitionBuilder on(Symbol triggerSymbol) {
            TransitionBuilder transitionBuilder = new TransitionBuilder(this, triggerSymbol);
            transitionsBuilders.add(transitionBuilder);
            return transitionBuilder;
        }

        public StateBuilder isAcceptingState() {
            isAcceptingState = true;
            return this;
        }

        public State build() {
            State newState = new State(this.name, isAcceptingState);
            for (TransitionBuilder transitionBuilder : transitionsBuilders) {
                newState.transitions.put(transitionBuilder.triggerSymbol, transitionBuilder.buildTransition());
            }
            validate(newState);
            return newState;
        }
    }

    public static class TransitionBuilder {
        private StateBuilder parent;
        private Symbol triggerSymbol;
        private Symbol writeSymbol;
        private Direction direction;
        private String nextStateName;

        public TransitionBuilder(StateBuilder parent, Symbol triggerSymbol) {
            this.parent = parent;
            this.triggerSymbol = triggerSymbol;
        }

        public TransitionBuilder write(Symbol symbol) {
            writeSymbol = symbol;
            return this;
        }

        public TransitionBuilder move(Direction direction) {
            this.direction = direction;
            return this;
        }

        public TransitionBuilder goTo(String nextStateName) {
            this.nextStateName = nextStateName;
            return this;
        }

        public TransitionBuilder on(Symbol triggerSymbol) {
            return parent.on(triggerSymbol);
        }

        private Transition buildTransition() {
            return new Transition(triggerSymbol, writeSymbol, direction, nextStateName);
        }

        public StateBuilder isAcceptingState() {
            return parent.isAcceptingState();
        }

        public State build() {
            return parent.build();
        }
    }
}
