package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static minesweeper.Calculations.CALCULATIONS;
import static minesweeper.Timer.TIMER;

public class Interface extends JFrame {

    public JButton[][] blocks;
    public ImageIcon[] icons = new ImageIcon[14];

    private JPanel topPanel = new JPanel();
    private JPanel mineFieldPanel = new JPanel();

    public JTextField flags, timerBoard;
    public JButton newGameButton = new JButton();

    private boolean check = true;
    private boolean startTime = false;

    public MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (check == true) {
                    for (int i = 0; i < CALCULATIONS.fieldRows; i++) {
                        for (int j = 0; j < CALCULATIONS.fieldColumns; j++) {
                            if (e.getSource() == blocks[i][j]) {
                                CALCULATIONS.flagRow = i;
                                CALCULATIONS.flagColumn = j;
                                i = CALCULATIONS.fieldRows;
                                break;
                            }
                        }
                    }
                    CALCULATIONS.setMines();
                    CALCULATIONS.calculation();
                    check = false;
                }
                CALCULATIONS.showValue(e);
                CALCULATIONS.winner();

                if (startTime == false) {
                    TIMER.Start();
                    startTime = true;
                }
            }
        };

    public Interface() {
        super("Сапёр");
        setLocation(400, 300);
        setIcons();
        setPanel(1, 0, 0, 0);

        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    TIMER.stop();
                    setPanel(1, 10, 10, 10);
                } catch (Exception ex) {
                    setPanel(1, 10, 10, 10);
                }
                reset();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setPanel(int level, int setRows, int setColumns, int setMines) {
        switch (level) {
            case 1:
                CALCULATIONS.minesFieldWidth = 200;
                CALCULATIONS.minesFieldHeight = 300;
                CALCULATIONS.fieldRows = 10;
                CALCULATIONS.fieldColumns = 10;
                CALCULATIONS.minesQuantity = 10;
                break;
            default:
                break;
        }

        CALCULATIONS.flagsFieldRows = CALCULATIONS.fieldRows;
        CALCULATIONS.flagsFieldColumns = CALCULATIONS.fieldColumns;
        CALCULATIONS.flagsQuantity = CALCULATIONS.minesQuantity;

        setSize(CALCULATIONS.minesFieldWidth, CALCULATIONS.minesFieldHeight);
        setResizable(false);

        blocks = new JButton[CALCULATIONS.fieldRows][CALCULATIONS.fieldColumns];

        getContentPane().removeAll();
        mineFieldPanel.removeAll();

        flags = new JTextField("" + CALCULATIONS.minesQuantity, 3);
        flags.setEditable(false);
        flags.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25));
        flags.setBackground(Color.BLACK);
        flags.setForeground(Color.RED);
        flags.setBorder(BorderFactory.createLoweredBevelBorder());
        
        timerBoard = new JTextField("000", 3);
        timerBoard.setEditable(false);
        timerBoard.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25));
        timerBoard.setBackground(Color.BLACK);
        timerBoard.setForeground(Color.RED);
        timerBoard.setBorder(BorderFactory.createLoweredBevelBorder());
        
        newGameButton.setIcon(icons[11]);
        newGameButton.setBorder(BorderFactory.createLoweredBevelBorder());

        topPanel.removeAll();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(flags, BorderLayout.WEST);
        topPanel.add(newGameButton, BorderLayout.CENTER);
        topPanel.add(timerBoard, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        mineFieldPanel.setPreferredSize(new Dimension(CALCULATIONS.minesFieldWidth, CALCULATIONS.minesFieldHeight));
        mineFieldPanel.setLayout(new GridLayout(0, CALCULATIONS.fieldColumns));

        for (int i = 0; i < CALCULATIONS.fieldRows; i++) {
            for (int j = 0; j < CALCULATIONS.fieldColumns; j++) {
                blocks[i][j] = new JButton("");
                blocks[i][j].addMouseListener(ma);
                mineFieldPanel.add(blocks[i][j]);
            }
        }
        reset();
        mineFieldPanel.revalidate();
        mineFieldPanel.repaint();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().repaint();
        getContentPane().add(mineFieldPanel, BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    public void reset() {
        check = true;
        startTime = false;
        for (int i = 0; i < CALCULATIONS.fieldRows; i++) {
            for (int j = 0; j < CALCULATIONS.fieldColumns; j++) {
                CALCULATIONS.color[i][j] = 'w';
            }
        }
    }

    void displayElapsedTime(long elapsedTime) {
        if (elapsedTime >= 0 && elapsedTime < 9) {
            timerBoard.setText("00" + elapsedTime);
        } else if (elapsedTime > 9 && elapsedTime < 99) {
            timerBoard.setText("0" + elapsedTime);
        } else if (elapsedTime > 99 && elapsedTime < 999) {
            timerBoard.setText("" + elapsedTime);
        }
    }
    
    public void setIcons() {
        String name;
        for (int i = 0; i <= 8; i++) {
            name = i + ".gif";
            icons[i] = new ImageIcon(name);
        }
        icons[9] = new ImageIcon("mine.gif");
        icons[10] = new ImageIcon("flag.gif");
        icons[11] = new ImageIcon("new game.gif");
        icons[12] = new ImageIcon("crape.gif");
    }
}