/**
 * Logs the passed in string to the SD on the currently opened 
 * file. If no file has been opened, then it will not log. 
 */
package huskieBoard.commands;

/**
 * @author BrandonJ
 * Logs a string to the SD card. Requires that a file is already open.
 */
public class LogStringCommand extends Command {
	
	private static final byte commandByte = 0x01;
	private byte[] logLineBytes = null;
	
	/**
	 * Log a line to the SD card. Maximum length of each command is 250 bytes, so the maximum length of each line is 247 characters.
	 * @param logLine 
	 * @param priority
	 * @throws Exception 
	 */
	public LogStringCommand(String logLine, int priority) throws Exception {
		super(priority);
		if (logLine.length()>247)
			throw new Exception("Log line string length must be less than or equal to 247 characters.");//TODO: Make custom/better exception.
		byte len = (byte) logLine.length();
		logLineBytes = appendChecksum(concatenateByteArrays(new byte[]{commandByte, len}, stringToByteArray(logLine)));//TODO:Make this line readable
	}

	/**
	 * @param logLine
	 * @throws Exception 
	 */
	public LogStringCommand(String logLine) throws Exception {
		this(logLine, kDefaultPriority);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		return logLineBytes;
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
