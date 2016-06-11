package oop.ex6.method_signatures;

import java.util.regex.Matcher;

import oop.ex6.main.RegexPatterns;
import oop.ex6.scopes.Scope;
import oop.ex6.variables.Type;
import oop.ex6.variables.Variable;

/**
 * This class represent a method's signature.
 */
public class MethodSignature {
    private Type[] acceptedArgumentsTypes;

    /**
     * Construct a new instance of MethodSignature, with the given variables.
     *
     * @param acceptedArgumentsTypes types that the method received as given arguments.
     */
    public MethodSignature(Type[] acceptedArgumentsTypes){
        this.acceptedArgumentsTypes = acceptedArgumentsTypes;
    }

    /**
     * The method check if the method signature accept given arguments.
     *
     * @param values argumants to check.
     * @return true if the method accept the given arguments, and false otherwise.
     */
    public boolean accept(String[] values, Scope scope) {
    	// If the number of given arguments is not the same of arguments of the method signature.
    	if(values.length != acceptedArgumentsTypes.length) {
    		return false;
    	}
    	
    	Matcher matcher;
    	
    	// For each method signature argument, check if the corresponding given argument match. 
    	for(int i = 0; i < acceptedArgumentsTypes.length; i++) {
    		Variable variable = scope.getVariable(values[i]);
    		if(variable != null && variable.getType() == acceptedArgumentsTypes[i]) {
    			continue;
    		}
    		
    		if(acceptedArgumentsTypes[i] == Type.BOOLEAN) {
    			matcher = RegexPatterns.BOOLEAN.matcher(values[i]);
    			if(!matcher.matches()) {
    				return false;
    			}
    		}
    		else if(acceptedArgumentsTypes[i] == Type.INTEGER) {
    			matcher = RegexPatterns.INTEGER.matcher(values[i]);
    			if(!matcher.matches()) {
    				return false;
    			}
    		}
    		else if(acceptedArgumentsTypes[i] == Type.DOUBLE) {
    			matcher = RegexPatterns.DOUBLE.matcher(values[i]);
    			if(!matcher.matches()) {
    				return false;
    			}
    		}
    		else if(acceptedArgumentsTypes[i] == Type.CHARACTER) {
    			matcher = RegexPatterns.CHARACTER.matcher(values[i]);
    			if(!matcher.matches()) {
    				return false;
    			}
    		}
    		else if(acceptedArgumentsTypes[i] == Type.STRING) {
    			matcher = RegexPatterns.STRING.matcher(values[i]);
    			if(!matcher.matches()) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
}
