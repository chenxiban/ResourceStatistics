package com.cyj.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * JFinal拦截器
 * @author 佳佳
 *
 */
public class MyInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {		
		
		/*try {*/
			System.out.println("FriendlyInterceptor intercept 拦截之前" + inv.getActionKey());
			System.out.println("是否拦截的控制器 => "+inv.isActionInvocation());
			System.out.println("getController=> "+inv.getController());
			System.out.println("getActionKey=> "+inv.getActionKey());
			System.out.println("getControllerKey=> "+inv.getControllerKey());
			System.out.println("getViewPath=> "+inv.getViewPath());
			System.out.println("getMethod=> "+inv.getMethod());
			System.out.println("getMethodName=> "+inv.getMethodName());
			System.out.println("getArgs=> "+inv.getArgs());// token值
			System.out.println("getArgs=> "+Arrays.asList(inv.getArgs()));
			
			inv.invoke();//被拦截方法的代理
			/**
			 * 以下两行完美解决跨域问题
			 * 在apiController中加上。下面代码，也可以作为全局的，也可以作为路由级别，也可以用在单类上，非常灵活方便使用
			 * 在控制器上架@before(MyInterceptor.class)
			 */
			HttpServletResponse response=inv.getController().getResponse();
			response.addHeader("Access-Control-Allow-Origin", "*");
			
			System.out.println("getReturnValue=> "+inv.getReturnValue());
			inv.setReturnValue("我是返回值");
			System.out.println("getReturnValue=> "+inv.getReturnValue());		
			System.out.println("FriendlyInterceptor intercept 拦截之后" + inv.getActionKey());
			
		/*} catch (Exception e) {
			System.err.println("e=>"+e);
			inv.getController().setSessionAttr("error", "服务器异常,请联系管理员那家伙来修理!!!");
			inv.getController().redirect("/index.jsp");
		}finally{
			System.out.println("finally=>");
		}*/
		
		
		
	}

}
