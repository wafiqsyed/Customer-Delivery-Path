import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Wafiq Syed
 * For the bonus marks. 
 *
 */
public class ComputeOptimalPath {
	private Map cityMap;
	
	/**Constructor that receives name of the file containing the description map, and the initial and 
	 * destination map cells. 
	 * Method creates an object of the class Map passing as parameter the given input file; this will
	 * display the map on the screen. 
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InvalidMapException 
	 */
	
	public ComputeOptimalPath(String filename){
		try {
			cityMap = new Map(filename);
		}catch (FileNotFoundException e){System.out.println("Sorry, the file is not found. Please select a valid file.");
		}catch (IOException e){System.out.println("Sorry, the file is not found. Please select a valid file.");}
	}
	
	/*
	 * Creates an object of the class ComputePath using above constructor. Parameter for the 
	 * constructor will be the first command line argument of the program (args[0])
	 * Method will try to find a path from the initial cell to the destination according to restrictions
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ComputeOptimalPath map = new ComputeOptimalPath(args[1]);
		try {
			MyStack<MapCell> travelHistory = new MyStack<MapCell>();		//Creates empty stack for the travel history.
			MapCell firstCell = map.cityMap.getStart();					//Gets the starting cell using methods from Map class.
			travelHistory.push(firstCell);								//Adds the starting cell into the stack
			firstCell.markInStack();										//Marks the starting cell as inStack using method from MapCell.
			MapCell lastCell = path(travelHistory);						//Get the last cell of the first path taken.
	
			if(lastCell.isCustomer()) {								//Check final cell to see if the destination is reached. 
				System.out.println("Congratulations, the product has been delivered to the customer's house via the optimal path.");
				System.out.println("The number of cells travelled by the drone: " + travelHistory.size());
			}
			
		}catch(Exception noPath) {System.out.println("Sorry, there is no avaialble path to the customer's house.");
		}
		
	}
	/*
	 * Adds the path to the array to keep track of travel history.
	 * Returns the last cell of the array, or the last cell the drone has traveled. Our aim is to make the last 
	 * cell the destination (customer's house), if it is not, we look for another path or handle an exception and 
	 * state that there is no available path. The exception is thrown when the stack array is empty. 
	 * @param pathStack is the stack array initialized in the main method. 
	 */
	private static MapCell path(MyStack<MapCell> pathStack) {
		MyStack<MapCell> path = pathStack;
			while (!path.isEmpty()) {							//Initiates loop on the condition that the stack isn't empty
				MapCell currentCell = path.peek();				//Peeks at top stack cell to get current map cell
				if(currentCell.isCustomer()) {break;}			//Exits the loop if the destination has been reached 
				if(interference(currentCell)) {					//Calls upon interference method to see if neighbouring cell is tower cell
					MapCell topCell = path.pop();				//If neighbour is tower cell, remove top cell, and
					topCell.markOutStack();						//Mark that cell as outofStack so that is is not an option of travel. 
				}else {
					MapCell nextMove = nextFreeCell(currentCell);
					if(nextMove == null) {
						if(!currentCell.isInitial()){
						MapCell removedCell = path.pop(); 
						removedCell.markOutStack();
						}
						else {
							MyStack<MapCell> highAltPath = pathStack; 
							while (!highAltPath.isEmpty()) {
									MapCell currentAltCell = highAltPath.peek();				//Peeks at top stack cell to get current map cell
									if(currentAltCell.isCustomer()) {break;}			//Exits the loop if the destination has been reached 
									if(interference(currentAltCell)) {					//Calls upon interference method to see if neighbouring cell is tower cell
										MapCell topCell = highAltPath.pop();				//If neighbour is tower cell, remove top cell, and
										topCell.markOutStack();						//Mark that cell as outofStack so that is is not an option of travel. 
									}else {
										MapCell next = nextFreeOrAltCell(currentCell);
										if(next == null) {
											if(!currentCell.isInitial()){
												MapCell removedCell = path.pop(); 
												removedCell.markOutStack();
												}
											else {
												MyStack<MapCell> thiefPath = pathStack; 
												while (!thiefPath.isEmpty()) {
														MapCell finalPathCell = thiefPath.peek();				//Peeks at top stack cell to get current map cell
														if(finalPathCell.isCustomer()) {break;}			//Exits the loop if the destination has been reached 
														if(interference(finalPathCell)) {					//Calls upon interference method to see if neighbouring cell is tower cell
															MapCell topCell = thiefPath.pop();				//If neighbour is tower cell, remove top cell, and
															topCell.markOutStack();						//Mark that cell as outofStack so that is is not an option of travel. 
														}else {
															MapCell nextFinal = nextCell(finalPathCell);
															if(nextFinal == null) {
																MapCell removedCell = thiefPath.pop(); 
																removedCell.markOutStack();
															}else {
																thiefPath.push(nextFinal);
																nextFinal.markInStack();
															}
														}
														
												}			
												MapCell lastCell = path.peek();
												return lastCell;	
											}
										}else {
											highAltPath.push(next);
											next.markInStack();
										}
									}
									
							}MapCell lastCell = highAltPath.peek();
							return lastCell;	
						}
					}else {
						path.push(nextMove);
						nextMove.markInStack();
					}
					}
	

				}	
			MapCell lastCell = path.peek();
			return lastCell;
			}
	
