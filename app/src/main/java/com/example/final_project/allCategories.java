package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class allCategories extends AppCompatActivity implements View.OnClickListener {

    TextView engineButton, suspensionButton, exhaustButton, lightsButton, ignitionButton, airButton, brakeButton,
    exteriorButton, fastenersButton, gasketButton, interiorButton, marineButton, mobileButton, oilsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        engineButton = findViewById(R.id.engineAllButton);
        engineButton.setOnClickListener(this);
        suspensionButton = findViewById(R.id.chassisAllButton);
        suspensionButton.setOnClickListener(this);
        exhaustButton = findViewById(R.id.exhaustAllButton);
        exhaustButton.setOnClickListener(this);
        lightsButton = findViewById(R.id.lightsAllButton);
        lightsButton.setOnClickListener(this);
        ignitionButton = findViewById(R.id.ignitionAllButton);
        ignitionButton.setOnClickListener(this);
        airButton = findViewById(R.id.airFuelButton);
        airButton.setOnClickListener(this);
        brakeButton = findViewById(R.id.brakeButton);
        brakeButton.setOnClickListener(this);
        exteriorButton = findViewById(R.id.exteriorAllButton);
        exteriorButton.setOnClickListener(this);
        fastenersButton = findViewById(R.id.fastenersAllButton);
        fastenersButton.setOnClickListener(this);
        gasketButton = findViewById(R.id.gasketAllButton);
        gasketButton.setOnClickListener(this);
        interiorButton = findViewById(R.id.interiorAllButton);
        interiorButton.setOnClickListener(this);
        marineButton = findViewById(R.id.marineAllButton);
        marineButton.setOnClickListener(this);
        mobileButton = findViewById(R.id.mobileAllButton);
        mobileButton.setOnClickListener(this);
        oilsButton = findViewById(R.id.oilsAllButton);
        oilsButton.setOnClickListener(this);






    }

    @Override
    public void onClick(View view) {

        if (view.getId() == engineButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Engines & Components");
            startActivity(intent);
        }

        else if (view.getId() == airButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Air & Fuel");
            startActivity(intent);
        }

        else if (view.getId() == brakeButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Brake Systems");
            startActivity(intent);
        }

        else if (view.getId() == suspensionButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Chassis & Suspension");
            startActivity(intent);
        }

        else if (view.getId() == engineButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Engines & Components");
            startActivity(intent);
        }


        else if (view.getId() == exteriorButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Exterior & Accessories");
            startActivity(intent);
        }


        else if (view.getId() == fastenersButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Fasteners & Hardware");
            startActivity(intent);
        }


        else if (view.getId() == gasketButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Gasket & Seal");
            startActivity(intent);
        }

        else if (view.getId() == exhaustButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Exhaust");
            startActivity(intent);
        }

        else if (view.getId() == ignitionButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Ignition & Electrical");
            startActivity(intent);
        }


        else if (view.getId() == interiorButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Interior & Accessories");
            startActivity(intent);
        }

        else if (view.getId() == lightsButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Lights & Lightning");
            startActivity(intent);
        }

        else if (view.getId() == marineButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Marine");
            startActivity(intent);
        }

        else if (view.getId() == mobileButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Mobile Electronics");
            startActivity(intent);
        }

        else if (view.getId() == oilsButton.getId()){

            Intent intent = new Intent(allCategories.this, categoriesFilter.class);

            intent.putExtra("CATEGORY", "Oils, Fluids & Sealer");
            startActivity(intent);

        }





    }
}
