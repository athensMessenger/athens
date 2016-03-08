package chat;

import java.util.Random;

/**
 * @author Wei Hua
 * @version 2016-02-29
 *
 * This class define some encryption methods that will be used
 * in the chat room project. This class also include a hash function
 * by using SHA-1 standard.
 *
 * The message will be double encrypted by Rail Fence Cipher
 * and a modified version of Monoalphabetic Substitution Ciphers.
 */
public class ChatroomSecurity {
    private Random r;
    public ChatroomSecurity(){
        r=new Random();
    }
    /**
     * This method will encrypt message by using Rail Fence Cipher
     *
     * @param message the plain text that will be encrypted.
     * @return the cipher text
     */
    public String railFenceEncrypt(String message){
        StringBuffer plaintext=new StringBuffer(message);
        StringBuffer ciphertext=new StringBuffer();

		/*
		 * The algorithm will list all the characters in a square matrix
		 *
		 * e.g. the plain text is "Birmingham University"
		 *
		 * The length of the plain text is 21 (including the 'space') since it is
		 * less than 5^2 and greater than 4^2, it will be listed in a 5x5 square
		 * matrix like:
		 *
		 *       [B][i][r][m][i]
		 *       [n][g][h][a][m]
		 *       [ ][U][n][i][v]
		 *       [e][r][s][i][t]
		 *       [y][4][t][y][u]    ------->the unused blocks will be filled with random characters
		 *
		 */
        int height=(int)Math.sqrt(plaintext.length())+1;
        int index=0;

        char[][] cipherbox=new char[height][height];

        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                if(index<plaintext.length()){
                    cipherbox[i][j]=plaintext.charAt(index);
                    index++;
                }
                else
                    cipherbox[i][j]=randomChar();
            }
        }

		/* The cipher text will be read column by column instead of row by row
		 *
		 * 		 [B][i][r][m][i]
		 *       [n][g][h][a][m]
		 *       [ ][U][n][i][v]
		 *       [e][r][s][i][t]
		 *       [y][4][t][y][u]
		 *
		 * S.t. the cipher text is [Bn eyigUr4rhnstmaiiyimvtu]
		 *
		 */

        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                ciphertext.append(cipherbox[j][i]);
            }
        }

		/*
		 * The the cipher text will be appended the real length of plain text
		 *
		 *  i.e. [Bn eyigUr4rhnstmaiiyimvtu21]
		 */
        ciphertext.append(plaintext.length());

        return ciphertext.toString();
    }
    /**
     * This method will decrypt the cipher text encrypted by RFC
     * @param message the cipher text
     * @return the plain text
     */
    public String railFenceDecrypt(String message){
        StringBuffer ciphertext=new StringBuffer(message);
        StringBuffer plaintext=new StringBuffer();

        //Load the size of the square matrix
        int height=(int)Math.sqrt(ciphertext.length());

        //read the real size of the cipher text
        int length=Integer.parseInt(ciphertext.substring(height*height));

        int index=0;

        //list the characters in the square matrix
        char[][] cipherbox=new char[height][height];

        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                cipherbox[i][j]=ciphertext.charAt(index);
                index++;
            }
        }

        //read the characters row by row and find the cipher text.
        for(int i=0;i<height;i++){
            for(int j=0;j<height;j++){
                plaintext.append(cipherbox[j][i]);
            }
        }

        //remove the appending bits of the cipher text.
        return plaintext.substring(0, length);

    }

    /**
     * This method will encrypt message by using Monoalphabetic Substitution Ciphers
     *
     * @param message the plain text that will be encrypted.
     * @return the cipher text
     */
    public String[] monoalphabeticEncrypt(String message,String keypad){
        StringBuffer plaintext=new StringBuffer(message);
        StringBuffer ciphertext=new StringBuffer();

		/*
		 * Generate a random key word (only contain[0-9][a-z][A-Z])
		 * randomString(r.nextInt(62))
		 */
        StringBuffer key=removeRepeat(keypad);

		/*
		 * generate a normal alphabet table
		 *
		 * number:    [0-9]	  [10-35]   [36-61]
		 * 				|        |         |
		 * chars:     [0-9]	  [A - Z]   [a - z]
		 */
        int[] alphabet=new int[62];
        int[] keyAlphabet=new int[62];

        for(int i=0;i<62;i++){
            alphabet[i]=i;
        }


		/*
		 * Generate a Monoalphabetic Substitution table
		 *
		 * e.g. the key is the [Athens]
		 *
		 * so the Substitution table will start with 'A' 't' 'h' 'e' 'n' 's'
		 * i.e. 10 55 43 40 49 54
		 *
		 * And then fill other left bits:
		 *
		 * normal alphabet table :
		 * 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
		 * 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61
		 *
		 * Substitution table:
		 * 10 55 43 40 49 54 0 1 2 3 4 5 6 7 8 9 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
		 * 32 33 34 35 36 37 38 39 41 42 44 45 46 47 48 50 51 52 53 56 57 58 59 60 61
		 *
		 * we get a Substitution table like 0-->10('0'-->'A')   1-->55 ....
		 */
        for(int j=0;j<key.length();j++){
            keyAlphabet[j]=antiAlphabet(key.charAt(j));
        }

        int index=key.length();

        for(int k=0;k<62;k++){
            if(!Contains(key,Alphabet(k))){
                keyAlphabet[index]=k;
                index++;
            }


        }

		/*
		 * replace the characters using the substitution table.
		 *
		 * Using the [Birmingham University] as example:
		 *
		 * the cipher text is [5dpjdkbcVj PkdvZpqdry]
		 */
        for(int x=0;x<plaintext.length();x++){
            char bit=plaintext.charAt(x);

            if(antiAlphabet(bit)==-100)
                ciphertext.append(bit);
            else{
                ciphertext.append(Alphabet(keyAlphabet[antiAlphabet(bit)]));
            }
        }

        String [] c_k=new String[2];
        //out put the cipher text.
        c_k[0]=ciphertext.toString();

        //out put the key.
        c_k[1]=key.toString();

        return c_k;


    }

    /**
     * This method will decrypt the cipher text encrypted by MSC
     * @param message the Cipher text
     * @param keypad the key
     * @return the plain text
     */
    public String monoalphabeticDecrypt(String message,String keypad){
        StringBuffer plaintext=new StringBuffer();
        StringBuffer ciphertext=new StringBuffer(message);

        StringBuffer key=removeRepeat(keypad);

        int[] alphabet=new int[62];
        int[] keyAlphabet=new int[62];

        for(int i=0;i<62;i++){
            alphabet[i]=i;
        }

        //generate substitution table
        for(int j=0;j<key.length();j++){
            keyAlphabet[j]=antiAlphabet(key.charAt(j));
        }

        int index=key.length();

        for(int k=0;k<62;k++){
            if(!Contains(key,Alphabet(k))){
                keyAlphabet[index]=k;
                index++;
            }

        }

        //replace the characters
        for(int x=0;x<ciphertext.length();x++){
            char bit=ciphertext.charAt(x);

            if(antiAlphabet(bit)==-100)
                plaintext.append(bit);
            else{
                int i=getIndex(keyAlphabet,antiAlphabet(bit));
                plaintext.append(Alphabet(alphabet[i]));
            }
        }

        return plaintext.toString();

    }
    /*
     * This method double encrypt the message using RFC and MSC
     */
    public String[] encrypt(String message,String key){
        String c1=railFenceEncrypt(message);
        String[] c2=monoalphabeticEncrypt(c1,key);

        return c2;
    }
    /*
     * This method double decrypt the message using MSC and RFC
     */
    public String decrypt(String message,String key){
        String p1= monoalphabeticDecrypt(message,key);
        String p2= railFenceDecrypt(p1);

        return p2;
    }

    //<-----------------There are some helper functions used in this class-------------------->

    /**
     * The method check whether the input message just contain
     * ASCII code.
     */
    public boolean isASCII(String input){
        boolean flag=true;

        for(int i=0;i<input.length();i++){
            if(!(0<=input.charAt(i)&&input.charAt(i)<=127))
                flag=false;
        }
        return flag;
    }

    /**
     * Generate a random character.
     * @return
     */
    public char randomChar(){
        int randomNumber=r.nextInt(62);
        return Alphabet(randomNumber);
    }

    /**
     * convert the number to the characters (base on the 0-61 table)
     * @param number
     * @return
     */
    public char Alphabet(int number){
        if(0<=number&&number<=9)
            return (char)(number+48);
        else if(10<=number&&number<=35)
            return (char)(number+55);
        else
            return (char)(number+61);
    }

    /**
     * convert the character to the number (base on the 0-61 table)
     * @param c
     * @return
     */
    public int antiAlphabet(char c){
        int asc=(int)c;

        if(48<=asc&&asc<=57)
            return asc-48;
        else if(65<=asc&&asc<=90)
            return asc-55;
        else if(97<=asc&&asc<=122)
            return asc-61;
        else
            return -100;
    }

    /**
     * This method print out all the element in a integer array
     * @param arr
     */
    public void printarr(int[] arr){
        for(int j:arr){
            System.out.print(j+" ");
        }
    }

    /**
     * Generate a random String (just contain [0-9][a-z][A-Z])
     * @param size the size of the random String
     * @return the generated String
     */
    public String randomString(int size){
        StringBuffer randomString=new StringBuffer();

        for(int i=0;i<size;i++){
            randomString.append(randomChar());
        }

        return randomString.toString();
    }

    /**
     * Check whether the Stringbuffer contain a char bit
     * @param s the String buffer
     * @param c the character
     * @return true if yes otherwise false
     */
    public boolean Contains(StringBuffer s,char c){
        if(s.length()==0)
            return false;
        boolean flag=false;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==c)
                flag=true;
        }
        return flag;
    }

    /**
     * Remove the duplicated characters in a String buffer
     * @param text the input String
     * @return the removed word
     */
    public StringBuffer removeRepeat(String text){
        StringBuffer noRepeat=new StringBuffer();
        StringBuffer message=new StringBuffer(text);
        for(int i=0;i<message.length();i++){
            if(!Contains(noRepeat,message.charAt(i)))
                noRepeat.append(message.charAt(i));
        }
        return noRepeat;
    }

    /**
     * This method return the first element's index in the integer array
     * @param arr the int array
     * @param n the integer to check
     * @return the index
     */
    public int getIndex(int[] arr,int n){
        for(int i=0;i<arr.length;i++){
            if(arr[i]==n)
                return i;
        }
        return -1;
    }

    public static void main(String[] args){
        //This is the test class
        ChatroomSecurity s=new ChatroomSecurity();

        String s1=s.railFenceEncrypt("Birmingham University");
        System.out.println("Encrypted using RFC: " +s1);

        String s2=s.railFenceDecrypt(s1);
        System.out.println("Decrypted using RFC: " +s2);

        String s3[]=s.monoalphabeticEncrypt("Birmingham University","Athens");
        System.out.println("Encrypted using MSC: " +s3[0]);

        String s4=s.monoalphabeticDecrypt(s3[0],"Athens");
        System.out.println("Decrypted using MSC: " +s4);

        String[] doubleEncrypt=s.encrypt("Birmingham University","Athens");
        System.out.println("Encrypted using RFC and MSC: "+doubleEncrypt[0]);

        String doubleDecrypt=s.decrypt(doubleEncrypt[0], "Athens");
        System.out.println("Decrypted using MSC and RFC: "+doubleDecrypt);



        //System.out.println(s.railFenceDecrypt(s1));
    }

}
