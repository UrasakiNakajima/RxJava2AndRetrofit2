## RxJava2AndRetrofit2
项目功能介绍：原本是RxJava2和Retrofit2项目，现已更新使用Kotlin+RxJava2+Retrofit2+MVP架构+组件化和
Kotlin+Retrofit2+协程+Jetpack MVVM架构+组件化，添加自动管理token功能，添加RxJava2生命周期管理，集成极光推送、阿里云Oss对象存储和高德地图定位功能。

## 应用截图（页面效果一般，不过看这个项目看的不是页面，主要学习的是Kotlin+RxJava2+Retrofit2+MVP架构+组件化
## 和Kotlin+Retrofit2+协程+MVVM架构+组件化的设计）
<table>
    <tr>
        <td><img src="/screenshots/screenshot_launch.jpg" />
        <td><img src="/screenshots/screenshot_login.jpg" />
        <td><img src="/screenshots/screenshot_register.jpg" />
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshots/screenshot_home.jpg" />
        <td><img src="/screenshots/screenshot_project.jpg" />
        <td><img src="/screenshots/screenshot_square.jpg" />
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshots/screenshot_android_and_js.jpg" />
        <td><img src="/screenshots/screenshot_edit_text_input_limits.jpg" />
        <td><img src="/screenshots/screenshot_edit_text_input_limits2.jpg" />
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshots/screenshot_jsbridge.jpg" />
        <td><img src="/screenshots/screenshot_picker_view.jpg" />
        <td><img src="/screenshots/screenshot_kotlin_coroutine.jpg" />
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshots/screenshot_room_insert.jpg" />
        <td><img src="/screenshots/screenshot_room_query.jpg" />
        <td><img src="/screenshots/screenshot_greendao.jpg" />
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshots/screenshot_base64_and_file.jpg" />
        <td><img src="/screenshots/screenshot_base64_and_file2.jpg" />
        <td><img src="/screenshots/screenshot_mounting.jpg" />
    </tr>
</table>
<table>
    <tr>
        <td><img src="/screenshots/screenshot_resource.jpg" />
        <td><img src="/screenshots/screenshot_mine.jpg" />
        <td><img src="/screenshots/screenshot_thread_pool.jpg" />
    </tr>
</table>



## 我们先来看一下组件化架构设计，组件化架构图解说明
<table>
    <tr>
        <td><img src="/screenshots/screenshot_component_description.jpg" />
    </tr>
</table>

### 1. 组件化概念
组件化开发：就是将一个app分成多个Module，每个Module都是一个组件（也可以是一个基础库供组件依赖），每一个业务模块彼此之间是没有任何关系的，
彼此的代码和资源都是隔离的，并且不能够相互引用，每一个都是平行关系。开发的过程中我们可以单独调试部分组件，组件间不需要互相依赖，
但可以相互调用，最终发布的时候所有组件以lib的形式被主app工程依赖并打包成1个apk。

#### 1.1 组件化架构的优势
基于以上这些问题，现在的组件化架构希望可以解决这些问题提升整个交付效率和交付质量。
组件化架构通常具备以下优点：
1. 代码复用-功能封装成组件更容易复用到不同的项目中，直接复用可以提高开发效率。并且每个组件职责单一使用时会带入最小的依赖。
2. 降低理解复杂度-工程拆分为小组件以后，对于组件使用方我们只需要通过组件对外暴露的公开API去使用组件的功能，不需要理解它内部的具体实现。
这样可以帮助我们更容易理解整个大的项目工程。
3. 更好的解耦-在传统单一工程项目中，虽然我们可以使用设计模式或者编码规范来约束模块间的依赖关系，但是由于都存放在单一工程目录中缺少清晰
的模块边界依然无法避免不健康的依赖关系。组件化以后可以明确定义需要对外暴露的能力，对于模块间的依赖关系我们可以进行强约束限制依赖，更好
的做到解耦。对一个模块的添加和移除都会更容易，并且模块间的依赖关系更加清晰。
4. 隔离技术栈-不同的组件可以使用不同的编程语言/技术栈，并且不用担心会影响到其他组件或主工程。例如在不同的组件内可以自由选择使用Kotlin或Swift，
可以使用不同的跨平台框架，只需要通过规范的方式暴露出页面路由或者服务方法即可。
5. 独立开发/维护/发布-大型项目通常有很多团队。在传统单一项目集成打包时可能会遇到代码提交/分支合并的冲突问题。组件化以后每个团队负责自己的组件，
组件可以独立开发/维护/发布提升开发效率。
6. 提高编译/构建速度-由于组件会提前编译发布成二进制库进行依赖使用，相比编译全部源代码可以节省大量的编译耗时。同时在日常组件开发时只需要编译
少量依赖组件，相比单一工程可以减少大量的编译耗时和编译错误。

#### 1.2 背景
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

### 2. 组件化构成：主要由app空壳工程、业务组件和功能组件构成

#### 2.1 app空壳工程：
1. 配置整个项目的Gradle脚本，例如混淆、签名等；
2. app组件的build.gradle中可以配置极光推送JPUSH_PKGNAME和JPUSH_APPKEY，以及它的权限；
3. 业务组件管理（组装）；

#### 2.2 业务组件：Main组件（module_main）：
* 属于业务组件，指定APP启动页面、主界面；

