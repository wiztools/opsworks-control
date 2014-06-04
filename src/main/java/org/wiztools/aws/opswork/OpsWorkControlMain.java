package org.wiztools.aws.opswork;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.opsworks.AWSOpsWorksClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 *
 * @author subhash
 */
public class OpsWorkControlMain {

    private static final String CMD_START_STACK = "start-stack";
    private static final String CMD_STOP_STACK = "stop-stack";
    private static final String CMD_START_INSTANCE = "start-instance";
    private static final String CMD_STOP_INSTANCE = "stop-instance";
    
    private static final int EXIT_CLI_ERROR = 1;
    private static final int EXIT_IO_ERROR = 2;
    private static final int EXIT_EC2_ERROR = 3;
    private static final int EXIT_SYS_ERROR = 4;
    
    private static void printCommandLineHelp(PrintStream out){
        out.println("Usage: opswork-control [options] <parameters>");
        out.println("Where options are:");
        
        String opts =
                "  -a  AWS access key (not needed when -k option is used).\n" +
                "  -s  AWS secret key (not needed when -k option is used).\n" +
                "  -k  Java properties file with AWS credentials.\n" +
                "  -c  Command. One of: start-stack, stop-stack, start-instance, stop-instance.\n" +
                "  -h  Prints this help.\n";
        out.println(opts);
        
        out.println("The parameters are either stack-ids or instance-ids (based on command).");
        
        String moreHelp = "Format of `aws-creds-file': \n"
                + "\tAWSAccessKeyId=XXX\n"
                + "\tAWSSecretKey=XXX";
        out.println(moreHelp);
    }
    
    public static void main(String[] args) {
        try {
            OptionParser parser = new OptionParser( "a:s:k:c:h" );
            OptionSet options = parser.parse(args);
            
            if(options.has("h")) {
                printCommandLineHelp(System.out);
                System.exit(0);
            }
            
            String awsCredsFile = (String) options.valueOf("k");
            String accessKey = (String) options.valueOf("a");
            String secretKey = (String) options.valueOf("s");
            String command = (String) options.valueOf("c");
            
            if(awsCredsFile == null && (accessKey == null || secretKey == null)) {
                System.err.println("Either -k or (-a and -s) options are mandatory.");
                printCommandLineHelp(System.err);
                System.exit(EXIT_CLI_ERROR);
            }
            
            if(awsCredsFile != null && (accessKey != null && secretKey != null)) {
                System.err.println("Options -k and (-a and -s) cannot coexist.");
                printCommandLineHelp(System.err);
                System.exit(EXIT_CLI_ERROR);
            }
            
            if(command == null || !(command.equals(CMD_START_INSTANCE) || command.equals(CMD_STOP_INSTANCE))) {
                System.err.println("Invalid command. You supplied: " + command);
                printCommandLineHelp(System.err);
                System.exit(EXIT_CLI_ERROR);
            }
            
            if(awsCredsFile != null) {
                Properties p = new Properties();
                try {
                    p.load(new FileInputStream(new File(awsCredsFile)));
                    
                    accessKey = p.getProperty("AWSAccessKeyId");
                    secretKey = p.getProperty("AWSSecretKey");
                }
                catch(IOException ex) {
                    System.err.println("Cannot read AWS property file.");
                    ex.printStackTrace(System.err);
                    System.exit(EXIT_IO_ERROR);
                }
            }
            
            List<String> instances = new ArrayList<>();
            for(Object o: options.nonOptionArguments()) {
                instances.add((String) o);
            }
            
            AWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
            AWSOpsWorksClient client = new AWSOpsWorksClient(awsCreds);
            
            OpsWorkControl control = new OpsWorkControl(client, instances);
            
            switch (command) {
                case CMD_START_STACK:
                    control.startStacks();
                    break;
                case CMD_STOP_STACK:
                    control.stopStacks();
                    break;
                case CMD_START_INSTANCE:
                    control.startInstances();
                    break;
                case CMD_STOP_INSTANCE:
                    control.stopInstances();
                    break;
            }
        }
        catch(OptionException ex) {
            System.err.println(ex.getMessage());
            printCommandLineHelp(System.err);
            System.exit(EXIT_CLI_ERROR);
        }
    }
}
