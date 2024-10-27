import java.time.LocalDateTime;
import java.util.*;

public class PDU {

    public static String codeMessage(String[] split) {

        String[][] chars = {
                {"@",	"Δ",	"\u0020",	"0",	"¡",	"P",	"¿",	"p"},
                {"£",	"_",	"!",	"1",	"A",	"Q",	"a",	"q"},
                {"$",	"Φ",	"\"",	"2",	"B",	"R",	"b",	"r"},
                {"¥",	"Γ",	"#",	"3",	"C",	"S",	"c",	"s"},
                {"è",	"Λ",	"¤",	"4",	"D",	"T",	"d",	"t"},
                {"é",	"Ω",	"%",	"5",	"E",	"U",	"e",	"u"},
                {"ù",	"Π",	"&",	"6",	"F",	"V",	"f",	"v"},
                {"ì",	"Ψ",	"'",	"7",	"G",	"W",	"g",	"w"},
                {"ò",	"Σ",	"(",	"8",	"H",	"X",	"h",	"x"},
                {"Ç",	"Θ",	")",	"9",	"I",	"Y",	"i",	"y"},
                {"\n",	"Ξ",	"*",	":",	"J",	"Z",	"j",	"z"},
                {"Ø",	"E",	"+",	";",	"K",	"Ä",	"k",	"ä"},
                {"ø",	"Æ",	",",	"<",	"L",	"Ö",	"l",	"ö"},
                {"\r",	"æ",	"-",	"=",	"M",	"Ñ",	"m",	"ñ"},
                {"Å",	"ß",	".",	">",	"N",	"Ü",	"n",	"ü"},
                {"å",	"É",	"/",	"?",	"O",	"§",	"o",	"à"}
        };

        Map<String, Integer> table = new HashMap<>();

        for(int row = 0; row < chars.length; row++ ){
            for(int col = 0; col < chars[row].length; col++){
                table.put(chars[row][col], row + (col * 16));
            }
        }


        List<Byte> numbers = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            numbers.addAll(Arrays.asList(ToBinary(table.get(split[i]))));
        }

        int toAdd = 8 - numbers.size() % 8;
        for (int i = 0; i < toAdd; i++) {
            Byte b = 0;
            numbers.add(b);
        }

        StringBuilder result = new StringBuilder();
        for (int i=0;i < numbers.size();i+=8) {
            int res = (numbers.get(i) & 0xFF)
                    + (numbers.get(1 + i) << 1 & 0xFF)
                    + (numbers.get(2 + i) << 2 & 0xFF)
                    + (numbers.get(3 + i) << 3 & 0xFF)
                    + (numbers.get(4 + i) << 4 & 0xFF)
                    + (numbers.get(5 + i) << 5 & 0xFF)
                    + (numbers.get(6 + i) << 6 & 0xFF)
                    + (numbers.get(7 + i) << 7 & 0xFF);
            String s = Integer.toHexString(res);
            if (s.length() == 1) {
                result.append("0");
            }
            result.append(s);
        }
        return result.toString();
    }


    public static String castNumberToHex(String number, boolean isSender) {
        int leng = 0;

        //NUMBER WITHOUT +
        if(isSender){
            if(number.length() % 2 != 0) leng = 1;
            //+2 to consider number type
            leng += (number.length()+2)/2;
        }

        else
            leng += number.length();

       if(number.length() % 2 != 0)
           number = number + "F";

       String[] digits = number.split("");

           String res = "";
           for(int i = 1; i < digits.length; i+=2){
               res = res + digits[i] + digits[i-1];
           }

                res ="0"+ Integer.toHexString(leng) + "91" + res;


        return res;
    }

    public static Byte[] ToBinary(Integer chars)
    {
        String[] binary = Integer.toBinaryString(chars).split("");

        Byte[] result = {0,0,0,0,0,0,0};
        int j=binary.length-1;
        for (int i =0; i  < binary.length; i++) {
            result[j] = binary[i].equals("1") ? (byte) 1 : 0;
            j--;
        }

        return result;
    }




}
