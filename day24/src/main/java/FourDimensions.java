import java.util.function.BiFunction;
import java.util.function.Function;

public record FourDimensions(int w, int x, int y, int z, String modelNumber, int modelIndex) {

    public FourDimensions inpX() {
        return new FourDimensions(w, Character.getNumericValue(modelNumber.charAt(modelIndex)), y, z, modelNumber, modelIndex + 1);
    }

    public FourDimensions inpY() {
        return new FourDimensions(w, x, Character.getNumericValue(modelNumber.charAt(modelIndex)), z, modelNumber,modelIndex + 1);
    }

    public FourDimensions inpW() {
        return new FourDimensions(Character.getNumericValue(modelNumber.charAt(modelIndex)), x, y, z, modelNumber,modelIndex + 1);
    }

    public FourDimensions inpZ() {
        return new FourDimensions(w, x, y, Character.getNumericValue(modelNumber.charAt(modelIndex)), modelNumber,modelIndex + 1);
    }

    public FourDimensions opW(String mul, BiFunction<Integer, Integer, Integer> operation) {
        return new FourDimensions(operation.apply(w, getArgument(mul)), x, y, z, modelNumber, modelIndex);
    }

    public FourDimensions opX(String mul, BiFunction<Integer, Integer, Integer> operation) {
        return new FourDimensions(w, operation.apply(x, getArgument(mul)), y, z, modelNumber, modelIndex);
    }

    public FourDimensions opY(String mul, BiFunction<Integer, Integer, Integer> operation) {
        return new FourDimensions(w, x, operation.apply(y, getArgument(mul)), z, modelNumber, modelIndex);
    }

    public FourDimensions opZ(String mul, BiFunction<Integer, Integer, Integer> operation) {
        return new FourDimensions(w, x, y, operation.apply(z, getArgument(mul)), modelNumber, modelIndex);
    }



    public static int mul(int input, int input2) {
        return input * input2;
    }

    public static int eql(int input, int input2) {
        return input == input2 ? 1 : 0;
    }

    public static int add(int input, int input2) {
        return input + input2;
    }

    public static int div(int input, int input2) {
        return Math.floorDiv(input, input2);
    }

    public static int mod(int input, int input2) {
        return input % input2;
    }

    public static int sub(int input, int input2) {
        return input - input2;
    }

    private int getArgument(String mul) {
        return switch (mul) {
            case "w" -> w();
            case "x" -> x();
            case "y" -> y();
            case "z" -> z();
            default -> Integer.parseInt(mul);
        };
    }
}
