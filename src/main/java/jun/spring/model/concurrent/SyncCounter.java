package jun.spring.model.concurrent;

public class SyncCounter {

    private int sum = 0;

    public static int staticSum = 0;

    public static synchronized void syncStaticCalculate() {
        staticSum = staticSum + 1;
    }

    public void syncCalculate() {
        synchronized (this) {
            setSum(getSum() + 1);
        }
    }

    public void calculate() {
        setSum(getSum() + 1);
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
