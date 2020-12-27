package snake;

public enum Direction 
{
	RIGHT,
	LEFT,
	UP,
	DOWN;
	
	
	public boolean isRight()
	{
		return this == RIGHT;
	}
	
	
	public boolean isLeft()
	{
		return this == LEFT;
	}
	
	
	public boolean isUp()
	{
		return this == UP;
	}
	
	
	public boolean isDown()
	{
		return this == DOWN;
	}
}
