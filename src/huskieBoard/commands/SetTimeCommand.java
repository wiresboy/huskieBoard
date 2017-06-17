/**
 * 
 */
package huskieBoard.commands;

/**
 * @author Brandon John
 *
 */
public class SetTimeCommand extends Command {

	private static final byte commandByte = 0x05;
	
	/**
	 * Set the time, used for date modified on the SD card.
	 * @param priority
	 */
	public SetTimeCommand(int priority) {
		super(commandByte, priority);
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
	 */
	@Override
	public byte[] getCommandByteArray() {
		// TODO Generate the time as a FAT32 timestamp, then write to output.
		return null;
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getExpectedResponseLength()
	 */
	@Override
	public int getExpectedResponseLength() {
		return 0; //No response expected
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#validateResponse(byte[])
	 */
	@Override
	public boolean validateResponse(byte[] response) {
		return false; //No response expected
	}

}
