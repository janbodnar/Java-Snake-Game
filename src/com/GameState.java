package snake;


/**
 *	Class to provide the data and rudimentary business logic that particular to
 *  which level and cycle the game is in. 
 */
public class GameState 
{
	private int level = 0;
	private int cycle = 0;
	private int lives = 5;
	private static final int MAX_CYCLES = 2;
	private static final int[] numOfLevels = {2, 3};
	private static final int[] requiredEats = {15, 25};
	private static final int[] fullSnakeLength = {10, 20};

	
	public GameState() 
	{
		
	}
	
	
	/**
	 * To be called prior to first life being put in game,
	 * and then after each death
	 */
	public void useALife()
	{
		--lives;
	}
	
	
	/**
	 * @return true if the game is still alive
	 */
	public boolean isAlive()
	{
		return lives > 0;
	}
	
	
	/**
	 * @return The number of apples which must be eaten
	 * to complete this level
	 */
	public int getRequiredEats()
	{
		return requiredEats[cycle];
	}
	
	
	
	public int getFullSnakeLength()
	{
		return fullSnakeLength[cycle];
	}
	
	
	/**
	 * @return number of levels in current cycle
	 */
	public int getNumOfLevels()
	{
		return numOfLevels[cycle];
	}
	
	
	public int getMaxCycles()
	{
		return MAX_CYCLES;
	}
	
	
	public boolean isGameCompleted()
	{
		return cycle == MAX_CYCLES;
	}
	
	
	public void levelUp()
	{
		++level;
		if(level == numOfLevels[cycle]) ++cycle;
		
	}
}
