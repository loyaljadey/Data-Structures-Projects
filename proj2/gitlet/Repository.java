package gitlet;

/**
 * @author Michael Jia
 * @source TA in office hours helped me realize i need a hashmap to keep track of blobs in here
 * @source documentation for the date
 *         https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
 * @source many of the reference methods
 *         https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/util
 * */


/** IMPORTS */

import static gitlet.Utils.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/** Represents a gitlet repository.
 *
 *  sets up the persistence (file directory) (where everything is stored basically)
 *  contains all the methods that the repository needs
 *
 *
 *  @author Michael Jia
 */
public class Repository {
    /** INSTANCE VARIABLES */
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    //the current working directory
    public static final File CWD = new File(System.getProperty("user.dir"));
    //the .gitlet directory
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    //the commits directory
    public static final File COMMIT_FOLDER = join(GITLET_DIR, "commits");
    //the staging area directory
    public static final File STAGING_AREA_FOLDER = join(GITLET_DIR, "stagingArea");
    //the staging area for removal directory
    public static final File STAGING_AREA_REMOVE_FOLDER = join(GITLET_DIR, "stagingAreaRemove");
    //the objects directory
    public static final File STORE_OBJECTS_FOLDER = join(GITLET_DIR, "storeObjects");
    //the head file in the .gitlet directory
    //is a files keep track of the commit sha1Hash
    public static final File HEAD = join(GITLET_DIR, "Head");
    //the file that keeps track of the branchMap
    public static final File BRANCH_MAP = join(GITLET_DIR, "BranchMap");
    //the file that keeps track of the name of the currentBranch
    public static final File CURRENT_BRANCH = join(GITLET_DIR, "CurrentBranch");



    /** HELPER METHODS */

    /**
     * Helper method that grabs the file within a directory and returns that file
     *
     * @param directory the directory in which to look for the file
     * @param filename the string name of the file
     */
    private static File grabFile(File directory, String filename) {
        return Utils.join(directory, filename);
    }

    /**
     * Helper method that makes a sha1 hash for commit block
     *
     * @param object a serializable object (an object that can be turned into a byte[])
     *               that you want the sha1Hash of
     */
    public static String makeHash(Serializable object) {
        return sha1(serialize(object));
    }

    /**
     * Helper method that makes a sha1 hash for commit block
     *
     * @param object a serializable object (an object that can be turned into a byte[])
     *               that you want the sha1Hash of
     * @param contents a serializable content that you want the hash to take into account
     */
    public static String makeHash(Serializable object, Serializable contents) {
        return sha1(serialize(object), serialize(contents));
    }

    /**
     * Helper method to grab the current commit given a file
     *
     * open either the head or master file,
     * read the contents as a string of the commit hash,
     * set to a string
     * go to the commit folder and find the commit with that hash, set it to a file object
     * grab the commit inside that file object
     * return the Commit
     *
     * @param headOrMaster pass in the file of either head or master to grab the commit at either
     * */
    public static Commit grabCommit(File headOrMaster) {
        String hashOfCommit = readContentsAsString(headOrMaster);
        File commitFile = grabFile(COMMIT_FOLDER, hashOfCommit);
        return readObject(commitFile, Commit.class);
    }

    /**
     * Helper method to grab the current commit given sha1hash
     *
     * if the hashOfCommit is null, then just return null since there is no commit with a null hash
     * go to the commit folder and find the commit with that hash, set it to a file object
     * grab the commit inside that file object
     * return the Commit
     *
     * @param hashOfCommit pass in the hash of the commit to grab the commit
     * */
    public static Commit grabCommit(String hashOfCommit) {
        if (hashOfCommit == null) {
            return null;
        } else {
            File commitFile = grabFile(COMMIT_FOLDER, hashOfCommit);
            if (!commitFile.exists()) {
                return null;
            }
            return readObject(commitFile, Commit.class);
        }
    }

    /**
     * Helper method to check if the repository has been initialized
     *
     * grabs the list of objects in the .gitlet directory and checks if the list is null or not
     * if null, then it means that the directory persistence has not been set up
     * */
    public static boolean exists() {
        if (GITLET_DIR.list() != null) {
            return true;
        }
        return false;
    }

    /**
     * Helper method that grabs all the files in the CWD
     * and puts them in a list of lexicographic order
     * */
    public static List<String> grabCWDFiles() {
        //the list of actual files in the CWD that we want to look at
        List<String> actualFilesInCWD = new ArrayList<String>();
        //grabs the files in the CWD
        List<String> listOfFilenamesInCWD = plainFilenamesIn(CWD);
        //iterator to grab the files that we want
        for (String filename : listOfFilenamesInCWD) {
            //skips over the files in the CWD that are ignored
            if (filename.equals("Makefile") || filename.equals("gitlet-design.md")
                    || filename.equals("pom.xml") || filename.equals("proj2.iml")) {
                continue;
            }
            actualFilesInCWD.add(filename);
        }
        return actualFilesInCWD;
    }

    /**
     * Helper method to grab the branchHashMap given a file
     *
     * open either the branch file, by reading the object from the file
     * return the object
     * @param branchFile pass in the file that keeps track of the branchMap
     * */
    public static HashMap<String, String> grabBranchMap(File branchFile) {
        return readObject(branchFile, HashMap.class);
    }

    /**
     * Helper method to grab the current branch's name
     *
     * grab the current branch name, by reading the string from the file
     * return the string
     *
     * @param currentBranchNameFile pass in the file that keeps track of the currentBranchName
     * */
    public static String grabCurrentBranchName(File currentBranchNameFile) {
        return readContentsAsString(currentBranchNameFile);
    }

