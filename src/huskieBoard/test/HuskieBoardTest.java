/**
 * HuskieBoardTest.java
 * 
 */
package huskieBoard.test;

import java.time.*;
import java.util.Date;

import huskieBoard.*;
import huskieBoard.commands.*;

/**
 * @author BrandonJ
 * Test a huskieboard connected to a local USB port
 */
public class HuskieBoardTest {

	public static void main(String[] args) {
		HuskieBoard board = HuskieBoard.getInstance();

		/*for (int len = 1; len<=124; len++)
		{
			int tmp = 0;
			for (int i = 0; i<1000; i++)
			{
				if (testLCD(board, "1234567890asdfghjklqwertyuiopzxcvbnmASDFGHJKLZXCVBNMQWERTYUIOP1234567890asdfghjklqwertyuiopzxcvbnmASDFGHJKLZXCVBNMQWERTYUIOP".substring(len)))
					tmp ++;
			}
			System.out.println("Finished test for len: "+len+". Passes: "+tmp+ " (out of 1000)");
		}*/
		
		testLCD(board, "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");// fails ~50% of the time
		testLCD(board, "2");	//Short ones don't seem to ever fail though ?
		
		//testSDCard(board);
		
		System.out.println("Ran successfully??");
	}
	
	
	public static boolean testLCD(HuskieBoard board, String displayString)
	{
		try {			
			Command c = new ConfigureLCDCommand(2); //2 lines
			board.sendCommand(c);
			//System.out.println("Configured LCD to be 2 lines");
			c = new DisplayLCDCommand(displayString);
			board.sendCommand(c);
			//System.out.println("Wrote to LCD: \"" + displayString + "\"");
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	public static boolean testSDCard(HuskieBoard board)
	{
		try {
			Command c = new SetTimeCommand();
			board.sendCommand(c);
			System.out.println("Set the time");
			
			String fileName = String.valueOf(Date.from(Instant.now()).getTime());
			fileName = fileName.substring(fileName.length()-8);
			c = new OpenLogCommand(fileName);
			board.sendCommand(c);
			System.out.println("Opened SD card file - "+fileName);
			
			c = new LogStringCommand("Test Line #1 - "+Date.from(Instant.now()).toString() + "\n");
			board.sendCommand(c);
			c = new LogStringCommand("Test Line #2 - "+Date.from(Instant.now()).toString() + "\n");
			board.sendCommand(c);
			c = new LogStringCommand("Test Line #3 - "+Date.from(Instant.now()).toString() + "\n");
			board.sendCommand(c);
			
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
