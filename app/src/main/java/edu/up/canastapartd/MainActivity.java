/**
 * @author
 *      Ryan Hunter-Bliss
 *      Sarah Ebner
 *      Lute Lillo Portero
 */

package edu.up.canastapartd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CanastaGameState state = new CanastaGameState();

        Button runTest = findViewById(R.id.test_button);
        runTest.setOnClickListener(state);

        TextView outputView = findViewById(R.id.output_view);
        state.setTextView(outputView);
    }
}