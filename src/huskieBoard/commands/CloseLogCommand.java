/**
 * CloseLogCommand.java
 * Flushes all buffers then closes the log file on the SD card. 
 * Prepares to log a new file. If new log string commands or 
 * header commands are sent, opens a default file name. 
 * Otherwise it waits for a 0x03 Set SD Log Title command.
 */
package huskieBoard.commands;

/**
 * @author Brandon John
 * Close the currently opened log file
 */
public class CloseLogCommand extends Command {

	private static final byte commandByte = 0x04;
	
	/**
	 * @param command
	 * @param priority
	 */
	public CloseLogCommand(int priority) {
		super(priority);
	}

	/**
	 * @param command
	 */
	public CloseLogCommand() {
		super(commandByte);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		return new byte[] {commandByte, commandByte};
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
