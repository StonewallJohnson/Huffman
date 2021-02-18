import datastructs.PriorityQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class HuffmanCoder {
    private File txt;
    private File result;
    private FileReader reader;
    private PriorityQueue<LetterData> queue;
    private LetterData root;
    private Map<char, Integer> data;

    private class LetterData implements Comparable<LetterData> {
        private String c;
        private int frequency;
        private LetterData left;
        private LetterData right;

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
            // TODO: Auto-generated method stub
            return 0;
        }
    }

    /**
     * Takes a file and Huffman codes it
     * 
     * @param given the file to be encoded
     * @param to    the file to write the encoded result to
     */
    public HuffmanCoder(File given, File to) {
        txt = given;
        result = to;
        try {
            reader = new FileReader(given);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        data = new HashMap<>();
        try {
            mapFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        queue = new PriorityQueue<>();
        populateQueue();
        createTree();
        encode();
    }

    private void mapFile() throws IOException {
        int input = reader.read();
        while(input > 0){
            if(data.get(input) != null){
                //already in map
                data.replace(input, data.get(input) + 1);
            }
        }
        
    }

    private void populateQueue() {
        Set<Integer> keys = data.keySet();
        for (Integer c : keys) {
            // for every key, value pair
            LetterData myLet = new LetterData(c., data.get(c));
            queue.pushIn(myLet);
        }
    }

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

    private void encode() {
        // write tree to front of file
        try{
            
        } 
        catch (IOException e1) {
            e1.printStackTrace();
        }



        try{
            scan = new Scanner(txt);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }
}