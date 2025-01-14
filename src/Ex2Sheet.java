import java.io.IOException;
import java.util.Arrays;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private SCell[][] table;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = null;
        SCell current = get(x, y);
        if (!current.getData().isEmpty() && current.getData().charAt(0) == '=') {    //Form might be found
            try {
                ans = Double.toString(current.computeForm(current.getData().replace("=", "")));
            } catch (NumberFormatException e) {  //Other cell might be mentioned
                ans = transForm(current.getData());
                if (ans.contains(Ex2Utils.ERR_FORM)) {
                    SCell otherCell = get(ans.substring(ans.indexOf('*') + 1));
                    if ((otherCell == current) || get(otherCell.getData().replace("=", "")) == current){    //cycle found
                        current.setValue(Double.MAX_VALUE);
                        return Ex2Utils.ERR_CYCLE;
                    }else {
                        current.setType(Ex2Utils.ERR_FORM_FORMAT);
                        return Ex2Utils.ERR_FORM;
                    }
                }else if(ans.contains(Ex2Utils.ERR_CYCLE)){
                    current.setValue(Double.MAX_VALUE);
                    return Ex2Utils.ERR_CYCLE;
                }else { //No error found
                    current.setValue(current.computeForm(ans.replace("=", "")));
                    ans = Double.toString(current.getValue());
                    current.setType(Ex2Utils.FORM);
                }
            }
        } else if (current.getType() == Ex2Utils.ERR_FORM_FORMAT) {                                   //Invalid format
            if (current.isForm(eval(x, y))){
                ans = Double.toString(current.computeForm(eval(x, y).replace("=", "")));
                current.setType(Ex2Utils.FORM);
                this.table[x][y].calcType();
            }
            else {
                ans = Ex2Utils.ERR_FORM;
                this.table[x][y].setType(Ex2Utils.ERR_FORM_FORMAT);
            }
        } else if (current.getValue() == Double.MAX_VALUE) {                                 //Invalid format
            ans = Ex2Utils.ERR_CYCLE;
        } else if (current.getType() == Ex2Utils.NUMBER) {  //Print number as is
            ans = Double.toString(current.getValue());
        }else if(current.getType() == Ex2Utils.TEXT){
            ans = current.getData();
        }else{
            ans = Ex2Utils.EMPTY_CELL;
        }
        return ans;
    }

    @Override
    public SCell get(int x, int y) {
        if (this.isIn(x, y)) return table[x][y];
        else return null;
    }

    @Override
    public SCell get(String cords) {
        SCell ans = null;
        cords = cords.toUpperCase();
        if (!cords.matches("^[A-Z][0-9]{1,2}$")) return ans; //If doesn't match the form of [letter][number]
        int row = ((int) cords.charAt(0) - 65);
        int col = Integer.parseInt(cords.substring(1));
        ans = this.get(row, col);
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        SCell c = new SCell(s);
        table[x][y] = c;
        // Add your code here

        /////////////////////
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                eval(i, j);
            }
        }
        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = (xx >= 0 && yy >= 0) && (xx < table.length && yy < table[0].length);
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        SCell current = get(x, y);
        ans = current.getData();
        if (!current.getData().isEmpty() && current.getData().charAt(0) == '=') {   //Formula might be found
            try {
                ans = Double.toString(current.computeForm(ans.replace("=", "")));
                current.setValue(Double.parseDouble(ans));
            } catch (NumberFormatException e) {
                ans = transForm(ans);
                if (!ans.contains("ERR")){  //If it doesnt contain any errors
                    ans = Double.toString(current.computeForm(ans.replace("=", "")));
                    current.setValue(Double.parseDouble(ans));
                }else { //Does contain an error
                    if (ans.contains(Ex2Utils.ERR_FORM)){
                        current.setValue(Double.MIN_VALUE);
                        return Ex2Utils.ERR_FORM;
                    }else if(ans.contains(Ex2Utils.ERR_CYCLE)){
                        current.setValue(Double.MAX_VALUE);
                        return Ex2Utils.ERR_CYCLE;
                    }
                }
            }
        }else if(current.getType() == Ex2Utils.TEXT) ans = current.getData();
        return ans;
    }

    public String transForm(String form) {
        if (!form.matches(".*[a-zA-Z].*")) {           //If I don't have letters in the string
            return form.replace("=", "");   //return value of the cell
        } else {
            int indexOfLetter = 0, indexOfNextOp = form.length();
            for (int i = 0; i < form.length(); i++) {
                if (Character.isLetter(form.charAt(i))) {
                    indexOfLetter = i;
                    break;
                }
            }
            for (int i = indexOfLetter + 1; i < indexOfNextOp; i++) {
                if ((!Character.isDigit(form.charAt(i)) && !Character.isLetter(form.charAt(i)))) {
                    indexOfNextOp = i;
                    break;
                }
            }
            if ((indexOfNextOp - indexOfLetter) > 1) {
                SCell c = get(form.substring(indexOfLetter, indexOfNextOp));
                //      cell not initialized                      or not a number/formula
                if (c.getValue() == Double.MAX_VALUE){
                    return Ex2Utils.ERR_CYCLE;
                }
                else if (c.getValue() == Double.MIN_VALUE || (c.getType() != Ex2Utils.NUMBER && c.getType() != Ex2Utils.FORM)){
                    return Ex2Utils.ERR_FORM + "*" + form.substring(indexOfLetter, indexOfNextOp);    //if cell is empty or a text
                }
                else return form.substring(0, indexOfLetter) + c.getValue() + transForm(form.substring(indexOfNextOp));
            }else{
                return Ex2Utils.ERR_FORM;
            }
        }
    }

}