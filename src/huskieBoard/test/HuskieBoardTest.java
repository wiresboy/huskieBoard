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
		
		//testLCD(board, "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");// fails ~50% of the time
		//testLCD(board, "2");	//Short ones don't seem to ever fail though ?
		
		//testSDCard(board);
		testSetLEDValue(board);
		//testGetFWVersion(board);
		//testGetAnalogInput(board);
		//testGetAllAnalogInput(board);
		
		System.out.println("Ran successfully?? \nWaiting 10 seconds before closing.");
		delay(10);
		System.out.println("Done");
	}
	
	public static boolean testGetFWVersion(HuskieBoard board)
	{
		try {
			GetFirmwareVersionCommand c = new GetFirmwareVersionCommand();
			board.sendCommand(c);
			if (c.getStatus() == Command.Status.SENT_SUCCESS)
			{
				System.out.println("BoardVersion is: "+c.getVersion());
				if (c.getVersion().equals(c.new HuskieBoardVersion(1,4,3,0)))
				{
					return true;
				}
				else
				{
					System.err.println("Board version not 1.4.3.0 ");
				}
			}
			else
			{
				System.err.println("GetFirmwareVersionCommand not sent successfully.");
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean testGetAnalogInput(HuskieBoard board)
	{
		try {
			boolean success = true;
			for (int i = 0; i < 8; i++)
			{
				GetAnalogInputCommand c = new GetAnalogInputCommand(i);
				board.sendCommand(c);
				if (c.getStatus()!=Command.Status.SENT_SUCCESS)
				{
					success = false;
				}
				else
				{
					double voltage = c.getVoltage();
					System.out.println("GetAnalogInputSingle - channel "+i+": "+voltage+" volts");
					if (voltage<0 || voltage > 3.3)
					{
						success = false;
					}
				}
			}
			return success;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean testGetAllAnalogInput(HuskieBoard board)
	{
		try {
			boolean success = true;
			GetAllAnalogInputsCommand c = new GetAllAnalogInputsCommand();

			board.sendCommand(c);
			
			if (c.getStatus()==Command.Status.SENT_SUCCESS)
			{
				for (int i = 0; i < 8; i++)
				{
					double voltage = c.getVoltageChannel(i);
					System.out.println("GetAnalogInputAll - channel "+i+": "+voltage+" volts");
					if (voltage<0 || voltage > 3.3)
					{
						success = false;
					}
				}
			}
			else
			{
				success = false;
			}
			
			return success;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
	
	public static boolean testSetLEDValue(HuskieBoard board)
	{
		try {
			Command c = new ConfigureLEDStripCommand(	ConfigureLEDStripCommand.HuskieLEDPin.GPIO_0, 
														ConfigureLEDStripCommand.HuskieLedMode.WS2812, 
														20);
			board.sendCommand(c);
			System.out.println("Configured LED Strip");
			
			
			c = new SetLEDRangeToValueCommand(255,255,255,0,10);
			board.sendCommand(c);
			System.out.println("Set LEDs 0..10 to be white");
			delay(1);
			
			c = new SetLEDRangeToValueCommand(255,0,255,0,10);
			board.sendCommand(c);
			System.out.println("Set LEDs 0..10 to be purple");
			/*delay(1);
			
			while(true)//Loop forever for fun :)
			{
				for (int x=0; x<255; x++)
				{
					board.sendCommand(new SetLEDRangeToValueCommand(x, 255-x, 0, 0, 10));
					delay(.0125);
				}

				for (int x=0; x<255; x++)
				{
					board.sendCommand(new SetLEDRangeToValueCommand(255-x, 0, x, 0, 10));
					delay(.0125);
				}

				for (int x=0; x<255; x++)
				{
					board.sendCommand(new SetLEDRangeToValueCommand(0, x, 255-x, 0, 10));
					delay(.0125);
				}
			}*/
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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

	private static void delay(double seconds)
	{
		try{
			Thread.sleep((int)(seconds*1000));
		}
		catch (Exception e)
		{
		}
	}
}
