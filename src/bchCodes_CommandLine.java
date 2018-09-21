/*
Author: Jacob John Williams
Function: Encodes and error corrects bch codes in the command line.
Date Created: 11 / 10 / 2017
Date Modified: 06 / 05 / 2018
                Modified to make program more streamlined.
                Intention to upload to github for use by others learning cryptography.
Notes: It is worth knowing the BCH formulas and processes before looking through this, alot of this will be much easier
        to understand when you how to do the maths yourself.
 */
import java.util.Arrays;
import java.util.Scanner;
public class bchCodes_CommandLine {
    public static void main(String[] args) {
        int mode = 0; //1 == encode //2 == error correction
        Scanner sc = new Scanner(System.in);
        int[] inputArray = new int[10];
        String input;
        String result;
        int s1, s2, s3, s4, p, q, r, pos1, pos2, mag1, mag2;

        while(true) {
            while (true) {
                System.out.println("\nEnter:\n'1' to encode\n'2' to error correct\n'3' to exit.");
                mode = sc.nextInt();
                if (mode == 1 || mode == 2) {
                    break;
                }else if(mode == 3){
                    System.exit(0);
                }
            }

            //ENCODE MODE
            if (mode == 1) {
                while (true) {
                    System.out.println("\nPlease enter a six digit number:");
                    input = sc.next();

                    //check and parse input.
                    if (input.length() == 6 && input.matches("[0-9]*")) {
                        for (int i = 0; i < input.length(); i++) {
                            inputArray[i] = Integer.parseInt(String.valueOf(input.charAt(i)));
                        }

                        //calculate the "padding"
                        inputArray[6] = ((4 * inputArray[0]) + (10 * inputArray[1]) + (9 * inputArray[2]) + (2 * inputArray[3])
                                + (inputArray[4]) + (7 * inputArray[5])) % 11;
                        inputArray[7] = ((7 * inputArray[0]) + (8 * inputArray[1]) + (7 * inputArray[2]) + (inputArray[3])
                                + (9 * inputArray[4]) + (6 * inputArray[5])) % 11;
                        inputArray[8] = ((9 * inputArray[0]) + (inputArray[1]) + (7 * inputArray[2]) + (8 * inputArray[3])
                                + (7 * inputArray[4]) + (7 * inputArray[5])) % 11;
                        inputArray[9] = ((inputArray[0]) + (2 * inputArray[1]) + (9 * inputArray[2]) + (10 * inputArray[3])
                                + (4 * inputArray[4]) + (inputArray[5])) % 11;

                        //determine output.
                        if (Arrays.toString(inputArray).contains("10")) {
                            System.out.println("\nUnusable Number.");
                        } else {
                            System.out.println("\nEncoded Number: "
                                    + (Arrays.toString(inputArray).replaceAll("[, \\[\\]]", "")));
                            break;
                        }
                    } else {
                        System.out.println("\nInvalid Input.");
                    }
                }
            //DECODE MODE
            } else if (mode == 2) {
                /*
                    THIS IS WHERE THE FUN BEGINS.
                */
                while (true) {
                    //input prompt
                    System.out.println("\nPlease enter the encoded number.");
                    input = sc.next();

                    //check input, if correct format, parse into array.
                    if(input.length() == 10 && input.matches("[0-9]*")){
                        for(int i = 0; i < input.length(); i++){
                            inputArray[i] = Integer.parseInt(String.valueOf(input.charAt(i)));
                        }

                        //Calculate the four signs based on the inputted value.
                        s1 = (inputArray[0] + inputArray[1] + inputArray[2] + inputArray[3] + inputArray[4]
                                + inputArray[5] + inputArray[6] + inputArray[7] + inputArray[8] + inputArray[9]) % 11;
                        s2 = (inputArray[0] + 2 * inputArray[1] + 3 * inputArray[2] + 4 * inputArray[3]
                                + 5 * inputArray[4] + 6 * inputArray[5] + 7 * inputArray[6] + 8 * inputArray[7]
                                + 9 * inputArray[8] + 10 * inputArray[9]) % 11;
                        s3 = (inputArray[0] + 4 * inputArray[1] + 9 * inputArray[2] + 5 * inputArray[3]
                                + 3 * inputArray[4] + 3 * inputArray[5] + 5 * inputArray[6] + 9 * inputArray[7]
                                + 4 * inputArray[8] + inputArray[9]) % 11;
                        s4 = (inputArray[0] + 8 * inputArray[1] + 5 * inputArray[2] + 9 * inputArray[3]
                                + 4 * inputArray[4] + 7 * inputArray[5] + 2 * inputArray[6] + 6 * inputArray[7]
                                + 3 * inputArray[8] + 10 * inputArray[9]) % 11;

                        //if the signs result in zero there is no error.
                        if(s1 == 0 && s2 == 0 && s3 == 0 && s4 == 0){
                            System.out.println("\n No Errors.");
                        } else { //ERROR DETECTED.
                            try{
                                p = mod11(s2 * s2 - s1 * s3);
                                q = mod11(s1 * s4 - s2 * s3);
                                r = mod11(s3 * s3 - s2 * s4);

                                //System.out.println(p + ", " + q + ", " + r);

                                // SINGLE ERROR
                                if(p == 0 && q == 0 && r ==0){
                                    pos1 = (int) mod11(s2 * inverse(s1));
                                    mag1 = s1;

                                    inputArray[pos1 - 1] = mod11(inputArray[pos1 - 1] - mag1);
                                    System.out.println("\nSingle Error" +
                                            "\nPosition= " + pos1 +
                                            "\nMagnitude= " + mag1 +
                                            "\nOriginal= " + input +
                                            "\nCorrected= " +
                                            Arrays.toString(inputArray).replaceAll("[, \\[\\]]", ""));
                                    break;
                                //DOUBLE ERROR
                                }else{
                                    //error one position calculation
                                    pos1 = sqrt(mod11((q * q) - (4 * p * r)));
                                    pos1 = (mod11(-q + pos1) * inverse(2 * p)) % 11;

                                    //error two position calculation.
                                    pos2 = sqrt(mod11((q * q) - (4 * p * r)));
                                    pos2 = (mod11(-q - pos2) * inverse(2 * p)) % 11;

                                    //error magnitude calculations.
                                    mag2 = mod11(mod11(pos1 * s1 - s2) * inverse(pos1 - pos2));
                                    mag1 = mod11(s1 - mag2);

                                    //System.out.println(pos1 + ", " + pos2 + ", " + mag1 + ", " + mag2);

                                    //if either position is zero then there is a double error.
                                    if(pos1 == 0 || pos2 == 0){
                                        throw new ArrayIndexOutOfBoundsException();
                                    }

                                    //correct based on positions and magnitudes.
                                    inputArray[pos1 - 1] = mod11(inputArray[pos1 - 1] - mag1);
                                    inputArray[pos2 - 1] = mod11(inputArray[pos2 - 1] - mag2);

                                    //if any position is corrected to ten, then there is a double error.
                                    if(Arrays.toString(inputArray).contains("10")){
                                        throw new ArrayIndexOutOfBoundsException();
                                    }

                                    System.out.println("\nDouble Error Detected." +
                                            "\nPosition 1: " + pos1 +
                                            "\nMagnitude: " + mag1 +
                                            "\nPosition 2: " + pos2 +
                                            "\nMagnitude: " + mag2 +
                                            "\nOriginal: " + input +
                                            "\nCorrected: " + Arrays.toString(inputArray)
                                                                .replaceAll("[, \\[\\]]", ""));
                                    break;
                                }
                            } catch(AssertionError | IndexOutOfBoundsException e){
                                System.out.println("\n More than two errors detected.");
                                break;
                            }
                        }
                    }else{
                        System.out.println("\n Invalid Input.");
                    }
                }
            }
        }
    }

    /*
    Name: mod11
    function: performs a mod function under the basis of base 11.
    Inputs: int i - single number value.
    Outputs: input modded by 11 under different format.
    Notes: typical mod11 calculation doesnt associate for working in base 11, therefore a unique function is needed to
            associate for such.
     */
    static int mod11(int i){
        return  ((i % 11) + 11) % 11;
    }

    static int inverse(int i){
        switch (mod11(i)){
            case 1:
                return 1;
            case 2:
                return 6;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 9;
            case 6:
                return 2;
            case 7:
                return 8;
            case 8:
                return 7;
            case 9:
                return 5;
            case 10:
                return 10;
        }
        return 0;
    }

    /*
    Name: sqrt
    Function: performs a square root function under base 11.
    Inputs: int i - singular number.
    Outputs: square root of input under base 11.
    Notes: Usual square root function doesnt work for base 11, so a custom one was needed.
     */
    static int sqrt(int i){
        switch(i){
            case 1:
                return 1;
            case 3:
                return 5;
            case 4:
                return 2;
            case 5:
                return 4;
            case 9:
                return 3;
            default:
                throw new AssertionError();
        }
    }
}
