package demo.ProducerAndConsumer.v2;

/**
 * @author Qinyunchan
 * @date Created in 下午5:38 2018/8/10
 * @Modified By
 */

public class PCData {

    private long intData;

    public long getData(){
        return intData;
    }

    public void set(long intData){
        this.intData = intData;

    }
    @Override
    public String toString(){
        return "data:"+intData;
    }
}
