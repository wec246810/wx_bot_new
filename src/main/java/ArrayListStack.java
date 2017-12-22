/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
import java.util.ArrayList;
import java.util.Stack;

public class ArrayListStack<T> implements Stacka<T> {

    ArrayList<T> arrayList =new ArrayList<>() ;

    @Override
    public void push(T obj) {
        arrayList.add(obj);
    }

    @Override
    public T get() {
        return arrayList.get(arrayList.size()-1);
    }

    @Override
    public T pop() {
        return arrayList.remove(arrayList.size()-1);
    }
}