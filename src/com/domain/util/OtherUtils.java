package com.domain.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.function.UnaryOperator;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.Comparator;


public class OtherUtils {

	private static final int MAX = Integer.MAX_VALUE;
	private static final int MIN = Integer.MIN_VALUE;
	public static String JAVA_VERSION = System.getProperty("java.version");
	public static String SYSTEM_ARCH = System.getProperty("sun.arch.data.model");

	public static String getType(Object object)
	{
		return object.getClass().toString();
	}

	/**
	 * @description 遍历hashmap
	 * @access public
	 * @return void
	 */
	public static void readHashMap()
	{
		String key = null, value = null;
		// 初始化一个有序的hashmap
		HashMap<String, String> hashMap = new LinkedHashMap<String, String>();
		hashMap.put(key, value);

		Iterator<?> iterator = hashMap.entrySet().iterator();
		while ( iterator.hasNext() )
		{
			Map.Entry entry = (Map.Entry)iterator.next();
			System.out.println("key:"+entry.getKey()+", value:"+entry.getValue());
		}

		boolean bool = hashMap.containsKey("demo");
		System.out.println("demo in hashMap? " + bool);
	}

	/**
	 * @descrption 输出一个矩阵
	 * @param N int
	 * @return void
	 */
	public static void graph(int N)
	{
		for ( int i = 1; i <= N; i++ )
		{
			for ( int j = 1; j <= N; j++ )
			{
				if ( i % j == 0 || j % i == 0 )
				{
					System.out.print("* ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println(i);
		}
	}

	/**
	 * @description 比较两个int32常量有多少位不同
	 * @param m int
	 * @param n int
	 * @return int
	 */
	public static int countBitDiff(int m, int n) {
		int a = m ^ n;
		int num = 0;
		while ( a > 0 )
		{
			a &= (a-1);
			num++;
		}
		return num;
	}

	/**
	 * @description 执行系统命令
	 * @param cmd String
	 * @return String
	 * @throws IOException
	 */
	public static String runCmd(String cmd) throws IOException
	{
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(cmd);
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sbf = new StringBuffer();
		String tmp;
		while ( (tmp = br.readLine()) != null )
		{
			sbf.append(tmp);
		}
		return sbf.toString();
	}

	/**
	 * @description 插入排序
	 * @param arr int[]
	 * @return int[]
	 */
	public static int[] insertSort(int[] arr)
	{
		if ( arr == null || arr.length < 2 )
		{
			return arr;
		}
		for ( int i = 1; i < arr.length; i++ )
		{
			for ( int j = i; j > 0; j-- )
			{
				if ( arr[j] < arr[j-1] )
				{
					int temp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = temp;
				} else {
					break;
				}
			}
		}
		return arr;
	}

	/**
	 * @description 选择排序
	 * @param arr int[]
	 * @return int[]
	 */
	public static int[] selectSort(int[] arr)
	{
		int minIndex = 0;
		int temp = 0;
		if ( (arr == null) || (arr.length == 0) )
		{
			return arr;
		}
		for ( int i = 0; i < arr.length - 1; i++ )
		{
			minIndex = i;//无序区的最小数据数组下标
			for ( int j = i + 1; j < arr.length; j++ )
			{//在无序区中找到最小数据并保存其数组下标
				if ( arr[j] < arr[minIndex] )
				{
					minIndex = j;
				}
			}
			if ( minIndex != i )
			{//如果无序区的最小值位置不是默认的第一个数据, 则交换之
				temp = arr[i];
				arr[i] = arr[minIndex];
				arr[minIndex] = temp;
			}
		}
		return arr;
	}

	/**
	 * @description 交换两个整型变量的值
	 * @param a int
	 * @param b int
	 * @return void
	 */
	public static void swap(int a, int b)
	{
		a = a ^ b;
		b = a ^ b;
		a = a ^ b;
	}


	/**
	 * @description 递归方法求阶乘
	 * @param n
	 * @return int
	 */
	public static int fact(int n)
	{
		if ( n == 1 )
			return 1;
		return n * fact(n - 1);
	}

	/**
	 * @description 日期转时间戳
	 * @param dateStr String 例如 2016-10-10 10:11:00
	 * @param isSecond Boolean 是否返回秒
	 * @return String 微秒
	 * @throws ParseException
	 */
	public static String date2Stamp(String dateStr, Boolean isSecond) throws ParseException
	{
		if ( ! dateStr.matches("^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s+(0?[0-9]|1[0-9]|2[0-3]):(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])$") )
			dateStr = "2017-04-01 10:30:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(dateStr);
		long timestamp = date.getTime();
		if ( isSecond )
			return String.valueOf(timestamp/1000);
		return String.valueOf(timestamp);
	}

	/**
	 * @description 时间戳转日期Date/String
	 * @param tsStr String 例如 1476065460000  微秒
	 * @return void
	 * @throws ParseException
	 */
	public static void stamp2Date(String tsStr) throws ParseException
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long ts = new Long(tsStr);
		String date1 = simpleDateFormat.format(ts);
		Date date2 = simpleDateFormat.parse(date1);
		System.out.println("format to String(Date):" + date1);
		System.out.println("format to Date:" + date2);
	}

	/**
	 * @description hashmap的一些操作举例
	 */
	public static void hashMapUtil()
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		//hashMap.put(t1, t2);
		//hashMap.get(t1);
		//hashMap.containsKey(t1);
		//hashMap.keySet(); //获取键集合
		//hashMap.isEmpty();
		//hashMap.remove(t1);
		Iterator iterator = hashMap.entrySet().iterator();
		while ( iterator.hasNext() )
		{
			String key = (String) iterator.next();
			String value = (String) hashMap.get(key);
		}
		// 另一种遍历方式
		Set<Entry<String, String>> sets = hashMap.entrySet();
		for ( Entry<String, String> entry : sets )
		{
			String key = entry.getKey();
			String value = entry.getValue();
		}

		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		Scanner input = new Scanner(System.in);  //交互式用户输入
		System.out.println("输入y表示继续，n表示退出");
		while ( "y".equals(input.next()) )
		{
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("no",input.next());
			map.put("name",input.next());
			map.put("age",input.nextInt());
			list.add(map);
			System.out.println("输入y表示继续，n表示退出");
		}
		Iterator<HashMap<String,Object>> it = list.iterator(); //可遍历

		Object[] obj = hashMap.keySet().toArray();  //将集合转为数组
		Arrays.sort(obj);  //按值排序

	}

	//测试java8下的java.time包提供的时间日期接口
	public static void testNewTimeAPI()
	{
		java.time.LocalDate today = java.time.LocalDate.now();
		System.out.println("今天的日期：" + today);
		System.out.printf("%s 是否是闰年: %s %n", today, today.isLeapYear());
		System.out.printf("字符串20160404格式化后的日期格式是 %s %n", java.time.LocalDate.parse("20160404", java.time.format.DateTimeFormatter.BASIC_ISO_DATE));
		System.out.println("今天的日期是2017-04-12吗？：" + today.equals(today));
		java.time.LocalDate nextWeek = today.plus(1, java.time.temporal.ChronoUnit.WEEKS);
		System.out.println("下周的今天的日期：" + nextWeek);
		java.time.LocalDate lastYear = today.minus(1, java.time.temporal.ChronoUnit.YEARS);
		System.out.println("去年的今天的日期：" + lastYear);

		int year = today.getYear();
		int month = today.getMonthValue();
		int day = today.getDayOfMonth();
		System.out.println("年：" + year + "月：" + month + "日：" + day);

		java.time.LocalDate dateOfBirth = java.time.LocalDate.of(1991, 10, 11);
		System.out.println("您输入的日期是：" + dateOfBirth);
		System.out.println("今天" + today + "是否在日期" + dateOfBirth + "之后? " + today.isAfter(dateOfBirth) );
		java.time.Period period = java.time.Period.between(today, dateOfBirth);
		System.out.printf("今天%s和生日%s相差%s天%n", today, dateOfBirth, period.getDays());


		java.time.MonthDay birthday = java.time.MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
		java.time.MonthDay currentMonthDay = java.time.MonthDay.from(today);
		System.out.println("今天是你的生日吗？" + currentMonthDay.equals(birthday));

		java.time.LocalTime localTime = java.time.LocalTime.now();
		System.out.println("当前时间：" + localTime);
		java.time.LocalTime twoHoursLater = localTime.plusHours(2);
		System.out.println("两小时后的日期：" + twoHoursLater);

		java.time.Clock clock = java.time.Clock.systemUTC();
		System.out.println("当前UTC时钟: " + clock);
		java.time.Clock.systemDefaultZone();
		System.out.println("默认时钟: " + clock);

		java.time.LocalDateTime localDateTime = java.time.LocalDateTime.now();
		System.out.println("当前的日期时间：" + localDateTime);
		java.time.ZoneOffset zoneOffset = java.time.ZoneOffset.of("+05:30");
		java.time.OffsetDateTime offsetDateTime = java.time.OffsetDateTime.of(localDateTime, zoneOffset);
		System.out.println("日期和时间在时区上的偏移日期和时间：" + offsetDateTime);

		java.time.Instant instant = java.time.Instant.now();
		System.out.println("当前时间戳:" + instant);


		java.time.ZoneId zoneId = java.time.ZoneId.of(java.time.ZoneId.SHORT_IDS.get("CTT"));
		System.out.println("现在遍历所有的时区：");
		for ( java.util.Map.Entry<String, String> entry : java.time.ZoneId.SHORT_IDS.entrySet() )
		{
			//System.out.println("时区编号：" + entry.getKey() + ", 时区名：" + entry.getValue());
			continue;
		}
		java.time.ZonedDateTime zonedDateTime = java.time.ZonedDateTime.of(localDateTime, zoneId);
		System.out.println("时区编号CTT下的日期时间：" + zonedDateTime);

		java.time.YearMonth yearMonth = java.time.YearMonth.now();
		System.out.printf("这个月的年月%s有%d天%n", yearMonth, yearMonth.lengthOfMonth());
		java.time.YearMonth yearMonth_2 = java.time.YearMonth.of(2018, java.time.Month.FEBRUARY);
		System.out.printf("你输入的年月日期是%s %n", yearMonth_2);

		java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("ss:HH:mm yy-MM-dd");
		java.time.LocalDateTime localDateTime_2 = java.time.LocalDateTime.parse("58:16:23 11-11-11", dateTimeFormatter);
		System.out.printf("字符58:16:23 11-11-11转换后的日期是%s %n", localDateTime_2);
	}

	// 测试java8中提供的stream io
	public static void testStream()
	{
		int sum = IntStream.range(0, 1000).parallel().map(n -> n * n).sum();
		System.out.println(sum);

		// 集合创建Stream
		List<Integer>list = (List<Integer>) Arrays.asList(1,2,4,3,5);
		Stream<Integer>stream = list.stream();
		stream.forEach(System.out::println);

		// 创建并行Stream
		List<Integer>list_2 = (List<Integer>) Arrays.asList(1,2,4,3,5);
		Stream<Integer>stream_2 = list_2.parallelStream();
		stream_2.forEach(System.out::println);

		// 数组创建Stream
		String[] names = {"chaimm","peter","john"};
		Stream<String>stream_3 = Arrays.stream(names);
		Stream<String>stream_4 = Stream.of("chaimm","peter","john");
		stream_3.forEach(System.out::println);
		stream_4.forEach(System.out::println);

		int [] ints = new int[]{1,3,4,2,5};
		IntStream intStream= Arrays.stream(ints);
		intStream.forEach(System.out::println);

		// 创建无限数据的Stream
		//Stream.generate(()->"Hello tony!!!").forEach(System.out::println);

		// 初始化有限数据的Stream
		Stream.generate(()->"Hello tony!!!").limit(5).forEach(System.out::println);

		// 迭代
		Stream.iterate(1, x -> x +1).limit(10).forEach(System.out::println);
		Stream.iterate("a", UnaryOperator.identity()).limit(10).forEach(System.out::println);

		// 过滤
		Arrays.asList("Abc","Bc","ac","op","IQ").stream().filter(s->Character.isUpperCase(s.charAt(0))).forEach(System.out::println);

		// 去重
		Arrays.asList("a","c","ac","c","a","b").stream().distinct().forEach(System.out::println);

		// 变换
		Arrays.asList("hdfa","adfc","aedc","yui").stream().map(s->s.toUpperCase()+" ,").forEach(System.out::print);

		String [] str_1 = {"a","b","c"};
		String [] str_2 = {"d","e","f"};
		String [] str_3 = {"a","g","h"};
		Arrays.asList(str_1,str_2,str_3).stream().flatMap(str -> Stream.of(str)).map(s -> s+",").forEach(System.out::println);

		// 拆分合并
		Arrays.asList(1,2,3,4,5).stream().limit(3).forEach(System.out::println);
		Arrays.asList(1,2,3,4,5).stream().skip(3).forEach(System.out::println);
		Stream<Integer>stream1 = Arrays.asList(1,2,3).stream();
		Stream<String>stream2 = Arrays.asList("a","b","c").stream();
		Stream.concat(stream1, stream2).forEach(System.out::println);

		// 排序
		Arrays.asList(1,2,3,4,5).stream().sorted().forEach(System.out::println);
		Arrays.asList("ae","f","gqet","ertyu","zxc").stream().sorted((s1,s2)-> Integer.compare(s1.length(), s2.length())).forEach(System.out::println);
		Arrays.asList("ae","f","gqet","ertyu","zxc").stream().sorted(Comparator.comparing(String::length).thenComparing(String::compareTo)).forEach(System.out::println);

		// 反转排序
		Arrays.asList(2,3,4,1,5).stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
		Arrays.asList("ae", "f", "gqet", "abcd", "ertyu", "zxc").stream().sorted(Comparator.comparing(String::length).reversed()).forEach(System.out::println);

		// 并行, 用forEachOrdered时排序才有效
		Arrays.asList(1,4,5,2,3,6,8,9,7).stream().parallel().sorted().forEachOrdered(System.out::print);

		// 聚合
		Arrays.asList(1,2,3).stream().reduce((s,i)-> s = s+i).ifPresent(System.out::println);

		// collect() 将Stream转为List
		List<Integer>list_3= Arrays.asList(1,2,3,4,5).stream().filter(i-> i>3).collect(Collectors.toList());
		list_3.forEach(System.out::println);

		// 从文件创建Stream
		try (Stream<String>lines = Files.lines(Paths.get("/var/www/demo.php"), Charset.defaultCharset()))
		{
			lines.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws ParseException
	{
		//args是来自命令行的参数
		int N = 0;
		if ( args.length > 0 )
		{
			N = Integer.parseInt(args[0]);
		}
		System.out.println(Long.MAX_VALUE);
	}

}
