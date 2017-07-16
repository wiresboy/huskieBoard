/**
 * 
 */
package huskieBoard.commands;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 *
 */
public class SetTimeCommand extends Command {

	private static final byte commandByte = 0x05;
	
	private Instant currentTime; //The instant in time to set the HuskieBoard's time to - if None, it will be determined when the time is transmitted for accuracy purposes.
	/**
	 * Set the time to a specified time, used for date modified on the SD card.
	 * @param priority
	 */
	public SetTimeCommand(int priority, Instant time) {
		super(priority);
		this.currentTime = time;
		//Note: The timestamp is generated on request instead of during instantiation, so that it will be as accurate as possible when it does get written.
	}
	
	/**
	 * Set the time, used for date modified on the SD card.
	 * @param priority
	 */
	public SetTimeCommand(int priority) {
		this(priority, null); //TODO: Get current timestamp?
		//Note: The timestamp is generated on request instead of during instantiation, so that it will be as accurate as possible when it does get written.
	}

	/**
	 * Set the time, used for date modified on the SD card.
	 */
	public SetTimeCommand() {
		this(kDefaultPriority);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 * The FAT32 date/time format is a bitmask:

               24                16                 8                 0
+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+
|Y|Y|Y|Y|Y|Y|Y|M| |M|M|M|D|D|D|D|D| |h|h|h|h|h|m|m|m| |m|m|m|s|s|s|s|s|
+-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+ +-+-+-+-+-+-+-+-+
 \___________/\________/\_________/ \________/\____________/\_________/
     year       month       day        hour       minute      second

The year is really the number of years since 1980. (so 2017 => 37)
Seconds are stored in two-second increments. 
	 */
	@Override
	public byte[] getCommandByteArray() {
		// TODO Generate the time as a FAT32 timestamp, then write to output.
		if (currentTime == null)
			currentTime = Instant.now();//TODO: Determine if this works in a competition environment on the RoboRIO
		Date date = Date.from(currentTime);
		
		//TODO: getXxxx() are deprecated
		int year = date.getYear() - 80; //This is the number of years since 1980
		int month = date.getMonth();
		int day = date.getDate();
		int hour = date.getHours();
		int minute = date.getMinutes();
		int second = date.getSeconds();
		
		
		byte [] b = new byte[6];
		b[0] = commandByte;
		b[1] = (byte)((year*2)+(month/8)); //Year + MSB of month
		b[2] = (byte)(((month%8)*32)+day); //3LSB of month + day
		b[3] = (byte)((hour*8) + (minute/8));//hour + 3MSB of minute
		b[4] = (byte)(((minute%8)*32) + (second/2)); //3LSB of minute + seconds/2 (only accurate to nearest even seconds
		b[5] = 0; //So that it does not affect the checksum calculation
		b[5] = sumByteArray(b); //Update the checksum
		return null;
	}

	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		// No response expected
		return true;
	}

}
