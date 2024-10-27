import java.io.FileOutputStream;
import java.util.List;

public class BinaryWriter {
    private FileOutputStream fos;
    private List<VBD> vbds;

    public BinaryWriter(List<VBD> vbds){
        this.vbds = vbds;
    }

        public void write(String filename) {
        try {
            fos = new FileOutputStream(filename);
            writeInfo();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //first byte contains the number of letters in a word
    //every letter occupies 1 byte (255) and is transformed into char
    //and the number of sent messages is saved last
    private void writeInfo() {
        try {
            for (int i = 0; i < vbds.size(); i++) {
                VBD current = vbds.get(i);
                String message = current.getMessage();
                char[] letters = message.toCharArray();
                byte[] bytes = charToBytes(letters);
                byte[] num = {(byte) (letters.length & 0xFF)};
                fos.write(num);
                fos.write(bytes);
                fos.write(longToBytes(current.getSentMes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private byte[] longToBytes(long sent) {
        byte[] res = new byte[] {
                (byte) (sent & 0xFF),
                (byte) ((sent >> 8) & 0xFF ),
                (byte) ((sent >> 16) & 0xFF),
                (byte) ((sent >> 24) & 0xFF),
                (byte) ((sent >> 32) & 0xFF),
                (byte) ((sent >> 40) & 0xFF),
                (byte) ((sent >> 48) & 0xFF),
                (byte) ((sent >> 56) & 0xFF)};
        return res;
    }



    private byte[] charToBytes(char[] letters) {
        byte [] res = new byte[letters.length];
        for(int i = 0; i < letters.length; i++){
            res[i] = (byte) (letters[i] & 0xFF);
        }
        return res;
    }


}
