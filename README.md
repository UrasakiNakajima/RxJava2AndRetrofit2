
# 应用截图
<table>
    <tr>
        <td><img src="/screenshot/screenshot_launch.jpg"></td>
        <td><img src="/screenshot/screenshot_login.jpg"></td>
        <td><img src="/screenshot/screenshot_register.jpg"></td>
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshot/screenshot_home.jpg"></td>
        <td><img src="/screenshot/screenshot_project.jpg"></td>
        <td><img src="/screenshot/screenshot_square.jpg"></td>
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshot/screenshot_android_and_js.jpg"></td>
        <td><img src="/screenshot/screenshot_edit_text_input_limits.jpg"></td>
        <td><img src="/screenshot/screenshot_edit_text_input_limits2.jpg"></td>
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshot/screenshot_bridge.jpg"></td>
        <td><img src="/screenshot/screenshot_picker_view.jpg"></td>
        <td><img src="/screenshot/screenshot_kotlin_coroutine.jpg"></td>
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshot/screenshot_base64_and_file.jpg"></td>
        <td><img src="/screenshot/screenshot_resource.jpg"></td>
        <td><img src="/screenshot/screenshot_mine.jpg"></td>
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshot/screenshot_thread_pool.jpg"></td>
    </tr>
</table>



# RxJava2AndRetrofit2
项目功能介绍：原本是RxJava2和Retrofit2项目，现已更新使用Kotlin+RxJava2+Retrofit2+MVP架构+组件化和
Kotlin+Retrofit2+协程+MVVM架构+组件化，添加自动管理token 功能，添加RxJava2 生命周期管理，集成极光推送、阿里云Oss对象存储和高德地图定位功能。


## 我们先来说一下组件化，组件化图解说明
<table>
    <tr>
        <td><img src="/screenshot/screenshot_component_description.jpg"></td>
    </tr>
</table>

### 组件化架构的优势
基于以上这些问题，现在的组件化架构希望可以解决这些问题提升整个交付效率和交付质量。
组件化架构通常具备以下优点：
* 代码复用-功能封装成组件更容易复用到不同的项目中，直接复用可以提高开发效率。并且每个组件职责单一使用时会带入最小的依赖。
* 降低理解复杂度-工程拆分为小组件以后，对于组件使用方我们只需要通过组件对外暴露的公开API去使用组件的功能，不需要理解它内部的具体实现。
这样可以帮助我们更容易理解整个大的项目工程。
* 更好的解耦-在传统单一工程项目中，虽然我们可以使用设计模式或者编码规范来约束模块间的依赖关系，但是由于都存放在单一工程目录中缺少清晰
的模块边界依然无法避免不健康的依赖关系。组件化以后可以明确定义需要对外暴露的能力， 对于模块间的依赖关系我们可以进行强约束限制依赖，更好
的做到解耦。对一个模块的添加和移除都会更容易，并且模块间的依赖关系更加清晰。
* 隔离技术栈-不同的组件可以使用不同的编程语言/技术栈，并且不用担心会影响到其他组件或主工程。例如在不同的组件内可以自由选择使用Kotlin或Swift，
可以使用不同的跨平台框架，只需要通过规范的方式暴露出页面路由或者服务方法即可。
* 独立开发/维护/发布-大型项目通常有很多团队。在传统单一项目集成打包时可能会遇到代码提交/分支合并的冲突问题。组件化以后每个团队负责自己的组件，
组件可以独立开发/维护/发布提升开发效率。
* 提高编译/构建速度-由于组件会提前编译发布成二进制库进行依赖使用，相比编译全部源代码可以节省大量的编译耗时。同时在日常组件开发时只需要编译
少量依赖组件，相比单一工程可以减少大量的编译耗时和编译错误。

### 组件化概念
组件化开发：就是将一个app分成多个Module，每个Module都是一个组件(也可以是一个基础库供组件依赖)，每一个业务模块彼此之间是没有任何关系的，
彼此的代码和资源都是隔离的，并且不能够相互引用，每一个都是平行关系。 开发的过程中我们可以单独调试部分组件， 组件间不需要互相依赖，
但可以相互调用，最终发布的时候所有组件以lib的形式被主app工程依赖并打包成1个apk。

### 背景
随着项目逐渐扩展，业务功能越来越多，代码量越来越多，开发人员数量也越来越多。此过程中，你是否有过以下烦恼？
1. 项目模块多且复杂，编译一次要5分钟甚至10分钟？太慢不能忍？
2. 改了一行代码或只调了一点UI，就要run整个项目，再忍受一次10分钟？
3. 合代码经常发生冲突？很烦？
4. 被人偷偷改了自己模块的代码？很不爽？
5. 做一个需求，发现还要去改动很多别人模块的代码？
6. 别的模块已实现的类似功能，自己要用只能去复制一份代码再改改？
7. “这个不是我负责的，我不管”，代码责任范围不明确？
8. 只做了一个模块的功能，但改动点很多，所以要完整回归测试？
9. 做了个需求，但不知不觉导致其他模块出现bug？
如果有这些烦恼，说明你的项目需要进行组件化了。

