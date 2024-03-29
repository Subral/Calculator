package com.example.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	TextView resultTextView, expressionTextView;
	String currentExpression = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		resultTextView = findViewById(R.id.result);
		expressionTextView = findViewById(R.id.solution);

		// Set click listeners for buttons
		int[] buttonIds = {R.id.button0, R.id.button1, R.id.button2, R.id.button3,
				R.id.button4, R.id.button5, R.id.button6, R.id.button7,
				R.id.button8, R.id.button9, R.id.button_add, R.id.button_sub,
				R.id.button_mul, R.id.button_devide, R.id.button_c,
				R.id.button_equal, R.id.button_dot};

		for (int id : buttonIds) {
			findViewById(id).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		Button button = (Button) v;
		String buttonText = button.getText().toString();

		switch (buttonText) {
			case "=":
				evaluateExpression();
				break;
			case "C":
				clearExpression();
				break;
			default:
				appendToExpression(buttonText);
				break;
		}
	}

	private void appendToExpression(String text) {
		currentExpression += text;
		expressionTextView.setText(currentExpression);
	}

	private void evaluateExpression() {
		Context rhino = Context.enter();
		rhino.setOptimizationLevel(-1); // Turn off optimization to avoid arithmetic rounding issues
		Scriptable scope = rhino.initStandardObjects();

		try {
			Object result = rhino.evaluateString(scope, currentExpression, "javascript", 1, null);
			double doubleResult = Double.parseDouble(String.valueOf(result));

			// Check if the result is an integer
			if (doubleResult % 1 == 0) {
				resultTextView.setText(String.valueOf((int) doubleResult)); // Display integer result
			} else {
				resultTextView.setText(String.valueOf(doubleResult)); // Display floating-point result
			}
		} catch (Exception e) {
			resultTextView.setText("Error");
		}

		currentExpression = ""; // Reset the expression
		expressionTextView.setText("");
	}


	private void clearExpression() {
		currentExpression = "";
		expressionTextView.setText("");
		resultTextView.setText("");
	}
}
