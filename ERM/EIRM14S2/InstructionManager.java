package EIRM14S2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* InstructionManager class is used to parse and process the instructions
 * specified in the 'instructions' file.
 */
public class InstructionManager {
    private Inventory inventory;
    private OutputManager outManager;

    public InstructionManager(Inventory inventory, OutputManager outManager) {
        this.inventory = inventory;
        this.outManager = outManager;
    }

    /* Parses the 'instruction' file, reads the instrctions given and
     * calls the necessary functions to process those instructions.
     */
    public void parseFile(String fileName) {
        try {
            String strRecord = "";

            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((strRecord = br.readLine()) != null) {
                if (strRecord.startsWith("buy")) {
                    manageBuy(strRecord);
                } else if (strRecord.startsWith("sell")) {
                    manageSell(strRecord);
                } else if (strRecord.startsWith("sort")) {
                    manageSort(strRecord);
                } else if (strRecord.startsWith("discard")) {
                    manageDiscard(strRecord);
                } else if (strRecord.startsWith("query")) {
                    manageQuery(strRecord);
                }
            }

            // prints the left-over inventory after all the instructions from the file are executed
            leftInventory();

            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Manages the 'buy' instruction.
     */
    private void manageBuy(String strData) {
        strData = strData.substring(strData.indexOf(' ') + 1);
        strData += "; ";
        strData = strData.replace("; ", "\n");
        strData += ".";

        Record r = new Record(strData); // make a new record from the text data
        inventory.addRecord(r); // add the record to the inventory
    }

    /* Manages the 'sell' instruction.
     */
    private void manageSell(String strData) {
        strData = strData.substring(strData.indexOf(' ') + 1);
        strData += "; ";
        strData = strData.replace("; ", "\n");
        strData += ".";

        Record r = new Record(strData); // make a new record from the text data
        inventory.addRecord(r); // add the record to the inventory
    }

    /* Manages the 'buy' instruction.
     */
    private void manageSort(String strData) {
        strData = strData.substring(strData.indexOf(' ') + 1);

        inventory.sortInventory(strData);
    }

    /* Manages the 'discard' instruction.
     */
    private void manageDiscard(String strData) {
        strData = strData.substring(strData.indexOf(' ') + 1);

        inventory.discardRecords(strData);
    }

    /* Manages the 'query' instructions.
     */
    private void manageQuery(String strData) {
        strData = strData.substring(strData.indexOf(' ') + 1);

        if (strData.startsWith("profit")) {
            manageQuery4(strData);
        } else if (strData.startsWith("worstsales")) {
            manageQuery3(strData);
        } else if (strData.startsWith("bestsales")) {
            manageQuery2(strData);
        } else {
            manageQuery1(strData);
        }
    }

    /* Manages available stock query
     * Example: query 8-5-2014
     */
    private void manageQuery1(String strData) {
        String query = strData.toString();
        strData = strData.substring(strData.indexOf(' ') + 1).trim();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(strData);

            Inventory inv = new Inventory(inventory); // clones the inventory
            List<Record> list = new ArrayList<Record>(); // list of availability records

            // available stock list
            List<String> names = new ArrayList<String>(); // names of products
            List<Integer> quant = new ArrayList<Integer>(); // quantity left

            Record r, r2, r3;

            // adding the quantity from looking into 'buy' records
            inv.sortInventory("boughton");
            for (int i = 0; i < inv.getSize(); i++) {
                r = inv.getRecord(i);

                if (r.getBoughtat() != -1) {
                    // if 'buy' record

                    Date boughton = r.getBoughton();
                    Date useby = r.getUseby();

                    if (boughton != null) {
                        if (boughton.compareTo(date) <= 0) {
                            // if bought within given time range, then add it to 'names'

                            boolean isAdded = false;

                            for (int j = 0; j < names.size(); j++) {
                                if (names.get(j).equals(r.getProduct())) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (! isAdded) {
                                names.add(r.getProduct());
                                quant.add(0);
                            }

                            if (useby != null) {
                                // if useby is available

                                if (useby.compareTo(date) >= 0) {
                                    // if useby within given time range, then assign useby to the availability record

                                    boolean isAdded2 = false;
                                    r2 = null;

                                    // searching if product is already included in the list
                                    for (int j = 0; j < list.size(); j++) {
                                        r3 = list.get(j);

                                        if (r.getProduct().equals(r3.getProduct())) {
                                            r2 = r3;
                                            isAdded2 = true;

                                            if (r.getUseby() != null && r3.getUseby() != null &&
                                                    r.getUseby().compareTo(r3.getUseby()) == 0) {
                                                break;
                                            } else {
                                                // do nothing and continue looping
                                            }
                                        }
                                    }

                                    if (! isAdded2) {
                                        list.add(r); // add record to the list
                                    } else if (r2 != null) {
                                        if (r.getUseby() == null) {
                                            // useby not found for that product
                                            r2.setUseby(null); // set useby to 'null'

                                            // add quantities as product is in multiple records
                                            r2.setQuantity(r2.getQuantity() + r.getQuantity());
                                        } else {
                                            list.add(r);
                                        }
                                    }
                                }
                            } else {
                                // useby is not available

                                boolean isAdded3 = false;
                                r2 = null;

                                // searching if product is already included in the list
                                for (int j = 0; j < list.size(); j++) {
                                    r3 = list.get(j);

                                    if (r.getProduct().equals(r3.getProduct())) {
                                        r2 = r3;
                                        isAdded3 = true;

                                        if (r.getUseby() != null && r3.getUseby() != null && r.getUseby().compareTo(r3.getUseby()) == 0) {
                                            break;
                                        } else {
                                            // do nothing and continue looping
                                        }
                                    }
                                }

                                if (! isAdded3) {
                                    list.add(r); // add record to the list
                                } else if (r2 != null) {
                                    if (r.getUseby() == null) {
                                        // useby not found for that product
                                        r2.setUseby(null); // set useby to 'null'

                                        // add quantities as product is in multiple records
                                        r2.setQuantity(r2.getQuantity() + r.getQuantity());
                                    } else {
                                        list.add(r);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // subtracting the quantity from looking into 'sell' records
            inv.sortInventory("soldon");
            for (int i = 0; i < inv.getSize(); i++) {
                r = inv.getRecord(i);

                if (r.getSoldat() != -1) {
                    Date soldon = r.getSoldon();

                    if (soldon != null) {
                        if (soldon.compareTo(date) <= 0) {
                            // if sold within given time range

                            for (int j = 0; j < list.size(); j++) {
                                r3 = list.get(j);

                                if (r.getProduct().equals(r3.getProduct())) {
                                    // if product found with 'sell' record
                                    // then subtract the 'sell' quantity from available quantity

                                    if (r3.getQuantity() >= r.getQuantity()) {
                                        r3.setQuantity(r3.getQuantity() - r.getQuantity());
                                        r.setQuantity(0);
                                        break;
                                    } else {
                                        r.setQuantity(r.getQuantity() - r3.getQuantity());
                                        r3.setQuantity(0);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // write the list to the file
            outManager.writeAvailabilityQuery(query, list, names, quant);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /* Manages best-sales query
     * Example: query bestsales 01-06-2014 30-6-2014
     */
    private void manageQuery2(String strData) {
        String query = strData.toString();
        strData = strData.substring(strData.indexOf(' ') + 1).trim();

        String date1, date2;
        int index = strData.indexOf(' ');

        if (index != -1) {
            date1 = strData.substring(0, index);
            date2 = strData.substring(index + 1);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date startDate = formatter.parse(date1);
                Date endDate = formatter.parse(date2);

                String name = "";
                double roa = Double.MAX_VALUE;

                Inventory inv = new Inventory(inventory); // clone the inventory
                inv.sortInventory("useby");

                List<String> names = new ArrayList<String>();
                List<Integer> startCnt = new ArrayList<Integer>();
                List<Integer> endCnt = new ArrayList<Integer>();
                List<Double> buyPrice = new ArrayList<Double>(); // purchasing cost
                List<Double> sellPrice = new ArrayList<Double>(); // net sales

                // require all records for calculating ROA
                // so adding discarded records too
                for (int i = 0; i < inv.getDiscardedSize(); i++) {
                    inv.addRecord(inv.getDiscardedRecord(i));
                }

                // adding names of all products/records to the list
                for (int i = 0; i < inv.getSize(); i++) {
                    boolean isAdded = false;

                    for (int j = 0; j < names.size(); j++) {
                        if (names.get(j).equals(inv.getRecord(i).getProduct())) {
                            isAdded = true;
                            break;
                        }
                    }

                    if (! isAdded) {
                        names.add(inv.getRecord(i).getProduct());
                        startCnt.add(0);
                        endCnt.add(0);
                        buyPrice.add(0.0);
                        sellPrice.add(0.0);
                    }
                }

                // calculating the start quantity and purchasing cost
                Record r;
                for (int i = 0; i < inv.getSize(); i++) {
                    r = inv.getRecord(i);

                    if (r.getBoughtat() != -1) {
                        Date boughton = r.getBoughton();

                        if (boughton != null) {
                            if (boughton.compareTo(startDate) >= 0 && boughton.compareTo(endDate) <= 0) {
                                for (int j = 0; j < names.size(); j++) {
                                    if (names.get(j).equals(r.getProduct())) {
                                        startCnt.set(j, (startCnt.get(j) + r.getQuantity()));
                                        endCnt.set(j, startCnt.get(j));
                                        buyPrice.set(j, (buyPrice.get(j) + (r.getBoughtat() * r.getQuantity())));
                                    }
                                }
                            }
                        }
                    }
                }

                // calculating the end quantity and net sales
                for (int i = 0; i < inv.getSize(); i++) {
                    r = inv.getRecord(i);

                    if (r.getSoldat() != -1) {
                        Date soldon = r.getSoldon();

                        if (soldon != null) {
                            if (soldon.compareTo(startDate) >= 0 && soldon.compareTo(endDate) <= 0) {
                                for (int j = 0; j < names.size(); j++) {
                                    if (names.get(j).equals(r.getProduct())) {
                                        endCnt.set(j, (endCnt.get(j) - r.getQuantity()));
                                        sellPrice.set(j, (sellPrice.get(j) + (r.getSoldat() * r.getQuantity())));
                                    }
                                }
                            }
                        }
                    }
                }

                // finding the max. ROA
                double tmpRoa;
                for (int i = 0; i < names.size(); i++) {
                    tmpRoa = (sellPrice.get(i) - buyPrice.get(i)) / ((startCnt.get(i) + endCnt.get(i)) / 2);

                    if (roa < tmpRoa) {
                        roa = tmpRoa;
                        name = names.get(i);
                    }
                }

                // write the info. to the file
                outManager.writeSalesQuery(query, name, roa);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /* Manages worst-sales query
     * Example: query worstsales 01-06-2014 30-6-2014
     */
    private void manageQuery3(String strData) {
        String query = strData.toString();
        strData = strData.substring(strData.indexOf(' ') + 1).trim();

        String date1, date2;
        int index = strData.indexOf(' ');

        if (index != -1) {
            date1 = strData.substring(0, index);
            date2 = strData.substring(index + 1);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date startDate = formatter.parse(date1);
                Date endDate = formatter.parse(date2);

                String name = "";
                double roa = Double.MAX_VALUE;

                Inventory inv = new Inventory(inventory); // clone the inventory
                inv.sortInventory("useby");

                List<String> names = new ArrayList<String>();
                List<Integer> startCnt = new ArrayList<Integer>();
                List<Integer> endCnt = new ArrayList<Integer>();
                List<Double> buyPrice = new ArrayList<Double>(); // purchasing cost
                List<Double> sellPrice = new ArrayList<Double>(); // net sales

                // require all records for calculating ROA
                // so adding discarded records too
                for (int i = 0; i < inv.getDiscardedSize(); i++) {
                    inv.addRecord(inv.getDiscardedRecord(i));
                }

                // adding names of all products/records to the list
                for (int i = 0; i < inv.getSize(); i++) {
                    boolean isAdded = false;

                    for (int j = 0; j < names.size(); j++) {
                        if (names.get(j).equals(inv.getRecord(i).getProduct())) {
                            isAdded = true;
                            break;
                        }
                    }

                    if (! isAdded) {
                        names.add(inv.getRecord(i).getProduct());
                        startCnt.add(0);
                        endCnt.add(0);
                        buyPrice.add(0.0);
                        sellPrice.add(0.0);
                    }
                }

                // calculating the start quantity and purchasing cost
                Record r;
                for (int i = 0; i < inv.getSize(); i++) {
                    r = inv.getRecord(i);

                    if (r.getBoughtat() != -1) {
                        Date boughton = r.getBoughton();

                        if (boughton != null) {
                            if (boughton.compareTo(startDate) >= 0 && boughton.compareTo(endDate) <= 0) {
                                for (int j = 0; j < names.size(); j++) {
                                    if (names.get(j).equals(r.getProduct())) {
                                        startCnt.set(j, (startCnt.get(j) + r.getQuantity()));
                                        endCnt.set(j, startCnt.get(j));
                                        buyPrice.set(j, (buyPrice.get(j) + (r.getBoughtat() * r.getQuantity())));
                                    }
                                }
                            }
                        }
                    }
                }

                // calculating the end quantity and net sales
                for (int i = 0; i < inv.getSize(); i++) {
                    r = inv.getRecord(i);

                    if (r.getSoldat() != -1) {
                        Date soldon = r.getSoldon();

                        if (soldon != null) {
                            if (soldon.compareTo(startDate) >= 0 && soldon.compareTo(endDate) <= 0) {
                                for (int j = 0; j < names.size(); j++) {
                                    if (names.get(j).equals(r.getProduct())) {
                                        endCnt.set(j, (endCnt.get(j) - r.getQuantity()));
                                        sellPrice.set(j, (sellPrice.get(j) + (r.getSoldat() * r.getQuantity())));
                                    }
                                }
                            }
                        }
                    }
                }

                // finding the min. ROA
                double tmpRoa;
                for (int i = 0; i < names.size(); i++) {
                    tmpRoa = (sellPrice.get(i) - buyPrice.get(i)) / ((startCnt.get(i) + endCnt.get(i)) / 2);

                    if (roa > tmpRoa) {
                        roa = tmpRoa;
                        name = names.get(i);
                    }
                }

                // write the info. to the file
                outManager.writeSalesQuery(query, name, roa);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /* Manages profit query
     * Example: query profit 3-3-2014 2-4-2014
     */
    private void manageQuery4(String strData) {
        String query = strData.toString();
        strData = strData.substring(strData.indexOf(' ') + 1).trim();

        String date1, date2;
        int index = strData.indexOf(' ');

        if (index != -1) {
            date1 = strData.substring(0, index);
            date2 = strData.substring(index + 1);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date startDate = formatter.parse(date1);
                Date endDate = formatter.parse(date2);

                Inventory inv = new Inventory(inventory); // clone the inventory
                inv.sortInventory("useby");

                // calculate the total income in the given period
                double income = 0.0;
                Record r;
                for (int i = 0; i < inv.getSize(); i++) {
                    r = inv.getRecord(i);

                    if (r.getSoldat() != -1) {
                        Date soldon = r.getSoldon();

                        if (soldon != null) {
                            if (soldon.compareTo(startDate) >= 0 && soldon.compareTo(endDate) <= 0) {
                                // income generated by selling the products
                                income += (r.getSoldat() * r.getQuantity());

                                for (int j = 0; j < inv.getSize(); j++) {
                                    Record r2 = inv.getRecord(j);

                                    if (r.getProduct().equals(r2.getProduct()) && r2.getBoughtat() != -1) {
                                        // income subtracted by purchasing the products
                                        income -= (r2.getBoughtat() * r.getQuantity());
                                        r2.setQuantity(r2.getQuantity() - r.getQuantity());
                                    }
                                }
                                r.setQuantity(0);
                            }
                        }
                    }
                }

                // calculate the total loss due to discarding products
                Double loss = 0.0;
                List<Record> discardList = new ArrayList<Record>();
                for (int i = 0; i < inv.getDiscardedSize(); i++) {
                    r = inv.getDiscardedRecord(i);

                    loss += (r.getBoughtat() * r.getQuantity());
                    discardList.add(r);
                }

                // write the list to the file
                outManager.writeProfitQuery(query, income, loss, discardList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /* Finds the left-over inventory after performing all the instructions.
     */
    private void leftInventory() {
        Inventory inv = new Inventory(inventory); // clones the inventory
        inv.sortInventory("useby");

        Record r, r2;
        for (int i = 0; i < inv.getSize(); i++) {
            r = inv.getRecord(i);

            if (r.getSoldat() != -1) {

                // calculating the left quantity after making sales
                for (int j = 0; j < inv.getSize(); j++) {
                    r2 = inv.getRecord(j);

                    if (r.getProduct().equals(r2.getProduct()) && r2.getBoughtat() != -1) {
                        if (r2.getQuantity() >= r.getQuantity()) {
                            r2.setQuantity(r2.getQuantity() - r.getQuantity());

                            r.setQuantity(0);
                            break;
                        } else {
                            r.setQuantity(r.getQuantity() - r2.getQuantity());
                            r2.setQuantity(0);
                        }
                    }
                }
            }
        }

        // making a list of records in the inventory
        List<Record> list = new ArrayList<Record>();
        for (int i = 0; i < inv.getSize(); i++) {
            r = inv.getRecord(i);

            list.add(r);
        }

        // write the list to the file
        outManager.writeRecords(list);
    }
}
