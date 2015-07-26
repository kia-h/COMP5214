package EIRM14S2;

/* EIRM class contains the main() function which is the entry point for a
 * console-based application like this program.
 */
public class EIRM {

    public static void main(String[] args) {
	    if (args.length != 4) {
            // program invoked with wrong no. of arguments
            System.err.println("Usage: java EIRM14S2/EIRM inventoryfile instructionfile outputfile reportfile");
        } else {
            // program invoked with correct no. of arguments
            String inventoryFile = args[0];
            String instructionFile = args[1];
            String resultFile = args[2];
            String reportFile = args[3];

            Inventory i = new Inventory();
            i.parseFile(inventoryFile);

            OutputManager om = new OutputManager(reportFile, resultFile);

            InstructionManager im = new InstructionManager(i, om);
            im.parseFile(instructionFile);
        }
    }
}
