package cn.com.hangdun.service.hansight;

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
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Utils.DateUtils;
import cn.com.hangdun.Mapper.EsUserMapper;
import cn.com.hangdun.Mapper.EsdataTempMapper;
import cn.com.hangdun.pojo.hansight.EsdataTemp;
import cn.com.hangdun.pojo.hansight.MyProps;

@Service
public class EsdataService {
	
	
	@Autowired
	public  MyProps myProps;

	@Autowired
	private static EsdataTempService esdataTempService;
	@Autowired
	private EsdataTempMapper esdataTempMapper;
	
    public  TransportClient transportClient(String clusterName, boolean snf,String poolSize,String clusterPort,String clusterNode) {
        TransportClient transportClient = null;
        try {
            Settings esSetting = Settings.builder()
                    .put("cluster.name", clusterName) //集群名字
                    .put("client.transport.sniff", snf)//增加嗅探机制，找到ES集群
                    .put("thread_pool.search.size", Integer.parseInt(poolSize))//增加线程池个数，暂时设为5
                    .build();
            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName(clusterNode), Integer.valueOf(clusterPort));
            transportClient.addTransportAddresses(transportAddress);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("elasticsearch TransportClient create error!!");
        }
        System.out.println("es客户端创建成功");
        return transportClient;
    }

    public  final  String scrollId = "";

    /**
     * 第一次查询的方式
     * @param client
     * @return
     */
    public  Map<String, Object> my(TransportClient client,String index,String type){
        BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
        //设置查询条件
        SearchResponse rep =  client.prepareSearch()
                .setIndices(index) // 索引
                .setTypes(type)  //类型
                .setQuery(mustQuery)
                .setScroll(TimeValue.timeValueMinutes(2))  //设置游标有效期
                .setSize(100)  //每页的大小
                .execute()
                .actionGet();
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("scrollId",rep.getScrollId());//获取返回的 游标值
        SearchHit[] hits = rep.getHits().getHits();
        m.put("id",  hits[0].getId());
        return m;
    }


    public  Map<String,Object>  my2(String scrollId,TransportClient client,int day,int level,String index, EsdataTempService esdataTempService2){
        SearchResponse rep1 = client.prepareSearchScroll(scrollId)  //设置游标
                    .setScroll(TimeValue.timeValueMinutes(2))  //设置游标有效期
                    .execute()
                    .actionGet();
        SearchHits hits = rep1.getHits(); // 获取命中次数，查询结果有多少对象
        Iterator<SearchHit> iterator = hits.iterator();
        //遍历是否有下一组数据
       	while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            //获取事件ID
            String id = next.getId();
            //告警级别
            int severity = (int) next.getSource().get("severity");
            //发生时间
            long start_time = (long) next.getSource().get("end_time");
            if(severity>=level) {
            		EsdataTemp esdataTemp = new EsdataTemp();
            		 String name =next.getSource().get("title")+"";
            		 String number =next.getSource().get("name")+"";
            		 String content =next.getSource().get("@pu")+"";
            		 String status =next.getSource().get("handle_status")+"" ;
            		 if(!status.isEmpty()) {
	            		 esdataTemp.setContent(content);
	            		 esdataTemp.setNumber(number);
	            		 esdataTemp.setName(name);
	            		 esdataTemp.setStarttime(start_time+"");
	            		 esdataTemp.setInserttime(DateUtils.getStringDate(start_time));
	            		 esdataTemp.setLevel(severity+"");
	            		 esdataTemp.setIndex(index);
	            		 esdataTemp.setInid(id);
	            		 esdataTemp.setStatus(status);
	            		 EsdataTemp byNumber = esdataTempMapper.getByNumber(number);
	            		 if(byNumber == null) {
	            			 if(number.indexOf("#") != -1) {
	            				 String esid = number.substring(1);
	            				 esdataTemp.setId(Integer.parseInt(esid));
	            				 esdataTempMapper.insertEs(esdataTemp);
	            			 }
	            		 }else {
	            			 esdataTemp.setId(byNumber.getId());
	            			 esdataTempMapper.updateEs(esdataTemp);
	            		 }
            		 }
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

}
