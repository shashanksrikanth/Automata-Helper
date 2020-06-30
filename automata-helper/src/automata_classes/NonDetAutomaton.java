package automata_classes;

import java.util.*;

public class NonDetAutomaton {
	private Set<String> states;
	private Map<Pair<String, Character>, Set<String>> delta;
	private Set<Character> alph;
	private String initialState;
	private Set<String> finalStates;
	private char EPSILON = '\u0395';

	/**
	 * Create an automaton on a given alphabet. {@link EPSILON} will never be part
	 * of the input alphabet.
	 * 
	 * @param alph The alphabet of the automaton, can be EMPTY_SET.
	 */
	public NonDetAutomaton(Set<Character> alph) {
		// TODO: Fill this.
		this.states = new HashSet<>();
		this.delta = new HashMap<>();
		this.alph = alph;
		this.initialState = "";
		this.finalStates = new HashSet<>();
	}

	// Alphabet of the automaton.
	public Set<Character> getAlphabet() {
		return this.alph;
	}

	/**
	 * Add a state to the automaton.
	 * 
	 * @param q The state to add.
	 */
	public void addState(String q) {
		if (!this.states.contains(q))
			this.states.add(q);
	}

	/**
	 * Add a transition to the automaton.
	 * 
	 * @param p     State from which the transition goes.
	 * @param label Label of the transition, can be {@link EPSILON}.
	 * @param q     State to which the transition goes.
	 */
	public void addTransition(String p, char label, String q) {
		Pair<String, Character> pair = new Pair<String, Character>(p, label);
		if (!this.delta.containsKey(pair)) {
			Set<String> destination = new HashSet<>();
			destination.add(q);
			this.delta.put(pair, destination);
		} else {
			this.delta.get(pair).add(q);
		}
	}

	// Implementing DFA to get states reached by E
	private Set<String> E(Set<String> S) {
		Set<String> reachedByE = new HashSet<>();
		reachedByE.addAll(S);
		for (String state : S) {
			reachedByE.addAll(E_helper(state));
		}
		return reachedByE;
	}

	private Set<String> E_helper(String state) {
		Map<String, Boolean> visited = new HashMap<String, Boolean>();
		Stack<String> stack = new Stack<>();
		Set<String> e_transitions = new HashSet<>();
		stack.push(state);
		while (!stack.isEmpty()) {
			String currentState = stack.pop();
			visited.put(currentState, true);
			Set<Pair<Character, String>> transitions = this.getTransitionsFrom(currentState);
			for (Pair<Character, String> pair : transitions) {
				if (pair.getKey() == EPSILON) {
					e_transitions.add(pair.getValue());
					if (!visited.containsKey(pair.getValue()))
						stack.push(pair.getValue());
				}
			}
		}
		return e_transitions;
	}

	/**
	 * Check whether a word is accepted by the automaton.
	 * 
	 * @param w An input word.
	 * @return Whether the automaton accepts the input word.
	 */
	public boolean accepts(String w) {
		Set<String> currentStates = new HashSet<>();
		Set<String> initialStates = new HashSet<>();
		initialStates.add(this.initialState);
		currentStates.addAll(this.E(initialStates));
		for (int i = 0; i < w.length(); i++) {
			char label = w.charAt(i);
			Set<String> nextStates = new HashSet<>();
			for (String state : currentStates) {
				for (Pair<String, Character> key : this.delta.keySet()) {
					if (key.getKey().equals(state) && key.getValue() == label) {
						nextStates.addAll(this.E(this.delta.get(key)));
					}
				}
			}
			if (nextStates.isEmpty())
				return false;
			currentStates = nextStates;
		}
		for (String state : currentStates) {
			if (this.finalStates.contains(state))
				return true;
		}
		return false;
	}

	/**
	 * Set a state to be final.
	 * 
	 * @param q State to add to the set of final states.
	 */
	public void setFinal(String q) {
		this.finalStates.add(q);
	}

	// Return the set of final states.
	public Set<String> getFinal() {
		return this.finalStates;
	}

	/**
	 * Set a state to be initial.
	 * 
	 * @param q The state to set as initial.
	 */
	public void setInitial(String q) {
		this.initialState = q;
	}

	// Return initial state
	public String getInitial() {
		return this.initialState;
	}

	// Return set of states
	public Set<String> getStates() {
		return this.states;
	}

	/**
	 * Get all transitions from a given state.
	 * 
	 * @param p The state of which we want the outgoing transitions.
	 * @return The set of outgoing transitions, where each outgoing transition is a
	 *         pair (label, destination).
	 */
	public Set<Pair<Character, String>> getTransitionsFrom(String p) {
		Set<Pair<Character, String>> transitions = new HashSet<>();
		for (Pair<String, Character> key : this.delta.keySet()) {
			if (key.getKey().equals(p)) {
				for (String state : this.delta.get(key)) {
					Pair<Character, String> pair = new Pair<Character, String>(key.getValue(), state);
					transitions.add(pair);
				}
			}
		}
		return transitions;
	}

	/**
	 * Determinize an automaton.
	 * 
	 * @return An equivalent deterministic automaton.
	 */
	public DetAutomaton determinize() {
		// TODO: Fill this.
		DetAutomaton B = new DetAutomaton(getAlphabet());

		Set<String> initialState = new HashSet<>();
		initialState.add(this.initialState);
		Set<String> initialStates = this.E(initialState);
		String initial = this.createState(initialStates);
		B.addState(initial);
		B.setInitial(initial);

		Queue<String> dfa_states = new LinkedList<>();
		dfa_states.add(initial);
		while (!dfa_states.isEmpty()) {
			String currentState = dfa_states.remove();
			for (char label : B.getAlphabet()) {
				String nextState = union(currentState, label);
				B.addTransition(currentState, label, nextState);
				if (!B.getStates().contains(nextState)) {
					B.addState(nextState);
					dfa_states.add(nextState);
				}
			}
		}

		for (String state : B.getStates()) {
			for (String finalState : this.finalStates) {
				if (state.contains(finalState))
					B.setFinal(state);
			}
		}
		return B;
	}

	private String union(String states, char label) {
		String newState = "";
		Set<String> nextStates = new HashSet<>();
		for (String state : states.split(",")) {
			Set<Pair<Character, String>> transitions = this.getTransitionsFrom(state);
			transitions.stream().filter(pair -> pair.getKey().equals(label)).forEach(pair -> nextStates.add(pair.getValue()));
		}
		newState = this.createState(this.E(nextStates));
		return newState;
	}

	private String createState(Set<String> s) {
		String initial = "";
		for (String state : s) {
			if (initial.length() == 0)
				initial += state;
			else if (!initial.contains(state))
				initial = initial + "," + state;
			else
				;
		}
		return initial;
	}
}

