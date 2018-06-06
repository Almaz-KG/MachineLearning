package multilayer_perceptron;

/**
 * Created by Almaz on 24.06.2015.
 */

/*
    Разбираем создание многослойного перцептрона на Java с нуля и
    решаем классическую задачу обучения перцептрона представлению
    функции "Исключающего ИЛИ"

 */
public class MultiLayerPerceptronXOR {
    public static final double ACTIVATION_FUNCTION_TOP_VALUE = 0.5;

    //Набор массивов для обучения
    private double[][] patterns = {
        {0, 0}, {1, 0}, {0, 1}, {1, 1}};
    double[] answers = {0, 1, 1, 0};

    private double[] entries;
    private double[] hiddens;
    private double[][] linksEH;
    private double[] linksHO;
    private double outer;

    public MultiLayerPerceptronXOR(){
        entries = new double[patterns[0].length];
        hiddens = new double[2];
        linksEH = new double[entries.length][hiddens.length];
        linksHO = new double[hiddens.length];
        init();
    }
    private void init(){
        for (int i = 0; i < linksEH.length; i++) {
            for (int j = 0; j < linksEH[i].length; j++) {
                linksEH[i][j] = Math.random() *0.3 + 0.1;
            }
        }
        for (int i = 0; i < linksHO.length; i++) {
            linksHO[i] = Math.random() *0.3 + 0.1;
        }
    }
    private void countOuter(){
        for (int i = 0; i < hiddens.length; i++) {
            hiddens[i] = 0;
            for (int j = 0; j < entries.length; j++) {
                hiddens[i] += entries[j] * linksEH[j][i];
            }
            if(hiddens[i] > ACTIVATION_FUNCTION_TOP_VALUE)
                hiddens[i] = 1;
            else
                hiddens[i] = 0;
        }

        outer = 0;
        for (int i = 0; i < linksHO.length; i++) {
            outer += linksHO[i] * hiddens[i];
        }
        if(outer > ACTIVATION_FUNCTION_TOP_VALUE)
            outer = 1;
        else
            outer = 0;
    }
    private void studyFunction(){
        double[] errors = new double[hiddens.length];
        double globalError;

        do {
            globalError = 0;
            for (int p = 0; p < patterns.length; p++) {
                for (int i = 0; i < entries.length; i++) {
                    entries[i] = patterns[p][i];
                }
                countOuter();
                double localError = answers[p] - outer;
                globalError += Math.abs(localError);

                for (int i = 0; i < hiddens.length; i++)
                    errors[i] = localError * linksHO[i];

                for (int i = 0; i < entries.length; i++)
                    for (int j = 0; j < hiddens.length; j++)
                        linksEH[i][j] += 0.1 * errors[j] * entries[i];

                for (int i = 0; i < hiddens.length; i++)
                    linksHO[i] += 0.1 * localError * hiddens[i];
            }
        } while(globalError != 0);
    }


    public static void main(String[] args) {
        MultiLayerPerceptronXOR mlp = new MultiLayerPerceptronXOR();

        mlp.studyFunction();

        System.out.println("Hidden neurons");
        for (int i = 0; i < mlp.hiddens.length; i++) {
            System.out.print(mlp.hiddens[i] + " ");
        }
        System.out.println("\nWeights entries to hidden");
        for (int i = 0; i < mlp.linksEH[0].length; i++) {
            for (int j = 0; j < mlp.linksEH.length; j++) {
                System.out.print(mlp.linksEH[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Weights hidden to out");
        for (int i = 0; i < mlp.linksHO.length; i++)
            System.out.print(mlp.linksHO[i] + " ");


        for (int p = 0; p < mlp.patterns.length; p++) {
            System.out.println("\nInput entries");
            for (int i = 0; i < mlp.entries.length; i++) {
                mlp.entries[i] = mlp.patterns[p][i];
                System.out.print(mlp.entries[i] + " ");
            }
            mlp.countOuter();
            System.out.println("\nOuter function: " + mlp.outer);
        }
    }
}
