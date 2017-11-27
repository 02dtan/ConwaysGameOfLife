import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/******************************************************************************
* APCS
* Name:		Daniel Tan
* Block:	D
* Date: 	11/25/17
* 
* Program #5: Conway's Game of Life
* Description:
*		Visual representation of Conway's Game of Life, following standard
* rules. Program should start where you, as the user, can click on the screen
* and input what you want the starting positions to be, and then auto-iterate
* once the start button is pressed.
* 
* You'll see a lot of multiplication and division of 4s running around because
* I arbitrarily set one actual value in the array to be 4x4 pixels on screen
* so the actual window size will be 4x the array size etc etc
*****************************************************************************/

public class Life extends JPanel implements MouseListener {
	
	//Two grids used to store game field values.
	static int grid[][];
	static int alternate[][];
	
	//Basically the game loop.
	//Every 100 ms, life will update and repaint.
	//Starts when "start" ActionListener triggered,
	//stops when "stop" ActionListener triggered.
	static Timer timer = new Timer(100, new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			life.update();
			life.repaint();
		}
	});
	
	static Life life = new Life(400, 400); //400x400 pixels on-screen, 100x100 grid
	
	/**
	 * Instance of the Life object: initializes window basically
	 * @param x		how large the playing grid is, horizontally
	 * @param y		how large the playing grid is, vertically
	 */
	public Life(int x, int y)
	{
		Life.grid = new int[(y/4)+2][(x/4)+2]; //each grid value has a 4x4 pixel "pixel" onscreen
											   //+2 on each axis for padding on top/bottom/left/right
		
		Life.alternate = new int[(y/4)+2][(x/4)+2];
		
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[i].length; j++)
			{
				grid[i][j] = 0;
			}
		}
		
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[i].length; j++)
			{
				alternate[i][j] = 0;
			}
		}
	}
	
	/**
	 * update()
	 * Goes through the whole grid and updates it, sending
	 * new values to the alternate grid to not disturb the current
	 * grid while it is being searched.
	 */
	public void update()
	{
		//Skips the padding.
		for(int i = 1; i < grid.length - 1; i++)
		{
			for(int j = 1; j < grid[0].length - 1; j++)
			{
				search(i, j);
			}
		}
		//Sets playing field to the results.
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid.length; j++)
			{
				grid[i][j] = alternate[i][j];
			}
		}
	}
	
	/**
	 * search()
	 * Takes a specific grid index and searches the surrounding 8 indices
	 * for their occupancy. Updates result grid accordingly.
	 * @param i			y value
	 * @param j			x value
	 */
	public void search(int i, int j)
	{
		//Y'all already know what's going on
		int sumOfSurrounding = 
			grid[i-1][j-1] + 	//top left
			grid[i-1][j] + 		//top
			grid[i-1][j+1] + 	//top right
			grid[i][j-1] +		//left
			grid[i][j+1] +		//right
			grid[i+1][j-1] +	//bottom left
			grid[i+1][j] +		//bottom
			grid[i+1][j+1];		//bottom right
		
		//Do something with the results
		if(sumOfSurrounding == 3)
		{
			alternate[i][j] = 1;
		}
		else if(sumOfSurrounding == 2)
		{
			if(grid[i][j] == 0)
			{
				alternate[i][j] = 0;
			}
			else
			{
				alternate[i][j] = 1;
			}
		}
		else if(sumOfSurrounding < 2)
		{
			alternate[i][j] = 0;
		}
		else if(sumOfSurrounding > 3)
		{
			alternate[i][j] = 0;
		}
	}
	
	//I'm not writing a whole block comment for this but yes good size
    public Dimension getPreferredSize()
    {
        return new Dimension(grid.length * 4, grid[0].length * 4);
    }
	
	/**
	 * paintComponent
	 * Not sure if I needed a method comment but just to be safe:
	 * This thing repaints things, according to the grid.
	 * 
	 * @param g		what we're gonna be painting
	 */
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        
        //Fill in the grid accordingly: Every 1 equals a 4x4 square at
        //the grid coordinates x 4
        //Ignore padding
        for (int i = 1; i < grid.length - 1; i++)
        {
            for (int j = 1; j < grid[i].length - 1; j++)
            {
                if (grid[i][j] == 1)
                {
                    g.setColor(Color.blue);
                    g.fillRect(j * 4, i * 4, 4, 4);
                }
            }
        }
	}
	
	public static void main(String[] args){
		
		//Ah yes, graphics
		JFrame frame = new JFrame(); //Playing frame
		JPanel panel = new JPanel(); //Little bottom strip that holds the start button
		JButton start = new JButton("esketit");
		JButton stop = new JButton("wait nah");
		JButton clear = new JButton("clear");
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(start);
		panel.add(stop);
		panel.add(clear);
		frame.getContentPane().add(life, BorderLayout.PAGE_START);
		frame.getContentPane().add(panel, BorderLayout.PAGE_END);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);		//can't be too safe can we
		
		//Starts iterating
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				timer.start();
			}
		});
		
		//Stops iterating
		stop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				timer.stop();
			}
		});
		
		//Clears playing field
		clear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for(int i = 0; i < grid.length; i++)
				{
					for(int j = 0; j < grid[i].length; j++)
					{
						grid[i][j] = 0;
					}
				}
				
				for(int i = 0; i < grid.length; i++)
				{
					for(int j = 0; j < grid[i].length; j++)
					{
						alternate[i][j] = 0;
					}
				}
				life.repaint();
			}
		});
		
		//This is for the user initialization: You choose where the starting
		//"live dots" are
		life.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				//User input: god bless integer division
				//Clusters all 16 on-screen pixels into one clickable square
				//Clicking on any of the 16 pixels in that square toggles
				//the same array indices which is awesome
				if(e.getY() / 4 < grid.length)
				{
					if(grid[e.getY()/4][e.getX()/4] == 1)
					{
						grid[e.getY()/4][e.getX()/4] = 0;
					}
					else
					{
						grid[e.getY()/4][e.getX()/4] = 1;
					}
					life.repaint();
				}
				
				//Inefficiently sets the border/padding to be 0
				for(int i=0;i<grid.length;i++)
				{
					grid[0][i] = 0;
					grid[grid.length-1][i] = 0;
					grid[i][0] = 0;
					grid[i][grid.length-1] = 0;
				}
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