### app组件功能（app空壳工程）：
1. 配置整个项目的Gradle脚本，例如 混淆、签名等；
2. app组件中可以初始化全局的库，例如JPushInterface.init(this)；
3. 集成极光推送的JPushService和JPushReceiver；
4. 业务组件管理（组装）；

### module_main组件功能（业务组件）：
1. 依赖于library_login，主要是登录和注册还有主页功能；

### module_home/module_project/module_square/module_resource/module_mine组件功能（业务组件）：
1. 这几个组件都是业务组件，根据产品的业务逻辑独立成一个组件；

### library_base/library_common/library_network/library_glide/library_room/library_mvp/library_mvvm......组件功能（基础组件）：
1. library_base和library_common组件是基础库，添加一些公用的类；
2. library_network组件是网络请求库、library_glide组件是图片加载库、library_room组件是数据库、library_mvp组件是MVP基础库，
library_mvvm组件是MVVM基础库等等；
3. 定义全局通用的主题（Theme）；

### 组件化Application
1. 如果功能module有Application，主module没有自定义Application，自然引用功能module的Application。如果功能module有两个自定义
Application，会编译出错，需要解决冲突。可以使用tools:replace="android:name"解决，因为App编译最终只会允许声明一个Application。

### 组件化的核心就是解耦，所以组件间是不能有依赖的，那么如何实现组件间的页面跳转呢？
例如 在首页模块 点击 购物车按钮 需要跳转到 购物车模块的购物车页面，两个模块之间没有依赖，也就说不能直接使用显示启动来打开购物车Activity，
那么隐式启动呢？隐式启动是可以实现跳转的，但是隐式Intent需要通过AndroidManifest配置和管理，协作开发显得比较麻烦。 这里我们采用业界通用
的方式—路由。比较著名的路由框架 有阿里的ARouter、美团的WMRouter，它们原理基本是一致的。这里我们采用使用更广泛的ARouter：“一个用于帮助
Android App进行组件化改造的框架——支持模块间的路由、通信、解耦”。
ARouter实现路由跳转集成请查看https://github.com/alibaba/ARouter

## 集成开发模式和组件开发模式转换
1. 首先打开Android项目的gradle.properties文件![Image](/screenshot/screenshot_gradle_properties_configuration.jpg) ，
然后将isModule改为你需要的开发模式（true/false）， 然后点击Sync Project按钮同步项目；
2. ![Image](/screenshot/screenshot_select_module.jpg) 在运行之前，请先按照图中选择一个能够运行的组件；

### app空壳工程下的组件化isModule配置
<table>
    <tr>
        <td><img src="/screenshot/screenshot_app_configuration.jpg"></td>
    </tr>
</table>

### module业务组件下的组件化isModule配置
<table>
    <tr>
        <td><img src="/screenshot/screenshot_module_configuration1.jpg"></td>
        <td><img src="/screenshot/screenshot_module_configuration2.jpg"></td>
    </tr>
</table>




## MVVM图解说明
<table>
    <tr>
        <td><img src="/screenshot/screenshot_mvvm.jpg"></td>
    </tr>
</table>

### MVVM介绍
Model-View-ViewModel，View指绿色的Activity/Fragment，主要负责界面显示，不负责任何业务逻辑和数据处理。 Model指的是Repository
包含的部分，主要负责数据获取，来组本地数据库或者远程服务器。ViewModel 指的是图中蓝色部分，主要负责业务逻辑和数据处理，本身不持有View层
引用，通过LiveData向View层发送数据。Repository统一了数据入口，不管来自数据库，还是服务器，统一打包给ViewModel。

### View
* View层做的就是和UI相关的工作，我们只在XML、Activity和Fragment写View层的代码，View层不做和业务相关的事，也就是我们在Activity不写
业务逻辑和业务数据相关的代码，更新UI通过数据绑定实现，尽量在ViewModel里面做（更新绑定的数据源即可）， Activity要做的事就是初始化一些控件
（如控件的颜色，添加RecyclerView的分割线），View层可以提供更新UI的接口（但是我们更倾向所有的UI元素都是通过数据来驱动更改UI），View层可以
处理事件（但是我们更希望UI事件通过Command来绑定）。简单地说：View层不做任何业务逻辑、不涉及操作数据、不处理数据，UI和数据严格的分开。

### ViewModel
* ViewModel层做的事情刚好和View层相反，ViewModel只做和业务逻辑和业务数据相关的事，不做任何和UI相关的事情，ViewModel层不会持有任何控件
的引用，更不会在ViewModel中通过UI控件的引用去做更新UI的事情。ViewModel就是专注于业务的逻辑处理，做的事情也都只是对数据的操作（这些数据绑定
在相应的控件上会自动去更改UI）。同时DataBinding框架已经支持双向绑定，让我们可以通过双向绑定获取View层反馈给ViewModel层的数据，并对这些
数据上进行操作。关于对UI控件事件的处理，我们也希望能把这些事件处理绑定到控件上，并把这些事件的处理统一化，为此我们通过BindingAdapter对一些
常用的事件做了封装，把一个个事件封装成一个个Command，对于每个事件我们用一个ReplyCommand 去处理就行了，ReplyCommand 会把你可能需要的数据
带给你，这使得我们在ViewModel层处理事件的时候只需要关心处理数据就行了。再强调一遍： ViewModel不做和UI相关的事。