    /**
     * Helper method to find the split point given the commit object and the commit id
     *
     * a set of commitIds to keep track of which commits have been looked at
     *
     * commitQueue keeps track of the commitIds that we want to check
     * add the two commitIds that we want to check
     *
     * add the two commitIds to the set so that they are now "marked"
     *
     * dequeue as long as the commitQueue contains items in it
     *
     * dequeue the top 2 commitIds
     * iterate through those parents of the those commitIds
     * if the commit's parent is "end" then skip that check
     * if the commitId can be added to the Set that means it hasn't been marked yet
     * so then add the commitId of the parent to the queue
     * else the commitId can't be added to the Set then that means it has been checked
     * return the commit with that commitID
     *
     * the return is the commitId of the splitPoint
     *
     * if all else fails return null
     *
     * @param branchCommitId the commitId of one of the branches
     * @param secondBranchCommitId the commitId of the other branches
     * */
    public static String findSplitPoint(String branchCommitId,
                                        String secondBranchCommitId) {

        Set<String> marked = new HashSet<String>();
        Queue<String> commitQueue = new ArrayDeque<String>();

        commitQueue.add(branchCommitId);
        commitQueue.add(secondBranchCommitId);

        marked.add(branchCommitId);
        marked.add(secondBranchCommitId);

        while (!commitQueue.isEmpty()) {
            String currentCommit = commitQueue.remove();
            String otherCurrentCommit = commitQueue.remove();
            for (String parents : grabCommit(currentCommit).getParents()) {
                if (parents.equals("end")) {
                    continue;
                } else {
                    if (marked.add(parents)) {
                        commitQueue.add(parents);
                    } else {
                        return parents;
                    }
                }
            }
            for (String parents : grabCommit(otherCurrentCommit).getParents()) {
                if (parents.equals("end")) {
                    continue;
                } else {
                    if (marked.add(parents)) {
                        commitQueue.add(parents);
                    } else {
                        return parents;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Helper method to grab all the files together from the splitPoint, currentBranch, otherBranch
     *
     * grab the set of the filenames in the splitPoint commit
     * then the currentBranch commit
     * then the otherBranch commit
     * add those keysets to the new created set
     * then return the set
     *
     * @param splitPoint the commitID of the splitPoint commit
     * @param currentBranch the currentBranch name
     * @param otherBranch the otherBranch name
     * */
    public static Set<String> grabAllFilesInCommits(String splitPoint,
                                                    String currentBranch,
                                                    String otherBranch) {
        //grab the branchMap
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);

        //creates a new set
        Set<String> allFiles = new HashSet<>();
        //adds the list of filenames in the splitPoint commit
        allFiles.addAll(grabCommit(splitPoint).getFilesTracked().keySet());
        //adds the list of filenames in the commit that is mapped to by the currentBranch name
        allFiles.addAll(grabCommit(branchMap.get(currentBranch)).getFilesTracked().keySet());
        //adds the list of filenames in the commit that is mapped to by the otherBranch name
        allFiles.addAll(grabCommit(branchMap.get(otherBranch)).getFilesTracked().keySet());

        //return the set of files
        return allFiles;
    }

    /**
     * Helper method for the merge to check the if cases 1-8 in the merge
     *
     * 4 5 --> 6 7 --> 3 8 ---> 1 2
     *     check if in the splitpoint first
     *     then check if absent in either
     *     then check if modified in both
     *empty spaces are where comments were originally located
     *
     * 5
     * 8c the file was absent at the split point and has
     * different contents in the given and current branches"
     *
     * 6
     * 8b case in here
     * "or the contents of one are changed and the other file is deleted"
     *
     * 8a both contents are changed but different from each other
     *
     * 1
     *
     * @param allFiles the set of all the files in the split, current, given branch commits
     * @param splitHashMap hashmap of split commit
     * @param currentHashMap hashmap of current commit
     * @param branchHashMap hashmap of branch commit
     * @param splitCommitHash hash of split commit
     * @param currentCommitHash hash of current commit
     * @param branchCommitHash hash of branch commit
     * */
    public static void checkIFs(Set<String> allFiles,
                                HashMap<String, String> splitHashMap,
                                HashMap<String, String> currentHashMap,
                                HashMap<String, String> branchHashMap,
                                String splitCommitHash,
                                String currentCommitHash,
                                String branchCommitHash) {
        for (String filename : allFiles) {
            String currentFileContents = "";
            String branchFileContents = "";
            if (!splitHashMap.containsKey(filename)) {
                if (!currentHashMap.containsKey(filename)
                        && branchHashMap.containsKey(filename)) {
                    checkout(branchCommitHash, filename);
                    add(filename);
                } else if ((currentHashMap.containsKey(filename)
                        && branchHashMap.containsKey(filename))
                        && (!currentHashMap.get(filename).equals(branchHashMap.get(filename)))) {
                    currentFileContents =  readContentsAsString(
                            grabFile(STORE_OBJECTS_FOLDER, currentHashMap.get(filename)));
                    branchFileContents =  readContentsAsString(
                            grabFile(STORE_OBJECTS_FOLDER, branchHashMap.get(filename)));
                    checkout(currentCommitHash, filename);
                    File fileToEdit = grabFile(CWD, filename);
                    writeContents(fileToEdit, "<<<<<<< HEAD\n"
                            + currentFileContents + "=======\n" + branchFileContents + ">>>>>>>\n");
                    add(filename);
                    Utils.message("Encountered a merge conflict.");
                }
            } else {
                if (!currentHashMap.containsKey(filename)
                        || !branchHashMap.containsKey(filename)) {
                    if (currentHashMap.containsKey(filename)
                            && splitHashMap.get(filename).equals(currentHashMap.get(filename))
                            && !branchHashMap.containsKey(filename)) {
                        rm(filename);
                    } else if ((currentHashMap.containsKey(filename)
                            && !splitHashMap.get(filename).equals(currentHashMap.get(filename)))
                            || (branchHashMap.containsKey(filename)
                            && !splitHashMap.get(filename).equals(branchHashMap.get(filename)))) {
                        if (currentHashMap.containsKey(filename)) {
                            checkout(currentCommitHash, filename);
                            currentFileContents =  readContentsAsString(
                                    grabFile(STORE_OBJECTS_FOLDER, currentHashMap.get(filename)));
                        } else if (branchHashMap.containsKey(filename)) {
                            checkout(branchCommitHash, filename);
                            branchFileContents =  readContentsAsString(
                                    grabFile(STORE_OBJECTS_FOLDER, branchHashMap.get(filename)));
                        }
                        File fileToEdit = grabFile(CWD, filename);
                        writeContents(fileToEdit, "<<<<<<< HEAD\n"
                            + currentFileContents + "=======\n" + branchFileContents + ">>>>>>>\n");
                        add(filename);
                        Utils.message("Encountered a merge conflict.");
                    }
                } else if (currentHashMap.containsKey(filename)
                        && branchHashMap.containsKey(filename)) {
                    if (!splitHashMap.get(filename).equals(currentHashMap.get(filename))
                            && !splitHashMap.get(filename).equals(branchHashMap.get(filename))) {
                        if (!currentHashMap.get(filename).equals(branchHashMap.get(filename))) {
                            currentFileContents =  readContentsAsString(
                                    grabFile(STORE_OBJECTS_FOLDER, currentHashMap.get(filename)));
                            branchFileContents =  readContentsAsString(
                                    grabFile(STORE_OBJECTS_FOLDER, branchHashMap.get(filename)));
                            checkout(currentCommitHash, filename);
                            File fileToEdit = grabFile(CWD, filename);
                            writeContents(fileToEdit, "<<<<<<< HEAD\n"
                                    + currentFileContents + "=======\n"
                                    + branchFileContents + ">>>>>>>\n");
                            add(filename);
                            Utils.message("Encountered a merge conflict.");
                        }
                    } else {
                        if (!splitHashMap.get(filename).equals(branchHashMap.get(filename))) {
                            checkout(branchCommitHash, filename);
                            add(filename);
                        }
                    }
                }
            }
        }
    }


    /**
     * Helper method that does a similar thing to the commit(string message)
     *
     * @param message the message that gets passed into the commit to be saved
     * */
    public static void mergeCommit(String message,
                                   String currentCommitHash,
                                   String branchCommitHash) {

        Commit currentCommit = grabCommit(HEAD);
        HashMap<String, String> oldHashMap = currentCommit.getFilesTracked();
        HashMap<String, String> newHashMap = currentCommit.getFilesTracked();
        String[] listOfFilenames = STAGING_AREA_FOLDER.list();
        String[] listOfFilenamesToRemove = STAGING_AREA_REMOVE_FOLDER.list();

        if (listOfFilenames.length == 0 && listOfFilenamesToRemove.length == 0) {
            Utils.message("No changes added to the commit.");
            System.exit(0);
        } else {

            if (listOfFilenamesToRemove.length != 0) {
                for (int i = 0; i < listOfFilenamesToRemove.length; i++) {
                    File fileOnStage = grabFile(STAGING_AREA_REMOVE_FOLDER,
                            listOfFilenamesToRemove[i]);
                    if (oldHashMap.containsKey(listOfFilenamesToRemove[i])) {
                        newHashMap.remove(listOfFilenamesToRemove[i]);
                    }
                    //delete file in staging area to remove
                    fileOnStage.delete();
                }
            }

            if (listOfFilenames.length != 0) {
                for (int i = 0; i < listOfFilenames.length; i++) {
                    File fileOnStage = grabFile(STAGING_AREA_FOLDER, listOfFilenames[i]);
                    if (oldHashMap.containsKey(listOfFilenames[i])
                            && !oldHashMap.containsValue(
                            makeHash(listOfFilenames[i],
                                    readContentsAsString(fileOnStage)))) {
                        newHashMap.remove(listOfFilenames[i]);
                    }
                    newHashMap.put(listOfFilenames[i],
                            makeHash(listOfFilenames[i],
                                    readContentsAsString(fileOnStage)));
                    // adds the file in the storeObjects folder with file name as a
                    // sha1Hash taking into account the contents of the file
                    File addToObjects = join(STORE_OBJECTS_FOLDER,
                            makeHash(listOfFilenames[i],
                                    readContentsAsString(fileOnStage)));
                    writeContents(addToObjects, readContentsAsString(fileOnStage));
                    //delete file in staging area
                    fileOnStage.delete();
                }
            }
        }
        // make a new commit object
        // pass in the current head commit save as parent1
        // pass in the branch head commit save as parent2
        // pass in the hashmap
        // pass in its message according to the user input
        Commit newCommit = new Commit(message, currentCommitHash, branchCommitHash, newHashMap);

        //hash the new commit
        String hashOfNewCommit = makeHash(newCommit);

        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);

        //check what the current head is
        //if current head is a branch then change the value in for that branch in the branchMap
        if (branchMap.containsValue(readContentsAsString(HEAD))) {
            branchMap.put(currentBranch, hashOfNewCommit);
            writeObject(BRANCH_MAP, branchMap);
        }
        //make head point at this commit
        writeContents(HEAD, hashOfNewCommit);

        //add commit to the commits directory
        File commitToAdd = join(COMMIT_FOLDER, hashOfNewCommit);
        writeObject(commitToAdd, newCommit);

    }

    /**
     * Helper method to grab the files in the aforementioned areas
     *
     * @param cwdFiles
     * @param stagingAreaFiles
     * @param stageToRemoveFiles
     * @param currentCommitFiles
     * */
    public static List<String> combineFilesInAreas(List<String> cwdFiles,
                                                  List<String> stagingAreaFiles,
                                                  List<String> stageToRemoveFiles,
                                                  Set<String> currentCommitFiles) {

        //creates a new set
        Set<String> allFiles = new HashSet<>();
        //adds the list of filenames in the current working directory
        allFiles.addAll(cwdFiles);
        //adds the list of filenames in the staging area
        allFiles.addAll(stagingAreaFiles);
        //adds the list of filenames in the staging to remove area
        allFiles.addAll(stageToRemoveFiles);
        //adds the list of filenames in the current commit
        allFiles.addAll(currentCommitFiles);

        //return the list of files
        List<String> outputList = new ArrayList<>();
        outputList.addAll(allFiles);

        return outputList;
    }

    /**
     * Helper method for status ec
     *
     * */
    public static void modButNotStage(List<String> actualFilesInCWD,
                                      List<String> stagedFiles,
                                      List<String> listOfFilenamesToRemove,
                                      Set<String> currentCommitFilenames,
                                      HashMap<String, String> currentHashMap) {
        List<String> fileList = combineFilesInAreas(actualFilesInCWD,
                stagedFiles,
                listOfFilenamesToRemove,
                currentCommitFilenames);
        Collections.sort(fileList);
        //go through all the files
        for (String filename : fileList) {
            //deleted cases
            //check if the file is staged for add
            //check if the file is tracked in the current commit

            //add cases
            if (stagedFiles.contains(filename)
                    && !actualFilesInCWD.contains(filename)) {
                //remove the filename in the actualfilesincwd for untracked
                actualFilesInCWD.remove(filename);
                System.out.println(filename + " (deleted)");
            } else if (currentCommitFilenames.contains(filename)
                    && !listOfFilenamesToRemove.contains(filename)
                    && !actualFilesInCWD.contains(filename)) {
                //remove the filename in the actualfilesincwd for untracked
                actualFilesInCWD.remove(filename);
                System.out.println(filename + " (deleted)");
            } else if (stagedFiles.contains(filename) && actualFilesInCWD.contains(filename)) {
                //make the sha1Hash of the contents of the file in the CWD
                String hashOfFile = makeHash(filename,
                        readContentsAsString(grabFile(CWD, filename)));
                String stagingAreaHash = makeHash(filename,
                        readContentsAsString(grabFile(STAGING_AREA_FOLDER, filename)));
                //compare to the same filename in the current commit
                //see if the hashes are the same
                //if they are not the same, that means the cwd has been modified
                if (!stagingAreaHash.equals(hashOfFile)) {
                    //remove the filename in the actualfilesincwd for untracked
                    actualFilesInCWD.remove(filename);
                    System.out.println(filename + " (modified)");
                }
            } else if (currentCommitFilenames.contains(filename)
                    && !stagedFiles.contains(filename)
                    && !listOfFilenamesToRemove.contains(filename)
                    && actualFilesInCWD.contains(filename)) {
                //make the sha1Hash of the contents of the file in the CWD
                String hashOfFile = makeHash(filename,
                        readContentsAsString(grabFile(CWD, filename)));
                //compare to the same filename in the current commit
                //see if the hashes are the same
                //if they are not the same, that means the cwd has been modified
                if (!currentHashMap.get(filename).equals(hashOfFile)
                        && !stagedFiles.contains(filename)) {
                    //remove the filename in the actualfilesincwd for untracked
                    actualFilesInCWD.remove(filename);
                    System.out.println(filename + " (modified)");
                }
            }

        }
    }


    /** METHODS */

    /**
     * Failure cases:
     * If there is already a Gitlet version-control system in the current directory,
     * it should abort.
     * It should NOT overwrite the existing system with a new one.
     * Should print the error message
     * "A Gitlet version-control system already exists in the current directory."
     *
     * Runtime:
     * Should be constant relative to any significant measure.
     *
     * else make a new gitlet version-control
     * make the initial commit
     *
     * create the .gitlet directory
     * create the commits directory
     * create the stagingArea directory
     * create the stagingAreaRemove directory
     * create the storeObjects directory
     * create the Head file
     * create the Master file
     *
     * make an initial commit object
     * a commit with no files
     * commit message is "initial commit"
     * single branch of (master)
     * timestamp 00:00:00 PST, Thursday, 1 January 1970
     * store the commits in the commits directory
     * use the helper method makeHash(Commit) to make a hash for the commit
     * write the commit object to the initCommitFile
     * that has the title of the sha1Hash of the commit
     *
     * initialize a master and head branch file and have it point to the initial commit
     * write the string hash that was retrieved earlier to the HEAD and MASTER files
     *
     * add the master branch to the hashMap that keeps track of the branches
     */
    public static void setupInit() {
        if (GITLET_DIR.exists()) {
            String mes = "A Gitlet version-control system already exists in the current directory.";
            Utils.message(mes);
            System.exit(0);
        } else {
            GITLET_DIR.mkdir();
            COMMIT_FOLDER.mkdir();
            STAGING_AREA_FOLDER.mkdir();
            STAGING_AREA_REMOVE_FOLDER.mkdir();
            STORE_OBJECTS_FOLDER.mkdir();
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BRANCH_MAP.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                CURRENT_BRANCH.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Commit initial = new Commit();
            String hashForCommit = makeHash(initial);
            File initCommitFile = join(COMMIT_FOLDER, hashForCommit);
            writeObject(initCommitFile, initial);

            writeContents(HEAD, hashForCommit);

            //initializes hashmap to keep track of the branches
            //keys are the branch names
            //values are the commit ids of the branch pointer
            HashMap<String, String> branchMap = new HashMap<String, String>();
            branchMap.put("master", hashForCommit);
            writeObject(BRANCH_MAP, branchMap);

            //initializes the name of the currentBranch to the Current_Branch file
            String currentBranch = "master";
            writeContents(CURRENT_BRANCH, currentBranch);
        }
    }

    /**
     * Failure cases:
     * If the file does not exist,
     * print the error message "File does not exist." and exit without changing anything.
     *
     * Runtime: In the worst case,
     * should run in linear time relative to the size of the file being added
     * and lgN, for N the number of files in the commit.
     *
     * grab the file with that associated filename first by using the helper method
     * check if the file with that path exists in the CWD move on
     * if not then error out
     *
     * if the file is in the staging to remove directory
     * then delete the file in the staging to remove
     *
     * if the same file is already staged(file with the same name but different contents
     * exists in the staging area directory)
     * rewrite the content of that file
     * do so by hashing both contents and comparing if they are the same
     * if not then delete the file in the staging area
     *
     * if the same file exists in the current commit (HEAD), then remove it
     * use the getCommit() helper method to get the commit
     * save the commit to a Commit object
     * grab the hashmap of the commit object
     * compare by hashing the fileToAdd then compare the hash to the hashMap in the commit
     * dont stage the file remove in the staging area
     *
     * @param filename the name of the file that the user wants to add to the staging area
     */
    public static void add(String filename) {
        File fileToAdd = grabFile(CWD, filename);
        if (!fileToAdd.exists()) {
            Utils.message("File does not exist.");
            System.exit(0);
        } else {
            if (grabFile(STAGING_AREA_REMOVE_FOLDER, filename).exists()) {
                grabFile(STAGING_AREA_REMOVE_FOLDER, filename).delete();
            } else {
                String hashOfFileToAdd = makeHash(filename, readContentsAsString(fileToAdd));
                Commit currentCommit = grabCommit(HEAD);
                HashMap<String, String> filesTracking = currentCommit.getFilesTracked();
                if (filesTracking.containsValue(hashOfFileToAdd)) {
                    if (grabFile(STAGING_AREA_FOLDER, filename).exists()) {
                        grabFile(STAGING_AREA_FOLDER, filename).delete();
                    }
                } else {
                    File fileToOverwrite = grabFile(STAGING_AREA_FOLDER, filename);

                    String hashOfAddContents = makeHash(filename, readContentsAsString(fileToAdd));
                    String hashOfOverwriteContents = null;
                    if (fileToOverwrite.exists()) {
                        hashOfOverwriteContents = makeHash(filename,
                                readContentsAsString(fileToOverwrite));
                        if (!hashOfAddContents.equals(hashOfOverwriteContents)) {
                            fileToOverwrite.delete();
                        }
                    }
                    File addedFile = join(STAGING_AREA_FOLDER, filename);
                    writeContents(addedFile, readContentsAsString(fileToAdd));
                }
            }
        }
    }

    /**
     * Failure cases:
     * If no files have been staged, abort.
     * Print the message "No changes added to the commit."
     * Every commit must have a non-blank message.
     * If it doesn’t, print the error message "Please enter a commit message."
     * It is not a failure for tracked files to be missing from the working directory
     * or changed in the working directory.
     * Just ignore everything outside the .gitlet directory entirely.
     *
     * Runtime: Runtime should be constant with respect to any measure of number of commits.
     * Runtime must be no worse than linear with respect to the
     * total size of files the commit is tracking.
     * Additionally, this command has a memory requirement:
     * Committing must increase the size of the .gitlet directory by no more than
     * the total size of the files staged for addition at the time of commit,
     * not including additional metadata.
     * This means don’t store redundant copies of versions of files that a commit
     * receives from its parent
     * (hint: remember that blobs are content addressable and use the SHA1 to your advantage).
     * You are allowed to save whole additional copies of files;
     * don’t worry about only saving diffs, or anything like that.
     *
     * By default, each commit’s snapshot of files will be exactly the same
     * as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     *
     * A commit will only update the contents of files it is tracking
     * that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged
     * instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition
     * but weren’t tracked by its parent.
     *
     * The staging area is cleared after a commit.
     *
     * The commit command never adds, changes, or removes files in the working directory
     * (other than those in the .gitlet directory).
     * The rm command will remove such files, as well as staging them for removal,
     * so that they will be untracked after a commit.
     *
     * Any changes made to files after staging for addition or removal
     * are ignored by the commit command, which only modifies the contents of the .gitlet directory
     * For example, if you remove a tracked file using the Unix rm command
     * (rather than Gitlet’s command of the same name),
     * it has no effect on the next commit,
     * which will still contain the (now deleted) version of the file.
     *
     * After the commit command,
     * the new commit is added as a new node in the commit tree.
     *
     * The commit just made becomes the 'current commit',
     * and the head pointer now points to it.
     * The previous head commit is this commit’s parent commit.
     *
     * Each commit should contain the date and time it was made.
     *
     * Each commit has a log message associated with it that describes
     * the changes to the files in the commit.
     * This is specified by the user.
     * The entire message should take up only one entry in the array args that is passed to main.
     * To include multiword messages, you’ll have to surround them in quotes.
     *
     * Each commit is identified by its SHA-1 id,
     * which must include the file (blob) references of its
     * files,
     * parent reference,
     * log message,
     * and commit time.
     *
     * // read from my computer the head commit hash put it in a string
     *         // read from my computer the head commit and put it in a commit
     *         // read from my computer the hashmap in the head commit
     *         // grab the list of files in the staging area
     *         // grab the list of files in the stage to remove area
     *
     * // check if there are even files in the staging area or the stage to remove area
     *         // if no files in both then that means no changes need to be made
     *         // print "No changes added to the commit." and abort
     *
     * // check to see if the staging area to remove is empty or not
     *             // if the staging area to remove is empty do nothing
     *             // if not empty, then remove the files from the newhashmap
     *
     * // check to see if the staging area is empty or not. if empty, do nothing
     *             // if not empty add the files to the newhashmap
     *             // for (# of files in the staging area)
     *             // grab the files in the staging area
     *             // compare which files to pass on and which to ignore
     *             // if the file is in both the staging area and the previous commit
     *             // have the same name not hash
     *             // (find if the same name of the file in the staging area is in the oldHashMap)
     *             // that mean the file in the staging area is an updated version, then
     *             //  replace the file in the newhashmap
     *             //
     *             // else the file is in the staging area only
     *             //  add the file in the hashmap
     *             //  hash the file i want to save
     *             //  input into the hashmap
     *             //  key = filename
     *             //  value = sha1Hash
     *             // store the new/edited files in the staging area,
     *             // in the storeObjects directory with name of sha1Hash
     *
     * @param message the message that is associated with the commit
     * */
    public static void commit(String message) {

        String hashOfParent = readContentsAsString(HEAD);
        Commit currentCommit = grabCommit(HEAD);
        HashMap<String, String> oldHashMap = currentCommit.getFilesTracked();
        HashMap<String, String> newHashMap = currentCommit.getFilesTracked();
        String[] listOfFilenames = STAGING_AREA_FOLDER.list();
        String[] listOfFilenamesToRemove = STAGING_AREA_REMOVE_FOLDER.list();

        if (listOfFilenames.length == 0 && listOfFilenamesToRemove.length == 0) {
            Utils.message("No changes added to the commit.");
            System.exit(0);
        } else {

            if (listOfFilenamesToRemove.length != 0) {
                for (int i = 0; i < listOfFilenamesToRemove.length; i++) {
                    File fileOnStage = grabFile(STAGING_AREA_REMOVE_FOLDER,
                            listOfFilenamesToRemove[i]);
                    if (oldHashMap.containsKey(listOfFilenamesToRemove[i])) {
                        newHashMap.remove(listOfFilenamesToRemove[i]);
                    }
                    //delete file in staging area to remove
                    fileOnStage.delete();
                }
            }

            if (listOfFilenames.length != 0) {
                for (int i = 0; i < listOfFilenames.length; i++) {
                    File fileOnStage = grabFile(STAGING_AREA_FOLDER, listOfFilenames[i]);
                    if (oldHashMap.containsKey(listOfFilenames[i])
                            && !oldHashMap.containsValue(
                                    makeHash(listOfFilenames[i],
                                            readContentsAsString(fileOnStage)))) {
                        newHashMap.remove(listOfFilenames[i]);
                    }
                    newHashMap.put(listOfFilenames[i],
                            makeHash(listOfFilenames[i],
                                    readContentsAsString(fileOnStage)));
                    // adds the file in the storeObjects folder with file name as a
                    // sha1Hash taking into account the contents of the file
                    File addToObjects = join(STORE_OBJECTS_FOLDER,
                            makeHash(listOfFilenames[i],
                                    readContentsAsString(fileOnStage)));
                    writeContents(addToObjects, readContentsAsString(fileOnStage));
                    //delete file in staging area
                    fileOnStage.delete();
                }
            }
        }
        // make a new commit object
        // pass in the current head commit save as parent1
        // pass in the hashmap
        // pass in its message according to the user input
        Commit newCommit = new Commit(message, hashOfParent, "end", newHashMap);

        //hash the new commit
        String hashOfNewCommit = makeHash(newCommit);

        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);

        //check what the current head is
        //if current head is a branch then change the value in for that branch in the branchMap
        if (branchMap.containsValue(readContentsAsString(HEAD))) {
            branchMap.put(currentBranch, hashOfNewCommit);
            writeObject(BRANCH_MAP, branchMap);
        }
        //make head point at this commit
        writeContents(HEAD, hashOfNewCommit);

        //add commit to the commits directory
        File commitToAdd = join(COMMIT_FOLDER, hashOfNewCommit);
        writeObject(commitToAdd, newCommit);
    }

    /**
     * Failure cases:
     * If the file is neither staged nor tracked by the head commit,
     * print the error message "No reason to remove the file."
     *
     * Runtime:
     * Should run in constant time relative to any significant measure.
     *
     * Unstage the file from the staging area if it is currently staged for addition.
     * else do nothing
     *
     * If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working directory
     * if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     *
     *
     * staging area to remove files are stored just by filename and no contents
     * the commit method later just checks if the oldHashmap contains the filename in its mapping
     * */
    public static void rm(String filename) {
        File fileToDelete = grabFile(STAGING_AREA_FOLDER, filename);

        Commit currentCommit = grabCommit(HEAD);
        HashMap<String, String> currentHashMap = currentCommit.getFilesTracked();
        File fileToAdd = grabFile(CWD, filename);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        } else if (currentHashMap.containsKey(filename)) {
            if (fileToAdd.exists()) {
                fileToAdd.delete();
            }
            File addFile = join(STAGING_AREA_REMOVE_FOLDER, filename);
            try {
                addFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.message("No reason to remove the file.");
            System.exit(0);
        }
    }

    /**
     * Failure cases: None
     *
     * Runtime: Should be linear with respect to the number of nodes in head’s history.
     *
     * Starting at the current head commit,
     * display information about each commit backwards along the commit tree
     * until the initial commit,
     * following the first parent commit links,
     * ignoring any second parents found in merge commits.
     * (In regular Git, this is what you get with git log --first-parent).
     * This set of commit nodes is called the commit’s history
     *
     * FORMAT
     * There is a === before each commit and an empty line after it.
     * As in real Git, each entry displays the unique SHA-1 id of the commit object.
     * The timestamps displayed in the commits reflect the current timezone, not UTC;
     * as a result, the timestamp for the initial commit
     * does not read Thursday, January 1st, 1970, 00:00:00,
     * but rather the equivalent Pacific Standard Time.
     * Your timezone might be different depending on where you live, and that’s fine.
     *
     * Display commits with the most recent at the top.
     *
     * grab the head commit
     * grab the hash of the head commit
     *
     * use a while loop to go until the parent hash is null
     *
     * grab the commit and call getparent2 method
     * check if the commit has 2 parents, if it does
     * For merge commits (those that have two parent commits),
     * add a line just below the first, as in
     * where the two hexadecimal numerals following 'Merge:'
     * consist of the first seven digits of the first and second parents’ commit ids,
     * in that order
     * The first parent is the branch you were on when you did the merge;
     * the second is that of the merged-in branch. This is as in regular Git.
     *
     * */
    public static void log() {

        String hashOfPrintCommit = readContentsAsString(HEAD);

        Commit currentToPrintCommit = grabCommit(HEAD);
        while (!hashOfPrintCommit.equals("end")) {
            if (!currentToPrintCommit.getParent2().equals("end")) {
                System.out.println("===");
                System.out.println(String.format("commit %s", hashOfPrintCommit));
                System.out.println(String.format("Merge: %s %s",
                        currentToPrintCommit.getParent().substring(0, 7),
                        currentToPrintCommit.getParent2().substring(0, 7)));
                System.out.println(String.format("Date: %s", currentToPrintCommit.getTimeStamp()));
                System.out.println(String.format(currentToPrintCommit.getMessage()));
                System.out.println();
            }
            System.out.println("===");
            System.out.println(String.format("commit %s", hashOfPrintCommit));
            System.out.println(String.format("Date: %s", currentToPrintCommit.getTimeStamp()));
            System.out.println(String.format(currentToPrintCommit.getMessage()));
            System.out.println();

            hashOfPrintCommit = currentToPrintCommit.getParent();
            currentToPrintCommit = grabCommit(hashOfPrintCommit);
        }
    }

    /**
     * Failure cases: None
     *
     * Runtime: Linear with respect to the number of commits ever made.
     *
     * Like log, except displays information about all commits ever made.
     * The order of the commits does not matter.
     * Hint: there is a useful method in gitlet.Utils
     * that will help you iterate over files within a directory.
     * */
    public static void globalLog() {
        for (String commitHash: COMMIT_FOLDER.list()) {
            Commit commitInHash = grabCommit(commitHash);
            if (!commitInHash.getParent2().equals("end")) {
                System.out.println("===");
                System.out.println(String.format("commit %s", commitHash));
                System.out.println(String.format("Merge: %s %s",
                        commitInHash.getParent().substring(0, 7),
                        commitInHash.getParent2().substring(0, 7)));
                System.out.println(String.format("Date: %s", commitInHash.getTimeStamp()));
                System.out.println(String.format(commitInHash.getMessage()));
                System.out.println();
            }
            System.out.println("===");
            System.out.println(String.format("commit %s", commitHash));
            System.out.println(String.format("Date: %s", commitInHash.getTimeStamp()));
            System.out.println(String.format(commitInHash.getMessage()));
            System.out.println();
        }
    }

    /**
     * Failure cases: If no such commit exists,
     * prints the error message "Found no commit with that message."
     *
     * Runtime: Should be linear relative to the number of commits.
     *
     * Prints out the ids of all commits that have the given commit message, one per line.
     * If there are multiple such commits, it prints the ids out on separate lines.
     * The commit message is a single operand; to indicate a multiword message,
     * put the operand in quotation marks, as for the commit command below.
     * Hint: the hint for this command is the same as the one for global-log.
     *
     * */
    public static void find(String commitMessage) {
        Boolean ifExists = false;

        for (String commitHash: COMMIT_FOLDER.list()) {
            Commit commitInHash = grabCommit(commitHash);
            if (commitInHash.getMessage().equals(commitMessage)) {
                ifExists = true;
                System.out.println(commitHash);
            }
        }
        if (!ifExists) {
            Utils.message("Found no commit with that message.");
            System.exit(0);
        }
    }

    /**
     * Failure cases: none
     *
     * Runtime: Make sure this depends only on the amount of data in the working directory
     * plus the number of files staged to be added or deleted plus the number of branches.
     *
     * Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal.
     *
     * There is an empty line between sections, and the entire status ends in an empty line as well
     * Entries should be listed in lexicographic order,
     * using the Java string-comparison order (the asterisk doesn’t count).
     * A file in the working directory is "modified but not staged" if it is
     *
     * (modified)
     * Tracked in the current commit, changed in the working directory, but not staged; or
     * Staged for addition, but with different contents than in the working directory; or
     *
     * (deleted)
     * Staged for addition, but deleted in the working directory; or
     * Not staged for removal,
     * but tracked in the current commit and deleted from the working directory.
     *
     * The final category ("Untracked Files") is for files present in the working directory
     * but neither staged for addition nor tracked.
     * This includes files that have been staged for removal,
     * but then re-created without Gitlet’s knowledge.
     * Ignore any subdirectories that may have been introduced,
     * since Gitlet does not deal with them.
     *
     * // modified but unstaged
     * //        //grabs the current commit at head
     * //        Commit currentCommit = grabCommit(HEAD);
     * //        //grabs the hashmap of the current commit
     * //        HashMap<String,String> commitHashMap = currentCommit.getFilesTracked();
     * //
     * //        //prints out the modifications to files that havent been staged yet
     *
     *
     *
     * //
     * //
     * //        //checks the deleted files
     * //        //(condition that the file is staged for addition
     * but is deleted in the working directory)
     * //        System.out.println(filename + " (deleted)");
     * //
     * //        //iterate through all the files that are tracked
     * //        for (String filename : actualFilesInCWD) {
     * //
     * //
     * //            //checks the modified files
     * //
     * //            //check if this file is in the staging area
     * //            //if statement shorts to else if the file is not in the staging area
     * //            //(condition that the file is staged for addition,
     * but with different contents than in the working directory)
     * //            if (grabFile(STAGING_AREA_FOLDER,filename).exists() &&
     * //                    !makeHash(readContents(grabFile
     * (STAGING_AREA_FOLDER,filename))).equals(hashOfFile)) {
     * //                System.out.println(filename + " (modified)");
     * //            } else {
     * //                //(condition that the file is tracked in the current commit,
     * changed in the working directory, but not staged)
     * //                if (commitHashMap.containsKey(filename) && !hashOfFile.
     * equals(commitHashMap.get(filename))) {
     * //                    System.out.println(filename + " (modified)");
     * //                }
     * //            }
     * //        }
     *
     * //untracked
     *
     //
     *
     * */
    public static void status() {
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);

        //lexicographical order
        List<String> branchMapList = new ArrayList<String>(branchMap.keySet());
        Collections.sort(branchMapList);

        //prints the branches
        System.out.println("=== Branches ===");
        for (String branchName: branchMapList) {
            if (branchName.equals(currentBranch)
                    && readContentsAsString(HEAD).equals(branchMap.get(currentBranch))) {
                System.out.println("*" + branchName);
            } else {
                System.out.println(branchName);
            }
        }
        System.out.println();

        //prints the filenames of the files in the stagingArea
        System.out.println("=== Staged Files ===");
        List<String> stagedFiles = plainFilenamesIn(STAGING_AREA_FOLDER);
        if (stagedFiles != null) {
            for (String filename : stagedFiles) {
                System.out.println(filename);
            }
        }
        System.out.println();

        //prints the filenames of the files in the staging to remove area
        System.out.println("=== Removed Files ===");
        List<String> listOfFilenamesToRemove = plainFilenamesIn(STAGING_AREA_REMOVE_FOLDER);
        if (listOfFilenamesToRemove != null) {
            for (String filename : listOfFilenamesToRemove) {
                System.out.println(filename);
            }
        }
        System.out.println();

        //the list of actual files in the CWD that we want to look at
        List<String> actualFilesInCWD = grabCWDFiles();
        //the hashmap of the current commit
        HashMap<String, String> currentHashMap = grabCommit(HEAD).getFilesTracked();
        //the keys of the current commit's hashmap
        Set<String> currentCommitFilenames = currentHashMap.keySet();

        //prints the file that are modified but not staged
        System.out.println("=== Modifications Not Staged For Commit ===");
        modButNotStage(actualFilesInCWD,
                stagedFiles,
                listOfFilenamesToRemove,
                currentCommitFilenames,
                currentHashMap);
        System.out.println();

        //prints out the files that are untracked
        System.out.println("=== Untracked Files ===");
        for (String filename : actualFilesInCWD) {
            //make the sha1Hash of the contents of the file in the CWD
            String hashOfFile = makeHash(filename, readContentsAsString(grabFile(CWD, filename)));
            //if the file is not in the staging area or
            //if the file is not tracked under the storeObjects
            //then print the name of the file
            if (!grabFile(STAGING_AREA_FOLDER, filename).exists()
                    && !grabFile(STORE_OBJECTS_FOLDER, hashOfFile).exists()) {
                System.out.println(filename);
            }
        }
        System.out.println();


    }

    /**
     * Failure cases:
     * If the file does not exist in the previous commit
     * , abort, printing the error message "File does not exist in that commit."
     * Do not change the CWD.
     *
     * Runtimes:
     * Should be linear relative to the size of the file being checked out.
     *
     * Takes the version of the file as it exists in the head commit
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     *
     * grab the head commit with the helper method
     * grab the hashmap in the headCommit
     *
     * grab the hash of the file
     * grab the right file in the store objects
     * grab the right file in the CWD
     * overwrite the file in the CWD with the contents of storeObjects
     *
     * */
    public static void checkout(String file) {
        Commit headCommit = grabCommit(HEAD);
        HashMap<String, String> tracking = headCommit.getFilesTracked();

        if (!tracking.containsKey(file)) {
            Utils.message("File does not exist in that commit.");
            System.exit(0);
        } else {
            String hashOfFile = tracking.get(file);
            File fileInStoreObjects = join(STORE_OBJECTS_FOLDER, hashOfFile);
            File fileInCWD = join(CWD, file);
            writeContents(fileInCWD, readContentsAsString(fileInStoreObjects));
        }
    }

    /**
     * Failure cases:
     * If no commit with the given id exists, print "No commit with that id exists."
     * Otherwise, if the file does not exist in the given commit,
     * print the same message as for failure case 1. Do not change the CWD.
     *
     * Runtimes:
     * Should be linear with respect to the
     * total size of the files in the commit’s snapshot.
     * Should be constant with respect to any measure involving number of commits.
     * Should be constant with respect to the number of branches.
     *
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     * */
    public static void checkout(String commitId, String file) {

        Commit pointCommit = null;
        for (String commitIds: COMMIT_FOLDER.list()) {
            if (commitIds.substring(0, 6).equals(commitId.substring(0, 6))) {
                pointCommit = readObject(grabFile(COMMIT_FOLDER, commitIds), Commit.class);
            }
        }

        if (pointCommit == null) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }
        HashMap<String, String> tracking = pointCommit.getFilesTracked();
        if (!tracking.containsKey(file)) {
            Utils.message("File does not exist in that commit.");
            System.exit(0);
        } else {
            String hashOfFile = tracking.get(file);
            File fileInStoreObjects = join(STORE_OBJECTS_FOLDER, hashOfFile);
            File fileInCWD = join(CWD, file);
            writeContents(fileInCWD, readContentsAsString(fileInStoreObjects));
        }

    }

