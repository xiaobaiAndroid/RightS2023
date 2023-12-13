package com.android.launcher.contacts;

import android.content.Context;

import com.android.launcher.base.PresenterBase;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;
import com.bzf.module_db.entity.ContactsTable;
import com.bzf.module_db.repository.ContactsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @description:
 * @createDate: 2023/5/30
 */
public class ContactsPresenter extends PresenterBase<ContactsIView> {

    private static final String TAG = ContactsPresenter.class.getSimpleName();

    public ContactsPresenter(Context context, ContactsIView iView) {
        super(context,iView);
    }

    public void loadData() {
        ObservableOnSubscribe<List<ContactsTable>> observable = emitter -> {
            try {
                String blueDeviceId = SPUtils.getString(mContext, SPUtils.SP_BLUETOOTH_DEVICEID);
                LogUtils.printI(TAG, "blueDeviceId="+blueDeviceId);

                List<ContactsTable> tableList = ContactsRepository.getInstance().getData(mContext, blueDeviceId);
                emitter.onNext(tableList);
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        };
        @NonNull Disposable disposable = Observable.create(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if(mIView != null){
                        mIView.getContactsResult(list);
                    }
                }, throwable -> {
                    if(mIView != null){
                        mIView.getContactsFail();
                    }
                });
        compositeDisposable.add(disposable);
    }

}
