package edu.wm.cs.cs301.slidingpuzzle;

import java.util.Arrays;
import java.util.Random;

public class SimplePuzzleState implements PuzzleState {

	private int dimension;
	private int empties;
	private int[] board;
	private PuzzleState parent;
	private Operation operation;
	private int pathLength;
	
	//constructor
	SimplePuzzleState(int[] newBoard){
		dimension = 0;
		empties = 0;
		board = newBoard;
		parent = null;
		operation = null;
		pathLength = 0;
	}
	
	//constructor
	SimplePuzzleState(){
		dimension = 0;
		empties = 0;
		board = null;
		parent = null;
		operation = null;
		pathLength = 0;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(board);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// see if two puzzle states are equal to each other
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePuzzleState other = (SimplePuzzleState) obj;
		if (!Arrays.equals(board, other.board))
			return false;
		
		return true;
	}

	@Override
	public void setToInitialState(int dimension, int numberOfEmptySlots) {
		//need to set object that can be changed in this method
		this.dimension = dimension;
		this.empties = numberOfEmptySlots;
		this.board = new int[dimension * dimension];
		int i;
		int filledBoxes;
		
		filledBoxes = dimension * dimension - numberOfEmptySlots;
		
		for(i = 0; i < filledBoxes; i++){
			this.board[i] = i + 1;
		}
		
		for(i = 0; i < numberOfEmptySlots; i++){
			this.board[i + filledBoxes] = 0;
		}
	}

	@Override
	public int getValue(int row, int column) {
		int index;
		int value;
		
		index = row * dimension + column;
		value = board[index];
		return value;
	}

	@Override
	public PuzzleState getParent() {
		return this.parent;
	}

	@Override
	public Operation getOperation() {
			return operation;
	}

	@Override
	public int getPathLength() {
		return this.pathLength;
	}

	@Override
	public PuzzleState move(int row, int column, Operation op) {
		// first check if it is possible to move a tile from the given position in the given 
		// direction. if it is possible, return a new instance of PuzzleState where the 
		//parent state is set to this object. if move is not possible, return null
		
		int value;
		int index;
		int indexEmptyCheck;
		int temp;
		int i;
		
		value = getValue(row, column);
		index = row * dimension + column;
		SimplePuzzleState state = new SimplePuzzleState();
		state.dimension = this.dimension;
		state.empties = this.empties;
		state.board = new int[dimension * dimension];
		for (i =0; i < dimension * dimension; i++){
			state.board[i] = this.board[i];
		}
		
		if (op == Operation.MOVERIGHT){
			if (isEmpty(row, column + 1) == true){
				state.parent = this;
				state.operation = op;
				state.pathLength = this.pathLength;
				//also sets the distance from the initial state for the returned state...?
				
				temp = state.board[index];
				state.board[index] = state.board[index + 1];
				state.board[index + 1] = temp;
				state.pathLength += 1;
				return state;
			}
			else{
				return null;
			}
		}
		
		else if (op == Operation.MOVELEFT){
			if (isEmpty(row, column - 1) == true){
				state.parent = this;
				state.operation = op;
				state.pathLength = this.pathLength;
				//also sets the distance from the initial state for the returned state...?
				
				temp = state.board[index];
				state.board[index] = state.board[index - 1];
				state.board[index - 1] = temp;
				state.pathLength += 1;
				return state;
			}
			else{
				return null;
			}
			
		}
		
		else if (op == Operation.MOVEUP){
			if (isEmpty(row - 1, column) == true){
				state.parent = this;
				state.operation = op;
				state.pathLength = this.pathLength;
				//also sets the distance from the initial state for the returned state...?
				
				temp = state.board[index];
				state.board[index] = state.board[index - dimension];
				state.board[index - dimension] = temp;
				state.pathLength += 1;
				return state;
			}
			else{
				return null;
			}
			
		}
		
		else if (op == Operation.MOVEDOWN){
			if (isEmpty(row + 1, column) == true){
				state.parent = this;
				state.operation = op;
				state.pathLength = this.pathLength;
				//also sets the distance from the initial state for the returned state...?
				
				temp = state.board[index];
				state.board[index] = state.board[index + dimension];
				state.board[index + dimension] = temp;
				state.pathLength += 1;
				return state;
			}
			else{
				return null;
			}
			
		}
				
		// add parent and previous operation to tuple containing path from initial state
		return null;
	}
	
