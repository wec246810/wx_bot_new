/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
public interface Stacka<T> {
    void push(T obj);//放入一个元素
    T get();//查看最后放入的元素
    T pop();//获取最后放入的元素
}