### Model
* Model层最大的特点是被赋予了数据获取的职责，与我们平常Model层只定义实体对象的行为截然不同。实例中，数据的获取、存储、数据状态变化都是Model层
的任务。Model包括实体模型（Bean）、Retrofit的Service ，获取网络数据接口，本地存储（增删改查）接口，数据变化监听等。Model提供数据获取接口
供ViewModel调用，经数据转换和操作并最终映射绑定到View层某个UI元素的属性上。




## 除此以外，还有以下功能：
1. Android与JS交互功能，博客地址https://blog.csdn.net/NakajimaFN/article/details/117927813?spm=1001.2014.3001.5502；  
2. JSBridge框架来实现Android与H5交互，博客地址https://blog.csdn.net/NakajimaFN/article/details/130908360?spm=1001.2014.3001.5502；  
3. Room数据库的使用与升级，博客地址https://blog.csdn.net/NakajimaFN/article/details/130901393?spm=1001.2014.3001.5502；  
4. 数据库加密（以Room为例），博客地址https://blog.csdn.net/NakajimaFN/article/details/130912193?spm=1001.2014.3001.5502；  
5. Android进行NDK开发，实现JNI，以及编写C++与Java交互（Java调用本地函数）并编译出本地so动态库，博客地址https://blog.csdn.net/NakajimaFN/article/details/130894177?spm=1001.2014.3001.5502；  
6. Android项目调用第三方库so动态库，博客地址https://blog.csdn.net/NakajimaFN/article/details/130996742?spm=1001.2014.3001.5502；  
7. Android CoordinatorLayout+AppBarLayout顶部栏吸顶效果的实现，博客地址https://blog.csdn.net/NakajimaFN/article/details/130892776?spm=1001.2014.3001.5502；  
8. Android RxPermissions的使用，博客地址https://blog.csdn.net/NakajimaFN/article/details/124822900?spm=1001.2014.3001.5502；  
9. Android Okhttp3.0及其以上版本忽略https证书，博客地址https://blog.csdn.net/NakajimaFN/article/details/124820012?spm=1001.2014.3001.5502；  
10. Android超清大尺寸图片压缩转Base64中卡顿/速度优化问题整理，博客地址https://blog.csdn.net/NakajimaFN/article/details/123952854?spm=1001.2014.3001.5502；  
11. Android屏幕适配，博客地址https://blog.csdn.net/NakajimaFN/article/details/117660342?spm=1001.2014.3001.5502；  
12. AndroidOkhttp3的使用，博客地址https://blog.csdn.net/NakajimaFN/article/details/109074166?spm=1001.2014.3001.5502；  
13. Android实现沉浸式状态栏，博客地址https://blog.csdn.net/NakajimaFN/article/details/109119759?spm=1001.2014.3001.5502；  
14. double类型精度丢失问题以及解决方法，博客地址https://blog.csdn.net/NakajimaFN/article/details/125912091?spm=1001.2014.3001.5502；  
15. double精确的加减乘除运算，并保留5位小数，博客地址https://blog.csdn.net/NakajimaFN/article/details/126145751；  
16. Android捕获异常，并打印日志文件到本地，博客地址https://blog.csdn.net/NakajimaFN/article/details/125023500?spm=1001.2014.3001.5502；  
17. RSA加密的使用，博客地址https://blog.csdn.net/NakajimaFN/article/details/125913821?spm=1001.2014.3001.5502；  
18. Mac查看Android apk keystore文件签名信息，博客地址https://blog.csdn.net/NakajimaFN/article/details/125937113?csdn_share_tail=%7B%22type%22%3A%22blog%22%2C%22rType%22%3A%22article%22%2C%22rId%22%3A%22125937113%22%2C%22source%22%3A%22NakajimaFN%22%7D；  
19. Android杀掉App所有相关的进程，并退出应用程序（彻底杀死App），博客地址https://blog.csdn.net/NakajimaFN/article/details/124820534?spm=1001.2014.3001.5502；  
20. Android获取手机系统版本号、获取手机型号、获取手机厂商、获取手机IMEI、获取手机CPU_ABI、获取手机唯一识别码，博客地址https://blog.csdn.net/NakajimaFN/article/details/125025659?spm=1001.2014.3001.5502；  

项目地址：https://gitee.com/urasaki/RxJava2AndRetrofit2
我的csdn博客地址https://blog.csdn.net/NakajimaFN?spm=1000.2115.3001.5343 
如有问题，请联系qq1164688204。  
