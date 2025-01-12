// Add your documentation below:

import java.util.Arrays;
import java.util.InputMismatchException;

public class SCell implements Cell {
    private String line;
    private int type;
    private double value = Double.MIN_VALUE;
    private final char[] operators = {'*', '+', '-', '/'};

    public SCell(){};

    public SCell(String s) {
        // Add your code here
        setData(s);
    }

    public void setValue(double value){
        this.value = value;
        calcType();
    }

    public double getValue() {
        return value;
    }

    @Override
    public int getOrder() {
        // Add your code here

        return 0;
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        this.line = s;
        calcType();
    }

    public void calcType(){
        if (line == null || line.isEmpty()){
            return;
        }
        else if (isNumber(this.line)){
            this.type = Ex2Utils.NUMBER;
        }
        else if(this.line.charAt(0) == '='){
            this.line = this.line.toUpperCase();
            if (this.value != Double.MIN_VALUE && this.value != Double.MAX_VALUE){
                this.type = Ex2Utils.ERR_FORM_FORMAT;
            }else if (this.isForm(this.line)) {
                this.type = Ex2Utils.FORM;
            }
        }
        else this.type = Ex2Utils.TEXT;
    }

    @Override
    public String getData() {
        return this.line;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int t) {
        if (this.isNumber(this.line)) this.type = Ex2Utils.NUMBER;
        else if (this.line.charAt(0) == '=') {   //Trying to be a formula
            if (this.value == Double.MIN_VALUE){
                this.type = Ex2Utils.ERR_FORM_FORMAT;
            }else if (this.value == Double.MAX_VALUE){
                this.type = Ex2Utils.ERR_CYCLE_FORM;
            }
            else{
                this.type = Ex2Utils.FORM;
            }
        } else this.type = Ex2Utils.TEXT;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here
    }

