import datastructs.PriorityQueue;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class HuffmanCoder {
    private File txt;
    private File result;
    private PriorityQueue<LetterData> queue;
    private LetterData root;
    private HashMap<Character, Byte> encodings;
    private Map<Character, Integer> data;

    private class LetterData implements Comparable<LetterData> {
        private String c;
        private int frequency;
        private LetterData left;
        private LetterData right;

        public LetterData(){

        }

        public LetterData(String letter, int freq) {
            this(null, letter, freq, null);
        }

        public LetterData(LetterData l, String letter, int freq, LetterData r) {
            left = l;
            c = letter;
            frequency = freq;
            right = r;
        }

        @Override
        public int compareTo(HuffmanCoder.LetterData o) {
            return this.frequency - o.frequency;
        }
    }

    /**
     * Takes a file and Huffman codes it
     * @param given the file to be encoded
     * @param to    the file to write the encoded result to
     */
    public HuffmanCoder(File given, File to) {
        txt = given;
        result = to;
        data = new HashMap<>();
        queue = new PriorityQueue<>();
        
        try {
            FileInputStream reader = new FileInputStream(txt);
            BufferedInputStream buffer = new BufferedInputStream(reader);
            mapFile(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        populateQueue();
        createTree();

        encodings = new HashMap<>();
        Byte b = 0x0;
        getEncodings(root, b);
        encode();
    }

    public HuffmanCoder(File in){
        txt = in;
        result = new File("decoded.txt");
        root = null;
        try{
            FileInputStream fis = new FileInputStream(txt);
            BufferedInputStream inputBuff = new BufferedInputStream(fis);
            decodeTree(inputBuff);
            //TODO: add the encodings to the map for O(1) access? Pretty sure I can't do this
            FileOutputStream fos = new FileOutputStream(result);
            BufferedOutputStream outputBuff = new BufferedOutputStream(fos);
            decodeMessage(root, inputBuff, outputBuff);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        result.createNewFile();
    }
    
    /**
     * Reads the tree from the given file and reconstructs it
     * @param inputBuff the input of the file given
     * @throws IOException
     */
    private void decodeTree(BufferedInputStream inputBuff) throws IOException{
        createTree(root, inputBuff);
    }

    /**
     * Recursively build the huffman tree from the given file
     * @param curr current node to be made
     * @param input input stream from given file
     * @throws IOException
     */
    private void createTree(LetterData curr, BufferedInputStream input) throws IOException{
        int out = input.read();
        if(out == 1){
            //base case: leaf node to be made, letter of node is next byte
            char letter = (char) input.read();
            curr = new LetterData(letter + "", -1);
        }
        else{
            //recursion, parent node inserted
            curr = new LetterData();
            createTree(curr.left, input);
            createTree(curr.right, input);
        }
    }

    private void decodeMessage(LetterData curr, BufferedInputStream input, BufferedOutputStream output) throws IOException{
        if(curr.left == null && curr.left == null){
            //base case: leaf node, a letter is decoded
            output.write(curr.c.charAt(0));
        }
        else{
            //recursive
            Byte in = (byte) input.read();
            if(in == 0x0){
                //go left
                decodeMessage(curr.left, input, output);
            }
            else{
                //go right
                decodeMessage(curr.right, input, output);
            }
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
     * @param encoding
     */
    private void getEncodings(LetterData curr, Byte encoding){
        if(curr.left == null && curr.right == null){
            //base case: leaf node
            encodings.put(curr.c.charAt(0), encoding);
        }
        else{
            //recursive
            //append 0 and go left
            encoding = (byte) (encoding << 1);
            getEncodings(curr.left, encoding);
            //append 1 and go right
            encoding = (byte) (encoding | 0x1);
            getEncodings(curr.right, encoding);
        }
    }

    /**
     * Uses the tree to create a new file according to the
     * huffman encoding
     */
    private void encode() {
        try{
            FileOutputStream fos = new FileOutputStream(result);
            BufferedOutputStream writeBuff = new BufferedOutputStream(fos);
            writeTree(root, writeBuff);
            writeGiven(writeBuff);
            writeBuff.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void writeTree(LetterData curr, BufferedOutputStream buffer) throws IOException {
        if(curr == null){
            //base case: don't write anything
        }
        else{
            //recursive
            if(curr.c.length() > 1){
                //this is a parent node, write 0
                buffer.write(0x0);
            }
            else{
                //leaf node
                buffer.write(0x1);
                //write letter
                buffer.write(curr.c.getBytes()[0]);
            }
            writeTree(curr.left, buffer);
            writeTree(curr.right, buffer);
        }
    }

    private void writeGiven(BufferedOutputStream outBuffer) throws IOException{
        FileInputStream fis = new FileInputStream(txt);
        BufferedInputStream inputBuffer = new BufferedInputStream(fis);
        int output = inputBuffer.read();
        char letter = (char) output;
        while(output >= 0){
            //until end of file
            outBuffer.write((byte) encodings.get(letter));
            output = inputBuffer.read();
            letter = (char) output;
        }
        inputBuffer.close();
    }
}