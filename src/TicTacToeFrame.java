import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class TicTacToeFrame extends JFrame {
    JPanel mainPnl;
    JPanel titlePnl;  // Top

    JPanel boardPnl;
    private JButton ticTacToeButtons[] =  new JButton[9];
    private ButtonStatus buttonStatus[] = new ButtonStatus[9];

    JLabel titleLbl;

    enum ButtonStatus {
        STATUS_UNSET,
        STATUS_X,
        STATUS_O
    }

    Boolean isMoveValid(int changeIndex) {
        if(buttonStatus[changeIndex] == ButtonStatus.STATUS_UNSET) {
            return true;
        }
        return false;
    }

    void SimulateComputerMove() {
        Random r = new Random();
        int move = 0;
        do {
            move = r.nextInt(9);
        } while (!isMoveValid(move));
        AttemptBoardChange(move, ButtonStatus.STATUS_O);
    }

    ButtonStatus AreColumnsSameValue() {
        for(int z = 0; z < 3; z++) {
            ButtonStatus firstStatus = buttonStatus[z];
            for (int i = z + 3; i < z + 9; i += 3) {
                if (firstStatus != buttonStatus[i]) {
                    firstStatus = ButtonStatus.STATUS_UNSET;
                    break;
                }
            }

            if(firstStatus != ButtonStatus.STATUS_UNSET) {
                return firstStatus;
            }

        }
        return ButtonStatus.STATUS_UNSET;
    }

    ButtonStatus AreRowsSameValue() {
        for(int z = 0; z < 3; z++) {
            ButtonStatus firstStatus = buttonStatus[(z * 3)];

            for (int i = (z * 3) + 1; i < (z * 3) + 3; i++) {
                if (firstStatus != buttonStatus[i]) {
                    firstStatus = ButtonStatus.STATUS_UNSET;
                    break;
                }
            }

            if(firstStatus != ButtonStatus.STATUS_UNSET) {
                return firstStatus;
            }
        }
        return ButtonStatus.STATUS_UNSET;
    }

    ButtonStatus AreDiagonalsSameValue() {
        ButtonStatus firstStatus = buttonStatus[0];
        if(firstStatus == buttonStatus[4] && firstStatus == buttonStatus[8]) {
            return firstStatus;
        }
        firstStatus = buttonStatus[2];
        if(firstStatus == buttonStatus[4] && firstStatus == buttonStatus[6]) {
            return firstStatus;
        }
        return ButtonStatus.STATUS_UNSET;
    }

    void DisplayWinner(ButtonStatus status) {
        JOptionPane jOptionPane = new JOptionPane();
        String winner = status == ButtonStatus.STATUS_X ? "Player " : "Computer ";
        winner += "wins! Would you like to play again?";
        int value = JOptionPane.showOptionDialog(null, winner, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if(value != 0) {
            System.exit(0);
        }
    }

    Boolean IsGameComplete() {
        ButtonStatus winningStatus = ButtonStatus.STATUS_UNSET;
        if((winningStatus = AreRowsSameValue()) != ButtonStatus.STATUS_UNSET ) {
            DisplayWinner(winningStatus);
            return true;
        } else if((winningStatus = AreColumnsSameValue()) != ButtonStatus.STATUS_UNSET) {
            DisplayWinner(winningStatus);
            return true;
        } else if((winningStatus = AreDiagonalsSameValue()) != ButtonStatus.STATUS_UNSET) {
            DisplayWinner(winningStatus);
            return true;
        }

        int numSet = 0;
        for(int i = 0; i < 9; i++) {
            if(buttonStatus[i] != ButtonStatus.STATUS_UNSET) {
                numSet++;
            }
        }

        if(numSet == 9) {
            JOptionPane jOptionPane = new JOptionPane();
            String winner = "Result is a draw. Would you like to play again?";
            int value = JOptionPane.showOptionDialog(null, winner, "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(value != 0) {
                System.exit(0);
            }
            return true;
        }
        return false;
    }

    void ResetBoard() {
        for(int i = 0; i < 9; i++) {
            ticTacToeButtons[i].setText("");
            buttonStatus[i] = ButtonStatus.STATUS_UNSET;
        }
    }

    Boolean AttemptBoardChange(int changeIndex, ButtonStatus status) {
        if(isMoveValid(changeIndex)) {
            ticTacToeButtons[changeIndex].setText(status == ButtonStatus.STATUS_X ? "X" : "O");
            buttonStatus[changeIndex] = status;
            if(IsGameComplete()) {
                //Reset board
                ResetBoard();
            }
            return true;
        }
        return false;
    }

    public TicTacToeFrame()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createTitlePanel();
        createBoard();

        mainPnl.add(titlePnl,BorderLayout.NORTH);
        mainPnl.add(boardPnl, BorderLayout.CENTER);

        add(mainPnl);
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ResetBoard();
        setVisible(true);
    }

    private void createTitlePanel()
    {
        titlePnl = new JPanel();;
        titleLbl = new JLabel("Tic Tac Toe Game", JLabel.CENTER);
        titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);
        titlePnl.add(titleLbl);
    }

    private void createBoard()
    {
        boardPnl = new JPanel();
        boardPnl.setLayout(new GridLayout(3, 2));
        for(int i = 0; i < 9; i++) {
            JButton newButton = new JButton();
            int finalI = i;
            newButton.addActionListener((ActionEvent ae) -> {
                if(!AttemptBoardChange(finalI, ButtonStatus.STATUS_X)) {
                    JOptionPane.showMessageDialog(null, "Invalid tile selected");
                } else {
                    SimulateComputerMove();
                }
            });
            newButton.setSize(20, 20);
            ticTacToeButtons[i] = newButton;
            boardPnl.add(newButton);
        }
    }

}