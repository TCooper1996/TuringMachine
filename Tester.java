
/**
 *
 * @author Tayler Cooper
 * @version 4/20/2020
 */
public class Tester
{
    public static void main(String...args){
        java.util.Scanner scan = new java.util.Scanner(System.in);
        String s = "0";
        boolean inputValid;
        while (!s.isBlank()){
            System.out.println("What is the initial input string? \n Enter a blank to abort.");
            s = scan.nextLine();
            inputValid = true;

            for (int i = 0; i < s.length(); i ++){
                if (s.charAt(i) != '0' && s.charAt(i) != '1'){
                    System.out.println("Invalid input string. Languages requires just 0s and 1s.");
                    inputValid = false;
                    break;
                }
            }

            if (inputValid){
                TM machine = new TM(s.toCharArray());
                machine.run();
            }

        }
    }
}
