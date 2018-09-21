import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class bchCodes_GUI {
    private JTextArea txtOutput;
    private JTextField txtGenerate;
    private JTextField txtDecode;
    private JButton btnGenerate;
    private JButton btnDecode;
    private JPanel bchPanel;
    private JLabel lblGenerate;
    private JLabel lblDecode;
    public int[] inputArray = new int[10];
    public String input;
    public String result;
    public int s1, s2, s3, s4, p, q, r, pos1, pos2, mag1, mag2;

    public bchCodes_GUI() {
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = txtGenerate.getText();

                txtOutput.setText("Input: " + input + "\n");
                if(input.length() == 6 && input.matches("[0-9]*")){
                    for(int i = 0; i < input.length(); i++){
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

                    if(Arrays.toString(inputArray).contains("10")){
                        txtOutput.append("Unusable Number.");
                    }else{
                        txtOutput.append("Encoded Number: " + Arrays.toString(inputArray).replaceAll("[, \\[\\]]", ""));
                    }
                }
            }
        });
        btnDecode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = txtDecode.getText();

                if (input.length() == 10 && input.matches("[0-9]*")) {
                    for (int i = 0; i < input.length(); i++) {
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
                    if (s1 == 0 && s2 == 0 && s3 == 0 && s4 == 0) {
                        txtOutput.setText("No Errors.");
                    } else { //ERROR DETECTED.
                        try {
                            p = mod11(s2 * s2 - s1 * s3);
                            q = mod11(s1 * s4 - s2 * s3);
                            r = mod11(s3 * s3 - s2 * s4);

                            //System.out.println(p + ", " + q + ", " + r);

                            // SINGLE ERROR
                            if (p == 0 && q == 0 && r == 0) {
                                pos1 = (int) mod11(s2 * inverse(s1));
                                mag1 = s1;

                                inputArray[pos1 - 1] = mod11(inputArray[pos1 - 1] - mag1);
                                txtOutput.setText("Single Error" +
                                        "\nPosition= " + pos1 +
                                        "\nMagnitude= " + mag1 +
                                        "\nOriginal= " + input +
                                        "\nCorrected= " +
                                        Arrays.toString(inputArray).replaceAll("[, \\[\\]]", ""));
                                //DOUBLE ERROR
                            } else {
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
                                if (pos1 == 0 || pos2 == 0) {
                                    throw new ArrayIndexOutOfBoundsException();
                                }

                                //correct based on positions and magnitudes.
                                inputArray[pos1 - 1] = mod11(inputArray[pos1 - 1] - mag1);
                                inputArray[pos2 - 1] = mod11(inputArray[pos2 - 1] - mag2);

                                //if any position is corrected to ten, then there is a double error.
                                if (Arrays.toString(inputArray).contains("10")) {
                                    throw new ArrayIndexOutOfBoundsException();
                                }

                                txtOutput.setText("Double Error Detected." +
                                        "\nPosition 1: " + pos1 +
                                        "\nMagnitude: " + mag1 +
                                        "\nPosition 2: " + pos2 +
                                        "\nMagnitude: " + mag2 +
                                        "\nOriginal: " + input +
                                        "\nCorrected: " + Arrays.toString(inputArray)
                                        .replaceAll("[, \\[\\]]", ""));
                            }
                        } catch (AssertionError | IndexOutOfBoundsException error) {
                            txtOutput.setText("More than two errors detected.");
                        }
                    }
                }
            }
        });
    }
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
    static int sqrt(int i) {
        switch (i) {
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("bchCodes");
        frame.setContentPane(new bchCodes_GUI().bchPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}


