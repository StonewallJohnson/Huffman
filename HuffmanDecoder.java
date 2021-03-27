import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.File;


public class HuffmanDecoder {
    private File txt;
    private File result;
    private LetterData root;
    private boolean EOFReached;

    public HuffmanDecoder(File in){
        txt = in;
        result = new File("decoded.txt");
        root = null;
        EOFReached = false;
        try{
            
            BitBuffer inputBuff = new BitBuffer(txt, false);
            decodeTree(inputBuff);
            FileOutputStream fis = new FileOutputStream(result);
            BufferedOutputStream outputBuff = new BufferedOutputStream(fis);
            while(!EOFReached){
                decodeLetter(root, inputBuff, outputBuff);
            }
            outputBuff.close();
            inputBuff.close();
            result.createNewFile();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

     /**
     * Reads the tree from the given file and reconstructs it
     * @param inputBuff the input of the file given
     * @throws IOException
     */
    private void decodeTree(BitBuffer inputBuff) throws IOException{
        root = createTree(root, inputBuff);
    }

    /**
     * Recursively build the huffman tree from the given file
     * @param curr current node to be made
     * @param input input stream from given file
     * @throws IOException
     */
    private LetterData createTree(LetterData curr, BitBuffer input) throws IOException{
        boolean out = input.readBit();
        if(out){
            //base case: leaf node to be made, letter of node is next byte
            char letter = (char) input.readByte();
            curr = new LetterData(letter + "", -1);
        }
        else{
            //recursion, parent node inserted
            curr = new LetterData();
            curr.left = createTree(curr.left, input);
            curr.right = createTree(curr.right, input);
        }
        return curr;
    }

    /**
     * Recursively traverses the huffman tree to decode one letter
     * and write it to the new file
     * @param curr the current node of the tree
     * @param input the bit buffer of the encoded file
     * @param output the output for the decoded file
     * @throws IOException
     */
    private void decodeLetter(LetterData curr, BitBuffer input, BufferedOutputStream output) throws IOException{
        if(curr.left == null && curr.left == null){
            //base case: leaf node, a letter is decoded
            char letter =  curr.c.charAt(0);
            if(letter != (char) 3){
                //not the end of text character
                output.write(letter);
            }
            else{
                //end of text reached
                EOFReached = true;
            }
        }
        else{
            //recursive
            boolean in = input.readBit();
            
            if(!in){
                //bit is zero, go left
                decodeLetter(curr.left, input, output);
            }
            else{
                //bit is one, go right
                decodeLetter(curr.right, input, output);
            }
        }
    }
}
