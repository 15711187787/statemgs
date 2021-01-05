///*     */ package cn.com.hangdun.Controller;
///*     */ 
/////*     */ import com.hansight.ice.iceweb.entity.EsResponse;
/////*     */ import com.hansight.ice.iceweb.entity.Page;
/////*     */ import com.hansight.ice.iceweb.entity.Sort;
/////*     */ import com.hansight.ice.iceweb.util.JsonUtil;
///*     */ import java.util.ArrayList;
///*     */ import java.util.Collection;
///*     */ import java.util.HashMap;
///*     */ import java.util.Iterator;
///*     */ import java.util.List;
///*     */ import java.util.Map;
///*     */ import org.elasticsearch.action.search.ClearScrollRequest;
///*     */ import org.elasticsearch.action.search.ClearScrollResponse;
///*     */ import org.elasticsearch.action.search.SearchRequest;
///*     */ import org.elasticsearch.action.search.SearchResponse;
///*     */ import org.elasticsearch.action.search.SearchScrollRequest;
///*     */ import org.elasticsearch.action.support.IndicesOptions;
///*     */ import org.elasticsearch.common.unit.TimeValue;
///*     */ import org.elasticsearch.index.query.QueryBuilder;
///*     */ import org.elasticsearch.index.query.QueryBuilders;
///*     */ import org.elasticsearch.search.SearchHit;
///*     */ import org.elasticsearch.search.aggregations.Aggregation;
///*     */ import org.elasticsearch.search.aggregations.AggregationBuilder;
///*     */ import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
///*     */ import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
///*     */ import org.elasticsearch.search.aggregations.bucket.terms.Terms;
///*     */ import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
///*     */ import org.elasticsearch.search.aggregations.metrics.valuecount.ParsedValueCount;
///*     */ import org.elasticsearch.search.builder.SearchSourceBuilder;
///*     */ import org.elasticsearch.search.sort.SortOrder;
///*     */ import org.joda.time.DateTime;
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ public class EsSearchUtil
///*     */   extends EsUtil
///*     */ {
///*     */   private static final int DEFAULT_FETCH_SIZE = 1000;
///*     */   
///*     */   public static EsResponse search(SearchRequest searchRequest) {
///*  49 */     EsResponse esResponse = new EsResponse();
///*     */     try {
///*  51 */       SearchResponse searchResponse = esClient.search(searchRequest, new org.apache.http.Header[0]);
///*  52 */       if (logger.isDebugEnabled()) {
///*  53 */         logger.debug("[search] - searchRequest:{}, searchResponse:{}", searchRequest, searchResponse);
///*     */       }
///*  55 */       esResponse.setTotal(searchResponse.getHits().getTotalHits());
///*  56 */       for (SearchHit it : searchResponse.getHits()) {
///*  57 */         esResponse.addData(it.getSource());
///*     */       }
///*  59 */       return esResponse;
///*     */     }
///*  61 */     catch (Exception e) {
///*  62 */       logger.error("search event error can't get es client error detail: {}", e);
///*     */       
///*  64 */       return esResponse;
///*     */     } 
///*     */   }
///*     */   
///*     */   public static List<Map> searchWithAggregation(SearchRequest searchRequest, AggregationBuilder aggregation) {
///*  69 */     SearchResponse searchResponse = null;
///*  70 */     List<Map> esResponse = new ArrayList<>();
///*     */     try {
///*  72 */       searchRequest.source().size(0).aggregation(aggregation);
///*  73 */       if (is2xVersion) {
///*  74 */         Map<String, List<Map<String, Object>>> aggregationMap = search2xWithAggregation(restClient, searchRequest);
///*  75 */         if (aggregationMap != null && !aggregationMap.isEmpty()) {
///*  76 */           Map<Object, Object> termsMap = new HashMap<>();
///*  77 */           ((List)aggregationMap.get(aggregation.getName())).stream().forEach(am -> 
///*  78 */               termsMap.put(am.get("key"), am.get("doc_count")));
///*     */           
///*  80 */           esResponse.add(termsMap);
///*     */         } else {
///*     */           
///*  83 */           logger.warn("aggregation failed in 2.x");
///*     */         } 
///*     */       } else {
///*     */         
///*  87 */         searchResponse = esClient.search(searchRequest, new org.apache.http.Header[0]);
///*  88 */         logger.debug("agg response:{}", JsonUtil.parseToJson(searchResponse));
///*  89 */         if (logger.isDebugEnabled()) {
///*  90 */           logger.debug("[searchWithAggregation] - searchRequest:{}, searchResponse:{}", searchRequest, searchResponse);
///*     */         }
///*  92 */         if (searchResponse.getAggregations() == null) {
///*  93 */           logger.debug("response has not aggs:{}. search:{}", searchResponse, searchRequest);
///*  94 */           return new ArrayList<>();
///*     */         } 
///*  96 */         Map<String, Aggregation> map = searchResponse.getAggregations().asMap();
///*  97 */         Iterator<Map.Entry<String, Aggregation>> iter = map.entrySet().iterator();
///*  98 */         while (iter.hasNext()) {
///*  99 */           Map.Entry<String, Aggregation> entry = iter.next();
///* 100 */           Aggregation agg = entry.getValue();
///* 101 */           if (agg instanceof Terms) {
///* 102 */             Map<Object, Object> termsMap = new HashMap<>();
///* 103 */             for (Terms.Bucket bucket : ((Terms)agg).getBuckets()) {
///* 104 */               termsMap.put(bucket.getKey(), Long.valueOf(bucket.getDocCount()));
///*     */             }
///* 106 */             esResponse.add(termsMap);
///*     */           } 
///*     */         } 
///*     */       } 
///*     */ 
///*     */ 
///*     */ 
///*     */       
///* 114 */       return esResponse;
///*     */     }
///* 116 */     catch (Exception e) {
///* 117 */       logger.error("search event error can't get es client error detail: {}", e);
///* 118 */       logger.error("aggregation:{}, searchRequest:{}, searchResponse:{}", new Object[] { aggregation, searchRequest, searchResponse });
///*     */       
///* 120 */       return new ArrayList<>();
///*     */     } 
///*     */   }
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */ 
///*     */   
///* 130 */   public static EsResponse search(Collection<String> eventIndexNames, String indexType, QueryBuilder query, Page page) { return search(eventIndexNames, indexType, query, null, page); }
///*     */ 
///*     */   
///*     */   public static EsResponse search(Collection<String> eventIndexNames, String indexType, QueryBuilder query, Sort sort, Page page) {
///* 134 */     EsResponse esResponse = new EsResponse();
///*     */     try {
///* 136 */       SearchRequest searchRequest = new SearchRequest(eventIndexNames.toArray(new String[eventIndexNames.size()]));
///* 137 */       searchRequest.indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
///* 138 */       searchRequest = (indexType != null) ? searchRequest.types(new String[] { indexType }) : searchRequest;
///* 139 */       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
///* 140 */       searchSourceBuilder.query(query);
///* 141 */       if (page != null) {
///* 142 */         searchSourceBuilder.from(page.getFrom());
///* 143 */         searchSourceBuilder.size(page.getSize());
///*     */       } else {
///* 145 */         searchSourceBuilder.size(1000);
///*     */       } 
///* 147 */       if (null != sort) {
///* 148 */         searchSourceBuilder.sort(sort.getField(), SortOrder.fromString(sort.getOrder().toString()));
///*     */       }
///*     */       
///* 151 */       searchRequest.source(searchSourceBuilder);
///* 152 */       SearchResponse searchResponse = esClient.search(searchRequest, new org.apache.http.Header[0]);
///* 153 */       if (logger.isDebugEnabled()) {
///* 154 */         logger.debug("[search] - searchRequest:{}, searchResponse:{}", searchRequest, searchResponse);
///*     */       }
///* 156 */       esResponse.setTotal(searchResponse.getHits().getTotalHits());
///* 157 */       for (SearchHit it : searchResponse.getHits()) {
///* 158 */         esResponse.addData(it.getSource());
///*     */       }
///* 160 */       return esResponse;
///*     */     }
///* 162 */     catch (Exception e) {
///* 163 */       logger.error("search event error can't get es client error detail: {}", e);
///*     */       
///* 165 */       return esResponse;
///*     */     } 
///*     */   }
///*     */   public static EsResponse searchWithScroll(Collection<String> eventIndexNames, String indexType, QueryBuilder query, Page page) {
///* 169 */     EsResponse esResponse = new EsResponse();
///*     */     try {
///* 171 */       SearchRequest searchRequest = new SearchRequest(eventIndexNames.toArray(new String[eventIndexNames.size()]));
///* 172 */       searchRequest.indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
///* 173 */       searchRequest = (indexType != null) ? searchRequest.types(new String[] { indexType }) : searchRequest;
///* 174 */       searchRequest.scroll(TimeValue.timeValueMinutes(30L));
///* 175 */       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
///* 176 */       searchSourceBuilder.query(query);
///* 177 */       if (page != null) {
///* 178 */         searchSourceBuilder.from(page.getFrom());
///* 179 */         searchSourceBuilder.size(page.getSize());
///*     */       } else {
///* 181 */         searchSourceBuilder.size(1000);
///*     */       } 
///* 183 */       searchRequest.source(searchSourceBuilder);
///* 184 */       SearchResponse searchResponse = esClient.search(searchRequest, new org.apache.http.Header[0]);
///* 185 */       if (logger.isDebugEnabled()) {
///* 186 */         logger.debug("[searchWithScroll] - searchRequest:{}, searchResponse:{}", searchRequest, searchResponse);
///*     */       }
///* 188 */       String scrollId = searchResponse.getScrollId();
///* 189 */       SearchHit[] searchHits = searchResponse.getHits().getHits();
///* 190 */       esResponse.setTotal(searchResponse.getHits().getTotalHits());
///* 191 */       while (searchHits != null && searchHits.length > 0) {
///* 192 */         for (SearchHit hit : searchHits) {
///* 193 */           esResponse.addData(hit.getSource());
///*     */         }
///* 195 */         SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
///* 196 */         scrollRequest.scroll(TimeValue.timeValueMinutes(30L));
///* 197 */         searchResponse = esClient.searchScroll(scrollRequest, new org.apache.http.Header[0]);
///* 198 */         scrollId = searchResponse.getScrollId();
///* 199 */         searchHits = searchResponse.getHits().getHits();
///*     */       } 
///*     */       try {
///* 202 */         ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
///* 203 */         clearScrollRequest.addScrollId(scrollId);
///* 204 */         ClearScrollResponse clearScrollResponse = esClient.clearScroll(clearScrollRequest, new org.apache.http.Header[0]);
///* 205 */         logger.debug("clear scroll handle :{}", Boolean.valueOf(clearScrollResponse.isSucceeded()));
///* 206 */       } catch (Exception exception) {}
///*     */ 
///*     */       
///* 209 */       return esResponse;
///*     */     }
///* 211 */     catch (Exception e) {
///* 212 */       logger.error("search event error can't get es client error detail: {}", e);
///*     */       
///* 214 */       return esResponse;
///*     */     } 
///*     */   }
///*     */   
///*     */   public static EsResponse searchWithAggregation(Collection<String> eventIndexNames, List<String> indexType, QueryBuilder query, AggregationBuilder aggregation) {
///* 219 */     EsResponse esResponse = new EsResponse();
///*     */     try {
///* 221 */       SearchRequest searchRequest = new SearchRequest(eventIndexNames.toArray(new String[eventIndexNames.size()]));
///* 222 */       searchRequest.indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
///* 223 */       searchRequest = (indexType != null) ? searchRequest.types(indexType.toArray(new String[indexType.size()])) : searchRequest;
///* 224 */       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
///* 225 */       searchSourceBuilder.query(query).aggregation(aggregation);
///* 226 */       searchSourceBuilder.size(0);
///* 227 */       searchRequest.source(searchSourceBuilder);
///*     */       
///* 229 */       if (is2xVersion) {
///* 230 */         Map<String, List<Map<String, Object>>> aggregationMap = search2xWithAggregation(restClient, searchRequest);
///* 231 */         if (aggregationMap != null && !aggregationMap.isEmpty()) {
///* 232 */           if (aggregation instanceof org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder) {
///* 233 */             ((List)aggregationMap.get(aggregation.getName())).forEach(m -> {
///* 234 */                   Map<Object, Object> termsMap = new HashMap<>();
///* 235 */                   if (m.get("key") != null) {
///* 236 */                     termsMap.put(m.get("key").toString(), m.get("doc_count"));
///*     */                   }
///* 238 */                   esResponse.addData(termsMap);
///*     */                 
///*     */                 });
///* 241 */           } else if (aggregation instanceof org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder) {
///* 242 */             ((List)aggregationMap.get(aggregation.getName())).forEach(m -> {
///* 243 */                   Map<Object, Object> dateMap = new HashMap<>();
///* 244 */                   dateMap.put("key", m.get("key"));
///* 245 */                   dateMap.put("keyAsString", m.get("keyAsString"));
///* 246 */                   dateMap.put("docCount", m.get("doc_count"));
///* 247 */                   esResponse.addData(dateMap);
///*     */                 
///*     */                 });
///* 250 */           } else if (aggregation instanceof org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder) {
///* 251 */             ((List)aggregationMap.get(aggregation.getName())).forEach(m -> {
///* 252 */                   Map<Object, Object> dateMap = new HashMap<>();
///* 253 */                   dateMap.put(aggregation.getName(), m.get("value"));
///* 254 */                   esResponse.addData(dateMap);
///*     */                 
///*     */                 });
///* 257 */           } else if (aggregation instanceof org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder) {
///*     */             
///* 259 */             ((List)aggregationMap.get(aggregation.getName())).forEach(m -> {
///* 260 */                   Map<Object, Object> dateMap = new HashMap<>();
///* 261 */                   dateMap.put(aggregation.getName(), m.get("value"));
///* 262 */                   esResponse.addData(dateMap);
///*     */                 });
///*     */           } 
///*     */         } else {
///*     */           
///* 267 */           logger.warn("aggregation failed in 2.x");
///*     */         } 
///*     */       } else {
///*     */         
///* 271 */         SearchResponse searchResponse = esClient.search(searchRequest, new org.apache.http.Header[0]);
///* 272 */         if (logger.isDebugEnabled()) {
///* 273 */           logger.debug("[searchWithAggregation] - searchRequest:{}, searchResponse:{}", searchRequest, searchResponse);
///*     */         }
///* 275 */         if (searchResponse.getAggregations() == null) {
///* 276 */           logger.debug("response has not aggs:{}. search:{}", searchResponse, searchRequest);
///* 277 */           return esResponse;
///*     */         } 
///* 279 */         Map<String, Aggregation> map = searchResponse.getAggregations().asMap();
///* 280 */         Iterator<Map.Entry<String, Aggregation>> iter = map.entrySet().iterator();
///* 281 */         while (iter.hasNext()) {
///* 282 */           Map.Entry<String, Aggregation> entry = iter.next();
///* 283 */           String aggName = entry.getKey();
///* 284 */           Aggregation agg = entry.getValue();
///* 285 */           if (agg instanceof Terms) {
///* 286 */             Map<Object, Object> termsMap = new HashMap<>();
///* 287 */             for (Terms.Bucket bucket : ((Terms)agg).getBuckets()) {
///* 288 */               if (bucket.getKey() == null)
///* 289 */                 continue;  termsMap.put(bucket.getKey().toString(), Long.valueOf(bucket.getDocCount()));
///*     */             } 
///* 291 */             esResponse.addData(termsMap); continue;
///* 292 */           }  if (agg instanceof ParsedDateHistogram) {
///* 293 */             for (Histogram.Bucket bucket : ((ParsedDateHistogram)agg).getBuckets()) {
///* 294 */               Map<Object, Object> dateMap = new HashMap<>();
///* 295 */               if (bucket.getKey() instanceof DateTime) {
///* 296 */                 dateMap.put("key", Long.valueOf(((DateTime)bucket.getKey()).getMillis()));
///*     */               }
///* 298 */               dateMap.put("keyAsString", bucket.getKeyAsString());
///* 299 */               dateMap.put("docCount", Long.valueOf(bucket.getDocCount()));
///* 300 */               esResponse.addData(dateMap);
///*     */             }  continue;
///*     */           } 
///* 303 */           if (agg instanceof ParsedSum) {
///* 304 */             Map<Object, Object> dateMap = new HashMap<>();
///* 305 */             dateMap.put(agg.getName(), Double.valueOf(((ParsedSum)agg).getValue()));
///* 306 */             esResponse.addData(dateMap); continue;
///*     */           } 
///* 308 */           if (agg instanceof ParsedValueCount) {
///* 309 */             Map<Object, Object> dateMap = new HashMap<>();
///* 310 */             dateMap.put(agg.getName(), Long.valueOf(((ParsedValueCount)agg).getValue()));
///* 311 */             esResponse.addData(dateMap);
///*     */           } 
///*     */         } 
///*     */       } 
///*     */ 
///*     */ 
///*     */ 
///*     */       
///* 319 */       return esResponse;
///*     */     }
///* 321 */     catch (Exception e) {
///* 322 */       logger.error("search event error can't get es client error detail: {}", e);
///*     */       
///* 324 */       return esResponse;
///*     */     } 
///*     */   }
///*     */   public static EsResponse searchByIds(Collection<String> eventIndexNames, String indexType, String[] id, Sort sort, Page page) {
///* 328 */     EsResponse esResponse = new EsResponse();
///* 329 */     if (id == null || id.length == 0) return esResponse; 
///*     */     try {
///* 331 */       String[] eventIndexNameArray = eventIndexNames.toArray(new String[eventIndexNames.size()]);
///* 332 */       SearchRequest searchRequest = new SearchRequest(eventIndexNameArray);
///* 333 */       searchRequest = (indexType != null) ? searchRequest.types(indexType.split(",")) : searchRequest;
///* 334 */       searchRequest.indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
///* 335 */       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
///* 336 */       searchSourceBuilder.query((QueryBuilder)QueryBuilders.idsQuery().addIds(id));
///* 337 */       if (sort != null) {
///* 338 */         searchSourceBuilder.sort(sort.getField(), SortOrder.fromString(sort.getOrder().toString()));
///*     */       }
///* 340 */       searchRequest.source(searchSourceBuilder);
///* 341 */       if (page != null) {
///* 342 */         searchSourceBuilder.from(page.getFrom());
///* 343 */         searchSourceBuilder.size(page.getSize());
///*     */       } else {
///* 345 */         searchSourceBuilder.size(1000);
///*     */       } 
///* 347 */       SearchResponse searchResponse = esClient.search(searchRequest, new org.apache.http.Header[0]);
///*     */       
///* 349 */       if (logger.isDebugEnabled()) {
///* 350 */         logger.debug("[searchByIds] - searchRequest:{}, searchResponse:{}", searchRequest, searchResponse);
///*     */       }
///* 352 */       esResponse.setTotal(searchResponse.getHits().getTotalHits());
///* 353 */       for (SearchHit it : searchResponse.getHits()) {
///* 354 */         esResponse.addData(it.getSource());
///*     */       }
///* 356 */       return esResponse;
///* 357 */     } catch (Exception e) {
///* 358 */       logger.error("search event error can't get es client error detail: {}", e);
///*     */       
///* 360 */       return esResponse;
///*     */     } 
///*     */   }
///*     */ 
///*     */ 
///*     */   
///* 366 */   public static EsResponse searchByIds(Collection<String> eventIndexNames, String indexType, String... id) { return searchByIds(eventIndexNames, indexType, id, null, null); }
///*     */ 
///*     */   
///*     */   public static EsResponse searchByIds(String index, String indexType, String... id) {
///* 370 */     List<String> indexList = new ArrayList<>(1);
///* 371 */     indexList.add(index);
///* 372 */     return searchByIds(indexList, indexType, id);
///*     */   }
///*     */ }
//
//
///* Location:              C:\Users\ZYJ\Desktop\tomcat\webapps\ice\WEB-INF\classes\!\com\hansight\ice\icewe\\util\es\EsSearchUtil.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.2
// */