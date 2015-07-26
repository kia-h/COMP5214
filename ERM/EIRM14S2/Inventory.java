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

/* Inventory class manages the inventory by adding new records,
 * discarding old/expired records, and provides methods for sorting
 * the records.
 */
public class Inventory {
    private List<Record> inventory;
    private List<Record> discarded;

    public List<Record> getInventory() {
        return inventory;
    }

    public Inventory() {
        this.inventory = new ArrayList<Record>();
        this.discarded = new ArrayList<Record>();
    }

    /* Copy constructor
     */
    public Inventory(Inventory inv) {
        this();

        // copying the products
        Record r, r2;
        for (int i = 0; i < inv.getSize(); i++) {
            r = inv.getRecord(i);

            r2 = new Record(r.getProduct(), r.getQuantity(),
                    (r.getBoughton()!=null ? new Date(r.getBoughton().getTime()) : null),
                    (r.getSoldon()!=null ? new Date(r.getSoldon().getTime()) : null),
                    r.getBoughtat(), r.getSoldat(),
                    (r.getUseby()!=null ? new Date(r.getUseby().getTime()) : null));

            addRecord(r2);
        }

        // copying the discarded products
        for (int i = 0; i < inv.getDiscardedSize(); i++) {
            r = inv.getDiscardedRecord(i);

            r2 = new Record(r.getProduct(), r.getQuantity(),
                    (r.getBoughton()!=null ? new Date(r.getBoughton().getTime()) : null),
                    (r.getSoldon()!=null ? new Date(r.getSoldon().getTime()) : null),
                    r.getBoughtat(), r.getSoldat(),
                    (r.getUseby()!=null ? new Date(r.getUseby().getTime()) : null));

            addDiscardedRecord(r2);
        }
    }

    public void addRecord(Record r) {
        if (! (r.getProduct().equals("") || r.getQuantity() < 0)) {
            // if Record has NO inValid mandatory fields
            inventory.add(r);
        }
    }

    public Record getRecord(int i) {
        return inventory.get(i);
    }

    public int getSize() {
        return inventory.size();
    }

    public void addDiscardedRecord(Record r) {
        if (! (r.getProduct().equals("") || r.getQuantity() < 0)) {
            // if Record has NO inValid mandatory fields
            discarded.add(r);
        }
    }

    public Record getDiscardedRecord(int i) {
        return discarded.get(i);
    }

    public int getDiscardedSize() {
        return discarded.size();
    }

    /* Parses the given file, selects the text-records,
     * and adds them to the inventory.
     */
    public void parseFile(String fileName) {
        try {
            String strRecord = "";
            StringBuffer strBuffer = new StringBuffer("");

            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((strRecord = br.readLine()) != null) {

                if (strRecord.equals("")) {
                    strBuffer.append(".");

                    Record r = new Record(strBuffer.toString());
                    addRecord(r);

                    strBuffer = new StringBuffer("");
                } else {
                    strBuffer.append(strRecord + "\n");
                }
            }

            if (strRecord == null) {
                // processing last record in the file
                strBuffer.append(".");

                Record r = new Record(strBuffer.toString());
                addRecord(r);
            }

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

    /* Sorts the inventory as per the parameter.
     */
    public void sortInventory(String parameter) {
        if (parameter.equals("product")) {
            sortByProduct();
        } else if (parameter.equals("quantity")) {
            sortByQuantity();
        } else if (parameter.equals("useby")) {
            sortByUseby();
        } else if (parameter.equals("boughtat")) {
            sortByBoughtat();
        } else if (parameter.equals("soldat")) {
            sortBySoldat();
        } else if (parameter.equals("boughton")) {
            sortByBoughton();
        } else if (parameter.equals("soldon")) {
            sortBySoldon();
        }
    }

    /* Sorts the inventory by product's name using Bubble Sort algorithm
     */
    private void sortByProduct() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                if (inventory.get(j).getProduct().compareToIgnoreCase(inventory.get(k).getProduct()) > 0){
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Sorts the inventory by product's quantity using Bubble Sort algorithm
     */
    private void sortByQuantity() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                if (inventory.get(j).getQuantity() > inventory.get(k).getQuantity()){
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Sorts the inventory by product's buy price using Bubble Sort algorithm
     */
    private void sortByBoughtat() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                if (inventory.get(j).getBoughtat() > inventory.get(k).getBoughtat()){
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Sorts the inventory by product's sell price using Bubble Sort algorithm
     */
    private void sortBySoldat() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                if (inventory.get(j).getSoldat() > inventory.get(k).getSoldat()){
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Sorts the inventory by product's useby date using Bubble Sort algorithm
     */
    private void sortByUseby() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                Date d1 = inventory.get(j).getUseby();
                Date d2 = inventory.get(k).getUseby();
                if (d1 != null && d2 != null) {
                    if (d1.compareTo(d2) > 0){
                        j = k;
                    }
                }
                else if (d1 == null) {
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Sorts the inventory by product's buy date using Bubble Sort algorithm
     */
    private void sortByBoughton() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                Date d1 = inventory.get(j).getBoughton();
                Date d2 = inventory.get(k).getBoughton();
                if (d1 != null && d2 != null) {
                    if (d1.compareTo(d2) > 0){
                        j = k;
                    }
                } else if (d1 == null) {
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Sorts the inventory by product's sell date using Bubble Sort algorithm
     */
    private void sortBySoldon() {
        int j = 0;
        Record tmp;
        int lenD = inventory.size();

        for(int i = 0; i < lenD; i++) {
            j = i;
            for(int k = i; k < lenD; k++){
                Date d1 = inventory.get(j).getSoldon();
                Date d2 = inventory.get(k).getSoldon();
                if (d1 != null && d2 != null) {
                    if (d1.compareTo(d2) > 0){
                        j = k;
                    }
                } else if (d1 == null) {
                    j = k;
                }
            }
            tmp = inventory.get(i);
            inventory.set(i, inventory.get(j));
            inventory.set(j, tmp);
        }
    }

    /* Discards records as per the given expiry date as string.
     */
    public void discardRecords(String strData) {
        try {
            Date useby;

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date expiry = new Date(formatter.parse(strData).getTime() - (1000 * 60 * 60 * 24));

            for (int i = 0; i < inventory.size(); i++) {
                useby = inventory.get(i).getUseby();

                if (useby != null && expiry != null) {
                    if (useby.compareTo(expiry) <= 0) {
                        discarded.add(inventory.get(i)); // add to discarded list
                        inventory.remove(i); // remove from inventory list
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
