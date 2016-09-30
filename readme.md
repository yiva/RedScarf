简要说明
---
 总体框架  
 - **包**
   - activity 项目界面文件，所有的activity和fragment都在这里面。
   `BaseActivity`和`BaseFragment`为其基类
   - adapter容量适配器，放置listview等容量的item构造文件。之后的版本我使用了`recyclerview`代替了之前的listivew和gridview，部分之前写的还没有改完，后面写的话建议使用`recyclerview`
   `BaseRecyclerAdapter`和`BaseRedScarfAdapter`为其基类
   - customwidget自定义组件
   - listener监听接口
   - network网络请求，所有url的访问都在这里
   - pojo实体类
   - util工具类（第三方，图片，JSON解析等）
 - **网络访问**  
 网络访问框架使用的是volley，url请求分别写在`BaseActivity`和`BaseFragment`，需要指明handle以及错误代码（自己定义）

