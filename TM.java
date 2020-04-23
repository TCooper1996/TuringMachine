import java.util.HashMap;
import java.util.Arrays;
/**
 *
 * @author Tayler Cooper
 * @version 4/19/2020
 */
public class TM
{
    int state;
    char[] tape;
    //The current input symbol.
    String input;
    //current position of the tape head
    int head;
    int numberOfStates;
    int numberOfSymbols; //Number of symbols that may be read.
    //The transition table is written below, and each line consists of 6 occurrences of the below pattern.
    //CURRENT_SYMBOL(NEXT-SYMBOL, NEXT-STATE, LEFT/RIGHT)
    //The first line provides the transitions from state q0, the second line the transitions from state q1, so on and so forth, where the final line lists the transitions from state q10, and each pair of parentheses represents a transition in which the digit preceding the parenthesis
    //is the input symbol, and the next three values in the parentheses indicate the next state, symbol to write, and whether to move left or right.
    //Parentheses that are left blank indicate that the machine must halt and reject if it is in the state specified at the beginning of that row and reads the symbol preceding the empty parentheses
    //For example, for state 0, represented below in the first line of the string, z() means halt if reading a z. If reading a 0, move to state 1, write an x, and move right.
    //The following strings encode a turing machine (although it really acts as a DFA) that halts and accepts
    //if the input string contains an even number of 0s and exactly 2 1's, and rejects otherwise.
    String[] transitions =
    {
            "0(1,0,>) 1(4,1,>) _()",
            "0(2,0,>) 1(3,1,>) _()",
            "0(1,0,>) 1(4,1,>) _()",
            "0(4,0,>) 1(5,1,>) _()",
            "0(3,0,>) 1(6,1,>) _()",
            "0(6,0,>) 1() _()",
            "0(5,0,>) 1() _(7,_,>)"
};
    //This is a 2d array containing the transitions. transitionTable[state][symbolMap.get(input)] returns the transition
    //for the given state and input symbol.
    Transition[][] transitionTable;

    //Use a hashmap to associate each symbol to a number, allowing us to index into the transition array with symbols.
    HashMap<String, Integer> symbolMap = new HashMap<>();

    //A class that represents a transition, containing the next state, symbol to write, and whether to move left or right
    private static class Transition{
        int nextState;
        String nextSymbol;
        boolean moveRight; //0 means move left, 1 means move right

        //Class representing a transition
        private Transition(String nextState, String nextSymbol, String direction){
            this.nextState = Integer.parseInt(nextState);
            this.nextSymbol = nextSymbol;
            this.moveRight = direction.charAt(0)=='>';
        }
    }
    
    //Initialize the Machine
    //Place the input on the tape. No more than 255 symbols
    public TM(char[] input){

        //Map each symbol to a number, so that each symbol can be used to index into the transition array
        symbolMap.put("0",0);
        symbolMap.put("1",1);
        symbolMap.put("_",2);
        symbolMap.put("x",3);
        symbolMap.put("y",4);
        symbolMap.put("z",5);


        initializeTable();

        //the tape is initially all blank symbols
        //Add a blank before and after the data.
        tape = new char[input.length+2];
        Arrays.fill(tape, '_');
        System.arraycopy(input, 0, tape, 0, input.length);
        
        head = 0;//The Read/Write head is at index 0
    }
    

    //parse the strings in transitions into a 2d array of Struct objects, representing transitions.
    private void initializeTable(){
        numberOfStates = transitions.length;
        numberOfSymbols = transitions[0].split(" ", 99).length;

        transitionTable = new Transition[numberOfStates][numberOfSymbols];
        for (int row = 0; row < numberOfStates; row++){
            String[] structStrings = transitions[row].split(" ", numberOfSymbols);
            for (int column = 0; column < numberOfSymbols; column++){
                String[] values = structStrings[column].substring(2).split(",", 3);
                if ( values.length == 1){
                   transitionTable[row][column] = null;
                }else{
                    transitionTable[row][column] = new Transition(values[0], values[1], values[2]);
                }
            }
        }

    }

    public void run(){
        input = Character.toString(tape[0]);

        //If the state is equal to the number of states, we have reached the accept state.
        while (state != numberOfStates){
            String startConfig = printConfiguration();
            Transition values = transitionTable[state][symbolMap.get(input)];

            //If no transition has been written for the current state and symbol, reject.
            if (values == null){
                System.out.println("REJECT");
                return;
            }

            //Update current state
            state = values.nextState;

            //Only write to the tape if head is in the allocated type. Otherwise you're just writing '_' on '_'.
                tape[head] = values.nextSymbol.charAt(0);

            //Move pointer
            head += values.moveRight ? 1 : -1;

            String direction = values.moveRight ? "right" : "left";
            System.out.println(String.format("Writing %s, moving %s, and transitioning to state %s after reading %s", values.nextSymbol, direction, state, input));

            input = Character.toString(tape[head]);

            System.out.println(startConfig + " -> " + printConfiguration());
            System.out.println("\n");
        }
        System.out.println("ACCEPT");
    }


    public String printConfiguration(){

        String inString = Arrays.toString(tape).substring(1, Arrays.toString(tape).length()-1).replace(",","").replace(" ","");
        String str = "";
        //Add input tape until where state should be printed
        if (head > 0) {
            str = inString.substring(0, Math.min(head, tape.length-1));
            if (head >= tape.length){
                str += "_".repeat(head-tape.length+1);
            }
        }
        //add state
        str += "[Q" + state + "]";

        //Add remaining tape symbols
        if (head >= 0 && head < tape.length){
            str += inString.substring(head);
        }
        return str;
    }

}

