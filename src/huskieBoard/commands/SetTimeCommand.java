/**
 * 
 */
package huskieBoard.commands;

import java.time.*;
import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 *
 */
public class SetTimeCommand extends Command {

	private static final byte commandByte = 0x05;
	
	private Instant time; //The instant in time to set the HuskieBoard's time to - if None, it will be determined when the time is transmitted for accuracy purposes.
	/**
	 * Set the time to a specified time, used for date modified on the SD card.
	 * @param priority
	 */
	public SetTimeCommand(int priority, Instant time) {
		super(priority);
		this.time = time;
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
    year        month       day      hour       minute        second

The year is really the number of years since 1980. (so 2017 => 37)
Seconds are stored in two-second increments. 
	 */
	@Override
	public byte[] getCommandByteArray() {
		// TODO Generate the time as a FAT32 timestamp, then write to output.
		if (time == null)
			time = Instant.now();//TODO: Determine if this works in a competition environment on the RoboRIO

		byte [] b = new byte[6];
		b[0] = commandByte;
		b[1] = 0;
		
		b[5] = 0; //So that it does not affect the checksum calculation
		b[5] = sumByteArray(b); //Update the checksum
		return null;
	}

	@Override
	public boolean handleResponse(HuskieBoard board) {
		// No response expected
		return true;
	}

}
