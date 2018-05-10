package com.zkxltech.ui;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ejet.cache.BrowserManager;
import com.ejet.cache.TeachingCache;
import com.ejet.core.util.StringUtils;
import com.ejet.core.util.constant.Constant;
import com.ejet.netty.message.LiveNettyClientInfo;
import com.ejet.netty.server.LiveServerHelper;
import com.zkxltech.config.ConfigConstant;
import com.zkxltech.config.Global;
import com.zkxltech.domain.Result;
import com.zkxltech.domain.StudentInfo;
import com.zkxltech.teaching.AnswerVO;
import com.zkxltech.teaching.CommunicationRequest;
import com.zkxltech.teaching.msg.AnswerRequest;
import com.zkxltech.teaching.service.StudentInfoService;
import com.zkxltech.teaching.service.impl.StudentInfoServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/*
 * 	页面回调指令
 *  get_project_type 获取软件类型（是否为老师端）
 * 	start_answer 开始作答
 * 	get_answer_data 从缓存中获取答题数据
 * 	clear_answerMap 清除答题缓存
 * 	get_ranking  从缓存中获取答题名次
 * 	stop_answer	停止作答
 * 	close_shell	关闭窗口
 * 	to_import	跳转导入页面
 * 	get_student_info	从数据库中获取学生信息
 * 	save_nobing_student	保存未绑定的学生信息
 * 	start_import	开始导入
 * 	start_bind	开始绑定
 * 	stop_bind	停止绑定
 * 	to_returnPre	返回统计页面
 * 	get_online_info	获取已经连接客户端信息
 * 	select_answer 同步选择答案
 */
public class MainPage {

	protected Object result;
	protected Shell shell;
	private MainStart mainStart;
	private int shellMaxWidth;/* 窗口最大宽度 */
	private int shellMaxHeight;/* 窗口最大高度 */
	private int shellX;/* 窗口x坐标 */
	private int shellY;/* 窗口y坐标 */
	private StudentInfoService studentInfoService;
	private Browser browser;
	
	private boolean isTest;

	public MainPage(Shell parent) {
		shell = parent;
	}

	public void setMainStart(MainStart mainStart) {
		this.mainStart = mainStart;
	}
	
	/* 初始化配置 */
	private void init() {
		shellMaxWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2.4);
		shellMaxHeight = shellMaxWidth * 560 / 800;
		shellX = 0;
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
		shellY = maximumWindowBounds.height - shellMaxHeight - 6;

