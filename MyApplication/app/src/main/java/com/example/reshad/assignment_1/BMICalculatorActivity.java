package com.example.reshad.assignment_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BMICalculatorActivity extends AppCompatActivity{
    private EditText body_weight, lengthTxt;
    private String weight, length;
    private TextView bmiView;
    private boolean calc_boolean;
    private int weight_double;
    private double lengthDouble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_activity);
        body_weight = (EditText)findViewById(R.id.weight_id);
        body_weight.setHint("Your weight in Kg");

        lengthTxt = (EditText)findViewById(R.id.length_id);
        lengthTxt.setHint("Your length in cm");

        bmiView = (TextView)findViewById(R.id.result_view_id);

        Button calc_btn = (Button)findViewById(R.id.compute_button);
        Button rst_btn = (Button)findViewById(R.id.reset_button);

        rst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body_weight.setText(null);
                lengthTxt.setText(null);
                bmiView.setText(null);
            }
        });
        calc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    weight = body_weight.getText().toString();
                    length = lengthTxt.getText().toString();
                    calc_boolean = true;
                }
                catch(NullPointerException e)
                {
                    Toast.makeText(getApplicationContext(), "Type your weight and length", Toast.LENGTH_LONG).show();
                    calc_boolean = false;
                }
                if(calc_boolean)
                {
                    try
                    {
                        weight_double = Integer.parseInt(weight);
                        lengthDouble = Double.parseDouble(length);
                        lengthDouble = lengthDouble / 100;
                        double bmi = weight_double / Math.pow(lengthDouble, 2.00);
                        bmi = Math.round(bmi);
                        bmiView.setTextSize(40);
                        bmiView.setGravity(Gravity.CENTER);
                        bmiView.setTextColor(Color.BLACK);
                        bmiView.setText("" + bmi);

                    }
                    catch(NumberFormatException e)
                    {
                        Toast.makeText(getApplicationContext(), "Type just numbers", Toast.LENGTH_LONG).show();
                        body_weight.setText(null);
                        lengthTxt.setText(null);
                    }
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
