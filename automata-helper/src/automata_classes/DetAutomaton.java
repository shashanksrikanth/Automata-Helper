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

	// Add a transition to the automaton, starting at p and going to q with label
	public void addTransition(String p, char label, String q) {
		Pair<String, Character> pair = new Pair<String, Character>(p, label);
		if (!this.delta.containsKey(pair)) {
			this.delta.put(pair, q);
		}
	}

	// Check whether a word w is accepted
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

	// Set a state to be final
	public void setFinal(String q) {
		this.finalStates.add(q);
	}

	// Return the set of final states
	public Set<String> getFinal() {
		return this.finalStates;
	}

	// Set a state to be initial
	public void setInitial(String q) {
		this.initialState = q;
	}

	// Get the initial state
	public String getInitial() {
		return this.initialState;
	}

	// Return all states
	public Set<String> getStates() {
		return this.states;
	}

	// Return transitions
	public Map<Pair<String, Character>, String> getDelta() {
		return this.delta;
	}

	// Get all transitions from a given state
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

	// Complete the automaton
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

