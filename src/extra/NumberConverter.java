package extra;

public class NumberConverter {
    public static double convert(String input){
        double num = 0;
        if(input.endsWith("p")){
            num = strToDouble(input) * 1e-12;
        }
        else if(input.endsWith("n")){
            num = strToDouble(input) * 1e-9;
        }
        else if (input.endsWith("u")){
            num = strToDouble(input) * 1e-6;
        }
        else if (input.endsWith("m")){
            num = strToDouble(input) * 1e-3;
        }
        else if (input.endsWith("k")){
            num = strToDouble(input) * 1e3;
        }
        else if (input.endsWith("M")){
            num = strToDouble(input) * 1e6;
        }
        else if (input.endsWith("G")){
            num = strToDouble(input) * 1e9;
        }
        else if (isNumeric(input)){
            num = Double.parseDouble(input);
        }
        else{
            System.out.println("Error Input Was : " + input);
            Errors.wrongInputError();
        }

        return num;
    }
    private static String numberStr(String input){
        String inp = input.substring(0,input.length()-1);
        return inp;
    }
    private static double strToDouble(String input){
        String inp = numberStr(input);
        if(!isNumeric(inp)){
            Errors.wrongInputError();
            return 0.0;
        }
        double num = Double.parseDouble(inp);
        if(num<0){
            Errors.wrongInputError();
            return 0.0;
        }
        return num;

    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
