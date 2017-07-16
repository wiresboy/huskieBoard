/**
 * Opens/creates a new file on the SD card with 
 * the name of the passed in string (the file name 
 * must conform to FAT 8.3 conventions)
 */
package huskieBoard.commands;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 * Open a file on the SD card with the given name.
 */
public abstract class OpenLogCommand extends Command {

	private static final byte commandByte = 0x03;
	private byte[] openLogBytes = null;
	
	/**
	 * @param fileName Name of file to open. String must conform to FAT 8.3 formatting.
	 * @param priority Priority of this command. 
	 */
	public OpenLogCommand(String fileName, int priority) {
		super(priority);
		// TODO: sanity check on file name
		byte len = (byte) fileName.length();
		openLogBytes = appendChecksum(concatenateByteArrays(new byte[]{commandByte, len}, stringToByteArray(fileName)));
	}

	/**
	 * @param fileName Name of file to open. String must conform to FAT 8.3 formatting.
	 */
	public OpenLogCommand(String fileName) {
		this(fileName, kDefaultPriority);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		return openLogBytes;
	}
	
	
	@Override
	public boolean handleResponse(HuskieBoard board) {
		// No response expected
		return true;
	}


}
