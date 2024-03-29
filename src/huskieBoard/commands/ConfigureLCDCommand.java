/**
 * Configure the size of the LCD screen attached to the Huskie Board
 */
package huskieBoard.commands;

import java.util.Arrays;

/**
 * @author Brandon John
 * Configure the size of the LCD screen attached to the Huskie Board
 */
public class ConfigureLCDCommand extends Command {

	private static final byte commandByte = 0x09;
	private byte[] configureLCDBytes = null;
	
	/**
	 * @param lines How large is the LCD display (options: 2,4 rows)
	 * @param priority
	 * @throws Exception 
	 */
	public ConfigureLCDCommand(int lines, int priority) throws Exception{
		super(priority);
		if (lines == 2)
			configureLCDBytes = new byte[] {commandByte, 0x02, commandByte+0x02};
		else if (lines == 4)
			configureLCDBytes = new byte[] {commandByte, 0x04, commandByte+0x04};
		else
			throw new Exception("Only 2 and 4 line displays are accepted");
	}

	/**
	 * @param lines How large is the LCD display (options: 2,4)
	 * @throws Exception 
	 */
	public ConfigureLCDCommand(int lines) throws Exception {
		this(lines, kDefaultPriority);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		return configureLCDBytes;
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getExpectedResponseLength()
	 */
	@Override
	public int getExpectedResponseLength() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#validateResponse(byte[])
	 */
	@Override
	public boolean validateResponse(byte[] response) {
		if (Arrays.equals(response,new byte[]{commandByte, commandByte}))
			return true;
		else
			return false;
	}

}
