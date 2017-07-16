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
		// TODO Auto-generated method stub
		HuskieBoard board = HuskieBoard.getInstance();
		try {
			
			//Write some bytes
			int bytesWritten = board.serialPortRef.writeString("asdf");
			System.out.println("Bytes written:"+bytesWritten);
			System.out.println("Read buffer: "+board.serialPortRef.readString());
			
			Command c = new ConfigureLCDCommand(2);
			board.sendCommand(c);
			System.out.println("Configured LCD to be 2 lines");
			c = new DisplayLCDCommand("asdffdsa");
			board.sendCommand(c);
			System.out.println("Wrote LCD");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Ran successfully??");
	}

}