    /**
     * Failure cases:
     * If no branch with that name exists, print "No such branch exists."
     * If that branch is the current branch, print "No need to checkout the current branch."
     * If a working file is untracked in the current branch
     * and would be overwritten by the checkout,
     * print "There is an untracked file in the way;
     * delete it, or add and commit it first." and exit;
     * perform this check before doing anything else. Do not change the CWD.
     *
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist.
     * Also, at the end of this command,
     * the given branch will now be considered the current branch (HEAD).
     * Any files that are tracked in the current branch but are not present in
     * the checked-out branch are deleted. The staging area is cleared,
     * unless the checked-out branch is the current branch (see Failure cases below).
     * */
    public static void checkoutbranch(String branch) {
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);

        if (!branchMap.containsKey(branch)) {
            Utils.message("No such branch exists.");
            System.exit(0);
        }

        //grabs the head commit and its hashmap
        Commit headCommit = grabCommit(HEAD);
        HashMap<String, String> headTracking = headCommit.getFilesTracked();
        //grabs the branch commit and its hashmap
        Commit branchCommit = grabCommit(branchMap.get(branch));
        HashMap<String, String> branchTracking = branchCommit.getFilesTracked();

        //grabs the list of files in the CWD
        List<String> cwdFiles = grabCWDFiles();

