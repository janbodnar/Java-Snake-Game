package snake;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Snake extends JFrame
{
	private static final long serialVersionUID = -5094450331303156231L;

	public Snake()
	{
		add(new Board(() -> dispose()));
		setResizable(false);
		pack();
		
		setTitle("Snaaake");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {
			JFrame ex = new Snake();
			ex.setVisible(true);
		});
	}
}
