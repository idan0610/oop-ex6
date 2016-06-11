package oop.ex6.parsing;

import oop.ex6.main.*;
import oop.ex6.method_signatures.*;
import oop.ex6.scopes.*;
import oop.ex6.variables.*;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * This class is a parser for sJava files.
 */
public class Parser {

    // These are messages to output when errors occur.
    public static final String DUPLICATE_VARIABLES_MESSAGE = "No duplicate variables allowed";
    public static final String VARIABLE_NOT_FOUND_MESSAGE = "Variable not found";
    public static final String INVALID_VARIABLE_TYPE = "Invalid variable type";
    public final static String DUPLICATE_METHODS_MESSAGE = "no duplicate methods allowed";
    public final static String BRACKETS_ERROR_MESSAGE = "invalid bracket usage - missing/too much brackets";
    public final static String UNRECOGNIZED_COMMAND_MESSAGE = "unrecognized sJava command";
    public final static String MISSING_RETURN_MESSAGE = "Missing return statement";

    // These variables are used in many methods in the class, there fore declared static.
    private static BufferedReader bufferedReader;
    private static String line;

    // This is used to store the methods signatures available in the main scope.
    private static HashMap<String, MethodSignature> methodsSignatures;

    /**
     * The main function which parse the given sJava file.
     *
     * @param sJavaFile the file to parse.
     * @throws IOException
     * @throws InvalidSyntaxException
     * @throws VariableException
     * @throws MethodException 
     */
    public static void parseFile(File sJavaFile)
            throws IOException, InvalidSyntaxException, VariableException, MethodException {

        try{
            bufferedReader = new LineNumberReader(new FileReader(sJavaFile));
            bufferedReader.mark(20000);     // Used for resetting the reader.
            Scope mainScope = new Scope(new HashMap<String, Variable>(), false);
            parseMainScope(mainScope);
        }
        finally {
            bufferedReader.close();
        }
    }

    /**
     * This method parse the main scope of the file.
     *
     * @param mainScope the main scope of the sJava file.
     *
     * @throws IOException
     * @throws VariableException
     * @throws InvalidSyntaxException
     * @throws MethodException 
     */
    private static void parseMainScope(Scope mainScope)
            throws IOException, InvalidSyntaxException,  VariableException, MethodException {

        final int METHOD_ARGUMENTS_GROUP = 2;   // The group number of the method's arguments in the regex.

        // Initialize file's available methods' signatures and global variables.
        initParseMainScope(mainScope);


        // Now run through the file and parse each method.

        Matcher matcher;
        Scope methodScope;
        line = bufferedReader.readLine();

        while (line != null){
            line = line.trim();

            matcher = RegexPatterns.METHOD_DECLARATION.matcher(line);

            // Check if the current line is a method declaration, and needs to be parsed.
            if (matcher.matches()){
                HashMap<String, Variable>  globalVariablesCopy = deepCopyVariables(
                        mainScope.getScopeVariables());

                // Construct a new scope for the method.
                methodScope = new Scope(globalVariablesCopy, true);
                
                if (! RegexPatterns.POSSIBLE_WHITE_SPACES.matcher(
                        matcher.group(METHOD_ARGUMENTS_GROUP)).matches()){
                	// Add the arguments given to the method as method's scope's variables.
                    methodScope.addVariables(getMethodArguments(matcher.group(METHOD_ARGUMENTS_GROUP)));
                }

                // Now parse the method.
                scopeParser(methodScope);

                // After the method was parsed, the bufferedReader will be at the last line of the method.
            }

            line = bufferedReader.readLine();
        }
    }

