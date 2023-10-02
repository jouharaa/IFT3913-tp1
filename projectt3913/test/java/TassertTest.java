import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TassertTest {
    private Calculatrice calculatrice;

    @Before
    public void setUp() {
        calculatrice = new Calculatrice();
    }

    @Test
    public void testAdditionner() {
        assertEquals(5, calculatrice.additionner(2, 3));
    }

    @Test
    public void testSoustraire() {
        assertEquals(-1, calculatrice.soustraire(2, 3));
    }

    @Test
    public void testMultiplier() {
        assertEquals(6, calculatrice.multiplier(2, 3));
    }

    @Test
    public void testDiviser() {
        assertEquals(2, calculatrice.diviser(6, 3));
    }

    @Test(expected = ArithmeticException.class)
    public void testDiviserParZero() {
        calculatrice.diviser(1, 0);
    }
}
