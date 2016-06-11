package oop.ex6.variables;

/**
 * This class represent a variable.
 * A variable can be of a specific Type (String, char, int, double, boolean, etc.).
 */
public class Variable {

    // These are messages to output when errors occur.
    public static final String FINAL_CHANGED_MESSAGE = "Final variable can not be changed";
    public static final String INVALID_ARGUMENT_MESSAGE = "Invalid argument";
    public static final String ACCESS_UNINITIALIZED_VARIABLE = "Access uninitialized variable";

    protected boolean isInitialized;
    protected boolean isFinal;
    protected Type type;
    protected String name;

    /**
     * Construct a new un-initialized Variable.
     * Note: This Variable can not be final, because final variables must be initialized at declaration.
     *
     * @param type the type of the variable.
     * @param name the name of the variable.
     */
    public Variable(Type type, String name){
        this.isInitialized = false;
        this.isFinal = false;
        this.type = type;
        this.name = name;
    }

    /**
     * Construct a new initialized variable, with no explicit argument given.
     *
     * @param type the type of the variable
     * @param name the name of the variable.
     * @param isFinal is the variable declared final or not.
     */
    public Variable(Type type, String name, boolean isFinal){
        this(type, name);

        this.isInitialized = true;
        this.isFinal = isFinal;
    }

    /**
     * Construct a new initialized Variable, and validate the given argument.
     *
     * @param type the type of the variable
     * @param name the name of the variable.
     * @param argument the argument to initialize with.
     * @param isFinal is the variable declared final or not.
     * @throws VariableException if the argument given is invalid.
     */
    public Variable(Type type, String name, String argument, boolean isFinal)
            throws VariableException {

        this(type, name, isFinal);  // Calls the constructor which construct a new initialized variable.

        if (! isArgumentValid(argument))
            throw new VariableException(INVALID_ARGUMENT_MESSAGE);
    }

    /**
     * Construct a new initialized Variable, and validate the given argument.
     *
     * @param type the type of the variable
     * @param name the name of the variable.
     * @param variableToAssign the variable to assign into the new variable with.
     * @param isFinal is the variable declared final or not.
     * @throws VariableException if the variable to assign is invalid.
     */
    public Variable(Type type, String name, Variable variableToAssign, boolean isFinal)
            throws VariableException {

        this(type, name, isFinal);  // Calls the constructor which construct a new initialized variable.

        checkVariableToAssignIsValid(variableToAssign);
    }

    /**
     * A copy-constructor.
     *
     * @param variableToCopy the variable to copy.
     */
    public Variable(Variable variableToCopy){
        this.isInitialized = variableToCopy.isInitialized;
        this.isFinal = variableToCopy.isFinal;
        this.type = variableToCopy.type;
        this.name = variableToCopy.name;
    }

    /**
     * Check if the variable  to assign is a valid argument for the new variable.
     * it means that the types matches and the new variable is initialized.
     *
     * @param variableToAssign the variable to assign
     * @throws VariableException if the argument given is invalid.
     */
    private void checkVariableToAssignIsValid(Variable variableToAssign) throws VariableException{
        // Check if the variable to assign is initialized.
        if (!variableToAssign.isInitialized){
            throw new VariableException(ACCESS_UNINITIALIZED_VARIABLE);
        }
        // Check if the types matches.
        if (this.type != variableToAssign.getType()) {
            // An exception is that double can be assigned with int.
            if (!(this.type == Type.DOUBLE && variableToAssign.type == Type.INTEGER)) {
                throw new VariableException(INVALID_ARGUMENT_MESSAGE);
            }
        }
    }

    /**
     * Change the variable's value to the given argument.
     *
     * @param argument the argument to set the variable value.
     * @throws VariableException if the argument is invalid or variable is final and can not be changed.
     */
    public void changeValue(String argument) throws VariableException {
        if (this.isFinal) {
            throw new VariableException(FINAL_CHANGED_MESSAGE);
        }
        if (! this.isArgumentValid(argument)) {
            throw new VariableException(INVALID_ARGUMENT_MESSAGE);
        }

        this.isInitialized = true;
    }

    /**
     * Change the variable's value to the given argument.
     *
     * @param variableToAssign the variable to assign with.
     * @throws VariableException if the argument is invalid or variable is final and can not be changed.
     */
    public void changeValue(Variable variableToAssign) throws VariableException {

        if (this.isFinal) {
            throw new VariableException(FINAL_CHANGED_MESSAGE);
        }
            
        checkVariableToAssignIsValid(variableToAssign);

        this.isInitialized = true;
    }

    private boolean isArgumentValid(String argument){
        return this.type.accept(argument);
    }

    /**
     * @return the Variable's type.
     */
    public Type getType(){
        return this.type;
    }

    /**
     * @return the Variable's name.
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * @return true if the variable has been initialized, false otherwise
     */
    public boolean isInitialized(){
    	return this.isInitialized;
    }
}