    /**
     * This function get a HashMap of variables (with String keys) and return a deep copy of the HashMap.
     * @param sourceVariables the source HashMap to copy.
     * @return a deep copy of the HashMap.
     */
    private static HashMap<String, Variable> deepCopyVariables(HashMap<String, Variable> sourceVariables){
        // Create a deep copy of global variables to send as upperScopeVariables of this method.
        HashMap<String, Variable>  variablesCopy = new HashMap<>();
        Variable originalVariable;

        // Run through each key in main scope's variables and copy the corresponding variable.
        for (String variableName: sourceVariables.keySet()){
            originalVariable = sourceVariables.get(variableName);
            variablesCopy.put(variableName, new Variable(originalVariable));
        }

        return variablesCopy;
    }

    /**
     * This method generate a HashMap of the given arguments to a method.
     * This HashMap can be empty if the method received no arguments.
     *
     * @param methodArgs the string represents the given arguments.
     * @return The new created HashMap of variables.
     * @throws VariableException if there are duplicate variables given.
     */
    private static HashMap<String, Variable> getMethodArguments(String methodArgs)
            throws VariableException {

        // Check if there are arguments given to the method.
        if (RegexPatterns.POSSIBLE_WHITE_SPACES.matcher(methodArgs).matches())
            return new HashMap<>();         // Return an empty HashSet.

        // splitArgs will hold each argument of the arguments given to the method.
        String[] splitArgs = methodArgs.split(RegexStrings.POSSIBLE_WHITE_SPACES +
                                              ReservedWords.COMMA + RegexStrings.POSSIBLE_WHITE_SPACES);

        // variables will split 'splitArgs' into variable's type and it name.
        String[][] vars = new String[splitArgs.length][];
        for (int i = 0; i < vars.length; i++)
            vars[i] = splitArgs[i].split(RegexStrings.WHITE_SPACES);

        // Create the variables using the Variable's Factory.
        return VariableFactory.createVariables(vars);
    }

    /**
     * Initialize file's available methods' signatures and global variables.
     *
     * @param mainScope the main scope of the file.
     *
     * @throws IOException
     * @throws VariableException
     * @throws InvalidSyntaxException
     */
    private static void initParseMainScope(Scope mainScope)
        throws IOException, InvalidSyntaxException, VariableException{

        // These constants are the different groups for catching the desired strings from the regexes.
        final int METHOD_NAME_GROUP = 1, METHOD_ARGUMENTS_GROUP = 2;
        final int FINAL_GROUP = 1, VARIABLES_TYPE_GROUP = 2, VARIABLES_DECLARATIONS_GROUP = 3;
        final int VARIABLE_NAME_GROUP = 1, VARIABLE_ARGUMENT_GROUP = 2;

        // Initialize methodSignatures, because they are the main scope's available methods.
    	methodsSignatures = new HashMap<>();

        // These matchers will be the matchers for the different commands in the read of the file.
        Matcher methodDeclarationMatcher, variableDeclarationMatcher, variableAssignmentMatcher;

        line = bufferedReader.readLine();

        // Run through the file until a 'null' row is read, and it means EOF has been reached.
        while(line != null){
            // Check if the line is a blank line or a comment line, and ignore it.
            if (isComment(line) || isBlank(line)){
                line = bufferedReader.readLine();
                continue;
            }
            line = line.trim();

            // Initialize the different matchers.
            methodDeclarationMatcher = RegexPatterns.METHOD_DECLARATION.matcher(line);
            variableDeclarationMatcher = RegexPatterns.VARIABLE_DECLARATION.matcher(line);
            variableAssignmentMatcher = RegexPatterns.VARIABLE_ASSIGNMENT.matcher(line);

            // Check if the line is a method declaration.
            if (methodDeclarationMatcher.matches()) {
                handleMethodDeclaration(methodDeclarationMatcher.group(METHOD_NAME_GROUP),
                        methodDeclarationMatcher.group(METHOD_ARGUMENTS_GROUP));
            }

            // Check if the line is a variables declaration.
            else if (variableDeclarationMatcher.matches()) {
                handleVariableDeclaration(variableDeclarationMatcher.group(FINAL_GROUP),
                                          variableDeclarationMatcher.group(VARIABLES_TYPE_GROUP),
                                          variableDeclarationMatcher.group(VARIABLES_DECLARATIONS_GROUP),
                                          mainScope);
            }

            // Check if the line is a variable assignment.
            else if (variableAssignmentMatcher.matches()){
                // group 1 is the variable's name, group 2 is the argument to be assigned.
                // Also call the corresponding method with the mainScope's variables as
                // scope variables and as source variables.
                handleVariableAssignment(variableAssignmentMatcher.group(VARIABLE_NAME_GROUP),
                        variableAssignmentMatcher.group(VARIABLE_ARGUMENT_GROUP), mainScope);
            }

            else    // The current line is not a recognized command of sJava.
                throw new InvalidSyntaxException(UNRECOGNIZED_COMMAND_MESSAGE);

            line = bufferedReader.readLine();
        }

        bufferedReader.reset();
    }
    
