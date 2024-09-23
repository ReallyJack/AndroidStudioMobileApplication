package com.example.ttf;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Product product;

    @Before
    public void setUp() {
        product = new Product("123", "Test Product", 50);
    }

    @Test
    public void testProductCreation() {
        assertEquals("123", product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(50, product.getStockCount());
    }

    @Test
    public void testStockCountUpdate() {
        product.setStockCount(75);
        assertEquals(75, product.getStockCount());
    }
}