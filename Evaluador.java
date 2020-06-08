import java.util.Stack;

import OperacionesModule.*;

public class Evaluador {
    private Operaciones operaciones;
	
    private char operators[] = { '*', '-', '+', '/', '^', 'm', '√' };
    
    public Evaluador(Operaciones operaciones) {
    	this.operaciones = operaciones;
    }
    
    public String evaluar(String expresion) throws NumberFormatException, UnsupportedOperationException {
    if(expresion.isEmpty()) return expresion;
	char[] tokens = expresion.toCharArray();

	// Pila para los números : 'valores'
	Stack<String> valores = new Stack<String>();

	// Pila de operadores: 'ops'
	Stack<Character> ops = new Stack<Character>();

	for (int i = 0; i < tokens.length; i++) {
	    // Si el token actual es un espacio, lo ignoramos
	    if (tokens[i] == ' ')
		continue;
	    if (tokens[i] == '√') {
		valores.push("2");

	    }
	    // El token actual es un numero o un punto lo empujamos a 'valores'
	    if ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.') {
		StringBuffer sbuf = new StringBuffer();
		// El número puede tener más de un dígito
		while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.'))
		    sbuf.append(tokens[i++]);
		valores.push(sbuf.toString());
	    }

	    // El token actual es un parentesis izq., lo empujamos a 'ops'
	    else if (tokens[i] == '(')
		ops.push(tokens[i]);

	    // Se encontró el parentesis de cierre, evaluamos las operaciones
	    // pendientes.
	    else if (tokens[i] == ')') {
		while (ops.peek() != '(')
		    valores.push(
			    aplicarOp(ops.pop(), Float.parseFloat(valores.pop()), Float.parseFloat(valores.pop())));
		ops.pop();
	    }

	    // El token actual es un operador
	    else if (esOperador(tokens[i])) {

		// Mientras el tope de 'ops' tenga precedencia igual o mayor
		// al token actual, el cual es un operador. Aplicamos la
		// operacion
		// en el tope de 'ops' a los dos elementos al tope de 'valores'.
		while (!ops.empty() && tienePrecedencia(tokens[i], ops.peek()))
		    valores.push(
			    aplicarOp(ops.pop(), Float.parseFloat(valores.pop()), Float.parseFloat(valores.pop())));

		// Push current token to 'ops'.
		ops.push(tokens[i]);
	    }
	}

	// La expresión ya se ha parseado por completo
	// aplicamos los operadores restatnes.
	while (!ops.empty())
	    valores.push(aplicarOp(ops.pop(), Float.parseFloat(valores.pop()), Float.parseFloat(valores.pop())));

	// El tope en 'valores' tiene el resultado final.
	return valores.pop();
    }

    public boolean esOperador(char op) {

	for (char c : operators) {
	    if (op == c)
		return true;
	}
	return false;
    }

    // Regresa true si op2 tiene mayor precedencia que op1
    // de otra manera regresa false.
    public boolean tienePrecedencia(char op1, char op2) {
	if (op2 == '(' || op2 == ')')
	    return false;
	if (op1 == '√' || op1 == '^')
	    return false;
	if ((op1 == '*' || op1 == '/' || op1 == 'm') && (op2 == '+' || op2 == '-'))
	    return false;
	else
	    return true;
    }

    // metodo para aplicar el operador op con los argumentos a y b
    public String aplicarOp(char op, float b, float a) throws UnsupportedOperationException {
	
	switch (op) {
	case '+':
	    return operaciones.suma(a, b);
	case '-':
	    return operaciones.resta(a, b);
	case '*':
	    return operaciones.multiplicacion(a, b);
	case '/':
	    if (b == 0) {
	    	return "No se puede divir entre cero";	
	    }	    	
	    return operaciones.division(a, b);
	case '^':
	    return operaciones.potenciaN(a, b);
	case 'm':
	    return operaciones.modulo(a, b);
	case '√':
	    return operaciones.raizCuadrada(b);
	}

	return "";
    }
}
