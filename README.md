# Automata-Helper

Before we go forward, the acronyms used in this guide are:
1. DFA: deterministic finite automaton
2. NFA: non-deterministic finite automaton

## Overview
This is a tool built using Java that helps with the following tasks:
1. Checking if a word is accepted by a DFA
2. Checking if a word is accepted by a NFA
3. Converting a DFA to a NFA
4. Completing a DFA
The logic behind these algorithms can be found online, or in the book *Introduction to the Theory of Computation*, by Michael Sipser. Note that the algorithms in this repo are based on the explanation given by Sipser.

## How to run the code
Before running this, clone the repo first. You can run this either through an IDE (recommended) or the terminal. To run it from an IDE of your choice, go to **Main.java** and simply run that file. To run it from the terminal, first check if you have Java installed on your machine: `java -version`. If you get an output with the version, you have Java on your machine. Go forward and run the following commands:

`cd path_to_folder/Automata-Helper/automata-helper/src/automata_classes`

`javac Main.java`

`java Main`

## Further work
Further work can consist of adding functionality that can change a regular expression into a NFA or a NFA into a regular expression.
