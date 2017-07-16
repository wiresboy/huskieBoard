/**
 * Takes the passed in string and displays it to the LCD attached 
 * to The Huskie Board.
 */
package huskieBoard.commands;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 * Display a string on an attached LCD
 */
public class DisplayLCDCommand extends Command {

	private static final byte commandByte = 0x08;
	private byte[] displayLCDBytes = null;
	
	/**
	 * TODO: documentation on formatting of the LCD screen. Grab from LV code? Link to Parallax?
	 * @param s String of data to send to the LCD screen
	 * @param priority
	 */
	public DisplayLCDCommand(byte[] s, int priority) throws Exception {
		super(priority);
		if (s.length>247)
			throw new Exception("LCD string length must be less than or equal to 247 characters.");//TODO: Make custom/better exception.
		byte len = (byte) s.length;
		displayLCDBytes = appendChecksum(concatenateByteArrays(new byte[]{commandByte, len}, s));//TODO:Make this line readable
	}
	
	/**
	 * @param s String of data to send to the LCD screen
	 * @param priority
	 * @throws Exception 
	 */
	public DisplayLCDCommand(String s, int priority) throws Exception {
		this(stringToByteArray(s), priority);
	}
	
	/**
	 * @param s String of data to send to the LCD screen
	 * @throws Exception 
	 */
	public DisplayLCDCommand(String s) throws Exception {
		this(s, kDefaultPriority);
	}
	
	/**
	 * @param s String of data to send to the LCD screen
	 * @throws Exception 
	 */
	public DisplayLCDCommand(byte[] s) throws Exception {
		this(s, kDefaultPriority);
	}
	
	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		return displayLCDBytes;
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#handleResponse(HuskieBoard)
	 * Return true on success
	 */
	@Override
	public boolean handleResponse(HuskieBoard board) {
		return handleSimpleResponse(board,new byte[]{commandByte, commandByte});
	}

}
