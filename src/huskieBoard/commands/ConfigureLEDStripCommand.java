/**
 * 
 */
package huskieBoard.commands;

import huskieBoard.HuskieBoard;

/**
 * @author Brandon
 * Configure the Huskie board to control a strip of LEDs
 */
public class ConfigureLEDStripCommand extends Command {

	private static final byte commandByte = (byte)0x17;
	private static final int defaultPriority = 1;

	public enum HuskieLEDPin {
		SPI_MOSI(6),
		SPI_MISO(7),
		SPI_CLK(8),
		SPI_CS(9),
		I2C_SCL(12),
		I2C_SDA(13),
		GPIO_0(14),
		GPIO_1(15),
		GPIO_2(16),
		GPIO_3(17),
		LCD(18);

		private int pin;    

		private HuskieLEDPin(int pin) {
			this.pin = pin;
		}

		public int getPin() {
			return pin;
		}

		/**
		 * GetPinShifted returns the pin with the bits shifted for transmitting
		 * @return shifted bits
		 */
		public int getPinShifted() {
			return (this.pin << 3);
		}
	}

	public enum HuskieLedMode {
		WS2812(0),
		WS2812B(1);

		private int mode;

		private HuskieLedMode(int mode) {
			this.mode = mode;
		}

		public int getMode() {
			return this.mode;
		}
	}

	private HuskieLEDPin pin;
	private HuskieLedMode mode;
	private byte length;

	/**
	 * Configure an LED strip. Only call once. Limit 1 strip, limit 256 LEDs
	 * @param priority
	 * @param pin
	 * @param mode
	 * @param lengthOfStrip
	 */
	public ConfigureLEDStripCommand(int priority, HuskieLEDPin pin, HuskieLedMode mode, int lengthOfStrip) {
		super(priority);
		this.pin = pin;
		this.mode = mode;
		this.length = (byte)(lengthOfStrip-1);
	}

	/**
	 * Configure an LED strip. Only call once. Limit 1 strip, limit 256 LEDs
	 * @param pin
	 * @param mode
	 * @param lengthOfStrip
	 */
	public ConfigureLEDStripCommand(HuskieLEDPin pin, HuskieLedMode mode, int lengthOfStrip) {
		this(defaultPriority, pin, mode, lengthOfStrip);
	}
	
	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#getCommandByteArray()
	 */
	@Override
	public byte[] getCommandByteArray() {
		byte config = (byte) (this.pin.getPinShifted() | this.mode.getMode());
		byte[] b = new byte[] {commandByte, config, length};
		return appendChecksum(b);
	}

	/* (non-Javadoc)
	 * @see huskieBoard.commands.Command#handleResponsePrivate(huskieBoard.HuskieBoard)
	 */
	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