    //this function checks if the original input contains a valid numebr
    public boolean isNumber(String form) {
        try {
            this.value = Double.parseDouble(form);    //try to read in value of cell
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //this function checks if the input is not a number and not trying to be a formula
    public boolean isText() {
        return (!isNumber(this.line) && this.line.charAt(0) != 0);      //check its non of the other(and not trying to be)
    }

    //checking if the given text is a valid formula
    public boolean isForm(String form) {
        if (!this.line.isEmpty() && !Double.isNaN(this.value)) return true;
        try {
            if (form == null || form.isEmpty() || form.replace("=", "").isEmpty() || form.charAt(0) != '=')
                return false;
            else if (isNumber(form.replace("=", ""))) return true;
            else if (form.indexOf('(') > -1 && getClosingIndex(form, form.indexOf('(')) < 0)
                return false;   //doesnt have closing )
            else if (form.indexOf('(') == 1 && getClosingIndex(form, form.indexOf('(')) == form.length() - 1) {    //everything in()
                return isForm("=" + form.substring(2, form.length() - 1));
            } else if (form.matches("[^a-zA-Z]*")) {     //can we compute the content
                try {
                    double test = computeForm(form.replace("=", ""));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }catch (Exception e) {
            return false;
        }
        return false;
    }

    //this function will help me by returning the number of times a char appears in a string
    public int numOfAppears(char c, String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) count++;
        }
        return count;
    }

    //works only on the string itself(no =)
    public double computeForm(String form) {
        double ans = 0.0, nans;
        try {                                                            //no operators left in the string(contains one number)
            ans = Double.parseDouble(form);                              //read-in the number
            return ans;                                                  //return the number
        } catch (NumberFormatException |
                 InputMismatchException e) {      //cant return the string as a number(we have operators in the string)
            if (form.indexOf('(') != -1) {
                int open = form.indexOf('(');
                int close = getClosingIndex(form, open);

                //if everything is in ()
                if (open == 0 && close == form.length() - 1) {
                    return computeForm(form.substring(open + 1, close));
                }

                //if right side is in ()
                else if (open > 0 && close == form.length() - 1) {
                    return computeForm(form.substring(0, open) + Double.toString(computeForm(form.substring(open + 1, close))));
                }

                //if left side is in ()
                else if (open == 0 && close < form.length() - 1) {
                    return computeForm(Double.toString(computeForm(form.substring(open + 1, close))) + form.substring(close + 1));
                }

                //if () is in the middle of the string
                else if (open > 0 && close < form.length() - 1) {
                    return computeForm(form.substring(0, open) + Double.toString(computeForm(form.substring(open + 1, close))) + form.substring(close + 1));
                }

            }

            String newS = "";                   //we'll start building a new string to calculate
            if (form.indexOf('/') != -1) {       //if there's division in the string
                int opBefore = -1, opAfter = form.length();
                for (int i = form.indexOf('/') - 1; i > 0; i--) {
                    if (Arrays.binarySearch(operators, form.charAt(i)) > -1 && Math.abs(i - form.indexOf('/')) > 1) {
                        opBefore = i;
                        break;
                    }
                }
                for (int i = form.indexOf('/') + 1; i < form.length(); i++) {
                    if (Arrays.binarySearch(operators, form.charAt(i)) > -1 && Math.abs(i - form.indexOf('/')) > 1) {
                        opAfter = i;
                        break;
                    }
                }
                double numerator = Double.parseDouble(form.substring(opBefore + 1, form.indexOf('/')));
                double denominator = Double.parseDouble(form.substring(form.indexOf('/') + 1, opAfter));
                double newNum = numerator / denominator;
                if (opBefore > 0) {
                    for (int i = 0; i <= opBefore; i++) {
                        newS += form.charAt(i);
                    }
                }
                newS += Double.toString(newNum);
                for (int i = opAfter; i < form.length(); i++) {
                    newS += form.charAt(i);
                }
                return computeForm(newS);
            } else if (form.indexOf('*') != -1) {  //we have multiplication in the string
                int opBefore = -1, opAfter = form.length();
                for (int i = form.indexOf('*') - 1; i >= 0; i--) {
                    if (Arrays.binarySearch(operators, form.charAt(i)) > -1 && Math.abs(i - form.indexOf('*')) > 1) {       //if got to operator
                        opBefore = i;
                        break;
                    }
                }
                for (int i = form.indexOf('*') + 1; i < form.length(); i++) {
                    if (Arrays.binarySearch(operators, form.charAt(i)) > -1 && Math.abs(i - form.indexOf('*')) > 1) {       //if got to operator
                        opAfter = i;
                        break;
                    }
                }
                double first = Double.parseDouble(form.substring(opBefore + 1, form.indexOf('*')));
                double second = Double.parseDouble(form.substring(form.indexOf('*') + 1, opAfter));
                double newNum = first * second;
                for (int i = 0; i <= opBefore; i++) {
                    newS += form.charAt(i);
                }
                newS += Double.toString(newNum);
                for (int i = opAfter; i < form.length(); i++) {
                    newS += form.charAt(i);
                }
                return computeForm(newS);
            }
            //No prioritized operator found in the string (ony + or -)
            for (int i = 0; i < form.length(); i++) {
                if (form.charAt(i) == '+') {
                    ans = Double.parseDouble(newS);
                    return ans + computeForm(form.substring(i + 1));
                } else if (i > 0 && form.charAt(i) == '-') {
                    ans = Double.parseDouble(newS);
                    return ans + computeForm(form.substring(i));
                } else newS += form.charAt(i);
            }
            return Double.parseDouble(newS);       //default value(should get here)
        }
    }

    //This function will help me by giving me the index of the closing )
    public int getClosingIndex(String form, int open) {
        int count = 1;
        for (int i = open + 1; i < form.lastIndexOf(')') + 1; i++) {
            if (form.charAt(i) == '(') count++;
            else if (form.charAt(i) == ')') {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }

}