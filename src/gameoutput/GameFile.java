package gameoutput;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import player.Player;

public class GameFile {

	public static void writeCSVData(String filename, Player player, int winAmount) {
		try {
			File file = new File("files/" + filename);
			
			PrintWriter output = new PrintWriter(file);
			
			System.out.println("Writing File...");
			
			writePlayerData(output, player, winAmount);
		
			output.close();
			System.out.println("File Written!");
		}catch(IOException ex) {
			System.out.println("Error opening file for output");
		}
	}
	public static void writeBinaryData(String fileName, Player player, int winAmount) {
		File file = new File("files/" + fileName);
		try(DataOutputStream output = new DataOutputStream(new FileOutputStream(file, true))){
			System.out.println("Writing File...");
			output.writeUTF(player.getId());
			output.writeUTF(player.getName());
			output.writeUTF(player.getHand().getHandDescr());
			output.writeInt(winAmount);
			output.writeInt(player.getBank());
			System.out.println("File Written!");
		}catch(IOException ex) {
			System.out.println("Error writing data");
		}
	}
	private static void writePlayerData(PrintWriter output, Player player, int winAmount) {
		output.print("Player ID,Player Name,Hand,Hand Descr,Win Amount,Bank\n");
		output.print(player.getId());
		output.print(",");
		output.print(player.getName());
		output.print(",");
		output.print(player.getHand());
		output.print(",");
		output.print(player.getHand().getHandDescr());
		output.print(",");
		output.print(winAmount);
		output.print(",");
		output.print(player.getBank());
		output.print("\n");
	}
}
