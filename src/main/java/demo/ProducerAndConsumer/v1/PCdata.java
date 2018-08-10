package demo.ProducerAndConsumer.v1;

/**
 * @author Qinyunchan
 * @date Created in 下午4:55 2018/8/10
 * @Modified By
 */

public class PCdata {

    private final int intData;

    public PCdata(int d){
        intData = d;
    }
    public PCdata(String d){
        intData = Integer.valueOf(d);
    }
    public int getData(){
        return intData;
    }
    @Override
    public String toString(){
        return "data:"+intData;
    }
}
