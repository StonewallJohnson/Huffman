import datastructs.PriorityQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HuffmanEncoder {
    private File txt;
    private File result;
    private PriorityQueue<LetterData> queue;
    private LetterData root;
    private HashMap<Character, ArrayList<Boolean>> encodings;
    private Map<Character, Integer> data;
    
    /**
     * Takes a file and Huffman codes it
     * @param given the file to be encoded
     * @param to    the file to write the encoded result to
     */
    public HuffmanEncoder(File given, File to) {
        txt = given;
        result = to;
        data = new HashMap<>();
        queue = new PriorityQueue<>();
 
        try {
            //add end of text character to file
            FileOutputStream output = new FileOutputStream(txt, true);
            output.write((char) 3);
            //count the occurence of letters in file
            FileInputStream reader = new FileInputStream(txt);
            BufferedInputStream buffer = new BufferedInputStream(reader);
            mapFile(buffer);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create huffman tree
        populateQueue();
        createTree();

        //write new encoded file
        encodings = new HashMap<>();
        ArrayList<Boolean> stepKeeper = new ArrayList<>();
        getEncodings(root, stepKeeper);
        encode();
        
        try{
            result.createNewFile();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

     /**
     * Reads the given file byte by byte and maps the frequency of
     * occurance into a data map
     * @throws IOException attempts to read the given file
     */
    private void mapFile(BufferedInputStream buffer) throws IOException {
        int output = buffer.read();
        char input = (char) output;
        while(output != -1){
            //until end of file
            if(data.get(input) != null){
                //already in map
                data.replace(input, data.get(input) + 1);
            }
            else{
                //not in map
                data.put(input, 1);
            }
            output = buffer.read();
            input = (char) output;
        }
        buffer.close();
    }

    /**
     * Takes the values from the frequency map, makes nodes 
     * of them and adds the nodes to a priority queue
     */
    private void populateQueue() {
        
        Set<Character> keys = data.keySet();
        for (Character c : keys) {
            // for every key, value pair
            LetterData myLet = new LetterData(c.toString(), data.get(c));
            queue.pushIn(myLet);
        }
    }

    /**
     * Uses the Huffman algorithm to create a huffman tree
     * from the data
     */
    private void createTree() {
        while (queue.size() >= 2) {
            // until one letter data left
            LetterData firstOut = queue.pop();
            LetterData secondOut = queue.pop();
            LetterData combined = new LetterData(firstOut, firstOut.c + secondOut.c,
                    firstOut.frequency + secondOut.frequency, secondOut);
            queue.pushIn(combined);
        }
        // tree is complete, letter data left is root
        root = queue.pop();
    }

    /**
     * Recursively builds the byte encoding for letters and
     * stores them in a hash map
     * @param curr
     * @param steps
     */
    private void getEncodings(LetterData curr, ArrayList<Boolean> steps){
        if(curr.left == null && curr.right == null){
            //base case: leaf node
            encodings.put(curr.c.charAt(0), new ArrayList<Boolean>(steps));
        }
        else{
            //recursive backtracking
            //append 0, go left, then remove the 0 once done
            steps.add(false);
            getEncodings(curr.left, steps);
            steps.remove(steps.size() - 1);
            //append 1, go right, then remove the 1 once done
            steps.add(true);
            getEncodings(curr.right, steps);
            steps.remove(steps.size() - 1);
        }
    }

    /**
     * Uses the tree to create a new file according to the
     * huffman encoding
     */
    private void encode() {
        try{
            BitBuffer writeBuff = new BitBuffer(result, true);
            writeTree(root, writeBuff);
            writeGiven(writeBuff);
            writeBuff.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Recursively writes the tree to the given file
     * @param curr the current node of the tree to recurse on
     * @param buffer the output stream for the output file
     * @throws IOException
     */
    private void writeTree(LetterData curr, BitBuffer buffer) throws IOException {
        if(curr == null){
            //base case: don't write anything
        }
        else{
            //recursive
            if(curr.c.length() > 1){
                //this is a parent node, write 0
                buffer.writeBit(false);
            }
            else{
                //leaf node
                buffer.writeBit(true);
                //write letter
                buffer.writeByte(curr.c.getBytes()[0]);
            }
            writeTree(curr.left, buffer);
            writeTree(curr.right, buffer);
        }
    }

    /**
     * Writes the message using the encodings of the huffman tree
     * @param outBuffer the output stream for the output file
     * @throws IOException
     */
    private void writeGiven(BitBuffer outBuffer) throws IOException{
        FileInputStream fis = new FileInputStream(txt);
        BufferedInputStream inputBuffer = new BufferedInputStream(fis);
        int output = inputBuffer.read();
        char letter = (char) output;
        while(output >= 0){
            //until end of file
            //Need to get the steps taken along the tree and write each
            //step as a bit
            ArrayList<Boolean> steps = encodings.get(letter);
            for(boolean step : steps){
                //for every step in the tree
                outBuffer.writeBit(step);
            }
            output = inputBuffer.read();
            letter = (char) output;
        }

        inputBuffer.close();
    }
}

