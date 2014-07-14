import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeGUI extends JFrame {
	private JButton[][] buttons = new JButton[3][3]; //JButtons for each square
	private Players whosTurn = Players.X; //Holds whos turn it is
	private String winner = "N/A"; //Holds the winner
	private int squaresRevealed = 0; //Counts how many squares have been clicked
	private String[] optionsEndOfGame = {"Play again", "Exit"}; //Options for end of game
	private HashMap<String, Integer> scoreBoard = new HashMap<>(); //Scoreboard
	
	private JLabel xScore, oScore, tieScore, whosTurnLabel; //Information for user
	
	/**
	 * Starts the JVM
	 * @param args Does nothing
	 */
	public static void main(String[] args) {
		new TicTacToeGUI();
	}
	/**
	 * Builds the GUI
	 */
	public TicTacToeGUI() {
		//Initialize the scoreBoard
		scoreBoard.put("X", 0);
		scoreBoard.put("O", 0);
		scoreBoard.put("Tie", 0);
		
		//Build & display GUI
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(playingField());
		add(infoPanel(), BorderLayout.SOUTH);
		setTitle("Tic Tac Toe");
		setSize(500, 500);
		setVisible(true);
	}
	/**
	 * Starts a new game by enabling all JButtons,
	 * clearing the last game, and setting squaresRevealed to 0
	 */
	public void newGame() {
		squaresRevealed = 0;
		
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setText("");
				buttons[i][j].setEnabled(true);
			}
		}
	}
	/**
	 * Updates the score part of the information panel for users
	 */
	private void updateScorePanel(){
		xScore.setText(" X wins: "+scoreBoard.get("X")+" |");
		oScore.setText(" O wins: "+scoreBoard.get("O")+" |");
		tieScore.setText(" Ties: "+scoreBoard.get("Tie")+" | ");
	}
	/**
	 * Updates who's turn it is on the information panel
	 */
	private void updateWhosTurn(){
		whosTurnLabel.setText(whosTurn+"'s turn");
	}
	/**
	 * Creates an information panel for the user
	 * @return the information panel
	 */
	private JPanel infoPanel(){
		JPanel p = new JPanel();
		xScore = new JLabel(" X wins: 0 |");
		oScore = new JLabel(" O wins: 0 |");
		tieScore = new JLabel(" Ties: 0 | ");
		whosTurnLabel = new JLabel(whosTurn+"'s turn");
		
		p.add(xScore);
		p.add(oScore);
		p.add(tieScore);
		p.add(whosTurnLabel);
		return p;
	}
	/**
	 * Creates the playing field for users
	 * @return the 3x3 playing grid
	 */
	private JPanel playingField() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 3));

		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				final int row = i, column = j;
				buttons[i][j] = new JButton();
				buttons[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!buttons[row][column].getText().equals("X")
								&& !buttons[row][column].getText().equals("O")) {
							buttons[row][column].setText(whosTurn.toString());
							squaresRevealed++;
							if(isWon(row, column)){ //Checks if player won
								winner = whosTurn.toString();
								endGame();
							}else if(noMoreMoves()){ //Checks if out of moves
								winner = "Tie";
								endGame();
							}else{ //Continue game
								switchTurns();
								updateWhosTurn();
							}
						}
					}
				});
				p.add(buttons[i][j]);
			}
		}
		return p;
	}
	
	/**
	 * Ends the game by disabling all buttons
	 * NOTE: Leaves each X and O mark in chosen boxes
	 */
	public void endGame(){
		
		//Disable all buttons
		for(int i=0; i<buttons.length; i++){
			for(int j=0; j<buttons[i].length; j++){
				buttons[i][j].setEnabled(false);
			}
		}
		
		scoreBoard.put(winner, scoreBoard.get(winner)+1);
		
		updateScorePanel();
		
		int userInput = JOptionPane.showOptionDialog(this, winner+" wins!", "Game over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionsEndOfGame, 0);
		
		if(userInput == 1){ //Displays the scores a final time and then exits the program
			displayScores();
			System.exit(0);
		}
		
		newGame();
	}
	/**
	 * Displays scores in a message dialog box
	 */
	public void displayScores(){
		JOptionPane.showMessageDialog(this,
				"X: "+scoreBoard.get("X")
				+"\nO: "+scoreBoard.get("O")
				+"\nTies: "+scoreBoard.get("Tie")
				, "Final scores", JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * Checks if the last move won the game
	 * @param x last clicked box x coordinate
	 * @param y last clicked box y coordinate
	 * @return if the game has been won
	 */
	private boolean isWon(int x, int y) {
		boolean won = true;
		
		// Check horizontal wins
		for (int i = 0; i < buttons[x].length; i++) {
			if (!buttons[x][i].getText().equals(whosTurn.toString())) {
				won = false;
				break;
			}
		}

		if (won == true)
			return true;
		won = true;
		
		// Check vertical wins
		for (int i = 0; i < buttons.length; i++) {
			if (!buttons[i][y].getText().equals(whosTurn.toString())) {
				won = false;
				break;
			}
		}

		if (won == true)
			return true;

		// Check for diagonal win
		if (buttons[1][1].getText().equals(whosTurn.toString())) {
			if (buttons[0][0].getText().equals(whosTurn.toString())
					&& buttons[2][2].getText().equals(whosTurn.toString()))
				return true;
			if (buttons[0][2].getText().equals(whosTurn.toString())
					&& buttons[2][0].getText().equals(whosTurn.toString()))
				return true;
		}

		return won;
	}
	/**
	 * There are no moves left
	 * @return true if there are no more moves left
	 */
	private boolean noMoreMoves() {
		if (squaresRevealed >= 9)
			return true;
		return false;
	}
	/**
	 * Lets the next player take their turn
	 */
	public void switchTurns() {
		if (whosTurn == Players.O) {
			whosTurn = Players.X;
			// System.out.println("X's turn");
			return;
		}
		whosTurn = Players.O;
		// System.out.println("O's turn");
	}
	/**
	 * Enum for players X and O
	 * @author Mike
	 *
	 */
	enum Players {
		X, O;
	}
}
