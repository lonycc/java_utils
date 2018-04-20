import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("rawtypes")
class CollectionGenFoo<T extends Collection>{
	private T x;
	
	public CollectionGenFoo(T x){
		this.x = x;
	}
	
	public T getX(){
		return x;
	}

	public  void setX(T x){
		this.x = x;
	}
}
public class CollectionGenFooDemo {
	public <T> void f(T x){  //泛型方法
		System.out.println(x.getClass().getName());
	}
	
	public static <T> T f2(T i){
		return i;
	}
	@SuppressWarnings("rawtypes")
	public static void main(String[] args){
		CollectionGenFoo<? extends Collection> listFoo = null;
		listFoo = new CollectionGenFoo<ArrayList>(new ArrayList());
		System.out.println("instance success");
		
		CollectionGenFooDemo co = new CollectionGenFooDemo();
		co.f(listFoo);
		co.f(co);
		co.f('a');
		co.f("abc");
		co.f(2.23);
		co.f(15);
	}
	
}
