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
public class HuskieBoard {

	private SerialPort serialPortRef = null;
	
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
	 * @author Brandon John
	 * @throws Exception(): Only 1 Huskie Board object can be instantiated at a time as there is only 1 serial port available
	 * Instantiates a HuskieBoard() object
	 * Configure the serial port.
	 * There should be a maximum of 1 instance of this object
	 */
	public HuskieBoard() throws Exception {
		if (serialPortRef == null)
		{
			serialPortRef = new SerialPort(kSerialPortBaudRate, kSerialPortPort, kSerialPortDataBits, kSerialPortParity, kSerialPortStopBits);
			serialPortRef.disableTermination();
			serialPortRef.setTimeout(kSerialPortTimeout);
			serialPortRef.setFlowControl(kSerialPortFlowControl);
			serialPortRef.setWriteBufferMode(kSerialPortBufferMode);
		}
		else
			throw new Exception("Only 1 Huskie Board can be instatiated at a time");//TODO: Define custom exceptions
	}
	
	/**
	 * Flushes, and then closes, the serial port
	 */
	public void close()
	{
		serialPortRef.flush();
		serialPortRef.free();
		serialPortRef = null;
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
