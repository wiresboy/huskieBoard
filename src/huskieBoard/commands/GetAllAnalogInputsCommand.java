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
public class GetAllAnalogInputsCommand extends Command {

	private static final byte commandByte = (byte)0x12;
	private static final double kRawToVoltageScalingFactor = 3.3/4096;
	
	private int[] rawValue = new int[8];
	private double[] voltage = new double[8];
	
	/**
	 * GetAnalogInputCommand(channel)
	 * 
	 */
	public GetAllAnalogInputsCommand() {
		super();
	}

	@Override
	public byte[] getCommandByteArray() {
		return new byte[] {commandByte, commandByte};
	}

	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		try {
			byte[] responseBytes = board.serialRead(14);
			if ((responseBytes.length == 14) && (responseBytes[0] == commandByte) && (checkChecksum(responseBytes)))
			{
				for (int channel=0, byteIndex=1; channel<8; channel+=2, byteIndex+=3)
				{
					rawValue[channel]   = ((int)responseBytes[byteIndex]   & 0xFF) << 4 | ((int)responseBytes[byteIndex+1] & 0xF0)<<4;	//TODO: Cleanup
					rawValue[channel+1] = ((int)responseBytes[byteIndex+1] & 0x0F) << 8 | ((int)responseBytes[byteIndex+2] & 0xFF);
					voltage[channel]   = rawValue[channel]   * kRawToVoltageScalingFactor;
					voltage[channel+1] = rawValue[channel+1] * kRawToVoltageScalingFactor;
				}
				
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
	 * @return the raw value of the specified channel. 1 to 8.
	 */
	public int getRawValueChannel(int channel) {
		return rawValue[channel];
	}

	/**
	 * @return the voltage of the specified channel (1 to 8).
	 */
	public double getVoltageChannel(int channel) {
		return voltage[channel];
	}
	
	/**
	 * @return array of raw values
	 */
	public int[] getRawValues() {
		return rawValue;
	}
	
	/**
	 * @return array of voltages
	 */
	public double[] geVoltages() {
		return voltage;
	}

}
