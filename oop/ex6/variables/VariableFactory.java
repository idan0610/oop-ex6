package oop.ex6.variables;

import java.util.HashMap;

/**
 * A factory for the different variables.
 */
public class VariableFactory {

    // These are messages to output when errors occur.
    public static final String FINAL_UNINITIALIZED_MESSAGE = "Final variables must declared initilaized";
    public static final String DUPLICATE_VARIABLES_MESSAGE = "No duplicate variables allowed";

    // Length of the sub-array with 'final' prefix. To use in the second factory method.
    private static final int LENGTH_WITH_FINAL = 3;

    /**
     * Create variables of a specific given type, with the given parameters.
     * This method create all of the arguments with the same type and same 'isFinal'.
     * Each variable can be created with initialization or without it.
     * The formant of the input array is: variable name and possible assignment (for each variable).
     *
     * @param isFinal are the variable final or not.
     * @param typeStr the type of the variables to create.
     * @param arguments the string represents the creation of the variables.
     * @param sourceVariables the source variables, to use when assigning another variable.
     * @return a new HashMap of the created variables.
     * @throws VariableException if an error occurred while trying to created the variables.
     */
    public static HashMap<String, Variable> createVariables
            (boolean isFinal, String typeStr, String[][] arguments, HashMap<String,Variable> sourceVariables)
            throws VariableException {

        HashMap<String, Variable> createdVariables = new HashMap<>();
        Type type = Type.getTypeFromString(typeStr);
        Variable variable, variableToAssign;
        String variableName, variableArgument;

        for (String[] singleVarDeclaration: arguments){
            variableName = singleVarDeclaration[0];

            // Check the factory already created a variable with that name.
            // No duplicate variables can be created in the same command!
            if (createdVariables.containsKey(variableName))
                throw new VariableException(DUPLICATE_VARIABLES_MESSAGE);

            // Check if the variable is assigned with a value at declaration.
            if (singleVarDeclaration.length > 1){
                variableArgument = singleVarDeclaration[1];

                // Check if the variableArgument is another variable to assign with.
                if (sourceVariables.containsKey(variableArgument)){
                    variableToAssign = sourceVariables.get(variableArgument);
                    variable = new Variable(type, variableName, variableToAssign, isFinal);
                }
                // Check if the variable is assigned with another variable that was created previously
                // in this current variables declaration.
                else if (createdVariables.containsKey(variableArgument)){
                    variableToAssign = createdVariables.get(variableArgument);
                    variable = new Variable(type, variableName, variableToAssign, isFinal);
                }
                // The variableArgument is not an existing variable.
                // Try to create a new variable with that variableArgument as a given argument.
                else{
                    variable = new Variable(type, variableName, variableArgument, isFinal);
                }
            }
            else {   // Variable is declared without initialization.

                // Check if declared final - final variables can not be declared without initialization!
                if (isFinal)
                    throw new VariableException(FINAL_UNINITIALIZED_MESSAGE);

                variable = new Variable(type, variableName);
            }

            createdVariables.put(variableName, variable);
        }

        return createdVariables;
    }

    /**
     * Create a HashMap of initialized variables, which can be of different types.
     * The variables will be constructed as initialized (maybe final) variables.
     * THe format of the input array is: possible "final" prefix, type and name (for each variable).
     *
     * @param vars the strings array represents each variable.
     * @return the new created variables.
     * @throws VariableException if there are duplicate variables given.
     */
    public static HashMap<String, Variable> createVariables(String[][] vars)
            throws VariableException {

        HashMap<String, Variable> createdVariables = new HashMap<>();
        Variable variable;
        boolean isFinal;
        String variableName;
        Type type;
        int index;

        // Run through each argument (which maybe contains final, and afterwards type and name).
        for (String[] singleVar: vars){

            // if singleVar length is LENGTH_WITH_FINAL, it means that the variable was declared final.
            if (singleVar.length == LENGTH_WITH_FINAL){
                isFinal = true;
                index = 1;      // index is the next index of the parameters.
            }
            else{   // The variable is declared non-final.
                isFinal = false;
                index = 0;      // index is the next index of the parameters.
            }

            type = Type.getTypeFromString(singleVar[index]);
            variableName = singleVar[++index];

            // Check for duplication.
            if (createdVariables.containsKey(variableName))
                throw new VariableException(DUPLICATE_VARIABLES_MESSAGE);

            variable = new Variable(type, variableName, isFinal);
            createdVariables.put(variableName, variable);
        }

        return createdVariables;
    }
}
