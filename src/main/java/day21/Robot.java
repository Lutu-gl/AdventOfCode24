package day21;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    int positionI;
    int positionJ;
    char[][] keypad;
    int notAllowedI = 0;
    int notAllowedJ = 0;

    public Robot(int positionI, int positionJ, char[][] keypad) {
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.keypad = keypad;
        for (int i = 0; i < keypad.length; i++) {
            for (int j = 0; j < keypad[0].length; j++) {
                if (keypad[i][j] == '-') {
                    notAllowedI = i;
                    notAllowedJ = j;
                }
            }
        }

    }

    // moving is done with >, <, v, and ^
    // I cant move through '-'
    public List<Character> getSequenceTo(Character c) {
        int targetI = -1;
        int targetJ = -1;

        for (int i = 0; i < keypad.length; i++) {
            for (int j = 0; j < keypad[0].length; j++) {
                if (keypad[i][j] == c) {
                    targetI = i;
                    targetJ = j;
                    break;
                }
            }
        }



        int moveJ = targetJ - positionJ;
        int moveI = targetI - positionI;


        List<Character> moveSequence = new ArrayList<>();
        if(notAllowedI == 3 && notAllowedJ == 0) {
            if (moveI < 0) {
                while (moveI != 0) {
                    if (moveI > 0) {
                        moveSequence.add('v');
                        moveI--;
                    } else {
                        moveSequence.add('^');
                        moveI++;
                    }
                }
                while (moveJ != 0) {
                    if (moveJ > 0) {
                        moveSequence.add('>');
                        moveJ--;
                    } else {
                        moveSequence.add('<');
                        moveJ++;
                    }
                }
            } else {
                while (moveJ != 0) {
                    if (moveJ > 0) {
                        moveSequence.add('>');
                        moveJ--;
                    } else {
                        moveSequence.add('<');
                        moveJ++;
                    }
                }
                while (moveI != 0) {
                    if (moveI > 0) {
                        moveSequence.add('v');
                        moveI--;
                    } else {
                        moveSequence.add('^');
                        moveI++;
                    }
                }
            }
        }

        if (notAllowedI == 0 && notAllowedJ == 0) {
            if (moveI < 0) {
                while (moveJ != 0) {
                    if (moveJ > 0) {
                        moveSequence.add('>');
                        moveJ--;
                    } else {
                        moveSequence.add('<');
                        moveJ++;
                    }
                }
                while (moveI != 0) {
                    if (moveI > 0) {
                        moveSequence.add('v');
                        moveI--;
                    } else {
                        moveSequence.add('^');
                        moveI++;
                    }
                }
            } else {
                while (moveI != 0) {
                    if (moveI > 0) {
                        moveSequence.add('v');
                        moveI--;
                    } else {
                        moveSequence.add('^');
                        moveI++;
                    }
                }
                while (moveJ != 0) {
                    if (moveJ > 0) {
                        moveSequence.add('>');
                        moveJ--;
                    } else {
                        moveSequence.add('<');
                        moveJ++;
                    }
                }
            }
        }

        positionI = targetI;
        positionJ = targetJ;
        return moveSequence;
    }
}