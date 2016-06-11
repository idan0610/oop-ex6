package oop.ex6.scopes;

import java.util.HashMap;
import java.util.Map;

import oop.ex6.variables.Variable;

/**
 * This class represents a structure initiates a new Scope on the s-java file.
 * @author Idan
 */
public class Scope {

	private HashMap<String, Variable> scopeVariables;
	private HashMap<String, Variable> upperScopeVariables;
	private boolean isMethod;
	
	/**
	 * Initiates the scope with 2 HashMap tables, the first keeping the Variable objects initialized on
	 * this scope, the second keeping all the Variable objects initialized on upper scopes.
	 * @param upperScopeVariables The upper scopes Variable objects HashMap
	 */
	public Scope(HashMap<String, Variable> upperScopeVariables, boolean isMethod) {
		this.scopeVariables = new HashMap<>();
		this.upperScopeVariables = upperScopeVariables;
		this.isMethod = isMethod;
	}
	
	/**
	 * @return The HashMap table containing the current scope's variables.
	 */
	public HashMap<String, Variable> getScopeVariables() {
		return new HashMap<>(scopeVariables);
	}
	
	/**
	 * @return The HashMap table containing the upper scopes's variables.
	 */
	public HashMap<String, Variable> getUpperScopeVariables() {
		return new HashMap<>(upperScopeVariables);
	}

	/**
	 * Add a new Variable object to the scope variables HashMap.
	 * @param var The Variable
	 */
	public void addVariable(Variable var) {
		scopeVariables.put(var.getName(), var);
	}

	/**
	 * Add the given Map of variables to the scope variables HashMap.
	 * @param variables the variables to add.
	 */
	public void addVariables(Map<String, Variable> variables){
		scopeVariables.putAll(variables);
	}

	/**
	 * Check if a varaiable is contained in the scope variables HashMap.
	 * @param varName the variable to check
	 * @return true if the variable is contained, false otherwise.
	 */
	public boolean containVariable(String varName) {
		return scopeVariables.containsKey(varName);
	}
	
	/**
	 * Returns the Variable whose name is varName
	 * @param varName The name of the variable
	 * @return The variable, or null if there is no variable with the name as varName
	 */
	public Variable getVariable(String varName) {
		Variable var = scopeVariables.get(varName);
		
		if (var == null) {
			var = upperScopeVariables.get(varName);
		}
		
		return var;
	}
	
	/**
	 * @return true if the scope is a method
	 */
	public boolean getIsMethod() {
		return isMethod;
	}
}