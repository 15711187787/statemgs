package cn.com.hangdun.Controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.hangdun.pojo.asset.Asset;
import cn.com.hangdun.pojo.hansight.EsUser;
import cn.com.hangdun.pojo.hansight.EsdataTemp;
import cn.com.hangdun.pojo.hansight.MyProps;
import cn.com.hangdun.pojo.hansight.SystemUser;
import cn.com.hangdun.pojo.mgs.Mgs;
import cn.com.hangdun.pojo.mgs.Mytask;
import cn.com.hangdun.service.hansight.EsUserService;
import cn.com.hangdun.service.hansight.EsdataService;
import cn.com.hangdun.service.hansight.EsdataTempService;
import cn.com.hangdun.service.hansight.SystemUserService;



/**
 * @ author  :ZYJ
 * @ 创建时间:2020年8月26日上午11:20:39
 * @ 修改备注:
 */
@Controller
@RequestMapping("/")
public class CommonCtrl {
//		private static Logger logger=Logger.getLogger(CommonCtrl.class);
		@Autowired
		private SystemUserService systemUser;
		
		@Autowired
		private EsdataTempService esdataTempService;
		
		@Autowired
		private EsUserService esUserService;
		@Autowired
		private EsdataService esdataService;
		
		@Autowired
		public MyProps myProps;
		
		@Autowired
		public CronJob cronJob;
		
