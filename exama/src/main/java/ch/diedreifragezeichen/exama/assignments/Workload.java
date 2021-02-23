package ch.diedreifragezeichen.exama.assignments;

public class Workload {
    public enum WorkloadDistribution{
        CONSTANT,
        LINEAR,
        EXPONENTIAL
    }

    public enum PrepareTime{
        ALLTIME,
        SEVENDAYS,
        FOURTEENDAYS
    }

    private double workingTime;
    private WorkloadDistribution distribution;
    private PrepareTime prepareTime;

    public Workload(double totalTime, WorkloadDistribution distribution, PrepareTime prepareTime){
        this.workingTime = totalTime;
        this.distribution = distribution;
        this.prepareTime = prepareTime;
    }
    public Workload(double totalTime, PrepareTime prepareTime){
        this.workingTime = totalTime;
        this.distribution = WorkloadDistribution.LINEAR;
        this.prepareTime = prepareTime;
    }
    public Workload(double totalTime){
        this.workingTime = totalTime;
        this.distribution = WorkloadDistribution.LINEAR;
        this.prepareTime = PrepareTime.SEVENDAYS;
    }

    public double getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(double workingTime) {
        this.workingTime = workingTime;
    }

    public WorkloadDistribution getDistribution() {
        return distribution;
    }

    public void setDistribution(WorkloadDistribution distribution) {
        this.distribution = distribution;
    }

    public int getPrepareTime() {
        switch(this.prepareTime){
            case ALLTIME:
                return -1;
            case SEVENDAYS:
                return 7;
            case FOURTEENDAYS:
                return 14;
            default:
                return -1;
        }
    }

    public void setPrepareTime(PrepareTime prepareTime) {
        this.prepareTime = prepareTime;
    }
}