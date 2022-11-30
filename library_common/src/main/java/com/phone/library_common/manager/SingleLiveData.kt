package com.phone.library_common.manager

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.phone.library_common.manager.LogManager.i
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 *
 * 主要是为了解决Fragment初次创建会执行生命周期的onViewCreated方法，会注册一次（在onViewCreated方法进行注册，
 * 注册方法是viewModel!!.getDataxSuccess().observe(this, dataxSuccessObserver!!)，具体看我的项目），跳到
 * 别的Fragment或者Activity，再返回当前Fragment时，又会执行生命周期的onViewCreated方法，然后再注册一次（在
 * onViewCreated方法进行注册，viewModel!!.getDataxSuccess().observe(this, dataxSuccessObserver!!)，
 * 具体看我的项目），所以在这种情况下，普通的MutableLiveData会注册多次后，会回调两次数据（按说会回调多次，但是
 * 实际上只回调两次，具体原因请查看https://blog.csdn.net/vivo_tech/article/details/122553764），我们其实
 * 只需要回调一次数据就可以了，回调多次，会消耗手机性能，用这个SingleLiveData来写，SingleLiveData注册多次，
 * 只会回调一次数据，正好符合我们的需要。
 *
 *
 * 感受：一开始我也没注意到这种情况，还以为MutableLiveData注册多次
 * （注册方法是viewModel!!.getDataxSuccess().observe(this, dataxSuccessObserver!!)，具体看我的项目），
 * 只回调一次，现实是MutableLiveData注册多次，按说会回调多次，但是实际上只回调两次（Android真是坑多，我也是
 * 服了，不知道设计人员为什么这么设计，真无语。。。）
 *
 */
class SingleLiveData<T>() : MutableLiveData<T>() {

    private val TAG = SingleLiveData::class.java.simpleName
    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            i(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

}