    /**
     * Used to parse the given scope and check if all the statements it contains are legal.
     * @param scope The scope to parse.
     * @throws IOException
     * @throws InvalidSyntaxException
     * @throws MethodException 
     */
    private static void scopeParser(Scope scope) throws IOException, InvalidSyntaxException, 
    			VariableException, MethodException {
    	
        String prevLine;
        boolean endOfScope = false;
        Matcher variableDeclarationMatcher, variableAssignmentMatcher, ifMatcher, whileMatcher,
                returnMatcher, methodCallMatcher;

        // The loop runs until the bufferedReader reaches the line containing "}".
        while(!endOfScope) {
            prevLine = line;
            line = bufferedReader.readLine();

            // If the line is empty or a comment, ignore it and continue to the next line.
            if(isBlank(line) || isComment(line)) {
                continue;
            }

            line = line.trim(); // Remove all spaces from start and end of the line.

            // Every line (Not in the main scope) may start with one of the reserved words (int, double,
            // boolean, char, string, final, if, while, return and '}') or with a name of a
            // variable or method

            variableDeclarationMatcher = RegexPatterns.VARIABLE_DECLARATION.matcher(line);
            variableAssignmentMatcher = RegexPatterns.VARIABLE_ASSIGNMENT.matcher(line);
            ifMatcher = RegexPatterns.IF_STATEMENT.matcher(line);
            returnMatcher = RegexPatterns.RETURN_STATEMENT.matcher(line);
            whileMatcher = RegexPatterns.WHILE_STATEMENT.matcher(line);
            methodCallMatcher = RegexPatterns.METHOD_CALL.matcher(line);

            if (variableDeclarationMatcher.matches()) {
            	handleVariableDeclaration(variableDeclarationMatcher.group(1),
                        variableDeclarationMatcher.group(2), variableDeclarationMatcher.group(3), scope);
            }
            else if (variableAssignmentMatcher.matches()){
            	handleVariableAssignment(variableAssignmentMatcher.group(1),
                        variableAssignmentMatcher.group(2), scope);
            }
            else if(ifMatcher.matches()) {
                // If the line starts with "if", check if the statement is legal, and if so, create a new
                // Scope object of the "if"
                String condition = ifMatcher.group(1);
                checkCondition(condition, scope);

                Scope newScope = createNewScope(scope);

                scopeParser(newScope);
            }
            else if(whileMatcher.matches()) {
                // If the line starts with "while", check if the statement is legal, and if so, create a new
                // Scope object of the "while"
                String condition = whileMatcher.group(1);
                checkCondition(condition, scope);

                Scope newScope = createNewScope(scope);

                scopeParser(newScope);
            }
            else if(methodCallMatcher.matches()) {
            	String methodName = methodCallMatcher.group(1);
            	String arguments = methodCallMatcher.group(2);
            	checkMethodCall(methodName, arguments, scope);
            }
            else if(line.equals(ReservedWords.END_SCOPE)) {
                // If the line equals to "}" (and nothing else), means the end of the scope.
                if(scope.getIsMethod()) {
                	checkEndMethod(prevLine);
                }
                endOfScope = true;
            }
            else if (!returnMatcher.matches()){
                // Every other word other then one of the reserved words mentioned or a name of variable or
                // method is illegal, ecept a 'return' statement.
                throw new InvalidSyntaxException(UNRECOGNIZED_COMMAND_MESSAGE);
            }
            
        }
    }

