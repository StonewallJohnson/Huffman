import java.io.File;
public class tester {
    public static void main(String[] args){
        File give = new File("message.txt");
        File recieve = new File("encoded.txt");
        HuffmanCoder coder = new HuffmanCoder(give, recieve);
        //HuffmanCoder decoder = new HuffmanCoder(recieve);
    }    
}
