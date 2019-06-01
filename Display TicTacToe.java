/**
 * @(#)Display TicTacToe.java
 *
 *
 * @author
 * @version 1.00 2019/5/31
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Display TicTacToe {

	static final String DATABASE_URL = "jdbc:mysql://localhost/tictactoe";

	public static void main(String args[])
	{
		Connection connection = null;
		Statement stament = null;
		ResultSet resultSet = null;

		try
		{
			connection = DriverManager.getConnection( DATABASE_URL, "root", "");

			statement = connection.createStatement();

			resultSet = statement.executeQuery( "SELECT consecutive wins, FROM  );

			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberofColumns = metaData.getColumnCount();
			System.out.println (" TicTacToe Table Database:\n");

			for ( int i = 1; i <numberOfColumns; i++)
				System.out.printf("%-8s\t", metaData.getColumnName(i));
			System.out.println();

			while (resultSet.next())
			{
				for (int i = 1; i <=numberOfColumns; i++)
					System.out.printf("%-8s\t", resultSet.getObject (i));
				System.out.println();
			}
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
				connection.close();
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
}