package hw1;

/**
 * Model of a Monopoly-like game. Two players take turns
 * rolling dice to move around a board. The game ends
 * when one of the players has at least MONEY_TO_WIN
 * money or one of the players goes bankrupt (they have
 * negative money).
 * 
 * @author Hugo Medina
 */
public class CyGame {
	/**
	 * The endzone square type.
	 */
	public static final int ENDZONE = 0;
	/**
	 * The CyTown square type.
	 */
	public static final int CYTOWN = 1;
	/**
	 * The pay rent square type.
	 */
	public static final int PAY_RENT = 2;
	/**
	 * The fall behind square type.
	 */
	public static final int FALL_BEHIND = 3;
	/**
	 * The blizzard square type.
	 */
	public static final int BLIZZARD = 4;
	/**
	 * The pass class square type.
	 */
	public static final int PASS_CLASS = 5;
	/**
	 * Points awarded when landing on or passing over the endzone square.
	 */
	public static final int ENDZONE_PRIZE = 200;
	/**
	 * The standard rent payed to the other player when landing on a
	 * pay rent square.
	 */
	public static final int STANDARD_RENT_PAYMENT = 80;
	/**
	 * The cost to by CyTown.
	 */
	public static final int CYTOWN_COST = 200;
	/**
	 * The amount of money required to win.
	 */
	public static final int MONEY_TO_WIN = 400;

	
	/** 
	 *  Instance variables
	 */
	/** Player with the current turn. */
	private int currentPlayer;
	/** Current board square that player 1 is on. */
	private int player1Square;
	/** Current board square that player 2 is on. */
	private int player2Square;
	/** Amount that player 1 currently has. */
	private int player1Money;
	/** Amount that player 2 currently has.*/
	private int player2Money;
	/** Number of squares on the board. */
	private int numSquares;
	/** Return true if player 1 owns CyTown. */
	private boolean ownsCyTownPlayer1;
	/** Return true if player 2 owns CyTown. */
	private boolean ownsCyTownPlayer2;

	
	
