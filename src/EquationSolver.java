public class EquationSolver {
    private static EquationSolver equationSolver = new EquationSolver();
    public static EquationSolver getInstance(){
        return equationSolver;
    }
    private Double deltaV;
    private Double deltaI;
    private Double deltaT;

    public Double getDeltaV() {
        return deltaV;
    }

    public void setDeltaV(Double deltaV) {
        this.deltaV = deltaV;
    }

    public Double getDeltaI() {
        return deltaI;
    }

    public void setDeltaI(Double deltaI) {
        this.deltaI = deltaI;
    }

    public Double getDeltaT() {
        return deltaT;
    }

    public void setDeltaT(Double deltaT) {
        this.deltaT = deltaT;
    }
}
