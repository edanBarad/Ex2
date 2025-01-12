
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCell_Tests {

    @Test
    public void isNumberTest(){
        SCell test = new SCell();
        String[] valid = new String[]{"6", "2", "6453", "-24", "14", "0"};
        for (int i = 0; i < valid.length; i++){
            test.setData(valid[i]);
            assertTrue(test.isNumber(test.getData()));
        }
        String[] invalid = new String[] {"=1", "=(1.2)", "=(0.2)", "=1+2", "=1+2*3", "=(1+2)*((3))-1"};
        for (int i = 0; i < invalid.length; i++){
            assertFalse(test.isNumber(invalid[i]));
        }
    }

    @Test
    public void isFormTest(){
        SCell test = new SCell();
        String[] valid = new String[] {"=1", "=(1.2)", "=(0.2)", "=1+2", "=1+2*3", "=(1+2)*((3))-1"};
        for (int i = 0; i < valid.length; i++){
            test.setData(valid[i]);
            assertTrue(test.isForm(test.getData()));
        }
        String[] invalid = new String[] {"a", "AB", "@2", "2+)", "(3+1*2)-", "=()", "=5**"};
        for (int i = 0; i < invalid.length; i++){
            assertFalse(test.isForm(invalid[i]));
        }
    }

    @Test
    public void computeFormTest(){
        SCell test = new SCell();
        Assert.assertEquals(5.0, test.computeForm("=1+2*2".replace("=", "")), 0);
        Assert.assertEquals(5.0, test.computeForm("=((1+2)*2)-1".replace("=", "")), 0);
        Assert.assertEquals(2.0, test.computeForm("=4+4+(4*4/(4*4/(4+4)-(4*4)/4-(4*4)+4*4)/4-4)".replace("=", "")), 0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, test.computeForm("=5/0".replace("=", "")), 0);
    }

    @Test
    public void getClosingIndexTest(){
        SCell test = new SCell();
        String[] forms = new String[] {"=1", "=(1.2)", "=(0.2)", "=(1+2)*((3))-1"};
        Assert.assertEquals(-1, test.getClosingIndex(forms[0], forms[0].indexOf('(')));
        Assert.assertEquals(5, test.getClosingIndex(forms[1], forms[1].indexOf('(')));
        Assert.assertEquals(5, test.getClosingIndex(forms[2], forms[2].indexOf('(')));
        Assert.assertEquals(5, test.getClosingIndex(forms[3], forms[3].indexOf('(')));
        Assert.assertEquals(11, test.getClosingIndex(forms[3], 7));
    }





}
