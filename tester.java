import java.io.File;
public class tester {
    public static void main(String[] args){
        File give = new File("message.txt");
        File recieve = new File("encoded.txt");
        HuffmanEncoder coder = new HuffmanEncoder(give, recieve);
        HuffmanDecoder decoder = new HuffmanDecoder(recieve);
    }    
}