	/**
	 * Constructs a CyGame with starting money and a given number of squares.
	 * Each player starts on square 0 and starts with given amount of money.
	 * Player 1 goes first
	 * 
	 * @param numSquares number of squares on the board
	 * @param startingMoney starting amount of money for each player
	 */
	public CyGame(int numSquares, int startingMoney) {
		this.numSquares = numSquares;
		this.player1Money = startingMoney;
		this.player2Money = startingMoney;
		this.player1Square = 0;
		this.player2Square = 0;
		this.ownsCyTownPlayer1 = false;
		this.ownsCyTownPlayer2 = false;
		this.currentPlayer = 1;
	}
	
	
	/**
	 * Returns the number of the player whose turn it currently is.
	 * 
	 * @return 1 if it is player 1's turn, 2 if it is player 2's turn
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	
	/**
	 * Returns the player who is not taking their turn.
	 * 
	 * @return 2 if it is currently Player 1's turn, otherwise 1
	 */
	public int getOtherPlayer() {
		if (currentPlayer == 1) {
			return 2;
		} else {
			return 1;
		}
	}
	
	
	/**
	 * Returns the square that the given player is on.
	 * 
	 * @param player the player number (1 or 2)
	 * @return the square number the player is on
	 */
	public int getPlayerSquare(int player) {
		if (player == 1) {
			return player1Square;
		} else {
			return player2Square;
		}
	}
	
	
	/**
	 * Returns amount of money of the given player.
	 * 
	 * @param player the player number (1 or 2)
	 * @return the player's current money
	 */
	public int getPlayerMoney(int player) {
		if (player == 1) {
			return player1Money;
		} else {
			return player2Money;
		}
	}
	
	
	/**
	 * Returns whether player 1 owns CyTown.
	 * 
	 * @return true if player 1 owns CyTown, false otherwise
	 */
	public boolean isPlayer1CyTownOwner() {
		return ownsCyTownPlayer1;
	}
	
	
	/**
	 * Returns true if player 2 owns CyTown.
	 * 
	 * @return true if player 2 owns CyTown, false otherwise
	 */ 
	public boolean isPlayer2CyTownOwner() {
		return ownsCyTownPlayer2;
	}
	
	
	/**
	 * Returns type of given square.
	 * Rules applied in order:
	 * 1. Square 0 = ENDZONE
	 * 2. Last square (numSquares - 1) = CYTOWN
	 * 3. Every 5th square = PAY_RENT
	 * 4. Every 7th or 11th square = FALL_BEHIND
	 * 5. Every 3rd square = BLIZZARD
	 * 6. All remaining = PASS_CLASS
	 * 
	 * @param square the square number
	 * @return the square type constant
	 */
	public int getSquareType(int square) {
		if (square == 0) {
			return ENDZONE;
		} else if (square == numSquares - 1) {
			return CYTOWN;
		} else if (square % 5 ==0) {
			return PAY_RENT;
		} else if (square % 7 == 0 || square % 11 == 0) {
			return FALL_BEHIND;
		} else if (square % 3 == 0) {
			return BLIZZARD;
		} else {
			return PASS_CLASS;
		}
	}
	
	
	/**
	 * This method is called to indicate the die has been rolled.
	 * Processes a die roll for the current player and advances their position.
	 * 
	 * Advances the current player forward by the rolled value with the following rules:
	 * 1:If player is on a BLIZZARD square and roll is even, the turns ends with no movement.
	 * 2:If player passes over square 0, they receive ENDZONE_PRIZE money
	 * 
	 * 
	 * @param value the number rolled by the die (1-6)
	 */
	public void roll(int value) {
		if (isGameEnded()) {
			return;
		}
		
		int currentSquare = getPlayerSquare(currentPlayer);
		int squareType = getSquareType(currentSquare);
		
		//If on BLIZZARD and roll is even, turn ends with no movement
		if (squareType == BLIZZARD && value % 2 == 0) {
			endTurn();
			return;
		}
		
		//Move forward
		int newSquare = (currentSquare + value) % numSquares;
		
		//Award endzone prize if the player passed over square 0 (only once per turn)
		
		if (currentSquare + value >= numSquares) {
			addMoney(currentPlayer, ENDZONE_PRIZE);
		}
		
		setPlayerSquare(currentPlayer, newSquare);
		
		//Apply action for the landed square
		applyAction(newSquare, false);
		
		
		//End turn unless we landed on CYTOWN (player must call buyCyTown or endTurn)
		if (getSquareType(getPlayerSquare(currentPlayer)) != CYTOWN) {
			endTurn();
		}
	}
	
	
	/**
	 * Applies action for the given square for current player.
	 * 
	 * @param square the square to apply the action for
	 * @param isChained true if this was triggered by FALL_BEHIND or PASS_CLASS
	 */
	private void applyAction(int square, boolean isChained) {
		int type = getSquareType(square);
		
		if (type == ENDZONE) {
			//Prize already awarded during movement in roll()
		} else if (type == CYTOWN) {
			//Player decides via buyCyTown() or endTurn()
		} else if (type == PAY_RENT) {
			int rent = STANDARD_RENT_PAYMENT;
			if (currentPlayer == 1 && ownsCyTownPlayer2) {
				rent *= 2;
			} else if (currentPlayer == 2 && ownsCyTownPlayer1) {
				rent *= 2;
			}
			
			addMoney(currentPlayer, -rent);
			addMoney(getOtherPlayer(), rent);
		} else if (type == FALL_BEHIND) {
			if (!isChained) {
				int behindSquare = (square - 1 + numSquares) % numSquares;
				setPlayerSquare(currentPlayer, behindSquare);
				int behindType = getSquareType(behindSquare);
				//chain only if new square is not another movement square
				if (behindType != FALL_BEHIND && behindType != PASS_CLASS) {
					applyAction(behindSquare, true);
				}
			}
		} else if (type == BLIZZARD) {
			//Player stays stuck until they rolled odd next turn
		} else if (type == PASS_CLASS) {
			if (!isChained) {
				int currentSquare = square;
				int forwardSquare = (currentSquare + 4) % numSquares;
				// Award endzone prize if passing over square 0
				if (currentSquare + 4 >= numSquares) {
					addMoney(currentPlayer, ENDZONE_PRIZE);
				}
				setPlayerSquare(currentPlayer, forwardSquare);
				int forwardType = getSquareType(forwardSquare);
				//chain only if the new square is not another movement square
				if (forwardType != FALL_BEHIND && forwardType != PASS_CLASS) {
					applyAction(forwardSquare, true);
				}
			}
		}
	}
	
	
	/**
	 * Method called to indicate current player is attempting to buy CyTown.
	 * 
	 * Purchase is only allowed if the player is on CYTOWN. no one owns it yet
	 * and the player has enough money. It deducts CYTOWN_COST if it's successful.
	 * 
	 * Ends current player's turn regardless of outcome if on CYTOWN square.
	 * Does nothing if not on CYTOWN or game has ended.
	 */
	public void buyCyTown() {
		if (isGameEnded()) {
			return;
		}
		
		int square = getPlayerSquare(currentPlayer);
		if (getSquareType(square) != CYTOWN) {
			return;
		}
		
		//Attempt purchase if no one owns it and player can afford it
		if (!ownsCyTownPlayer1 && !ownsCyTownPlayer2 && getPlayerMoney(currentPlayer) >= CYTOWN_COST) {
			addMoney(currentPlayer, -CYTOWN_COST);
			if (currentPlayer == 1) {
				ownsCyTownPlayer1 = true;
			} else {
				ownsCyTownPlayer2 = true;
			}
		}
		
		//Turn always ends after calling buyCyTown on CYTOWN square
		endTurn();
	}
	
	
	/**
	 * Ends current player's turn and passes control to the other player.
	 */
	public void endTurn() {
		if (currentPlayer == 1) {
			currentPlayer = 2;
		} else {
			currentPlayer = 1;
		}
	}
	
	
	/**
	 * Returns true if the game is over
	 * Game ends when either player has at least MONEY_TO_WIN money,
	 * or when either player has a negative amount of money.
	 * 
	 * @return true if game is over, false otherwise
	 */
	public boolean isGameEnded() {
		return player1Money >= MONEY_TO_WIN || player2Money >= MONEY_TO_WIN || player1Money < 0 || player2Money < 0;
	}
	
	
	/**
	 * This is a helper method
	 * Adjusts given player's balance by the specified amount.
	 * pass a negative value to subtract money
	 * 
	 * @param player the player number(1 or 2)
	 * @param amount the amount to add or uses negative to subtract
	 */
	private void addMoney(int player, int amount) {
		if (player == 1) {
			player1Money += amount;
		} else {
			player2Money += amount;
		}
	}
	
	
	/**
	 * This is a helper method that places the given player on specified board square.
	 * 
	 * @param player the player number(1 or 2)
	 * @param square the square number to place the player to
	 */
	private void setPlayerSquare(int player, int square) {
		if (player == 1) {
			player1Square = square;
		} else {
			player2Square = square;
		}
	}
	


	// The toString method below is provided for you and you should not modify
	// it. The compile errors will go away after you have written stubs for the
	// rest of the API methods.

	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * <p>
	 * <tt>Player 1*: (0, false, $0) Player 2: (0, false, $0)</tt>
	 * <p>
	 * The asterisks next to the player's name indicates which players turn it
	 * is. The values (0, false, $0) indicate which square the player is on,
	 * if the player is the owner of CyTown, and how much money the player has
	 * respectively.
	 * 
	 * @return one-line string representation of the game state
	 */
	public String toString() {
		String fmt = "Player 1%s: (%d, %b, $%d) Player 2%s: (%d, %b, $%d)";
		String player1Turn = "";
		String player2Turn = "";
		if (getCurrentPlayer() == 1) {
			player1Turn = "*";
		} else {
			player2Turn = "*";
		}
		return String.format(fmt,
				player1Turn, getPlayerSquare(1), isPlayer1CyTownOwner(), getPlayerMoney(1),
				player2Turn, getPlayerSquare(2), isPlayer2CyTownOwner(), getPlayerMoney(2));
	}
}