    /**
     * Handle variables declarations.
     * It create new variables and add them to current scope's variables.
     *
     * @param finalStr a string represent if the variables are final or not.
     * @param typeStr a string represent if the variables' type.
     * @param argumentsStr a string represent the different variables and their assignments.
     * @param scope the current scope.
     * @throws VariableException if an error occurred while creating the variable,
     *                           or if duplicate variables with the same name is being created.
     */
    private static void handleVariableDeclaration
    (String finalStr, String typeStr, String argumentsStr, Scope scope)
            throws VariableException {

        // The variables are declared final iff finalStr is not null.
        boolean isFinal = finalStr != null;

        // Split each variable declaration.
        String[] splitVarsDeclarations = argumentsStr.split(
                RegexStrings.SEPERATE_ARGUMENTS);

        // Now split each variable declaration to name and assignment (if such exists).
        String[][] arguments = new String[splitVarsDeclarations.length][];
        for (int i = 0; i < arguments.length; i++)
            arguments[i] = splitVarsDeclarations[i].split(
                    RegexStrings.POSSIBLE_WHITE_SPACES + ReservedWords.ASSIGNMENT
                            + RegexStrings.POSSIBLE_WHITE_SPACES);

        // Run through the variables' names and check for duplications with the scope's variables.
        String variableName;
        for (String[] variableDeclaration: arguments){
            variableName = variableDeclaration[0];

            if (scope.containVariable(variableName)){
                throw new VariableException(DUPLICATE_VARIABLES_MESSAGE);
            }
        }

        // Create a new HashMap of variables that will contain all variables that can bee accessed
        // from the current scope (i.e. scopeVariables and upperScopeVariables).
        HashMap<String, Variable> sourceVariablesForCreation = new HashMap<>();
        sourceVariablesForCreation.putAll(scope.getScopeVariables());
        sourceVariablesForCreation.putAll(scope.getUpperScopeVariables());

        // Create the variables using the corresponding factory. Might produce VariableException.
        HashMap<String, Variable> createdVariables = VariableFactory.createVariables(
                isFinal, typeStr, arguments, sourceVariablesForCreation);

        // Add the created variables to scopeVariables.
        scope.addVariables(createdVariables);
    }
    
    /**
     * This method handle a single variable assignment command.
     * It change the value of the corresponding variable's name given.
     *
     * @param variableName the name of the variable.
     * @param newValue the new value to change.
     * @param scope the current scope.
     * @throws VariableException if the argument given is invalid or a final variable is tried to change,
     *                           or if the variable does not exist.
     */
    private static void handleVariableAssignment(String variableName, String newValue, Scope scope)
            throws VariableException, InvalidSyntaxException {
    	
        Variable variable = scope.getVariable(variableName);

        // Check if the variable is found in scopeVariables.
        if (variable == null){
            // The variable does not exist, neither in scope variables nor in upper scopes' variables.
            throw new VariableException(VARIABLE_NOT_FOUND_MESSAGE);
        }

        // The actions below might throw VariableException if the variable is final and being changed,
        // or if the argument given is invalid.
        
        Variable variableToAssign = scope.getVariable(newValue);
        
        // Check if the variable was assign with a different variable in scopeVariables.
        if (variableToAssign != null){
            variable.changeValue(variableToAssign);
        }
        else{ // The variable is assigned with an actual argument.
            variable.changeValue(newValue);
        }
    }

