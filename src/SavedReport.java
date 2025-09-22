import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class SavedReport {

	public static void main(String[] args) {
		System.out.printf("%-15s", "Game ID");
		System.out.printf("%-30s", "Player Name");
		System.out.printf("%-20s", "Hand Descr");
		System.out.printf("%-20s", "Win Amount");
		System.out.println("Bank");

		try(DataInputStream input = new DataInputStream(new FileInputStream("files/report.dat"))) {
			while(true) {
				System.out.printf("%-15s",input.readUTF());
				System.out.printf("%-30s",input.readUTF());
				System.out.printf("%-20s",input.readUTF());
				System.out.printf("%-20s",input.readInt());
				System.out.println(input.readInt());
			}
			
		} catch (EOFException ex) {
			System.out.println("\nAll data has been read");
		} catch (IOException ex) {
			System.out.println(ex);
		}



	}

}
