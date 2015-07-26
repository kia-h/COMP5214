package EIRM14S2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/* Record class holds information about one record.
 * It provides getter and setter methods for accessing and modifying the
 * contents of its attributes or instance variables.
 */
public class Record {
    private String product;
    private int quantity;
    private Date boughton;
    private Date soldon;
    private double boughtat;
    private double soldat;
    private Date useby;

    private Record() {
        product = "";
        quantity = -1;
        boughton = null;
        soldon = null;
        boughtat = -1.0;
        soldat = -1.0;
        useby = null;
    }

    public Record(String product, int quantity) {
        this();

        // mandatory fields
        this.product = product;
        this.quantity = quantity;
    }

    public Record(String product, int quantity, Date boughton, Date soldon, double boughtat, double soldat, Date useby) {
        this();

        this.product = product;
        this.quantity = quantity;
        this.boughton = boughton;
        this.soldon = soldon;
        this.boughtat = boughtat;
        this.soldat = soldat;
        this.useby = useby;
    }

    /* Constructor to initialize this class's object from a text represented record.
     * It is used to create this class's objects from the record-as-text from given 'products' file.
     */
    public Record(String strData) {
        this();

        int index;
        String line;
        String varName, varValue;
        Scanner scanner = new Scanner(strData);

        varName = varValue = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            if (line.startsWith("product") || line.startsWith("quantity") || line.startsWith("boughton") ||
                    line.startsWith("useby") || line.startsWith("soldon") || line.startsWith("boughtat") ||
                    line.startsWith("soldat") || line.equals(".")) {

                if (! varName.equals("")) {
                    // if varName is not empty, i.e., varValue has some information about the record
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    if (varName.equals("product")) {
                        product = varValue.toString();

                        varName = varValue = "";
                    } else if (varName.equals("quantity")) {
                        try {
                            quantity = Integer.parseInt(varValue);
                        } catch (NumberFormatException e) {
                            quantity = -1;
                        }

                        varName = varValue = "";
                    } else if (varName.equals("useby")) {
                        try {
                            useby = formatter.parse(varValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        varName = varValue = "";
                    } else if (varName.equals("boughtat")) {
                        try {
                            boughtat = Double.parseDouble(varValue.substring(1));
                        } catch (NumberFormatException e) {
                            boughtat = -1;
                        }

                        varName = varValue = "";
                    } else if (varName.equals("soldat")) {
                        try {
                            soldat = Double.parseDouble(varValue.substring(1));
                        } catch (NumberFormatException e) {
                            soldat = -1;
                        }

                        varName = varValue = "";
                    } else if (varName.equals("boughton")) {
                        try {
                            boughton = formatter.parse(varValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        varName = varValue = "";
                    } else if (varName.equals("soldon")) {
                        try {
                            soldon = formatter.parse(varValue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        varName = varValue = "";
                    }

                    if ((index = line.indexOf(' ')) != -1) {
                        // separating information's name and value
                        varName = line.substring(0, index);
                        varValue = line.substring(index).trim();
                    }
                } else {
                    if ((index = line.indexOf(' ')) != -1) {
                        // separating information's name and value
                        varName = line.substring(0, index);
                        varValue = line.substring(index).trim();
                    }
                }
            } else {
                varValue += " " + line.trim();
            }
        }
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getBoughton() {
        return boughton;
    }

    public void setBoughton(Date boughton) {
        this.boughton = boughton;
    }

    public Date getSoldon() {
        return soldon;
    }

    public void setSoldon(Date soldon) {
        this.soldon = soldon;
    }

    public double getBoughtat() {
        return boughtat;
    }

    public void setBoughtat(double boughtat) {
        this.boughtat = boughtat;
    }

    public double getSoldat() {
        return soldat;
    }

    public void setSoldat(double soldat) {
        this.soldat = soldat;
    }

    public Date getUseby() {
        return useby;
    }

    public void setUseby(Date useby) {
        this.useby = useby;
    }
}
