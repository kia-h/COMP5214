package EIRM14S2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* OutputManager class deals with writing data to output files.
 * It has various methods to write multiple types of information to
 * the two specified files.
 */
public class OutputManager {
    String reportFile;
    String resultFile;

    public OutputManager(String reportFile, String resultFile) {
        this.reportFile = reportFile;
        this.resultFile = resultFile;
    }

    /* Writes the profit query to the report file.
     */
    public void writeProfitQuery(String query, double income, double loss, List<Record> discarded) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            FileWriter fw = new FileWriter(reportFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("---------------------- ");
            bw.write("query " + query);
            bw.write(" ----------------------");
            bw.write("\n");

            bw.write("\n");

            bw.write("net income:\t\t$" + income);
            bw.write("\n");

            bw.write("\n");

            bw.write("loss:\t\t\t$" + loss);
            bw.write("\n");

            bw.write("\n");
            bw.write("\n");

            bw.write("discarded items:");
            bw.write("\n");

            bw.write("\n");

            Record r;
            for (int i = 0; i < discarded.size(); i++) {
                r = discarded.get(i);

                bw.write("product\t\t\t" + r.getProduct());
                bw.write("\n");
                bw.write("boughton\t\t" + formatter.format(r.getBoughton()));
                bw.write("\n");
                bw.write("useby\t\t\t" + formatter.format(r.getUseby()));
                bw.write("\n");
                bw.write("boughtat\t\t$" + r.getBoughtat());
                bw.write("\n");
                bw.write("quantity\t\t" + r.getQuantity());
                bw.write("\n");

                bw.write("\n");
            }

            bw.write("---------------------- ");
            bw.write("End");
            bw.write(" ----------------------");
            bw.write("\n");

            bw.write("\n");


            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Writes the list of available stock to the report file.
     */
    public void writeAvailabilityQuery(String query, List<Record> list, List<String> names, List<Integer> quant) {
        Date d;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            FileWriter fw = new FileWriter(reportFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            Inventory inv = new Inventory();
            Record r;
            for (int i = 0; i < list.size(); i++) {
                r = list.get(i);

                inv.addRecord(r);
            }
            inv.sortInventory("useby");

            bw.write("---------------------- ");
            bw.write("query " + query);
            bw.write(" ----------------------");
            bw.write("\n");

            bw.write("\n");

            for (int i = 0; i < inv.getSize(); i++) {
                r = inv.getRecord(i);

                if (r.getQuantity() > 0) {
                    for (int j = 0; j < names.size(); j++) {
                        if (names.get(j).equals(r.getProduct())) {
                            quant.set(j, (quant.get(j) + r.getQuantity()));
                        }
                    }
                }

                bw.write("product\t\t\t" + r.getProduct());
                bw.write("\n");
                if ((d = r.getUseby()) != null) {
                    bw.write("useby\t\t\t" + formatter.format(d));
                    bw.write("\n");
                }
                bw.write("quantity\t\t" + r.getQuantity());
                bw.write("\n");

                bw.write("\n");
            }

            bw.write("\n");

            bw.write("suggestion:");
            bw.write("\n");

            bw.write("\n");

            for (int i = 0; i < quant.size(); i++) {
                if (quant.get(i) < 10) {
                    bw.write("product\t\t\t" + names.get(i));
                    bw.write("\n");
                }
            }

            bw.write("\n");
            bw.write("\n");

            bw.write("---------------------- ");
            bw.write("End");
            bw.write(" ----------------------");
            bw.write("\n");

            bw.write("\n");


            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Writes the best sales and worst sales queries to the report file.
     */
    public void writeSalesQuery(String query, String name, double quant) {
        try {
            FileWriter fw = new FileWriter(reportFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("---------------------- ");
            bw.write("query " + query);
            bw.write(" ----------------------");
            bw.write("\n");

            bw.write("\n");

            bw.write("product\t\t\t" + name);
            bw.write("\n");
            bw.write("ROA\t\t\t\t$" + quant);
            bw.write("\n");

            bw.write("\n");

            bw.write("---------------------- ");
            bw.write("End");
            bw.write(" ----------------------");
            bw.write("\n");

            bw.write("\n");


            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Writes the leftover records to the results file.
     */
    public void writeRecords(List<Record> inv) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            FileWriter fw = new FileWriter(resultFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            Record r;
            for (int i = 0; i < inv.size(); i++) {
                r = inv.get(i);

                if (r.getBoughtat() != -1 && r.getQuantity() > 0) {
                    bw.write("product\t\t\t" + r.getProduct());
                    bw.write("\n");
                    bw.write("boughton\t\t" + formatter.format(r.getBoughton()));
                    bw.write("\n");
                    bw.write("boughtat\t\t$" + r.getBoughtat());
                    bw.write("\n");
                    bw.write("quantity\t\t" + r.getQuantity());
                    bw.write("\n");

                    bw.write("\n");
                }
            }


            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
