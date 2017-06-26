/**
 * HuskieBoard.java
 * For use with Team 3061's Huskie Board.
 */
package huskieBoard;

import edu.wpi.first.wpilibj.SerialPort;
import huskieBoard.commands.Command;

/**
 * @author Brandon John
 * @author Team 3061 Huskie Robotics
 * @version 1.0
 */
public final class HuskieBoard {

	private SerialPort serialPortRef = null;
	private static HuskieBoard HuskieBoardInstance;
	
	//Serial Port requirements
	private static final int kSerialPortBaudRate = 230400;
	private static final SerialPort.Port kSerialPortPort = SerialPort.Port.kMXP;
	private static final int kSerialPortDataBits = 8;
	private static final SerialPort.Parity kSerialPortParity = SerialPort.Parity.kNone;
	private static final SerialPort.StopBits kSerialPortStopBits = SerialPort.StopBits.kOne;
	private static final double kSerialPortTimeout = 1.0; //measured in seconds
	private static final SerialPort.FlowControl kSerialPortFlowControl = SerialPort.FlowControl.kNone;
	private static final SerialPort.WriteBufferMode kSerialPortBufferMode = SerialPort.WriteBufferMode.kFlushOnAccess;
	
	/**
	 * Configures the serial port. There should be a maximum of 
	 * 1 instance of this object, so we use the Singleton pattern
	 * to make sure that only one is created. To gain access to 
	 * the HuskieBoard object, use HuskieBoard.getInstance()
	 */
	private HuskieBoard() {
			serialPortRef = new SerialPort(kSerialPortBaudRate, kSerialPortPort, kSerialPortDataBits, kSerialPortParity, kSerialPortStopBits);
			serialPortRef.disableTermination();
			serialPortRef.setTimeout(kSerialPortTimeout);
			serialPortRef.setFlowControl(kSerialPortFlowControl);
			serialPortRef.setWriteBufferMode(kSerialPortBufferMode);
	}
	
	/**
	 * Get a reference to the instance of the HuskieBoard. This
	 * follows the singleton pattern, so that only one HuskieBoard
	 * object can exist at a time.
	 * @return HuskieBoard object
	 */
	public static HuskieBoard getInstance() {
		if (HuskieBoardInstance == null) {
			HuskieBoardInstance = new HuskieBoard();//TODO: This is not thread safe!
		}
		return HuskieBoardInstance;
	}
	
	/**
	 * Flushes, and then closes, the serial port.
	 * Note: Be careful when using this in a multi-threaded environment, or where 
	 * multiple references to this object may exist. 
	 * Only use when absolutely necessary!
	 */
	public void close()
	{
		serialPortRef.flush();
		serialPortRef.free();
		serialPortRef = null;
		HuskieBoardInstance = null;
	}
	
	/**
	 * Send a command
	 * @param c Command to send
	 * @throws Exception when the command validation fails.
	 */
	public void sendCommand(Command c) throws Exception
	{
		byte[] commandByteArray = c.getCommandByteArray();//TODO: commandByteArray sanity check? Length>=2, <=255, etc.
		serialPortRef.write(commandByteArray, commandByteArray.length);
		
		int lenResponse = c.getExpectedResponseLength();
		if (lenResponse > 0)
		{
			byte[] responseBytes = serialPortRef.read(lenResponse);
			if (c.validateResponse(responseBytes))
			{
				return; //Success!
			}
			else
			{
				throw new Exception("Response was not valid");//TODO: Explain cause of exception? Received vs expected? At least which command it was?
															  //Possibly just send the exception from the command itself?
			}
		}
		return;
	}
	
	//TODO:Command with response?

}
