package com.example.ttf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Keep;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {

    @Keep
    public SecondFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        EditText edit_id = view.findViewById(R.id.edit_id);
        EditText edit_name = view.findViewById(R.id.edit_name);
        EditText edit_stock_value = view.findViewById(R.id.edit_stock_value);
        Button submitbtn = view.findViewById(R.id.submitbtn);
        LinearLayout productsLayout = view.findViewById(R.id.productsLayout);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        submitbtn.setOnClickListener(v -> {
            String id = edit_id.getText().toString();
            String name = edit_name.getText().toString();
            int stockValue;

            if (id.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a Product ID", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a Product Name", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                stockValue = Integer.parseInt(edit_stock_value.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Please enter a valid Stock Value", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean productExists = false;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null && product.getName().equals(name)) {
                            product.setStockCount(product.getStockCount() + stockValue);
                            databaseReference.child(id).setValue(product)
                                    .addOnSuccessListener(suc -> Toast.makeText(getActivity(), "Stock updated", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(er -> Toast.makeText(getActivity(), "Error: " + er.getMessage(), Toast.LENGTH_SHORT).show());
                            productExists = true;
                            break;
                        }
                    }

                    if (!productExists) {
                        Product newProduct = new Product(id, name, stockValue);
                        databaseReference.child(id).setValue(newProduct)
                                .addOnSuccessListener(suc -> Toast.makeText(getActivity(), "Record added", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(er -> Toast.makeText(getActivity(), "Error: " + er.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Failed to check product.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productsLayout.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);

                    if (product != null) {
                        TextView productView = new TextView(getContext());
                        productView.setText("ID: " + product.getId() + " Name: " + product.getName() + " Remaining Stock: " + product.getStockCount());
                        productView.setPadding(16, 16, 16, 16);

                        productView.setOnClickListener(v -> {
                            edit_id.setText(product.getId());
                            edit_name.setText(product.getName());
                            edit_stock_value.setText(String.valueOf(product.getStockCount()));
                        });

                        productsLayout.addView(productView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

