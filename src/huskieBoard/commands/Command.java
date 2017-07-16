/**
 * Abstract class for all 1 way (transmit-only) commands to be sent from the RoboRIO to the Huskie Board
 */
package huskieBoard.commands;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 * Extend this class for all 1 way (transmit-only) commands to be sent from the RoboRIO to the Huskie Board.
 * By nature, this class should be queueable
 * TODO: a subclass that allows for responses to commands like "getAnalogInput", etc.
 */
public abstract class Command {
	
	private int priority;
	protected static final int kDefaultPriority = 0;

	/**
	 * @param command Byte value of the command
	 * @param priority Higher priorities mean the command will be sent earlier when a prioritizing queue is implemented.
	 */
	public Command(int priority) {
		this.priority = priority;
	}
	
	/**
	 * @param command Byte value of the command
	 */
	public Command() {
		this(kDefaultPriority);
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
	 * Set the priority of this command. Generally will be set by the instantiator method.
	 * @param priority
	 */
	protected void setPriority(int priority)
	{
		this.priority = priority;
	}
	
	
	/**
	 * Get the byte array to be sent to the HuskieBoard
	 * @return the byte array to be sent
	 */
	public abstract byte[] getCommandByteArray();
	
	/**
	 * Get the expected response length. This will determine how many bytes will be read and then sent to validateResponse
	 * A length of 0 means that no response is expected, and validateResponse will not be called.
	 * ***This method is being removed soon.***
	 * @return number of bytes
	 */
	//public abstract int getExpectedResponseLength();
	
	/**
	 * Validate the response to the command that was just sent. This method is being removed soon.
	 * @param response
	 * @return True on success, false on invalid response
	 */
	//public abstract boolean validateResponse(byte[] response); //TODO: Possibly throw an exception when there is a problem with the response?
	
	
	/**
	 * Parse, validate, and handle the response from the HuskieBoard.
	 * The HuskieBoard object is passed so that the command may use the getters/setters to
	 * access the serial port functionality, so that if more complex processing than "read x bytes" 
	 * is required, it can be handled fully.
	 * @param board: reference to HuskieBoard for serial port access
	 * @return True when command was successful, false if the checksum or a timeout failed.
	 */
	public abstract boolean handleResponse(HuskieBoard board);
	
	
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
	
	/**
	 * Handle simple response
	 * This can be used to handle responses that must return a pre-defined set of characters to be considered a success.
	 * @param board HuskieBoard reference for accessing serial port
	 * @param expectedBytes The byte array that is expected to be received.
	 * @return True on success, false on failure.
	 */
	public boolean handleSimpleResponse(HuskieBoard board, byte[] expectedBytes)
	{

		try {
			//length of expected response: 2
			byte[] responseBytes = board.serialRead(expectedBytes.length);
			if (Arrays.equals(responseBytes,expectedBytes))
			{
				return true; //Success!
			}
			else
			{
				throw new Exception("Response for " + this.getClass().getSimpleName() + " was not valid. \n\t" + 
									"Expecting: 0x" + bytesToHexString(expectedBytes)+"\n\t" +
									"Received:  0x" + bytesToHexString(responseBytes));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder(); 
		for(byte b : bytes)
		{
			sb.append(String.format("%02x", b&0xff));
		}
		return sb.toString();
	}
	
}
