package architectgroup.fact.access.test.util;

import architectgroup.fact.access.util.CommonFunction;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: toandph
 * Date: 5/13/13
 * Time: 2:42 AM
 */
public class CommonFunctionTest {
    @Test
    public void testImplodeArray() throws Exception {
        List<String> test1List = new ArrayList<String>();
        test1List.add(0, "apple");
        test1List.add(1, "blue");
        test1List.add(2, "orange");
        assertEquals(CommonFunction.implodeArray(test1List, ";"), "apple;blue;orange");
        assertEquals(CommonFunction.implodeArray(test1List, ""), "appleblueorange");
        assertEquals(CommonFunction.implodeArray(test1List, "//"), "apple//blue//orange");

        List<String> test2List = new ArrayList<String>();
        test2List.add(0, "onlyOneString");
        assertEquals(CommonFunction.implodeArray(test2List, "anyWork"), "onlyOneString");
    }

    @Test
    public void testImplodeArrayInt() throws Exception {
        List<Integer> test1List = new ArrayList<Integer>();
        test1List.add(0, 43);
        test1List.add(1, 32);
        test1List.add(2, 55552);
        assertEquals(CommonFunction.implodeArrayInt(test1List, ";"), "43;32;55552");
        assertEquals(CommonFunction.implodeArrayInt(test1List, ""), "433255552");
        assertEquals(CommonFunction.implodeArrayInt(test1List, "//"), "43//32//55552");

        List<Integer> test2List = new ArrayList<Integer>();
        test2List.add(0, 1024);
        assertEquals(CommonFunction.implodeArrayInt(test2List, "anyWork"), "1024");
    }

    @Test
    public void testIsNumeric() throws Exception {
        assertTrue(CommonFunction.isNumeric("1034"));
        assertTrue(CommonFunction.isNumeric("024"));
        assertTrue(CommonFunction.isNumeric("0074"));
        assertTrue(CommonFunction.isNumeric("109323987"));
        assertTrue(!CommonFunction.isNumeric("dfadf"));
        assertTrue(!CommonFunction.isNumeric("8934-"));
        assertTrue(!CommonFunction.isNumeric("00043fa"));
    }

    @Test
    public void testGetHostAddress() throws Exception {

    }

    @Test
    public void testEncodeFilePath() throws Exception {

    }

    @Test
    public void testDecodeFilePath() throws Exception {

    }
}