	/*
	 * Returns true if any of the adjacent cells to the current one is a tower cell
	 * Returns false otherwise
	 * Parameters is the cell where the drone currently is
	 */
	private static boolean interference(MapCell cell) {
		MapCell currentCell = cell;
		
		try{
			for(int neighbour = 0; neighbour < 6; neighbour++) {		//Loop through neighbouring cells
				if (currentCell.getNeighbour(neighbour) != null) {
				if(currentCell.getNeighbour(neighbour).isTower()) {return true;}
				}
				}
		}catch (Exception e) {System.out.println("ERROR Occurred: Unable to identify if the adjacent cell is a tower cell. " + e.getMessage());}
		
		return false;
	}
	
	
	/*
	 * Returns the best cell for the drone to move to from the current one. 
	 * In the case of multiple free cells, the order of cell preference is: 
	 * a free cell, if one exists, or the customer cell. If there are several free cells, then the 
	 * first one (one with smallest index) is returned. 
	 * a high altitude cell
	 * a thief's cell
	 * If there are no unmarked cells adjacent to the current one, this method returns null. 
	 * @param cell is where the drone is currently.
	 */
	
	private static MapCell nextFreeCell(MapCell cell) {
		MapCell currentCell = cell;
		try {
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the customer cell
				if (currentCell.getNeighbour(neighbour) != null && !currentCell.getNeighbour(neighbour).isMarked()) {
						if(currentCell.getNeighbour(neighbour).isCustomer()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
			
			// not a customer cell. check for free cell
			
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the free cell
				if (currentCell.getNeighbour(neighbour) != null && !currentCell.getNeighbour(neighbour).isMarked()) {
					if(currentCell.getNeighbour(neighbour).isFree()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
		}catch(Exception e) {
			System.out.println("ERROR Occurred: Unable to get the next cell. " + e.getMessage());}
		
		return null;
	}
	
	private static MapCell nextFreeOrAltCell(MapCell cell) {
		MapCell currentCell = cell;
		try{
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the customer cell
				if (currentCell.getNeighbour(neighbour) != null) {
						if(currentCell.getNeighbour(neighbour).isCustomer()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
			
			// not a customer cell. check for free cell
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the free cell
				if (currentCell.getNeighbour(neighbour) != null) {
					if(currentCell.getNeighbour(neighbour).isFree()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
			
			// high Altitude
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the customer cell
				if (currentCell.getNeighbour(neighbour) != null) {
					if(currentCell.getNeighbour(neighbour).isHighAltitude()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
		}catch(Exception e) {
			System.out.println("ERROR Occurred: Unable to get the next cell. " + e.getMessage());
		}
		return null;
	}
	private static MapCell nextCell(MapCell cell) {
		MapCell currentCell = cell;
		
		try {
		
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the customer cell
				if (currentCell.getNeighbour(neighbour) != null) {
						if(currentCell.getNeighbour(neighbour).isCustomer()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
			
			// not a customer cell. check for free cell
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the free cell
				if (currentCell.getNeighbour(neighbour) != null) {
					if(currentCell.getNeighbour(neighbour).isFree()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
			
			// high Altitude
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the customer cell
				if (currentCell.getNeighbour(neighbour) != null) {
					if(currentCell.getNeighbour(neighbour).isHighAltitude()) {return currentCell.getNeighbour(neighbour);}		
				}
			}
			
			// thiefCell
			for(int neighbour = 0; neighbour <= 5; neighbour++) {	
				//loop through to find the free cell
				if (currentCell.getNeighbour(neighbour) != null) {
					if(currentCell.getNeighbour(neighbour).isThief()) {return currentCell.getNeighbour(neighbour);}			
				}
			}
		
		}catch (Exception e) {
			System.out.println("ERROR Occurred: Unable to get the next cell. " + e.getMessage());}
		
		return null;
	}
	
	
	
}
