/**
 * Abstract class for all 1 way (transmit-only) commands to be sent from the RoboRIO to the Huskie Board
 */
package huskieBoard.commands;

/**
 * @author Brandon John
 * Extend this class for all 1 way (transmit-only) commands to be sent from the RoboRIO to the Huskie Board.
 * By nature, this class should be queueable
 * TODO: a subclass that allows for responses to commands like "getAnalogInput", etc.
 */
public abstract class Command {
	
	protected int priority;
	protected byte command;
	
	/**
	 * @param command_ Byte value of the command
	 * @param priority_ Higher priorities mean the command will be sent earlier when a prioritizing queue is implemented.
	 */
	public Command(byte command_, int priority_) {
		command = command_;
		priority = priority_;
	}
	
	/**
	 * @param command_ Byte value of the command
	 */
	public Command(byte command_) {
		command = command_;
		priority = 0;
	}
	
	/**
	 * Get the priority of this command. Higher priorities mean the command should be sent earlier
	 * @return priority
	 */
	public int getPriority()
	{
		return priority;
	}
	
	
	
	//TODO: javadoc for these abstract methods
	public abstract byte[] getCommandByteArray();
	
	public abstract int getExpectedResponseLength(); //note: length of 0 means that validateResponse will not be called.
	public abstract boolean validateResponse(byte[] response);
	
	
	
	//Utils:
	
	/**
	 * Generate the checksum for a byte array.
	 * @param bytes
	 * @return sum of the bytes mod 256, for purposes of the checksum
	 */
	protected static byte sumByteArray(byte[] bytes)
	{
		byte sum = 0;
		for (byte b : bytes)
			sum += b;
		return sum;
	}
	
	/**
	 * Check the checksum in a byte string. 
	 * @param bytes
	 * @return True: Checksum matches; False: Checksum does not match.
	 */
	protected static boolean checkChecksum(byte[] bytes)
	{
		int len = bytes.length;
		byte providedSum = bytes[len - 1];
		byte runningSum = 0;
		for (int i=0; i<len-1; i++) //Add up sum of all bytes except for the last one, which is the checksum
			runningSum += bytes[i];
		return providedSum==runningSum;	
	}
}