	public int numberOfEmptySlots(int row, int column){
		int emptySlots = 0;
		
		if (isEmpty(row + 1, column) == true){
			emptySlots += 1;
		}
		if (isEmpty(row - 1, column) == true){
			emptySlots += 1;
		}
		if (isEmpty(row, column + 1) == true){
			emptySlots += 1;
		}
		if (isEmpty(row, column - 1) == true){
			emptySlots += 1;
		}
		
		return emptySlots;
	}
	
	@Override
	public PuzzleState flip(int startRow, int startColumn, int endRow, int endColumn) {
//       short cut for series of move operations
//       check if it is possible to move tile from start to end position
		
		// remember: changing row changes the vertical position and 
		// changing column changes the horizontal position
		int moveVertical;
		int moveHorizontal;
		int currentRow;
		int currentColumn;
		int i;
		int count;
		int emptySlots;
		PuzzleState state = null;

		count = 0;
		emptySlots = 0;
		currentRow = startRow;
		currentColumn = startColumn;
		moveVertical = startRow - endRow;
		moveHorizontal = startColumn - endColumn;
			while (moveVertical != 0 || moveHorizontal != 0){
				if(count == 0){
					if ((isEmpty(currentRow + 1, currentColumn) == true) && moveVertical < 0){
						state = this.move(currentRow, currentColumn, Operation.MOVEDOWN);
						moveVertical += 1;
						count += 1;
						currentRow += 1;
					}
					
					else if ((isEmpty(currentRow - 1, currentColumn) == true) && moveVertical > 0){
						state = this.move(currentRow, currentColumn, Operation.MOVEUP);
						moveVertical -= 1;
						count += 1;
						currentRow -= 1;
					}
					
					else if ((isEmpty(currentRow, currentColumn + 1) == true) && moveHorizontal < 0){
						state = this.move(currentRow, currentColumn, Operation.MOVERIGHT);
						moveHorizontal += 1;
						count += 1;
						currentColumn += 1;
					}
					
					else if ((isEmpty(currentRow, currentColumn - 1) == true) && moveHorizontal > 0){
						state = this.move(currentRow, currentColumn, Operation.MOVELEFT);
						moveHorizontal -= 1;
						count += 1;
						currentColumn -= 1;
					}
					else{
						return null;
					}
				}
					
				
				else{
					if ((isEmpty(currentRow + 1, currentColumn) == true) && moveVertical < 0){
						state = state.move(currentRow, currentColumn, Operation.MOVEDOWN);
						moveVertical += 1;
						count += 1;
						currentRow += 1;
					}
					
					else if ((isEmpty(currentRow - 1, currentColumn) == true) && moveVertical > 0){
						state = state.move(currentRow, currentColumn, Operation.MOVEUP);
						moveVertical -= 1;
						count += 1;
						currentRow -= 1;
					}
					
					else if ((isEmpty(currentRow, currentColumn + 1) == true) && moveHorizontal < 0){
						state = state.move(currentRow, currentColumn, Operation.MOVERIGHT);
						moveHorizontal += 1;
						count += 1;
						currentColumn += 1;
					}
					
					else if ((isEmpty(currentRow, currentColumn - 1) == true) && moveHorizontal > 0){
						state = state.move(currentRow, currentColumn, Operation.MOVELEFT);
						moveHorizontal -= 1;
						count += 1;
						currentColumn -= 1;
					}
					else{
						return null;
					}
				
				}
			}
			return state;
		}
	
	public Operation chooseOperation(){
		Operation operation;
		Operation[] operations = new Operation[4];
		operations[0] = Operation.MOVEDOWN;
		operations[1] = Operation.MOVELEFT;
		operations[2] = Operation.MOVERIGHT;
		operations[3] = Operation.MOVEUP;
		
		int index = new Random().nextInt(operations.length);
		operation = (operations[index]);
	
		return operation;
	}
	