    /**
     * This function handle method declaration.
     * It adds a new MethodSignature to the parser HashMap,
     * and advance the reader until the end of the method.
     *
     * @param methodName the method's name.
     * @param methodArguments a string represent the arguments given to the method (could be empty).
     * @throws IOException
     * @throws InvalidSyntaxException if during the algorithm couldn't find the end of the method.
     *                                or if there is already a method with the same name.
     */
    private static void handleMethodDeclaration(String methodName, String methodArguments)
            throws IOException, InvalidSyntaxException {

        // Check if there is already a method with the same name.
        if (methodsSignatures.containsKey(methodName)){
            throw new InvalidSyntaxException(DUPLICATE_METHODS_MESSAGE);
        }

        // splitArgs is the array of the split arguments, and types will hold only the types' strings.
        String[] types, splitArgs;

        // Check if the method received parameters.
        if (methodArguments == null || 
        	RegexPatterns.POSSIBLE_WHITE_SPACES.matcher(methodArguments).matches()){

            types = new String[0];  // Set as empty String list, because the methods receive no arguments.
        }
        else{   // The method has arguments given to it.
            splitArgs = methodArguments.split(RegexStrings.POSSIBLE_WHITE_SPACES + ReservedWords.COMMA +
                                              RegexStrings.POSSIBLE_WHITE_SPACES);
            types = new String[splitArgs.length];

            for (int i = 0; i < types.length; i++){
            	splitArgs[i] = splitArgs[i].trim();
                types[i] = splitArgs[i].split(RegexStrings.WHITE_SPACES)[0];
            }
        }

        // Create the new method signature and ass it to the HashMap.
        MethodSignature methodSignature = MethodSignatureFactory.createMethodSignature(types);
        methodsSignatures.put(methodName, methodSignature);

        // Finished creating new MethodSignature object.
        // Now advance the reader until the last line of the method (which is a closing bracket).
        // This method will also validate the brackets of the method and its inner scopes.
        goToEndOfMethod();
    }

    /**
     * This method is used to advance the reader until the end of a method,
     * and validate the brackets - no missing / too many brackets.
     *
     * @throws InvalidSyntaxException if there is a missing bracket / too many brackets.
     * @throws IOException
     */
    private static void goToEndOfMethod() throws IOException, InvalidSyntaxException {
        int bracketsCounter = 0;

        line = bufferedReader.readLine();

        // Advance the reader until the matching closing bracket is found.
        while (line != null){
            line = line.trim();

            if (line.endsWith(ReservedWords.BEGIN_SCOPE))
                bracketsCounter++;

            else if (line.endsWith(ReservedWords.END_SCOPE)){
                if (bracketsCounter > 0)
                    bracketsCounter--;
                else    // A matching closing bracket was found. exit the function.
                    return;
            }

            line = bufferedReader.readLine();
        }

        // If this code is reached, it means the line is null, and we've finished reading the file with
        // no matching closing bracket for the method declaration.
        throw new InvalidSyntaxException(BRACKETS_ERROR_MESSAGE);
    }

    /**
     * Check if the line is a comment.
     * Assume the input string is not null.
     *
     * @param line the line to check.
     * @return true if the line is a comment, false otherwise.
     */
    private static boolean isComment(String line){
        return line.startsWith(ReservedWords.COMMENT);
    }

    /**
     * Check if the line is blank.
     * Assume the input string is not null.
     *
     * @param line the line to check.
     * @return true if the line is blank, false otherwise.
     */
    private static boolean isBlank(String line){
        return RegexPatterns.POSSIBLE_WHITE_SPACES.matcher(line).matches();
    }

