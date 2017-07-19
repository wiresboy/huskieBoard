/**
 * GetFirmwareVersionCommand.java
 * Retrieve the current firmware version
 */
package huskieBoard.commands;

import java.util.Arrays;

import huskieBoard.HuskieBoard;


public class GetFirmwareVersionCommand extends Command {

	private static final byte commandByte = (byte)0xFF;
	
	private HuskieBoardVersion huskieBoardVersion = null;
	
	public class HuskieBoardVersion {
		private int huskieBoardVersionMajor;
		private int huskieBoardVersionMinor;
		private int huskieBoardVersionFix;
		private int huskieBoardVersionTest;
		
		/**
		 * Pass in the entire response (all 6 bytes)
		 * @param versionResponseBytes
		 */
		public HuskieBoardVersion(byte [] versionResponseBytes)
		{
			huskieBoardVersionMajor = toPositive(versionResponseBytes[1]);
			huskieBoardVersionMinor = toPositive(versionResponseBytes[2]);
			huskieBoardVersionFix   = toPositive(versionResponseBytes[3]);
			huskieBoardVersionTest  = toPositive(versionResponseBytes[4]);
		}
		
		private int toPositive(byte val) {
			return (val + 128) % 256;
		}
		
		/**
		 * @return the huskieBoardVersionMajor
		 */
		public int getHuskieBoardVersionMajor() {
			return huskieBoardVersionMajor;
		}

		/**
		 * @return the huskieBoardVersionMinor
		 */
		public int getHuskieBoardVersionMinor() {
			return huskieBoardVersionMinor;
		}

		/**
		 * @return the huskieBoardVersionFix
		 */
		public int getHuskieBoardVersionFix() {
			return huskieBoardVersionFix;
		}

		/**
		 * @return the huskieBoardVersionTest
		 */
		public int getHuskieBoardVersionTest() {
			return huskieBoardVersionTest;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "" + huskieBoardVersionMajor + ":" + huskieBoardVersionMinor + ":" + huskieBoardVersionFix + ":" + huskieBoardVersionTest;
		}

	}

	/**
	 * Get the firmware version of the attached Huskie Board.
	 */
	public GetFirmwareVersionCommand() {
		super();
	}


	/**
	 * Get the firmware version of the attached Huskie Board.
	 * @param priority
	 */
	public GetFirmwareVersionCommand(int priority) {
		super(priority);
	}

	@Override
	public byte[] getCommandByteArray() {
		return new byte[] {commandByte, commandByte};
	}

	@Override
	protected boolean handleResponsePrivate(HuskieBoard board) {
		try {
			byte[] responseBytes = board.serialRead(6);
			if ((responseBytes.length == 6) && (responseBytes[0] == commandByte) && (checkChecksum(responseBytes)))
			{
				
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
	 * Return the version of the Huskie Board.
	 * @return Firmware version, or -1 if the board has not yet responded/the response gave an error.
	 */
	public HuskieBoardVersion getVersion()
	{
		if (getStatus()==Status.SENT_SUCCESS)
		{
			return huskieBoardVersion;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return the version of the Huskie Board once the command has finished running. (Blocking)
	 * @return Firmware version (HuskieBoardVersion object), or null if the response gave an error.
	 */
	public HuskieBoardVersion getVersionBlocking()
	{
		waitForResponseFinished();
		return getVersion();
	}
}