        boolean untracked = false;
        for (String filename : cwdFiles) {
            //check the current branch
            //if not there then error
            //check the go to branch
            //if there then error
            if (!headTracking.containsKey(filename)
                && branchTracking.containsKey(filename)) {
                untracked = true;
                break;
            }
        }

        if (branch.equals(currentBranch)) {
            Utils.message("No need to checkout the current branch.");
            System.exit(0);
        } else if (untracked) {
            Utils.message("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        } else {
            //checks out all the files in the go to branch to the CWD
            for (String filename: branchTracking.keySet()) {
                if (grabFile(CWD, filename).exists()) {
                    grabFile(CWD, filename).delete();
                }
                File fileInStoreObjects = join(STORE_OBJECTS_FOLDER, branchTracking.get(filename));
                File fileInCWD = join(CWD, filename);
                writeContents(fileInCWD, readContentsAsString(fileInStoreObjects));
            }
            //removes any files currently tracked in the head but not in the branch from the CWD
            for (String filename: headTracking.keySet()) {
                if (!branchTracking.containsKey(filename)) {
                    grabFile(CWD, filename).delete();
                }
            }
            //clears the staging area
            List<String> stagedFiles = plainFilenamesIn(STAGING_AREA_FOLDER);
            if (stagedFiles != null) {
                for (String filename : stagedFiles) {
                    grabFile(STAGING_AREA_FOLDER, filename).delete();
                }
            }

        }

        //set the head to the branch's commitID
        writeContents(HEAD, branchMap.get(branch));
        //change the currentBranch variable name to branch
        writeContents(CURRENT_BRANCH, branch);
    }

