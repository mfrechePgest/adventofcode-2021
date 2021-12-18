import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElementTest {

    @Test
    public void parseTest1() {
        // GIVEN
        String content = "[1,2]";
        Element element = Element.parseContent(content, 0, null);

        // WHEN

        // THEN
        Assertions.assertEquals(content, element.toString());
    }

    @Test
    public void parseTest2() {
        // GIVEN
        String content = "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]";
        Element element = Element.parseContent(content, 0, null);

        // WHEN

        // THEN
        Assertions.assertEquals(content, element.toString());
    }

    @Test
    public void magnitudeTest1() {
        Element element = Element.parseContent("[[1,2],[[3,4],5]]", 0, null);

        Assertions.assertEquals(143, element.getMagnitude());
    }

    @Test
    public void magnitudeTest2() {
        Element element = Element.parseContent("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", 0, null);

        Assertions.assertEquals(1384, element.getMagnitude());
    }

    @Test
    public void magnitudeTest3() {
        Element element = Element.parseContent("[[[[1,1],[2,2]],[3,3]],[4,4]]", 0, null);

        Assertions.assertEquals(445, element.getMagnitude());
    }


    @Test
    public void additionTest1() {
        Element element1 = Element.parseContent("[1,2]", 0, null);
        Element element2 = Element.parseContent("[[3,4],5]", 0, null);

        Element sum = element1.add(element2);

        Assertions.assertEquals("[[1,2],[[3,4],5]]", sum.toString());
    }

    @Test
    public void reduceShouldExplode1() {
        Element element = Element.parseContent("[[[[[9,8],1],2],3],4]", 0, null);

        boolean b = element.reduce();

        Assertions.assertEquals("[[[[0,9],2],3],4]", element.toString());
        Assertions.assertTrue(b);
    }

    @Test
    public void reduceShouldExplode2() {
        Element element = Element.parseContent("[7,[6,[5,[4,[3,2]]]]]", 0, null);

        element.reduce();

        Assertions.assertEquals("[7,[6,[5,[7,0]]]]", element.toString());
    }

    @Test
    public void reduceShouldExplode3() {
        Element element = Element.parseContent("[[6,[5,[4,[3,2]]]],1]", 0, null);

        element.reduce();

        Assertions.assertEquals("[[6,[5,[7,0]]],3]", element.toString());
    }

    @Test
    public void reduceShouldExplode4() {
        Element element = Element.parseContent("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", 0, null);

        element.reduce();

        Assertions.assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", element.toString());
    }

    @Test
    public void reduceShouldExplode5() {
        Element element = Element.parseContent("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", 0, null);

        element.reduce();

        Assertions.assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", element.toString());
    }

    @Test
    public void addAndReduce() {
        Element elem1 = Element.parseContent("[[[[4,3],4],4],[7,[[8,4],9]]]",0, null);
        Element elem2 = Element.parseContent("[1,1]",0, null);

        Element sum = elem1.add(elem2);
        Assertions.assertEquals("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", sum.toString());

        boolean reduced = sum.reduce();
        Assertions.assertEquals("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", sum.toString());
        Assertions.assertTrue(reduced);

        reduced = sum.reduce();
        Assertions.assertEquals("[[[[0,7],4],[15,[0,13]]],[1,1]]", sum.toString());
        Assertions.assertTrue(reduced);

        reduced = sum.reduce();
        Assertions.assertEquals("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", sum.toString());
        Assertions.assertTrue(reduced);

        reduced = sum.reduce();
        Assertions.assertEquals("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", sum.toString());
        Assertions.assertTrue(reduced);

        reduced = sum.reduce();
        Assertions.assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", sum.toString());
        Assertions.assertTrue(reduced);

        reduced = sum.reduce();
        Assertions.assertFalse(reduced);
    }


    @Test
    public void addAndReduceUntilFinished() {
        Element elem1 = Element.parseContent("[[[[4,3],4],4],[7,[[8,4],9]]]",0, null);
        Element elem2 = Element.parseContent("[1,1]",0, null);

        Element sum = elem1.add(elem2);
        Assertions.assertEquals("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", sum.toString());

        sum.reduceUntilFinished();

        Assertions.assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", sum.toString());
    }

    @Test
    public void addAndReduceUntilFinished2() {
        Element elem1 = Element.parseContent("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",0, null);
        Element elem2 = Element.parseContent("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",0, null);

        Element sum = elem1.add(elem2);

        sum.reduceUntilFinished();

        Assertions.assertEquals("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]", sum.toString());
    }

    
}
