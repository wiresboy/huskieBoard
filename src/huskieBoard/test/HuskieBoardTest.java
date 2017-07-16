/**
 * HuskieBoardTest.java
 * 
 */
package huskieBoard.test;

import huskieBoard.*;
import huskieBoard.commands.*;

/**
 * @author BrandonJ
 * Test a huskieboard connected to a local USB port
 */
public class HuskieBoardTest {

	public static void main(String[] args) {
		HuskieBoard board = HuskieBoard.getInstance();

		testLCD(board, "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");// fails ~50% of the time
		testLCD(board, "2");	//Short ones don't seem to ever fail though ?

		System.out.println("Ran successfully??");
	}
	
	
	public static boolean testLCD(HuskieBoard board, String displayString)
	{
		try {			
			Command c = new ConfigureLCDCommand(2); //2 lines
			board.sendCommand(c);
			System.out.println("Configured LCD to be 2 lines");
			c = new DisplayLCDCommand(displayString);
			board.sendCommand(c);
			System.out.println("Wrote to LCD: \"" + displayString + "\"");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
