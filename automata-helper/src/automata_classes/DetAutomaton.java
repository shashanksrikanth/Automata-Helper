package automata_classes;

import java.util.Set;
import java.util.*;

// Implementation of deterministic automata.
public class DetAutomaton {
	private Set<String> states;
	private Map<Pair<String, Character>, String> delta;
	private Set<Character> alph;
	private String initialState;
	private Set<String> finalStates;
	private int sink_id;

	public DetAutomaton(Set<Character> alph) {
		this.states = new HashSet<>();
		this.delta = new HashMap<>();
		this.alph = alph;
		this.initialState = "";
		this.finalStates = new HashSet<>();
		this.sink_id = 0;
	}

	// Alphabet of the automaton
	public Set<Character> getAlphabet() {
		return this.alph;
	}

	// Add a state
	public void addState(String q) {
		if (!this.states.contains(q))
			this.states.add(q);
	}

	/**
	 * Add a transition to the automaton.
	 * 
	 * @param p     State from which the transition goes.
	 * @param label Label of the transition.
	 * @param q     State to which the transition goes.
	 */
	public void addTransition(String p, char label, String q) {
		Pair<String, Character> pair = new Pair<String, Character>(p, label);
		if (!this.delta.containsKey(pair)) {
			this.delta.put(pair, q);
		}
	}

	/**
	 * Check whether a word is accepted by the automaton.
	 * 
	 * @param w An input word.
	 * @return Whether the automaton accepts the input word.
	 */
	public boolean accepts(String w) {
		String state = this.initialState;
		Pair<String, Character> transition;
		for (int i = 0; i < w.length(); i++) {
			transition = new Pair<String, Character>(state, w.charAt(i));
			state = this.delta.get(transition);
		}
		if (this.finalStates.contains(state))
			return true;
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

	// Return the set of final states
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

	/**
	 * Get the initial state.
	 * 
	 * @return The initial state.
	 */
	public String getInitial() {
		return this.initialState;
	}

	/**
	 * Return the set of states.
	 * 
	 * @return The set of states.
	 */
	public Set<String> getStates() {
		return this.states;
	}

	// Return transitions
	public Map<Pair<String, Character>, String> getDelta() {
		return this.delta;
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
				Pair<Character, String> pair = new Pair<Character, String>(key.getValue(), this.delta.get(key));
				transitions.add(pair);
			}
		}
		return transitions;
	}

	/**
	 * Complete the automaton in place, adding a new state only if necessary.
	 */
	public void complete() {
		String sink = "sink" + this.sink_id;
		this.addState(sink);
		this.sink_id = this.sink_id + 1;
		for (String state : this.states) {
			Set<Character> labels = new HashSet<>();
			Set<Pair<Character, String>> transitions = this.getTransitionsFrom(state);
			for (Pair<Character, String> pair : transitions) {
				labels.add(pair.getKey());
			}
			for (char alphabet : this.alph) {
				if (!labels.contains(alphabet)) {
					this.addTransition(state, alphabet, sink);
					this.addTransition(sink, alphabet, sink);
				}
			}
		}
	}
}

