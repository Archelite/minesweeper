package minesweeper;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JOptionPane;
import static minesweeper.StartGame.INTERFACE;
import static minesweeper.Timer.TIMER;

public class Calculations {
    
    public static Calculations CALCULATIONS = new Calculations();
    
    public int minesFieldWidth, minesFieldHeight, flagRow, flagColumn, minesQuantity, flagsQuantity = 0, level = 1,
            flagsFieldRows, flagsFieldColumns = 10;
    
    private int[] r = {-1, -1, -1, 0, 1, 1, 1, 0};
    private int[] c = {-1, 0, 1, 1, 1, 0, -1, -1};
    
    int fieldRows = 10;
    int fieldColumns = 10;
    int[][] countmine = new int[fieldRows][fieldColumns];
    int[][] color = new int[fieldRows][fieldColumns];
        
    private Random random = new Random();
    
    public void winner() {
        int q = 0;
        for (int k = 0; k < fieldRows; k++) {
            for (int l = 0; l < fieldColumns; l++) {
                if (color[k][l] == 'w') {
                    q = 1;
                }
            }
        }

        if (q == 0) {
            for (int k = 0; k < fieldRows; k++) {
                for (int l = 0; l < fieldColumns; l++) {
                    INTERFACE.blocks[k][l].removeMouseListener(INTERFACE.ma);
                }
            }
            TIMER.stop();
            JOptionPane.showMessageDialog(INTERFACE, "Вы выиграли !");
        }
    }

    public void showValue(MouseEvent e) {
        for (int i = 0; i < fieldRows; i++) {
            OUTER:
            for (int j = 0; j < fieldColumns; j++) {
                if (e.getSource() == INTERFACE.blocks[i][j]) {
                    if (e.isMetaDown() == false) {
                        if (INTERFACE.blocks[i][j].getIcon() == INTERFACE.icons[10]) {
                            if (flagsQuantity < minesQuantity) {
                                flagsQuantity++;
                            }
                            INTERFACE.flags.setText("" + flagsQuantity);
                        }
                        switch (countmine[i][j]) {
                            case -1:
                                for (int k = 0; k < fieldRows; k++) {
                                    for (int l = 0; l < fieldColumns; l++) {
                                        if (countmine[k][l] == -1) {
                                            INTERFACE.blocks[k][l].setIcon(INTERFACE.icons[9]);
                                            INTERFACE.blocks[k][l].removeMouseListener(INTERFACE.ma);
                                        }
                                        INTERFACE.blocks[k][l].removeMouseListener(INTERFACE.ma);
                                    }
                                }   
                                TIMER.stop();
                                INTERFACE.newGameButton.setIcon(INTERFACE.icons[12]);
                                JOptionPane.showMessageDialog(INTERFACE, "Вы проиграли");
                                break;
                            case 0:
                                fieldRefresh(i, j);
                                break;
                            default:
                                INTERFACE.blocks[i][j].setIcon(INTERFACE.icons[countmine[i][j]]);
                                color[i][j] = 'b';
                                break OUTER;
                        }
                    } else {
                        if (flagsQuantity != 0) {
                            if (INTERFACE.blocks[i][j].getIcon() == null) {
                                flagsQuantity--;
                                INTERFACE.blocks[i][j].setIcon(INTERFACE.icons[10]);
                            }
                            INTERFACE.flags.setText("" + flagsQuantity);
                        }
                    }
                }
            }
        }
    }

    public void calculation() {
        int row, column;
        for (int i = 0; i < fieldRows; i++) {
            for (int j = 0; j < fieldColumns; j++) {
                int value = 0;
                int R, C;
                row = i;
                column = j;
                if (countmine[row][column] != -1) {
                    for (int k = 0; k < 8; k++) {
                        R = row + r[k];
                        C = column + c[k];

                        if (R >= 0 && C >= 0 && R < fieldRows && C < fieldColumns) {
                            if (countmine[R][C] == -1) {
                                value++;
                            }
                        }
                    }
                    countmine[row][column] = value;
                }
            }
        }
    }

    public void fieldRefresh(int row, int column) {
        int R, C;
        color[row][column] = 'b';
        INTERFACE.blocks[row][column].setBackground(Color.GRAY);
        INTERFACE.blocks[row][column].setIcon(INTERFACE.icons[countmine[row][column]]);
        for (int i = 0; i < 8; i++) {
            R = row + r[i];
            C = column + c[i];
            if (R >= 0 && R < fieldRows && C >= 0 && C < fieldColumns && color[R][C] == 'w') {
                if (countmine[R][C] == 0) {
                    fieldRefresh(R, C);
                } else {
                    INTERFACE.blocks[R][C].setIcon(INTERFACE.icons[countmine[R][C]]);
                    color[R][C] = 'b';
                }
            }
        }
    }

    public void setMines() {
        int row = 0, column = 0;
        Boolean[][] flag = new Boolean[fieldRows][fieldColumns];

        for (int i = 0; i < fieldRows; i++) {
            for (int j = 0; j < fieldColumns; j++) {
                flag[i][j] = true;
                countmine[i][j] = 0;
            }
        }

        flag[flagRow][flagColumn] = false;
        color[flagRow][flagColumn] = 'b';

        for (int i = 0; i < minesQuantity; i++) {
            row = random.nextInt(fieldRows);
            column = random.nextInt(fieldColumns);

            if (flag[row][column] == true) {
                countmine[row][column] = -1;
                color[row][column] = 'b';
                flag[row][column] = false;
            } else {
                i--;
            }
        }
    }  
}
