package day17;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ChronospatialComputer {
    private long registerA = 0;
    private long registerB = 0;
    private long registerC = 0;

    private long instructionPointer = 0;

    List<Long> instructions;

    List<Long> output;

    // for task 2
    List<Long> searchedOutput;

    public ChronospatialComputer (long A, long B, long C, List<Long> instructions) {
        this(A, B, C, instructions, null);
    }

    public ChronospatialComputer (long A, long B, long C, List<Long> instructions, List<Long> searchedOutput) {
        this.registerA = A;
        this.registerB = B;
        this.registerC = C;
        this.instructions = instructions;
        this.output = new ArrayList<>();
        if(searchedOutput != null) {
            this.searchedOutput = searchedOutput;
        }
    }

    public void run() {
        while (instructionPointer < instructions.size()) {
            // for part2
            if (searchedOutput != null) {
                if(output.size() > searchedOutput.size()) {
                    System.out.println("No match found");
                    return;
                }
            }
            if (instructionPointer + 1 >= instructions.size()) break; // Verhindere OutOfBoundsException
            long instruction = instructions.get((int) instructionPointer);
            long operant = instructions.get((int) (instructionPointer + 1));

            //System.out.println("Instruction: " + instruction + ", Operant: " + operant + ", Pointer: " + instructionPointer);

            switch ((int) instruction) {
                case 0:
                    opCode0(operant);
                    break;
                case 1:
                    opCode1(operant);
                    break;
                case 2:
                    opCode2(operant);
                    break;
                case 3:
                    if (!opCode3(operant)) instructionPointer += 2;
                    continue; // Verhindert zusätzliches Increment
                case 4:
                    opCode4();
                    break;
                case 5:
                    opCode5(operant);
                    break;
                case 6:
                    opCode6(operant);
                    break;
                case 7:
                    opCode7(operant);
                    break;
                default:
                    System.err.println("Ungültiger Opcode: " + instruction);
                    return; // Abbruch bei unbekanntem Opcode
            }
            instructionPointer += 2; // Standardmäßig weiter
            //System.out.println("Registers: A=" + registerA + ", B=" + registerB + ", C=" + registerC);
        }
    }


    public void opCode0(long num) {
        long divisor = (long) Math.pow(2, getComboOperant(num));
        if (divisor != 0) {
            registerA /= divisor;
        } else {
            System.err.println("Division by zero");
        }
    }


    public void opCode1 (long num) {
        registerB = registerB ^ num;
    }
    public void opCode2 (long num) {
        registerB = getComboOperant(num) % 8;
    }
    public boolean opCode3 (long num) {
        if (registerA == 0) return false;
        instructionPointer = num;
        return true;
    }
    public void opCode4 () {
        registerB = registerB ^ registerC;
    }
    public void opCode5 (long num) {
        output.add(getComboOperant(num) % 8);
    }
    public void opCode6 (long num) {
        registerB = (long) (registerA / Math.pow(2, getComboOperant(num)));
    }
    public void opCode7 (long num) {
        registerC = (long) (registerA / Math.pow(2, getComboOperant(num)));
    }

    public long getComboOperant(long i) {
        if (i >= 0 && i <= 3) return i;
        if (i == 4) return registerA;
        if (i == 5) return registerB;
        if (i == 6) return registerC;

        System.err.println("Ungültiger Combo-Operand: " + i);

        return -1;
    }


    public String getOutputSeperatedByComma() {
        StringBuilder sb = new StringBuilder();
        for (long i = 0; i < output.size(); i++) {
            sb.append(output.get((int) i));
            if (i != output.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public long getRegisterA() {
        return registerA;
    }

    public void setRegisterA(long registerA) {
        this.registerA = registerA;
    }

    public long getRegisterB() {
        return registerB;
    }

    public void setRegisterB(long registerB) {
        this.registerB = registerB;
    }

    public long getRegisterC() {
        return registerC;
    }

    public void setRegisterC(long registerC) {
        this.registerC = registerC;
    }

    public List<Long> getOutput() {
        return output;
    }
}
