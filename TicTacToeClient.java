// Fig. 27.15: TicTacToeClient.java
// Client side of client/server Tic-Tac-Toe program.
// HW #5
// Denisse Espinoza

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class TicTacToeClient extends JFrame implements Runnable
{
   private TextArea display;
   private TextField field;
   private Panel board_one, board_2;
   private Square board[][], currentSquare;
   private TextField field2;
   private Socket link;
   private DataInputStream dataIn;
   private DataOutputStream output;
   private Thread outputThread;
   private JTextField idField; // textfield to display player's mark
   private JTextArea displayArea; // JTextArea to display output
   private JPanel boardPanel; // panel for tic-tac-toe board
   private JPanel panel2; // panel to hold board
   private JButton terminate;
   private Square[][] board; // tic-tac-toe board
   private Square currentSquare; // current square
   private Socket connection; // connection to server
   private Scanner input; // input from server
   private Formatter output; // output to server
   private String ticTacToeHost; // host name for server
   private String myMark; // this client's mark
   private boolean myTurn; // determines which client's turn it is
   private final String X_MARK = "X"; // mark for first client
   private final String O_MARK = "O"; // mark for second client

   // set up user-interface and board
   public TicTacToeClient( String host )
   {
      ticTacToeHost = host; // set name of server
      displayArea = new JTextArea( 4, 30 ); // set up JTextArea
      displayArea.setEditable( false );
      add( new JScrollPane( displayArea ), BorderLayout.SOUTH );

      boardPanel = new JPanel(); // set up panel for squares in board
      boardPanel.setLayout( new GridLayout( 3, 3, 0, 0 ) );

      board = new Square[ 3 ][ 3 ]; // create board

      // loop over the rows in the board
      for ( int row = 0; row < board.length; row++ )
      {
         // loop over the columns in the board
         for ( int column = 0; column < board[ row ].length; column++ )
         {
            // create square
            board[ row ][ column ] = new Square( " ", row * 3 + column );
            boardPanel.add( board[ row ][ column ] ); // add square
         } // end inner for
      } // end outer for

      idField = new JTextField(); // set up textfield
      idField.setEditable( false );
      add( idField, BorderLayout.NORTH );

      panel2 = new JPanel(); // set up panel to contain boardPanel
      panel2.add( boardPanel, BorderLayout.CENTER ); // add board panel
      add( panel2, BorderLayout.CENTER ); // add container panel

      setSize( 300, 225 ); // set size of window
      setVisible( true ); // show window

      startClient();
   } // end TicTacToeClient constructor

   // start the client thread
   public void startClient()
   {
      try // connect to server and get streams
      {
         // make connection to server
         connection = new Socket(
            InetAddress.getByName( ticTacToeHost ), 12345 );

         // get streams for input and output
         input = new Scanner( connection.getInputStream() );
         output = new Formatter( connection.getOutputStream() );
      } // end try
      catch ( IOException ioException )
      {
         ioException.printStackTrace();
      } // end catch

      // create and start worker thread for this client
      ExecutorService worker = Executors.newFixedThreadPool( 1 );
      worker.execute( this ); // execute client
   } // end method startClient

   // control thread that allows continuous update of displayArea
   public void run()
   {
      myMark = input.nextLine(); // get player's mark (X or O)

      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run()
            {
               // display player's mark
               idField.setText( "You are player \"" + myMark + "\"" );
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater

      myTurn = ( myMark.equals( X_MARK ) ); // determine if client's turn

      // receive messages sent to client and output them
      while ( true )
      {
         if ( input.hasNextLine() )
            processMessage( input.nextLine() );
      } // end while
   } // end method run

   // process messages received by client
   private void processMessage( String message )
   {
      // valid move occurred
      if ( message.equals( "Valid move." ) )
      {
         displayMessage( "Valid move, please wait.\n" );
         setMark( currentSquare, myMark ); // set mark in square
      } // end if
      else if ( message.equals( "Invalid move, try again" ) )
      {
         displayMessage( message + "\n" ); // display invalid move
         myTurn = true; // still this client's turn
      } // end else if
      else if ( message.equals( "Opponent moved" ) )
      {
         int location = input.nextInt(); // get move location
         input.nextLine(); // skip newline after int location
         int row = location / 3; // calculate row
         int column = location % 3; // calculate column

         setMark(  board[ row ][ column ],
            ( myMark.equals( X_MARK ) ? O_MARK : X_MARK ) ); // mark move
         displayMessage( "Opponent moved. Your turn.\n" );
         myTurn = true; // now this client's turn
         System.exit(0); // 161 new
      } // end else if
      else
         displayMessage( message + "\n" ); // display the message
   } // end method processMessage

		// CHANGED
	  public void init()

    {
       setLayout( new BorderLayout() );
       display = new TextArea( 4, 30 );
       display.setEditable( false );
       add( display, BorderLayout.SOUTH );

       board_one = new Panel();
       board_one.setLayout( new GridLayout( 3, 3, 0, 0 ) );
       board = new Square[ 3 ][ 3 ];

       for ( int row = 0; row < board.length; row++ )
       for ( int col = 0; col < board[ row ].length; col++ )
       {
    	   board[ row ][ col ] = new Square( ' ', row * 3 + col );
       board[ row ][ col ].addMouseListener(new SquareListener(this, board[ row ][ col ] ) );
       board_one.add( board[ row ][ col ] );

       }

       field = new TextField();
       field.setEditable( false );
       add( field, BorderLayout.NORTH );
       board_2 = new Panel();
       board_2.add( board_one );
       add( board_2, BorderLayout.CENTER );

 }

       public void start()
       {
    	   try
	       {
		     link = new Socket( InetAddress.getLocalHost(), 5000 );
	         dataIn = new DataInputStream(link.getInputStream() );
	         output = new DataOutputStream(link.getOutputStream() );
	       }
	       catch ( IOException e )
	       {
	    	   e.printStackTrace();
	       }
       		outputThread = new Thread( this );
       		outputThread.start();

       }

       public void run()
       {
    	   try
	       {
	          myMark = dataIn.readChar();
	          field.setText( "You're player: \\" + myMark + "\\" );
	       }
    	   catch ( IOException e )
	       {
	    	   e.printStackTrace();
	       }
             while ( true )
	       {
	    	   try
	          {
	    		 String s = dataIn.readUTF();
	             processMessage( s );
	          }
              catch ( IOException e )
    	   {
        	 e.printStackTrace();
            }
         }
       }

        public void processMessage( String s )
        {

        if ( s.equals( "Move OK." ) )
        {
           display.append( "Valid move, please wait.\n" );
           currentSquare.setMark( myMark );
           currentSquare.repaint();
        }
         else if ( s.equals( "Bad move, try once more." ) )
        {
         display.append( s + "\n" );
        }
         else if ( s.equals( "Other player moved" ) )
        {
        	   try
	           {
        		   int loc = dataIn.readInt();
        		   done:
	              for ( int row = 0; row < board.length; row++ )
	            	  for ( int col = 0;col < board[ row ].length; col++ )

	                   	if ( row * 3 + col == loc )
	                    {
	                    	board[ row ][ col ].setMark(( myMark == 'X' ? 'O' : 'X' ) );
	                    	board[ row ][ col ].repaint();
	                        break done;
	                    }

        		   display.append("Other player finished, your move.\n" );
	            }

        	   catch ( IOException e )
        	   {
        		   e.printStackTrace();
               }
        }
         else
        {
        	 display.append( s + "\n" );
        }

         }

        public void sendClickedSquare( int loc )
	     {
        	try
	        {
        		output.writeInt( loc );
	        }

	        catch ( IOException ie )
        	{
	        	ie.printStackTrace();
	        }
	     }

        public void setCurrentSquare( Square s )
	       {
        	currentSquare = s;
           }
}

	    class Square extends Canvas
	    {
	    	private char mark;
	    	private int location;

	    	public Square( char MARK, int LOCATION)
	    	{
		        mark = MARK;
		        location = LOCATION;
		        setSize ( 35, 35 );
	    	}

		     public void setMark( char myChar ) { mark = myChar; }
		     public int getSquareLocation() { return location; }
		     public void paint( Graphics grph )
		     {
		    	grph.drawRect( 0, 0, 29, 29 );
		        grph.drawString( String.valueOf( mark ), 11, 20 );
		     }
}

  class SquareListener extends MouseAdapter
  {
	 private TicTacToeClient applet;
     private Square mySquare;
     public SquareListener( TicTacToeClient tc, Square ss )
     {
    	applet = tc;
        mySquare = ss;
     }

     public void mouseReleased( MouseEvent e )
     {
        applet.setCurrentSquare( mySquare );
        applet.sendClickedSquare( mySquare.getSquareLocation() );
     }

  }
	/*private JButton btnExit; // new 169-179
	btnExit = new JButton("Exit");
	btnExit.addActionListner(new ActionLister(){
		public void actionPerformed(ActionEvent e){
			Container frame = btnExit.getParent();
			do
				frame = frame.getParent();
			while (!(frame instanceof JFrame));
			((JFrame) frame).dispose();
		}
	}); // modified */
	// CHANGED

   // manipulate displayArea in event-dispatch thread
   private void displayMessage( final String messageToDisplay )
   {
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run()
            {
               displayArea.append( messageToDisplay ); // updates output
            } // end method run
         }  // end inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method displayMessage

   // utility method to set mark on board in event-dispatch thread
   private void setMark( final Square squareToMark, final String mark )
   {
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run()
            {
               squareToMark.setMark( mark ); // set mark in square
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method setMark

   // send message to server indicating clicked square
   public void sendClickedSquare( int location )
   {
      // if it is my turn
      if ( myTurn )
      {
         output.format( "%d\n", location ); // send location to server
         output.flush();
         myTurn = false; // not my turn any more
      } // end if
   } // end method sendClickedSquare

   // set current Square
   public void setCurrentSquare( Square square )
   {
      currentSquare = square; // set current square to argument
   } // end method setCurrentSquare

   // private inner class for the squares on the board
   private class Square extends JPanel
   {
      private String mark; // mark to be drawn in this square
      private int location; // location of square

      public Square( String squareMark, int squareLocation )
      {
         mark = squareMark; // set mark for this square
         location = squareLocation; // set location of this square

         addMouseListener(
            new MouseAdapter()
            {
               public void mouseReleased( MouseEvent e )
               {
                  setCurrentSquare( Square.this ); // set current square

                  // send location of this square
                  sendClickedSquare( getSquareLocation() );
               } // end method mouseReleased
            } // end anonymous inner class
         ); // end call to addMouseListener
      } // end Square constructor

      // return preferred size of Square
      public Dimension getPreferredSize()
      {
         return new Dimension( 30, 30 ); // return preferred size
      } // end method getPreferredSize

      // return minimum size of Square
      public Dimension getMinimumSize()
      {
         return getPreferredSize(); // return preferred size
      } // end method getMinimumSize

      // set mark for Square
      public void setMark( String newMark )
      {
         mark = newMark; // set mark of square
         repaint(); // repaint square
      } // end method setMark

      // return Square location
      public int getSquareLocation()
      {
         return location; // return location of square
      } // end method getSquareLocation

      // draw Square
      public void paintComponent( Graphics g )
      {
         super.paintComponent( g );

         g.drawRect( 0, 0, 29, 29 ); // draw square
         g.drawString( mark, 11, 20 ); // draw mark
         repaint ();
      } // end method paintComponent
   } // end inner-class Square
} // end class TicTacToeClient

/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/