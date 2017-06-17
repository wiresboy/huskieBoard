/**
 * Abstract class for all 1 way (transmit-only) commands to be sent from the RoboRIO to the Huskie Board
 */
package huskieBoard.commands;

import java.io.ByteArrayOutputStream;

/**
 * @author Brandon John
 * Extend this class for all 1 way (transmit-only) commands to be sent from the RoboRIO to the Huskie Board.
 * By nature, this class should be queueable
 * TODO: a subclass that allows for responses to commands like "getAnalogInput", etc.
 */
public abstract class Command {
	
	protected int priority;
	protected byte command;
	protected static final int kDefaultPriority = 0;

	/**
	 * @param command Byte value of the command
	 * @param priority Higher priorities mean the command will be sent earlier when a prioritizing queue is implemented.
	 */
	public Command(byte command, int priority) {
		this.command = command;
		this.priority = priority;
	}
	
	/**
	 * @param command Byte value of the command
	 */
	public Command(byte command) {
		this(command, kDefaultPriority);
	}
	
	/**
	 * Get the priority of this command. Higher priorities mean the command should be sent earlier
	 * @return priority
	 */
	public int getPriority()
	{
		return priority;
	}
	
	
	
	/**
	 * Get the byte array to be sent to the HuskieBoard
	 * @return the byte array to be sent
	 */
	public abstract byte[] getCommandByteArray();
	
	/**
	 * Get the expected response length. This will determine how many bytes will be read and then sent to validateResponse
	 * A length of 0 means that no response is expected, and validateResponse will not be called.
	 * @return number of bytes
	 */
	public abstract int getExpectedResponseLength();
	
	/**
	 * Validate the response to the command that was just sent. 
	 * @param response
	 * @return True on success, false on invalid response
	 */
	public abstract boolean validateResponse(byte[] response); //TODO: Possibly throw an exception when there is a problem with the response?
	
	
	
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
	 * Append the checksum to a byte array
	 * @param bytes
	 * @return full command
	 */
	protected static byte[] appendChecksum(byte[] bytes)
	{
		byte sum = sumByteArray(bytes);
		return concatenateByteArrays(bytes, new byte[]{sum});
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
	
	/**
	 * Convert a string into a byte array
	 * @param s string
	 * @return byte array representation of the string
	 */
	protected static byte[] stringToByteArray(String s)
	{
		return s.getBytes();
	}
	
	/**
	 * Append byte arrays into a single array
	 * @param byte arrays
	 * @return appended byte arrays
	 */
	protected static byte[] concatenateByteArrays(byte[]... byteArrays) {
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    for (byte[] b : byteArrays) {
	        os.write(b, 0, b.length);
	    }
	    return os.toByteArray();
	}
}
