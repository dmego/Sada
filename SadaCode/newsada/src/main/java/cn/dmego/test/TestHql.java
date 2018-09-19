package cn.dmego.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;

import cn.dmego.dao.ICommonDao;
import cn.dmego.dao.IDictDao;
import cn.dmego.dao.IUserDao;
import cn.dmego.domain.Dict;
import cn.dmego.domain.User;
import cn.dmego.service.IUserService;

public class TestHql {

	@Test 
	public void TestHql1(){
		//DictionaryDaoImpl dictionaryDao = new DictionaryDaoImpl();
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		IDictDao dictionaryDao = (IDictDao) ac.getBean(IDictDao.DAO_NAME);
		//List<Dict> list = dictionaryDao.findDictionaryListByDistinct();
//		/System.out.println(list.toString());
		IUserService userervice = (IUserService) ac.getBean(IUserService.SERVICE_NAME);
		User user = (User) userervice.get(User.class, "11");
		//List<User> list = userDao.list(User.class);
		//System.out.println(list.get(0));
		System.out.println(user.getBirthday());
//		User user2 = new User();
//		user2 = user;
//		String jsonString = JSON.toJSONString(user2);
//		System.out.println(jsonString);
	}
}
