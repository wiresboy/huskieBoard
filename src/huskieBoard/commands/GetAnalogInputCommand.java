/**
 * GetAnalogInputCommand.java
 * Read a single analog value 
 */
package huskieBoard.commands;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon John
 *
 */
public class GetAnalogInputCommand extends Command {

	private static final byte commandByte = (byte)0x11;
	private static final double kRawToVoltageScalingFactor = 3.3/4096;

	private int channel;
	private int rawValue;
	private double voltage;
	
	/**
	 * GetAnalogInputCommand(channel)
	 */
	public GetAnalogInputCommand(int channel) {
		super();
		this.channel = channel;
	}

	@Override
	public byte[] getCommandByteArray() {
		return appendChecksum(new byte[] {commandByte, (byte) channel});
	}

	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		try {
			byte[] responseBytes = board.serialRead(4);
			if ((responseBytes.length == 4) && (responseBytes[0] == commandByte) && (checkChecksum(responseBytes)))
			{
				rawValue = ((((int)responseBytes[2]&0xFF)<<8)
						 +   ((int)responseBytes[1]&0xFF)    );	//TODO: Make this cleaner
				
				if ((rawValue & 0xF000) != 0)//Only 12 bits are valid, if any of first 4 bits are set, then invalid response.
				{
					throw new Exception("Response for " + this.getClass().getSimpleName() + " was not valid. Value too large.\n\t" + 
										"Received:  0x" + bytesToHexString(responseBytes));
				}
				
				voltage = rawValue * kRawToVoltageScalingFactor;//Scaling factor for raw->voltage conversion
				return true; //Success!
			}
			else
			{
				throw new Exception("Response for " + this.getClass().getSimpleName() + " was not valid. \n\t" + 
									"Received:  0x" + bytesToHexString(responseBytes));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @return the rawValue
	 */
	public int getRawValue() {
		return rawValue;
	}

	/**
	 * @return the voltage
	 */
	public double getVoltage() {
		return voltage;
	}

}
