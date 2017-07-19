/**
 * CloseLogCommand.java
 * Flushes all buffers then closes the log file on the SD card. 
 * Prepares to log a new file. If new log string commands or 
 * header commands are sent, opens a default file name. 
 * Otherwise it waits for a 0x03 Set SD Log Title command.
 */
package huskieBoard.commands;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 * Close the currently opened log file
 */
public class CloseLogCommand extends Command {

	private static final byte commandByte = 0x04;
	
	/**
	 * @param priority
	 */
	public CloseLogCommand(int priority) {
		super(priority);
	}

	/**
	 * Close the open log file on the HuskieBoard.
	 */
	public CloseLogCommand() {
		super(kDefaultPriority);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		return new byte[] {commandByte, commandByte};
	}

	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		// No response expected
		return true;
	}

}
