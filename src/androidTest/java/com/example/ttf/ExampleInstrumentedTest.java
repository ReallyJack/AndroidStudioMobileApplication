package com.example.ttf;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private DatabaseReference databaseReference;

    @Before
    public void setUp() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        addProduct("test_id", "test_name", 1);
    }

    private void addProduct(String id, String name, int stock) {
        Product product = new Product(id, name, stock);
        databaseReference.child(id).setValue(product);
    }

    @Test
    public void testNavigateToStockPageAndUpdateStock() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginbtn)).perform(click());
        onView(withId(R.id.stock)).perform(click());

        onView(withId(R.id.titleText))
                .check(matches(isDisplayed()));

        onView(withText("Update existing stock or add new stock"))
                .check(matches(isDisplayed()));

        String productId = "test_id";
        String productName = "test_name";
        int productStock = 1;

        onView(withText("ID: " + productId + " Name: " + productName + " Remaining Stock: " + productStock))
                .check(matches(isDisplayed()));

        CountDownLatch latch = new CountDownLatch(1);
        databaseReference.child(productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                Product product = dataSnapshot.getValue(Product.class);
                assert product != null;

                //updating entry to 2 (1 + 1 = 2) as entry was created with value 1
                int updatedStock = product.getStockCount() + 1;
                product.setStockCount(updatedStock);

                databaseReference.child(productId).setValue(product).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        latch.countDown();
                    }
                });
            }
        });
        latch.await();
        onView(withText("ID: " + productId + " Name: " + productName + " Remaining Stock: " + (productStock + 1))).check(matches(isDisplayed()));
    }
}