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
	 * Send 
	 * @param c
	 * @throws Exception when the command validation fails.
	 */
	public void sendCommand(Command c) throws Exception
	{
		return;
	}
	
	

}