    /**
     * Used to initiate a new Scope object located inside the given scope.
     * @param scope The scope where the new scope is located.
     * @return The new scope.
     */
    private static Scope createNewScope (Scope scope) {

        // Take the Hash Maps of given scope variables and its upper scope variables and initiate a new Hash
        // Map containing all the variables. It will be used as the upper scopes variables of the new scope.
        HashMap<String, Variable> scopeVariables = scope.getScopeVariables();
        HashMap<String, Variable> upperScopeVariables = scope.getUpperScopeVariables();
        HashMap<String, Variable> newScopeVariables = new HashMap<>();
        newScopeVariables.putAll(scopeVariables);
        newScopeVariables.putAll(upperScopeVariables);

        // Return the new scope.
        return new Scope(newScopeVariables, false);
    }

    /**
     * Used to check if a condition (belongs to "if" or "while" statement) is legal, means every
     * sub-condition is true/false or integer/double number or boolean/int/double variable.
     * @param condition The whole condition may compose some sub-conditions.
     * @param scope The scope the condition is located
     * @throws VariableException if the condition is illegal.
     */
    private static void checkCondition(String condition, Scope scope) throws VariableException {
        // Split the condition to sub-conditions every time a "||" or "&&" appear.
        String[] subConditions = condition.split(RegexStrings.SEPERATE_CONDITIONS);
        
        Matcher number;
        
        for(String subCondition : subConditions) {
            // For each sub-condition, if it is not a number (int or double), check if it "true/false"
        	// (boolean) and if its not, try to find a variable whose name is the sub-condition.
        	// if no variable was found, of if its type is not boolean, int or double, throw an exception.
        	number = RegexPatterns.DOUBLE.matcher(subCondition);
        	if(!number.matches()) {
        		if(!(subCondition.equals(ReservedWords.TRUE) || subCondition.equals(ReservedWords.FALSE))) {
                	Variable variable = scope.getVariable(subCondition);
                	if(variable == null || !variable.isInitialized()) {
                        throw new VariableException(VARIABLE_NOT_FOUND_MESSAGE);
                    }
                	Type varType = variable.getType();
                	if(varType != Type.BOOLEAN && varType != Type.DOUBLE && varType != Type.INTEGER) {
                		throw new VariableException(INVALID_VARIABLE_TYPE);
                	}
                }
        	}
        }
    }

    /**
     * Used to check if a method call is legal, means all the arguments are valid.
     * @param methodName The method name
     * @param arguments The arguments
     * @param scope The scope where the method call is located
     * @throws MethodException
     */
    private static void checkMethodCall(String methodName, String arguments, Scope scope) 
    		throws MethodException {
    	
    	// Try to find the matching method signature. Also cut spaces from begin and end of the argumetns
    	// strings.
    	MethodSignature methodSignature = methodsSignatures.get(methodName);
    	arguments = arguments.trim();
    	String[] subArguments;
    	
    	if(methodSignature == null) {
    		// If there is no method with the given method name, throw an exception.
    		throw new MethodException(MethodException.NO_METHOD_EXIST);
    	}
    	
    	if (RegexPatterns.POSSIBLE_WHITE_SPACES.matcher(arguments).matches()) {
    		// If there are no arguments, initiate an empty array of sub-arguments.
    		subArguments = new String[0];
    	}
    	else {
    		// Split the arguments to array of sub-arguments by ","
        	subArguments = arguments.split(RegexStrings.SEPERATE_ARGUMENTS);
    	}
    	
    	if(!methodSignature.accept(subArguments, scope)) {
    		// If the arguments don't match the method signature, throw an exception.
    		throw new MethodException(MethodException.INVALID_ARGUMANTS);
    	}	
    }

    /**
     * Used to check if a scope ends correctly, means the last command before "}" is 'return' statement.
     *
     * @param prevLine The previous command line
     * @throws InvalidSyntaxException if the method didn't end with return statement.
     */
    private static void checkEndMethod(String prevLine) throws InvalidSyntaxException {
        if(! RegexPatterns.RETURN_STATEMENT.matcher(prevLine).matches()) {
            throw new InvalidSyntaxException(MISSING_RETURN_MESSAGE);
        }
    }
}