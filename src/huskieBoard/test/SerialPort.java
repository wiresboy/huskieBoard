package huskieBoard.test;

import java.io.UnsupportedEncodingException;

//import com.fazecast.jSerialComm.*; Can't double-import SerialPort

public class SerialPort {
	
	com.fazecast.jSerialComm.SerialPort localPort;
	
	public enum Port {
		kOnboard(0), kMXP(1), kUSB(2), kUSB1(2), kUSB2(3);

		public int value;

		private Port(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents the parity to use for serial communications.
	 */
	public enum Parity {
		kNone(0), kOdd(1), kEven(2), kMark(3), kSpace(4);

		public final int value;

		private Parity(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents the number of stop bits to use for Serial Communication.
	 */
	public enum StopBits {
		kOne(1), kOnePointFive(2), kTwo(3);

		public final int value;

		private StopBits(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents what type of flow control to use for serial communication.
	 */
	public enum FlowControl {
		kNone(0), kXonXoff(65536+1048576), kRtsCts(1+16), kDtsDsr(256+4096);

		public final int value;

		private FlowControl(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents which type of buffer mode to use when writing to a serial m_port.
	 */
	public enum WriteBufferMode {
		kFlushOnAccess(1), kFlushWhenFull(2);

		public final int value;

		private WriteBufferMode(int value) {
			this.value = value;
		}
	}

	
	
	
	/**
	 * SerialPort constructor for testing
	 * Ignores the port parameter, and opens a hard-coded port (currently whichever is first in the list)
	 * This is because I want this to be a drop-in piece of testing code, so I do not want to cause problems
	 * by having to change external parameters.
	 * @param baudRate
	 * @param port
	 * @param dataBits
	 * @param parity
	 * @param stopBits
	 */
	public SerialPort(int baudRate, SerialPort.Port port, int dataBits, SerialPort.Parity parity, SerialPort.StopBits stopBits)
	{
		com.fazecast.jSerialComm.SerialPort [] ports = com.fazecast.jSerialComm.SerialPort.getCommPorts();
		System.out.println(ports);
		localPort = ports[0];
		localPort.setBaudRate(baudRate);
		localPort.setNumDataBits(dataBits);
		localPort.setParity(parity.value);
		localPort.setNumStopBits(stopBits.value);
		localPort.setFlowControl(FlowControl.kNone.value);
		
	}
	
	/**
	 * Destructor.
	 */
	public void free() {
		localPort.closePort();
	}

	/**
	 * Set the type of flow control to enable on this port.
	 *
	 * <p>By default, flow control is disabled.
	 *
	 * @param flowControl the FlowControl m_value to use
	 */
	public void setFlowControl(FlowControl flowControl) {
		localPort.setFlowControl(flowControl.value);
	}

	/**
	 * Get the number of bytes currently available to read from the serial port.
	 *
	 * @return The number of bytes available to read.
	 */
	public int getBytesReceived() {
		return localPort.bytesAvailable();
	}

	/**
	 * Read a string out of the buffer. Reads the entire contents of the buffer
	 *
	 * @return The read string
	 */
	public String readString() {
		return readString(getBytesReceived());
	}

	/**
	 * Read a string out of the buffer. Reads the entire contents of the buffer
	 *
	 * @param count the number of characters to read into the string
	 * @return The read string
	 */
	public String readString(int count) {
		byte[] out = read(count);
		try {
			return new String(out, 0, out.length, "US-ASCII");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * Read raw bytes out of the buffer.
	 *
	 * @param count The maximum number of bytes to read.
	 * @return An array of the read bytes
	 */
	public byte[] read(final int count) {
		byte[] dataReceivedBuffer = new byte[count];
		localPort.readBytes(dataReceivedBuffer,  count);
		return dataReceivedBuffer;
	}

	/**
	 * Write raw bytes to the serial port.
	 *
	 * @param buffer The buffer of bytes to write.
	 * @param count  The maximum number of bytes to write.
	 * @return The number of bytes actually written into the port.
	 */
	public int write(byte[] buffer, int count) {
		return localPort.writeBytes(buffer, count);
	}

	/**
	 * Write a string to the serial port
	 *
	 * @param data The string to write to the serial port.
	 * @return The number of bytes actually written into the port.
	 */
	public int writeString(String data) {
		return write(data.getBytes(), data.length());
	}

	/**
	 * Configure the timeout of the serial m_port.
	 *
	 * <p>This defines the timeout for transactions with the hardware. It will affect reads if less
	 * bytes are available than the read buffer size (defaults to 1) and very large writes.
	 *
	 * @param timeout The number of seconds to to wait for I/O.
	 */
	public void setTimeout(double timeout) {
		localPort.setComPortTimeouts(com.fazecast.jSerialComm.SerialPort.TIMEOUT_NONBLOCKING, (int)timeout, (int)timeout);
	}
	
	/**
	 * Force the output buffer to be written to the port.
	 *
	 * <p>This is used when setWriteBufferMode() is set to kFlushWhenFull to force a flush before the
	 * buffer is full.
	 */
	public void flush() {
	}

	/**
	 * Reset the serial port driver to a known state.
	 *
	 * <p>Empty the transmit and receive buffers in the device and formatted I/O.
	 */
	public void reset() {
	}
	
	//Not supported anyway
	public void disableTermination(){
	}
	
	//Not supported
	public void setWriteBufferMode(SerialPort.WriteBufferMode m){
	}
}
