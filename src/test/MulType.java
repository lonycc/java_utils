class Gen<T> {    //声明一个泛型类,这里没有限制类型持有者T的范围
	private T ob;
	
	public Gen(T ob){
		this.ob = ob;
	}
	public T getOb(){
		return ob;
	}
	public void setOb(T ob){
		this.ob = ob;
	}
	public void showType(){
		System.out.println("T的实际类型是" + ob.getClass().getName());
	}
}

class Gen2 {   //一个Object类
	private Object ob;
    public Gen2(Object ob) {
        this.ob = ob;
    }
 
    public Object getOb() {
        return ob;
    }
 
    public void setOb(Object ob) {
        this.ob = ob;
    }
 
    public void showType() {
        System.out.println("实际类型是: " + ob.getClass().getName());
    }
}

public class MulType {
	public static void main(String[] args){
		// 定义类Gen的一个Integer版本
		Gen<Integer> intOb = new Gen<Integer>(88);
		intOb.showType();
		System.out.println(intOb.getOb());
		// 定义类Gen的一个String版本
		Gen<String> strOb = new Gen<String>("what a fuck");
		strOb.showType();
		System.out.println(strOb.getOb());
		// 定义类Gen的一个Object版本
		Gen<Object> obj = new Gen<Object>(intOb);
		obj.showType();
		System.out.println(obj.getOb());
		// 定义类Gen的一个Double版本
		Gen<Double> dbl = new Gen<Double>(new Double("33"));  //注,这里初始化参数没有直接写33,因为这会被认为是int类型
		dbl.showType();
		System.out.println(dbl.getOb());
		
		System.out.println("====================================\n\n");
		
		// 定义类Gen2的一个Integer版本
		Gen2 intOb2 = new Gen2(new Integer(88));  //需要显示声明类型
		intOb2.showType();
		System.out.println((Integer) intOb2.getOb());
		// 定义类Gen2的一个String版本
		Gen2 strOb2 = new Gen2("Hello Gen!");
		strOb2.showType();
		System.out.println((String) strOb2.getOb());
		// 定义类Gen2的一个Object版本
		Gen2 objOb = new Gen2(new Object());
		objOb.showType();
		System.out.println((Object) objOb.getOb());
	}
}
