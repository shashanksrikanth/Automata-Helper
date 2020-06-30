package automata_classes;

import java.util.*;

public class Helper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to AutomataHelper!");
		System.out.println("What would you like to do today? Enter the option below");
		System.out.println("A: Check if a word is accepted by a DFA.");
		System.out.println("B: Check if a word is accepted by a NFA.");
		System.out.println("C: Convert a NFA to a DFA.");
		System.out.println("D: Convert NFA to a Regular Expression.");
		System.out.println("E: Convert a Regular Expression to a NFA.");
		System.out.println("F: Quit the program.");
		String choice = scan.nextLine();
		Set<String> choices = new HashSet<String>();
		choices.add("a");
		choices.add("b");
		choices.add("c");
		choices.add("d");
		choices.add("e");
		choices.add("f");
		if(!choices.contains(choice.toLowerCase())) {
			System.out.println("Unknown choice detected. Terminating program.");
			System.exit(0);
		}
		if(choice.toLowerCase().equals("a")) {
			System.out.println("You have chosen to check if a word is accepted by a DFA!");
			System.out.println("Enter the alphabet of your DFA. Each alphabet needs to be seperated by a comma.");
			System.out.println("Example: a,b,c");
			String alphabetInput = scan.nextLine();
			String[] alphabetArray = alphabetInput.split(",");
			Set<Character> alphabet = new HashSet<>();
			for(String alph : alphabetArray) alphabet.add(alph.charAt(0));
			DetAutomaton dfa = new DetAutomaton(alphabet);
			System.out.println("Enter the states of your DFA. Each state needs to be seperated by a comma.");
			System.out.println("Example: q0,q1,q2");
			String stateInput = scan.nextLine();
			String[] stateArray = stateInput.split(",");
			for(String state : stateArray) dfa.addState(state);
			System.out.println("Enter initial state");
			String initialState = scan.nextLine();
			dfa.setInitial(initialState);
			System.out.println("Enter the final states of your DFA. Each state needs to be seperated by a comma.");
			System.out.println("Example: q0,q1,q2");
			String finalInput = scan.nextLine();
			String[] finalArray = finalInput.split(",");
			for(String state : finalArray) dfa.setFinal(state);
			System.out.println("Enter the transitions between each state.");
			while(true) {
				System.out.println("Start state: ");
				String start = scan.nextLine();
				System.out.println("Transition: ");
				String label = scan.nextLine();
				System.out.println("End state: ");
				String end = scan.nextLine();
				dfa.addTransition(start, label.charAt(0), end);
				System.out.println("Type YES to continue adding transitions. Type NO to finish");
				String cont = scan.nextLine();
				if(cont.toLowerCase().equals("no")) break;
			}
			System.out.println("DFA created with...");
			System.out.println("Alphabet: "+dfa.getAlphabet());
			System.out.println("States: "+dfa.getStates());
			System.out.println("Initial state: "+dfa.getInitial());
			System.out.println("Final state: "+dfa.getFinal());
			System.out.println("Transitions: ");
			for(Pair<String, Character> key : dfa.getDelta().keySet()) {
				System.out.println(key.getKey()+","+key.getValue()+","+dfa.getDelta().get(key));
			}
			System.out.println("Enter word: ");
			String word = scan.nextLine();
			boolean accept = dfa.accepts(word);
			if(accept) System.out.println("Word is accepted.");
			else System.out.println("Word is not accepted.");
		}
		scan.close();
	}

}

