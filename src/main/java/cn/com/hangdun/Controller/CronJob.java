package cn.com.hangdun.Controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import Utils.MyTest;
import cn.com.hangdun.pojo.hansight.MyProps;
import cn.com.hangdun.service.hansight.EsdataService;
import cn.com.hangdun.service.hansight.EsdataTempService;

@Configuration
public class CronJob {
	
	@Autowired
	public MyProps myProps;
	@Autowired
	public EsdataTempService  esdataTempService;
	
	@Autowired
	public EsdataService  esdataService;
	
	/***
	 * 商网定时任务
	 */
//	@PostConstruct
//	@Scheduled(cron="0 0 0/2 * * *")
	@RequestMapping("/EsdataDetaile")
	public void sendEsData() {
		long starttime = new Date().getTime();
		 TransportClient client =  esdataService.transportClient(myProps.getClusterName(),myProps.getSnf(),myProps.getPoolSize(),myProps.getClusterPort(),myProps.getClusterNode());
	        Map<String,Object> m1 = esdataService.my(client,myProps.getIndex(),myProps.getType());
	        String s = m1.get("scrollId").toString();
	        int i = 0;
	        while (true){
	            i++;
	            Map<String,Object> m2 = esdataService.my2(s,client,myProps.getDay(),myProps.getLevel(),myProps.getIndex(),esdataTempService);
	            // 查询不到数据了，就表示查询完了
	            if(m2 == null){
	                break;
	            }
	        }
	        client.close();
	        System.out.println("总次数："+i);
	        long endtime = new Date().getTime();
	        System.out.println("本次执行耗时："+(endtime-starttime)/1000+"秒");
	}
	/***
	 * 
	 * 本地数据库测试
	 */
//	@PostConstruct
//	@Scheduled(cron="0 0 0/2 * * ?")
	public void test() {
		
		long starttime = new Date().getTime();
		System.out.println("定时任务开始执行。。。。");
		 TransportClient client =  MyTest.transportClient(myProps.getClusterName(),myProps.getSnf(),myProps.getPoolSize(),myProps.getClusterPort(),myProps.getClusterNode());
	        Map<String,Object> m1 = MyTest.my(client,myProps.getIndex(),myProps.getType());
	        System.out.println("first:"+m1.get("id"));
	        String s = m1.get("scrollId").toString();
	        System.out.println("first:"+s);
	        int i = 0;
	        while (true){
	            i++;
	            Map<String,Object> m2 = MyTest.my2(s,client,myProps.getDay(),myProps.getLevel(),myProps.getIndex(),esdataTempService);
	            // 查询不到数据了，就表示查询完了
	            if(m2 == null){
	                break;
	            }
	            System.out.println("正在执行中。。。。");
	        }
	        System.out.println("总次数："+i);
	        long endtime = new Date().getTime();
	        System.out.println("本次执行耗时："+(endtime-starttime)/1000+"秒");
	}
	
	
	
	
	
	
	/*public void sendEsData() {
		System.out.println("我是定时任务-------下面是ES告警信息");
		System.out.println("正在读取告警信息。。。。");
		Date start = new Date();
		long starttime = start.getTime();
        try {
            // 设置集群名称biehl01,Settings设置es的集群名称,使用的设计模式，链式设计模式、build设计模式。
            Settings settings = Settings.builder().put("cluster.name", "hansight-enterprise").build();
            // 读取es集群中的数据,创建client。
            @SuppressWarnings("resource")
            TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                    // 用java访问ES用的端口是9300。es的9200是restful的请求端口号
                    // 由于我使用的是伪集群,所以就配置了一台机器,如果是集群方式,将竞选主节点的加进来即可。
                    // new InetSocketTransportAddress(InetAddress.getByName("192.168.110.133"),
                    // 9300),
                    // new InetSocketTransportAddress(InetAddress.getByName("192.168.110.133"),
                    // 9300),
                     new InetSocketTransportAddress(InetAddress.getByName(myProps.getIp()), 9300));
            // 搜索数据(.actionGet()方法是同步的，没有返回就等待)
            // 方式是先去索引里面查询出索引数据,再去文档里面查询出数据。
//            GetResponse response = client.prepareGet("audit_log", "log", "1").execute().actionGet();
            SearchRequestBuilder prepareSearch = client.prepareSearch("incident_merge");
//            SearchResponse searchResponse = prepareSearch.get();
            //SearchRequestBuilder request = client.prepareSearch();
            long totalHits = prepareSearch.get().getHits().getTotalHits();
            System.out.println("一共发现数据："+totalHits+"条");
            prepareSearch.setSearchType(SearchType.QUERY_THEN_FETCH);
            if(5<totalHits) {
            	prepareSearch.setFrom((int) (totalHits-5));
            	prepareSearch.setSize((int) totalHits);
            }
            prepareSearch.setExplain(false);
            SearchResponse response = prepareSearch.get();
            System.out.println("totalHits==="+totalHits);
//            SearchHits his2 = response.getHits();
             String string = response.toString();
             HashMap<String,Object> parseArray = JSONObject.parseObject(string,HashMap.class);
//            System.out.println("322"+parseArray.get("hits").toString());
            //获取所有的原始日志信息
            HashMap<String,Object> parseObject = JSONObject.parseObject(parseArray.get("hits").toString(),HashMap.class);
            //获取每一条原始日志，转换为ArrayList<HashMap> 如果想要获取"_source"字段信息，需要在获取key的时候转换为Map进行获取
            ArrayList<HashMap<String,String>> list = JSONObject.parseObject(parseObject.get("hits").toString(),ArrayList.class);
            
            
          ArrayList<HashMap<String,String>> listMap = new ArrayList<>();
            String json ="";
            for (int i = 0; i < list.size(); i++) {
            	HashMap<String,Object> parseObject2 = JSONObject.parseObject(list.get(i)+"",HashMap.class);
                Set<String> keySet = parseObject2.keySet();
                for (String key : keySet) {
//                	System.out.println(key+"--"+parseObject2.get(key));
    			}
			}
            SearchHits hits = response.getHits(); // 获取命中次数，查询结果有多少对象
            Iterator<SearchHit> iterator = hits.iterator();
           	while (iterator.hasNext()) {
                SearchHit next = iterator.next();
                int alarm_level = (int) next.getSource().get("alarm_level");
                long start_time = (long) next.getSource().get("start_time");
                if(alarm_level>=2) {
                	if(DateUtils.equalsDate(start_time, -1)) {
                		 next.getSource().get("alarm_name");
                		 next.getSource().get("alarm_advice");
                		 next.getSource().get("alarm_content");
                		 System.out.println("告警名称为："+next.getSource().get("alarm_name"));
                		 System.out.println("告警建议为："+next.getSource().get("alarm_advice"));
                		 System.out.println("告警详情为："+next.getSource().get("alarm_content"));
                	}
                }
            }
            // 关闭client
            client.close();
            Date end = new Date();
            long endtime = end.getTime(); 
            System.out.println("耗时："+(endtime-starttime)/1000+"秒");
        } catch (Exception e) {
            e.printStackTrace();
        }

	}*/
}