    /**
     * Failure cases:
     * If a branch with the given name already exists,
     * print the error message "A branch with that name already exists."
     *
     * Runtime:
     * Should be constant relative to any significant measure.
     *
     * Creates a new branch with the given name, and points it at the current head commit.
     * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to the newly created branch (just as in real Git).
     * Before you ever call branch,
     * your code should be running with a default branch called "master".
     * */
    public static void branch(String branchName) {
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        if (branchMap.containsKey(branchName)) {
            Utils.message("A branch with that name already exists.");
            System.exit(0);
        } else {
            String commitIdOfHead = readContentsAsString(HEAD);
            branchMap.put(branchName, commitIdOfHead);
            writeObject(BRANCH_MAP, branchMap);
        }
    }

    /**
     * Failure cases:
     * If a branch with the given name does not exist, aborts.
     * Print the error message "A branch with that name does not exist."
     * If you try to remove the branch you’re currently on, aborts,
     * printing the error message "Cannot remove the current branch."
     *
     * Runtime: Should be constant relative to any significant measure.
     *
     * Deletes the branch with the given name.
     * This only means to delete the pointer associated with the branch;
     * it does not mean to delete all commits that were created under the branch,
     * or anything like that.
     */
    public static void rmBranch(String branchName) {
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);
        if (!branchMap.containsKey(branchName)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        } else if (currentBranch.equals(branchName)) {
            Utils.message("Cannot remove the current branch.");
            System.exit(0);
        } else {
            branchMap.remove(branchName);
            writeObject(BRANCH_MAP, branchMap);
        }
    }

    /**
     * Failure case:
     * If no commit with the given id exists, print "No commit with that id exists."
     * If a working file is untracked in the current branch and would be overwritten by the reset,
     * print "There is an untracked file in the way; delete it, or add and commit it first."
     * and exit; perform this check before doing anything else.
     *
     * Runtime:
     * Should be linear with respect to the total size of files
     * tracked by the given commit’s snapshot.
     * Should be constant with respect to any measure involving number of commits.
     *
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     * See the intro for an example of what happens to the head pointer after using reset.
     * The [commit id] may be abbreviated as for checkout.
     * The staging area is cleared.
     * The command is essentially checkout of an
     * arbitrary commit that also changes the current branch head.
     * */
    public static void reset(String commitId) {
        //check if the commit exists (in shortened form)
        Commit pointCommit = null;
        for (String commitIds: COMMIT_FOLDER.list()) {
            if (commitIds.substring(0, 6).equals(commitId.substring(0, 6))) {
                pointCommit = readObject(grabFile(COMMIT_FOLDER, commitIds), Commit.class);
            }
        }
        //error out if the commit doesnt exist
        if (pointCommit == null) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }
        //grabs the hashmap of the commit that we want to go to
        HashMap<String, String> commitToTracking = pointCommit.getFilesTracked();
        //grabs the head commit and its hashmap
        Commit headCommit = grabCommit(HEAD);
        HashMap<String, String> headTracking = headCommit.getFilesTracked();
        //grabs the list of files in the CWD
        List<String> cwdFiles = grabCWDFiles();
        boolean untracked = false;
        for (String filename : cwdFiles) {
            //check the current branch
            //if not there then error
            //check the go to branch
            //if there then error
            if (!headTracking.containsKey(filename)
                    && commitToTracking.containsKey(filename)) {
                untracked = true;
                break;
            }
        }
        //checks if there are untracked files
        if (untracked) {
            Utils.message("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        } else {
            //checks out all the files in the go to commit to the CWD
            for (String filename: commitToTracking.keySet()) {
                if (grabFile(CWD, filename).exists()) {
                    grabFile(CWD, filename).delete();
                }
                File fileInStoreObjects = join(STORE_OBJECTS_FOLDER,
                        commitToTracking.get(filename));
                File fileInCWD = join(CWD, filename);
                writeContents(fileInCWD, readContentsAsString(fileInStoreObjects));
            }
            //removes any files currently tracked in the head but not in the commit from the CWD
            for (String filename: headTracking.keySet()) {
                if (!commitToTracking.containsKey(filename)) {
                    grabFile(CWD, filename).delete();
                }
            }
            //clears the staging area
            List<String> stagedFiles = plainFilenamesIn(STAGING_AREA_FOLDER);
            if (stagedFiles != null) {
                for (String filename : stagedFiles) {
                    grabFile(STAGING_AREA_FOLDER, filename).delete();
                }
            }
        }
        //change the current branch's pointer to go to the checked out commitID
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);

        branchMap.put(currentBranch, commitId);
        writeObject(BRANCH_MAP, branchMap);

        //set the head to the commit's commitID
        writeContents(HEAD, commitId);
    }

    /**
     * Failure cases:
     * If there are staged additions or removals present,
     * print the error message "You have uncommitted changes." and exit.
     * If a branch with the given name does not exist, print the error message
     * "A branch with that name does not exist."
     * If attempting to merge a branch with itself, print the error message
     * "Cannot merge a branch with itself."
     * If merge would generate an error because the commit that it does has no changes in it,
     * just let the normal commit error message for this go through.
     * If an untracked file in the current commit would be overwritten or deleted by the merge,
     * print "There is an untracked file in the way; delete it, or add and commit it first."
     * and exit; perform this check before doing anything else.
     *
     * If the split point is the same commit as the given branch,then we do nothing;
     * the merge is complete, and the operation ends with the message
     * "Given branch is an ancestor of the current branch."
     * If the split point is the current branch, then the effect is to check out the given branch,
     * and the operation ends after printing the message
     * "Current branch fast-forwarded."
     * Otherwise, we continue with the steps below.
     *
     *
     *
     * Once files have been updated according to the above,
     * and the split point was not the current branch or the given branch,
     * merge automatically commits with the log message
     * Merged [given branch name] into [current branch name].
     * Then, if the merge encountered a conflict, print the message Encountered a merge conflict.
     * on the terminal (not the log).
     * Merge commits differ from other commits:
     * they record as parents both the head of the current branch (called the first parent)
     * and the head of the branch given on the command line to be merged in.
     *
     *
     * grab the branchMap
     * grab the current branch name
     *
     * grabs the list of the files in the staging area
     * grabs the list of files in staging to remove area
     *
     * if there are staged additions or removals in their directories, then error out
     * if the branch that we want to merge with the current branch doesn't exist,
     * then error out
     * if the branch that we want to merge with is the same branch as the current branch
     * throw the error that we cannot merge with itself
     *
     * find and return the split point
     * grab the split commit
     * grab the hashmap of the split point
     *
     * grab the commit associated with the currentBranch
     * grab the current commit
     * grab the hashmap of the currentCommit
     *
     * grab the commit associated with the branchName
     * grab the branch commit
     * grab the hashmap of the branch commit
     *
     * grab all the files that i could possibly want to look at
     *
     * grabs the list of files in the CWD
     * check the current branch
     * if not there then error
     * check the go to branch
     * if there then error
     *
     * start the file comparisons
     *
     * checks if there are untracked files
     * checks if the split point is the
     *  same commit as the given branch, then we do nothing
     * If the split point is the current branch,
     *  then the effect is to check out the given branch
     *
     * call the helper method to check all the if statements
     * */
    public static void merge(String branchName) {
        HashMap<String, String> branchMap = grabBranchMap(BRANCH_MAP);
        String currentBranch = grabCurrentBranchName(CURRENT_BRANCH);
        List<String> listOfFilenames = plainFilenamesIn(STAGING_AREA_FOLDER);
        List<String> listOfFilenamesToRemove = plainFilenamesIn(STAGING_AREA_REMOVE_FOLDER);
        if (!listOfFilenamesToRemove.isEmpty() || !listOfFilenames.isEmpty()) {
            Utils.message("You have uncommitted changes.");
            System.exit(0);
        }
        if (!branchMap.containsKey(branchName)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }

        if (currentBranch.equals(branchName)) {
            Utils.message("Cannot merge a branch with itself.");
            System.exit(0);
        }
        String splitCommitHash = findSplitPoint(branchMap.get(currentBranch),
                branchMap.get(branchName));
        Commit splitCommit = grabCommit(splitCommitHash);
        HashMap<String, String> splitHashMap = splitCommit.getFilesTracked();
        String currentCommitHash = branchMap.get(currentBranch);
        Commit currentCommit = grabCommit(currentCommitHash);
        HashMap<String, String> currentHashMap = currentCommit.getFilesTracked();
        String branchCommitHash = branchMap.get(branchName);
        Commit branchCommit = grabCommit(branchCommitHash);
        HashMap<String, String> branchHashMap = branchCommit.getFilesTracked();
        Set<String> allFiles = grabAllFilesInCommits(splitCommitHash, currentBranch, branchName);
        List<String> cwdFiles = grabCWDFiles();
        boolean untracked = false;
        for (String filename : cwdFiles) {
            if (!currentHashMap.containsKey(filename)
                    && branchHashMap.containsKey(filename)) {
                untracked = true;
                break;
            }
        }
        if (untracked) {
            Utils.message("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        } else if (splitCommitHash.equals(branchCommitHash)) {
            Utils.message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (splitCommitHash.equals(currentCommitHash)) {
            checkoutbranch(branchName);
            Utils.message("Current branch fast-forwarded.");
            System.exit(0);
        } else {
            checkIFs(allFiles, splitHashMap, currentHashMap, branchHashMap,
                    splitCommitHash, currentCommitHash, branchCommitHash);

            mergeCommit("Merged " + branchName + " into " + currentBranch + ".",
                    currentCommitHash, branchCommitHash);
        }
    }

}
