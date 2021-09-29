package gitlet;

/** IMPORTS */
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  has the commit blocks with the instance variables
 *  the commit blocks simply keep track of the files that are changed
 *
 *  list of files (blobs) with their sha1 hash names
 *
 *
 *  commit class is serializable because a commit object can be written as a byte[] object
 *
 *  @author Michael Jia
 *  @source TA in office hours helped me realize i need a hashmap to keep track of blobs in here
 *  @source documentation for the date
 *          https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
 *  @source many of the reference methods
 *          https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/util
 */
public class Commit implements Serializable {

    /** INSTANCE VARIABLES */

    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    // something that keeps track of what files that this commit is tracking
    // use sha1hash files of blobs
    // create a hashmap to keep track of what blob(sha1ids) i want to keep track
    // add to the hashmap the sha1Hash of the blobs(sha1ids) in the staging area
    // a commit will never store 2 of the same filenames
    // no collisions will happen in the hashmap
    // key is the filename
    // values is the sha1Hash of (filename + contents)
    // prevents issues with if there are different files with the same contents
    private HashMap<String, String> filesToTrack;
    //the message of the commit
    private String message;
    //the timeStamp of when the commit was made
    private String timeStamp;
    //the parent of this commit in terms of the sha1Hash of the previous commit
    private String parent;
    //the second parent for merges (branch) in terms of the sha1Hash of the previous commit
    private String parent2;


    /** CONSTRUCTORS */

    /** initial commit constructor
     * sets up the variables of the message and the parent pointer
     */
    public Commit() {
        this.message = "initial commit";
        this.parent = "end";
        this.parent2 = "end";
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        timeStamp = simpleDateFormat.format(new Date(0));
        filesToTrack = new HashMap<String, String>();
    }

    /**commit constructor
     * sets up the variables of the message and the parent pointer
     *
     * @param message the message of the commit
     * @param parent the parent of the commit
     * @param parent2 the second parent of the commit
     * @param filesToTrack the hashmap of files in the staging area
     *                     that the commit wants to keep track of
     */
    public Commit(String message, String parent, String parent2,
                  HashMap<String, String> filesToTrack) {
        this.message = message;
        this.parent = parent;
        this.parent2 = parent2;
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        timeStamp = simpleDateFormat.format(new Date());
        this.filesToTrack = filesToTrack;
    }

    /** HELPER METHODS */

    //returns the message
    public String getMessage() {
        return this.message;
    }
    //returns the timeStamp
    public String getTimeStamp() {
        return this.timeStamp;
    }
    //returns the parent
    public String getParent() {
        return this.parent;
    }
    //returns parent2
    public String getParent2() {
        return this.parent2;
    }
    //returns parents as a list
    public List<String> getParents() {
        List<String> parents = new ArrayList<>();
        parents.add(parent);
        parents.add(parent2);
        return parents;
    }
    //returns the HashMap
    public HashMap<String, String> getFilesTracked() {
        return filesToTrack;
    }
}