		studentInfoService = new StudentInfoServiceImpl();
		isTest = Boolean.parseBoolean(ConfigConstant.projectConf.getApp_test());
	}

	public void closeShell() {
		shell.setVisible(false);
		mainStart.changeImage();
		mainStart.frame.repaint();
	}

	public void showShell() {
		shell.setVisible(true);
	}

	public Shell open() {
		try {
			init();
			createContents();
			shell.layout();
			Display display = shell.getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
		}

		return shell;
	}

	private void createContents() {
		shell = new Shell(shell, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setVisible(false);
		shell.setSize(shellMaxWidth, shellMaxHeight);
		shell.setLocation(shellX, shellY);
		shell.setLayout(new FormLayout());
		shell.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				// 屏蔽按下Esc按键
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}
			}
		});

		String flieUrl = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/" + "htmls/showAnswer.html";

		// //FIXME /*测试部分*/
		// CLabel refresh = new CLabel(shell, SWT.NONE);
		// FormData fd_refresh = new FormData();
		// fd_refresh.left = new FormAttachment(50);
		// fd_refresh.top = new FormAttachment(50);
		// refresh.setLayoutData(fd_refresh);
		// refresh.setText("刷新");

		browser = new Browser(shell, SWT.NONE);
		FormData fd_browser = new FormData();
		fd_browser.left = new FormAttachment(0);
		fd_browser.top = new FormAttachment(0);
		fd_browser.width = shellMaxWidth;
		fd_browser.height = shellMaxHeight;
		browser.setLayoutData(fd_browser);
		browser.setUrl(flieUrl);
		/* 创建java调用页面的方法 */
		BrowserManager.setBrower(browser);
		BrowserManager.setShell(shell);
		setFunction(browser);
		// //FIXME /*测试部分*/
		// refresh.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseDown(MouseEvent e) {
		//// browser.evaluate("document.getElementById('toImport').click();");
		// browser.refresh();
		// }
		// });
	}

	public void setFunction(Browser browser) {
		/****
		 * 
		 * 统计页面相关接口
		 * 
		 */
		// 获取软件类型（是否为老师端）
		new BrowserFunction(browser, "get_project_type") {
			@Override
			public Object function(Object[] params) {

				// //FIXME 测试自动切换页面
				// List<String> strings = new ArrayList<String>();
				// strings.add("1");
				// strings.add("2");
				// strings.add("3");
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// while (true) {
				//
				// int i = (int) (Math.random()*(2-0));
				// BrowserManager.selectAnswerType(strings.get(i));
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				// }
				// }).start();
				return Global.isTeacher();
			}
		};

		// 开始作答
		new BrowserFunction(browser, "start_answer") {
			@Override
			public Object function(Object[] params) {
				if (params.length > 0) {
					// 清除答题缓存
					TeachingCache.cleanAnswerCache();
					String answerType = (String) params[0]; // 答题类型
					// 发送开始答题指令
					AnswerRequest req = new AnswerRequest();
					req.setBusinessQusetionType(StringUtils.changeBusinessQusetionTypeToNum(answerType));
					req.setQuestionType(StringUtils.changeQusetionTypeToNum(answerType));
					new Thread(new Runnable() {
						@Override
						public void run() {
							CommunicationRequest.startAnswer(req);
						}
					}).start();
				}

				// //FIXME 测试 获取每个答案的选择情况
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// List<String> answerInfos = new ArrayList<String>();
				//
				// answerInfos.add("张三");
				// while (true) {
				// TeachingCache.answerMap.put("A", answerInfos);
				// answerInfos.add("张三");
				// TeachingCache.answerMap.put("B", answerInfos);
				// TeachingCache.answerMap.put("C", answerInfos);
				// try {
				// Thread.sleep(2000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				// }
				// }).start();
				return null;
			}
		};

		// 从缓存中获取答题数据
		new BrowserFunction(browser, "get_answer_data") {
			@Override
			public Object function(Object[] params) {
				if (isTest) {
					// FIXME 测试
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("A", 20);
					jsonObject.put("B", 20);
					jsonObject.put("C", 20);
					jsonObject.put("D", 10);
					jsonObject.put("max", 50);
					return jsonObject.toString();
				} else {
					if (params.length > 0) {
						JSONObject jsonObject = new JSONObject();
						Map<String, Object> map = (Map<String, Object>) TeachingCache.getAnswerMap()
								.get(StringUtils.changeQusetionTypeToNum((String) params[0]));
						if (map == null) {
							map = new HashMap<String, Object>();
						}
						for (String answer : map.keySet()) {
							Object obj = map.get(answer);
							if (obj instanceof List) {
								jsonObject.put(answer, ((List) obj).size());
							}
						}
						jsonObject.put("max", TeachingCache.getAnswerNum()); // 最大人数
						return jsonObject.toString();
					}
					return null;
				}
			}
		};

		// 清除答题缓存
		new BrowserFunction(browser, "clear_answerMap") {
			@Override
			public Object function(Object[] params) {
				TeachingCache.cleanAnswerCache();
				return null;
			}
		};

		// 从缓存中获取答题名次
		new BrowserFunction(browser, "get_ranking") {
			@Override
			public Object function(Object[] params) {
				if (params.length > 0) {
					JSONArray jsonArray = JSONArray.fromObject(params[0]);
					if (isTest) {
						List<String> names = new ArrayList<String>();
						names.add("张三三年四班");
						names.add("李四一年三班");
						names.add("张琪二年二班");
						return JSONArray.fromObject(names).toString();
					} else {
						List<String> names = new ArrayList<String>();
						Map<String, Object> answerFastList = (Map<String, Object>) TeachingCache.getAnswerMap()
								.get(StringUtils.changeQusetionTypeToNum(jsonArray.get(0).toString()));
						List<AnswerVO> answerResponses = (List<AnswerVO>) answerFastList
								.get(StringUtils.changeAnswer(jsonArray.get(1).toString()));
						if (answerResponses == null) {
							answerResponses = new ArrayList<AnswerVO>();
						}
						Collections.sort(answerResponses, new Comparator<AnswerVO>() {
							@Override
							public int compare(AnswerVO o1, AnswerVO o2) {
								if (o1.getAnswerDateTime().compareTo(o2.getAnswerDateTime()) > 0) {
									return 1;
								} else if (o1.getAnswerDateTime().compareTo(o2.getAnswerDateTime()) < 0) {
									return -1;
								}
								return 0;
							}
						});
						for (int i = 0; i < answerResponses.size() && i < 3; i++) {
							names.add(answerResponses.get(i).getStudentName() + answerResponses.get(i).getClassName());
						}
						return JSONArray.fromObject(names).toString();
					}
				} else { // 未选择正确答案，从答题最快的缓存中获取
					if (isTest) {
						List<String> list = new ArrayList<String>();
						list.add("王红亮三年四班");
						list.add("李潇潇一年三班");
						list.add("张思琪二年二班");
						return JSONArray.fromObject(list).toString();
					} else {
						List<AnswerVO> answerFastList = TeachingCache.getFastMap();
						List<String> list = new ArrayList<String>();
						for (int i = 0; i < answerFastList.size(); i++) {
							AnswerVO answerVO = answerFastList.get(i);
							list.add(answerVO.getStudentName() + answerVO.getClassName());
						}
						return JSONArray.fromObject(list).toString();
					}

				}

			}
		};

		// 停止作答
		new BrowserFunction(browser, "stop_answer") {
			@Override
			public Object function(Object[] params) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						CommunicationRequest.stopAnswer();
					}
				}).start();

