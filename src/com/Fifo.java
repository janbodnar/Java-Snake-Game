package snake;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class Fifo 
{
	private final int maxSize; 
	private Deque<Point> list;
	private int size = 1;
	
	public Fifo(int maxSize)
	{
		this.maxSize = maxSize;
		list = new LinkedList<>();
	}
	
	
	/**
	 * @param point The point to be put in the FIFO
	 * @return true if the FIFO will have point stored twice, false otherwise
	 */
	public boolean add(Point point)
	{
		if(list.size() == size) list.removeLast();
		boolean isInTwice = isInList(point);
		list.addFirst(point);
		return isInTwice;
	}

	
	public void grow()
	{
		if(size < maxSize)
		{
			++size;
		}
	}
	
	
	public boolean isInList(Point point)
	{
		return list.parallelStream().map(p -> point.equals(p)).filter(b -> b).findFirst().orElse(false);
	}
	
	
	public Iterator<Point> getIterator()
	{
		return list.iterator();
	}
	
	
	public void clear()
	{
		size = 1;
		list.clear();
	}
	
	
	public Point getFirst()
	{
		return list.getFirst();
	}
	
	
	public int getCurrentSize()
	{
		return list.size();
	}
	
	
	public int getMaxSize()
	{
		return maxSize;
	}
}
