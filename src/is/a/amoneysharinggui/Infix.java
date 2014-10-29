package is.a.amoneysharinggui;

import java.util.Stack;
import java.util.StringTokenizer;

public class Infix {
	public double infix(String expression) {
		// remove white space and add evaluation operator
		if (expression == "")
		{
			double x = 0;
			return x;
		}
		else {
			expression = expression.replaceAll("[\t\n ]", "") + "=";
			String operator = "*/+-=";
			// split up the operators from the values
			StringTokenizer tokenizer = new StringTokenizer(expression,
					operator, true);
			Stack<String> operatorStack = new Stack<String>();
			Stack<String> valueStack = new Stack<String>();
			while (tokenizer.hasMoreTokens()) {
				// add the next token to the proper stack
				String token = tokenizer.nextToken();
				if (operator.indexOf(token) < 0)
					valueStack.push(token);
				else
					operatorStack.push(token);
				// perform any pending operations
				resolve(valueStack, operatorStack);
			}
			// return the top of the value stack
			String lastOne = (String) valueStack.pop();
			try{
			return Double.parseDouble(lastOne);
			}
			catch(Exception e)
			{
				return 0;
			}
		}
	}

	public int getPriority(String op) {
		if (op.equals("*") || op.equals("/"))
			return 1;
		else if (op.equals("+") || op.equals("-"))
			return 2;
		else if (op.equals("="))
			return 3;
		else
			return Integer.MIN_VALUE;
	}

	public void resolve(Stack<String> values, Stack<String> operators) {
		while (operators.size() >= 2) {
			String first = (String) operators.pop();
			String second = (String) operators.pop();
			if (getPriority(first) < getPriority(second)) {
				operators.push(second);
				operators.push(first);
				return;
			} else {
				String firstValue = (String) values.pop();
				String secondValue;
				try
				{
				secondValue = (String) values.pop();
				}
				catch(Exception e)
				{
					secondValue = "0";
				}
				values.push(getResults(secondValue, second, firstValue));
				operators.push(first);
			}
		}
	}

	public String getResults(String operand1, String operator, String operand2) {
		// System.out.println("Performing " + operand1 + operator + operand2);
		double op1;
		double op2;
		try {
			op1 = Double.parseDouble(operand1);

		} catch (Exception e) {
			op1 = 0;

		}
		try {
			op2 = Double.parseDouble(operand2);

		} catch (Exception e) {
			op2 = 0;

		}		
		if (operator.equals("*"))
			return "" + (op1 * op2);
		else if (operator.equals("/"))
			return "" + (op1 / op2);
		else if (operator.equals("+"))
			return "" + (op1 + op2);
		else if (operator.equals("-"))
			return "" + (op1 - op2);
		else
			return "0";
	}
}