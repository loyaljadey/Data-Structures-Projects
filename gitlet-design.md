# Gitlet Design Document

**Name**: Michael Jia

## Persistence
* .gitlet
    * commits
        * stores files of the commits
        * name of the files are the sha1Hash of the commits
    * stagingArea
        * write a new file and 
          then copy the contents of the file in the working 
          directory into this new file
        * keeps track of the files that add adds???
        * gets cleared after a commit
        * separate files for each add 
    * storeObjects
        * stores blob(files) by sha1Hash
    
    1. headFile that stores the sha1Hash of the commit it want to track
 


## Classes and Data Structures

### *Main*
#### Methods
* public static void validateNumFormatArgs(String cmd, String[] args, int n)
    * Checks the number of arguments versus the expected number,
    * throws a RuntimeException if they do not match.
    * @param cmd Name of command you are validating
    * @param args Argument array from command line
    * @param n Number of expected arguments

### *Repository*
#### Instance Variables
* public static final File CWD = new File(System.getProperty("user.dir"));
    * the current working directory
* public static final File GITLET_DIR = join(CWD, ".gitlet");
    * the .gitlet directory
* public static final File COMMIT_FOLDER = join(GITLET_DIR, "commits");
    * the commits directory
* private Pointer head;
    * the head that points to the most recent commit


#### Methods
* public static void setupInit()
    * setup the persistence here
    * if there is a gitlet version-control system in the current directory
    * "A Gitlet version-control system already exists in the current directory."
    * else make a new gitlet version-control
    * make the initial commit
    * create the .gitlet directory
    * create the rest of the objects in the directory
    * make an initial commit object
    * a commit with no files
    * commit message is "initial commit"
    * single branch of (master)
    * timestamp 00:00:00 UTC, Thursday, 1 January 1970
    * store the commits in the commits directory
    * write the commit object to the initCommitFile that has the title of the sha1Hash of the commit


### *Commit*
#### Instance Variables
* private String message;
    * the message of the commit
* private Date timeStamp;
    * the timeStamp of when the commit was made
* private Commit parent;
    * the parent of this commit
* private Commit parent2;
    * the second parent for merges


#### Methods
* public Commit()
    * initial commit constructor
    * sets up the variables of the message and the parent pointer

* public Commit(String message, Commit parent, Commit parent2)
  *commit constructor
    * sets up the variables of the message and the parent pointer
    * @param message the message of the commit
    * @param parent the parent of the commit
    * @param parent2 the second parent of the commit

* public String makeHash()
    * makes a sha1 hash for commit block

* public String getMessage()
    * returns the message
* public Date getTimeStamp()
    * returns the timeStamp
* public Commit getParent()
    * returns parent
* public Commit getParent2()
    * returns parent2


## Algorithms


    