//				 //FIXME 测试调用硬件返回结果并在页面提示
//				 new Thread(new Runnable() {
//				 @Override
//				 public void run() {
//					 BrowserManager.chartClick("选B");
//				 }
//				 }).start();
				return null;
			}
		};
		
		// 同步选择答案
		new BrowserFunction(browser, "select_answer") {
			@Override
			public Object function(Object[] params) {
				if (params.length>0) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							CommunicationRequest.selectAnswer((String)params[0]);
						}
					}).start();
				}
				return null;
			}
		};
		
		
		// 关闭窗口
		new BrowserFunction(browser, "close_shell") {
			@Override
			public Object function(Object[] params) {
				MainStart.isShow = false;
				closeShell();
				return null;
			}
		};

		/****
		 * 
		 * 导入页面相关接口
		 * 
		 */

		// 跳转导入页面
		new BrowserFunction(browser, "to_import") {
			@Override
			public Object function(Object[] params) {
				String flieUrl = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/" + "htmls/import.html";
				browser.setUrl(flieUrl);
				return null;
			}
		};

		// 从数据库中获取学生信息
		new BrowserFunction(browser, "get_student_info") {
			@Override
			public Object function(Object[] params) {
				StudentInfo studentInfo = new StudentInfo();
				Result result = studentInfoService.selectStudentInfo(studentInfo);
				return JSONObject.fromObject(result).toString();
			}
		};

		// 保存未绑定的学生信息
		new BrowserFunction(browser, "save_nobing_student") {
			@Override
			public Object function(Object[] params) {
				if (params.length > 0) {
					JSONArray jsonArray = JSONArray.fromObject(params[0]);
					StudentInfo[] sInfos = (StudentInfo[]) JSONArray.toArray(jsonArray, StudentInfo.class);
					for (int i = 0; i < sInfos.length; i++) {
						if (!"1".equals(sInfos[i].getStatus())) {
							TeachingCache.addNoBindStudent(sInfos[i]);
						}
					}
				}
				return null;
			}
		};

		// 开始导入
		new BrowserFunction(browser, "start_import") {
			@Override
			public Object function(Object[] params) {
				if (params.length > 0) {
					String fileName = (String) params[0];
					new Thread(new Runnable() {
						@Override
						public void run() {
//							BrowserManager.showLoading();
							Result result = studentInfoService.importStudentInfo(fileName);
//							BrowserManager.removeLoading();
							if (Constant.SUCCESS.equals(result.getRet())) {
								// 清除绑定
								TeachingCache.cleanBindCache();
								BrowserManager.retResult("导入成功!");
								BrowserManager.refreshStudent();
							} else {
								BrowserManager.retResult("导入失败!");
								BrowserManager.refreshStudent();
							}
						}
					}).start();

				}
				return null;
			}
		};

		// 开始绑定
		new BrowserFunction(browser, "start_bind") {
			@Override
			public Object function(Object[] params) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						CommunicationRequest.startBind();
					}
				}).start();

				// FIXME 测试部分模拟接收当前绑定信息
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// while (true) {
				// EchoRequest echoRequest = new EchoRequest();
				// echoRequest.setId("104201");
				// BrowserManager.bindCard(echoRequest);
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				// }
				// }).start();
				return null;
			}
		};

		// 停止绑定
		new BrowserFunction(browser, "stop_bind") {
			@Override
			public Object function(Object[] params) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						CommunicationRequest.stopBind();
					}
				}).start();
				return null;
			}
		};

		// 返回统计页面
		new BrowserFunction(browser, "to_returnPre") {
			@Override
			public Object function(Object[] params) {
				String flieUrl = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/" + "htmls/showAnswer.html";
				browser.setUrl(flieUrl);
				return null;
			}
		};
		
		// 获取已经连接客户端信息
		new BrowserFunction(browser, "get_online_info") {
			@Override
			public Object function(Object[] params) {
				if (isTest) {
					LiveNettyClientInfo liveNettyClientInfo1 = new LiveNettyClientInfo();
					liveNettyClientInfo1.setClassName("二年一班");
					liveNettyClientInfo1.setHostName("192.168.10.182");
					liveNettyClientInfo1.setIp("192.168.10.182");
					liveNettyClientInfo1.setPort(8080);
					liveNettyClientInfo1.setState(1);
					LiveNettyClientInfo liveNettyClientInfo2 = new LiveNettyClientInfo();
					liveNettyClientInfo2.setClassName("二年一班");
					liveNettyClientInfo2.setHostName("192.168.10.182");
					liveNettyClientInfo2.setIp("192.168.10.181");
					liveNettyClientInfo2.setPort(8080);
					liveNettyClientInfo2.setState(0);
					List<LiveNettyClientInfo> liveNettyClientInfos = LiveServerHelper.getClients();			
					liveNettyClientInfos.add(liveNettyClientInfo1);
					liveNettyClientInfos.add(liveNettyClientInfo2);
					return JSONArray.fromObject(liveNettyClientInfos).toString();
				}else {
					List<LiveNettyClientInfo> liveNettyClientInfos = LiveServerHelper.getClients();
					return JSONArray.fromObject(liveNettyClientInfos).toString();
				}
			}
		};
	}


}