	public SimplePuzzleState performOperation(int row, int column, Operation operation){
		int i;
		
		SimplePuzzleState state = new SimplePuzzleState();
		state.dimension = this.dimension;
		state.empties = this.empties;
		state.board = new int[dimension * dimension];
		state.parent = this.parent;
		state.operation = this.operation;
		for (i =0; i < dimension * dimension; i++){
			state.board[i] = this.board[i];
		}
		i = 0;
		if (operation.name() == "MOVERIGHT"){
			column --;
			if (column < 0){
				return null;
			}
			else{
				state.pathLength = this.pathLength;
				return (SimplePuzzleState) state.move(row, column, operation);
			}
		}
		else if (operation.name() == "MOVELEFT"){
			column ++;
			if (column == dimension){
				return null;
			}
			else{
				state.pathLength = this.pathLength;
				return (SimplePuzzleState) state.move(row, column, operation);
			}
		}
		else if (operation.name() == "MOVEUP"){
			row ++;
			if (row == dimension){
				return null;
			}
			else{
				state.pathLength = this.pathLength;
				return (SimplePuzzleState) state.move(row, column, operation);
			}
		}
		else if (operation.name() == "MOVEDOWN"){
			row --;
			if (row < 0){
				return null;
			}
			else{
				state.pathLength = this.pathLength;
				return (SimplePuzzleState) state.move(row, column, operation);
			}
		}
		return state;
	}
	
	@Override
	public PuzzleState shuffleBoard(int pathLength) {
		// TODO Auto-generated method stub
		// shuffles the board to create a new game for the user
		// need to create move operation before this one can be created
		// numberOfEmptySlots(row, column)
		int count;
		int i;
		int j;
		int [] board;
		int emptyRow;
		int emptyColumn;
		Operation operation;
	
		SimplePuzzleState state = new SimplePuzzleState();
		state.dimension = this.dimension;
		state.empties = this.empties;
		state.board = new int[dimension * dimension];
		for (i =0; i < dimension * dimension; i++){
			state.board[i] = this.board[i];
		}
		
		if (empties == 1){
			for (i = 0; i < pathLength;){
				// first need to find the empty space
				for (j = 0; j < dimension * dimension; j++){
					if (state.board[j] == 0){
						emptyColumn = j % dimension;
						emptyRow = j / dimension;
						operation = chooseOperation();
						// check if operation is possible on puzzle state
						if (state.performOperation(emptyRow, emptyColumn, operation) == null){
							continue;
						}
						else{
							state = (SimplePuzzleState) state.performOperation(emptyRow, emptyColumn, operation);
							i ++;
							break;
						}
					}	
				}
			}	

		}
		
		// random number generator, iterate through the empties, and then 
		// use the random number generator again to choose which tile to move
		// that is around the empty tile
		else if (empties == 2){
			int[] listOfEmpties = new int[2];
			for (i = 0; i < pathLength;){
				count = 0;
				// first need to find the empty space
				for (j = 0; j < dimension * dimension; j++){
					if (state.board[j] == 0){
						listOfEmpties[count] = j;
						count += 1;
					}
				}
				
				int index = new Random().nextInt(listOfEmpties.length);
				j = listOfEmpties[index];
				emptyColumn = j % dimension;
				emptyRow = j / dimension;
				operation = chooseOperation();
				// check if operation is possible on puzzle state
				if (!(state.performOperation(emptyRow, emptyColumn, operation) == null)){
					state = (SimplePuzzleState) state.performOperation(emptyRow, emptyColumn, operation);
					i ++;
				}
				else{
					continue;
					}	
			}
		}
		
		else if (empties == 3){
			int[] listOfEmpties = new int[3];
			for (i = 0; i < pathLength;){
				count = 0;
				// first need to find the empty space
				for (j = 0; j < dimension * dimension; j++){
					if (state.board[j] == 0){
						listOfEmpties[count] = j;
						count += 1;
					}
				}
				
				int index = new Random().nextInt(listOfEmpties.length);
				j = listOfEmpties[index];
				emptyColumn = j % dimension;
				emptyRow = j / dimension;
				operation = chooseOperation();
				// check if operation is possible on puzzle state
				if (!(state.performOperation(emptyRow, emptyColumn, operation) == null)){
					state = (SimplePuzzleState) state.performOperation(emptyRow, emptyColumn, operation);
					i ++;
				}
				else{
					continue;
					}	
				}
			}
		return state;
	}

	@Override
	public boolean isEmpty(int row, int column) {
		int index;
		index = row * dimension + column;
		
		//special conditions for array out of bounds exception
		if (index < 0){
			return false;
		}
		//special conditions for array out of bounds exception
		else if (index > dimension * dimension - 1){
			return false;
		}
		else if (board[index] == 0){
			return true;
		}
		else{
			return false;
		}
	}
}
