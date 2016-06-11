package oop.ex6.main;

import oop.ex6.method_signatures.MethodException;
import oop.ex6.parsing.InvalidSyntaxException;
import oop.ex6.parsing.Parser;
import oop.ex6.variables.VariableException;

import java.io.File;
import java.io.IOException;

/**
 * The manager class of the program
 */
public class Sjavac {
    private static final int NUMBER_OF_ARGUMENTS = 1;  // expected number of arguments given to the manager.
    private static final String SJAVA_EXTENSION = ".sjava";     // sJava file's extension.

    // These are used to output the corresponding results of the verifier.
    private static final String LEGAL = "0";
    private static final String ILLEGAL = "1";
    private static final String IO_ERRORS = "2";

    // Messages for the different errors that might occur.
    private static final String SJAVA_ERROR_MESSAGE = "this program run only on sJava files";
    private static final String ILLEGAL_ARGUMENTS_MESSAGE = "illegal given arguments";

    /**
     * This is the main method which is used to run the program
     * @param args the arguments given. Should be a single string represents sJava filename.
     */
    public static void main(String[] args){
        try {
            testSJavaFile(args);
            System.out.println(LEGAL);
        } catch (SJavaException e) {
        	System.out.println(ILLEGAL);
        	System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(IO_ERRORS);
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method verify the sJava file, by calling the parser
     * and verify the arguments given to the manager.
     *
     * @param args the arguments given. Should be a single string represents sJava filename.
     * @throws InvalidArgumentsException If an illegal argument is given.
     * @throws IOException
     * @throws VariableException If variable exception occurred when trying to verify the file.
     * @throws InvalidSyntaxException If invalid syntax occurred when trying to verify the file.
     * @throws MethodException If method exception occurred when trying to verify the file.
     */
    private static void testSJavaFile(String[] args) throws IOException,
            VariableException, InvalidSyntaxException, MethodException {

        final int FILE_INDEX_IN_ARGS = 0;

        // Check if the number of given arguments equal the desired number of arguments.
        if (args.length != NUMBER_OF_ARGUMENTS){
            throw new InvalidArgumentsException(ILLEGAL_ARGUMENTS_MESSAGE);
        }

        String fileName = args[FILE_INDEX_IN_ARGS];

        // Check if the extension of the file is sJava extension.
        if (!fileName.endsWith(SJAVA_EXTENSION)){
            throw new InvalidArgumentsException(SJAVA_ERROR_MESSAGE);
        }

        File sJavaFile = new File(fileName);

        // Check if the file exists.
        if (!sJavaFile.exists()){
            throw new IOException();
        }

        // Calls the parser to parse the sJava file.
        Parser.parseFile(sJavaFile);
    }
}
