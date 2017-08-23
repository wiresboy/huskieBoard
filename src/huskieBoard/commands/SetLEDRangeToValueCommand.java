/**
 * 
 */
package huskieBoard.commands;

import huskieBoard.HuskieBoard;
import java.awt.Color;


/**
 * @author Brandon
 *
 */
public class SetLEDRangeToValueCommand extends Command {

	private static final byte commandByte = (byte)0x19;
	private static final int defaultPriority = 0;

	private Color color;
	private int startIndex;//Max 255
	private int endIndex;//Max 255
	
	
	/**
	 * @param priority
	 * @param color the color to set all of the LEDs to. Color object from java.awt.color
	 * @param startIndex (0 based, max of 255)
	 * @param endIndex (0 based, max of 255)
	 */
	public SetLEDRangeToValueCommand(int priority, Color color, int startIndex, int endIndex) {
		super(priority);
		this.color = color;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	/**
	 * @param priority
	 * @param r Red value, range 0-255
	 * @param g Green value, range 0-255
	 * @param b Blue value, range 0-255
	 * @param startIndex (0 based, max of 255)
	 * @param endIndex (0 based, max of 255)
	 */
	public SetLEDRangeToValueCommand(int priority, int r, int g, int b, int startIndex, int endIndex)
	{
		this(priority, new Color(r,g,b), startIndex, endIndex);
	}

	/**
	 * @param r Red value, range 0-255
	 * @param g Green value, range 0-255
	 * @param b Blue value, range 0-255
	 * @param startIndex (0 based, max of 255)
	 * @param endIndex (0 based, max of 255)
	 */
	public SetLEDRangeToValueCommand(int r, int g, int b, int startIndex, int endIndex)
	{
		this(defaultPriority, r,g,b, startIndex, endIndex);
	}

	/**
	 * Override the color to be sent to be a new color.
	 * @param newColor
	 */
	public void setColor(Color newColor)
	{
		this.color = newColor;
	}

	/**
	 * Override the range to be sent.
	 * @param start_index (0 based, max of 255)
	 * @param end_index (0 based, max of 255)
	 */
	public void setRange(int start_index, int end_index)
	{
		this.startIndex = start_index;
		this.endIndex = end_index;
	}
	
	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		// 0x15 r + g + b + start_led + end_led + checksum
		// TODO Auto-generated method stub
		System.out.println("Color is: "+this.color.toString());
		byte r = (byte)this.color.getRed();
		byte g = (byte)this.color.getGreen();
		byte b = (byte)this.color.getBlue();
		byte[] bytes = new byte[] {commandByte, r, g, b, (byte)this.startIndex, (byte)this.endIndex};
		return appendChecksum(bytes);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#handleResponsePrivate(huskieBoard.HuskieBoard)
	 */
	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		// No response expected
		return true;
	}

}
