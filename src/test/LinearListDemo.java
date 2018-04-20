class LinearList {
	private int table[];
	private int n;
	//private static final int MAX_VALUE = 100;
	//顺序表初始化,分配内存,
	public LinearList(int n){
		table = new int[n];
		this.n = 0;
	}
	//顺序表是否为空
	public boolean isEmpty(){
		return n==0;
	}
	//顺序表是否已满
	public boolean isFull(){
		return n>=table.length;
	}
	//获取顺序表长度
	public int length(){
		return n;
	}
	//获取第i个数据元素的值
	public int get(int i){
		if(i>0 && i<=n)
			return table[i-1];
		else
			return -1;

	}
	////设置第i个数据元素值为k, 若i超出n,则同时也要更新表长度
	public void set(int i, int k){
		if(i>0 && i<=n+1)
			table[i-1] = k;
			if(i==n+1)
				n++;
	}
	//查找线性表是否包含k值
	public boolean contains(int k){
	    int j = indexof(k);
	    if(j != -1)
	          return true;
	    else
	          return false;
	}
	//查找k值,若成功返回k值首次出现位置, 否则返回-1
	public int indexof(int k){
	    int j = 0;
	    while(j<n && table[j] !=k)
	          j++;
	    if(j>=0 && j<n)
	          return j;
	    else
	          return -1;
	}
	//在第i个位置插入数据元素k,则第i个元素之后的元素都要向后移动
	public void insert(int i,int k){
		if(isFull()){
			System.out.println("--溢出--");
			return;
		}else if(i<1 || i>n+1){
			System.out.println("--插入位置非法--");
			return;
		}
		for(int j=n;j>=i-1;j--)
			table[j+1] = table[j];
		table[i] = k;
		n++;
	}
	//删除第i个元素,若成功则返回被删除的值,若失败返回-1
	public int remove(int i){
		if(i<1 || i>n){
			System.out.println("--删除位置非法--");
			return -1;
		}
		int k = table[i-1];
		for(int j=i-1;j<n-1;j++)
			table[j] = table[j+1];
		n--;
		return k;
	}
}

public class LinearListDemo{
	public static void main(String[] args){
		LinearList sql = new LinearList(20);
		System.out.println("顺序表初始元素：");
		for(int i=1;i<=5;i++){
			sql.set(i, i*i);
			System.out.print(sql.get(i)+"  ");
		}
		sql.remove(6);

		for(int i=1;i<=sql.length();i++){
			System.out.print(sql.get(i)+"  ");
		}
	}
}