#### 2.3 业务组件: 根据公司具体业务而独立形成一个个的工程（module_home/module_project/module_square/module_resource/module_mine）：
1. 这几个组件都是业务组件，根据产品的业务逻辑独立成一个组件；
2. 每个组件都应该是可独立运行的，没有必要把model封装到基础层。如果需要共用同一个model，那要么划分组件就不合理，要么通过ARouter从别的组件获取fragment的方式解决。
如果实在要把model封装到基础层，方便组件共享数据模型，那么必须新建一个库放在基础层（这里命名为library_login）不允许放library_common里，因为要保证基础层的库都服从单一职责。

#### 2.4 library组件：就是功能组件（library_base/library_common/library_network/library_glide/library_room/library_mvp/library_mvvm......）：
1. 基础层的设定就是为了让组件放心地依赖基础层，放心地复用基础层的代码，以达到高效开发的目的。所以，不要让基础层成为你的代码垃圾桶。对基础层的要求有两个，对内和对外。
对外，命名要秒懂。这样在写组件的业务代码的时候，多想一下基础层里的代码，复用比造轮子更重要。
对内，要分类清晰。还有，封装到基础层的代码，其他组件不一定想用，不是说不写，而是说少写，以及分类清晰地写，所以在封装到基础层之前，先经过code review。
2. library_base和library_common组件是基础库；
3. library_network组件是网络请求库、library_glide组件是图片加载库、library_room组件是数据库、library_mvp组件是MVP基础库，
library_mvvm组件是MVVM基础库等等；

#### 2.5 组件化Application
* 如果功能module有Application，主module没有自定义Application，自然引用功能module的Application。如果功能module有两个自定义Application，
会编译出错，需要解决冲突。可以使用tools:replace="android:name"解决，因为App编译最终只会允许声明一个Application。

### 3. 组件化的核心就是解耦，所以组件间是不能有依赖的，那么如何实现组件间的页面跳转呢？
例如：在Square模块点击thread pool按钮需要跳转到mine模块的ThreadPoolActivity，两个模块之间没有依赖，也就说不能直接使用显示启动来打开
ThreadPoolActivity。 那么隐式启动呢？隐式启动是可以实现跳转的，但是隐式Intent需要通过AndroidManifest配置和管理，协作开发显得比较麻烦。
这里我们采用业界通用的方式—路由，比较著名的路由框架有阿里的ARouter、美团的WMRouter，它们原理基本是一致的。这里我们采用使用更广泛的ARouter：
“一个用于帮助Android App进行组件化改造的框架——支持模块间的路由、通信、解耦”。

#### 3.1 ARouter的使用
1. 添加依赖和配置
```gradle
android {
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [AROUTER_MODULE_NAME: project.getName()]
            }
        }
    }
}

dependencies {
    implementation "com.alibaba:arouter-api:$BuildVersions.arouter_api_version"
    kapt "com.alibaba:arouter-compiler:$BuildVersions.arouter_compiler_version"
}
```

2. 初始化SDK
```kotlin
// 必须写在init之前，否则这些配置在init过程中将无效（推荐在Application中初始化）
if (!BuildConfig.IS_RELEASE) {
    // 打印日志
    ARouter.openLog()
    // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
    ARouter.openDebug()
}
ARouter.init(this)
```

3. 添加注解
```kotlin
// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = ConstantData.Route.ROUTE_PICKER_VIEW)
class PickerViewActivity : BaseBindingRxAppActivity<MineActivityPickerViewBinding>() {
    ...
}
```

4. 发起路由操作
```kotlin
// 1. 应用内简单的跳转
ARouter.getInstance().build(ConstantData.Route.ROUTE_PICKER_VIEW).navigation()
// 2. 跳转并携带参数
ARouter.getInstance().build(ConstantData.Route.ROUTE_THREAD_POOL)
    .withString("title", "線程池")
    .withParcelable("biographyData", BiographyData("book", "Rommel的传记", "Rommel的简介"))
    .navigation()
```

5. 获取携带参数
```kotlin
@Route(path = ConstantData.Route.ROUTE_THREAD_POOL)
class ThreadPoolActivity : BaseRxAppActivity() {

    companion object {
        private val TAG = ThreadPoolActivity::class.java.simpleName
    }

    //为每一个参数声明一个字段，并使用 @Autowired 标注
    @Autowired(name = "title")
    lateinit var mTitle: String
    //为每一个参数声明一个字段，并使用 @Autowired 标注，通过ARouter api可以传递Parcelable对象
    @Autowired(name = "biographyData")
    lateinit var mBiographyData: Parcelable

    //initData函数需要放在Activity onCreate函数内
    override fun initData() {
        // 在对应的Activity注入
        ARouter.getInstance().inject(this)
    }

    //initViews函数需要放在Activity onCreate函数内
    override fun initViews() {
        //在使用的地方去设置参数
        tevTitle?.text = mTitle
        val biographyData = mBiographyData as BiographyData
        LogManager.i(TAG, "biographyData*****" + biographyData.toString())
    }
}
```

6. 添加混淆规则(如果使用了Proguard)
```
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
```

