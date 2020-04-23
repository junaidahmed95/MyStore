package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mystore.Adapter.ProductAdapter;
import com.example.mystore.Model.Product;
import com.example.mystore.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.mystore.Adapter.ProductAdapter.selectedProducts;
import static com.example.mystore.ui.home.HomeFragment.forWhat;

public class AllProductsActivity extends AppCompatActivity {

    private ArrayList<Product> productList;
    private GridView gridView;
    private Toolbar toolbar;
    public static boolean myflag = false;
    public static String now = "no";
    private String inChat = "no";
    private ProductAdapter gridProductAdapter;
    public static FloatingActionButton mChatFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        toolbar = findViewById(R.id.appBar);
        toolbar.setTitle("Corner Store");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        now = "yes";
        gridView = findViewById(R.id.grid_view);
        mChatFab = findViewById(R.id.cartFab);

        productList = new ArrayList<>();







        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));
        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));
        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));
        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));
        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));
        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));
        productList.add(new Product(R.drawable.cocomo, 10, "Biscuit"));
        productList.add(new Product(R.drawable.slice, 20, "Juice"));


        gridProductAdapter = new ProductAdapter(productList);
        gridView.setAdapter(gridProductAdapter);
        gridProductAdapter.notifyDataSetChanged();

        mChatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllProductsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(AllProductsActivity.this,MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("forcart","yes");
//                startActivity(intent);
//                if(inChat.equals("yes")){
//                    Intent intent = new Intent(AllProductsActivity.this,MessagingActivity.class);
//                    //intent.putParcelableArrayListExtra("some_key", (ArrayList<? extends Parcelable>) productList);
//                    startActivity(intent);
//                    intent.putExtra("user_id","DTTCF5Tjeeerckb3cNCgKLJBImg2");
//                    startActivity(intent);
//                }else {
//                    myflag = true;
//                    toolbar.setTitle("Selected Products");
//                    gridProductAdapter = new ProductAdapter(selectedProducts);
//                    gridView.setAdapter(gridProductAdapter);
//                    gridProductAdapter.notifyDataSetChanged();
//                    inChat = "yes";
//                }

            }
        });

    }

    @Override
    public void onBackPressed() {
//        if (inChat.equals("no")) {
//            super.onBackPressed();
//            ProductAdapter.selected = "no";
//            now = "no";
//            myflag = false;
//        } else {
//            myflag = false;
//            inChat = "no";
//            mChatFab.hide();
//            ProductAdapter.selected = "no";
//            toolbar.setTitle("Corner Store");
//            selectedProducts.clear();
//            gridProductAdapter = new ProductAdapter(productList);
//            gridView.setAdapter(gridProductAdapter);
//            gridProductAdapter.notifyDataSetChanged();
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
//            if (inChat.equals("no")) {
//                ProductAdapter.selected = "no";
//                now = "no";
//                myflag = false;
//                finish();
//            } else {
//                inChat = "no";
//                myflag = false;
//                mChatFab.hide();
//                ProductAdapter.selected = "no";
//                toolbar.setTitle("Corner Store");
//                selectedProducts.clear();
//                gridProductAdapter = new ProductAdapter(productList);
//                gridView.setAdapter(gridProductAdapter);
//                gridProductAdapter.notifyDataSetChanged();
//            }
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}