		@RequestMapping("/")
		public String login(HttpServletRequest request, HttpServletResponse response) {
			return "login";
		}
		@GetMapping("/login")
		public String loginTologin(HttpServletRequest request, HttpServletResponse response) {
			return "login";
		}
		/***
		 * 
		 * @param request
		 * @param response
		 * @param sessionID
		 * @param userID
		 * @param verifySSO
		 * @param PID
		 * @param WSUrl
		 * @return
		 * 
		   *   智慧企业访问接口
		 * 
		 */
		@ResponseBody
		@RequestMapping("sendMyStateMgs")
		public String getMgs(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(value = "sessionID") String sessionID,
				@RequestParam(value = "userID") String userID,
				@RequestParam(value = "verifySSO") String verifySSO,
				@RequestParam(value = "PID") String PID,
				@RequestParam(value = "WSUrl") 
				String WSUrl) {
//			System.out.println("sessionID"+sessionID+"userID"+userID+"PID"+PID+"verifySSO"+verifySSO+"WSUrl"+WSUrl);
			try {
				SystemUser byId = systemUser.getById(PID);
				//同步ES数据
//				syncEsMethod();
				if(byId!=null) {
						Mytask mytask = new Mytask();
//						List<EsdataTemp> esdataTemp= esdataTempService.getOne();
						List<EsdataTemp> esdataTemp=esdataTempService.getTodoMsg();
						ArrayList<Mgs> mgslist = new ArrayList<Mgs>();
						Mgs mgs=new Mgs();
						mgs.setSentTime(esdataTemp.get(0).getInserttime());
						mgs.setTitle("态势感知已捕获到"+esdataTemp.size()+"条告警信息");
						mgs.setMgsMessage("待处置");
						mgs.setMgsUrgent("一般");
						mgs.setUrl(myProps.getUrl());
						mgslist.add(mgs);
						/*for (EsdataTemp es : esdataTemp) {
							Mgs mgs = new Mgs();
							String inserttime = es.getInserttime();
							String level = es.getLevel();
							mgs.setSentTime(inserttime);
							mgs.setTitle(es.getName());
							if("0".equals(es.getStatus()))
								mgs.setMgsMessage("处置中");
							if("1".equals(es.getStatus()))
								mgs.setMgsMessage("待处置");
							if("2".equals(es.getStatus()))
								mgs.setMgsMessage("处置完成");
							if("3".equals(es.getStatus()))
								mgs.setMgsMessage("忽略");
							if("1".equals(level))
								mgs.setMgsUrgent("低");
							if("2".equals(level))
								mgs.setMgsUrgent("中");
							if("3".equals(level))
								mgs.setMgsUrgent("高");
							mgs.setUrl(myProps.getUrl());
							mgslist.add(mgs);
						}*/
						mytask.setMgs(mgslist);
						String str = new String(mytask.toString());
						System.out.println("智企待办数据返回成功！！");
						return str;
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("未找到该用户--参数：sessionID="+sessionID+"--userID="+userID+"--verifySSO="+verifySSO+"--PID="+PID+"--WSUrl="+WSUrl);
		return "未找到该用户";
		}
		
 		/***
		 * 
		 * @param request
		 * @param response
		 * @param PID
		 * @return 登陆系统
		 */
		@RequestMapping("loginEntSystem")
		public  String login(HttpServletRequest request, HttpServletResponse response,@RequestParam String PID) {
			try {
				SystemUser systemUser2 = getSystemUser(PID);
				if(systemUser2==null) {
					request.setAttribute("msg", "没有此用户！");
					request.getRequestDispatcher("/login").forward(request, response);
				}
				request.setAttribute("username", "zhangyongjian");
				request.setAttribute("password", "123qwe!@#QWE");
				response.setContentType("text/html");
				return "login";
			} catch (ServletException e) {
				// TODO Auto-generated catch  block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "login"; 
		}
		/***
		 * 
		 * 系统退出
		 * 
		 */
		@RequestMapping("/logout")
		public String logout(HttpServletRequest request, HttpServletResponse response) {
			request.getSession().removeAttribute("es_user");
			return "login";
		}
		/***
		 * 校验数据库用户是否存在
		 * true 返回用户信息
		 * false 返回null
		 * parame pid
		 */
		public SystemUser getSystemUser(String pid ) {
			SystemUser user = new SystemUser();
			if(pid.isEmpty()) {
				return user;
			}
			user= systemUser.getById(pid);
			return user;
		}
		
		@RequestMapping("/getEsdatatoday")
		public String  getEsdata(HttpServletRequest request, HttpServletResponse response) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			ArrayList<EsdataTemp> es = esdataTempService.esYesterday(date);
			request.setAttribute("list", es);
			return "esfile/list";
		}
		
		
		/***
		 * 
		 * login
		 */
		@RequestMapping("/login")
		public String loginSystem(HttpServletRequest request, HttpServletResponse response ) {
			String logname = request.getParameter("logname");
			String password = request.getParameter("password");
			if(logname==null || logname =="") {
				return "/login";
			}
			ArrayList<EsUser> esUser = esUserService.getUserByUsername(logname);
			if(esUser.isEmpty()) {
				request.setAttribute("msg", "用户不存在");
				System.out.println("不存在");
				return "/login";
			}
			String password2 = esUser.get(0).getPassword();
			if(!password2.equals(password)) {
				request.setAttribute("msg", "用户名或密码不正确");
				request.setAttribute("retuen_logname",logname);
				System.out.println("不正确");
				return "/login";
			} 
//			esUser.get(0).setPassword("");
			HttpSession session = request.getSession();
			session.setAttribute("es_user", esUser.get(0));
			
			return "index";
			
		}
		/**
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/main")
		public String  getEsdataAll(HttpServletRequest request, HttpServletResponse response ){
//			request.getSession().removeAttribute("es_user");
			ArrayList<EsdataTemp> es = esdataTempService.esdataAll();
			request.setAttribute("list", es);
			ArrayList<HashMap>  map = esdataTempService.esDataEcharts();
			String json = JSON.toJSON(map).toString();
			request.setAttribute("map", json);
			return "esfile/list";
		}
		@ResponseBody
		@RequestMapping("/getEsdatatodayAjax")
		public EsdataTemp  getEsdataAjax(HttpServletRequest request, HttpServletResponse response) {
			String numberid = request.getParameter("numberid");
			EsdataTemp es = esdataTempService.esYesterdayOne(numberid);
			return es;
		}
		/***
		 * echarts返回折线图数据
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 */
		@ResponseBody
		@RequestMapping("/getEsdataEchars")
		public ArrayList<HashMap> getEsdataEchars(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//获取每天的数据总量
			ArrayList<HashMap>  map = esdataTempService.esDataEcharts();
			//获取四个告警级别每天的总量
//			esdataTempService.esdataLevelSum();
			
			request.setAttribute("map", map);
			return map;
		}
		/***
		 * 	echarts返回柱状图数据
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 */
		@ResponseBody
		@RequestMapping("/getesLevelSum")
		public ArrayList<HashMap> getesdataLevelSum(HttpServletRequest request, HttpServletResponse response) throws IOException {
			//获取每天的数据总量
			ArrayList<HashMap>  map = esdataTempService.getesLevelSum();
			//获取四个告警级别每天的总量
			return map;
		}
		
	
		/***
		 *  
		 *  智企跳转免密登录
		 * @throws ServiceException 
		 * @throws RemoteException 
		 */ 
		@RequestMapping("loginJump")
		public String   loginJump(HttpServletRequest request, HttpServletResponse response) {
			String userID = request.getParameter("userID");
			String userName = request.getParameter("userName");
			String sessionID = request.getParameter("sessionID");
			String WSUrl = request.getParameter("WSUrl");
			String PID = request.getParameter("PID");
			String verifySSO = request.getParameter("verifySSO");
			ArrayList<EsUser> esUser = esUserService.getUserByPid(PID);

			String requestStrings = "userID:" + userID + " userName:" + userName + " sessionID:" + sessionID + " WSUrl:"
					+ WSUrl + " PID:" + PID + " verifySSO:" + verifySSO;
			System.out.println(requestStrings);

			String projectDetails = "<root><data><userID>" + userID + "</userID><PID>" + PID + "</PID><sessionID>"
					+ sessionID + "</sessionID><verifySSO>" + verifySSO + "</verifySSO></data></root>";

			System.out.println(projectDetails);
			// 进行校验		
			String backXml = "";
			Service service = new Service();
			Call call = null;
			try {
				call = (Call) service.createCall();
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			call.setTargetEndpointAddress(WSUrl);
			call.setOperationName("runBiz");// 设置操作名

//			 设置入口参数 
			call.addParameter("packageName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("unitId", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("processName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("bizDataXML", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
//			 调用门户系统WebService服务，返回结果 
			String[] param = { "common", "0", "biz.bizCheckSSO", projectDetails };
			Object obj = null;
			try {
				obj = call.invoke(param);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			// 返回格式<root><data><msg>1</msg></data></root>
//			String obj ="<root><data><msg>1</msg></data></root>";
			backXml = obj.toString();
			System.out.println(obj.toString());
			if (!"".equals(backXml) && null != backXml) {
				// 解析xml
				try {
					Document document = DocumentHelper.parseText(backXml);
					String domNode = document.getRootElement().element("data").elementText("msg");
					if ("1".equals(domNode)) {
						if(esUser.isEmpty()) {
							request.setAttribute("msg", "用户不存在");
							System.out.println("不存在");
							return "login";
						}else {
							request.setAttribute("username",esUser.get(0).getUsername());
							request.setAttribute("password",esUser.get(0).getPassword());
							HttpSession session = request.getSession();
							session.setAttribute("es_user", esUser.get(0));
							session.setMaxInactiveInterval(60*30);
							Cookie cookie = new Cookie("loginName", esUser.get(0).getUsername());
							 cookie.setMaxAge(60 * 60 * 24 * 7);
							 cookie.setPath("/");
							 cookie.setDomain("localhost");
							 response.addCookie(cookie);
							 System.out.println("session:"+session.getAttribute("es_user"));
							 Cookie[] cookies = request.getCookies();
							 if (cookies != null) {
							    for (Cookie cookie2 : cookies) {
							        // 若登录，放行
							        if ("loginName".equals(cookie2.getName())) {
							            System.out.println("Cookie："+cookie2.getName());
							        }
							    }
							 }
							return "index";
						}
					} else {
						System.out.println("登陆口令未校验成功。");
					}
					System.out.println(domNode);
				} catch (DocumentException e) {
					System.out.println("解析xml异常");
					e.printStackTrace();
					return "login";
				}
			} else {
				System.out.println("连接智慧平台:  未返回数据。");
				return "login";
			}
			return "login";
		}
		@RequestMapping("loginJumpToDo")
		public String loginForJump(HttpServletRequest request, HttpServletResponse response) {
			String PID ="";
			PID = request.getParameter("PID");
			if(!"".equals(PID) && null != PID) {
				ArrayList<EsUser> esUser = esUserService.getUserByPid(PID);
				HttpSession session = request.getSession();
				session.setAttribute("es_user", esUser.get(0));
				session.setMaxInactiveInterval(60*30);
				return "index";
			}
			return "login";
		}
		
		/**
		 * 智企登录接口
		 */
		@RequestMapping("/linkLogin")
		public String service(HttpServletRequest request,ModelMap modelMap) {
			String userID = request.getParameter("userID");
			String userName = request.getParameter("userName");
			String sessionID = request.getParameter("sessionID");
			String WSUrl = request.getParameter("WSUrl");
			String PID = request.getParameter("PID");
			System.out.println(PID);
			String verifySSO = request.getParameter("verifySSO");
			Service service = new Service();
			Call call = null;
			try {
				call = (Call) service.createCall();
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			call.setTargetEndpointAddress(WSUrl);
			call.setOperationName("runBiz");// 设置操作名

//			 设置入口参数
			call.addParameter("packageName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("unitId", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("processName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("bizDataXML", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);

			//判断pid是否存在
			if (!"".equals(PID) && PID != null) {
//				EsUser esUser=esUserService.findUserByPid(PID);
				ArrayList<EsUser> esUser = esUserService.getUserByPid(PID);

				//判断用户是否存在
				if (esUser.size()>0){
					modelMap.put("username", esUser.get(0).getUsername());
					modelMap.put("password", esUser.get(0).getPassword());
					return "zqlogin";
				}else {
					modelMap.put("error","未找到用户:\" + PID");
					return "login";
				}
			} else {
				modelMap.put("error","未找到用户:\" + PID");
				return "login";
			}
		}
		@ResponseBody
		@RequestMapping("syncEsDatabase")
		public String syncEsDatabase(HttpServletRequest request,ModelMap modelMap) {
			cronJob.sendEsData();
			return "index";
		}
		/***
		 * 
		 * 单点登录
		 */
//		@ResponseBody
		@RequestMapping("ssaLogin")
		public String ssaLogin(HttpServletRequest request,ModelMap modelMap) {
			System.out.println("进来了");
			return "esfile/ssalogin";
		}
		/***
		 * 资产列表
		 * @param request
		 * @param modelMap
		 * @return
		 */
//		@ResponseBody
		@RequestMapping("/asset")
		public String asset(HttpServletRequest request,ModelMap modelMap) {
			
			System.out.println("进来了");
			return "asset/list";
		}
		/***
		 * 添加资产
		 * @param request
		 * @param modelMap
		 * @return
		 */
		@RequestMapping("/asset_add")
		public String asset_add(HttpServletRequest request,ModelMap modelMap) {
			System.out.println("进来了");
			return "asset/add";
		}
		@RequestMapping("/asset_save")
		public void asset_save(HttpServletRequest request,HttpServletResponse response,Asset asset) throws IOException {
			System.out.println("save进来了"+asset);
			response.sendRedirect("asset");
		}
		
		
		/**
		 * 资产新增
		 */
		@RequestMapping("/addZc")
		@ResponseBody
		public String addZc(HttpServletRequest request,ModelMap modelMap) throws IOException {
		   //创建一个client
		   TransportClient client =  esdataService.transportClient(myProps.getClusterName(),myProps.getSnf(),myProps.getPoolSize(),myProps.getClusterPort(),myProps.getClusterNode());
		       //存入字段
		   XContentBuilder doc = XContentFactory.jsonBuilder()
		         .startObject()
		         .field("asset_name","1")
		         .field("ip_address","1.1.1.1")
		         .field("asset_type_name","测试")
		         .endObject();
		   //添加一个doc
		   IndexResponse response = client.prepareIndex("asset_confirmed","asset",null)//id为null，由ES自己生成
		         .setSource(doc).get();
		   System.out.println(response.status());//打印添加是否成功
		   client.close();
		   return "esfile/addGd";
		}
}