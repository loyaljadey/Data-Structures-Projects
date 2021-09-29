package gitlet;

// TODO: any imports you need here

/** Driver class for Gitlet, a subset of the Git version-control system.
 *
 * handles all the operations that gitlet needs to have
 * checks to make sure that there are enough arguments
 *
 *  @author Michael Jia
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        // If a user doesn't input any arguments, print the message
        // Please enter a command. and exit.
        if (args.length <= 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        } else {
            //checks if the command matches any of the predetermined cases
            String firstArg = args[0];
            switch (firstArg) {
                case "init":
                    validateNumFormatArgs("init", args, 1);
                    // runs the setupInit in the repository class
                    Repository.setupInit();
                    break;
                case "add":
                    validateNumFormatArgs("add", args, 2);
                    //runs the add method in the repository class
                    Repository.add(args[1]);
                    break;
                case "commit":
                    // Every commit must have a non-blank message.
                    // If it doesn’t, print the error message
                    // "Please enter a commit message." and abort
                    validateNumFormatArgs("commit", args, 2);
                    //runs the commit method in the repository class
                    Repository.commit(args[1]);
                    break;
                case "rm":
                    validateNumFormatArgs("rm", args, 2);
                    //runs the rm method in the repository class
                    Repository.rm(args[1]);
                    break;
                case "log":
                    validateNumFormatArgs("log", args, 1);
                    //runs the log method in the repository class
                    Repository.log();
                    break;
                case "global-log":
                    validateNumFormatArgs("global-log", args, 1);
                    //runs the global-log method in the repository class
                    Repository.globalLog();
                    break;
                case "find":
                    validateNumFormatArgs("find", args, 2);
                    //runs the find method in the repository class
                    Repository.find(args[1]);
                    break;
                case "status":
                    // TODO: handle the `status` command ec
                    validateNumFormatArgs("status", args, 1);
                    // runs the status method in the repository class
                    Repository.status();
                    break;
                case "checkout":
                    if (args.length == 3) {
                        validateNumFormatArgs("checkout", args, 3);
                        Repository.checkout(args[2]);
                        break;
                    }
                    if (args.length == 4) {
                        validateNumFormatArgs("checkout", args, 4);
                        Repository.checkout(args[1], args[3]);
                        break;
                    }
                    if (args.length == 2) {
                        validateNumFormatArgs("checkout", args, 2);
                        Repository.checkoutbranch(args[1]);
                        break;
                    }
                    break;
                case "branch":
                    validateNumFormatArgs("branch", args, 2);
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    validateNumFormatArgs("rm-branch", args, 2);
                    Repository.rmBranch(args[1]);
                    break;
                case "reset":
                    validateNumFormatArgs("reset", args, 2);
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    // TODO: handle the `merge [branch name]` command
                    validateNumFormatArgs("merge", args, 2);
                    Repository.merge(args[1]);
                    break;
                //If a user inputs a command that doesn’t exist,
                // print the message "No command with that name exists." and exit.
                default:
                    Utils.message("No command with that name exists.");
                    System.exit(0);
            }
        }
    }


    /**
     * Checks for the rest of the commands to see if the repository has been initialized
     * runs the exists() helper method of repository
     *
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * If a user inputs a command with the wrong number or format of operands,
     * print the message "Incorrect operands." and exit.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumFormatArgs(String cmd, String[] args, int n) {
        if (!cmd.equals("init") && !Repository.exists()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (cmd.equals("commit") && args[1].equals("")) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }
        if (cmd.equals("checkout")) {
            if (n == 3 && !args[1].equals("--")) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
            if (n == 4 && !args[2].equals("--")) {
                Utils.message("Incorrect operands.");
                System.exit(0);
            }
        }

        if (args.length != n) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
    }

}
