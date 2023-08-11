package com.phone.library_network

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


/**
 * com.phone.rxjava2andretrofit2.A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 *
 * 主要是为了解决ViewPager+Fragment实现主页五大模块，当初次创建会执行生命周期的onViewCreated方法，会订阅一次（在onViewCreated方法进行订阅，
 * 订阅方法是viewModel.getDataxSuccess().observe(this, dataxSuccessObserver)，具体看我的项目），跳到
 * 别的Fragment或者Activity，再返回当前Fragment时，又会执行生命周期的onViewCreated方法，然后再订阅一次（在
 * onViewCreated方法进行订阅，viewModel.getDataxSuccess().observe(this, dataxSuccessObserver)，
 * 具体看我的项目），所以在这种情况下，普通的MutableLiveData会订阅多次（按说会回调多次，但是
 * 打印日志只回调两次，具体原因请查看https://blog.csdn.net/vivo_tech/article/details/122553764），我们其实
 * 只需要最新的一次回调数据就可以了，用这个SingleLiveData来写，SingleLiveData订阅多次，
 * 只会回调最新的一次数据，正好符合我们的需要。
 *
 *
 * 感受：一开始我也没注意到这种情况，还以为MutableLiveData订阅多次
 * （订阅方法是viewModel.getDataxSuccess().observe(this, dataxSuccessObserver)，具体看我的项目），
 * 只回调一次，实际是MutableLiveData订阅多次，就会回调多次，查看LiveData源码之后，发现每订阅一次就会记录一次，
 * 还可以绑定生命周期，而且它是粘性的（在页面处于后台的时候订阅，当页面回到前台的时候就会进行数据回调）
 *
 */
class SingleLiveData<T> : MutableLiveData<T>() {

    private val TAG = SingleLiveData::class.java.simpleName
    private val mPendingMap = HashMap<Observer<in T?>, AtomicBoolean>()


    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        val lifecycle = owner.lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }
        mPendingMap[observer] = AtomicBoolean(false)
        lifecycle.addObserver(LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                mPendingMap.remove(observer)
            }
        })
        super.observe(owner, { t ->
            val pending = mPendingMap[observer]
            if (pending != null && pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })


//        super.observe(owner) { t ->
//            val pending = mPendingMap[observer]
//            if (pending != null && pending.compareAndSet(true, false)) {
//                observer.onChanged(t)
//            }
//        }
//        super.observe(owner) {
//            val pending = mPendingMap[observer]
//            if (pending != null && pending.compareAndSet(true, false)) {
//                observer.onChanged(it)
//            }
//        }
    }

    override fun observeForever(observer: Observer<in T?>) {
        mPendingMap[observer] = AtomicBoolean(false)
        super.observeForever(observer)
    }

    override fun removeObserver(observer: Observer<in T?>) {
        mPendingMap.remove(observer)
        super.removeObserver(observer)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        mPendingMap.clear()
        super.removeObservers(owner)
    }

    @MainThread
    override fun setValue(value: T?) {
        for (t in mPendingMap.values) {
            t.set(true)
        }
        super.setValue(value)
    }

    @MainThread
    override fun postValue(value: T?) {
        for (t in mPendingMap.values) {
            t.set(true)
        }
        super.postValue(value)
    }

}