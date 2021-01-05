package Utils;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.hangdun.pojo.hansight.EsdataTemp;
import cn.com.hangdun.pojo.hansight.MyProps;
import cn.com.hangdun.service.hansight.EsdataTempService;

@Component
public class MyTest {
		@Resource
		public static MyProps myProps;

		@Resource
		private static EsdataTempService esdataTempService;
	    public static TransportClient transportClient(String clusterName, boolean snf,String poolSize,String clusterPort,String clusterNode) {
	        TransportClient transportClient = null;
	        try {
	            Settings esSetting = Settings.builder()
	                    .put("cluster.name", "hansight-enterprise") //集群名字
	                    .put("client.transport.sniff", "true")//增加嗅探机制，找到ES集群
	                    .put("thread_pool.search.size",10)//增加线程池个数，暂时设为5
	                    .build();
	            //配置信息Settings自定义
	            transportClient = new PreBuiltTransportClient(esSetting);
	            TransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName("192.168.1.4"),9300);
	            transportClient.addTransportAddresses(transportAddress);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("elasticsearch TransportClient create error!!");
	        }
	        System.out.println("es客户端创建成功");
	        return transportClient;
	    }

	    public static final  String scrollId = "";

	    /**
	     * 第一次查询的方式
	     * @param client
	     * @return
	     */
	    public static Map<String, Object> my(TransportClient client,String index,String type){
	        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
	        //设置查询条件
//	        mustQuery.must(QueryBuilders.matchQuery("sex","男"));
//	        mustQuery.must(QueryBuilders.matchQuery("city","杭州市"));
	        SearchResponse rep =  client.prepareSearch()
	                .setIndices("monitor_alert") // 索引
	                .setTypes("doc")  //类型
	                .setQuery(mustQuery)
	                .setScroll(TimeValue.timeValueMinutes(2))  //设置游标有效期
	                .setSize(10)  //每页的大小
	                .execute()
	                .actionGet();
	        Map<String,Object> m = new HashMap<String,Object>();
	        m.put("scrollId",rep.getScrollId());//获取返回的 游标值
	        SearchHit[] hits = rep.getHits().getHits();
	        m.put("id",  hits[0].getId());
	        return m;
	    }


	    public static Map<String,Object>  my2(String scrollId,TransportClient client,int day,int level,String index, EsdataTempService esdataTempService2){
	    	System.out.println("正在执行my2方法");
	        SearchResponse rep1 = client.prepareSearchScroll(scrollId)  //设置游标
	                    .setScroll(TimeValue.timeValueMinutes(2))  //设置游标有效期
	                    .execute()
	                    .actionGet();
	        SearchHits hits = rep1.getHits(); // 获取命中次数，查询结果有多少对象
            Iterator<SearchHit> iterator = hits.iterator();
            //遍历是否有下一组数据
           	while (iterator.hasNext()) {
                SearchHit next = iterator.next();
                String id = next.getId();
                System.out.println("id=="+id);
                int severity = (int) next.getSource().get("alert_level");
                long start_time = (long) next.getSource().get("start_time");
                if(severity>=level) {
//                	if(DateUtils.equalsDate(start_time, day)) {
                		EsdataTemp esdataTemp = new EsdataTemp();
                		 String name = next.getSource().get("alert_advice").toString();
                		 String number = next.getSource().get("host_id").toString();
                		 String content = next.getSource().get("rule_description").toString();
                		 esdataTemp.setContent(content);
                		 esdataTemp.setNumber(number);
                		 esdataTemp.setName(name);
                		 esdataTemp.setStarttime(start_time+"");
                		 esdataTemp.setLevel(severity+"");
//                		 esdataTemp.setIndex(index);
                		 System.out.println("------------这是一条分割线---------------------------");
                		 System.out.println(day+"天前警名称为name："+name);
                		 System.out.println(day+"天前告警编号为number："+number);
                		 System.out.println(day+"天前告警详情为content："+content);
                		 EsdataTemp byNumber = esdataTempService2.getByNumber(number);
                		 if(byNumber == null) {
                			 esdataTempService2.insertEs(esdataTemp);
                		 }
//                	} 
                }
            }
	        Map<String,Object> m = new HashMap<String,Object>();
	        m.put("scrollId",rep1.getScrollId());
	        SearchHit[] s = rep1.getHits().getHits();
	        if(s == null || s.length == 0){
	            return null;
	        }
	        m.put("id",  (rep1.getHits().getHits())[0].getId());
	        return m;
	    }


	

	   /* public static void main (String[] args) {
	        TransportClient client =  transportClient();
	        Map<String,Object> m1 = my(client);
	        System.out.println("first:"+m1.get("id"));
	        String s = m1.get("scrollId").toString();
	        System.out.println("first:"+s);
	        int i = 0;
	        while (true){
	            i++;
	            Map<String,Object> m2 = my2(s,client);
	            // 查询不到数据了，就表示查询完了
	            if(m2 == null){
	                break;
	            }
	            System.out.println("insert  to mysql");
	        }
	        System.out.println("总次数："+i);
	        System.out.println("end");
	    }*/

}
