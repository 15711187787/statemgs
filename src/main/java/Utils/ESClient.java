package Utils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSONObject;



public class ESClient {
	
	@SuppressWarnings("unchecked")
	public static void getESdata() {
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
                     new InetSocketTransportAddress(InetAddress.getByName("192.168.1.4"), 9300));
            // 搜索数据(.actionGet()方法是同步的，没有返回就等待)
            // 方式是先去索引里面查询出索引数据,再去文档里面查询出数据。
//            GetResponse response = client.prepareGet("audit_log", "log", "1").execute().actionGet();
            SearchRequestBuilder prepareSearch = client.prepareSearch("monitor_alert");
            SearchResponse searchResponse = prepareSearch.get();
            
            
            
            //SearchRequestBuilder request = client.prepareSearch();
            long totalHits = prepareSearch.get().getHits().getTotalHits();
            prepareSearch.setSearchType(SearchType.QUERY_THEN_FETCH);
            if(5<totalHits) {
            	prepareSearch.setFrom((int) (totalHits-5));
            	prepareSearch.setSize((int) totalHits);
            }
            prepareSearch.setExplain(false);
            SearchResponse response = prepareSearch.get();
            System.out.println("totalHits==="+totalHits);
            SearchHits his2 = response.getHits();
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

	}
	/*public static void main(String[] args) {
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
                     new InetSocketTransportAddress(InetAddress.getByName("192.168.1.4"), 9300));
            // 搜索数据(.actionGet()方法是同步的，没有返回就等待)
            // 方式是先去索引里面查询出索引数据,再去文档里面查询出数据。
//            GetResponse response = client.prepareGet("audit_log", "log", "1").execute().actionGet();
            SearchRequestBuilder prepareSearch = client.prepareSearch("monitor_20200718");
            SearchResponse searchResponse = prepareSearch.get();
            
            
            
            //SearchRequestBuilder request = client.prepareSearch();
            long totalHits = prepareSearch.get().getHits().getTotalHits();
            prepareSearch.setSearchType(SearchType.QUERY_THEN_FETCH);
            prepareSearch.setScroll(TimeValue.timeValueMinutes(2));
//            if(5<totalHits) {
            	//prepareSearch.setFrom(5000);
//            	prepareSearch.setSize(10000);
//            }
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