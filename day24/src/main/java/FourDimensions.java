import java.util.function.BiFunction;

public record FourDimensions(int w, int x, int y, int z, String modelNumber, int modelIndex) {

    public FourDimensions inpX() {
        return inpX(Character.getNumericValue(modelNumber.charAt(modelIndex)));
    }

    public FourDimensions inpX(int input) {
        return new FourDimensions(w, input, y, z, modelNumber, modelIndex + 1);
    }

    public FourDimensions inpY() {
        return inpY(Character.getNumericValue(modelNumber.charAt(modelIndex)));
    }

    public FourDimensions inpY(int input) {
        return new FourDimensions(w, x, input, z, modelNumber,modelIndex + 1);
    }

    public FourDimensions inpW() {
        return inpW(Character.getNumericValue(modelNumber.charAt(modelIndex)));
    }

    public FourDimensions inpW(int input) {
        return new FourDimensions(input, x, y, z, modelNumber,modelIndex + 1);
    }

    public FourDimensions inpZ() {
        return inpZ(Character.getNumericValue(modelNumber.charAt(modelIndex)));
    }

    public FourDimensions inpZ(int input) {
        return new FourDimensions(w, x, y, input, modelNumber,modelIndex + 1);
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