7. 使用 Gradle 插件实现路由表的自动加载 (可选)
```gradle
apply plugin: 'com.alibaba.arouter'

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "com.alibaba:arouter-register:$BuildVersions.arouter_api_version"
    }
}
```
可选使用，通过 ARouter 提供的注册插件进行路由表的自动加载(power by [AutoRegister](https://github.com/luckybilly/AutoRegister))， 默认通过扫描 dex 的方式
进行加载通过 gradle 插件进行自动注册可以缩短初始化时间解决应用加固导致无法直接访问
dex 文件，初始化失败的问题，需要注意的是，该插件必须搭配 api 1.3.0 以上版本使用！

8. 使用IDE插件导航到目标类 (可选)
在Android Studio插件市场中搜索 `ARouter Helper`, 或者直接下载文档上方 `最新版本` 中列出的 `arouter-idea-plugin` zip 安装包手动安装，安装后
插件无任何设置，可以在跳转代码的行首找到一个图标 (![navigation](https://raw.githubusercontent.com/alibaba/ARouter/develop/arouter-idea-plugin/src/main/resources/icon/outline_my_location_black_18dp.png))
点击该图标，即可跳转到标识了代码中路径的目标类.

9. 通过依赖注入解耦:暴露服务
```kotlin
// 声明接口，其他组件通过接口来调用服务
interface IHomeProvider : IProvider {

    var mHomeDataList: MutableList<ResultData.JuheNewsBean>
}

// 实现接口
@Route(path = ConstantData.Route.ROUTE_HOME_SERVICE)
class HomeProviderImpl : IHomeProvider {

    private val TAG = HomeProviderImpl::class.java.simpleName

    override var mHomeDataList: MutableList<ResultData.JuheNewsBean> = mutableListOf()
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun init(context: Context?) {
    }
}
```


10. 使用依赖注入的方式发现服务，通过注解标注字段，即可使用，无需主动获取
```kotlin
val homeService = ARouter.getInstance().build(ConstantData.Route.ROUTE_HOME_SERVICE)
                    .navigation() as IHomeProvider
homeService.mHomeDataList = homeAdapter.mJuheNewsBeanList
```

11. 使用依赖查找的方式发现服务，主动去发现服务并使用
```kotlin
val homeService = ARouter.getInstance().build(ConstantData.Route.ROUTE_HOME_SERVICE)
    .navigation() as IHomeProvider
LogManager.i(TAG, "homeService.mHomeDataList******" + homeService.mHomeDataList.toString())
```


### 4. 集成开发模式和组件开发模式转换
* 首先打开Android项目的gradle.properties文件![Image](/screenshots/screenshot_gradle_properties_configuration.jpg) ，
然后将isModule改为你需要的开发模式（true/false），然后点击Sync Project按钮同步项目；
* ![Image](/screenshots/screenshot_select_module.jpg) 在运行之前，请先按照图中选择一个能够运行的组件；

#### 4.1 app空壳工程下的组件化isModule配置
<table>
    <tr>
        <td><img src="/screenshots/screenshot_app_configuration.jpg" />
    </tr>
</table>

#### 4.2 module业务组件下的组件化isModule配置
<table>
    <tr>
        <td><img src="/screenshots/screenshot_module_configuration1.jpg" />
        <td><img src="/screenshots/screenshot_module_configuration2.jpg" />
    </tr>
</table>

### 5. 资源命名冲突
* 在组件化方案中，资源命名冲突是一个比较严重的问题，由于在打包时会进行res下的资源（包括drawable、layout、mipmap、raw、values下的资源文件。
请注意：values下的资源文件名不要改动，只需要改动文件内部的内容前缀![Image](/screenshots/screenshot_resource_prefix_value.jpg)；
drawable、layout、mipmap、raw下的资源文件都需要加前缀![Image](/screenshots/screenshot_resource_prefix_common.jpg)）合并，
如果两个模块中res下的资源有两个相同名字的文件，那么最后只会保留一份，如果不知道这个问题的小伙伴，在遇到这个问题时肯定是一脸懵逼的状态。问题既然已经出现，
那我们就要去解决，解决办法就是每个组件res下的资源都用固定的命名前缀，这样就不会出现两个相同的文件的现象了，我们可以在build.gradle配置文件中去配置
前缀限定，如果不按该前缀进行命名，AS就会进行红色警告提示，配置如下：
```gradle
android {
    resourcePrefix "前缀_"
}
```


## 再来看一下Jetpack MVVM架构设计，Jetpack MVVM架构图解说明
<table>
    <tr>
        <td><img src="/screenshots/screenshot_mvvm.jpg" />
    </tr>
</table>

### 1. Jetpack MVVM介绍（使用Jetpack组件dataBinding、viewModel、liveData、room、lifecycle；dataBinding进行数据的单向
### 或者双向绑定；viewModel进行业务逻辑和数据处理，绑定view和数据；liveData进行数据的更新；room是数据库，进行数据存储；lifecycle进行生命周期管理。）
* Model-View-ViewModel，View指绿色的Activity/Fragment，主要负责界面显示，不负责任何业务逻辑和数据处理。Model指的是Repository
包含的部分，主要负责数据获取，来组本地数据库或者远程服务器。ViewModel指的是图中蓝色部分，主要负责业务逻辑和数据处理，在里面开启协程，在协程
里进行网络请求/下载文件/操作数据库，它本身不持有View层引用，通过LiveData向View层发送数据。Repository统一了数据入口，不管来自数据库，
还是服务器，统一打包给ViewModel。

#### 1.1 View
* View层做的就是和UI相关的工作，我们只在XML、Activity和Fragment写View层的代码，View层不做和业务相关的事，也就是我们在Activity不写
业务逻辑和业务数据相关的代码，更新UI通过数据绑定实现，尽量在ViewModel里面做（更新绑定的数据源即可），Activity要做的事就是初始化一些控件
（如控件的颜色，添加RecyclerView的分割线），View层可以提供更新UI的接口（但是我们更倾向所有的UI元素都是通过数据来驱动更改UI），View层可以
处理事件（但是我们更希望UI事件通过Command来绑定）。简单地说：View层不做任何业务逻辑、不涉及操作数据、不处理数据，UI和数据严格的分开。

#### 1.2 ViewModel
* ViewModel层做的事情刚好和View层相反，ViewModel只做和业务逻辑和业务数据相关的事，不做任何和UI相关的事情，ViewModel层不会持有任何控件
的引用，更不会在ViewModel中通过UI控件的引用去做更新UI的事情。ViewModel就是专注于业务的逻辑处理，做的事情也都只是对数据的操作（这些数据绑定
在相应的控件上会自动去更改UI）。同时DataBinding框架已经支持双向绑定，让我们可以通过双向绑定获取View层反馈给ViewModel层的数据，并对这些
数据上进行操作。关于对UI控件事件的处理，我们也希望能把这些事件处理绑定到控件上，并把这些事件的处理统一化，为此我们通过BindingAdapter对一些
常用的事件做了封装，把一个个事件封装成一个个Command，对于每个事件我们用一个ReplyCommand去处理就行了，ReplyCommand会把你可能需要的数据
带给你，这使得我们在ViewModel层处理事件的时候只需要关心处理数据就行了。再强调一遍： ViewModel不做和UI相关的事。

#### 1.3 Model
* Model层最大的特点是被赋予了数据获取的职责，与我们平常Model层只定义实体对象的行为截然不同。实例中，数据的获取、存储、数据状态变化都是Model层
的任务。Model包括实体模型（Bean）、Retrofit的Service，获取网络数据接口，本地存储（增删改查）接口，数据变化监听等。Model提供数据获取接口
供ViewModel调用，经数据转换和操作并最终映射绑定到View层某个UI元素的属性上。

### 2. Jetpack MVVM的使用
#### 2.1 启用databinding，添加协程依赖
1. 在主工程app的build.gradle的中加入：
```gradle
android {
    dataBinding {
        enabled true
    }
}

dependencies {
    //kotlin协程导入
    kapt "org.jetbrains.kotlinx:kotlinx-coroutines-android:$BuildVersions.kotlinx_coroutines_android_version"
    kapt "org.jetbrains.kotlinx:kotlinx-coroutines-core:$BuildVersions.kotlinx_coroutines_core_version"
    
    //room数据库导入
    implementation "androidx.room:room-runtime:$BuildVersions.room_version"
    kapt "androidx.room:room-compiler:$BuildVersions.room_version"
    //可选 - Kotlin扩展和协程支持
    implementation "androidx.room:room-ktx:$BuildVersions.room_version"
    
    //Mvvm模式系列组件导入
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$BuildVersions.lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$BuildVersions.lifecycle_version"
}
```

#### 2.2 快速上手，以SquareFragment为例
* 在square_fragment_square.xml中关联SquareViewModelImpl。
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    
    <data>
        <variable
            name="viewModel"
            type="com.phone.module_square.view_model.SquareViewModelImpl" />

        <variable
            name="subDataSquare"
            type="com.phone.library_common.bean.SubDataSquare" />

        <import type="android.view.View" />
    </data>
    
    .....

</layout>
```
> variable - type：类的全路径 <br>variable - name：变量名


#### 2.3 SquareFragment继承BaseMvvmRxFragment，继承基类传入相关泛型，第一个泛型为你创建的SquareViewModelImpl，第二个泛型为ViewDataBind，
#### 保存square_fragment_square.xml后databinding会生成一个SquareFragmentSquareBinding类。（如果没有生成，试着点击Build->Rebuild Project）
**BaseMvvmRxFragment：**
```kotlin
abstract class BaseMvvmRxFragment<VM : BaseViewModel, DB : ViewDataBinding> : RxFragment(),
    IBaseView {

    companion object {
        private val TAG = BaseMvvmRxFragment::class.java.simpleName
    }

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected lateinit var mViewModel: VM
    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected lateinit var mBaseApplication: BaseApplication
    // 是否第一次加载
    protected var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDatabind = DataBindingUtil.inflate(inflater, initLayoutId(), container, false)
        mDatabind.lifecycleOwner = viewLifecycleOwner
        return mDatabind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRxAppCompatActivity = activity as RxAppCompatActivity
        mBaseApplication = mRxAppCompatActivity.application as BaseApplication
        mViewModel = initViewModel()
        initData()
        initObservers()
        initViews()
    }

    protected abstract fun initLayoutId(): Int

    protected abstract fun initViewModel(): VM

    protected abstract fun initData()

    protected abstract fun initObservers()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    override fun onResume() {
        super.onResume()
        initLoadData()
    }

    override fun showLoading() {
        ...
    }

    override fun hideLoading() {
        ...
    }

    override fun onDestroy() {
        mDatabind.unbind()
        viewModelStore.clear()
        super.onDestroy()
    }
}
```

**SquareFragment：**
```kotlin
class SquareFragment : BaseMvvmRxFragment<SquareViewModelImpl, SquareFragmentSquareBinding>() {

    companion object {
        private val TAG: String = SquareFragment::class.java.simpleName
    }

    override fun initLayoutId() = R.layout.square_fragment_square

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(SquareViewModelImpl::class.java)

    override fun initData() {
        ...
    }

    override fun initObservers() {
        ...
    }

    override fun initViews() {
        ...
    }

    override fun initLoadData() {
        if (isFirstLoad) {
            initSquareData("$currentPage")
            isFirstLoad = false
        }
    }

    fun squareDataSuccess(success: List<SubDataSquare>) {
        ...
    }

    fun squareDataError(error: String) {
        ...
    }

    private fun initSquareData(currentPage: String) {
        ...
    }
}
```

#### 2.4 SquareViewModelImpl继承BaseViewModel，在ViewModel中发起请求，所有请求都是在mJob上启动，请求会发生在IO线程，最终回调在主线程上，当页面销毁的时候，请求需要在onCleared方法取消，避免内存泄露的风险
**BaseViewModel：**
```kotlin
open class BaseViewModel : ViewModel() {

    companion object {
        private val TAG: String = BaseViewModel::class.java.simpleName
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> executeRequest(block: suspend () -> ApiResponse<T>): ApiResponse<T> =
        withContext(Dispatchers.IO) {
            var response = ApiResponse<T>()
            runCatching {
                block()
            }.onSuccess {
                response = it
            }.onFailure {
                it.printStackTrace()
                val apiException = getApiException(it)
                response.errorCode = apiException.errorCode
                response.errorMsg = apiException.errorMessage
                response.error = apiException
            }.getOrDefault(response)
        }

    /**
     * 捕获异常信息
     */
    private fun getApiException(e: Throwable): ApiException {
        return when (e) {
            is UnknownHostException -> {
                ApiException("网络异常", -100)
            }

            is JSONException -> {//|| e is JsonParseException
                ApiException("数据异常", -100)
            }

            is SocketTimeoutException -> {
                ApiException("连接超时", -100)
            }

            is ConnectException -> {
                ApiException("连接错误", -100)
            }

            is HttpException -> {
                ApiException("http code ${e.code()}", -100)
            }

            is ApiException -> {
                e
            }
            /**
             * 如果协程还在运行，个别机型退出当前界面时，viewModel会通过抛出CancellationException，
             * 强行结束协程，与java中InterruptException类似，所以不必理会,只需将toast隐藏即可
             */
            is CancellationException -> {
                ApiException("取消请求异常", -10)
            }

            else -> {
                ApiException("未知错误", -100)
            }
        }
    }

    override fun onCleared() {
        LogManager.i(TAG, "onCleared")
        super.onCleared()
    }
}
```

**SquareViewModelImpl：**
```kotlin
class SquareViewModelImpl : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private var mSquareModel = SquareModelImpl()
//    private var mJob: Job? = null

    //1.首先定义一个SingleLiveData的实例
    val liveData = SingleLiveData<State<List<SubDataSquare>>>()

    //2.首先定义一个SingleLiveData的实例
    val downloadData = SingleLiveData<DownloadState<Int>>()

    //3.首先定义一个SingleLiveData的实例
    val insertData = MutableLiveData<State<Book>>()

    //4.首先定义一个SingleLiveData的实例
    val queryData = MutableLiveData<State<List<Book>>>()

    override fun squareData(rxFragment: RxFragment, currentPage: String) {
        LogManager.i(TAG, "squareData thread name*****${Thread.currentThread().name}")

//        mJob?.cancel()
//        //使用GlobalScope 单例对象直接调用launch/async开启协程
//        //在应用范围内启动一个新协程，协程的生命周期与应用程序一致。
//        //由于这样启动的协程存在启动协程的组件已被销毁但协程还存在的情况，极限情况下可能导致资源耗尽，
//        //所以Activity 销毁的时候记得要取消掉，避免内存泄漏
//        //不建议使用，尤其是在客户端这种需要频繁创建销毁组件的场景。
//        //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是子线程）。
//        mJob =
//            GlobalScope.launch(Dispatchers.Main) {
//                val apiResponse = executeRequest { mSquareModel.squareData(currentPage) }
//
//                if (apiResponse.data != null && apiResponse.errorCode == 0) {
//                    val responseData = apiResponse.data?.datas ?: mutableListOf()
//                    if (responseData.size > 0) {
//                        liveData.value = State.SuccessState(responseData)
//                    } else {
//                        liveData.value =
//                            State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
//                    }
//                } else {
//                    liveData.value = State.ErrorState(apiResponse.errorMsg)
//                }
//            }

        viewModelScope.launch {
            val apiResponse = executeRequest { mSquareModel.squareData(currentPage) }

            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                val responseData = apiResponse.data?.datas ?: mutableListOf()
                if (responseData.size > 0) {
                    liveData.value = State.SuccessState(responseData)
                } else {
                    liveData.value =
                        State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
                }
            } else {
                liveData.value = State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun downloadFile(rxFragment: RxFragment) {
        rxFragment.lifecycleScope.launch(Dispatchers.IO) {
            RetrofitManager.instance().downloadFile3(mSquareModel.downloadFile2(),
                BaseApplication.instance().externalCacheDir!!.absolutePath,
                "artist_kirara_asuka.mov",
                object : OnDownloadCallBack {
                    override fun onProgress(progress: Int, total: Long, speed: Long) {
                        LogManager.i(TAG, "progress:$progress, speed:$speed")
                        downloadData.postValue(DownloadState.ProgressState(progress, total, speed))
                    }

                    override fun onCompleted(file: File) {
                        LogManager.i(TAG, "下载文件成功")
                        downloadData.postValue(DownloadState.CompletedState(file))
                    }

                    override fun onError(e: Throwable?) {
                        LogManager.i(TAG, "下载文件异常", e)
                        downloadData.postValue(DownloadState.ErrorState("下载文件异常*****${e.toString()}"))
                    }
                })
        }
    }

    override fun insertBook(rxFragment: RxFragment, success: String) {
        rxFragment.lifecycleScope.launch {
            val book = executeInsertBook {
                val appRoomDataBase = AppRoomDataBase.instance()
                val book = Book()
                val strArr = success.split(".")
                book.bookName = "書名：${strArr[0]}"
                book.anchor = "作者：${strArr[1]}"
                book.briefIntroduction = "簡介：${strArr[2]}"
                appRoomDataBase.bookDao().insert(book)

//                val book2 = Book()
//                book2.bookName = "EnglishXC2"
//                book2.anchor = "rommelXC2"
//                appRoomDataBase.bookDao().insert(book2)
//                val bookList = appRoomDataBase.bookDao().queryAll()
//                for (i in 0..bookList.size - 1) {
//                    LogManager.i(TAG, "book*****" + bookList.get(i).bookName)
//                }
//                AppRoomDataBase.decrypt(
//                    AppRoomDataBase.DATABASE_DECRYPT_NAME,
//                    AppRoomDataBase.DATABASE_ENCRYPT_NAME,
//                    AppRoomDataBase.DATABASE_DECRYPT_KEY
//                )
                book
            }

            if (book.isSuccess) {
                insertData.value = State.SuccessState(book)
            } else {
                insertData.value = State.ErrorState(book.message)
            }
        }
    }

    private suspend fun executeInsertBook(block: suspend () -> Book): Book =
        withContext(Dispatchers.IO) {
            var book = Book()
            runCatching {
                block.invoke()
            }.onSuccess {
                book = it
                book.isSuccess = true
                book.message = "插入數據庫成功"
            }.onFailure {
                it.printStackTrace()
                book.isSuccess = false
                book.message = "插入數據庫失敗"
            }.getOrDefault(book)
        }

    override fun queryBook() {
        viewModelScope.launch {
            val bookList = executeQueryBook {
                val appRoomDataBase = AppRoomDataBase.instance()
                appRoomDataBase.bookDao().queryAll()
            }
            queryData.value = State.SuccessState(bookList)
        }
    }

    private suspend fun executeQueryBook(block: () -> List<Book>): List<Book> =
        withContext(Dispatchers.IO) {
            var bookList = mutableListOf<Book>()
            runCatching {
                block.invoke()
            }.onSuccess {
                bookList = it as MutableList<Book>
            }.onFailure {
                it.printStackTrace()
            }.getOrDefault(bookList)
        }
    
    override fun onCleared() {
//        mJob?.cancel()
        super.onCleared()
    }

}
```

#### 2.5 SquareModelImpl
**SquareViewModelImpl：**
```kotlin
class SquareModelImpl() : ISquareModel {

    override suspend fun squareData(currentPage: String): ApiResponse<DataSquare> {
        return RetrofitManager.instance().mRetrofit
            .create(SquareRequest::class.java)
            .getSquareData(currentPage)
    }
}
```

#### 2.6 封装网络请求（Retrofit+协程）
**SquareRequest：**
```kotlin
interface SquareRequest {

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
    @GET(ConstantUrl.SQUARE_URL)
    suspend fun getSquareData(
        @Path("currentPage") currentPage: String
    ): ApiResponse<DataSquare>
}
```

**RetrofitManager：**
```kotlin
class RetrofitManager private constructor() {

    private val TAG = RetrofitManager::class.java.simpleName

    @JvmField
    val mRetrofit: Retrofit

    /**
     * 私有构造器 无法外部创建
     * 初始化必要对象和参数
     */
    init {
        //缓存
        val cacheFile = File(BaseApplication.instance().externalCacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 10) //10Mb
        val rewriteCacheControlInterceptor = RewriteCacheControlInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor()
        // 包含header、body数据
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //        HeaderInterceptor headerInterceptor = new HeaderInterceptor()

        // 初始化okhttp
        val client = OkHttpClient.Builder()
            .connectTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //连接超时
            .readTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //读取超时
            .writeTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //写入超时
            .cache(cache)
            .addInterceptor(CacheControlInterceptor())
            .addInterceptor(AddAccessTokenInterceptor()) //拦截器用于设置header
            .addInterceptor(ReceivedAccessTokenInterceptor()) //拦截器用于接收并持久化cookie
            .addInterceptor(BaseUrlManagerInterceptor())
            .addInterceptor(rewriteCacheControlInterceptor) //                .addNetworkInterceptor(rewriteCacheControlInterceptor)
            //                .addInterceptor(headerInterceptor)
//            .addInterceptor(loggingInterceptor)
            //                .addInterceptor(new GzipRequestInterceptor()) //开启Gzip压缩
            .sslSocketFactory(SSLSocketManager.sslSocketFactory()) //配置
            .hostnameVerifier(SSLSocketManager.hostnameVerifier()) //配置
            //                .proxy(Proxy.NO_PROXY)
            .build()

        // 初始化Retrofit
        mRetrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(ConstantUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        @Volatile
        private var instance: RetrofitManager? = null
            get() {
                if (field == null) {
                    field = RetrofitManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): RetrofitManager {
            return instance!!
        }

        /**
         * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
         * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
         */
        private const val CACHE_CONTROL_AGE = "max-age=0"

        /**
         * 设缓存有效期为两天
         */
        const val CACHE_STALE_SEC = (60 * 60 * 24 * 2).toLong()

        /**
         * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
         * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
         */
        private const val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"

        fun buildSign(secret: String, time: Long): String {
            //        Map treeMap = new TreeMap(params)// treeMap默认会以key值升序排序
            val stringBuilder = StringBuilder()
            stringBuilder.append(secret)
            stringBuilder.append(time)
            stringBuilder.append("1.1.0")
            stringBuilder.append("861875048330495")
            stringBuilder.append("android")
            Log.d("GlobalConfiguration", "sting:$stringBuilder")
            val md5: MessageDigest
            var bytes: ByteArray? = null
            try {
                md5 = MessageDigest.getInstance("MD5")
                bytes = md5.digest(stringBuilder.toString().toByteArray(charset("utf-8"))) // md5加密
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            // 将MD5输出的二进制结果转换为小写的十六进制
            val sign = StringBuilder()
            bytes?.let {
                for (i in 0 until it.size) {
                    val hex = Integer.toHexString((it[i] and 0xFF.toByte()).toInt())
                    if (hex.length == 1) {
                        sign.append("0")
                    }
                    sign.append(hex)
                }
            }
            Log.d("GlobalConfiguration", "MD5:$sign")
            return sign.toString()
        }

        fun getCacheControl(): String {
            return if (isNetworkAvailable()) CACHE_CONTROL_AGE else CACHE_CONTROL_CACHE
        }

        /**
         * 判断网络是否可用
         *
         * @return
         */
        fun isNetworkAvailable(): Boolean {
            val connectivityManager =
                BaseApplication.instance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //如果仅仅是用来判断网络连接
            //connectivityManager.getActiveNetworkInfo().isAvailable()
            val info = connectivityManager.allNetworkInfo
            //            LogManager.i(TAG, "isNetworkAvailable*****" + info.toString())
            for (i in info.indices) {
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }
    }
}
```

#### 2.6 数据绑定，拥有databinding框架自带的双向绑定，也有扩展
##### 2.6.1 传统绑定
```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:includeFontPadding="false"
    android:text="@={subDataSquare.title}"
    android:textColor="@color/library_color_FFE066FF"
    android:textSize="@dimen/base_sp_18"
    android:visibility="@{subDataSquare.collect? View.GONE:View.VISIBLE}" />

```

##### 2.6.2 自定义ImageView图片加载，url是图片路径，这样绑定后，这个ImageView就会去显示这张图片，不限网络图片还是本地图片。
```xml
<ImageView
    android:layout_width="@dimen/base_dp_95"
    android:layout_height="@dimen/base_dp_95"
    android:layout_marginStart="@dimen/base_dp_16"
    android:layout_gravity="center_horizontal"
    android:scaleType="centerCrop"
    app:imageUrl="@{dataBean.picUrl}"
    tools:ignore="ContentDescription" />
```

* BindingAdapter中的实现
```koltin
object CommonBindingAdapter {

    @JvmStatic
    val TAG = CommonBindingAdapter::class.java.simpleName

    /**
     * 加载图片
     */
    @JvmStatic
    @BindingAdapter("app:imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        ImageLoaderManager.display(imageView.context, imageView, url)
    }
}
```

###### 2.6.3 RecyclerView绑定，在ProjectAdapter中绑定
```xml
<TextView
    android:id="@+id/tev_name_data"
    style="@style/library_text_3"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@={dataBean.date}"
    app:layout_constraintBottom_toBottomOf="@+id/imv_title"
    app:layout_constraintLeft_toLeftOf="@+id/tev_title"
    tools:text="2020-03-14 | zskingking" />
<ImageView
    android:id="@+id/imv_collect"
    android:layout_width="@dimen/base_dp_30"
    android:layout_height="@dimen/base_dp_30"
    android:layout_marginEnd="@dimen/base_dp_16"
    android:paddingStart="@dimen/base_dp_10"
    android:paddingTop="@dimen/base_dp_10"
    android:scaleType="centerCrop"
    app:articleCollect="@{dataBean.collect}"
    app:layout_constraintBottom_toBottomOf="@id/imv_title"
    app:layout_constraintRight_toRightOf="parent"
    tools:ignore="ContentDescription" />
```

```kotlin
class ProjectAdapter(val context: Context, val list: MutableList<ArticleListBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = ProjectAdapter::class.java.simpleName
    }

    fun clearData() {
        notifyItemRangeRemoved(0, this.list.size)
        notifyItemRangeChanged(0, this.list.size)
        this.list.clear()
    }

    fun addData(list: MutableList<ArticleListBean>) {
        notifyItemRangeInserted(this.list.size, list.size)
        this.list.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: CustomViewItemProjectBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_view_item_project,
            parent,
            false
        )
        return ArticlePicViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<View>(R.id.root)?.clickNoRepeat {
            onItemViewClickListener?.onItemClickListener(position, it)
        }
        //收藏
        holder.itemView.findViewById<View>(R.id.ivCollect)?.clickNoRepeat {
            onItemViewClickListener?.onItemClickListener(position, it)
        }

        val binding = DataBindingUtil.getBinding<CustomViewItemProjectBinding>(holder.itemView)?.apply {
            dataBean = list[position]
        }
        binding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * 防止重复点击
     * @param interval 重复间隔
     * @param onClick  事件响应
     */
    var lastTime = 0L
    fun View.clickNoRepeat(interval: Long = 400, onClick: (View) -> Unit) {
        setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (lastTime != 0L && (currentTime - lastTime < interval)) {
                return@setOnClickListener
            }
            lastTime = currentTime
            onClick(it)
        }
    }

    /**
     * 带图片viewHolder
     */
    class ArticlePicViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    
    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }
}
```


## 主要开源框架
* RxJava2
* Rxlifecycle3
* RxPermissions
* Okhttp3
* Retrofit2
* Fastjson
* BASE64Decoder
* ImmersionBar
* GreenDao
* Room
* Glide
* ARouter
* Jsbridge
* SmartRefreshLayout
* LeakCanary
* PictureSelector
* PickerView
* MagicIndicator





## 除此以外，还有以下功能：
1. Android与JS交互功能，博客地址https://blog.csdn.net/NakajimaFN/article/details/117927813?spm=1001.2014.3001.5502；  
2. JSBridge框架来实现Android与H5交互，博客地址https://blog.csdn.net/NakajimaFN/article/details/130908360?spm=1001.2014.3001.5502；  
3. Room数据库的使用与升级，博客地址https://blog.csdn.net/NakajimaFN/article/details/130901393?spm=1001.2014.3001.5502；  
4. 数据库加密（以Room为例），博客地址https://blog.csdn.net/NakajimaFN/article/details/130912193?spm=1001.2014.3001.5502；  
5. Android进行NDK开发，实现JNI，以及编写C++与Java交互（Java调用本地函数）并编译出本地so动态库，博客地址https://blog.csdn.net/NakajimaFN/article/details/130894177?spm=1001.2014.3001.5502；  
6. Android项目调用第三方库so动态库，博客地址https://blog.csdn.net/NakajimaFN/article/details/130996742?spm=1001.2014.3001.5502；  
7. kotlin协程的详细介绍和六种启动方式与挂起函数原理https://blog.csdn.net/NakajimaFN/article/details/131493588?spm=1001.2014.3001.5501
8. Android CoordinatorLayout+AppBarLayout顶部栏吸顶效果的实现，博客地址https://blog.csdn.net/NakajimaFN/article/details/130892776?spm=1001.2014.3001.5502；  
9. Android RxPermissions的使用，博客地址https://blog.csdn.net/NakajimaFN/article/details/124822900?spm=1001.2014.3001.5502；  
10. Android Okhttp3.0及其以上版本忽略https证书，博客地址https://blog.csdn.net/NakajimaFN/article/details/124820012?spm=1001.2014.3001.5502；  
11. Android超清大尺寸图片压缩转Base64中卡顿/速度优化问题整理，博客地址https://blog.csdn.net/NakajimaFN/article/details/123952854?spm=1001.2014.3001.5502；  
12. Android屏幕适配，博客地址https://blog.csdn.net/NakajimaFN/article/details/117660342?spm=1001.2014.3001.5502；  
13. AndroidOkhttp3的使用，博客地址https://blog.csdn.net/NakajimaFN/article/details/109074166?spm=1001.2014.3001.5502；  
14. Android实现沉浸式状态栏，博客地址https://blog.csdn.net/NakajimaFN/article/details/109119759?spm=1001.2014.3001.5502；  
15. double类型精度丢失问题以及解决方法，博客地址https://blog.csdn.net/NakajimaFN/article/details/125912091?spm=1001.2014.3001.5502；  
16. double精确的加减乘除运算，并保留5位小数，博客地址https://blog.csdn.net/NakajimaFN/article/details/126145751；  
17. Android捕获异常，并打印日志文件到本地，博客地址https://blog.csdn.net/NakajimaFN/article/details/125023500?spm=1001.2014.3001.5502；  
18. RSA加密的使用，博客地址https://blog.csdn.net/NakajimaFN/article/details/125913821?spm=1001.2014.3001.5502；  
19. Mac查看Android apk keystore文件签名信息，博客地址https://blog.csdn.net/NakajimaFN/article/details/125937113?csdn_share_tail=%7B%22type%22%3A%22blog%22%2C%22rType%22%3A%22article%22%2C%22rId%22%3A%22125937113%22%2C%22source%22%3A%22NakajimaFN%22%7D；  
20. Android杀掉App所有相关的进程，并退出应用程序（彻底杀死App），博客地址https://blog.csdn.net/NakajimaFN/article/details/124820534?spm=1001.2014.3001.5502；  
21. Android获取手机系统版本号、获取手机型号、获取手机厂商、获取手机IMEI、获取手机CPU_ABI、获取手机唯一识别码，博客地址https://blog.csdn.net/NakajimaFN/article/details/125025659?spm=1001.2014.3001.5502；

## 项目地址：https://gitee.com/urasaki/RxJava2AndRetrofit2
## 我的csdn博客地址https://blog.csdn.net/NakajimaFN?spm=1000.2115.3001.5343 
## 如有问题，请联系qq1164688204。  
